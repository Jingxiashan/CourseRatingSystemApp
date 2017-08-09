package com.courseratingsystem.app.view;

import android.content.Context;
import android.util.AttributeSet;

import com.courseratingsystem.app.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by panda on 2017/8/7.
 */

public class CustomizedHorizontalBarChart extends HorizontalBarChart{
    private float[] scores={0,0,0,0,0};
    private int bar_color= 0xffffa500;
    private int value_color = 0xffffa500;
    private float bar_width = 0.9f;
    private boolean value_above_bar = true;
    private Context mContext;

    public CustomizedHorizontalBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomizedHorizontalBarChart(Context context){
        super(context);
    }

    public CustomizedHorizontalBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(float[] dataset){
        scores = dataset;
        initChart();
    }
    public void setScores(float s1,float s2,float s3,float s4,float s5){
        scores[0] = s1;
        scores[2] = s2;
        scores[3] = s3;
        scores[4] = s4;
        scores[5] = s5;
    }
    public void setBar_color(int bar_color){
        this.bar_color = bar_color;
    }
    public void setValue_color(int value_color){
        this.value_color = value_color;
    }
    public void setBar_width(float bar_width){
        this.bar_width=bar_width;
    }
    public void setValue_above_bar(boolean value_above_bar){
        this.value_above_bar=value_above_bar;
    }

    private void initChart() {
        BarEntry e1 = new BarEntry(0, scores[0]);
        BarEntry e2 = new BarEntry(1, scores[1]);
        BarEntry e3 = new BarEntry(2, scores[2]);
        BarEntry e4 = new BarEntry(3, scores[3]);
        BarEntry e5 = new BarEntry(4, scores[4]);
        ArrayList<BarEntry> vals = new ArrayList<>();
        vals.add(e1);
        vals.add(e2);
        vals.add(e3);
        vals.add(e4);
        vals.add(e5);
        BarDataSet dataSet = new BarDataSet(vals, "scores");
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        dataSet.setColor(bar_color);
        dataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                // write your logic here
                DecimalFormat mFormat;
                mFormat = new DecimalFormat("0.0"); // use one decimal
                return mFormat.format(value); // e.g. append a dollar-sign
            }
        });
        ArrayList<String> xvals = new ArrayList<String>();
        xvals.add("M1");
        xvals.add("M2");
        xvals.add("M3");
        xvals.add("M4");
        xvals.add("M5");
        BarData data = new BarData(dataSets);
        data.setBarWidth(bar_width);
        data.setValueTextSize(20f);
        data.setValueTextColor(value_color);
        this.setData(data);
        this.invalidate();
        //设置相关属性
        this.setTouchEnabled(false);
        this.setDrawBarShadow(false);
        this.setDrawValueAboveBar(value_above_bar);
        this.getDescription().setEnabled(false);
        this.setPinchZoom(false);
        this.setDrawGridBackground(false);

        //x轴
        XAxis xl = this.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);

        xl.setValueFormatter(new IndexAxisValueFormatter(new String[]{"点名少否", "给分高否", "占时少否", "上课爽否", "内容好否"}));
        xl.setTextSize(15);

        //y轴
        YAxis yl = this.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f);
        yl.setDrawLabels(false);

        //y轴
        YAxis yr = this.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);
        yr.setDrawLabels(false);

        //设置数据
        this.setFitBars(true);
        this.animateY(1800);

        Legend l = this.getLegend();
        l.setEnabled(false);
    }


}
