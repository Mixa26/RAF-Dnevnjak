package com.example.rafdnevnjak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.core.splashscreen.SplashScreen;

import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.rafdnevnjak.model.User;
import com.example.rafdnevnjak.model.UsersDTO;
import com.example.rafdnevnjak.viewmodels.SplashViewModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    //View components
    private EditText loginEmail;
    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;

    private SplashViewModel splashViewModel;

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSplash();

        setContentView(R.layout.activity_login);

        loadUsers();
        initView();
        initListeners();
    }

    /**
     * Initializes the logo before the app starts.
     */
    private void initSplash(){
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
    }

    /**
     * Finds view elements from the screen so we can use their data later.
     */
    private void initView(){
        loginEmail = findViewById(R.id.loginEmail);
        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
    }

    /**
     * Loads users from a JSON file ("users.json" in res/raw) into a Java ArrayList named "users".
     */
    private void loadUsers(){
        Resources resources = getResources();

        //Fetching the users.json from th res/raw folder
        int resourceId = resources.getIdentifier("users", "raw", getPackageName());
        InputStream inputStream = resources.openRawResource(resourceId);

        //Reading the contents of the file
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        catch (IOException e){
            //TODO drop a snackbar with an error message
            e.printStackTrace();
        }

        //Transfering users from a JSON string "line" to a Java ArrayList "users"
        UsersDTO usersDTO = new UsersDTO();
        users = usersDTO.getUsersFromJSON(stringBuilder.toString());
    }

    /**
     * Initializes all the listeners for button presses ect.
     */
    private void initListeners(){
        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString();
            String username = loginUsername.getText().toString();
            String password = loginPassword.getText().toString();

            for (User user: users){
                if (username.equals(user.getUsername()) && email.equals(user.getEmail()) && password.equals(user.getPassword())){
                    //TODO successful login, redirect the user
                    return;
                }
            }

            //TODO unsuccessful login, drop a snackbar with an error message
        });
    }

    private void login(){

    }
}