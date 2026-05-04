package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class LoginBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_login, container, false);

        Button btnLogin = view.findViewById(R.id.btnLogin);
        Button btnSignup = view.findViewById(R.id.btnSignup);

        // Both open SignupActivity (as you requested)
        btnLogin.setOnClickListener(v -> openSignup());
        btnSignup.setOnClickListener(v -> openSignup());

        return view;
    }

    private void openSignup() {
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), SignupActivity.class));

        }
    }

    @Override
    public void onDismiss(@NonNull android.content.DialogInterface dialog) {
        super.onDismiss(dialog);

        // If popup closed → open home
        if (getActivity() instanceof LocationActivity) {
            ((LocationActivity) getActivity()).openHome();
        }
    }
}