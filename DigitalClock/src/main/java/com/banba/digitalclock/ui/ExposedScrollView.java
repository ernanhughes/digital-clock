package com.banba.digitalclock.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Property;
import android.widget.ScrollView;

/**
 * Created by Ernan on 14/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class ExposedScrollView extends ScrollView {
    public ExposedScrollView(Context context) {
        super(context);
    }

    public ExposedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExposedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public static final Property<ScrollView, Integer> SCROLL_POS
            = new Property<ScrollView, Integer>(Integer.class, "scrollPos") {
        @Override
        public void set(ScrollView object, Integer value) {
            object.scrollTo(0, value);
        }

        @Override
        public Integer get(ScrollView object) {
            return object.getScrollY();
        }
    };
}