package com.osmdroid.sample.util;

import android.app.Activity;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.Ring;
import com.osmdroid.sample.file.FilePathManage;
import com.osmdroid.sample.mapsforge.MapsForgeTileProvider;
import com.osmdroid.sample.mapsforge.MapsForgeTileSource;

import org.json.JSONException;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.rendertheme.AssetsRenderTheme;
import org.mapsforge.map.rendertheme.XmlRenderTheme;
import org.osmdroid.tileprovider.IRegisterReceiver;
import org.osmdroid.tileprovider.modules.ArchiveFileFactory;
import org.osmdroid.tileprovider.modules.IArchiveFile;
import org.osmdroid.tileprovider.modules.OfflineTileProvider;
import org.osmdroid.tileprovider.tilesource.FileBasedTileSource;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 支持加载离线地图（格式包括zip，sqlite，mbtiles，gemf，map五种格式）
 * 离线地图放在/com_map/map目录下
 */
public class SomeDataMapManger {

    //json数据的解析并绘制
    public static void initGeoJsonData(MapView mapView, Activity context) {
        try {
            InputStream input = context.getResources().getAssets().open("test.json");
            String lineData = getStringFromInputStream(input);
            FeatureCollection features = (FeatureCollection) GeoJSON.parse(lineData);

            for (Feature f : features.getFeatures()) {
                if (f.getGeometry().getType().equalsIgnoreCase("LineString")) {
                    //线
                    List<GeoPoint> list = new ArrayList<>();
                    com.cocoahero.android.geojson.LineString lineString = (com.cocoahero.android.geojson.LineString) f.getGeometry();
                    for (com.cocoahero.android.geojson.Position p : lineString.getPositions()) {
                        list.add(new org.osmdroid.util.GeoPoint(p.getLatitude(), p.getLongitude(), p.getAltitude()));
                    }
                    if (list.size() > 0) {
                        org.osmdroid.views.overlay.Polyline Polyline = new org.osmdroid.views.overlay.Polyline();
                        Polyline.setWidth(2);
                        Polyline.setColor(0xFF1B7BCD);
                        Polyline.setPoints(list);
                        mapView.getOverlays().add(Polyline);
                    }

                } else if (f.getGeometry().getType().equalsIgnoreCase("Polygon")) {
                    //面
                    List<org.osmdroid.util.GeoPoint> list = new ArrayList<>();
                    com.cocoahero.android.geojson.Polygon geometry = (com.cocoahero.android.geojson.Polygon) f.getGeometry();
                    for (Ring ring : geometry.getRings()) {
                        for (com.cocoahero.android.geojson.Position p : ring.getPositions()) {
                            list.add(new org.osmdroid.util.GeoPoint(p.getLatitude(), p.getLongitude(), p.getAltitude()));
                        }
                    }
                    if (list.size() > 0) {
                        org.osmdroid.views.overlay.Polygon polygon = new org.osmdroid.views.overlay.Polygon();
                        polygon.setStrokeWidth(1);
                        polygon.setFillColor(0x8032B5EB);
                        polygon.setPoints(list);
                        mapView.getOverlays().add(polygon);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getStringFromInputStream(InputStream inputStream) {
        BufferedReader bufferedReader=null;
        StringBuilder stringBuilder=new StringBuilder();
        String line;
        try {
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void initmapViewData(MapView mapView, Activity context){
        String strFilepath = FilePathManage.GetInstance().getMapDirectory() + "/" + "China.map";
        File exitFile = new File(strFilepath);
        String fileName = "China.map";
        if (!exitFile.exists() && !fileName.contains(".")) {
            mapView.setTileSource(TileSourceFactory.MAPNIK);
        }else {
            //加载map地图格式
            if(fileName.contains(".map")){
                File[] mapfile = new File[1];
                mapfile[0] = exitFile;
                XmlRenderTheme theme = null;
                try {
                    theme = new AssetsRenderTheme(context, "renderthemes/","rendertheme-v4.xml");
                    AndroidGraphicFactory.createInstance(context.getApplication());
                }catch (Exception ex){
                    ex.printStackTrace();
                    return;
                }
                MapsForgeTileProvider forge = new MapsForgeTileProvider(new SimpleRegisterReceiver(context),
                        MapsForgeTileSource.createFromFiles(mapfile,theme, "rendertheme-v4"),null);
                mapView.setTileProvider(forge);
                return;
            }else if(fileName.contains(".gpkg")){


                return;
            }
            fileName = fileName.substring(fileName.lastIndexOf(".") + 1);
            if (fileName.length() == 0)
                return;
            /**
             *
             *  extensionMap.put("zip", ZipFileArchive.class);
             if(VERSION.SDK_INT >= 10) {
             extensionMap.put("sqlite", DatabaseFileArchive.class);
             extensionMap.put("mbtiles", MBTilesFileArchive.class);
             extensionMap.put("gemf", GEMFFileArchive.class);
             }
             这里加载上面四种地图格式
             */
            if (ArchiveFileFactory.isFileExtensionRegistered(fileName)) {
                try {
                    OfflineTileProvider tileProvider = new OfflineTileProvider((IRegisterReceiver) new SimpleRegisterReceiver(context),
                            new File[] { exitFile });
                    mapView.setTileProvider(tileProvider);

                    String source = "";
                    IArchiveFile[] archives = tileProvider.getArchives();
                    if (archives.length > 0) {
                        Set<String> tileSources = archives[0].getTileSources();
                        if (!tileSources.isEmpty()) {
                            source = tileSources.iterator().next();
                            mapView.setTileSource(FileBasedTileSource.getSource(source));
                        } else {
                            mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
                        }

                    } else
                        mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
