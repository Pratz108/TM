package com.example.dell.tm1;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    Scene loginScene;
    Scene registerScene;
    ViewGroup mSceneRoot;
    Button loginBtn, registerBtn;
    TextInputEditText username, password, confirmpassword, firstname, lastname;
    TextView dob;
    boolean isLogin = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSceneRoot = findViewById(R.id.login_register_scene);
        loginScene = Scene.getSceneForLayout(mSceneRoot,R.layout.login,this);
        registerScene = Scene.getSceneForLayout(mSceneRoot,R.layout.register,this);
        initLogin();
    }

    public void swapScenes(boolean isLogin) {
        Transition t = new AutoTransition();
        if (isLogin) {
            TransitionManager.go(registerScene, t);
            this.isLogin = false;
            initRegister();
        } else {
            TransitionManager.go(loginScene, t);
            this.isLogin = true;
            initLogin();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isLogin) {
            swapScenes(false);
        } else super.onBackPressed();
    }

    public void initLogin() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapScenes(true);
            }
        });
    }

    private void attemptLogin() {
        String usernameText = username.getText().toString().trim();
        String passwordText = password.getText().toString().trim();
        boolean cancel = false;
        View errorView = null;
        if ("".equals(passwordText)) {
            errorView = password;
            password.setError(getString(R.string.requiredError));
            cancel = true;
        } else if (passwordText.length() < 8) {
            errorView = password;
            password.setError(getString(R.string.passwordError));
            cancel = true;
        }
        if ("".equals(usernameText)) {
            errorView = username;
            username.setError(getString(R.string.requiredError));
            cancel = true;
        }

        if (cancel) {
            errorView.requestFocus();
            return;
        } else {
            Toast.makeText(this, "login successful", Toast.LENGTH_SHORT).show();
        }
    }

    public void initRegister() {
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirm_password);
        lastname = findViewById(R.id.lastname);
        firstname = findViewById(R.id.firstname);
        dob = findViewById(R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        dob.setText("D.O.B. : " + date + "/" + (month + 1) + "/" + year);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dp.getDatePicker().setMaxDate(System.currentTimeMillis());
                dp.show();
            }
        });
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
    }

    private void attemptRegistration() {
        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();
        String confirmpasswordText = confirmpassword.getText().toString();
        String firstnameText = firstname.getText().toString();
        String lastnameText = lastname.getText().toString();
        String gender;
        boolean cancel = false;
        View errorView;
        RadioGroup rg = findViewById(R.id.rgroup);
        switch (rg.getCheckedRadioButtonId()) {
            case (R.id.rMale):
                gender = "Male";
                break;
            case (R.id.rFemale):
                gender = "Female";
                break;
            default:
                errorView = rg;
                cancel = true;
                Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this, "register clicked", Toast.LENGTH_SHORT).show();
    }
}
