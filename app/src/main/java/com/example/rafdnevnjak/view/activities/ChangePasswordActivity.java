package com.example.rafdnevnjak.view.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rafdnevnjak.R;
import com.example.rafdnevnjak.model.User;
import com.example.rafdnevnjak.model.UsersDTO;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChangePasswordActivity extends AppCompatActivity {

    //The input for a new password
    private TextView newPasswordInput;

    //Input to confirm new password
    private TextView confirmNewPasswordInput;

    //Button to submit new password
    private Button submitNewPasswordButton;

    //Button to cancel password change
    private Button cancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle(R.string.change_password);

        init();
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
        newPasswordInput = findViewById(R.id.newPasswordInput);
        confirmNewPasswordInput = findViewById(R.id.confirmNewPasswordInput);
        submitNewPasswordButton = findViewById(R.id.submitNewPasswordButton);
        cancelButton = findViewById(R.id.cancelNewPasswordButton);
    }

    /**
     * Checks if new password is different from old password, and the confirmation of the new
     * password is the same as new password
     * Also check that the password contains at least one number, one upper case letter,
     * that it doesn't contain any of the ~#^|$%&*! symbols, has at least 5 characters
     * @return true if the above statements are correct, and false otherwise
     */
    private boolean checkInput(SharedPreferences prefs){;
        Gson gs = new Gson();
        User user = gs.fromJson(prefs.getString("current_user", ""),User.class);
        String password = newPasswordInput.getText().toString();
        if (!password.equals(confirmNewPasswordInput.getText().toString())){
            Toast.makeText(this, R.string.new_password_confirm_alert, Toast.LENGTH_LONG).show();
            return false;
        }
        else if (password.equals(user.getPassword())){
            Toast.makeText(this, R.string.new_password_same_as_old_alert, Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 5)
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

    /**
     * Writes the new password in the raw/users.json file
     * @param prefs SharedPreferences, obtain them with the getPackageName()
     */
    private void writeNewPasswordToFile(SharedPreferences prefs){
        //Getting the logged in user
        Gson gs = new Gson();
        User currUser = gs.fromJson(prefs.getString("current_user", ""), User.class);

        //Getting all the users
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
        ArrayList<User> users = usersDTO.getUsersFromJSON(stringBuilder.toString());

        for (User user: users){
            if (user.getPassword().equals(currUser.getPassword()) && user.getEmail().equals(currUser.getEmail()) && user.getUsername().equals(currUser.getUsername())){
                user.setPassword(newPasswordInput.getText().toString());
                break;
            }
        }

        //This is the data we wold store in a file
        String JSON = usersDTO.getJSONFromUsers(users);

        //We would change the password for the user in the database, but here we just
        //update the password for the runtime (it wont survive a restart)
        LoginActivity.updateUserPassword(currUser.getUsername(), currUser.getEmail(), newPasswordInput.getText().toString());

        //Data from res folder is read-only
//        try{
//            OutputStream os = openFileOutput("users.json", Context.MODE_PRIVATE);
//            os.write(JSON.getBytes(), 0, JSON.length());
//            os.close();
//        }
//        catch (IOException e){
//            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
//        }

    }

    /**
     * Initializes listeners for button presses
     */
    private void initListeners(){
        submitNewPasswordButton.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            if (checkInput(prefs)){
                writeNewPasswordToFile(prefs);
                Gson gs = new Gson();
                User user = gs.fromJson(prefs.getString("current_user",""), User.class);
                user.setPassword(newPasswordInput.getText().toString());
                prefs.edit().putString("current_user", gs.toJson(user)).apply();

                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);

                finish();
            }
        });

        cancelButton.setOnClickListener(v -> {
            finish();
        });
    }
}
