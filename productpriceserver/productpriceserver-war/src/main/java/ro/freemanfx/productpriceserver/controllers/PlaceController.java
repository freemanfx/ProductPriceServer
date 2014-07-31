package ro.freemanfx.productpriceserver.controllers;

import com.google.appengine.api.datastore.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ro.freemanfx.productpriceserver.KeyTypes;
import ro.freemanfx.productpriceserver.domain.Place;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/place")
public class PlaceController {

    @RequestMapping(value = "all", method = RequestMethod.GET)
    @ResponseBody
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

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public void addPlace(@RequestBody Place place) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        datastoreService.put(place.toNewEntity());
    }
}
