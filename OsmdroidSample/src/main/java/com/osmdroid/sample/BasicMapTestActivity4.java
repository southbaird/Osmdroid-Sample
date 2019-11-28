package com.osmdroid.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.DirectedLocationOverlay;

public class BasicMapTestActivity4 extends AppCompatActivity implements LocationListener {

    private MapView mMapView;
    private LocationManager mLocationManager;
    private ScaleBarOverlay mScaleBarOverlay;
    private CompassOverlay compassOverlay;
    private DirectedLocationOverlay myLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_test_4);
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        initLocat();
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(this);
    }

    @SuppressLint("MissingPermission")
    private void initLocat() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.mymapview);
        mMapView.setMaxZoomLevel(20);
        mMapView.setMinZoomLevel(6);
        mMapView.getController().setZoom(12);
        mMapView.setTilesScaledToDpi(true);
        mMapView.getController().setCenter(new GeoPoint(23.12645, 113.365575));
        mMapView.setUseDataConnection(true);
        mMapView.setMultiTouchControls(true);// 触控放大缩小
        mMapView.getOverlayManager().getTilesOverlay().setEnabled(true);

        mScaleBarOverlay = new ScaleBarOverlay(mMapView);
        IMapController mController = mMapView.getController();
        mController.setZoom(18);//地图显示级别
        // 设置显示指南针
        compassOverlay = new CompassOverlay(this,
                new InternalCompassOrientationProvider(this), mMapView);
        // 显示图层
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mMapView.setFlingEnabled(true);
        mMapView.setTilesScaledToDpi(true);

        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        myLocationOverlay = new DirectedLocationOverlay
                (this);
        myLocationOverlay.setDirectionArrow(bMap);
        myLocationOverlay.setEnabled(true);

        // 地图移动到某个点
//        mMapView.getController().animateTo(geoPoint);
        // 添加到图层中
        mMapView.getOverlays().add(mScaleBarOverlay);
        mMapView.getOverlays().add(myLocationOverlay);
    }

    /**
     * 定位成功调用
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {

    }

    /**
     * 定位状态改变时调用
     *
     * @param provider
     * @param status
     * @param extras
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * 定位可用时调用
     *
     * @param provider
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * 定位关闭时调用
     *
     * @param provider
     */
    @Override
    public void onProviderDisabled(String provider) {

    }
}
