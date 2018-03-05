package com.bravvura.nestledtime.model.googleaddress;

/**
 * Created by android on 21/12/16.
 */
public class GeometryLocation {
    private double lat;

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
