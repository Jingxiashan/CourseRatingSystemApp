package com.courseratingsystem.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ListView;

/**
 * Created by Jingxiashan on 2017/8/8.
 */

public class CommentListView extends ListView {

    private int scaledTouchSlop;
    private OnSwipeListener onSwipeListener;
    private float mStartY, mEndY;

    public CommentListView(Context context) {
        super(context);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mEndY = ev.getY();
                if ((mEndY - mStartY) <= scaledTouchSlop) {
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
