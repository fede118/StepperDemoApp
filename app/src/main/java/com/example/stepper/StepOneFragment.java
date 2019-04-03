package com.example.stepper;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class StepOneFragment extends Fragment {

    EditText nameEditText;
    EditText emailEditText;
    EditText phoneEditText;
    EditText zipEditText;

    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void firstStep (HashMap<String,String> userInfo);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + R.string.fragment_class_exception);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_one, container, false);
        nameEditText = view.findViewById(R.id.nameEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        zipEditText = view.findViewById(R.id.zipEditText);

//        al clickear continue checkear que todos los campos esten comp;etos o mandar a MainActivity via interface listener
        Button continueBtn = view.findViewById(R.id.continueBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String zip = zipEditText.getText().toString();

                if (name.equals("") || email.equals("") || phone.equals("") || zip.equals("")) {
                    Toast.makeText(getContext(), "Please fill all slots", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String,String> userInfo = new HashMap<>();
                userInfo.put(MainActivity.NAME, name);
                userInfo.put(MainActivity.EMAIL, email);
                userInfo.put(MainActivity.PHONE, phone);
                userInfo.put(MainActivity.ZIPCODE, zip);

                listener.firstStep(userInfo);
            }
        });
        return view;
    }
}
