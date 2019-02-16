package fr.free.nrw.commons

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.FloatingActionButton
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import fr.free.nrw.commons.HandlerService
import fr.free.nrw.commons.auth.SessionManager
import fr.free.nrw.commons.contributions.Contribution
import fr.free.nrw.commons.kvstore.BasicKvStore
import fr.free.nrw.commons.nearby.NearbyMapFragment
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.lang.reflect.Array
import android.R.attr.onClick
import android.R.attr.restoreNeedsApplication
import org.mockito.Mockito



class NearbyMapFragmentTest {

    @Mock
    internal var fabPlus: FloatingActionButton = mock(FloatingActionButton::class.java)
    @Mock
    var fabRecenter: FloatingActionButton = mock(FloatingActionButton::class.java)
    @Mock
    var bottomSheetDetailsBehavior: BottomSheetBehavior<*> = mock(BottomSheetBehavior::class.java)
    @Mock
    var bottomSheetListBehavior: BottomSheetBehavior<*> = mock(BottomSheetBehavior::class.java)
    @Mock
    var title: TextView = mock(TextView::class.java)
    @Mock
    var bottomSheetDetails: View = mock(View::class.java)
    @Mock
    var directionsButtonText: TextView = mock(TextView::class.java)
    @Mock
    var wikipediaButtonText: TextView = mock(TextView::class.java)
    @Mock
    var wikidataButtonText: TextView = mock(TextView::class.java)
    @Mock
    var commonsButtonText: TextView = mock(TextView::class.java)
    @Mock
    var applicationKvStore: BasicKvStore = mock(BasicKvStore::class.java)


    @InjectMocks
    var nearBy: NearbyMapFragment = NearbyMapFragment()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        //val uploadService = mock(UploadService::class.java)
        //val binder = mock(HandlerService.HandlerServiceLocalBinder::class.java)
        //`when`(binder.service).thenReturn(uploadService)
        //uploadController!!.uploadServiceConnection.onServiceConnected(mock(ComponentName::class.java), binder)
    }

    @Test
    fun prepareService() {
        //uploadController!!.prepareService()
    }

    @Test
    fun cleanup() {
        //uploadController!!.cleanup()
        var x = 0
    }

    /**
     * test button text visibility (should be hidden if linecount > 1)
     */
    @Test
    fun testSetListeners1() {
        //var applicationKvStore: BasicKvStore = mock(BasicKvStore::class.java)
        //nearBy.applicationKvStore = applicationKvStore
        //applicationKvStore.getBoolean("login_skipped", false)
        `when`(directionsButtonText.getLineCount()).thenReturn(2)
        nearBy.setListeners()
        verify(wikipediaButtonText).setVisibility(View.GONE)
        verify(wikidataButtonText).setVisibility(View.GONE)
        verify(commonsButtonText).setVisibility(View.GONE)
        verify(directionsButtonText).setVisibility(View.GONE)




    }

    /**
     * test button text visibility (should be hidden if linecount == 1)
     */
    @Test
    fun testSetListeners2() {
        //var applicationKvStore: BasicKvStore = mock(BasicKvStore::class.java)
        //nearBy.applicationKvStore = applicationKvStore
        //applicationKvStore.getBoolean("login_skipped", false)
        `when`(directionsButtonText.getLineCount()).thenReturn(0)
        nearBy.setListeners()
        verify(wikipediaButtonText, times(1)).setVisibility(View.GONE)
        verify(wikidataButtonText, times(1)).setVisibility(View.GONE)
        verify(commonsButtonText, times(1)).setVisibility(View.GONE)
        verify(directionsButtonText, times(1)).setVisibility(View.GONE)




    }

    /**
     * test button text visibility (should be NOT hidden if linecount == 1)
     */
    @Test
    fun testSetListeners3() {
        //var applicationKvStore: BasicKvStore = mock(BasicKvStore::class.java)
        //nearBy.applicationKvStore = applicationKvStore
        //applicationKvStore.getBoolean("login_skipped", false)
        `when`(directionsButtonText.getLineCount()).thenReturn(1)
        nearBy.setListeners()
        verify(wikipediaButtonText, times(0)).setVisibility(View.GONE)
        verify(wikidataButtonText, times(0)).setVisibility(View.GONE)
        verify(commonsButtonText, times(0)).setVisibility(View.GONE)
        verify(directionsButtonText, times(0)).setVisibility(View.GONE)




    }

}