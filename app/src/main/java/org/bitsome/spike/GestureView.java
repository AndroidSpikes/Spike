package org.bitsome.spike;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class GestureView extends View {

    private Paint paint;
    private int drag = -1;
    private float centerX = 0F;
    private float centerY = 0F;
    private float previousX, previousY;
    private boolean clockwise = true;

    public GestureView(Context context) {
        this(context, null);
        setup();
    }

    public GestureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setup();
    }

    public GestureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    private void setup() {
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    drag = 0;
                    invalidate();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    drag = -1;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    clockwise = isClockwise(centerX, centerY, event.getX(), event.getY());
                    previousX = event.getX();
                    previousY = event.getY();
                    drag = 1;
                    invalidate();
                }
                return true;
            }
        });
    }

    /*********************************************************************************
    * http://gamedev.stackexchange.com/questions/22133/how-to-detect-if-object-is-moving-in-clockwise-or-counterclockwise-direction
    *        A
    *        |\         // A = Rotation Center
    *        | C        // B = Previous Frame Position
    *        B          // C = Current Frame Position
    *
    *        bool isLeft(Vector2 a, Vector2 b, Vector2 c) {
    *            return ((b.x - a.x)*(c.y - a.y) - (b.y - a.y)*(c.x - a.x)) > 0;
    *        }
    ********************************************************************************/
    private boolean isClockwise(float centerX, float centerY, float x, float y) {
        return ((previousX - centerX)*(y - centerY) - (previousY - centerY)*(x - centerX)) > 0;
    }

    @Override protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        if (paint == null) {
            paint = new Paint();
            paint.setTextSize(160);
            paint.setStrokeWidth(6);
            centerX = (float)getWidth()/2F;
            centerY = ((float)getHeight()/8F)*5F;
        }

        if (drag<=0) {
            paint.setColor(Color.parseColor("#aaaaff"));
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
            paint.setColor(Color.parseColor("#5555ff"));
        } else {
            if (clockwise) {
                paint.setColor(Color.parseColor("#c8df8d"));
                canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
                paint.setColor(Color.parseColor("#4f6f1a"));
            } else {
                paint.setColor(Color.parseColor("#e2b6b3"));
                canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
                paint.setColor(Color.parseColor("#b32318"));
            }
            canvas.drawCircle(previousX, previousY, 40F, paint);
            canvas.drawLine(previousX, previousY, centerX, centerY, paint);
            String text = clockwise ? "Clockwise" : "Counter";
            float w = paint.measureText(text);
            canvas.drawText(text, (getWidth() - w) / 2, getHeight() / 4, paint);
        }
//        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, 40f, paint);
    }
}
