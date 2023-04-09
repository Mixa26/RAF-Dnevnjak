package com.example.rafdnevnjak.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.User;
import com.example.rafdnevnjak.view.activities.ChangePasswordActivity;
import com.google.gson.Gson;


public class ProfileFragment extends Fragment {

    //Shows the current usernames username
    private TextView profileUsername;

    //Shows the current usernames email
    private TextView profileEmail;

    //Button for opening the form for changing the password
    private Button changePasswordButton;

    //Logs out the current user
    private Button logOutButton;

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        init();

        return view;
    }

    /**
     * Initializes all the necessary components
     */
    private void init(){
        initView();
        initListeners();
    }

    /**
     * Gets all the view components in code so we can work with them
     */
    private void initView(){
        profileUsername = view.findViewById(R.id.profileUsername);
        profileEmail = view.findViewById(R.id.profileEmail);
        logOutButton = view.findViewById(R.id.logOutButton);
        changePasswordButton = view.findViewById(R.id.changePasswordButton);

        SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE);
        Gson gs = new Gson();
        User user = gs.fromJson(prefs.getString("current_user",""), User.class);

        profileUsername.setText(user.getUsername());
        profileEmail.setText(user.getEmail());
    }

    /**
     * Initializes listeners for button presses
     */
    private void initListeners(){
        logOutButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences(getActivity().getPackageName(), Context.MODE_PRIVATE).edit();

            editor.remove("current_user");
            editor.apply();
            getActivity().finish();
        });

        changePasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    //Receives RESULT_OK if the password has been changed so it can inform the user
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            Toast.makeText(getContext(), R.string.password_changed_success, Toast.LENGTH_LONG).show();
        }
    }
}
