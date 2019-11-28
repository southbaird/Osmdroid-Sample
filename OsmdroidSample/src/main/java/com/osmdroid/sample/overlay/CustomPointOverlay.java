package com.osmdroid.sample.overlay;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Overlay;

/**
 * 自定义绘制的地图坐标
 */
public class CustomPointOverlay extends Overlay {

    private final Point mMapCoordsProjected = new Point();
    private final Point mMapCoordsTranslated = new Point();
    protected Paint mCirclePaint = new Paint();

    public CustomPointOverlay() {
    }

    static CustomPointOverlay mGisOverlay = null;

    public static CustomPointOverlay GetInstance() {
        if (mGisOverlay == null) {
            synchronized (CustomPointOverlay.class) {
                if (mGisOverlay == null) {
                    mGisOverlay = new CustomPointOverlay();
                }
            }
        }
        return mGisOverlay;
    }

    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {

        //经纬度坐标到屏幕坐标的转换
        mapView.getProjection().toProjectedPixels(23.12658183, 113.365588756, mMapCoordsProjected);
        Projection pj = mapView.getProjection();
        pj.toPixelsFromProjected(mMapCoordsProjected, mMapCoordsTranslated);

//            final float radius = lastFix.getAccuracy()
//                    / (float) TileSystem.GroundResolution(lastFix.getLatitude(),
//                    mapView.getZoomLevel());
        final float radius = 10L;
        mCirclePaint.setColor(Color.BLUE);
        mCirclePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mMapCoordsTranslated.x, mMapCoordsTranslated.y, radius, mCirclePaint);
    }
}
