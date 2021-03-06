package ro.freemanfx.productpriceserver;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.*;
import ro.freemanfx.productpriceserver.domain.Place;
import ro.freemanfx.productpriceserver.domain.fuel.FuelPrice;

import java.util.LinkedList;
import java.util.List;

import static com.google.api.server.spi.config.ApiMethod.HttpMethod.GET;
import static com.google.api.server.spi.config.ApiMethod.HttpMethod.POST;
import static com.google.appengine.api.datastore.DatastoreServiceFactory.getDatastoreService;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

@Api(name = "fuelprice", version = "v1", description = "Prices for fuels")
public class FuelPriceEndpoint {
    public static final FetchOptions FETCH_OPTIONS = withLimit(100);

    @ApiMethod(name = "add", path = "fuelprice/add", httpMethod = POST)
    public void add(FuelPrice fuelPrice) {
        DatastoreService ds = getDatastoreService();
        ds.put(fuelPrice.getPlace().toNewGasStationEntity());
        ds.put(fuelPrice.toNewEntity());
    }

    @ApiMethod(name = "findPrices", path = "fuelprice/find", httpMethod = GET)
    public List<FuelPrice> findPricesFor(@Named("fuelKey") String fuelKey) {
        List<FuelPrice> fuelPrices = new LinkedList<>();
        DatastoreService ds = getDatastoreService();

        Query query = new Query(fuelKey).addSort(KeyTypes.PRICE, Query.SortDirection.ASCENDING);
        PreparedQuery preparedQuery = ds.prepare(query);
        List<Entity> entities = preparedQuery.asList(FETCH_OPTIONS);

        for (Entity entity : entities) {
            FuelPrice fuelPrice = new FuelPrice(fuelKey, Place.find((String) entity.getProperty(KeyTypes.GAS_STATION), KeyTypes.GAS_STATION), (Double) entity.getProperty(KeyTypes.PRICE));
            fuelPrices.add(fuelPrice);
        }

        return fuelPrices;
    }

    @ApiMethod(name = "allGasStations", path = "gasStations/all", httpMethod = GET)
    public List<Place> getGasStations() {
        List<Place> gasStations = new LinkedList<>();
        DatastoreService ds = getDatastoreService();

        Query query = new Query(KeyTypes.GAS_STATION);
        PreparedQuery preparedQuery = ds.prepare(query);

        for (Entity entity : preparedQuery.asIterable()) {
            gasStations.add(Place.from(entity));
        }

        return gasStations;
    }
}
