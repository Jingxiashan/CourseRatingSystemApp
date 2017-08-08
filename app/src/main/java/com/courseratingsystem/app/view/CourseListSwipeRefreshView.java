package com.courseratingsystem.app.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.ListView;

import com.courseratingsystem.app.R;

/**
 * Created by kongx on 2017/8/8 0008.
 */

public class CourseListSwipeRefreshView extends SwipeRefreshLayout {
    private float scaledTouchSlop;
    private ListView mCourseList;
    private boolean isLastItem, isLoadingMore;
    private float mStartY, mEndY;
    private OnLoadMoreListener onLoadMoreListener;
    private OnSwipeListener onSwipeListener;
    private View mFooterView;

    public CourseListSwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFooterView = View.inflate(context, R.layout.fragment_courselist_list_footer, null);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mCourseList == null) {
            // 判断容器有多少个孩子
            if (getChildCount() > 0) {
                // 判断第一个孩子是不是ListView
                if (getChildAt(0) instanceof ListView) {
                    // 创建ListView对象
                    mCourseList = (ListView) getChildAt(0);
                    // 设置ListView的滑动监听
                    setListViewOnScroll();
                }
            }
        }
    }

    private void setListViewOnScroll() {
        mCourseList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //不滑动
                    case SCROLL_STATE_IDLE:
                        //判断是否是否显示到了最后一个item
                        isLastItem = view.getLastVisiblePosition() == (view.getCount() - 1);
                        break;
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (isLastItem && !isLoadingMore) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mEndY = ev.getY();
                    if ((mEndY - mStartY) <= scaledTouchSlop) {
                        setLoading(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        } else {
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
        }
        return super.dispatchTouchEvent(ev);
    }

    private void setLoading(boolean loading) {
        isLoadingMore = loading;
        if (loading) {
            mCourseList.addFooterView(mFooterView);
            onLoadMoreListener.onLoadMore();
        } else {
            mCourseList.removeFooterView(mFooterView);
        }
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    ;

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }

    public enum SwipeDirect {UP, DOWN}

    public interface OnSwipeListener {
        void onSwipe(SwipeDirect swipeDirect);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}

