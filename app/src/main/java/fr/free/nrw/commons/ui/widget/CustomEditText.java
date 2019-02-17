package fr.free.nrw.commons.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Custom edit text with a drawable click listener
 * https://stackoverflow.com/questions/13135447/setting-onclicklistener-for-the-drawable-right-of-an-edittext
 */
@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText {

    private Drawable drawableRight;
    private Drawable drawableLeft;
    private Drawable drawableTop;
    private Drawable drawableBottom;

    int actionX, actionY;

    private DrawableClickListener clickListener;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // this Contructure required when you are using this view in xml
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top,
                                     Drawable right, Drawable bottom) {
        if (left != null) {
            drawableLeft = left;
        }
        if (right != null) {
            drawableRight = right;
        }
        if (top != null) {
            drawableTop = top;
        }
        if (bottom != null) {
            drawableBottom = bottom;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    /**
     * Fires the appropriate drawable click listener on touching the icon
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean[] testCoverage = new boolean[31];
        Rect bounds;
        testCoverage[0] = true;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            testCoverage[1] = true;
            actionX = (int) event.getX();
            actionY = (int) event.getY();
            if (drawableBottom != null
                    && drawableBottom.getBounds().contains(actionX, actionY)) {
                testCoverage[2] = true;
                testCoverage[3] = true;
                clickListener.onClick(DrawableClickListener.DrawablePosition.BOTTOM);
                for(int i = 0; i < testCoverage.length; i++)
                    System.out.println("Branch " + i + " reached: " + testCoverage[i]);
                return super.onTouchEvent(event);
            } else
                testCoverage[4] = true;

            if (drawableTop != null
                    && drawableTop.getBounds().contains(actionX, actionY)) {
                testCoverage[5] = true;
                testCoverage[6] = true;
                clickListener.onClick(DrawableClickListener.DrawablePosition.TOP);
                for(int i = 0; i < testCoverage.length; i++)
                    System.out.println("Branch " + i + " reached: " + testCoverage[i]);
                return super.onTouchEvent(event);
            } else
                testCoverage[7] = true;

            // this works for left since container shares 0,0 origin with bounds
            if (drawableLeft != null) {
                testCoverage[8] = true;
                bounds = null;
                bounds = drawableLeft.getBounds();

                int x, y;
                int extraTapArea = (int) (13 * getResources().getDisplayMetrics().density + 0.5);

                x = actionX;
                y = actionY;

                if (!bounds.contains(actionX, actionY)) {
                    testCoverage[9] = true;
                    // Gives the +20 area for tapping.
                    x = (int) (actionX - extraTapArea);
                    y = (int) (actionY - extraTapArea);

                    if (x <= 0) {
                        testCoverage[10] = true;
                        x = actionX;
                    } else
                        testCoverage[11] = true;
                    if (y <= 0) {
                        testCoverage[12] = true;
                        y = actionY;
                    } else
                        testCoverage[13] = true;

                    // Creates square from the smallest value
                    if (x < y) {
                        testCoverage[14] = true;
                        y = x;
                    } else
                        testCoverage[15] = true;
                } else
                    testCoverage[16] = true;

                if (bounds.contains(x, y) && clickListener != null) {
                    testCoverage[17] = true;
                    testCoverage[18] = true;
                    clickListener
                            .onClick(DrawableClickListener.DrawablePosition.LEFT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    for(int i = 0; i < testCoverage.length; i++)
                        System.out.println("Branch " + i + " reached: " + testCoverage[i]);
                    return false;

                } else
                    testCoverage[19] = true;
            } else
                testCoverage[20] = true;

            if (drawableRight != null) {
                testCoverage[21] = true;

                bounds = null;
                bounds = drawableRight.getBounds();

                int x, y;
                int extraTapArea = 13;

                /*
                  IF USER CLICKS JUST OUT SIDE THE RECTANGLE OF THE DRAWABLE
                  THAN ADD X AND SUBTRACT THE Y WITH SOME VALUE SO THAT AFTER
                  CALCULATING X AND Y CO-ORDINATE LIES INTO THE DRAWBABLE
                  BOUND. - this process help to increase the tappable area of
                  the rectangle.
                 */
                x = (int) (actionX + extraTapArea);
                y = (int) (actionY - extraTapArea);

                /*
                  Since this is right drawable subtract the value of x from the width
                  of view. so that width - tappedarea will result in x co-ordinate in drawable bound.
                 */
                x = getWidth() - x;

                /*x can be negative if user taps at x co-ordinate just near the width.
                 * e.g views width = 300 and user taps 290. Then as per previous calculation
                 * 290 + 13 = 303. So subtract X from getWidth() will result in negative value.
                 * So to avoid this add the value previous added when x goes negative.
                 */

                if (x <= 0) {
                    testCoverage[22] = true;
                    x += extraTapArea;
                } else
                    testCoverage[23] = true;

                /* If result after calculating for extra tappable area is negative.
                 * assign the original value so that after subtracting
                 * extra tapping area value doesn't go into negative value.
                 */

                if (y <= 0) {
                    testCoverage[24] = true;
                    y = actionY;
                } else
                    testCoverage[25] = true;

                // If drawble bounds contains the x and y points then move ahead.
                if (bounds.contains(x, y) && clickListener != null) {
                    testCoverage[26] = true;
                    testCoverage[27] = true;
                    clickListener
                            .onClick(DrawableClickListener.DrawablePosition.RIGHT);
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    return false;
                }
                else
                    testCoverage[28] = true;

                for(int i = 0; i < testCoverage.length; i++)
                    System.out.println("Branch " + i + " reached: " + testCoverage[i]);
                return super.onTouchEvent(event);
            }
            else
                testCoverage[29] = true;

        } else
            testCoverage[30] = true;

        for(int i = 0; i < testCoverage.length; i++)
            System.out.println("Branch " + i + " reached: " + testCoverage[i]);
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        drawableRight = null;
        drawableBottom = null;
        drawableLeft = null;
        drawableTop = null;
        super.finalize();
    }

    /**
     * Attaches the drawable click listener to the custom edit text
     * @param listener
     */
    public void setDrawableClickListener(DrawableClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * Interface for drawable click listener
     */
    public interface DrawableClickListener {
        enum DrawablePosition {TOP, BOTTOM, LEFT, RIGHT}
        void onClick(DrawablePosition target);
    }

}
