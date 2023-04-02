package com.example.rafdnevnjak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.core.splashscreen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rafdnevnjak.model.User;
import com.example.rafdnevnjak.model.UsersDTO;
import com.example.rafdnevnjak.viewmodels.SplashViewModel;
import com.google.gson.Gson;

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

    //Logical variables
    private ArrayList<User> users;
    public static final String currentUserKey = "current-user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSplash();
        checkSession();

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
            Toast.makeText(this, R.string.app_user_internal_error, Toast.LENGTH_LONG).show();
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

            if (!checkInput(email,username,password))return;

            for (User user: users){
                if (username.equals(user.getUsername()) && email.equals(user.getEmail()) && password.equals(user.getPassword())){
                    //If user found, bring him to the main activity
                    SharedPreferences.Editor sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit();

                    Gson gs = new Gson();

                    sharedPreferences.putString(currentUserKey, gs.toJson(user));
                    sharedPreferences.apply();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    return;
                }
            }

            Toast.makeText(this, R.string.no_such_user_alert, Toast.LENGTH_LONG).show();
        });
    }

    /**
     * Checks if the login input is correct.
     * @param email String of user email input.
     * @param username String of user username input.
     * @param password String of user password input.
     * @return Return true if all input is ok, false otherwise.
     */
    private boolean checkInput(String email, String username, String password){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        if (email.equals(""))
        {
            loginEmail.setError(getResources().getString(R.string.email_empty_alert));
            return false;
        }

        if (username.equals(""))
        {
            loginUsername.setError(getResources().getString(R.string.username_empty_alert));
            return false;
        }

        if (password.equals(""))
        {
            loginPassword.setError(getResources().getString(R.string.password_empty_alert));
            return false;
        }

        if (!email.matches(emailRegex))
        {
            Toast.makeText(this, R.string.email_invalid_alert, Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password.length() < 5)
        {
            Toast.makeText(this, R.string.password_short_alert, Toast.LENGTH_LONG).show();
            return false;
        }
        else if (password.equals(password.toLowerCase()))
        {
            Toast.makeText(this, R.string.password_lowercase_alert, Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!password.matches(".*\\d+.*"))
        {
            Toast.makeText(this, R.string.password_no_num_alert, Toast.LENGTH_LONG).show();
            return false;
        }
        else if (!password.matches(".*[~#^|$%&*!]*.*"))
        {
            Toast.makeText(this, R.string.password_forbidden_symbols_alert, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void checkSession(){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        if (sharedPreferences.contains(currentUserKey)){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}