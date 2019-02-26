    package com.bulygin.nikita.healthapp.data;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import com.bulygin.nikita.healthapp.data.db.TremorDao;
import com.bulygin.nikita.healthapp.data.db.TremorEventEntity;

    public class TremorListener implements SensorEventListener {

    private final TremorDao mDao;
    private long mShakeTimestamp = 0;
    private int mShakeCount;
    private Context context;
    private float sensibility = 1.2F;
    private long interval = 2000;
    private int expectCount = 3;


    public TremorListener(Context context, TremorDao dao) {
        this(context, 1.2F, 2000, 3,dao);
    }

    public TremorListener(Context context, float sensibility, long interval, int expectCount,TremorDao dao) {
        this.mDao = dao;
        this.sensibility = sensibility;
        this.context = context;
        this.interval = interval;
        this.expectCount = expectCount;
    }

    public void resetShakeCount() {
        this.mShakeCount = 0;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        float gX = x / 9.80665F;
        float gY = y / 9.80665F;
        float gZ = z / 9.80665F;
        //Модуль вектора
        float gForce = (float) Math.sqrt((double) (gX * gX + gY * gY + gZ * gZ));
        if (gForce > this.sensibility) {
            Log.d("LISTENER", "force: " + gForce + " count: " + this.mShakeCount);
            long now = System.currentTimeMillis();
            if (this.mShakeTimestamp + 500L > now) {
                return;
            }

            if (this.mShakeTimestamp + interval < now) {
                this.mShakeCount = 0;
            }

            this.mShakeTimestamp = now;
            ++this.mShakeCount;
            if (expectCount == this.mShakeCount) {
                saveAction(now,gForce,mShakeCount);
                mShakeCount = 0;
            }
        }

    }

    private void saveAction(long now, float gForce, int mShakeCount) {
        mDao.insert(new TremorEventEntity(null,now,gForce,mShakeCount));
    }


}