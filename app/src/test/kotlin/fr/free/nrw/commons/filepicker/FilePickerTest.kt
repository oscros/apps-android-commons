package fr.free.nrw.commons.filepicker

import android.app.Activity
import android.content.Intent
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FilePickerTest {

    private var requestCode: Int = 1
    private var resultCode: Int = 2

    @Mock
    private lateinit var data: Intent
    @Mock
    private lateinit var activity: Activity
    @Mock
    private lateinit var callbacks: FilePicker.Callbacks

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    @Throws(Exception::class)
    fun tearDown() {
        for(value in FilePicker.testCoverage)
            println(value)
    }

    @Test
    fun handleActivityResult() {
        FilePicker.handleActivityResult(requestCode, resultCode, data, activity, callbacks)

    }
}