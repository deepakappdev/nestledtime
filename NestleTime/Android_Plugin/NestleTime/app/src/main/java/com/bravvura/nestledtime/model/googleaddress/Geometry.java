package com.bravvura.nestledtime.model.googleaddress;

/**
 * Created by android on 21/12/16.
 */
public class Geometry {
    public void setLocation(GeometryLocation location) {
        this.location = location;
    }

    private GeometryLocation location;

    public GeometryLocation getLocation() {
        return location;
    }
}
