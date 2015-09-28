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
    private float previousX, previousY;

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
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float centerX = (float)getWidth()/2F;
                    float centerY = (float)getHeight()/2F;
                    C.log(isClockwise(centerX, centerY, event.getX(), event.getY()) ? "Clockwise" : "Counter-Clockwise");
                    previousX = event.getX();
                    previousY = event.getY();
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
        }
        paint.setColor(Color.BLUE);
        canvas.drawCircle(getWidth()/2, getHeight()/2, 20F, paint);
    }
}
