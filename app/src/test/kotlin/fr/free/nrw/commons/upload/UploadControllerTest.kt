package fr.free.nrw.commons.upload

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import fr.free.nrw.commons.HandlerService
import fr.free.nrw.commons.auth.SessionManager
import fr.free.nrw.commons.contributions.Contribution
import fr.free.nrw.commons.kvstore.BasicKvStore
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.lang.reflect.Array

class UploadControllerTest {

    @Mock
    internal var sessionManager: SessionManager = mock(SessionManager::class.java)
    @Mock
    internal var context: Context? = null
    @Mock
    internal var prefs: BasicKvStore? = mock(BasicKvStore::class.java)


    @InjectMocks
    var uploadController: UploadController? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val uploadService = mock(UploadService::class.java)
        val binder = mock(HandlerService.HandlerServiceLocalBinder::class.java)
        `when`(binder.service).thenReturn(uploadService)
        uploadController!!.uploadServiceConnection.onServiceConnected(mock(ComponentName::class.java), binder)
    }

    @Test
    fun prepareService() {
        uploadController!!.prepareService()
    }

    @Test
    fun cleanup() {
        uploadController!!.cleanup()
    }

    @Test
    fun startUpload() {
        val contribution = mock(Contribution::class.java)
        uploadController!!.startUpload(contribution)
    }

    /**
     * Test if authorName is present, it is set to the contribution
     */
    @Test
    fun startUpload2() {
        val contribution = mock(Contribution::class.java)
        `when`(prefs!!.getBoolean("useAuthorName", false)).thenReturn(true)
        `when`(prefs!!.getString("authorName", "")).thenReturn("oscar")

        uploadController!!.startUpload(contribution)
        verify(contribution).setCreator("oscar");
    }

    /**
     * Test when the description field in the contribution is null
     */
    @Test
    fun startUpload3() {
        val contribution = mock(Contribution::class.java)
        `when`(contribution.getDescription()).thenReturn(null)
        uploadController!!.startUpload(contribution)
        verify(contribution).setDescription("")
        //`when`(TextUtils.isEmpty(ArgumentMatchers.anyString())).thenReturn(true)
    }

    /**
     * Test when the description field in the contribution is NOT null
     */
    @Test
    fun startUpload4() {
        val contribution = mock(Contribution::class.java)
        `when`(contribution.getDescription()).thenReturn("test")
        uploadController!!.startUpload(contribution)
        verify(contribution, times(0)).setDescription("")
        //`when`(TextUtils.isEmpty(ArgumentMatchers.anyString())).thenReturn(true)

    }
}