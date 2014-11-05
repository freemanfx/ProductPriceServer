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
        List<Place> places = new ArrayList<Place>();
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();

        PreparedQuery preparedQuery = datastoreService.prepare(new Query(KeyTypes.PLACE));

        for (Entity entity : preparedQuery.asIterable()) {
            places.add(Place.from(entity));
        }

        //GAS STATIONS are also places where they have other products but not the other way around
        preparedQuery = datastoreService.prepare(new Query(KeyTypes.GAS_STATION));
        for (Entity entity : preparedQuery.asIterable()) {
            places.add(Place.from(entity));
        }

        return places;
    }

    @ApiMethod(name = "add", path = "place", httpMethod = ApiMethod.HttpMethod.POST)
    public void addPlace(Place place) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        datastoreService.put(place.toNewEntity());
    }
}
