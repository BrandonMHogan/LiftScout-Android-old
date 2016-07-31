package com.brandonhogan.liftscout.foundation.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BaseFrameLayout extends RelativeLayout {


    public BaseFrameLayout(Context context) {
        super(context);
    }

    public BaseFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public float getXFraction() {
        final int width = getWidth();
        if (width != 0) {
            return getX() / getWidth();
        } else {
            return getX();
        }
    }

    public void setXFraction(float xFraction) {
        final int width = getWidth();
        if (width > 0) {
            setX(xFraction * width);
        } else {
            setX(-10000);
        }
    }
}