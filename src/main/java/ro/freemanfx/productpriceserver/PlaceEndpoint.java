package ro.freemanfx.productpriceserver;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.*;
import ro.freemanfx.productpriceserver.domain.Place;

import java.util.ArrayList;
import java.util.List;

@Api(name = "place", version = "v1", description = "API for places")
public class PlaceEndpoint {

    @ApiMethod(name = "all", path = "place", httpMethod = ApiMethod.HttpMethod.GET)
    public List<Place> getAll() {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery preparedQuery = datastoreService.prepare(new Query(KeyTypes.PLACE));
        List<Place> places = new ArrayList<Place>();

        for (Entity entity : preparedQuery.asIterable()) {
            String name = (String) entity.getProperty(Place.NAME);
            Double latitude = (Double) entity.getProperty(Place.LATITUDE);
            Double longitude = (Double) entity.getProperty(Place.LONGITUDE);
            places.add(new Place(name, latitude, longitude));
        }
        return places;
    }

    @ApiMethod(name = "add", path = "place", httpMethod = ApiMethod.HttpMethod.POST)
    public void addPlace(Place place) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        datastoreService.put(place.toNewEntity());
    }
}
