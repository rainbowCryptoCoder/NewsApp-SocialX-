package com.example.newsapp.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.MainActivity;
import com.example.newsapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Locale;

public class SignIn extends AppCompatActivity {

    private Button btn_login_page, btn_signup_page, btn_login;
    private CardView card_login, card_signup;
    private EditText et_mail, et_password, et_name_signup, et_mail_signup, et_phone_signup, et_password_signup;
    private TextView tv_forgot_password, tv_register_now;
    private ImageView ic_google;
    private MaterialCheckBox cb_tnc;
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private boolean isLoginSelected=true, isSignupSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initComponents();

        initListener();

    }

    private void initComponents() {
        btn_login_page = findViewById(R.id.btn_login_page);
        btn_signup_page = findViewById(R.id.btn_signup_page);
        card_login = findViewById(R.id.card_login);
        et_mail = findViewById(R.id.et_mail);
        et_password = findViewById(R.id.et_password);
        tv_forgot_password = findViewById(R.id.tv_forgot_password);
        ic_google = findViewById(R.id.ic_google);
        tv_register_now = findViewById(R.id.tv_register_now);
        card_signup = findViewById(R.id.card_signup);
        et_name_signup = findViewById(R.id.et_name_signup);
        et_mail_signup = findViewById(R.id.et_mail_signup);
        et_phone_signup = findViewById(R.id.et_phone_signup);
        et_password_signup = findViewById(R.id.et_password_signup);
        cb_tnc = findViewById(R.id.cb_tnc);
        btn_login = findViewById(R.id.btn_login);
    }

    private void initListener() {

        // Initialize sign in options
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken("1003650277991-pfhlmag2eoiq8ggr18fb4mg46e56voo0.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(SignIn.this, googleSignInOptions);

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            // When user already sign in
            // redirect to profile activity
            startActivity(new Intent(SignIn.this, MainActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        ic_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_login.getText().toString().toLowerCase(Locale.ROOT).equalsIgnoreCase("login")) {
                    loginUserAccount();
                } else {
                    registerNewUser();
                }
            }
        });

        btn_login_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSignupSelected){
                    isSignupSelected = false;
                    isLoginSelected = true;

                    card_login.setVisibility(View.VISIBLE);
                    card_signup.setVisibility(View.GONE);

                    btn_login.setText("Login");

                    btn_login.setBackgroundColor(Color.parseColor("#FE0000"));
                    btn_signup_page.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                }
            }
        });

        btn_signup_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoginSelected){
                    isSignupSelected = true;
                    isLoginSelected = false;

                    card_login.setVisibility(View.GONE);
                    card_signup.setVisibility(View.VISIBLE);

                    btn_login.setText("Register");

                    btn_login.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                    btn_signup_page.setBackgroundColor(Color.parseColor("#FE0000"));
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            if (signInAccountTask.isSuccessful()) {
                displayToast("Google sign in successful");
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);
                    if (googleSignInAccount != null) {
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(SignIn.this, MainActivity.class)
                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                            displayToast("Firebase authentication successful");
                                        } else {
                                            displayToast("Authentication Failed :" + task.getException().getMessage());
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void registerNewUser() {
        String email, password;
        email = et_mail_signup.getText().toString();
        password = et_password_signup.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Registration successful!",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Registration failed!!"
                                            + " Please try again later",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void loginUserAccount()
    {
        String email, password;
        email = et_mail.getText().toString();
        password = et_password.getText().toString();

        // validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG).show();

                                    Intent intent = new Intent(SignIn.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login failed!!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
    }
}