// clase elemento de la stepsBar para hacerlo mas reutilizable
// (si cada elemento es un objeto, si sabemos cuantos pasos queremos, podemos crear la cantidad
// de pasos en la barra que necesitamos y no que sea hardcoded.

package com.example.stepper.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.stepper.R;

public class StepsBarElement extends View {
    private Paint paintInsideCircText;
    private Paint paintCircle;
    private Paint paintStepTitleText;

    // si el elemento esta activo o no (not setup yet).
    public boolean active = false;

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
        paintCircle.setColor(Color.BLACK);
        paintCircle.setAlpha(97);

        paintInsideCircText = new Paint();
        paintInsideCircText.setAntiAlias(true);
        paintInsideCircText.setStyle(Paint.Style.FILL);
        paintInsideCircText.setTextSize(getResources().getDimension(R.dimen.innerCircleFontSize));

        paintStepTitleText = new Paint();
        paintStepTitleText.setAntiAlias(true);
        paintStepTitleText.setStyle(Paint.Style.FILL);
        paintStepTitleText.setTextSize(getResources().getDimension(R.dimen.stepTitleInactiveFontSize));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Float cx, cy;
        cx = getWidth() / 2f;
        cy = getHeight() / 2f;

        float radius = getResources().getDimensionPixelSize(R.dimen.circleRadius);

        canvas.drawCircle(cx, cy, radius, paintCircle);

        canvas.drawText("1", cx - paintInsideCircText.measureText("1") / 2, cy + paintInsideCircText.getTextSize() / 2 , paintInsideCircText);

        canvas.drawText("title", cx - paintStepTitleText.measureText("title") / 2, cy + radius + paintStepTitleText.getTextSize(), paintStepTitleText);
    }
}
