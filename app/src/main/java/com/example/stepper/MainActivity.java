package com.example.stepper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements StepOneFragment.OnItemSelectedListener, StepTwoFragment.OnItemSelectedListener, StepThreeFragment.OnItemSelectedListener, StepFourFragment.OnItemSelectedListener {
//   data management constants
    public static final String CONTINUE = "continue";
    public static final String DONE = "done";
    public static final String BACK = "back";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";
    public static final String ZIPCODE = "zipcode";
    public static final String DELIVERY = "delivery";

//    stepsBar:
    LinearLayout linearLayout;
    StepsNavBar stepsNavBar;
    private String[] stepsNames = {"info", "delivery", "review", "done"};

//    to init
    private int currStep = 0;
    private StepOneFragment stepOneFragment;
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

        linearLayout = findViewById(R.id.include);
        linearLayout.setOrientation(LinearLayoutCompat.HORIZONTAL);
        stepsNavBar = new StepsNavBar(this, linearLayout, stepsNames);
        stepsNavBar.create();

//        init first step
        stepOneFragment = new StepOneFragment();
        startFirstFragment(stepOneFragment);
    }

//    iniciar el primer paso (cree un metodo para utilizarlo en el onCreate y cuando se necesita restartear la app, al final del proceso)
    private void startFirstFragment (Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.placeholder, fragment).commit();

        currStep = 0;
        stepsNavBar.setActiveStep(currStep);
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

        stepsNavBar.setActiveStep(currStep);
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


    @Override
    public void onBackPressed() {
        if (currStep > 0 && currStep != 3) currStep--;
        if (currStep == 0) backBtn.setVisibility(View.INVISIBLE);
        if (currStep == stepsNames.length - 1) restart();

        stepsNavBar.setActiveStep(currStep);

//      si retrocedemos y no estamos en el primer paso (0) hace pop al backstack y sino retroceso normal
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count > 1) getSupportFragmentManager().popBackStack();
        else super.onBackPressed();
    }

//    si apretamos back en el ultimo paso (donde ya se confirmo la orden volvemos a empezar)
    public void backBtn (View view) {
        if (currStep == 3) restart();
        else onBackPressed();
    }

//     limpia backstack, elimina el stepOne creado y crea uno nuevo (limpia los inputs)
    private void restart() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportFragmentManager().beginTransaction().detach(stepOneFragment).commit();
        startFirstFragment(stepOneFragment = new StepOneFragment());
    }

//    change fragment Helper
    private void changeFragment (Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.placeholder, fragment).addToBackStack(null);
        ft.commit();
    }
}


