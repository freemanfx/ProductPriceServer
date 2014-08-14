package ro.freemanfx.productpriceserver.domain;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.datastore.*;
import ro.freemanfx.productpriceserver.KeyTypes;

public class Place {
    public static final String NAME = "NAME";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    private String name;
    private double latitude;
    private double longitude;

    public Place() {
    }

    public Place(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Place find(String placeKey, String entityType) {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Key key = KeyFactory.createKey(entityType, placeKey);
        Query query = new Query(entityType, key);
        Entity entity = ds.prepare(query).asSingleEntity();

        String name = (String) entity.getProperty(Place.NAME);
        Double latitude = (Double) entity.getProperty(Place.LATITUDE);
        Double longitude = (Double) entity.getProperty(Place.LONGITUDE);
        return new Place(name, latitude, longitude);
    }

    public static Place from(Entity entity) {
        return new Place((String) entity.getProperty(NAME), (Double) entity.getProperty(LATITUDE), (Double) entity.getProperty(LONGITUDE));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Entity toNewEntity() {
        Key key = KeyFactory.createKey(KeyTypes.PLACE, getKey());
        Entity entity = new Entity(key);
        entity.setProperty(NAME, name);
        entity.setProperty(LATITUDE, latitude);
        entity.setProperty(LONGITUDE, longitude);
        return entity;
    }

    public Entity toNewGasStationEntity() {
        Key key = KeyFactory.createKey(KeyTypes.GAS_STATION, getKey());
        Entity entity = new Entity(key);
        entity.setProperty(NAME, name);
        entity.setProperty(LATITUDE, latitude);
        entity.setProperty(LONGITUDE, longitude);
        return entity;
    }

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public String getKey() {
        return name + latitude + longitude;
    }
}
