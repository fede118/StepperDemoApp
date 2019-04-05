package com.example.stepper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class StepTwoFragment extends Fragment {

    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void secondStep (String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StepOneFragment.OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + R.string.fragment_class_exception);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_step_two, container, false);

        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);

//        idem step one, al apretar continue manda la opcion elegida a MainActivity
        Button continueBtn = view.findViewById(R.id.continueBtn2);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton selected = view.findViewById(radioGroup.getCheckedRadioButtonId());
                if (selected != null) {
                    String optionSelected = selected.getText().toString();
                    if (!optionSelected.equals("")) {
                        listener.secondStep(optionSelected);
                    }
                } else Toast.makeText(getContext(), R.string.toast_text_choose_delivery, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
