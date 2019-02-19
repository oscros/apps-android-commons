package fr.free.nrw.commons;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.Field;

import static fr.free.nrw.commons.location.LocationServiceManager.LocationChangeType.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import fr.free.nrw.commons.location.LatLng;
import fr.free.nrw.commons.location.LocationServiceManager;
import fr.free.nrw.commons.nearby.NearbyFragment;
import fr.free.nrw.commons.utils.NetworkUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest(NetworkUtils.class)
public class NearbyFragmentTest {

    /**
     * Checks whether {@link NearbyFragment#refreshView refreshView} stores the right value in
     * curLatLng when it is given LOCATION_CHANGED_MEDIUM as argument.
     * @throws Exception if field names are inappropriate.
     */
    @Test
    public void refreshViewReaches() throws Exception {
        NearbyFragment nf = new NearbyFragment();

        Field lockNearbyViewField = NearbyFragment.class.getDeclaredField("lockNearbyView");
        lockNearbyViewField.setAccessible(true);
        lockNearbyViewField.set(nf, false);

        Field curLatLngField = NearbyFragment.class.getDeclaredField("curLatLng");
        curLatLngField.setAccessible(true);
        curLatLngField.set(nf, null);

        PowerMockito.mockStatic(NetworkUtils.class);
        when(NetworkUtils.isInternetConnectionEstablished(nf.getActivity())).thenReturn(true);

        // Set LocationManager
        LocationServiceManager mockLocationManager = mock(LocationServiceManager.class);
        Field locationManagerField = NearbyFragment.class.getDeclaredField("locationManager");
        locationManagerField.setAccessible(true);
        locationManagerField.set(nf, mockLocationManager);

        LatLng mockLocation = mock(LatLng.class);
        when(mockLocationManager.getLastLocation()).thenReturn(mockLocation);

        nf.refreshView(LOCATION_MEDIUM_CHANGED);

        Assert.assertNotEquals(null, mockLocation);
        Assert.assertNotEquals(null, curLatLngField.get(nf));
        Assert.assertEquals(mockLocation, curLatLngField.get(nf));
    }

    /**
     * Checks whether {@link NearbyFragment#refreshView refreshView} stores the right value in
     * curLatLng when it is given PERMISSION_JUST_GRANTED as argument.
     * @throws Exception if the field names are inappropriate.
     */
    @Test
    public void refreshView2() throws Exception {
        NearbyFragment nf = new NearbyFragment();

        Field lockNearbyViewField = NearbyFragment.class.getDeclaredField("lockNearbyView");
        lockNearbyViewField.setAccessible(true);
        lockNearbyViewField.set(nf, false);

        Field curLatLngField = NearbyFragment.class.getDeclaredField("curLatLng");
        curLatLngField.setAccessible(true);
        curLatLngField.set(nf, null);

        LatLng mockLastKnownLocation = mock(LatLng.class);

        Field lastKnownLocationField = NearbyFragment.class.getDeclaredField("lastKnownLocation");
        lastKnownLocationField.setAccessible(true);
        lastKnownLocationField.set(nf, mockLastKnownLocation);

        PowerMockito.mockStatic(NetworkUtils.class);
        when(NetworkUtils.isInternetConnectionEstablished(nf.getActivity())).thenReturn(true);

        // Set LocationManager
        LocationServiceManager mockLocationManager = mock(LocationServiceManager.class);
        Field locationManagerField = NearbyFragment.class.getDeclaredField("locationManager");
        locationManagerField.setAccessible(true);
        locationManagerField.set(nf, mockLocationManager);

        LatLng mockLocation = mock(LatLng.class);
        when(mockLocationManager.getLastLocation()).thenReturn(mockLocation);

        try {
            nf.refreshView(PERMISSION_JUST_GRANTED);
        } catch (NullPointerException e) {
            // Is expected
        }

        Assert.assertNotEquals(null, mockLastKnownLocation);
        Assert.assertNotEquals(null, curLatLngField.get(nf));
        Assert.assertEquals(mockLastKnownLocation, curLatLngField.get(nf));
    }
}
