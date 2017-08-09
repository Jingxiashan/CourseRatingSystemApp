package com.courseratingsystem.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by panda on 2017/8/8.
 */

public class ObservableScrollView extends ScrollView {
    private ScrollViewListener scrollViewListener;
    float scale=1.0f;
    public interface ScrollViewListener {
        void onScrollChanged(ObservableScrollView scrollView,int l,int t,int oldl,int oldt);
    };
    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(scrollViewListener !=null){
            scrollViewListener.onScrollChanged(this,l,t,oldl,oldt);
        }
    }
    public void setScrollViewListener(ScrollViewListener scrollViewListener){
        this.scrollViewListener = scrollViewListener;
    }
}
