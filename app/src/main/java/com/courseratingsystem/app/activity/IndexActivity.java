package com.courseratingsystem.app.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.fragment.CourseListFragment;
import com.courseratingsystem.app.fragment.DiscoverFragment;
import com.courseratingsystem.app.fragment.UserFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import static android.view.View.GONE;

@ContentView(R.layout.activity_index)
public class IndexActivity extends AppCompatActivity {

    private static final long mBackPressThreshold = 3500;
    AnimatorSet hideElementAnimatorSet;//这是隐藏头尾元素使用的动画
    AnimatorSet showElementAnimatorSet;//这是显示头尾元素使用的动画
    private long mLastBackPressed;
    @ViewInject(R.id.activity_index_search_detail_layout)
    private LinearLayout mSearchDetailLayout;
    @ViewInject(R.id.activity_index_search_detail_button_by_course)
    private TextView mDetialCourse;
    @ViewInject(R.id.activity_index_search_detail_button_by_teacher)
    private TextView mDetialTeacher;
    @ViewInject(R.id.activity_index_layout_search)
    private RelativeLayout mSearchLayout;
    @ViewInject(R.id.activity_index_search_icon)
    private ImageView mSearchIcon;
    @ViewInject(R.id.activity_index_input_search)
    private EditText mSearch;
    @ViewInject(R.id.activity_index_text_courseTab)
    private TextView mTabCourse;
    @ViewInject(R.id.activity_index_text_discoverTab)
    private TextView mTabDiscover;
    @ViewInject(R.id.activity_index_text_userTab)
    private TextView mTabUser;
    @ViewInject(R.id.activity_index_bar_footer)
    private LinearLayout footerBar;
    private Toast mPressBackToast;
    private CourseListFragment courseListFragment;
    private DiscoverFragment discoverFragment;
    private UserFragment userFragment;

    private boolean isSearching;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setStatusBarColor(getResources().getColor(R.color.splashBackColor));
        x.view().inject(this);
        onTabSelected(mTabCourse);
        mSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearch.setFocusableInTouchMode(false);
        mPressBackToast = Toast.makeText(this, getString(R.string.activity_index_toast_press_back_again_to_exit), Toast.LENGTH_SHORT);
        mDetialCourse.setSelected(true);
        mSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mSearchDetailLayout.getVisibility() == GONE) {
                    isSearching = true;
                    mSearch.setFocusableInTouchMode(true);
                    mSearchDetailLayout.setVisibility(View.VISIBLE);
                    mSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back_24dp, null));
                    mSearchIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            exitSearchingMode();
                        }
                    });
                    mSearchDetailLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
                return false;
            }
        });
    }


    //搜索栏回车键监听
    @Event(value = R.id.activity_index_input_search, type = TextView.OnKeyListener.class)
    private boolean onSearchKeyPressed(View view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
            String searchText = mSearch.getText().toString();
            exitSearchingMode();
            searchForCourses(searchText);
        }
        return false;
    }

    //搜索详情页面按钮监听
    @Event(value = {R.id.activity_index_search_detail_button_by_course, R.id.activity_index_search_detail_button_by_teacher}, type = View.OnClickListener.class)
    private void onSearchTypeSelected(View view) {
        switch (view.getId()) {
            case R.id.activity_index_search_detail_button_by_course:
                mDetialCourse.setSelected(true);
                mDetialTeacher.setSelected(false);
                break;
            case R.id.activity_index_search_detail_button_by_teacher:
                mDetialCourse.setSelected(false);
                mDetialTeacher.setSelected(true);
                break;
        }
    }

    private void exitSearchingMode() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchLayout.getWindowToken(), 0);
        mSearchDetailLayout.setVisibility(GONE);
        mSearchIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_search_24dp, null));
        mSearchIcon.setOnClickListener(null);
        mSearch.setFocusableInTouchMode(false);
        isSearching = false;
    }

    //执行搜索
    private void searchForCourses(String searchText) {
        if (courseListFragment == null) {
            courseListFragment = new CourseListFragment();
        }
        int searchType;
        if (mDetialCourse.isSelected()) {
            searchType = CourseListFragment.CourseListSession.SEARCH_BY_COURSE;
        } else {
            searchType = CourseListFragment.CourseListSession.SEARCH_BY_TEACHER;
        }
        courseListFragment.search(searchText, searchType);
        onTabSelected(mTabCourse);
    }

    //底部导航栏动作监听
    @Event(value = {R.id.activity_index_text_courseTab, R.id.activity_index_text_discoverTab, R.id.activity_index_text_userTab}, type = View.OnClickListener.class)
    private void onTabSelected(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        hideAllFragment(transaction);
        clearSelectedMark();
        switch (view.getId()) {
            case R.id.activity_index_text_courseTab:
                mTabCourse.setSelected(true);
                if (courseListFragment == null) {
                    courseListFragment = new CourseListFragment();
                    transaction.add(R.id.activity_index_fragment_container, courseListFragment);
                } else {
                    transaction.show(courseListFragment);
                    courseListFragment.returnToTop();
                }
                break;
            case R.id.activity_index_text_discoverTab:
                mTabDiscover.setSelected(true);
                if (discoverFragment == null) {
                    discoverFragment = new DiscoverFragment();
                    transaction.add(R.id.activity_index_fragment_container, discoverFragment);
                } else {
                    transaction.show(discoverFragment);
                }
                break;
            case R.id.activity_index_text_userTab:
                mTabUser.setSelected(true);
                if (userFragment == null) {
                    userFragment = new UserFragment();
                    transaction.add(R.id.activity_index_fragment_container, userFragment);
                } else {
                    transaction.show(userFragment);
                }
                break;
        }
        transaction.commit();
    }

    //清除导航栏选中标记
    private void clearSelectedMark() {
        mTabCourse.setSelected(false);
        mTabDiscover.setSelected(false);
        mTabUser.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction) {
        if (courseListFragment != null) {
            transaction.hide(courseListFragment);
        }
        if (discoverFragment != null) {
            transaction.hide(discoverFragment);
        }
        if (userFragment != null) {
            transaction.hide(userFragment);
        }
    }

    //监听返回
    @Override
    public void onBackPressed() {
        if (isSearching) {
            exitSearchingMode();
            return;
        }
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - mLastBackPressed) > mBackPressThreshold) {
            mPressBackToast.show();
            mLastBackPressed = currentTime;
        } else {
            mPressBackToast.cancel();
            finish();
        }
    }


    public void startHiddingToolbar() {
//先清除其他动画
        if (showElementAnimatorSet != null && showElementAnimatorSet.isRunning()) {
            showElementAnimatorSet.cancel();
        }
        if (hideElementAnimatorSet != null && hideElementAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            hideElementAnimatorSet = new AnimatorSet();
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(mSearchLayout, "translationY", mSearchLayout.getTranslationY(), -mSearchLayout.getHeight());//将ToolBar隐藏到上面
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(footerBar, "translationY", footerBar.getTranslationY(), footerBar.getHeight());//将Button隐藏到下面
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            animators.add(footerAnimator);
            hideElementAnimatorSet.setDuration(200);
            hideElementAnimatorSet.playTogether(animators);
            hideElementAnimatorSet.start();
        }
    }

    public void startShowingToolbar() {
        //先清除其他动画
        if (hideElementAnimatorSet != null && hideElementAnimatorSet.isRunning()) {
            hideElementAnimatorSet.cancel();
        }
        if (showElementAnimatorSet != null && showElementAnimatorSet.isRunning()) {
            //如果这个动画已经在运行了，就不管它
        } else {
            showElementAnimatorSet = new AnimatorSet();
            //下面两句是将头尾元素放回初始位置。
            ObjectAnimator headerAnimator = ObjectAnimator.ofFloat(mSearchLayout, "translationY", mSearchLayout.getTranslationY(), 0f);
            ObjectAnimator footerAnimator = ObjectAnimator.ofFloat(footerBar, "translationY", footerBar.getTranslationY(), 0f);
            ArrayList<Animator> animators = new ArrayList<>();
            animators.add(headerAnimator);
            animators.add(footerAnimator);
            showElementAnimatorSet.setDuration(300);
            showElementAnimatorSet.playTogether(animators);
            showElementAnimatorSet.start();
        }
    }
}
