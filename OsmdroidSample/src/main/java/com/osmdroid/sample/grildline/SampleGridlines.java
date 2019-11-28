package com.osmdroid.sample.grildline;

import android.graphics.Color;

import com.osmdroid.sample.util.BaseSampleFragment;

import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay;

/**
 * An example on how to use the lat/lon gridline overlay.
 *
 * basically, listen for map motion/zoom events and remove the old overlay, then add the new one.
 * you can also override the color scheme and font sizes for the labels and lines
 */
public class SampleGridlines extends BaseSampleFragment implements MapListener {

    FolderOverlay activeLatLonGrid;

    @Override
    public String getSampleTitle() {
        return "Lat/Lon Gridlines";
    }

    @Override
    protected void addOverlays() {
        super.addOverlays();
        LatLonGridlineOverlay.setDefaults();
        mMapView.setMaxZoomLevel(20);
        mMapView.setMinZoomLevel(6);
        mMapView.getController().setZoom(12);
        mMapView.getController().setCenter(new GeoPoint(23.12645, 113.365575));
        mMapView.setTilesScaledToDpi(true);
        mMapView.setMapListener(this);
        LatLonGridlineOverlay.fontSizeDp=16;
        LatLonGridlineOverlay.fontColor= Color.argb(255,0,255,0);
        LatLonGridlineOverlay.backgroundColor=Color.BLACK;
        LatLonGridlineOverlay.lineColor=LatLonGridlineOverlay.fontColor;
        updateGridlines();
    }

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
        activeLatLonGrid = LatLonGridlineOverlay.getLatLonGrid(getActivity(), mMapView);
        mMapView.getOverlays().add(activeLatLonGrid);

    }

    @Override
    public void onDestroyView(){

        if (activeLatLonGrid!=null)
            activeLatLonGrid.onDetach(mMapView);
        activeLatLonGrid=null;
        super.onDestroyView();
    }

}
