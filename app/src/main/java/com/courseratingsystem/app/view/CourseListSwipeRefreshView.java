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
    private boolean canHide;

    public CourseListSwipeRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mFooterView = View.inflate(context, R.layout.footer_fragment_courselist_list, null);
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
                if (view.getFirstVisiblePosition() == 0) {
                    onSwipeListener.onSwipe(SwipeDirect.DOWN);
                    canHide = false;
                } else {
                    canHide = true;
                }
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
            //for loading more
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mEndY = ev.getY();
                    if ((mEndY - mStartY) <= scaledTouchSlop) {
                        setLoadingMore(true);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
        } else {
            //for swiping detection
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mEndY = ev.getY();
                    if (onSwipeListener != null) {
                        if ((mEndY - mStartY) <= -scaledTouchSlop) {
                            //内容向下
                            if (canHide) {
                                onSwipeListener.onSwipe(SwipeDirect.UP);
                            }
                        } else if ((mEndY - mStartY) >= scaledTouchSlop) {
                            //内容向上
                            onSwipeListener.onSwipe(SwipeDirect.DOWN);
                        }
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setLoadingMore(boolean loading) {
        if (loading && isLoadingMore) {
            //如果正在加载并且再要求加载的话，取消此次操作
            return;
        }
        if (!loading && !isLoadingMore) {
            //如果不是正在加载但要求取消加载，取消此次操作
            return;
        }
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

