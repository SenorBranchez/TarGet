package com.target.kremwolf.target;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by kremwolf on 13.08.2015.
 */
public class TargetCanvas {

    private final Paint whiteFill = new Paint();
    private final Paint blackFill = new Paint();
    private final Paint blueFill = new Paint();
    private final Paint redFill = new Paint();
    private final Paint yellowFill = new Paint();

    private final Paint whiteStroke = new Paint();
    private final Paint blackStroke = new Paint();

    private final Paint hitmarkerFill = new Paint();

    private SurfaceHolder holder;
    private Canvas canvas;
    private float cx, cy;

    public TargetCanvas(SurfaceHolder holder) {

        this.holder = holder;
        this.canvas = holder.lockCanvas();

        cx = canvas.getWidth() / 2;
        cy = canvas.getHeight() / 2;

        hitmarkerFill.setColor(Color.GREEN);

        // Initialize Fills
        whiteFill.setStyle(Paint.Style.FILL);
        whiteFill.setColor(Color.WHITE);

        blackFill.setStyle(Paint.Style.FILL);
        blackFill.setColor(Color.BLACK);

        blueFill.setStyle(Paint.Style.FILL);
        blueFill.setColor(0xFF41b7c8);

        redFill.setStyle(Paint.Style.FILL);
        redFill.setColor(0xFFfd1b14);

        yellowFill.setStyle(Paint.Style.FILL);
        yellowFill.setColor(0xFFfff535);


        // Initialize Strokes
        whiteStroke.setStyle(Paint.Style.STROKE);
        whiteStroke.setColor(Color.WHITE);
        whiteStroke.setStrokeWidth(3);
        whiteStroke.setAntiAlias(true);

        blackStroke.setStyle(Paint.Style.STROKE);
        blackStroke.setColor(Color.BLACK);
        blackStroke.setStrokeWidth(3);
        blackStroke.setAntiAlias(true);

        // Draw target
        canvas.drawColor(Color.WHITE);

        float maxr = canvas.getHeight() / 2 / 10;

        // <circle cx="0" cy="0" r="39.9" fill="white" stroke="black" stroke-width="0.2" />
        drawSegment(maxr * 10 , whiteFill, blackStroke);

        //<circle cx="0" cy="0" r="35.9" fill="white" stroke="black" stroke-width="0.2" />
        drawSegment(maxr * 9, whiteFill, blackStroke);

        //<circle cx="0" cy="0" r="32"   fill="black" />
        drawSegment(maxr * 8, blackFill);

        //<circle cx="0" cy="0" r="27.9" fill="black" stroke="white" stroke-width = "0.2" />
        drawSegment(maxr * 7, blackFill, whiteStroke);

        //<circle cx="0" cy="0" r="24"   fill="#41b7c8" />
        drawSegment(maxr * 6, blueFill);

        //<circle cx="0" cy="0" r="19.9" fill="#41b7c8" stroke="black" stroke-width="0.2" />
        drawSegment(maxr * 5, blueFill, blackStroke);

        //<circle cx="0" cy="0" r="15.9" fill="#fd1b14" stroke="black" stroke-width="0.2" />
        drawSegment(maxr * 4, redFill, blackStroke);

        //<circle cx="0" cy="0" r="11.9" fill="#fd1b14" stroke="black" stroke-width="0.2" />
        drawSegment(maxr * 3, redFill, blackStroke);

        //<circle cx="0" cy="0" r="7.9" fill="#fff535" stroke="black" stroke-width="0.2" />
        drawSegment(maxr * 2, yellowFill, blackStroke);

        // <circle cx="0" cy="0" r="3.9" fill="#fff535" stroke="black" stroke-width="0.2" />
        drawSegment(maxr * 1, yellowFill, blackStroke);

        // <circle cx="0" cy="0" r="1.9" fill="#fff535" stroke="black" stroke-width="0.1" />
        drawSegment(maxr * 0.5f, yellowFill, blackStroke);

        holder.unlockCanvasAndPost(canvas);
    }


    private void drawSegment(float radius, Paint fill) {

        canvas.drawCircle(cx, cy, radius, fill);
    }

    private void drawSegment(float radius, Paint fill, Paint stroke) {

        canvas.drawCircle(cx, cy, radius, stroke);
        drawSegment(radius, fill);
    }

    public void drawHit(float x, float y) {

        holder.lockCanvas();

        canvas.drawCircle(canvas.getWidth() / 100 * x, canvas.getHeight() / 100 * y, 10, hitmarkerFill);

        holder.unlockCanvasAndPost(canvas);
    }
}

