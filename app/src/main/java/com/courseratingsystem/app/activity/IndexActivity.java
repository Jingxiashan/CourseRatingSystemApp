package com.courseratingsystem.app.activity;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
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

@ContentView(R.layout.activity_index)
public class IndexActivity extends AppCompatActivity {

    private static final long mBackPressThreshold = 3500;
    private long mLastBackPressed;

    @ViewInject(R.id.activity_index_input_search)
    private EditText mSearch;
    @ViewInject(R.id.activity_index_text_courseTab)
    private TextView mTabCourse;
    @ViewInject(R.id.activity_index_text_discoverTab)
    private TextView mTabDiscover;
    @ViewInject(R.id.activity_index_text_userTab)
    private TextView mTabUser;

    private Toast mPressBackToast;
    private CourseListFragment courseListFragment;
    private DiscoverFragment discoverFragment;
    private UserFragment userFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        onTabSelected(mTabCourse);
        mSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mSearch.clearFocus();
        mPressBackToast = Toast.makeText(this, getString(R.string.activity_index_toast_press_back_again_to_exit), Toast.LENGTH_SHORT);
    }

    //搜索栏回车键监听
    @Event(value = R.id.activity_index_input_search, type = TextView.OnEditorActionListener.class)
    private boolean onSearchKeyPressed(View view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH
                || actionId == EditorInfo.IME_ACTION_DONE
                || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
            String searchText = mSearch.getText().toString();
            searchForCourses(searchText);
        }
        return false;
    }

    //执行搜索
    private void searchForCourses(String searchText) {
        //TODO:联网执行搜索
    }

    //底部导航栏动作监听
    @Event(value = {R.id.activity_index_text_courseTab, R.id.activity_index_text_discoverTab, R.id.activity_index_text_userTab}, type = View.OnClickListener.class)
    private void onTabSelected(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
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
        long currentTime = System.currentTimeMillis();
        if (Math.abs(currentTime - mLastBackPressed) > mBackPressThreshold) {
            mPressBackToast.show();
            mLastBackPressed = currentTime;
        } else {
            mPressBackToast.cancel();
            finish();
        }
    }
}
