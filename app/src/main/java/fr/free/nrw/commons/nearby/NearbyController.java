package fr.free.nrw.commons.nearby;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.graphics.drawable.VectorDrawableCompat;

import com.mapbox.mapboxsdk.annotations.IconFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import fr.free.nrw.commons.R;
import fr.free.nrw.commons.location.LatLng;
import fr.free.nrw.commons.utils.UiUtils;
import timber.log.Timber;

import static fr.free.nrw.commons.utils.LengthUtils.computeDistanceBetween;
import static fr.free.nrw.commons.utils.LengthUtils.formatDistanceBetween;

public class NearbyController {
    private static final int MAX_RESULTS = 1000;
    private final NearbyPlaces nearbyPlaces;
    public static double searchedRadius = 10.0; //in kilometers
    public static LatLng currentLocation;

    @Inject
    public NearbyController(NearbyPlaces nearbyPlaces) {
        this.nearbyPlaces = nearbyPlaces;
    }


    /**
     * Prepares Place list to make their distance information update later.
     *
     * @param curLatLng current location for user
     * @param latLangToSearchAround the location user wants to search around
     * @param returnClosestResult if this search is done to find closest result or all results
     * @return NearbyPlacesInfo a variable holds Place list without distance information
     * and boundary coordinates of current Place List
     */
    public NearbyPlacesInfo loadAttractionsFromLocation(LatLng curLatLng, LatLng latLangToSearchAround, boolean returnClosestResult, boolean checkingAroundCurrentLocation) throws IOException {
        boolean branchCoverage[] = new boolean[20];

        Timber.d("Loading attractions near %s", latLangToSearchAround);
        NearbyPlacesInfo nearbyPlacesInfo = new NearbyPlacesInfo();

        if (latLangToSearchAround == null) {
            branchCoverage[0] = true;
            Timber.d("Loading attractions nearby, but curLatLng is null");
            int index = 0;
            while(index < branchCoverage.length){
                System.out.println("Branch-ID " + index + " " + branchCoverage[index]);
                index++;
            }

            return null;
        }
        else{
            branchCoverage[1] = true;
        }
        List<Place> places = nearbyPlaces.radiusExpander(latLangToSearchAround, Locale.getDefault().getLanguage(), returnClosestResult);

        if (null != places && places.size() > 0) {
            branchCoverage[2] = true;
            branchCoverage[3] = true;
            LatLng[] boundaryCoordinates = {places.get(0).location,   // south
                    places.get(0).location, // north
                    places.get(0).location, // west
                    places.get(0).location};// east, init with a random location


            if (curLatLng != null) {
                branchCoverage[4] = true;
                Timber.d("Sorting places by distance...");
                final Map<Place, Double> distances = new HashMap<>();
                for (Place place : places) {
                    branchCoverage[5] = true;
                    distances.put(place, computeDistanceBetween(place.location, curLatLng));
                    // Find boundaries with basic find max approach
                    if (place.location.getLatitude() < boundaryCoordinates[0].getLatitude()) {
                        branchCoverage[6] = true;
                        boundaryCoordinates[0] = place.location;
                    }
                    else{
                        branchCoverage[7] = true;
                    }
                    if (place.location.getLatitude() > boundaryCoordinates[1].getLatitude()) {
                        branchCoverage[8] = true;
                        boundaryCoordinates[1] = place.location;
                    }
                    else{
                        branchCoverage[9] = true;
                    }
                    if (place.location.getLongitude() < boundaryCoordinates[2].getLongitude()) {
                        branchCoverage[10] = true;
                        boundaryCoordinates[2] = place.location;
                    }
                    else{
                        branchCoverage[11] = true;
                    }
                    if (place.location.getLongitude() > boundaryCoordinates[3].getLongitude()) {
                        branchCoverage[12] = true;
                        boundaryCoordinates[3] = place.location;
                    }
                    else{
                        branchCoverage[13] = true;
                    }
                }
                Collections.sort(places,
                        (lhs, rhs) -> {
                            double lhsDistance = distances.get(lhs);
                            double rhsDistance = distances.get(rhs);
                            return (int) (lhsDistance - rhsDistance);
                        }
                );
            }
            nearbyPlacesInfo.placeList = places;
            nearbyPlacesInfo.boundaryCoordinates = boundaryCoordinates;
            if (!returnClosestResult && checkingAroundCurrentLocation) {
                branchCoverage[14] = true;
                // Do not update searched radius, if controller is used for nearby card notification
                searchedRadius = nearbyPlaces.radius;
                currentLocation = curLatLng;
            }
            else{
                branchCoverage[15] = true;
            }
            int index = 0;
            while(index < branchCoverage.length){
                System.out.println("Branch-ID " + index + " " + branchCoverage[index]);
                index++;
            }
            return nearbyPlacesInfo;
        }
        else {
            branchCoverage[16] = true;
            int index = 0;
            while(index < branchCoverage.length){
                System.out.println("Branch-ID " + index + " " + branchCoverage[index]);
                index++;
            }
            return null;
        }
    }

    /**
     * Loads attractions from location for list view, we need to return Place data type.
     *
     * @param curLatLng users current location
     * @param placeList list of nearby places in Place data type
     * @return Place list that holds nearby places
     */
    static List<Place> loadAttractionsFromLocationToPlaces(
            LatLng curLatLng,
            List<Place> placeList) {
        placeList = placeList.subList(0, Math.min(placeList.size(), MAX_RESULTS));
        for (Place place : placeList) {
            String distance = formatDistanceBetween(curLatLng, place.location);
            place.setDistance(distance);
        }
        return placeList;
    }

    /**
     * Loads attractions from location for map view, we need to return BaseMarkerOption data type.
     *
     * @param curLatLng users current location
     * @param placeList list of nearby places in Place data type
     * @return BaseMarkerOptions list that holds nearby places
     */
    public static List<NearbyBaseMarker> loadAttractionsFromLocationToBaseMarkerOptions(
            LatLng curLatLng,
            List<Place> placeList,
            Context context,
            List<Place> bookmarkplacelist) {
        List<NearbyBaseMarker> baseMarkerOptions = new ArrayList<>();

        if (placeList == null) {
            return baseMarkerOptions;
        }

        placeList = placeList.subList(0, Math.min(placeList.size(), MAX_RESULTS));

        VectorDrawableCompat vectorDrawable = null;
        try {
            vectorDrawable = VectorDrawableCompat.create(
                    context.getResources(), R.drawable.ic_custom_bookmark_marker, context.getTheme()
            );
        } catch (Resources.NotFoundException e) {
            // ignore when running tests.
        }
        if (vectorDrawable != null) {
            Bitmap icon = UiUtils.getBitmap(vectorDrawable);

            for (Place place : bookmarkplacelist) {

                String distance = formatDistanceBetween(curLatLng, place.location);
                place.setDistance(distance);

                NearbyBaseMarker nearbyBaseMarker = new NearbyBaseMarker();
                nearbyBaseMarker.title(place.name);
                nearbyBaseMarker.position(
                        new com.mapbox.mapboxsdk.geometry.LatLng(
                                place.location.getLatitude(),
                                place.location.getLongitude()));
                nearbyBaseMarker.place(place);
                nearbyBaseMarker.icon(IconFactory.getInstance(context)
                        .fromBitmap(icon));
                placeList.remove(place);

                baseMarkerOptions.add(nearbyBaseMarker);
            }
        }

        vectorDrawable = null;
        try {
            vectorDrawable = VectorDrawableCompat.create(
                    context.getResources(), R.drawable.ic_custom_map_marker, context.getTheme()
            );
        } catch (Resources.NotFoundException e) {
            // ignore when running tests.
        }
        if (vectorDrawable != null) {
            Bitmap icon = UiUtils.getBitmap(vectorDrawable);

            for (Place place : placeList) {
                String distance = formatDistanceBetween(curLatLng, place.location);
                place.setDistance(distance);

                NearbyBaseMarker nearbyBaseMarker = new NearbyBaseMarker();
                nearbyBaseMarker.title(place.name);
                nearbyBaseMarker.position(
                        new com.mapbox.mapboxsdk.geometry.LatLng(
                                place.location.getLatitude(),
                                place.location.getLongitude()));
                nearbyBaseMarker.place(place);
                nearbyBaseMarker.icon(IconFactory.getInstance(context)
                        .fromBitmap(icon));

                baseMarkerOptions.add(nearbyBaseMarker);
            }
        }

        return baseMarkerOptions;
    }

    /**
     * We pass this variable as a group of placeList and boundaryCoordinates
     */
    public class NearbyPlacesInfo {
        public List<Place> placeList; // List of nearby places
        public LatLng[] boundaryCoordinates; // Corners of nearby area
    }
}
