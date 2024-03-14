package com.example.tracksnap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class SignupFragment extends Fragment {
    private ImageButton infoBtn;
    private EditText signupEmail, signupUsername, signupPassword, signupBio;
    private CheckBox[] signupGenre = new CheckBox[21];
    private CheckBox[] signupRating = new CheckBox[5];
    private CheckBox[] signupDuration = new CheckBox[6];
    private Button signupBtn;
    private TextView genresTxtView;
    private TextView ratingsTxtView;
    private TextView durationsTxtView;
    FirebaseDatabase database;
    DatabaseReference reference;
    private MediaPlayer failSound;
    private MediaPlayer validSound;
    private ImageView Yellowcircle;
    private ImageView Purplecircle;
    private ImageView Redcircle;
    private ImageView Pinkcircle;
    private ImageView Greencircle;
    private ImageView Bluecircle;
    private ImageView OrangeCircle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        // Initializes info, email, username, password, bio, signupBtn
        infoBtn = view.findViewById(R.id.informationIconBtn);
        signupEmail = view.findViewById(R.id.signup_email_edit);
        signupUsername = view.findViewById(R.id.signup_username_edit);
        signupPassword = view.findViewById(R.id.signup_password_edit);
        signupBio = view.findViewById(R.id.bio_edit);
        signupBtn = view.findViewById(R.id.signup_complete_btn);
        genresTxtView = view.findViewById(R.id.genres_txtview);
        ratingsTxtView = view.findViewById(R.id.movierating_txtview);
        durationsTxtView = view.findViewById(R.id.movieduration_txtview);

        failSound = MediaPlayer.create(getContext(), R.raw.casualsound);
        validSound = MediaPlayer.create(getContext(), R.raw.beep);

        Yellowcircle = view.findViewById(R.id.circleYellow);
        Purplecircle = view.findViewById(R.id.circlePurple);
        Redcircle = view.findViewById(R.id.circleRed);
        Pinkcircle = view.findViewById(R.id.circlePink);
        Greencircle = view.findViewById(R.id.circleGreen);
        Bluecircle = view.findViewById(R.id.circleBlue);
        OrangeCircle = view.findViewById(R.id.circleOrange);

        // Initializes genre, rating, and duration checkboxes
        for (int i = 0; i < 21; i++) {
            int checkBoxId = getResources().getIdentifier("gcheckBox" + (i + 1), "id", requireActivity().getPackageName());
            signupGenre[i] = view.findViewById(checkBoxId);
        }
        for (int i = 0; i < 5; i++) {
            int checkBoxId = getResources().getIdentifier("rcheckBox" + (i + 1), "id", requireActivity().getPackageName());
            signupRating[i] = view.findViewById(checkBoxId);
        }
        for (int i = 0; i < 6; i++) {
            int checkBoxId = getResources().getIdentifier("dcheckBox" + (i + 1), "id", requireActivity().getPackageName());
            signupDuration[i] = view.findViewById(checkBoxId);
        }


        // Opens dialog that contains information about the film rating
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoPopup();
            }
        });


        // Signup button to store all data in firebase
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users");

                // Removing whitespaces at the start and end of the string for each of the following
                String email = signupEmail.getText().toString().trim();
                String username = signupUsername.getText().toString().trim();
                String lowercase_username = username.toLowerCase();
                String password = signupPassword.getText().toString().trim();
                String bio = signupBio.getText().toString().trim();

                // Validating username (requirement: length 3-15)
                if (username.length() < 3 || username.length() > 15) {
                    signupUsername.setError("Username must be between 3 and 15 characters");
                    signupUsername.requestFocus();
                    failSound.start();

                    return;
                }

                // Validating password (requirement: one capital letter, one number/special character, and between 6-15)
                if (!password.matches("^(?=.*[A-Z])(?=.*[0-9\\W]).{6,15}$")) {
                    signupPassword.setError("Password must contain at least one capital letter, one number/special character, and be between 6-15 in length");
                    signupPassword.requestFocus();
                    failSound.start();
                    return;
                }

                // Validating bio (requirement: length <= 100)
                if (bio.length() > 100) {
                    signupBio.setError("Bio must be less than 100 characters");
                    signupBio.requestFocus();
                    failSound.start();
                    return;
                }

                // Stores genre
                StringBuilder genresBuilder = new StringBuilder();
                int genresSelected = 0;
                for (CheckBox checkBox : signupGenre) {
                    if (checkBox.isChecked()) {
                        if (genresSelected < 3) {
                            genresBuilder.append(checkBox.getText()).append(",");
                            genresSelected++;
                        } else {
                            break;
                        }
                    }
                }
                String genres = genresBuilder.toString();


                // Stores rating
                StringBuilder ratingsBuilder = new StringBuilder();
                int ratingsSelected = 0;
                for (CheckBox checkBox : signupRating) {
                    if (checkBox.isChecked()) {
                        if (ratingsSelected < 3) {
                            ratingsBuilder.append(checkBox.getText()).append(",");
                            ratingsSelected++;
                        } else {
                            break;
                        }
                    }
                }
                String ratings = ratingsBuilder.toString();

                // Stores duration
                StringBuilder durationsBuilder = new StringBuilder();
                int durationsSelected = 0;
                for (CheckBox checkBox : signupDuration) {
                    if (checkBox.isChecked()) {
                        if (durationsSelected < 3) {
                            durationsBuilder.append(checkBox.getText()).append(",");
                            durationsSelected++;
                        } else {
                            break;
                        }
                    }
                }
                String durations = durationsBuilder.toString();


                // Validating the checkboxes
                if (genresSelected < 1 || genresSelected > 3) {
                    Toast.makeText(requireContext(), "Must select 1 to 3 genres", Toast.LENGTH_SHORT).show();
                    failSound.start();
                    return;
                }

                if (ratingsSelected < 1 || ratingsSelected > 3) {
                    Toast.makeText(requireContext(), "Must select 1 to 3 ratings", Toast.LENGTH_SHORT).show();
                    failSound.start();
                    return;
                }

                if (durationsSelected < 1 || durationsSelected > 3) {
                    Toast.makeText(requireContext(), "Must select 1 to 3 durations", Toast.LENGTH_SHORT).show();
                    failSound.start();
                    return;
                }

                HelperClass helperClass = new HelperClass(email, username, lowercase_username, password, bio, genres, ratings, durations);
                reference.child(username).setValue(helperClass);
                validSound.start();

                Toast.makeText(requireContext(), "You account has been created!", Toast.LENGTH_SHORT).show();

                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_loginFragment);
            }
        });

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


        return view;
    }

//    This is going to be the popup that displays the information about MPA Film Ratings
    public void showInfoPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("MPA Film Rating System");
        String message = "<b>G - General Audiences:</b><br>All ages admitted. Nothing that would offend parents for viewing by children.<br><br>" +
                "<b>PG - Parental Guidance Suggested:</b><br>Some material may not be suitable for children. Parents urged to give 'parental guidance'.<br><br>" +
                "<b>PG-13 - Parents Strongly Cautioned:</b><br>Some material may be inappropriate for children under 13. Parents urged to be cautious.<br><br>" +
                "<b>R - Restricted:</b><br>Under 17 requires accompanying parent or adult guardian. Contains some adult material.<br><br>" +
                "<b>NC-17 - Adults Only:</b><br>No one 17 and under admitted. Clearly adult. Children are not admitted.";
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("THANKS!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer when the activity is destroyed to free up resources
        if (failSound != null) {
            failSound.release();
            failSound = null;
        }
        if (validSound != null) {
            validSound.release();
            validSound = null;
        }
    }
}