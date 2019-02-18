package fr.free.nrw.commons.filepicker

import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.preference.PreferenceManager
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Test

class FilePickerTest {

    @MockK
    private lateinit var data: Intent
    @RelaxedMockK
    private lateinit var activity: Activity
    @RelaxedMockK
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

        val mList= mockk<List<UploadableFile>>(relaxed = true)

        mockkStatic(PickedFiles::class)
        mockkStatic(PreferenceManager::class)

        every { data.data  } returns uri
        every { data.clipData } returns clipData
        every { PickedFiles.pickedExistingPicture(any<Activity>(), any<Uri>()) } returns uploadableFile
        every { callbacks.onImagesPicked(mList, FilePicker.ImageSource.DOCUMENTS, any<Int>()) } just Runs
        every { PreferenceManager.getDefaultSharedPreferences(activity).getInt("type", 0) } returns 1

        FilePicker.handleActivityResult(requestCode, resultCode, data, activity, callbacks)

        verify{
            PickedFiles.pickedExistingPicture(any<Activity>(), any<Uri>())
        }
    }
}