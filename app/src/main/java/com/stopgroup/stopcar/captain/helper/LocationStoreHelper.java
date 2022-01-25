package com.stopgroup.stopcar.captain.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LocationStoreHelper {
    public static String TRIP_KEY = "TRIP_LOCATION";
    public Context context;
    private SharedPreferences file;

    public LocationStoreHelper( Context context,int trip_id ) {
        this.context = context;
        getSharedFile(String.valueOf(trip_id));
    }

    private void getSharedFile(String trip_id) {
        String lastTrip = context.getSharedPreferences("data" , 0).getString("lastTrip","");

        if(!lastTrip.isEmpty() && !lastTrip.equals(trip_id)){
            context.getSharedPreferences("data" , 0).edit().remove("lastTrip").apply();

            context.getSharedPreferences(lastTrip , 0).edit().clear().apply();
        }
        file = context.getSharedPreferences(trip_id , 0);


    }
    private void checkTripCoordinates(int tripID) {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);


        if(!json.equals("") && model.tripID != tripID) {
            SharedPreferences.Editor editor = file.edit();
            editor.putString("json","");
            editor.apply();
        }
    }

    private LocationDegree initPoint(double lat, double lng) {
        LocationDegree location = new LocationDegree(lat, lng);
        location.lat = lat;
        location.lng = lng;

        long tsLong = System.currentTimeMillis()/1000;
        String ts = Long.toString(tsLong);
        try {
            location.instertedTime = Integer.parseInt(ts);
        } catch (Exception e) {
            location.instertedTime = 0;
        }
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        location.date = date;
        location.repeated = 0;
        return location;
    }
    private Location initPointLocation(double lat, double lng) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);
        return location;
    }
    private void initTripCoordinates(LocationDegree point, int tripID) {
        TripCoordinate model = new TripCoordinate();
        model.tripID = tripID;
        model.locations = new ArrayList<LocationDegree>();
        model.locations.add(point);
        model.traveledDistance = 0;
        model.delay = 0;
        Gson gson = new Gson();
        String json = gson.toJson(model);
        if(!json.equals("")) {
            SharedPreferences.Editor editor = file.edit();
            editor.putString("json",json);
            editor.apply();
        }
    }

    public Location getLastPoint() {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);

        if(!model.locations.isEmpty()) {
            LocationDegree lastPoint = model.locations.get(model.locations.size()-1);
            Location location = new Location("");
            location.setLatitude(lastPoint.lat);
            location.setLongitude(lastPoint.lng);
            return location;
        } else {
            return null;
        }
    }
    public void updateLastPoint(LocationDegree point) {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);

        if(!model.locations.isEmpty()) {
            model.locations.remove( model.locations.size() - 1 );
            model.locations.add(point);
        }

    }
    public TripCoordinate getPoints() {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);

        if(model != null) {
            return model;
        } else {
            return null;
        }
    }
    private void appendPoint(LocationDegree point) {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);

        if(model != null && !model.locations.isEmpty() && getLastPoint() != null) {

            Location locationUpdated = initPointLocation(point.lat, point.lng);
            Location lastPoint = getLastPoint();
            if(locationUpdated != null && lastPoint != null) {
                double distance = lastPoint.distanceTo(locationUpdated);
                if (distance >= 50) {
                    model.traveledDistance = updateDistance(distance);
                    model.delay = updateDelay(point);
                    model.locations.add(point);
                } else {
                    LocationDegree location = model.locations.get(model.locations.size() - 1);
                    location.repeated += 1;
                    model.locations.remove( model.locations.size() - 1 );
                    model.locations.add(location);
                }
            }

//            if(getLastPoint().getLatitude() == point.lat && getLastPoint().getLongitude() == point.lng) {
//                return;
//            }
//            model.locations.add(point);
        } else {
            model.locations = new ArrayList<>();
            model.locations.add(point);
        }

        Gson gson = new Gson();
        String jsonNew = gson.toJson(model);
        if(!jsonNew.equals("")) {
            SharedPreferences.Editor editor = file.edit();
            editor.putString("json",jsonNew);
            editor.apply();
        }

    }
    private double updateDistance(double distance) {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);

//        Location locationUpdated = initPointLocation(point.lat, point.lng);
//        Location lastPoint = getLastPoint();
//        if(locationUpdated != null && lastPoint != null) {
//            model.traveledDistance += lastPoint.distanceTo(locationUpdated);
//        }
        assert model != null;
        model.traveledDistance += distance;
        return model.traveledDistance;

//        Gson gson = new Gson();
//        String jsonNew = gson.toJson(model);
//        if(!jsonNew.equals("")) {
//            SharedPreferences.Editor editor = file.edit();
//            editor.putString("json",jsonNew);
//            editor.apply();
//        }
    }
    public double updateDelay(LocationDegree point) {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);

        assert model != null;
        int delayPoints = (point.instertedTime) - (model.locations.get(model.locations.size() - 1).instertedTime);
        if (delayPoints > 9) {
            model.delay += delayPoints;
        }
        return model.delay;

        //        let jsonString = try? JSONEncoder().encode(model)
        //        guard let jsonValue = jsonString else { return }
        //        storedTrip = jsonValue
    }
    public double getDistance() {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);

        if(model == null) {
            return 0;
        }
        double distanceDouble = model.traveledDistance;
        distanceDouble = distanceDouble/1000;
        try {
            String distance = String.format(Locale.ENGLISH,"%.1f", distanceDouble);
            return Double.parseDouble(distance);
        } catch (Exception e) {
            return 0;
        }

    }
    public double getDelayInMinutes() {
        String json = file.getString("json", "");

        Type dataType = new TypeToken<TripCoordinate>() {
        }.getType();
        TripCoordinate model = new Gson().fromJson(json, dataType);

        assert model != null;
        if (model.locations.size() == 1) {
            double repeatedPoint = (model.locations.get(model.locations.size() - 1).repeated);
            model.delay = repeatedPoint * 3.5;
        }
        double delayMinutes = model.delay;
        delayMinutes = delayMinutes / 60;
        String delay = String.format(Locale.ENGLISH,"%.1f", delayMinutes);
        try {
            return Double.parseDouble(delay);
        } catch(Exception e) {
            return 0;
        }

    }
    public void calculateDistance(double lat, double lng, int tripID) {
        checkTripCoordinates(tripID);

        LocationDegree point = initPoint(lat, lng);

        String json = file.getString("json", "");

        if(json != null && !json.isEmpty()) {
            appendPoint(point);
        } else {
            initTripCoordinates(point, tripID);
        }
    }
}