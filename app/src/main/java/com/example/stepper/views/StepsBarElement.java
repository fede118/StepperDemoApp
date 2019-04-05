//TODO:
// clase elemento de la stepsBar para hacerlo mas reutilizable
// (si cada elemento es un objeto, si sabemos cuantos pasos queremos, podemos crear la cantidad
// de pasos en la barra que necesitamos y no que sea hardcoded.

// todavia en desarrollo!!!

package com.example.stepper.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.stepper.R;

public class StepsBarElement extends View {
    private Paint paintInsideCircText;
    private Paint paintCircle;
    private Paint paintStepTitleText;
    private int circleColor;

    private String stepName = "";
    private int stepNumber;
    private float textSize;
    private int alpha;

    //Todo: si el elemento esta activo o no (not setup yet).
//    public boolean active = false;

    public StepsBarElement(Context context) {
        super(context);

        init(null);
    }

    public StepsBarElement(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    public StepsBarElement(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(attrs);
    }

    public StepsBarElement(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(attrs);
    }

//    alpha y size values de acuerdo a MaterialDesign (iniciando inactivos)
    private void init (@Nullable AttributeSet set) {
        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setAlpha(97);

        paintInsideCircText = new Paint();
        paintInsideCircText.setAntiAlias(true);
        paintInsideCircText.setStyle(Paint.Style.STROKE);
        paintInsideCircText.setTextSize(getResources().getDimension(R.dimen.innerCircleFontSize));

        paintStepTitleText = new Paint();
        paintStepTitleText.setAntiAlias(true);
        paintStepTitleText.setStyle(Paint.Style.STROKE);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_medium_ttf.ttf");
        paintStepTitleText.setTypeface(tf);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeigth = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(measureDimension(desiredWidth, widthMeasureSpec), measureDimension(desiredHeigth, heightMeasureSpec));
    }


    private int measureDimension (int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result,specSize);
            }
        }

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paintCircle.setColor(circleColor);
        paintStepTitleText.setTextSize(textSize);
        paintStepTitleText.setAlpha(alpha);

        float cx, cy;
        cx = getWidth() / 2f;
        cy = getHeight() / 2f;

        float radius = getResources().getDimensionPixelSize(R.dimen.circleRadius);

        canvas.drawCircle(cx, cy, radius, paintCircle);

        if (stepNumber != -1) {
            canvas.drawText(String.valueOf(stepNumber), cx - paintInsideCircText.measureText(String.valueOf(stepNumber)) / 2, cy + paintInsideCircText.getTextSize() / 2 , paintInsideCircText);
        }

        canvas.drawText(stepName, cx - paintStepTitleText.measureText(stepName) / 2, cy + radius + paintStepTitleText.getTextSize(), paintStepTitleText);
    }


//    public methods
    public void setStepName (String newName) {
        stepName = newName;
    }

    public void setStepNumber (int number) {
        stepNumber = number;
    }

    public void setCircleColor (int newColor) {
        circleColor = newColor;
        invalidate();
    }

    public void setTitleSize (float size) {
        textSize = size;
    }

    public void setTitleAlpha (int newAlpha) {
        alpha = newAlpha;
    }
}
