package com.bravvura.nestledtime.model.googleaddress;

import java.util.ArrayList;

/**
 * Created by android on 21/12/16.
 */
public class AddressComponents {

    private String long_name;
    private String short_name;
    private ArrayList<String> types;

    public String getLong_name() {
        return long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public ArrayList<String> getTypes() {
        return types;
    }
}
