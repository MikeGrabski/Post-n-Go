package com.example.iosuser11.postonwall;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


public class MotionSensors implements SensorEventListener {
    SensorManager mSensorManager;
    private Sensor gameRotationVector;
    private Sensor rotationVector;
    private Sensor accelerometer;
    private Sensor geoRotationVector;
    float[] rotationMatrix;
    float[] speedXYZ;

    public MotionSensors(Activity activity) {

        mSensorManager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);

        gameRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        rotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        geoRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        mSensorManager.registerListener(this, gameRotationVector, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, rotationVector, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        speedXYZ = new float[3];
    }

    public float[] getRotationMatrix() {
        return rotationMatrix;
    }

      @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_GAME_ROTATION_VECTOR) {
            float[] values = sensorEvent.values.clone();
            rotationMatrix = new float[16];
            mSensorManager.getRotationMatrixFromVector(rotationMatrix, values);
        }
        else {
            return;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}