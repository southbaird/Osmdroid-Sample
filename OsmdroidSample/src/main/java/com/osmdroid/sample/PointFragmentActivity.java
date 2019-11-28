package com.osmdroid.sample;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.osmdroid.sample.overlay.SampleSimpleFastPointOverlay;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

//绘制5万个点(使用fragment)
public class PointFragmentActivity extends AppCompatActivity  {

    private SampleSimpleFastPointOverlay simpleFastPointOverlay= null;
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

        mapView.setDrawingCacheEnabled(true);
        mapView.setMaxZoomLevel(20);
        mapView.setMinZoomLevel(6);
        mapView.getController().setZoom(12);
        mapView.getController().setCenter(new GeoPoint(23.12645, 113.365575));
        mapView.setUseDataConnection(true);
        mapView.setMultiTouchControls(true);// 触控放大缩小
        //是否显示地图数据源
        mapView.getOverlayManager().getTilesOverlay().setEnabled(false);

        FragmentManager fm = this.getSupportFragmentManager();
        if (fm.findFragmentByTag("SampleGridlines") == null) {
            mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE);
            simpleFastPointOverlay = new SampleSimpleFastPointOverlay();
            fm.beginTransaction().add(R.id.samples_container, simpleFastPointOverlay, "SampleGridlines").commit();
        }
    }

}
