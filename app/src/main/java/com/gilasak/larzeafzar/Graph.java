package com.gilasak.larzeafzar;


import android.content.Context;
import android.hardware.SensorEvent;
import android.os.Handler;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Locale;


public class Graph {

    Context context;
    GraphView graph2;
    String unit;

    public LineGraphSeries<DataPoint> mSeries1;
    public LineGraphSeries<DataPoint> mSeries2;
    public LineGraphSeries<DataPoint> mSeries3;
    public BarGraphSeries<DataPoint> mSeries1Bar;
    LineGraphSeries<DataPoint> unitSeries;

    private final Handler mHandler = new Handler();
    private Runnable mTimer2;

    public float[] last100X = new float[100];
    public float last100SumX = 0.0f;
    public float maxLast100X = 0.0f;
    public float last100AverageX = 0.0f;
    //
    public float[] last100Y = new float[100];
    public float last100SumY = 0.0f;
    public float maxLast100Y = 0.0f;
    public float last100AverageY = 0.0f;
    //
    public float[] last100Z = new float[100];
    public float last100SumZ = 0.0f;
    public float maxLast100Z = 0.0f;
    public float last100AverageZ = 0.0f;
    //
    public float[] last100Bar = new float[100];
    public float last100SumBar = 0.0f;
    public float maxLast100Bar = 0.0f;
    public float last100AverageBar = 0.0f;
    //

    public boolean last100FirstCycle = true;
    public int last100Counter = 0;

    public static int MaxX = 150;

    public double graph2LastXValue = 0d;

    public static float showItemx = 0.0f;
    public static float showItemy = 0.0f;
    public static float showItemz = 0.0f;
    public static float showItemzBar = 0.0f;

    float[] gravity = new float[3];
    final float alpha = 0.8f;

    public static final int ACC_THRESHOLD = 250;
    float limit = ACC_THRESHOLD / 10000f;

    float MS2toMS2 = 1.0f;
    float MS2toGal = 100.0f;
    float MS2toFTS2 = 3.28084f;
    float MS2toST = 0.101972f;
    float unitScale;

    public Graph(Context _context, String _unit) {
        this.context = _context;
        applyUnit(_unit);
    }

    public void applyUnit(String _unit) {
        this.unit = _unit;
        if (this.unit.equals("MS2")) {
            unitScale = MS2toMS2;
        } else if (this.unit.equals("Gal")) {
            unitScale = MS2toGal;
        } else if (this.unit.equals("FTS2")) {
            unitScale = MS2toFTS2;
        } else if (this.unit.equals("ST")) {
            unitScale = MS2toST;
        }
        limit *= unitScale;
    }

    public void setGraphAccess(GraphView _graph2) {
        this.graph2 = _graph2;
    }

    public void graphInit() {

        mSeries1Bar = new BarGraphSeries<>();
        setGraphTitles("Shock", 0.0d, 0.0d);
        //mSeries1Bar.setTitle("Shock (AVG = 0.000)(MAX = 0.000)");
        mSeries1Bar.setColor(context.getResources().getColor(R.color.graphSeries4Color));
        graph2.addSeries(mSeries1Bar);
        mSeries3 = new LineGraphSeries<>();
        setGraphTitles("Z", 0.0d, 0.0d);
        //mSeries3.setTitle("Z (AVG = 0.000)(MAX = 0.000)");
        mSeries3.setColor(context.getResources().getColor(R.color.graphSeries3Color));
        //mSeries3.setDrawBackground(true);
        //mSeries3.setBackgroundColor(context.getResources().getColor(R.color.graphSeries3ColorBelowBG));
        graph2.addSeries(mSeries3);
        mSeries2 = new LineGraphSeries<>();
        setGraphTitles("Y", 0.0d, 0.0d);
        //mSeries2.setTitle("Y (AVG = 0.000)(MAX = 0.000)");
        mSeries2.setColor(context.getResources().getColor(R.color.graphSeries2Color));
        //mSeries2.setDrawBackground(true);
        //mSeries2.setBackgroundColor(context.getResources().getColor(R.color.graphSeries2ColorBelowBG));
        graph2.addSeries(mSeries2);
        mSeries1 = new LineGraphSeries<>();
        setGraphTitles("X", 0.0d, 0.0d);
        //mSeries1.setTitle("X (AVG = 0.000)(MAX = 0.000)");
        mSeries1.setColor(context.getResources().getColor(R.color.graphSeries1Color));
        //mSeries1.setDrawBackground(true);
        //mSeries1.setBackgroundColor(context.getResources().getColor(R.color.graphSeries1ColorBelowBG));
        graph2.addSeries(mSeries1);

        //unit shows in Legend
        unitSeries = new LineGraphSeries<>();
        setUnit();
        unitSeries.setColor(context.getResources().getColor(R.color.graphSeriesUnitColor));
        graph2.addSeries(unitSeries);
    }

    public void setViewPort() {
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setMinX(0);
        graph2.getViewport().setMaxX(MaxX);

        graph2.getViewport().setYAxisBoundsManual(true);
        graph2.getViewport().setMinY(-0.4 * unitScale);
        graph2.getViewport().setMaxY(+0.4 * unitScale);
        graph2.getViewport().setScalableY(true);
        graph2.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph2.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.HORIZONTAL);
        graph2.getGridLabelRenderer().setVerticalLabelsColor(context.getResources().getColor(R.color.graphGridLabelColor));

        graph2.setBackgroundColor(context.getResources().getColor(R.color.graphBG));

        graph2.getLegendRenderer().setVisible(true);
        //graph2.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.BOTTOM);
        graph2.getLegendRenderer().setFixedPosition(0, 0);
        graph2.getLegendRenderer().setBackgroundColor(context.getResources().getColor(R.color.graphLegendBG));

    }

    public void setLimit(float _limit) {
        this.limit = _limit * unitScale;
    }

    public void showXseries(boolean isChecked) {
        if (isChecked) {
            graph2.addSeries(mSeries1);
            //make unit to be the last item
            showUnitSeries(false);
            showUnitSeries(true);
        } else {
            graph2.removeSeries(mSeries1);
        }
    }

    public void showYseries(boolean isChecked) {
        if (isChecked) {
            graph2.addSeries(mSeries2);
            //make unit to be the last item
            showUnitSeries(false);
            showUnitSeries(true);
        } else {
            graph2.removeSeries(mSeries2);
        }
    }

    public void showZseries(boolean isChecked) {
        if (isChecked) {
            graph2.addSeries(mSeries3);
            //make unit to be the last item
            showUnitSeries(false);
            showUnitSeries(true);
        } else {
            graph2.removeSeries(mSeries3);
        }
    }

    public void showBarseries(boolean isChecked) {
        if (isChecked) {
            graph2.addSeries(mSeries1Bar);
            //make unit to be the last item
            showUnitSeries(false);
            showUnitSeries(true);
        } else {
            graph2.removeSeries(mSeries1Bar);
        }
    }

    public void showUnitSeries(boolean isChecked) {
        if (isChecked) {
            graph2.addSeries(unitSeries);
        } else {
            graph2.removeSeries(unitSeries);
        }
    }

    public void showLegend(boolean isChecked) {
        if (isChecked) {
            graph2.getLegendRenderer().setVisible(true);
            //to take effect this is the way
            graph2.addSeries(mSeries1Bar);
            graph2.removeSeries(mSeries1Bar);
        } else {
            graph2.getLegendRenderer().setVisible(false);
            //to take effect this is the way
            graph2.addSeries(mSeries1Bar);
            graph2.removeSeries(mSeries1Bar);
        }
    }

    public void setUnit() {
        if (this.unit.equals("MS2")) {
            unitSeries.setTitle("UNIT : " + context.getResources().getString(R.string.accSensorUnitMS2));
        } else if (this.unit.equals("Gal")) {
            unitSeries.setTitle("UNIT : " + context.getResources().getString(R.string.accSensorUnitGal));
        } else if (this.unit.equals("FTS2")) {
            unitSeries.setTitle("UNIT : " + context.getResources().getString(R.string.accSensorUnitFTS2));
        } else if (this.unit.equals("ST")) {
            unitSeries.setTitle("UNIT : " + context.getResources().getString(R.string.accSensorUnitStandardGravity));
        }
    }

    public void setGraphTitles(String type, double avg, double maximum) {

        String title = type +
                " (AVG = " +
                String.format(Locale.US, "%.3f", avg) +
                ")(MAX = " +
                String.format(Locale.US, "%.3f", maximum) +
                ")";
        if (type.equals("X"))
            mSeries1.setTitle(title);
        else if (type.equals("Y"))
            mSeries2.setTitle(title);
        else if (type.equals("Z"))
            mSeries3.setTitle(title);
        else if (type.equals("Shock"))
            mSeries1Bar.setTitle(title);


        //mSeries1Bar.setTitle("X (AVG = " + String.valueOf(avg) + ")(MAX = " + String.valueOf(maximum) + ")");
    }


    public void sampling() {
        mTimer2 = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue += 1d;
                mSeries1.appendData(new DataPoint(graph2LastXValue, showItemx), true, MaxX * 100);
                mSeries2.appendData(new DataPoint(graph2LastXValue, showItemy), true, MaxX * 100);
                mSeries3.appendData(new DataPoint(graph2LastXValue, showItemz), true, MaxX * 100);
                mSeries1Bar.appendData(new DataPoint(graph2LastXValue, showItemzBar), true, MaxX * 100);


                //calculation max and average
                last100SumX -= last100X[last100Counter];
                last100X[last100Counter] = Math.abs(showItemx);
                last100SumX += last100X[last100Counter];
                //
                last100SumY -= last100Y[last100Counter];
                last100Y[last100Counter] = Math.abs(showItemy);
                last100SumY += last100Y[last100Counter];
                //
                last100SumZ -= last100Z[last100Counter];
                last100Z[last100Counter] = Math.abs(showItemz);
                last100SumZ += last100Z[last100Counter];
                //
                last100SumBar -= last100Bar[last100Counter];
                last100Bar[last100Counter] = Math.abs(showItemzBar);
                last100SumBar += last100Bar[last100Counter];
                //
                ++last100Counter;
                if (last100Counter == 100) {
                    last100Counter = 0;
                    last100FirstCycle = false;
                }
                maxLast100X = last100X[0];
                maxLast100Y = last100Y[0];
                maxLast100Z = last100Z[0];
                maxLast100Bar = last100Bar[0];
                if (last100FirstCycle) {
                    last100AverageX = last100SumX / last100Counter;
                    last100AverageY = last100SumY / last100Counter;
                    last100AverageZ = last100SumZ / last100Counter;
                    last100AverageBar = last100SumBar / last100Counter;


                    for (int i = 0; i < last100Counter; ++i) {
                        if (maxLast100X < last100X[i])
                            maxLast100X = last100X[i];
                        if (maxLast100Y < last100Y[i])
                            maxLast100Y = last100Y[i];
                        if (maxLast100Z < last100Z[i])
                            maxLast100Z = last100Z[i];
                        if (maxLast100Bar < last100Bar[i])
                            maxLast100Bar = last100Bar[i];
                    }
                } else {
                    last100AverageX = last100SumX / 100;
                    last100AverageY = last100SumY / 100;
                    last100AverageZ = last100SumZ / 100;
                    last100AverageBar = last100SumBar / 100;


                    for (int i = 0; i < 100; ++i) {
                        if (maxLast100X < last100X[i])
                            maxLast100X = last100X[i];
                        if (maxLast100Y < last100Y[i])
                            maxLast100Y = last100Y[i];
                        if (maxLast100Z < last100Z[i])
                            maxLast100Z = last100Z[i];
                        if (maxLast100Bar < last100Bar[i])
                            maxLast100Bar = last100Bar[i];
                    }
                }


                mSeries1.setTitle("X" + " (AVG = " + String.format(Locale.US, "%.3f", last100AverageX) + ")(MAX = " + String.format(Locale.US, "%.3f", maxLast100X) + ")");
                mSeries2.setTitle("Y" + " (AVG = " + String.format(Locale.US, "%.3f", last100AverageY) + ")(MAX = " + String.format(Locale.US, "%.3f", maxLast100Y) + ")");
                mSeries3.setTitle("Z" + " (AVG = " + String.format(Locale.US, "%.3f", last100AverageZ) + ")(MAX = " + String.format(Locale.US, "%.3f", maxLast100Z) + ")");
                mSeries1Bar.setTitle("Shock" + " (AVG = " + String.format(Locale.US, "%.3f", last100AverageBar) + ")(MAX = " + String.format(Locale.US, "%.3f", maxLast100Bar) + ")");


                //textViewDirection.setText(lastShowItemzBarDirection);

                                            /*if (firstRun) {
                                                showItemzBarLast = showItemzBar;
                                                firstRun = false;
                                            } else {
                                                if (showItemzBar == showItemzBarLast) {
                                                    ++lastCounter;
                                                    if (lastCounter > 5) {
                                                        showItemzBar = 0;
                                                        lastCounter = 0;
                                                    }
                                                } else {
                                                    showItemzBarLast = showItemzBar;
                                                }
                                            }*/
                mHandler.postDelayed(this, 50);

            }
        };
        mHandler.postDelayed(mTimer2, 1000);
    }

    public void reset() {

        graph2LastXValue = 0d;

        last100X = new float[100];
        last100SumX = 0.0f;
        maxLast100X = 0.0f;
        last100AverageX = 0.0f;
        //
        last100Y = new float[100];
        last100SumY = 0.0f;
        maxLast100Y = 0.0f;
        last100AverageY = 0.0f;
        //
        last100Z = new float[100];
        last100SumZ = 0.0f;
        maxLast100Z = 0.0f;
        last100AverageZ = 0.0f;
        //
        last100Bar = new float[100];
        last100SumBar = 0.0f;
        maxLast100Bar = 0.0f;
        last100AverageBar = 0.0f;
        //

        last100FirstCycle = true;
        last100Counter = 0;
    }

    public void removeCallBacks() {
        mHandler.removeCallbacks(mTimer2);
    }

    public void removeAllSeries() {
        graph2.removeAllSeries();
    }

    public void onSensorChanged(SensorEvent event) {
        /*float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if (first) {
                firstX = 0 - x;
                firstY = 0 - y;
                firstZ = 0 - z;
                first = false;
            }

            lastAcc = currentAcc;

            currentAcc = x * x + y * y + z * z;

            acc = currentAcc * (currentAcc - lastAcc);

            //if (acc > ACC_THRESHOLD) {
            if (acc > thresholdSeekBaarGraphAcc2.getProgress()) {
                //Toast.makeText(getApplicationContext(),String.valueOf(acc),Toast.LENGTH_SHORT).show();
                showItemx = x + firstX;
                if (showItemx > -0.025f && showItemx < 0.025f) {
                    showItemx = 0;
                }
                showItemy = y + firstY;
                if (showItemy > -0.025f && showItemy < 0.025f) {
                    showItemy = 0;
                }
                showItemz = z + firstZ;
                if (showItemz > -0.025f && showItemz < 0.025f) {
                    showItemz = 0;
                }
                showItemzBar = showItemz;
                if (showItemzBar < 0) {
                    showItemzBar = -showItemzBar;
                }
            } else {
                //showItemx = 0;
                //showItemy = 0;
                //showItemz = 0;
            }*/

        // Isolate the force of gravity with the low-pass filter.

        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

        showItemx = (event.values[0] - gravity[0]) * unitScale;
        if (showItemx > -limit && showItemx < limit) {
            showItemx = 0;
        }
        showItemy = (event.values[1] - gravity[1]) * unitScale;
        if (showItemy > -limit && showItemy < limit) {
            showItemy = 0;
        }
        showItemz = (event.values[2] - gravity[2]) * unitScale;
        if (showItemz > -limit && showItemz < limit) {
            showItemz = 0;
        }

        showItemzBar = Math.max(Math.abs(showItemx), Math.abs(showItemy));
        showItemzBar = Math.max(Math.abs(showItemz), showItemzBar);


        //showItemzBar = showItemx * showItemx + showItemy * showItemy + showItemz * showItemz;

            /*if(Math.abs(showItemx) > Math.abs(showItemy)){
                showItemzBar = Math.abs(showItemx) ;
                newShowItemzBarDirection = "X";
            }
            else {
                showItemzBar = Math.abs(showItemy);
                newShowItemzBarDirection = "Y";
            }

            if(Math.abs(showItemz) > showItemz){
                showItemzBar = Math.abs(showItemz);
                newShowItemzBarDirection = "Z";
            }

            if(showItemzBar < 0.025f){
                lastShowItemzBarDirection = newShowItemzBarDirection;
                showItemzBar = 0.0f;
            }*/
    }

    public void applyLoad(ArrayList<DataPoint> mseries1ArrayList,
                          ArrayList<DataPoint> mseries2ArrayList,
                          ArrayList<DataPoint> mseries3ArrayList,
                          ArrayList<DataPoint> mseries1BarArrayList) {
        double XValue = 0d;
        double YValue = 0d;
        double ZValue = 0d;
        double BarValue = 0d;

        double sumX = 0d;
        double sumY = 0d;
        double sumZ = 0d;
        double sumBar = 0d;

        double maximumX = 0d;
        double maximumY = 0d;
        double maximumZ = 0d;
        double maximumBar = 0d;

        int index = 0;


        for (index = 0; index < mseries1ArrayList.size(); ++index) {
            mSeries1.appendData(mseries1ArrayList.get(index), true, MaxX * 100);
            mSeries2.appendData(mseries2ArrayList.get(index), true, MaxX * 100);
            mSeries3.appendData(mseries3ArrayList.get(index), true, MaxX * 100);
            mSeries1Bar.appendData(mseries1BarArrayList.get(index), true, MaxX * 100);

            XValue = Math.abs(mseries1ArrayList.get(index).getY());
            YValue = Math.abs(mseries2ArrayList.get(index).getY());
            ZValue = Math.abs(mseries3ArrayList.get(index).getY());
            BarValue = Math.abs(mseries1BarArrayList.get(index).getY());


            sumX += XValue;
            sumY += YValue;
            sumZ += ZValue;
            sumBar += BarValue;

            if (maximumX < XValue) {
                maximumX = XValue;
            }
            if (maximumY < YValue) {
                maximumY = YValue;
            }
            if (maximumZ < ZValue) {
                maximumZ = ZValue;
            }
            if (maximumBar < BarValue) {
                maximumBar = BarValue;
            }

        }

        mSeries1.setTitle("X" + " (AVG = " + String.format(Locale.US, "%.3f", sumX / index) + ")(MAX = " + String.format(Locale.US, "%.3f", maximumX) + ")");
        mSeries2.setTitle("Y" + " (AVG = " + String.format(Locale.US, "%.3f", sumY / index) + ")(MAX = " + String.format(Locale.US, "%.3f", maximumY) + ")");
        mSeries3.setTitle("Z" + " (AVG = " + String.format(Locale.US, "%.3f", sumZ / index) + ")(MAX = " + String.format(Locale.US, "%.3f", maximumZ) + ")");
        mSeries1Bar.setTitle("Shock" + " (AVG = " + String.format(Locale.US, "%.3f", sumBar / index) + ")(MAX = " + String.format(Locale.US, "%.3f", maximumBar) + ")");

    }

}
