package com.gilasak.larzeafzar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.os.Process;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Locale;

public class GraphActivityComplete extends AppCompatActivity {

    RelativeLayout activity_graph_complete;

    GraphView graph2;
    GraphView graphViewCompare1;
    GraphView graphViewCompare2;

    Graph graphComplete;
    Graph graphCompare1;
    Graph graphCompare2;

    public boolean runFlag = false;

    public ArrayList<DataPoint> mseries1ArrayList;
    public ArrayList<DataPoint> mseries2ArrayList;
    public ArrayList<DataPoint> mseries3ArrayList;
    public ArrayList<DataPoint> mseries1BarArrayList;

    TextView graphToolBarTextview;
    Button buttonStartStopSampling;
    Button buttonResetComplete;
    Button buttonSaveComplete;
    Button buttonLoadComplete;
    TextView textViewSampling;
    SeekBar thresholdSeekBaarGraphView;
    TextView thresholdLabelTextViewGraphView;
    Toolbar graphViewToolBar;
    DrawerLayout graphViewDrawerLayout;
    NavigationView graphViewNavigationView;
    ProgressBar progressBarSaveFileWait;
    LinearLayout linearLayoutControlsGraphComplete;
    LinearLayout linearLayoutSeekControlComplete;
    LinearLayout linearLayoutLoadGraphComplete;
    LinearLayout relativeLayoutGraphComplete;
    //save operation
    EditText saveFileNameEditText;
    TextInputLayout textInputLayoutEntrySaveFile;
    Dialog dialogSave;
    Dialog dialogResult;
    TextView dialogSaveOkResultTextView;
    Button dialogSaveOkResultButton;
    //
    //GraphCompare Elements
    //
    LinearLayout linearLayoutGraphCompareMainStart;
    LinearLayout linearLayoutGraphCompare;
    LinearLayout graphCompare1StartControlLayout;
    LinearLayout graphCompare1TimeControlLayout;
    LinearLayout graphCompare2StartControlLayout;
    LinearLayout graphCompare2TimeControlLayout;
    ImageView buttonImageCompareTimeDecrease;
    ImageView buttonImageCompareTimeIncrease;
    ImageView buttonImageCompareVibrationReduceDecrease;
    ImageView buttonImageCompareVibrationReduceIncrease;
    TextView textViewgraphCompareTime;
    TextView textViewgraphCompareVibrationReduce;
    Button graphCompareMainStartButton;
    Button graphCompareStartButton1;
    Button graphCompareStartButton2;
    TextView textViewGraphCompare1Time;
    TextView textViewGraphCompare2Time;
    //
    //
    int counter = 3;

    int compareTime = 6;
    int compareTimeTmp = 6;

    String strIntent = "";

    String unitStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_complete);

        activity_graph_complete = (RelativeLayout) findViewById(R.id.activity_graph_complete);

        graphViewToolBar = (Toolbar) findViewById(R.id.graphViewToolBar);
        //add setting hamburger
        setSupportActionBar(graphViewToolBar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        //
        //GraphCompare Elements
        //
        linearLayoutGraphCompareMainStart = (LinearLayout) findViewById(R.id.linearLayoutGraphCompareMainStart);
        linearLayoutGraphCompare = (LinearLayout) findViewById(R.id.linearLayoutGraphCompare);
        graphCompare1StartControlLayout = (LinearLayout) findViewById(R.id.graphCompare1StartControlLayout);
        graphCompare1TimeControlLayout = (LinearLayout) findViewById(R.id.graphCompare1TimeControlLayout);
        graphCompare2StartControlLayout = (LinearLayout) findViewById(R.id.graphCompare2StartControlLayout);
        graphCompare2TimeControlLayout = (LinearLayout) findViewById(R.id.graphCompare2TimeControlLayout);

        buttonImageCompareTimeDecrease = (ImageView) findViewById(R.id.buttonImageCompareTimeDecrease);
        buttonImageCompareTimeIncrease = (ImageView) findViewById(R.id.buttonImageCompareTimeIncrease);
        buttonImageCompareVibrationReduceDecrease = (ImageView) findViewById(R.id.buttonImageCompareVibrationReduceDecrease);
        buttonImageCompareVibrationReduceIncrease = (ImageView) findViewById(R.id.buttonImageCompareVibrationReduceIncrease);
        graphCompareStartButton1 = (Button) findViewById(R.id.graphCompareStartButton1);
        graphCompareStartButton2 = (Button) findViewById(R.id.graphCompareStartButton2);
        textViewgraphCompareTime = (TextView) findViewById(R.id.textViewgraphCompareTime);
        textViewgraphCompareVibrationReduce = (TextView) findViewById(R.id.textViewgraphCompareVibrationReduce);
        graphCompareMainStartButton = (Button) findViewById(R.id.graphCompareMainStartButton);
        textViewGraphCompare1Time = (TextView) findViewById(R.id.textViewGraphCompare1Time);
        textViewGraphCompare2Time = (TextView) findViewById(R.id.textViewGraphCompare2Time);
        //
        //
        graphToolBarTextview = (TextView) findViewById(R.id.graphToolBarTextview);
        relativeLayoutGraphComplete = (LinearLayout) findViewById(R.id.relativeLayoutGraphComplete);
        graphViewNavigationView = (NavigationView) findViewById(R.id.graphViewNavigationView);
        graphViewDrawerLayout = (DrawerLayout) findViewById(R.id.graphViewDrawerLayout);
        textViewSampling = (TextView) findViewById(R.id.textViewSampling);
        buttonStartStopSampling = (Button) findViewById(R.id.buttonStartStopSampling);
        buttonResetComplete = (Button) findViewById(R.id.buttonResetComplete);
        buttonSaveComplete = (Button) findViewById(R.id.buttonSaveComplete);
        progressBarSaveFileWait = (ProgressBar) findViewById(R.id.progressBarSaveFilewait);
        linearLayoutControlsGraphComplete = (LinearLayout) findViewById(R.id.linearLayoutControlsGraphComplete);
        linearLayoutSeekControlComplete = (LinearLayout) findViewById(R.id.linearLayoutSeekControlComplete);
        linearLayoutLoadGraphComplete = (LinearLayout) findViewById(R.id.linearLayoutLoadGraphComplete);
        buttonLoadComplete = (Button) findViewById(R.id.buttonLoadComplete);
        thresholdSeekBaarGraphView = (SeekBar) findViewById(R.id.thresholdSeekBaarGraphView);
        thresholdLabelTextViewGraphView = (TextView) findViewById(R.id.thresholdLabelTextViewGraphView);
        thresholdSeekBaarGraphView.setProgress(Graph.ACC_THRESHOLD);

        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/A.Pixel.2.ttf");
        graphToolBarTextview.setTypeface(tf1);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        unitStr = prefs.getString("sensorUnit", unitStr);

        strIntent = getIntent().getExtras().getString("TYPE");
        if (strIntent.equals("GraphComplete")) {
            relativeLayoutGraphComplete.setVisibility(View.VISIBLE);
            linearLayoutControlsGraphComplete.setVisibility(View.VISIBLE);
            linearLayoutSeekControlComplete.setVisibility(View.VISIBLE);
            linearLayoutLoadGraphComplete.setVisibility(View.GONE);
            linearLayoutGraphCompareMainStart.setVisibility(View.GONE);
            linearLayoutGraphCompare.setVisibility(View.GONE);

            graph2 = (GraphView) findViewById(R.id.graphComplete);
            graphComplete = new Graph(getApplicationContext(), unitStr);
            graphComplete.setGraphAccess(graph2);
            graphComplete.graphInit();
            graphComplete.setViewPort();

            thresholdLabelTextViewGraphView.setText(String.format(Locale.US, "%.4f", graphComplete.limit));

        } else if (strIntent.equals("Review")) {
            relativeLayoutGraphComplete.setVisibility(View.VISIBLE);
            linearLayoutControlsGraphComplete.setVisibility(View.GONE);
            linearLayoutSeekControlComplete.setVisibility(View.GONE);
            linearLayoutLoadGraphComplete.setVisibility(View.VISIBLE);
            linearLayoutGraphCompareMainStart.setVisibility(View.GONE);
            linearLayoutGraphCompare.setVisibility(View.GONE);

            graph2 = (GraphView) findViewById(R.id.graphComplete);
            graphComplete = new Graph(getApplicationContext(), unitStr);
            graphComplete.setGraphAccess(graph2);
            graphComplete.graphInit();
            graphComplete.setViewPort();

            String strIntentLoadFileName = getIntent().getExtras().getString("FILE");
            if (strIntentLoadFileName != null) {
                //
                // start async
                //
                new LongOperationLoad().execute(strIntentLoadFileName);


            }
        } else if (strIntent.equals("Compare")) {
            relativeLayoutGraphComplete.setVisibility(View.GONE);
            linearLayoutControlsGraphComplete.setVisibility(View.GONE);
            linearLayoutSeekControlComplete.setVisibility(View.GONE);
            linearLayoutLoadGraphComplete.setVisibility(View.GONE);
            linearLayoutGraphCompareMainStart.setVisibility(View.VISIBLE);
            linearLayoutGraphCompare.setVisibility(View.VISIBLE);
            graphCompare1TimeControlLayout.setVisibility(View.GONE);
            graphCompare2TimeControlLayout.setVisibility(View.GONE);


            graphViewCompare1 = (GraphView) findViewById(R.id.graphCompare1);
            graphCompare1 = new Graph(getApplicationContext(), unitStr);
            graphCompare1.setGraphAccess(graphViewCompare1);
            graphCompare1.graphInit();
            graphCompare1.setViewPort();

            graphViewCompare2 = (GraphView) findViewById(R.id.graphCompare2);
            graphCompare2 = new Graph(getApplicationContext(), unitStr);
            graphCompare2.setGraphAccess(graphViewCompare2);
            graphCompare2.graphInit();
            graphCompare2.setViewPort();

            double vibrate = Double.parseDouble(textViewgraphCompareVibrationReduce.getText().toString());
            vibrateReduceTextManipulater(vibrate * graphCompare2.unitScale);

        }


        thresholdSeekBaarGraphView.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                graphComplete.setLimit(i / 10000f);
                thresholdLabelTextViewGraphView.setText(String.format(Locale.US, "%.4f", graphComplete.limit));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        buttonStartStopSampling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!runFlag) {
                    textViewSampling.setText(String.valueOf(counter));
                    textViewSampling.setVisibility(View.VISIBLE);
                    if (counter == 3) {
                        final Handler h = new Handler();
                        final int delay = 1000; //milliseconds

                        h.postDelayed(new Runnable() {
                            public void run() {
                                //do something
                                --counter;
                                textViewSampling.setText(String.valueOf(counter));
                                if (counter == 0) {
                                    textViewSampling.setText(getResources().getString(R.string.samplingStartText));
                                    h.postDelayed(this, delay);
                                } else if (counter == -1) {
                                    counter = 3;
                                    textViewSampling.setVisibility(View.GONE);
                                    buttonStartStopSampling.setText(getResources().getString(R.string.graphViewCompleteStopSamplingButton));
                                    h.removeCallbacksAndMessages(null);
                                    //start sampling
                                    //
                                    //

                                    //mHandler.postDelayed(mTimer2, 1000);

                                    runFlag = true;

                                    graphComplete.sampling();

                                    //
                                    //
                                } else {
                                    h.postDelayed(this, delay);
                                }
                            }
                        }, delay);
                    }
                } else {
                    //stop sampling
                    //
                    //
                    buttonStartStopSampling.setText(getResources().getString(R.string.graphViewCompleteStartSamplingButton));
                    graphComplete.removeCallBacks();
                    runFlag = false;
                    //
                    //
                }
            }
        });


        MenuItem switchItemX = graphViewNavigationView.getMenu().findItem(R.id.graphViewNavigationAxisXSwitch);
        final CompoundButton switchViewX = (CompoundButton) MenuItemCompat.getActionView(switchItemX);
        switchViewX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (strIntent.equals("Compare")) {
                    graphCompare1.showXseries(isChecked);
                    graphCompare2.showXseries(isChecked);
                } else {
                    graphComplete.showXseries(isChecked);
                }
            }
        });

        MenuItem switchItemY = graphViewNavigationView.getMenu().findItem(R.id.graphViewNavigationAxisYSwitch);
        final CompoundButton switchViewY = (CompoundButton) MenuItemCompat.getActionView(switchItemY);
        switchViewY.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (strIntent.equals("Compare")) {
                    graphCompare1.showYseries(isChecked);
                    graphCompare2.showYseries(isChecked);
                } else {
                    graphComplete.showYseries(isChecked);
                }
            }
        });

        MenuItem switchItemZ = graphViewNavigationView.getMenu().findItem(R.id.graphViewNavigationAxisZSwitch);
        final CompoundButton switchViewZ = (CompoundButton) MenuItemCompat.getActionView(switchItemZ);
        switchViewZ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (strIntent.equals("Compare")) {
                    graphCompare1.showZseries(isChecked);
                    graphCompare2.showZseries(isChecked);
                } else {
                    graphComplete.showZseries(isChecked);
                }
            }
        });

        MenuItem switchItemBar = graphViewNavigationView.getMenu().findItem(R.id.graphViewNavigationAxisBarSwitch);
        final CompoundButton switchViewBar = (CompoundButton) MenuItemCompat.getActionView(switchItemBar);
        switchViewBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (strIntent.equals("Compare")) {
                    graphCompare1.showBarseries(isChecked);
                    graphCompare2.showBarseries(isChecked);
                } else {
                    graphComplete.showBarseries(isChecked);
                }
            }
        });

        MenuItem switchItemLegend = graphViewNavigationView.getMenu().findItem(R.id.graphViewNavigationLegendSwitch);
        CompoundButton switchViewLegend = (CompoundButton) MenuItemCompat.getActionView(switchItemLegend);
        switchViewLegend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (strIntent.equals("Compare")) {
                    graphCompare1.showLegend(isChecked);
                    graphCompare2.showLegend(isChecked);
                } else {
                    graphComplete.showLegend(isChecked);
                }
            }
        });

        buttonResetComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //stop sampling
                //
                //
                buttonStartStopSampling.setText(getResources().getString(R.string.graphViewCompleteStartSamplingButton));
                graphComplete.removeCallBacks();
                runFlag = false;
                //
                //

                graphComplete.removeAllSeries();

                graphComplete.graphInit();

                graphComplete.reset();

                //check if it has to show every series or not based on setting

                if (!switchViewX.isChecked()) {
                    graphComplete.showXseries(false);
                }
                if (!switchViewY.isChecked()) {
                    graphComplete.showYseries(false);
                }
                if (!switchViewZ.isChecked()) {
                    graphComplete.showZseries(false);
                }
                if (!switchViewBar.isChecked()) {
                    graphComplete.showBarseries(false);
                }


            }
        });

        buttonSaveComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (runFlag) {
                    //stop sampling
                    //
                    //
                    buttonStartStopSampling.setText(getResources().getString(R.string.graphViewCompleteStartSamplingButton));
                    graphComplete.removeCallBacks();
                    runFlag = false;
                    //
                    //
                }

                dialogSave = new Dialog(GraphActivityComplete.this);
                dialogSave.setCancelable(false);
                dialogSave.setContentView(R.layout.dialog_save_graph_complete);
                //dialog.getWindow().getAttributes().width = WindowManager.LayoutParams.MATCH_PARENT;

                Button dialogSaveSAveButton = (Button) dialogSave.findViewById(R.id.dialogSaveSAveButton);
                Button dialogSaveCancelButton = (Button) dialogSave.findViewById(R.id.dialogSaveCancelButton);
                textInputLayoutEntrySaveFile = (TextInputLayout) dialogSave.findViewById(R.id.textInputLayoutEntrySaveFile);
                saveFileNameEditText = (EditText) dialogSave.findViewById(R.id.saveFileNameEditText);

                dialogSaveSAveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        textInputLayoutEntrySaveFile.setErrorEnabled(false);

                        //save file
                        /*GraphIO graphIO = new GraphIO(getApplicationContext());
                        GraphIOResult graphIOResult = new GraphIOResult();
                        graphIOResult = graphIO.save(saveFileNameEditText.getText().toString(),
                                graphComplete.mSeries1.getValues(0, graphComplete.graph2LastXValue),
                                graphComplete.mSeries2.getValues(0, graphComplete.graph2LastXValue),
                                graphComplete.mSeries3.getValues(0, graphComplete.graph2LastXValue),
                                graphComplete.mSeries1Bar.getValues(0, graphComplete.graph2LastXValue),
                                progressBarSaveFileWait);

                        if (graphIOResult.result) {

                            dialogSave.dismiss();
                            final Dialog dialogResult = new Dialog(GraphActivityComplete.this);
                            dialogResult.setCancelable(false);
                            dialogResult.setContentView(R.layout.dialog_save_graph_result);

                            TextView dialogSaveOkResultTextView = (TextView) dialogResult.findViewById(R.id.dialogSaveOkResultTextView);
                            Button dialogSaveOkResultButton = (Button) dialogResult.findViewById(R.id.dialogSaveOkResultButton);
                            dialogSaveOkResultTextView.setText(graphIOResult.message);

                            dialogSaveOkResultButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialogResult.dismiss();
                                }
                            });

                            dialogResult.show();
                        } else {
                            textInputLayoutEntrySaveFile.setErrorEnabled(true);
                            textInputLayoutEntrySaveFile.setError(graphIOResult.message);
                        }*/

                        new LongOperationSave().execute(saveFileNameEditText.getText().toString());

                    }
                });

                dialogSaveCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogSave.dismiss();
                    }
                });

                dialogSave.show();
            }
        });

        buttonLoadComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GraphActivityComplete.this, GraphFileLoadActivity.class);
                startActivity(intent);
                finish();

            }
        });

        buttonImageCompareTimeDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int time = Integer.parseInt(textViewgraphCompareTime.getText().toString());
                if (time > 0) {
                    --time;
                }
                textViewgraphCompareTime.setText(String.valueOf(time));
            }
        });

        buttonImageCompareTimeIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int time = Integer.parseInt(textViewgraphCompareTime.getText().toString());
                if (time < 30) {
                    ++time;
                }
                textViewgraphCompareTime.setText(String.valueOf(time));
            }
        });

        buttonImageCompareVibrationReduceDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double vibrate = Double.parseDouble(textViewgraphCompareVibrationReduce.getText().toString());
                if (vibrate > 0.0) {
                    vibrate -= 0.001 * graphCompare2.unitScale;
                    if(vibrate < 0.0){
                        vibrate = 0.0;
                    }
                }

                vibrateReduceTextManipulater(vibrate);

            }
        });

        buttonImageCompareVibrationReduceIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double vibrate = Double.parseDouble(textViewgraphCompareVibrationReduce.getText().toString());
                if (vibrate < 0.05 * graphCompare2.unitScale) {
                    vibrate += 0.001 * graphCompare2.unitScale;
                }

                vibrateReduceTextManipulater(vibrate);

            }
        });

        graphCompareMainStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayoutGraphCompareMainStart.setVisibility(View.GONE);
                linearLayoutGraphCompare.setVisibility(View.VISIBLE);

                compareTime = Integer.parseInt(textViewgraphCompareTime.getText().toString());
                compareTimeTmp = compareTime;

                float setLimit = Float.parseFloat(textViewgraphCompareVibrationReduce.getText().toString());
                graphCompare1.setLimit(setLimit);
                graphCompare2.setLimit(setLimit);
            }
        });

        graphCompareStartButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graphCompareStartButton2.setEnabled(false);

                graphCompare1StartControlLayout.setVisibility(View.GONE);
                graphCompare1TimeControlLayout.setVisibility(View.VISIBLE);


                textViewGraphCompare1Time.setText(String.valueOf(counter));
                textViewGraphCompare1Time.setVisibility(View.VISIBLE);

                if (counter == 3) {
                    final Handler h = new Handler();
                    final int delay = 1000; //milliseconds

                    h.postDelayed(new Runnable() {
                        public void run() {
                            //do something
                            --counter;
                            if (counter == 0) {
                                textViewGraphCompare1Time.setText(getResources().getString(R.string.samplingStartText));
                                h.postDelayed(this, delay);
                            } else if (counter == -1) {
                                counter = 3;
                                //textViewGraphCompare1Time.setVisibility(View.GONE);
                                h.removeCallbacksAndMessages(null);
                                //start sampling
                                //
                                //

                                //mHandler.postDelayed(mTimer2, 1000);

                                graphCompare1.sampling();

                                compareTimeTmp = compareTime;

                                final Handler hTime = new Handler();
                                final int delayTime = 1000; //milliseconds
                                textViewGraphCompare1Time.setText(String.valueOf(compareTimeTmp));
                                hTime.postDelayed(new Runnable() {
                                    public void run() {
                                        if (compareTimeTmp > 1) {

                                            --compareTimeTmp;
                                            textViewGraphCompare1Time.setText(String.valueOf(compareTimeTmp));
                                            hTime.postDelayed(this, delayTime);
                                        } else if (compareTimeTmp == 1) {

                                            --compareTimeTmp;
                                            textViewGraphCompare1Time.setText(getResources().getString(R.string.samplingEndText));
                                            graphCompare1.removeCallBacks();
                                            hTime.postDelayed(this, delayTime);
                                        } else if (compareTimeTmp == 0) {

                                            --compareTimeTmp;
                                            hTime.postDelayed(this, delayTime);
                                        } else if (compareTimeTmp == -1) {
                                            --compareTimeTmp;
                                            textViewGraphCompare1Time.setVisibility(View.GONE);
                                            graphCompare1TimeControlLayout.setVisibility(View.GONE);
                                            hTime.removeCallbacksAndMessages(null);
                                            graphCompareStartButton2.setEnabled(true);
                                        }
                                    }

                                }, delayTime + 1000);

                                //
                                //
                            } else {
                                textViewGraphCompare1Time.setText(String.valueOf(counter));
                                h.postDelayed(this, delay);
                            }
                        }
                    }, delay);
                }


            }
        });

        graphCompareStartButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                graphCompareStartButton1.setEnabled(false);

                graphCompare2StartControlLayout.setVisibility(View.GONE);
                graphCompare2TimeControlLayout.setVisibility(View.VISIBLE);


                textViewGraphCompare2Time.setText(String.valueOf(counter));
                textViewGraphCompare2Time.setVisibility(View.VISIBLE);

                if (counter == 3) {
                    final Handler h = new Handler();
                    final int delay = 1000; //milliseconds

                    h.postDelayed(new Runnable() {
                        public void run() {
                            //do something
                            --counter;
                            if (counter == 0) {
                                textViewGraphCompare2Time.setText(getResources().getString(R.string.samplingStartText));
                                h.postDelayed(this, delay);
                            } else if (counter == -1) {
                                counter = 3;
                                //textViewGraphCompare2Time.setVisibility(View.GONE);
                                h.removeCallbacksAndMessages(null);
                                //start sampling
                                //
                                //

                                //mHandler.postDelayed(mTimer2, 1000);

                                graphCompare2.sampling();

                                compareTimeTmp = compareTime;

                                final Handler hTime = new Handler();
                                final int delayTime = 1000; //milliseconds
                                textViewGraphCompare2Time.setText(String.valueOf(compareTimeTmp));
                                hTime.postDelayed(new Runnable() {
                                    public void run() {
                                        if (compareTimeTmp > 1) {

                                            --compareTimeTmp;
                                            textViewGraphCompare2Time.setText(String.valueOf(compareTimeTmp));
                                            hTime.postDelayed(this, delayTime);
                                        } else if (compareTimeTmp == 1) {

                                            --compareTimeTmp;
                                            textViewGraphCompare2Time.setText(getResources().getString(R.string.samplingEndText));
                                            graphCompare2.removeCallBacks();
                                            hTime.postDelayed(this, delayTime);
                                        } else if (compareTimeTmp == 0) {

                                            --compareTimeTmp;
                                            hTime.postDelayed(this, delayTime);
                                        } else if (compareTimeTmp == -1) {
                                            --compareTimeTmp;
                                            textViewGraphCompare2Time.setVisibility(View.GONE);
                                            graphCompare2TimeControlLayout.setVisibility(View.GONE);
                                            hTime.removeCallbacksAndMessages(null);
                                            graphCompareStartButton1.setEnabled(true);
                                        }
                                    }

                                }, delayTime + 1000);

                                //
                                //
                            } else {
                                textViewGraphCompare2Time.setText(String.valueOf(counter));
                                h.postDelayed(this, delay);
                            }
                        }
                    }, delay);
                }


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();
        // handle arrow click here
        if (id == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (id == R.id.navigationMenuAction) {
            if (!graphViewDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                graphViewDrawerLayout.openDrawer(Gravity.RIGHT);
            } else {
                graphViewDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            sensorManager.registerListener(sensorEventListener,
                    accelerometerSensor,
                    SensorManager.SENSOR_DELAY_FASTEST);

        } else {
            sensorUnavailableInformer();
        }

        //keep screen awake
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    public void onPause() {
        if (runFlag) {
            //stop sampling
            //
            //
            buttonStartStopSampling.setText(getResources().getString(R.string.graphViewCompleteStartSamplingButton));
            if (!strIntent.equals("Compare")) {
                graphComplete.removeCallBacks();
            }
            runFlag = false;
            //
            //
        }
        if (!strIntent.equals("Compare")) {
            graphComplete.removeCallBacks();
        }
        super.onPause();
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null) {
            sensorManager.unregisterListener(sensorEventListener, accelerometerSensor);
        }
        //let screen to turn off
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {


            if (strIntent.equals("Compare")) {
                graphCompare1.onSensorChanged(event);
                graphCompare2.onSensorChanged(event);
            } else {
                graphComplete.onSensorChanged(event);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onBackPressed() {
        if (graphViewDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            graphViewDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
            return;
        }
    }


    public void vibrateReduceTextManipulater(double vibrate){
        if(unitStr.equals("MS2")){
            textViewgraphCompareVibrationReduce.setText(String.format(Locale.US, "%.3f", vibrate));
        }else if(unitStr.equals("Gal")){
            textViewgraphCompareVibrationReduce.setText(String.format(Locale.US, "%.1f", vibrate));
        }else if(unitStr.equals("FTS2")){
            textViewgraphCompareVibrationReduce.setText(String.format(Locale.US, "%.3f", vibrate));
        }else if(unitStr.equals("ST")){
            textViewgraphCompareVibrationReduce.setText(String.format(Locale.US, "%.6f", vibrate));
        }
    }


    public void sensorUnavailableInformer() {
        Snackbar snackbar = Snackbar
                .make(activity_graph_complete, getResources().getString(R.string.accSensorNotAvailble), Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.menuConnectionUpdateInformerButton), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //
                    }
                });


        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textViewSnackText = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textViewSnackText.setTextSize(20);
        // set text to center
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            textViewSnackText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            textViewSnackText.setGravity(Gravity.CENTER_HORIZONTAL);


        TextView textViewSnackAction = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
        textViewSnackAction.setTextSize(20);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            textViewSnackAction.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            textViewSnackAction.setGravity(Gravity.CENTER_HORIZONTAL);
        textViewSnackAction.setBackgroundColor(getResources().getColor(R.color.greenButton));

        snackbar.setActionTextColor(getResources().getColor(R.color.menuConnectionSnackBarActionColor));

        snackbar.show();
    }


    private class LongOperationLoad extends AsyncTask<String, Void, GraphIOResult> {

        @Override
        protected final GraphIOResult doInBackground(String... params) {

            //make async a little bit faster
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE);
            //Process.setThreadPriority(Process.THREAD_PRIORITY_FOREGROUND);

            GraphIO graphIO = new GraphIO(getApplicationContext());
            GraphIOResult graphIOResult = new GraphIOResult();
            mseries1ArrayList = new ArrayList<DataPoint>();
            mseries2ArrayList = new ArrayList<DataPoint>();
            mseries3ArrayList = new ArrayList<DataPoint>();
            mseries1BarArrayList = new ArrayList<DataPoint>();
            graphIOResult = graphIO.load(params[0], mseries1ArrayList, mseries2ArrayList, mseries3ArrayList, mseries1BarArrayList);


            return graphIOResult;
        }

        @Override
        protected void onPostExecute(GraphIOResult graphIOResult) {

            progressBarSaveFileWait.setVisibility(View.GONE);

            if (!graphIOResult.result) {
                //
                // show error
                //
                textViewSampling.setText(graphIOResult.message);
                textViewSampling.setVisibility(View.VISIBLE);
            } else {
                textViewSampling.setVisibility(View.GONE);

                graphComplete.removeAllSeries();
                graphComplete.applyUnit(graphIOResult.unit);
                graphComplete.graphInit();
                graphComplete.setViewPort();
                graphComplete.applyLoad(mseries1ArrayList,
                        mseries2ArrayList,
                        mseries3ArrayList,
                        mseries1BarArrayList);
            }

        }

        @Override
        protected void onPreExecute() {
            progressBarSaveFileWait.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private class LongOperationSave extends AsyncTask<String, Void, GraphIOResult> {

        @Override
        protected final GraphIOResult doInBackground(String... params) {

            //make async a little bit faster
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND + Process.THREAD_PRIORITY_MORE_FAVORABLE);
            //Process.setThreadPriority(Process.THREAD_PRIORITY_FOREGROUND);

            GraphIO graphIO = new GraphIO(getApplicationContext());
            GraphIOResult graphIOResult = new GraphIOResult();
            graphIOResult = graphIO.save(params[0],
                    unitStr,
                    graphComplete.mSeries1.getValues(0, graphComplete.graph2LastXValue),
                    graphComplete.mSeries2.getValues(0, graphComplete.graph2LastXValue),
                    graphComplete.mSeries3.getValues(0, graphComplete.graph2LastXValue),
                    graphComplete.mSeries1Bar.getValues(0, graphComplete.graph2LastXValue));

            return graphIOResult;
        }

        @Override
        protected void onPostExecute(GraphIOResult graphIOResult) {

            progressBarSaveFileWait.setVisibility(View.GONE);


            if (graphIOResult.result) {

                dialogSave.dismiss();
                dialogResult = new Dialog(GraphActivityComplete.this);
                dialogResult.setCancelable(false);
                dialogResult.setContentView(R.layout.dialog_save_graph_result);

                dialogSaveOkResultTextView = (TextView) dialogResult.findViewById(R.id.dialogSaveOkResultTextView);
                dialogSaveOkResultButton = (Button) dialogResult.findViewById(R.id.dialogSaveOkResultButton);
                dialogSaveOkResultTextView.setText(graphIOResult.message);

                dialogSaveOkResultButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogResult.dismiss();
                    }
                });

                dialogResult.show();
            } else {
                dialogSave.show();
                textInputLayoutEntrySaveFile.setErrorEnabled(true);
                textInputLayoutEntrySaveFile.setError(graphIOResult.message);
            }

        }

        @Override
        protected void onPreExecute() {
            progressBarSaveFileWait.setVisibility(View.VISIBLE);
            dialogSave.hide();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
