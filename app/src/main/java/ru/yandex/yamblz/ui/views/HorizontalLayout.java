package ru.yandex.yamblz.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Litun on 22.07.2016.
 */

public class HorizontalLayout extends ViewGroup {
    public static final String LOG_TAG = "custom view";

    public HorizontalLayout(Context context) {
        this(context, null, 0);
    }

    public HorizontalLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(LOG_TAG, "onLayout");
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                return;
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
            if (maxHeight < curHeight)
                maxHeight = curHeight;
            curLeft += curWidth;
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.i(LOG_TAG, "onMeasure");
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(LOG_TAG, "widthSize = " + widthSize);
        final LayoutParams layoutParams = getLayoutParams();
        final int count = getChildCount();

        boolean equality = false;
        int totalWidth = 0;
        final List<View> matchParentChildren = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            final LayoutParams childLayoutParams = child.getLayoutParams();
            if (child.getVisibility() == GONE) {
                continue;
            }
            if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
                matchParentChildren.add(child);
                continue;
            }
            child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.UNSPECIFIED));
            totalWidth += child.getMeasuredWidth();
            Log.i(LOG_TAG, "totalWidth1 = " + totalWidth);
        }
        if (matchParentChildren.size() > 0) {
            int freeWidth = widthSize - totalWidth;
            Log.i(LOG_TAG, "totalWidth2 = " + totalWidth);
            if (freeWidth <= 0 || layoutParams.width == LayoutParams.WRAP_CONTENT) {
                equality = true;

                for (View child : matchParentChildren) {
                    child.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
                    child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST),
                            MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.UNSPECIFIED));
                    totalWidth += child.getMeasuredWidth();
                }
            } else {
                int freeWidthEach = freeWidth / matchParentChildren.size();
                for (View child : matchParentChildren) {
                    child.measure(MeasureSpec.makeMeasureSpec(freeWidthEach, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.UNSPECIFIED));
                    totalWidth += child.getMeasuredWidth();
                }
            }
            Log.i(LOG_TAG, "totalWidth3 = " + totalWidth);
        }

        setMeasuredDimension(totalWidth,
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));

        Log.i(LOG_TAG, "myWidth = " + getMeasuredWidth());
        Log.i(LOG_TAG, "myHeight = " + getMeasuredHeight());
    }

}
