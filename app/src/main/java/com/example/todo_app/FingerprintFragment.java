package com.example.todo_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class FingerprintFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fingerprint, container, false);
    }

    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState) {
        ImageView login_background = getView().findViewById(R.id.login_background);
        ImageView fingerprint = getView().findViewById(R.id.fingerprint);
        fingerprint.setOnClickListener(viewItem -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }
}