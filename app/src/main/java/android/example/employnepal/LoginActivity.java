package android.example.employnepal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity{
    Vibrator v;
    private EditText emailLogin, passwordLogin;
    private Button btnLoginJobProvider, btnLoginJobSeeker, btnCreateAccount, btnForgotPassword;
    private ProgressBar progressBar;
    private CheckBox rememberMeLogin;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        rememberMeLogin = findViewById(R.id.remember_me_login);
        btnLoginJobProvider = findViewById(R.id.btn_login_as_job_provider);
        btnLoginJobSeeker = findViewById(R.id.btn_login_as_job_seeker);
        btnCreateAccount = findViewById(R.id.btn_create_account);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);
        progressBar = findViewById(R.id.progressbarLogin);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirebaseUser mFirebaseUser = auth.getCurrentUser();


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                intentSignUp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentSignUp);
                finish();
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordByEmail.class));

            }
        });

        btnLoginJobProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString().trim();
                final String password = passwordLogin.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailLogin.setError("Please Enter Email");
                    v.vibrate(100);
                    emailLogin.requestFocus();
                    return;

                } else if (TextUtils.isEmpty(password)) {
                    passwordLogin.setError("Please Enter password");
                    v.vibrate(100);
                    passwordLogin.requestFocus();
                    return;
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
                } else if (password.length() < 6) {
                    passwordLogin.setError(getString(R.string.minimum_password));
                } else if (!(email.isEmpty() && !password.isEmpty())) {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Logging in....", Toast.LENGTH_SHORT).show();
                                        Map<Object,String> userdata = new HashMap<>();
                                        userdata.put("Email",email);
                                        userdata.put("password",password);

                                        database.getReference("JobProvider")
                                                .child("LoginInfo").child(auth.getCurrentUser().getUid()).setValue(userdata);


                                        emailLogin.setText("");
                                        passwordLogin.setText("");
                                        Intent intent = new Intent(LoginActivity.this, DashboardJobProvider.class);
                                        startActivity(intent);
                                        finish();
                                        //moveToDashboardActivity(task.getResult().getUser());


                                    }
                                }

                            });
                }
            }


        });
        btnLoginJobSeeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString().trim();
                final String password = passwordLogin.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    emailLogin.setError("Please Enter password");
                    v.vibrate(100);
                    emailLogin.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    passwordLogin.setError("Please Enter password");
                    v.vibrate(100);
                    passwordLogin.requestFocus();
                    return;
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();

                } else if (password.length() < 6) {
                    passwordLogin.setError(getString(R.string.minimum_password));
                } else if (!(email.isEmpty() && !password.isEmpty())) {


                    progressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Logging in....", Toast.LENGTH_SHORT).show();

                                        Map<Object,String> userdata = new HashMap<>();
                                        userdata.put("Email",email);
                                        userdata.put("password",password);

                                        database.getReference("JobSeeker")
                                                .child("LoginInfo").child(auth.getCurrentUser().getUid()).setValue(userdata);

                                        emailLogin.setText("");
                                        passwordLogin.setText("");

                                        Intent intent = new Intent(LoginActivity.this, DashboardJobSeeker.class);
                                        startActivity(intent);
                                        finish();
                                        //moveToDashboardActivity(task.getResult().getUser());


                                    }
                                }

                            });
                }
            }


        });

    }




    //    private void moveToDashboardActivity(FirebaseUser mFirebaseUser) {
//
//        database.getReference().child(mFirebaseUser.getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        UserHelper userHelper = snapshot.getValue(UserHelper.class);
//                       //String name = userHelper.getFirstName() + " " + userHelper.getLastName();
//                        Intent intentJobProvider = new Intent(getApplicationContext(), DashboardJobProvider.class);
//                        //Intent intentJobSeeker = new Intent(getApplicationContext(), DashboardJobSeeker.class);
//                        intentJobProvider.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        //intentJobSeeker.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        //intentJobProvider.putExtra("name", name);
//                        //intentJobSeeker.putExtra("name", name);
//                        startActivity(intentJobProvider);
//                        //startActivity(intentJobSeeker);
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//
//
//                });
//    }
}
