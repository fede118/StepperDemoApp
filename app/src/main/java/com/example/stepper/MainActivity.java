/*
  Stepper Creado por Federico Cano.
  Éste es un stepper horizontal reutilizable, en este caso cuenta con 4 pasos pero fácilmente se le pueden agregar o quitar
  simplemente crear una clase java y layout, agregar el caso en el switch statement. (si necesita se puede agregar parámentros
  en el switch, como en el caso de los ejemplos, pero sí es sólo un layout informativo solo bastaría con agregar el switch case)
  a su vez hay que agregar o eliminar un círculo y título del Stepper_layout, que es el status bar del paso en el que estamos.

  Pasos realizados:
  Primero analice la guia de material design y vi ejemplos de steppers,
  luego realice un bosquejo de como queria que se viera la app, una guia
  luego realice el layout de la status bar Stepper_layout
  luego comence a escribir los diferentes fragmentos con informacion dummy (a partir del segundo fragmento fui testeando la
  funcionalidad del boton "continue" y "back" al igual que el retroceso del celular=
  luego de tener los fragmentos y que los botones fueran hacia adelante y hacia atras cambiando el status bar
  agregué algo mas de elementos a todos los fragments y comunicación con la MainActivity para lograr interacción entre ellos
  Finalmente hice un breve periodo de pruba arreglando algunos bugs o implementando mejorias

  Las clases utilizadas son fragmentos por cada paso que se necesite con su respectivo layout (IMPORTANTE: he realizado en otra ocasión un solo fragmento
  que llamaba inflate a distintos layouts, minimizando bastante la cantidad de clases, pero al fin de ser reutilizable me parece mejor tener mas clases
  donde cada paso tiene su logica y para modificar un paso solo tomamos ese fragmento o ese layout)
  Los layouts utilizados como decía son los del statusBar y los de cada paso
  Los recursos en este caos decidi utilizar recursos predeterminados de android para los circulos indicadores de cada paso, los recursos de strings
  para el texto en botones que se repiten en cada fragment, etc.

  Al final del codigo la funcion helper updateStatusBar actualiza la informacion de la status bar de acuerdo al paso en el que nos encontramos
  cambiando el tamaño del texto y color al indicado en la guia de material design

  Soy conciente de que se pueden mejorar algunos aspectos con mas tiempo de trabajo (como hacerla aun mas reutilizable por ejemplo realizando
  la insercion de parametros dinámica y no hard coded, o que el stepper status bar tambien sea dinamico de acuerdo a los pasos que se necesiten),
  sin embargo pare acá siendo el tiempo una variable importante en esta situación.

*/

package com.example.stepper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements StepOneFragment.OnItemSelectedListener, StepTwoFragment.OnItemSelectedListener, StepThreeFragment.OnItemSelectedListener, StepFourFragment.OnItemSelectedListener {
//    Constants
    public static final String CONTINUE = "continue";
    public static final String DONE = "done";
    public static final String BACK = "back";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ZIPCODE = "zipcode";
    public static final String DELIVERY = "delivery";

    private int[] circles = {R.id.circle_one, R.id.circle_two, R.id.circle_three, R.id.circle_four};
    private int[] texts = {R.id.text1, R.id.text2, R.id.text3, R.id.text4};

    private int currStep = 0;

    StepOneFragment stepOneFragment;

    private Button backBtn;

//    first step data
    private HashMap<String,String> userData;
//    second step data
    private String stepTwoOptionChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backBtn = findViewById(R.id.backBtn);

//        init first step
        stepOneFragment = new StepOneFragment();
        startFirstFragment(stepOneFragment);
    }

    public void startFirstFragment (Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.placeholder, fragment).commit();

        currStep = 0;
        updateStepsBar(currStep, circles, texts);
        backBtn.setVisibility(View.INVISIBLE);
    }

    public void nextStep(int currStep) {
        switch (currStep) {
            case 0:
                changeFragment(new StepOneFragment());
                break;
            case 1:
                backBtn.setVisibility(View.VISIBLE);
                changeFragment(new StepTwoFragment());
                break;

            case 2:
                Bundle args = new Bundle();
                args.putString(NAME, userData.get(NAME));
                args.putString(EMAIL, userData.get(EMAIL));
                args.putString(PHONE, userData.get(PHONE));
                args.putString(ZIPCODE, userData.get(ZIPCODE));
                args.putString(DELIVERY, stepTwoOptionChosen);
                StepThreeFragment stepThreeFragment = new StepThreeFragment();
                stepThreeFragment.setArguments(args);

                changeFragment(stepThreeFragment);
                break;
            case 3:
                changeFragment(new StepFourFragment());
                break;
            default:

        }

        updateStepsBar(currStep, circles, texts);
    }

    @Override
    public void firstStep (HashMap<String,String> userInfo) {
        if (userInfo != null) {
            userData = userInfo;
            nextStep(currStep += 1);
        }
    }

    @Override
    public void secondStep (String text) {
        stepTwoOptionChosen = text;
        nextStep(currStep += 1);
    }

    @Override
    public void thirdStep (String text) {
        if (text.equals(CONTINUE)) nextStep(currStep += 1);
        else if (text.equals(BACK)) restart();
    }
    @Override
    public void fourthStep (String text) {
        if (text.equals(DONE)) restart();
    }

    @Override
    public void onBackPressed() {
        if (currStep > 0) currStep--;
        if (currStep == 0) backBtn.setVisibility(View.INVISIBLE);

        if (currStep == 2) {
            Button continueBtn = findViewById(R.id.continueBtn);
            continueBtn.setText(R.string.button_continue);
        }

        updateStepsBar(currStep,circles,texts);

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) getSupportFragmentManager().popBackStack();
        else super.onBackPressed();
    }

    public void backBtn (View view) {
        if (currStep == 3) restart();
        else onBackPressed();
    }

//    restart, to be used after finishing steps
    private void restart() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().detach(stepOneFragment).commit();

        startFirstFragment(new StepOneFragment());
    }

//    change fragment Helper
    private void changeFragment (Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, fragment).addToBackStack(null);
        ft.commit();
    }


//    setting size and color according to material design guide
    private void updateStepsBar (int currStep, int[] circles, int[] texts) {
        for (int i = 0; i < circles.length; i++) {

            View circle  = findViewById(circles[i]);
            TextView text = findViewById(texts[i]);

            if (i < currStep) {
                text.setTextSize(12);
                text.setAlpha(0.36f);
            } else if (i == currStep) {
                circle.setBackground(getResources().getDrawable(android.R.drawable.presence_online));

                text.setAlpha(0.87f);
                text.setTextSize(14);
            } else {
                circle.setBackground(getResources().getDrawable(android.R.drawable.presence_invisible));

                text.setTextSize(12);
                text.setAlpha(0.36f);
            }
        }
    }
}


