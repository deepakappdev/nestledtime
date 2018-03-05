package com.bravvura.nestledtime.model.googleaddress;

import java.util.ArrayList;

/**
 * Created by android on 16/12/16.
 */
public class GooglePlaceItem {
    private String formatted_address;
    private ArrayList<String> types = new ArrayList<>();
    private Geometry geometry;
    private ArrayList<AddressComponents> address_components = new ArrayList<>();
    private String place_id;

    public String getFormatted_address() {
        return formatted_address;
    }

    public ArrayList<String> getTypes() {
        return types;
    }
    public ArrayList<String> setTypes(ArrayList<String> types) {
        return this.types = types;
    }


    public String getPlaceId() {
        return place_id;
    }

    public ArrayList<AddressComponents> getAddress_components() {
        return address_components;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setFormatted_address(String address) {
        formatted_address = address;
    }

    public void setGeometry(double lat, double lng) {
        GeometryLocation location = new GeometryLocation();
        location.setLat(lat);
        location.setLng(lng);
        geometry=new Geometry();
        geometry.setLocation(location);
    }
}
