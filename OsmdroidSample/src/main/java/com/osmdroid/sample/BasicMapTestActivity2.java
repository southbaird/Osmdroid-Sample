package com.osmdroid.sample;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.osmdroid.sample.overlay.CustomPointOverlay;
import com.osmdroid.sample.util.SomeDataMapManger;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class BasicMapTestActivity2 extends AppCompatActivity implements View.OnClickListener {

    private MapView mapView;
    //地图旋转
    private RotationGestureOverlay mRotationGestureOverlay;
    //比例尺
    private ScaleBarOverlay mScaleBarOverlay;
    //指南针方向
    private CompassOverlay mCompassOverlay = null;
    //设置导航图标的位置
    private MyLocationNewOverlay mLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_test_2);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        initView();
    }

    private void initView() {

        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);

        mapView = (MapView) findViewById(R.id.mymapview);
        mapView.setDrawingCacheEnabled(true);
        mapView.setMaxZoomLevel(20);
        mapView.setMinZoomLevel(6);
        mapView.getController().setZoom(12);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setUseDataConnection(true);
        mapView.setMultiTouchControls(true);// 触控放大缩小
        //是否显示地图数据源
        mapView.getOverlayManager().getTilesOverlay().setEnabled(true);


        //地图自由旋转
        mRotationGestureOverlay = new RotationGestureOverlay(mapView);
        mRotationGestureOverlay.setEnabled(true);
        mapView.getOverlays().add(this.mRotationGestureOverlay);

        //比例尺配置
        final DisplayMetrics dm = getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(mapView);
        mScaleBarOverlay.setCentred(true);
        mScaleBarOverlay.setAlignBottom(true); //底部显示
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 5, 80);
        mapView.getOverlays().add(this.mScaleBarOverlay);

        //指南针方向
        mCompassOverlay = new CompassOverlay(this, new InternalCompassOrientationProvider(this),
                mapView);
        mCompassOverlay.enableCompass();
        mapView.getOverlays().add(this.mCompassOverlay);

        //设置导航图标
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this),
                mapView);
        mapView.getOverlays().add(this.mLocationOverlay);
        mLocationOverlay.enableMyLocation();  //设置可视
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                //定位当前的位置，并设置缩放级别
                mapView.getController().setZoom(18);
                mapView.getController().setCenter(new GeoPoint(23.12648183, 113.365548756));
                break;
            case R.id.button2: //绘制点
                // add overlay
                mapView.getOverlays().add(CustomPointOverlay.GetInstance());
                break;
            case R.id.button3://屏幕点击的坐标
                //这个功能的使用，需要打开gps，就是说gps有数据
                MyLocationOverlayWithClick overlay = new MyLocationOverlayWithClick(mapView);
                overlay.enableFollowLocation();
                overlay.enableMyLocation();
                mapView.getOverlayManager().add(overlay);
                break;
            case R.id.button4:
                SomeDataMapManger.initGeoJsonData(mapView,this);
                mapView.getController().setZoom(12);
                mapView.getController().setCenter(new GeoPoint(23.12648183, 113.365548756));
                break;

        }
    }

    public static class MyLocationOverlayWithClick extends MyLocationNewOverlay{

        public MyLocationOverlayWithClick(MapView mapView) {
            super(mapView);
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e, MapView map) {
            if (getLastFix() != null)
                Toast.makeText(map.getContext(), "Tap! I am at " + getLastFix().getLatitude() + "," + getLastFix().getLongitude(), Toast.LENGTH_LONG).show();
            return true;

        }
    }

    @Override
    public void onPause() {
        this.mLocationOverlay.disableMyLocation();
        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
