package com.bravvura.nestledtime.model.googleaddress;

import java.util.ArrayList;

/**
 * Created by Deepak Saini on 1/11/2016.
 */
public class PlacePredictions {
    private ArrayList<GooglePlaceItem> results = new ArrayList<>();
    public ArrayList<GooglePlaceItem> getResults() {
        ArrayList<GooglePlaceItem> uniqueResult = new ArrayList<>();
        for(GooglePlaceItem googlePlace:results) {
            boolean found = false;
            for(GooglePlaceItem storedPlace:uniqueResult) {
                if(storedPlace.getFormatted_address().equalsIgnoreCase(googlePlace.getFormatted_address())) {
                    found = true;
                    break;
                }
            }
            if(!found)
                uniqueResult.add(googlePlace);
        }
        return uniqueResult;
    }
}
