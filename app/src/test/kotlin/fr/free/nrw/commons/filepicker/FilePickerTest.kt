package fr.free.nrw.commons.filepicker

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test

class FilePickerTest {

    @MockK
    private lateinit var data: Intent
    @MockK
    private lateinit var activity: Activity
    @MockK
    private lateinit var callbacks: FilePicker.Callbacks
    @MockK
    private lateinit var uri: Uri
    @MockK
    private lateinit var clipData: ClipData
    @MockK
    private lateinit var uploadableFile: UploadableFile

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun handleActivityResultOKFromDocuments() {
        val requestCode = 2924
        val resultCode = -1

        mockkStatic(PickedFiles::class)

        every { data.data  } returns uri
        every { data.clipData } returns clipData

      //  Mockito.`when`(data.data).thenReturn(uri)
      //  Mockito.`when`(data.clipData).thenReturn(clipData)
      //  Mockito.`when`(PickedFiles.pickedExistingPicture(activity, uri)).thenReturn(uploadableFile)

        FilePicker.handleActivityResult(requestCode, resultCode, data, activity, callbacks)
      //  Mockito.verify(FilePicker.onPictureReturnedFromDocuments(data, activity, callbacks))
    }
}