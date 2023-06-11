package com.example.smartkitchen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

public class KnobController extends View implements View.OnTouchListener {
    private static final float MIN_ANGLE = 0.0f;
    private static final float MAX_ANGLE = 360.0f;
    private float mAngle = 0;
    private OnKnobChangeListener mListener;
    private float knobSize;
    private float knobX;
    private float knobY;
    private final float[] mTickAngles = { MIN_ANGLE, MAX_ANGLE / 10, MAX_ANGLE / 5, MAX_ANGLE * 3 / 10,
                                    MAX_ANGLE * 2 / 5, MAX_ANGLE / 2, MAX_ANGLE * 3 / 5, MAX_ANGLE * 7 / 10,
                                    MAX_ANGLE * 4 / 5, MAX_ANGLE * 9 / 10};

    public KnobController(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public interface OnKnobChangeListener {
        void onKnobValueChanged(int value);
    }

    public void setOnKnobChangeListener(OnKnobChangeListener listener) {
        mListener = listener;
    }

    private float snapToTick(float angle) {
        float snapAngle = angle;
        float minDistance = Float.MAX_VALUE;

        for (float tickAngle : mTickAngles) {
            float distance = Math.abs(angle - tickAngle);
            if (distance < minDistance) {
                minDistance = distance;
                snapAngle = tickAngle;
            }
        }

        return snapAngle;
    }

    private float getAngle(float x, float y) {
        float cx = getWidth() / 2.0f;
        float cy = getHeight() / 2.0f;
        float dx = cx - x;
        float dy = cy - y;
        float radians = (float) Math.atan2(dx, dy);
        float angle = (float) Math.toDegrees(radians);
        angle -= 90.0f; // adjust to make 0 degrees the starting point
        if (angle < 0.0f) {
            angle += 360.0f; // ensure angle is positive
        }
        return angle;
    }

    private boolean insideKnob(float x, float y){
        float distance = (float) Math.sqrt(Math.pow((x - knobX), 2) + Math.pow((y - knobY), 2));
        return !(distance > knobSize / 2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float snapAngle;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Save the touch position
                float mTouchX = event.getX();
                float mTouchY = event.getY();
                if(insideKnob(mTouchX, mTouchY)){
                    float angle = getAngle(mTouchX, mTouchY);
                    snapAngle = snapToTick(angle);
                    if (snapAngle != mAngle) {
                        mAngle = snapAngle;
                        invalidate();
                        if (mListener != null) {
                            mListener.onKnobValueChanged((int) mAngle / 36);
                        }
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Define the size and position of the knob
        knobSize = Math.min(getWidth(), getHeight()) * 0.8f;
        knobX = getWidth() / 2.0f;
        knobY = getHeight() / 2.0f;

        // Define the paint for the knob
        Paint knobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(ContextCompat.getColor(getContext(), R.color.companyBlue));
        knobPaint.setStyle(Paint.Style.FILL);

        // Draw the knob on the canvas
        canvas.drawCircle(knobX, knobY, knobSize / 2, knobPaint);

        // Define the size and position of the ticks
        float tickRadius = knobSize / 2 - 50;

        Paint labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(Color.WHITE);
        labelPaint.setTextSize(60);
        labelPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        canvas.save();
        canvas.rotate(360-mAngle, knobX, knobY);
        // Draw the ticks and labels
        for (int i=mTickAngles.length-1; i>=0; i--)  {
            canvas.save();
            canvas.rotate(mTickAngles[i], knobX, knobY);
            canvas.drawText(Integer.toString(i), knobX - 18, knobY - tickRadius + 10, labelPaint);
            canvas.restore();
        }

        canvas.restore();
    }

    protected void setKnob(int pos){
        mAngle = mTickAngles[pos];
        invalidate();
        if (mListener != null) {
            mListener.onKnobValueChanged((int) mAngle / 36);
        }
    }
}
