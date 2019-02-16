package fr.free.nrw.commons;

import fr.free.nrw.commons.location.LatLng;
import fr.free.nrw.commons.nearby.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Multiple tests for the function loadAttractionsFromLocation in the class NearbyController.
 * Gets 100% branch coverage of the function.
 */

public class NearbyControllerTest2 {

    @Test
    public void testLoadAttractionsFromLocation1() throws IOException {
        LatLng curLatLng = new LatLng(14.0, 13.0, 0f);
        LatLng latLangToSearchAround = new LatLng(12.0, 12.0, 0f);

        LatLng location = new LatLng(1.4, 1.3, 5f);
        Place place = new Place("myPlace", Label.AIRPORT, "This is a description", location, "cat", null);

        List<Place> places = new ArrayList<>();
        places.add(place);

        NearbyPlaces nearbyPlaces = mock(NearbyPlaces.class);
        NearbyController nearbyController = new NearbyController(nearbyPlaces);

        when(nearbyPlaces.radiusExpander(any(LatLng.class), any(String.class), any(boolean.class))).thenReturn(places);

        nearbyController.loadAttractionsFromLocation(curLatLng, latLangToSearchAround, true, true);
    }

   @Test
   public void testPlacesNull() throws IOException {
       LatLng curLatLng = new LatLng(14.0, 13.0, 0f);
       LatLng latLangToSearchAround = new LatLng(12.0, 12.0, 0f);

       NearbyPlaces nearbyPlaces = mock(NearbyPlaces.class);
       NearbyController nearbyController = new NearbyController(nearbyPlaces);

       when(nearbyPlaces.radiusExpander(any(LatLng.class), any(String.class), any(boolean.class))).thenReturn(null);
       nearbyController.loadAttractionsFromLocation(curLatLng, latLangToSearchAround, true, true);
   }


   @Test
   public void testLatLangToSearchAroundNull() throws IOException {
       LatLng curLatLng = new LatLng(14.0, 13.0, 0f);
       NearbyPlaces nearbyPlaces = mock(NearbyPlaces.class);
       NearbyController nearbyController = new NearbyController(nearbyPlaces);

       nearbyController.loadAttractionsFromLocation(curLatLng, null, true, true);
   }

   @Test
   public void testCurLatLngNull() throws IOException {
       LatLng location = new LatLng(1.4, 1.3, 5f);
       LatLng latLangToSearchAround = new LatLng(12.0, 12.0, 0f);

       Place place = new Place("myPlace", Label.AIRPORT, "This is a description", location, "cat", null);
       List<Place> places = new ArrayList<>();
       places.add(place);

       NearbyPlaces nearbyPlaces = mock(NearbyPlaces.class);
       NearbyController nearbyController = new NearbyController(nearbyPlaces);

       when(nearbyPlaces.radiusExpander(any(LatLng.class), any(String.class), any(boolean.class))).thenReturn(places);

       nearbyController.loadAttractionsFromLocation(null, latLangToSearchAround, false, true);
   }

    @Test
    public void testCurLatLngNull2() throws IOException {
        LatLng location = new LatLng(1.4, 1.3, 5f);
        LatLng latLangToSearchAround = new LatLng(12.0, 12.0, 0f);

        Place place = new Place("myPlace", Label.AIRPORT, "This is a description", location, "cat", null);
        List<Place> places = new ArrayList<>();
        places.add(place);

        NearbyPlaces nearbyPlaces = mock(NearbyPlaces.class);
        NearbyController nearbyController = new NearbyController(nearbyPlaces);

        when(nearbyPlaces.radiusExpander(any(LatLng.class), any(String.class), any(boolean.class))).thenReturn(places);

        nearbyController.loadAttractionsFromLocation(null, latLangToSearchAround, false, false);
    }

    @Test
    public void testPlacesZero() throws IOException {
        LatLng curLatLng = new LatLng(14.0, 13.0, 0f);
        LatLng latLangToSearchAround = new LatLng(12.0, 12.0, 0f);

        List<Place> places = new ArrayList<>();

        NearbyPlaces nearbyPlaces = mock(NearbyPlaces.class);
        NearbyController nearbyController = new NearbyController(nearbyPlaces);

        when(nearbyPlaces.radiusExpander(any(LatLng.class), any(String.class), any(boolean.class))).thenReturn(places);

        nearbyController.loadAttractionsFromLocation(curLatLng, latLangToSearchAround, true, true);
    }

    @Test
    public void testBoundaryLocation() throws IOException {
        LatLng curLatLng = new LatLng(14.0, 13.0, 0f);
        LatLng latLangToSearchAround = new LatLng(12.0, 12.0, 0f);

        LatLng location1 = new LatLng(10.0, 10.0, 5f);
        LatLng location2 = new LatLng(1.0, 1.0, 1f);
        LatLng location3 = new LatLng(20.0, 20.0, 1f);
        Place place1 = new Place("myPlace", Label.AIRPORT, "This is a description", location1, "cat", null);
        Place place2 = new Place("myPlace", Label.AIRPORT, "This is a description", location2, "cat", null);
        Place place3 = new Place("myPlace", Label.AIRPORT, "This is a description", location3, "cat", null);

        List<Place> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
        places.add(place3);

        NearbyPlaces nearbyPlaces = mock(NearbyPlaces.class);
        NearbyController nearbyController = new NearbyController(nearbyPlaces);

        when(nearbyPlaces.radiusExpander(any(LatLng.class), any(String.class), any(boolean.class))).thenReturn(places);

        nearbyController.loadAttractionsFromLocation(curLatLng, latLangToSearchAround, true, true);
    }

}
