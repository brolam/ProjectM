package br.com.brolam.projectm.data.models;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by brenomar on 08/08/17.
 */

public class GeoLocation {
    public static final String REFERENCE_NAME = "geoLocation";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";

    public static boolean hasGeoLocation(Map map){
        return (map != null) && map.containsKey(REFERENCE_NAME);
    }

    public static double getLatitude(Map map){
        if (hasGeoLocation(map)){
            return (double)((Map)map.get(REFERENCE_NAME)).get(LATITUDE);
        }
        return -1;
    }

    public static double getLongitude(Map map){
        if (hasGeoLocation(map)){
            return (double)((Map)map.get(REFERENCE_NAME)).get(LONGITUDE);
        }
        return -1;
    }
}
