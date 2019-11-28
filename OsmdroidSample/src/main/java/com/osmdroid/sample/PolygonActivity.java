package com.osmdroid.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

//绘制面
public class PolygonActivity extends AppCompatActivity  {

    private MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        initView();
    }

    private void initView() {
        mapView = (MapView)findViewById(R.id.mymapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setDrawingCacheEnabled(true);
        mapView.setMaxZoomLevel(20);
        mapView.setMinZoomLevel(6);
        mapView.getController().setZoom(12);
        mapView.setUseDataConnection(true);
        mapView.setMultiTouchControls(true);// 触控放大缩小
        //是否显示地图数据源
        mapView.getOverlayManager().getTilesOverlay().setEnabled(false);

        // create 10k labelled points
        // in most cases, there will be no problems of displaying >100k points, feel free to try
        List<GeoPoint> points = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            points.add(new GeoPoint(37 + Math.random() * 5, -8 + Math.random() * 5, 0));
        }

        //随机产生的1000个点并连成面
        if(points.size()>0){
            org.osmdroid.views.overlay.Polygon polygon=new org.osmdroid.views.overlay.Polygon();
            polygon.setStrokeWidth(1);
            polygon.setFillColor(0x8032B5EB);
            polygon.setStrokeColor(Color.BLUE);
            polygon.setPoints(points);
            mapView.getOverlays().add(polygon);
        }

        //计算边界值，定位边界
       final BoundingBox box = new BoundingBox(37,-8,42,-3);
        // zoom to its bounding box
        mapView.addOnFirstLayoutListener(new MapView.OnFirstLayoutListener() {

            @Override
            public void onFirstLayout(View v, int left, int top, int right, int bottom) {
                if(mapView != null && mapView.getController() != null) {
                    mapView.getController().zoomTo(6);
                    mapView.zoomToBoundingBox(box, true);
                }

            }
        });

    }

}
