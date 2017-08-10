package com.courseratingsystem.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by kongx on 2017/8/9 0009.
 */

public class DiscoverScrollView extends ScrollView {
    private OnSwipeListener onSwipeListener;
    private float mStartY, mEndY;
    private float scaledTouchSlop;

    public DiscoverScrollView(Context context) {
        super(context);
        init(context);
    }

    public DiscoverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DiscoverScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public DiscoverScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop() * 5;
        this.scrollTo(0, 0);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mEndY = ev.getY();
                if ((mEndY - mStartY) <= -scaledTouchSlop) {
                    //下滑
                    onSwipeListener.onSwipe(SwipeDirect.UP);
                } else if ((mEndY - mStartY) >= scaledTouchSlop) {
                    //上滑
                    onSwipeListener.onSwipe(SwipeDirect.DOWN);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public enum SwipeDirect {UP, DOWN}

    public interface OnSwipeListener {
        void onSwipe(SwipeDirect swipeDirect);
    }
}
