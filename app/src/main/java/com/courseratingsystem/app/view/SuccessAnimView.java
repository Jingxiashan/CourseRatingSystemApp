package com.courseratingsystem.app.view;

import android.content.Context;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.courseratingsystem.app.R;
import com.mcxtzhang.pathanimlib.PathAnimView;
import com.mcxtzhang.pathanimlib.StoreHouseAnimView;
import com.mcxtzhang.pathanimlib.utils.SvgPathParser;

import java.text.ParseException;

/**
 * Created by kongx on 2017/8/5 0005.
 */

public class SuccessAnimView extends RelativeLayout {
    PathAnimView animView;

    private SuccessAnimView(Context context) {
        super(context);
    }

    public SuccessAnimView(Context context, BgColor color) {
        super(context);
        animView = new StoreHouseAnimView(context);

        //设置颜色
        int int_color;
        switch (color) {
            case DARK:
                int_color = context.getResources().getColor(R.color.splashBackColor);
                break;
            default:
            case LIGHT:
                int_color = context.getResources().getColor(R.color.pureWhite);
                break;
        }
        //设置路径
        SvgPathParser svgPathParser = new SvgPathParser();
        String part_1, part_2;
        part_1 = context.getResources().getString(R.string.path_success_part_1);
        part_2 = context.getResources().getString(R.string.path_success_part_2);
//        String pathString = context.getResources().getString(R.string.path_loading);
        Path path = null;
        try {
            path = svgPathParser.parsePath(part_1 + part_2);
            animView.setSourcePath(path);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //设置动画效果
        animView
                .setColorFg(getResources().getColor(R.color.finishGreenLight))
                .setColorBg(getResources().getColor(R.color.lightGrey))
                .setAnimTime(2000)
                .setAnimInfinite(false);

        //居中显示
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int animWidth = (int) ((60.0 / 320.0) * screenWidth);
        int animHeight = (int) ((60.0 / 480.0) * screenHeight);
        int marginStart = (screenWidth - animWidth) / 2;
        int marginTop = (screenHeight - animHeight) / 2;

        RelativeLayout.LayoutParams layoutParams = new LayoutParams(animWidth, animHeight);
        layoutParams.setMargins(marginStart, marginTop, marginStart, marginTop);
        animView.setLayoutParams(layoutParams);

        //设置Layout属性
        ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(layoutParams1);
        this.setGravity(CENTER_IN_PARENT);
        this.setBackgroundColor(int_color);
        this.addView(animView);

        //运行动画
        startAnim();
    }

    void startAnim() {
        animView.startAnim();
    }

    public enum BgColor {DARK, LIGHT}
}
