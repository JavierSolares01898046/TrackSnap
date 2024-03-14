package com.example.tracksnap;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;


public class HomeFragment extends Fragment {
    private Button loginBtn;
    private Button signupBtn;
    private ImageView Yellowcircle;
    private ImageView Purplecircle;
    private ImageView Redcircle;
    private ImageView Pinkcircle;
    private ImageView Greencircle;
    private ImageView Bluecircle;
    private ImageView OrangeCircle;
    private MediaPlayer homeSound;
    private MediaPlayer startupSound;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        signupBtn = view.findViewById(R.id.signup_btn);
        loginBtn = view.findViewById(R.id.login_btn);

        Yellowcircle = view.findViewById(R.id.circleYellow);
        Purplecircle = view.findViewById(R.id.circlePurple);
        Redcircle = view.findViewById(R.id.circleRed);
        Pinkcircle = view.findViewById(R.id.circlePink);
        Greencircle = view.findViewById(R.id.circleGreen);
        Bluecircle = view.findViewById(R.id.circleBlue);
        OrangeCircle = view.findViewById(R.id.circleOrange);

        homeSound = MediaPlayer.create(getContext(), R.raw.beep);
        startupSound = MediaPlayer.create(getContext(), R.raw.startup);

        startupSound.start();

        Animation animation1 = AnimationUtils.loadAnimation(requireActivity(), R.anim.movecircle);
        Yellowcircle.startAnimation(animation1);

        // Animation for moving circle2
        Animation animation2 = AnimationUtils.loadAnimation(requireActivity(), R.anim.movecircle);
        Redcircle.startAnimation(animation2);

        Animation animation3 = AnimationUtils.loadAnimation(requireActivity(), R.anim.movecircle);
        Pinkcircle.startAnimation(animation3);

        // Animation for moving circle2
        Animation animation4 = AnimationUtils.loadAnimation(requireActivity(), R.anim.movecircle);
        Purplecircle.startAnimation(animation4);

        Animation animation5 = AnimationUtils.loadAnimation(requireActivity(), R.anim.movecircle);
        Greencircle.startAnimation(animation5);

        Animation animation6 = AnimationUtils.loadAnimation(requireActivity(), R.anim.movecircle);
        Bluecircle.startAnimation(animation6);

        Animation animation7 = AnimationUtils.loadAnimation(requireActivity(), R.anim.movecircle);
        OrangeCircle.startAnimation(animation7);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_signupFragment);
                homeSound.start();

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_loginFragment);
                homeSound.start();

            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer when the activity is destroyed to free up resources
        if (homeSound != null) {
            homeSound.release();
            homeSound = null;
        }
        if (startupSound != null) {
            startupSound.release();
            startupSound = null;
        }
    }

}