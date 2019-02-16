package fr.free.nrw.commons.ui.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CustomEditTextTest {
    @Mock
    private lateinit var mEvent:MotionEvent
    @Mock
    private lateinit var mContext: Context
    @Mock
    private lateinit var mAttrs: AttributeSet
    @Mock
    private lateinit var mDrawable: Drawable
    @Mock
    private lateinit var mListener: CustomEditText.DrawableClickListener

    private lateinit var mCustomEditText: CustomEditText

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mEvent.action = MotionEvent.ACTION_DOWN
        mCustomEditText = CustomEditText(mContext, mAttrs)
    }

    @After
    fun tearDown() {
        for(value in mCustomEditText.testCoverage)
            println(value)
    }

    @Test
    fun onTouchEventTestDrawableLeft() {
        mCustomEditText.setCompoundDrawables(mDrawable, null, null, null)
        val rect = Rect()

        Mockito.`when`(mDrawable.bounds).thenReturn(rect)
        Mockito.`when`(rect.contains(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(true)

        mCustomEditText.onTouchEvent(mEvent)

        Mockito.verify(mListener.onClick(CustomEditText.DrawableClickListener.DrawablePosition.BOTTOM))
    }
}