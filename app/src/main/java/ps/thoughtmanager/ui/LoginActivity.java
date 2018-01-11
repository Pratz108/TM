package ps.thoughtmanager.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.Date;

import ps.thoughtmanager.R;

public class LoginActivity extends AppCompatActivity {
    private final String LOG = "LoginActivity";
    Scene loginScene;
    Scene registerScene;
    ViewGroup mSceneRoot;
    Button loginBtn, registerBtn;
    Date date;
    TextInputEditText email, password, confirmpassword, firstname, lastname;
    TextView dob, gender;
    boolean isLogin = true;
    private FirebaseAuth pAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ps.thoughtmanager.R.layout.activity_main);
        mSceneRoot = findViewById(ps.thoughtmanager.R.id.login_register_scene);
        loginScene = Scene.getSceneForLayout(mSceneRoot, ps.thoughtmanager.R.layout.login, this);
        registerScene = Scene.getSceneForLayout(mSceneRoot, ps.thoughtmanager.R.layout.register, this);
        initLogin();
        pAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = pAuth.getCurrentUser();
        if (currentUser != null) {
            //todo launch main-activity and finish this activity
        }
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
        email = findViewById(ps.thoughtmanager.R.id.email);
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
        String emailText = email.getText().toString().trim();
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
        if ("".equals(emailText)) {
            errorView = email;
            email.setError(getString(ps.thoughtmanager.R.string.requiredError));
            cancel = true;
        }

        if (cancel) {
            errorView.requestFocus();
            return;
        } else {
            pAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG, getString(R.string.loginSuccess));
                        Toast.makeText(LoginActivity.this, getString(R.string.loginSuccess), Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d(LOG, getString(R.string.loginFailed));
                        Toast.makeText(LoginActivity.this, getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }
    }

    public void initRegister() {
        email = findViewById(ps.thoughtmanager.R.id.email);
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
                DatePickerDialog dp = new DatePickerDialog(LoginActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        String emailText = email.getText().toString();
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
        if ("".equals(emailText)) {
            errorView = email;
            email.setError(getString(ps.thoughtmanager.R.string.requiredError));
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

            pAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG, getString(R.string.userCreated));

                        pAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(LoginActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    //todo: show verification scene
                                }
                            }
                        });

                        Toast.makeText(LoginActivity.this, getString(R.string.postRegistration), Toast.LENGTH_SHORT).show();
                        swapScenes(false);

                    } else {
                        Log.d(LOG, getString(R.string.createUserFailed));
                    }
                }
            });
        }
    }
}
