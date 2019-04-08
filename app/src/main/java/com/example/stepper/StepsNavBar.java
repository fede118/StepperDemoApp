package com.example.stepper;

import android.content.Context;
import android.graphics.Color;

import android.view.View;
import android.widget.LinearLayout;

import com.example.stepper.views.StepsBarElement;

// package private
class StepsNavBar {
    private LinearLayout linearLayout;
    private String[] titles;
    private Context context;

    private static final int WIDTH = R.dimen.navBarElemWidtht;
    private static final int HEIGHT = R.dimen.navBarElemHeight;


    StepsNavBar(Context context, LinearLayout linearLayout, String[] stepsTitles) {
        this.titles = stepsTitles;
        this.linearLayout = linearLayout;
        this.context = context;
    }

//    crea un elemento con una linea entremedio por cada titulo en string[]
    void create() {
        int lineWidth = (int) context.getResources().getDimension(R.dimen.lineBetweenStepsLength);
        int lineheight = (int) context.getResources().getDimension(R.dimen.lineBetweenStepsHeight);
        if (titles.length > 4) {
            lineWidth -= lineWidth / 2;
        }
        for (int i = 0; i < titles.length; i++) {
            if (i > 0) {
                //linea entremedio
                View view = new View(context);

                view.setBackgroundColor(Color.argb(96,0,0,0));
                linearLayout.addView(view, lineWidth, lineheight);
            }

            StepsBarElement stepsBarElement = new StepsBarElement(context);
            int width = (int) context.getResources().getDimension(WIDTH);
            int height = (int) context.getResources().getDimension(HEIGHT);
            stepsBarElement.setStepName(titles[i]);
            stepsBarElement.setStepNumber(i + 1);
            linearLayout.addView(stepsBarElement, width, height);
        }
    }

    void setActiveStep(int index) {
//        index es el paso por el que vamos en main activity, este no va a coincidir con el child element porque tenemos las lineas entre objetos
        int currStep = 0;
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            StepsBarElement stepsBarElement = null;
            try {
                stepsBarElement = (StepsBarElement) linearLayout.getChildAt(i);
            } catch (Exception e) {
//                view wasnt a stepbar element but a the line between them.
            }
             if (stepsBarElement != null) {
                 if (currStep < index) {
                     stepsBarElement.setCircleColor(context.getColor(R.color.colorPrimary));
                     stepsBarElement.setTitleSize(context.getResources().getDimension(R.dimen.stepTitleInactiveFontSize));
                     stepsBarElement.setTitleAlpha(96);
                 } else if (currStep == index) {
                     stepsBarElement.setCircleColor(context.getColor(R.color.colorPrimary));
                     stepsBarElement.setTitleSize(context.getResources().getDimension(R.dimen.stepTitleActiveFontSize));
                     stepsBarElement.setTitleAlpha(221);
                 } else {
                     stepsBarElement.setCircleColor(Color.argb(96,0,0,0));
                     stepsBarElement.setTitleSize(context.getResources().getDimension(R.dimen.stepTitleInactiveFontSize));
                     stepsBarElement.setTitleAlpha(96);
                 }

                 currStep++; // currStep seria el paso por que vamos ya que solo lo aumentamos en los elementos que no sean la linea
             }
        }
    }
}
