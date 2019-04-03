/*
  Éste es un stepper horizontal reutilizable, en este caso cuenta con 4 pasos pero fácilmente se le pueden agregar o quitar
  simplemente crear una clase java y layout, agregar el caso en el switch statement. (si necesita se puede agregar parámentros
  en el switch, como en el caso de los ejemplos, pero sí es sólo un layout informativo solo bastaría con agregar el switch case)
  a su vez hay que agregar o eliminar un círculo y título del Stepper_layout, que es el status bar del paso en el que estamos.

  IMPORTANTE:
  Estoy trabajando para implementar una stepsBar dinamica, que cree la cantidad de pasos de acuerdo la informacion que ingresemos.
  simplemente creando un string [] con los titulos o nombres de los steps crearia la cantidad que necesitamos.
  Para eso estoy implementando StepBarElement que seria basicamente el circulo con numero de paso y el titulo
  luego podria hacerse un loop sobre cada string en el array creando un elemento con ese titulo y actualizando el numero de paso

*/

package com.example.stepper;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stepper.views.StepCircle;

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

//    stepsBar elements
    private int[] circles = {R.id.circle_one, R.id.circle_two, R.id.circle_three, R.id.circle_four};
    private int[] texts = {R.id.text1, R.id.text2, R.id.text3, R.id.text4};

    private int currStep = 0;

    private StepOneFragment stepOneFragment;

    private Button backBtn;

//    first step data
    private HashMap<String,String> userData;
//    second step data
    private String stepTwoOptionChosen;

//    para probar stepBar dinamico
//    private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backBtn = findViewById(R.id.backBtn);

//        init first step
        stepOneFragment = new StepOneFragment();
        startFirstFragment(stepOneFragment);

/*        TODO: crear elemento del stepBar como prueba para implementar steps dinamicos
        relativeLayout = findViewById(R.id.relativeLayout);
        StepsBarElement stepBarElement = new StepsBarElement(this);
        stepBarElement.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        relativeLayout.addView(stepBarElement);

        prueba de 2 elementos:
        StepsBarElement stepsBarEl2 = new StepsBarElement(this);
        stepsBarEl2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
        relativeLayout.addView(stepsBarEl2); */

    }

//    iniciar el primer paso (cree un metodo para utilizarlo en el onCreate y cuando se necesita restartear la app, al final del proceso)
    private void startFirstFragment (Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.placeholder, fragment).commit();

        currStep = 0;
        updateStepsBar(currStep, circles, texts);
        backBtn.setVisibility(View.INVISIBLE);
    }

//    actualiza el fragment en uso al que corresponda
    private void nextStep(int currStep) {
        switch (currStep) {
            case 0:
                changeFragment(new StepOneFragment());
                break;
            case 1:
//                a partir del segundo paso se puede ver el botton "back"
                backBtn.setVisibility(View.VISIBLE);
                changeFragment(new StepTwoFragment());
                break;

            case 2:
//                agregando argumentos a la creacion del 3er paso, donde se ve el review de la informacion hasta ahora
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
//        luego de cambiar el fragmento actualizar la barra con el paso activo y los colores y tamaños segun MaterialDesign
        updateStepsBar(currStep, circles, texts);
    }

//    recibiendo la informacion del primer paso y guardandola en userData
    @Override
    public void firstStep (HashMap<String,String> userInfo) {
        if (userInfo != null) {
            userData = userInfo;
            nextStep(currStep += 1);
        }
    }

//    idem pero para el segundo paso
    @Override
    public void secondStep (String text) {
        stepTwoOptionChosen = text;
        nextStep(currStep += 1);
    }

//    recibiendo feedback del AlertDialog: continue o Back
    @Override
    public void thirdStep (String text) {
        if (text.equals(CONTINUE)) nextStep(currStep += 1);
        else if (text.equals(BACK)) restart();
    }

//    recibiendo feedback de la confirmacion de la orden, en este caso solo se puede volver a empezar
    @Override
    public void fourthStep (String text) {
        if (text.equals(DONE)) restart();
    }

/*    en caso de que se apriete el boton atras
        si estamos a partir del 2do paso retrocedemos al paso anterior
        en caso de estar en el primer paso siempre se hace invisible boton "back"
       el boton continue en el ultimo paso cambia a submit, si volvemos atras en esa instancia tiene que volver a decir continue */
    @Override
    public void onBackPressed() {
        if (currStep > 0) currStep--;
        if (currStep == 0) backBtn.setVisibility(View.INVISIBLE);
        if (currStep == 2) {
            Button continueBtn = findViewById(R.id.continueBtn);
            continueBtn.setText(R.string.button_continue);
        }

        updateStepsBar(currStep,circles,texts);

/*       y finalmente cuando retrocedemos queremos sacar del stack la ultima entry (pop)
         a menos que solo quede el paso 1, en ese caso se cierra la app (default on back pressed) */
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) getSupportFragmentManager().popBackStack();
        else super.onBackPressed();
    }

//    si apretamos back en el ultimo paso (donde ya se confirmo la orden volvemos a empezar)
    public void backBtn (View view) {
        if (currStep == 3) restart();
//        sino la misma funcionalidad que apretar atras
        else onBackPressed();
    }

/*   restart, to be used after finishing steps:
        eleminar los elementos del backstack
        eleminar el stepOne creado en onCreate ya que hasta esta altura guarda la informacion ingresada
        y creamos un StepOneFragment para que todos los inputs esten limpios */
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
//        para cada elemento del stepBar:
            StepCircle circle  = findViewById(circles[i]);
            TextView text = findViewById(texts[i]);

////          si el elemento ya paso:
            if (i < currStep) {
                text.setTextSize(12);
                text.setAlpha(0.38f);

            } else if (i == currStep) {
                circle.setCircleColor(getColor(R.color.colorPrimary));

                text.setAlpha(0.87f);
                text.setTextSize(14);
            } else {
//                MaterialDesign inactive circle 38% black
                circle.setCircleColor(Color.argb(96,0,0,0));

                text.setTextSize(12);
                text.setAlpha(0.38f);
            }
        }
    }
}


