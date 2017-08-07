package com.courseratingsystem.app.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.courseratingsystem.app.R;
import com.courseratingsystem.app.view.CustomizedHorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;

@ContentView(R.layout.activity_course)
public class CourseActivity extends AppCompatActivity {
    @ViewInject(R.id.activity_course_horizontalbarchart)
    CustomizedHorizontalBarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        barChart.setBar_color(getResources().getColor(R.color.pureBlack));
        barChart.init(new float[]{1.1f,2.2f,3.3f,4.4f,4.9f});
//        setContentView(R.layout.activity_course);
//        BarEntry e1 = new BarEntry(0, 1.18f);
//        BarEntry e2 = new BarEntry(1, 2);
//        BarEntry e3 = new BarEntry(2, 3);
//        BarEntry e4 = new BarEntry(3, 4);
//        BarEntry e5 = new BarEntry(4, 5);
//        ArrayList<BarEntry> vals = new ArrayList<>();
//        vals.add(e1);
//        vals.add(e2);
//        vals.add(e3);
//        vals.add(e4);
//        vals.add(e5);
//        BarDataSet dataSet = new BarDataSet(vals, "scores");
//        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//        dataSets.add(dataSet);
//        dataSet.setDrawValues(true);
////        dataSet.setValueTextSize(10);
//        dataSet.setValueTextColor(getResources().getColor(R.color.pureBlack));
//        dataSet.setColor(0xffffa500);
//        dataSet.setValueFormatter(new IValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                // write your logic here
//                DecimalFormat mFormat;
//                mFormat = new DecimalFormat("0.0"); // use one decimal
//                return mFormat.format(value); // e.g. append a dollar-sign
//            }
//        });
//        ArrayList<String> xvals = new ArrayList<String>();
//        xvals.add("M1");
//        xvals.add("M2");
//        xvals.add("M3");
//        xvals.add("M4");
//        xvals.add("M5");
//        BarData data = new BarData(dataSets);
//        data.setBarWidth(0.9f);
//        data.setValueTextSize(20f);
//        data.setValueTextColor(getResources().getColor(R.color.pureBlack));
//        barchart.setData(data);
//        barchart.invalidate();
    }
//     class HorizontalBarChartBuilder {
//        private float[] scores={0,0,0,0,0};
//        private int bar_color= 0xffffa500;
//        private int value_color = getResources().getColor(R.color.pureBlack);
//        private float bar_width = 0.9f;
//        private boolean value_above_bar = true;
//        private HorizontalBarChart barchart;
//
//        public HorizontalBarChart getBarchart(){
//            return barchart;
//        }
//
//        public void setScores(float s1,float s2,float s3,float s4,float s5){
//            scores[0] = s1;
//            scores[2] = s2;
//            scores[3] = s3;
//            scores[4] = s4;
//            scores[5] = s5;
//        }
//        public void setBar_color(int bar_color){
//            this.bar_color = bar_color;
//        }
//        public void setValue_color(int value_color){
//            this.value_color = value_color;
//        }
//        public void setBar_width(float bar_width){
//            this.bar_width=bar_width;
//        }
//        public void setValue_above_bar(boolean value_above_bar){
//            this.value_above_bar=value_above_bar;
//        }
//
//        public void initChart() {
//            BarEntry e1 = new BarEntry(0, scores[0]);
//            BarEntry e2 = new BarEntry(1, scores[1]);
//            BarEntry e3 = new BarEntry(2, scores[2]);
//            BarEntry e4 = new BarEntry(3, scores[3]);
//            BarEntry e5 = new BarEntry(4, scores[4]);
//            ArrayList<BarEntry> vals = new ArrayList<>();
//            vals.add(e1);
//            vals.add(e2);
//            vals.add(e3);
//            vals.add(e4);
//            vals.add(e5);
//            BarDataSet dataSet = new BarDataSet(vals, "scores");
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//            dataSets.add(dataSet);
//            dataSet.setColor(bar_color);
//            dataSet.setValueFormatter(new IValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                    // write your logic here
//                    DecimalFormat mFormat;
//                    mFormat = new DecimalFormat("0.0"); // use one decimal
//                    return mFormat.format(value); // e.g. append a dollar-sign
//                }
//            });
//            ArrayList<String> xvals = new ArrayList<String>();
//            xvals.add("M1");
//            xvals.add("M2");
//            xvals.add("M3");
//            xvals.add("M4");
//            xvals.add("M5");
//            BarData data = new BarData(dataSets);
//            data.setBarWidth(bar_width);
//            data.setValueTextSize(20f);
//            data.setValueTextColor(value_color);
//            barchart.setData(data);
//            barchart.invalidate();
//            //设置相关属性
//            barchart.setTouchEnabled(false);
//            barchart.setDrawBarShadow(false);
//            barchart.setDrawValueAboveBar(value_above_bar);
//            barchart.getDescription().setEnabled(false);
//            barchart.setPinchZoom(false);
//            barchart.setDrawGridBackground(false);
//
//            //x轴
//            XAxis xl = barchart.getXAxis();
//            xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xl.setDrawAxisLine(false);
//            xl.setDrawGridLines(false);
//            xl.setValueFormatter(new IndexAxisValueFormatter(new String[]{"点名少否", "给分高否", "占时少否", "上课爽否", "内容好否"}));
//            xl.setTextSize(15);
//
//            //y轴
//            YAxis yl = barchart.getAxisLeft();
//            yl.setDrawAxisLine(false);
//            yl.setDrawGridLines(false);
//            yl.setAxisMinimum(0f);
//            yl.setDrawLabels(false);
//
//            //y轴
//            YAxis yr = barchart.getAxisRight();
//            yr.setDrawAxisLine(false);
//            yr.setDrawGridLines(false);
//            yr.setAxisMinimum(0f);
//            yr.setDrawLabels(false);
//
//            //设置数据
//            barchart.setFitBars(true);
//            barchart.animateY(1800);
//
//            Legend l = barchart.getLegend();
//            l.setEnabled(false);
//        }
//    }
}
