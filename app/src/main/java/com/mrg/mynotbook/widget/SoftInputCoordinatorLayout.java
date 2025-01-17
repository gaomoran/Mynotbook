package com.mrg.mynotbook.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.WindowInsets;

/**
 * Created by MrG on 2017-05-18.
 */
public class SoftInputCoordinatorLayout extends CoordinatorLayout {
    public SoftInputCoordinatorLayout(Context context) {
        super(context);
    }

    public SoftInputCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftInputCoordinatorLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }

    @Override
    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0, insets.getSystemWindowInsetBottom()));
        } else {
            return insets;
        }
    }
}
