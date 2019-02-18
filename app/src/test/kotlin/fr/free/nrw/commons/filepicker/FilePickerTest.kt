package fr.free.nrw.commons.filepicker

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest({PickedFiles::class})
class FilePickerTest {

    @Mock
    private lateinit var data: Intent
    @Mock
    private lateinit var activity: Activity
    @Mock
    private lateinit var callbacks: FilePicker.Callbacks
    @Mock
    private lateinit var uri: Uri
    @Mock
    private lateinit var clipData: ClipData
    @Mock
    private lateinit var uploadableFile: UploadableFile

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun handleActivityResultOKFromDocuments() {
        val requestCode = 2924
        val resultCode = -1

        PowerMockito.mockStatic(PickedFiles::class.java)

        Mockito.`when`(data.data).thenReturn(uri)
        Mockito.`when`(data.clipData).thenReturn(clipData)
        Mockito.`when`(PickedFiles.pickedExistingPicture(activity, uri)).thenReturn(uploadableFile)

        FilePicker.handleActivityResult(requestCode, resultCode, data, activity, callbacks)
        Mockito.verify(FilePicker.onPictureReturnedFromDocuments(data, activity, callbacks))
    }
}