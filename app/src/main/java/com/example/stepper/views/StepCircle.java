// Custom view circulo con numero adentro que se muestra en la StepsBar
package com.example.stepper.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.stepper.R;

public class StepCircle extends View {
    private Paint paintInsideCircText;
    private Paint paintCircle;
    private String attrText;
    private int circleColor;

    public StepCircle(Context context) {
        super(context);

        init(null);
    }

    public StepCircle(Context context, AttributeSet attrs) {
        super(context, attrs);

        int[] set = {android.R.attr.text};

//        attributo text puesto en el layout.xml
        TypedArray attrArray = context.obtainStyledAttributes(attrs, set);
        CharSequence text = attrArray.getText(0);
        attrText = text.toString();
        attrArray.recycle();

        init(attrs);
    }

    public StepCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public StepCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

    public void init (@Nullable AttributeSet params) {
        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);

        paintInsideCircText = new Paint();
        paintInsideCircText.setAntiAlias(true);
        paintInsideCircText.setStyle(Paint.Style.FILL);
        paintInsideCircText.setTextSize(getResources().getDimension(R.dimen.innerCircleFontSize));
    }

    public void setCircleColor (int newColor) {
        circleColor = newColor;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintCircle.setColor(circleColor);

        Float cx, cy;
        cx = getWidth() / 2f;
        cy = getHeight() / 2f;

        float radius = getResources().getDimensionPixelSize(R.dimen.circleRadius);

        canvas.drawCircle(cx, cy, radius, paintCircle);

        canvas.drawText(attrText, cx - paintInsideCircText.measureText(attrText) / 2, cy + paintInsideCircText.getTextSize() / 2 , paintInsideCircText);
    }
}
