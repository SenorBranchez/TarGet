package com.target.kremwolf.target;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import java.lang.annotation.Target;
import java.util.Arrays;

public class ShootingActivity extends AbstractBluetoothActivity {

    private TargetBtService btService;
    private TargetCanvas targetCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting);

        //btService = getBtService();

        SurfaceView surface = (SurfaceView) findViewById(R.id.target_canvas);

        surface.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

                targetCanvas = new TargetCanvas(holder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @Override
    protected void onNewMessage(String data) {

        String[] parts = data.split("\\,");

        Log.i("Hit", Arrays.toString(parts));
        targetCanvas.drawHit(Float.valueOf(parts[0]), Float.valueOf(parts[1]));
    }

}
