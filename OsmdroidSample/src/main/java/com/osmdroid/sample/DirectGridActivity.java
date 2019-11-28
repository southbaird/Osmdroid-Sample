package com.osmdroid.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay;

//不带fragment的格网
public class DirectGridActivity extends AppCompatActivity implements View.OnClickListener {
    FolderOverlay activeLatLonGrid;
    private MapView mMapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        initView();
    }

    private void initView() {
        mMapView = (MapView)findViewById(R.id.mymapview);

        LatLonGridlineOverlay.setDefaults();
        mMapView.setMaxZoomLevel(20);
        mMapView.setMinZoomLevel(6);
        mMapView.getController().setZoom(12);
        mMapView.setTilesScaledToDpi(true);
        mMapView.getController().setCenter(new GeoPoint(23.12645, 113.365575));
        mMapView.setUseDataConnection(true);
        mMapView.setMultiTouchControls(true);// 触控放大缩小
        mMapView.getOverlayManager().getTilesOverlay().setEnabled(true);
//        mMapView.setMapListener(mapListener);
        LatLonGridlineOverlay.fontSizeDp=16;
        LatLonGridlineOverlay.fontColor= Color.argb(255,0,255,0);
        LatLonGridlineOverlay.backgroundColor=Color.BLACK;
        LatLonGridlineOverlay.lineColor=LatLonGridlineOverlay.fontColor;
        updateGridlines();

    }

    MapListener mapListener = new MapListener() {
        @Override
        public boolean onScroll(ScrollEvent scrollEvent) {
            updateGridlines();
            return false;
        }

        @Override
        public boolean onZoom(ZoomEvent zoomEvent) {
            updateGridlines();
            return false;
        }
    };

    protected void updateGridlines(){

        if (mMapView==null)
            return; //happens during unit tests with rapid recycling of the fragment
        if (activeLatLonGrid != null) {
            mMapView.getOverlayManager().remove(activeLatLonGrid);
            activeLatLonGrid.onDetach(mMapView);
        }
        LatLonGridlineOverlay.backgroundColor= Color.BLACK;
        LatLonGridlineOverlay.fontColor= Color.BLUE;
        LatLonGridlineOverlay.lineColor= Color.BLUE;
        activeLatLonGrid = LatLonGridlineOverlay.getLatLonGrid(this, mMapView);
        mMapView.getOverlays().add(activeLatLonGrid);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:


                break;
        }
    }
}
