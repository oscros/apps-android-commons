package fr.free.nrw.commons.ui.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import junit.framework.Assert.assertFalse
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
    @Mock
    private lateinit var mRect: Rect

    private lateinit var mCustomEditText: CustomEditText

    @Before
    @Throws(Exception::class)
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mEvent.action = MotionEvent.ACTION_DOWN
        mCustomEditText = CustomEditText(mContext, mAttrs)
    }

    /**
     * Tests if the method onTouchEvent behaves correctly when only a bottom drawable
     * is given.
     */
    @Test
    fun onTouchEventTestDrawableBottom() {
        mCustomEditText.setCompoundDrawables(null, null, null, mDrawable)

        Mockito.`when`(mDrawable.bounds).thenReturn(mRect)
        Mockito.`when`(mRect.contains(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(true)

        mCustomEditText.setDrawableClickListener(mListener)
        mCustomEditText.onTouchEvent(mEvent)

        Mockito.verify(mListener).onClick(CustomEditText.DrawableClickListener.DrawablePosition.BOTTOM)
    }

    /**
     * Tests if the method onTouchEvent behaves correctly when only a right drawable
     * is given.
     */
    @Test
    fun onTouchEventTestDrawableRight() {
        mCustomEditText.setCompoundDrawables(null, null, mDrawable, null)

        Mockito.`when`(mDrawable.bounds).thenReturn(mRect)
        Mockito.`when`(mRect.contains(Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(true)

        mCustomEditText.setDrawableClickListener(mListener)
        val result = mCustomEditText.onTouchEvent(mEvent)

        Mockito.verify(mListener).onClick(CustomEditText.DrawableClickListener.DrawablePosition.RIGHT)
        Mockito.verify(mEvent).action = MotionEvent.ACTION_CANCEL
        assertFalse(result)
    }
}