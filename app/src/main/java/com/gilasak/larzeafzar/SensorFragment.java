package com.gilasak.larzeafzar;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class SensorFragment extends Fragment {

    private static final int TIME_INTERVAL = 5000;
    private long mButtonPressed = 0;

    TextView accSensorSensivityTextView;
    Button accSensorSensivityButton;
    TextView accSensorAvailabilityTextView;
    LinearLayout accSensorAvailabilityLinearLayout;
    RadioGroup radioGroupAccUnit;
    RadioButton radioAccUnitMS2;
    RadioButton radioAccUnitGal;
    RadioButton radioAccUnitFTS2;
    RadioButton radioAccUnitStandardGravity;

    boolean calculateFlag = false;
    int calculateAmount = 0;
    boolean callFuncNeeded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View returnLayout = inflater.inflate(R.layout.fragment_sensor, container, false);

        if (((MainActivity) getActivity()).getSupportActionBar() != null) {
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        accSensorSensivityTextView = (TextView) returnLayout.findViewById(R.id.accSensorSensivityTextView);
        accSensorSensivityButton = (Button) returnLayout.findViewById(R.id.accSensorSensivityButton);
        accSensorAvailabilityTextView = (TextView) returnLayout.findViewById(R.id.accSensorAvailabilityTextView);
        accSensorAvailabilityLinearLayout = (LinearLayout) returnLayout.findViewById(R.id.accSensorAvailabilityLinearLayout);
        radioGroupAccUnit = (RadioGroup) returnLayout.findViewById(R.id.radioGroupAccUnit);
        radioAccUnitMS2 = (RadioButton) returnLayout.findViewById(R.id.radioAccUnitMS2);
        radioAccUnitGal = (RadioButton) returnLayout.findViewById(R.id.radioAccUnitGal);
        radioAccUnitFTS2 = (RadioButton) returnLayout.findViewById(R.id.radioAccUnitFTS2);
        radioAccUnitStandardGravity = (RadioButton) returnLayout.findViewById(R.id.radioAccUnitStandardGravity);

        accSensorSensivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateAmount = 0;
                mButtonPressed = System.currentTimeMillis();
                calculateFlag = true;
                callFuncNeeded = true;
                accSensorSensivityButton.setEnabled(false);
                accSensorSensivityButton.setTextColor(getResources().getColor(R.color.greenButtonDisabledText));
                accSensorSensivityButton.setText(getResources().getString(R.string.accSensorSensivityButtonCalculation));
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = prefs.edit();
        String unitStr = "";
        unitStr = prefs.getString("sensorUnit" , unitStr);
        if(unitStr.equals("MS2")){
            radioAccUnitMS2.setChecked(true);
        }else if(unitStr.equals("Gal")){
            radioAccUnitGal.setChecked(true);
        }else if(unitStr.equals("FTS2")){
            radioAccUnitFTS2.setChecked(true);
        }else if(unitStr.equals("ST")){
            radioAccUnitStandardGravity.setChecked(true);
        }

        radioGroupAccUnit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(radioAccUnitMS2.getId() == i){
                    editor.putString("sensorUnit" , "MS2");
                }else if(radioAccUnitGal.getId() == i){
                    editor.putString("sensorUnit" , "Gal");
                }
                else if(radioAccUnitFTS2.getId() == i){
                    editor.putString("sensorUnit" , "FTS2");
                }
                else if(radioAccUnitStandardGravity.getId() == i){
                    editor.putString("sensorUnit" , "ST");
                }
                editor.commit();
            }
        });

        return returnLayout;
    }

    @Override
    public void onResume() {
        super.onResume();

        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null)
        {
            sensorManager.registerListener(sensorEventListener,
                    accelerometerSensor,
                    SensorManager.SENSOR_DELAY_FASTEST);
            // Device has Accelerometer
            accSensorAvailabilityTextView.setText(getResources().getString(R.string.accSensorAvailble));
            accSensorAvailabilityLinearLayout.setBackgroundColor(getResources().getColor(R.color.greenButton));
        }
        else {
            accSensorAvailabilityTextView.setText(getResources().getString(R.string.accSensorNotAvailble));
            accSensorAvailabilityLinearLayout.setBackgroundColor(getResources().getColor(R.color.redButton));
            //disable sensivity checker button
            accSensorSensivityButton.setEnabled(false);
            accSensorSensivityButton.setTextColor(getResources().getColor(R.color.greenButtonDisabledText));
        }

        //keep screen awake
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onPause() {
        super.onPause();
        SensorManager sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor != null)
        {
            sensorManager.unregisterListener(sensorEventListener, accelerometerSensor);
        }
        //let screen to turn off
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            if(calculateFlag){
                if ( (mButtonPressed + TIME_INTERVAL >= System.currentTimeMillis())) {
                    ++calculateAmount;
                }
                else{
                    calculateFlag = false;
                    if(callFuncNeeded){
                        func();
                        callFuncNeeded = false;
                    }
                }
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void func(){
        accSensorSensivityTextView.setText(" " + calculateAmount/5);
        accSensorSensivityButton.setEnabled(true);
        accSensorSensivityButton.setTextColor(getResources().getColor(R.color.textWhite));
        accSensorSensivityButton.setText(getResources().getString(R.string.accSensorSensivityButtonText));
    }

}
