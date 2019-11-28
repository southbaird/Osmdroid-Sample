package com.osmdroid.sample;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.osmdroid.sample.util.SomeDataMapManger;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

//有离线格式的数据就加载，没有使用在线地图
public class OffLineMapActivity extends AppCompatActivity implements View.OnClickListener {
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
        SomeDataMapManger.initmapViewData(mMapView,OffLineMapActivity.this);
        mMapView.setMaxZoomLevel(20);
        mMapView.setMinZoomLevel(2);
        mMapView.getController().setZoom(10);
        mMapView.setTilesScaledToDpi(true);
        mMapView.getController().setCenter(new GeoPoint(23.12685, 113.367575));
        mMapView.setUseDataConnection(true);
        mMapView.setMultiTouchControls(true);// 触控放大缩小
        mMapView.getOverlayManager().getTilesOverlay().setEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:


                break;
        }
    }
}
