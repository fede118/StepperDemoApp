package com.example.stepper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class StepThreeFragment extends Fragment {

    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void thirdStep (String text);
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
        View view = inflater.inflate(R.layout.fragment_step_three, container, false);
        Bundle args = getArguments();

        TextView name = view.findViewById(R.id.nameTextView);
        name.setText(String.format("Name: %s", args.getString(MainActivity.NAME)));

        TextView email = view.findViewById(R.id.emailTextView);
        email.setText(String.format("Email: %s", args.getString(MainActivity.EMAIL)));

        TextView phone = view.findViewById(R.id.phoneNumTextView);
        phone.setText(String.format("Phone Number: %s", args.getString(MainActivity.PHONE)));

        TextView zip = view.findViewById(R.id.zipTextView);
        zip.setText(String.format("Zipcode: %s", args.getString(MainActivity.ZIPCODE)));

        TextView delivery = view.findViewById(R.id.deliveryTextView);
        delivery.setText(String.format("Delivery: %s", args.getString(MainActivity.DELIVERY)));


        Button continueBtn = view.findViewById(R.id.continueBtn3);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("are you sure you want to continue?")
                        .setMessage("there is no going back.")
                        .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.thirdStep(MainActivity.CONTINUE);
                            }
                        })
                        .setNegativeButton("go back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.thirdStep(MainActivity.BACK);
                            }
                        }).show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        return view;
    }
}
