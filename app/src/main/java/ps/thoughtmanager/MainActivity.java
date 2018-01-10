package ps.thoughtmanager;

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
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Scene loginScene;
    Scene registerScene;
    ViewGroup mSceneRoot;
    Button loginBtn, registerBtn;
    Date date;
    TextInputEditText username, password, confirmpassword, firstname, lastname;
    TextView dob, gender;
    boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ps.thoughtmanager.R.layout.activity_main);
        mSceneRoot = findViewById(ps.thoughtmanager.R.id.login_register_scene);
        loginScene = Scene.getSceneForLayout(mSceneRoot, ps.thoughtmanager.R.layout.login, this);
        registerScene = Scene.getSceneForLayout(mSceneRoot, ps.thoughtmanager.R.layout.register, this);
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
        username = findViewById(ps.thoughtmanager.R.id.username);
        password = findViewById(ps.thoughtmanager.R.id.password);
        loginBtn = findViewById(ps.thoughtmanager.R.id.loginBtn);
        registerBtn = findViewById(ps.thoughtmanager.R.id.registerBtn);
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
            password.setError(getString(ps.thoughtmanager.R.string.requiredError));
            cancel = true;
        } else if (passwordText.length() < 8) {
            errorView = password;
            password.setError(getString(ps.thoughtmanager.R.string.passwordError));
            cancel = true;
        }
        if ("".equals(usernameText)) {
            errorView = username;
            username.setError(getString(ps.thoughtmanager.R.string.requiredError));
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
        username = findViewById(ps.thoughtmanager.R.id.username);
        password = findViewById(ps.thoughtmanager.R.id.password);
        confirmpassword = findViewById(ps.thoughtmanager.R.id.confirm_password);
        lastname = findViewById(ps.thoughtmanager.R.id.lastname);
        firstname = findViewById(ps.thoughtmanager.R.id.firstname);
        gender = findViewById(ps.thoughtmanager.R.id.tGender);
        dob = findViewById(ps.thoughtmanager.R.id.dob);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog dp = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dob.setText("D.O.B. : " + day + "/" + (month + 1) + "/" + year);
                        date = new Date(year, month, day);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dp.getDatePicker().setMaxDate(System.currentTimeMillis());
                dp.show();
            }
        });
        registerBtn = findViewById(ps.thoughtmanager.R.id.registerBtn);
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
        String gd;

        boolean cancel = false;
        View errorView = null;
        if (date == null) {
            errorView = dob;
            dob.setError(getString(ps.thoughtmanager.R.string.requiredError));
            cancel = true;
        }

        RadioGroup rg = findViewById(ps.thoughtmanager.R.id.rgroup);
        switch (rg.getCheckedRadioButtonId()) {
            case (ps.thoughtmanager.R.id.rMale):
                gd = "Male";
                break;
            case (ps.thoughtmanager.R.id.rFemale):
                gd = "Female";
                break;
            default:
                errorView = gender;
                cancel = true;
                gender.setError(getString(ps.thoughtmanager.R.string.requiredError));
        }
        if ("".equals(confirmpasswordText)) {
            errorView = confirmpassword;
            confirmpassword.setError(getString(ps.thoughtmanager.R.string.requiredError));
            cancel = true;
        } else if (!confirmpasswordText.equals(passwordText)) {
            errorView = confirmpassword;
            confirmpassword.setError(getString(ps.thoughtmanager.R.string.confirmPasswordError));
            cancel = true;
        }
        if ("".equals(passwordText)) {
            errorView = password;
            password.setError(getString(ps.thoughtmanager.R.string.requiredError));
            cancel = true;
        } else if (passwordText.length() < 8) {
            errorView = password;
            password.setError(getString(ps.thoughtmanager.R.string.passwordError));
            cancel = true;
        }
        if ("".equals(usernameText)) {
            errorView = username;
            username.setError(getString(ps.thoughtmanager.R.string.requiredError));
            cancel = true;
        }
        if ("".equals(lastnameText)) {
            errorView = lastname;
            lastname.setError(getString(ps.thoughtmanager.R.string.requiredError));
            cancel = true;
        }
        if ("".equals(firstnameText)) {
            errorView = firstname;
            firstname.setError(getString(ps.thoughtmanager.R.string.requiredError));
            cancel = true;
        }
        if (errorView != null && errorView.equals(gender)) {
            Toast.makeText(this, R.string.genderError, Toast.LENGTH_SHORT).show();
        }
        if (errorView != null && errorView.equals(dob)) {
            Toast.makeText(this, "Please select birthdate", Toast.LENGTH_SHORT).show();
        }
        if (cancel) {
            errorView.requestFocus();
        } else {
            Toast.makeText(this, "registered", Toast.LENGTH_SHORT).show();
        }
    }
}
