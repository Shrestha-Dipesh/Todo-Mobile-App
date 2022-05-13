package com.example.todo_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState) {
        ImageView splash_background = getView().findViewById(R.id.splash_background);
        splash_background.animate().translationY(-1600).setDuration(1000).setStartDelay(4000);
        LottieAnimationView lottieAnimationView = getView().findViewById(R.id.splash_lottie);
        lottieAnimationView.playAnimation();
        lottieAnimationView.animate().translationY(1700).setDuration(1000).setStartDelay(4000);
    }
}