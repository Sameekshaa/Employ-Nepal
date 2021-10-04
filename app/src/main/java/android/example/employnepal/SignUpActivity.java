package android.example.employnepal;

import android.content.Context;
import android.content.Intent;
import android.example.employnepal.models.UserHelper;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    UserHelper userHelper;
    FirebaseDatabase database;
    DatabaseReference reference;
    long i = 0;
    Vibrator v;
    private EditText inputFirstName, inputLastName, emailsignup, contactsignup, locationsignup, passwordsignup, confirmpasswordsignup;
    private Button btnsignup, btnback;
    private ProgressBar progressBar;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private RadioButton radioOthers;
    private String gender = "";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        userHelper = new UserHelper();

        inputFirstName = findViewById(R.id.firstName_signup);
        inputLastName = findViewById(R.id.lastName_signup);
        emailsignup = findViewById(R.id.email_signup);
        contactsignup = findViewById(R.id.contact_signup);
        locationsignup = findViewById(R.id.location_signup);
        passwordsignup = findViewById(R.id.password_signup);
        confirmpasswordsignup = findViewById(R.id.confirm_password_signup);
        btnsignup = findViewById(R.id.btnSignUp);
        btnback = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressbarSignup);
        radioMale = findViewById(R.id.male_signup);
        radioFemale = findViewById(R.id.female_signup);
        radioOthers = findViewById(R.id.others_signup);

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    i = (snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ///

            }
        });




        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = inputFirstName.getText().toString().trim();
                String lastName = inputLastName.getText().toString().trim();
                String emailSignUp = emailsignup.getText().toString().trim();
                String contactSignUp = contactsignup.getText().toString().trim();
                String locationSignUp = locationsignup.getText().toString().trim();
                String passwordSignUp = passwordsignup.getText().toString().trim();
                String confirmPasswordSignUp = confirmpasswordsignup.getText().toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$)(?=.*[@#$%^&+=!;]).{6,}$";
//                String passwordVal="^" +
////                                    "(?=.*[0-9])"+
//                                    "(?=.*[a-z])"+
//                                    "(?=.*[A-Z])"+
////                                    "(?=.*[a-zA-Z])"+
//                                    "(?=.*[@#$%^&+=])"+
//                                    "$";



                if (TextUtils.isEmpty(firstName)) {
                    inputFirstName.setError("Please Enter your first name");
                    v.vibrate(100);
                    inputFirstName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    inputLastName.setError("Please Enter your last name");
                    v.vibrate(100);
                    inputLastName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(emailSignUp)) {
                    emailsignup.setError("Please Enter your email address");
                    v.vibrate(100);
                    emailsignup.requestFocus();
                    return;
                }
                if (!emailSignUp.matches(emailPattern)) {
                    emailsignup.setError("Invalid email address!");
                    v.vibrate(100);
                    emailsignup.requestFocus();
                    return;

                }
                if (TextUtils.isEmpty(contactSignUp)) {
                    contactsignup.setError("Please Enter your phone number");
                    v.vibrate(100);
                    contactsignup.requestFocus();
                    return;
                }
                if (!(contactSignUp.length() == 10)) {
                    contactsignup.setError("Please Enter 10 digits");
                    v.vibrate(100);
                    contactsignup.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(locationSignUp)) {
                    locationsignup.setError("Please Enter your address");
                    v.vibrate(100);
                    locationsignup.requestFocus();
                    return;
                }
                if (radioFemale.isChecked()) {
                    gender = "Female";
                } else if (radioMale.isChecked()) {
                    gender = "Male";

                } else {
                    gender = "Others";
                }

                if (TextUtils.isEmpty(passwordSignUp)) {
                    passwordsignup.setError("Please Enter password");
                    v.vibrate(100);
                    passwordsignup.requestFocus();
                    return;
                }

//                if (passwordSignUp.length() < 6) {
//                    passwordsignup.setError("Password too short, enter minimum 6 characters!");
//                    v.vibrate(100);
//                    passwordsignup.requestFocus();
//                    return;
//                }
                if (!passwordSignUp.matches(passwordPattern)) {
                    passwordsignup.setError("Not a strong password! .... Must contain atleast one number, atleast one letter, no space allowed, at least one special symbol - @#$%^&+=!; and must contain atleast 6 chatacters in total");
                    v.vibrate(100);
                    passwordsignup.requestFocus();
                    return;

                }
//                if (!passwordSignUp.matches(passwordVal)){
//                    passwordsignup.setError("Password too weak");
//                    v.vibrate(100);
//                    passwordsignup.requestFocus();
//                    return;
//
//                }
                if (TextUtils.isEmpty(confirmPasswordSignUp)) {
                    confirmpasswordsignup.setError("You have to enter password twice for confirmation");
                    v.vibrate(100);
                    confirmpasswordsignup.requestFocus();
                    return;
                }
                if (!passwordSignUp.equals(confirmPasswordSignUp)) {
                    confirmpasswordsignup.setError("Passwords didn't match");
                    v.vibrate(100);
                    confirmpasswordsignup.requestFocus();
                    return;
                }
                //verify phone
                Intent intent=new Intent(SignUpActivity.this,OTPActivity.class);
                intent.putExtra("firstName",firstName);
                intent.putExtra("lastName",lastName);
                intent.putExtra("email",emailSignUp);
                intent.putExtra("contact",contactSignUp);
                intent.putExtra("address",locationSignUp);
                intent.putExtra("gender",gender);
                intent.putExtra("password",passwordSignUp);
                intent.putExtra("confirmPassword",confirmPasswordSignUp);
                // intent.putExtra("whatTODo","storeData");


                startActivity(intent);
                progressBar.setVisibility(View.VISIBLE);

                //setting value to database
                /*UserHelper userHelper = new UserHelper(firstName, lastName, emailSignUp, contactSignUp, locationSignUp, gender, passwordSignUp, confirmPasswordSignUp);
                //reference.child("users").child(String.valueOf(i)).push().setValue(userHelper);
                reference.child("users").child(contactSignUp).setValue(userHelper);*/




                //authentication with email and password
//                auth.createUserWithEmailAndPassword(emailSignUp, passwordSignUp).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//
//
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(SignUpActivity.this, "Authentication failed :" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        } else {
//                            //Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
//                            Toast.makeText(SignUpActivity.this, "User Authentication successful", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//
//                            Map<Object,String> userdata = new HashMap<>();
//                            userdata.put("firstName",firstName);
//                            userdata.put("lastName",lastName);
//                            userdata.put("email",emailSignUp);
//                            userdata.put("contact",contactSignUp);
//                            userdata.put("location",locationSignUp);
//                            userdata.put("gender",gender);
//                            userdata.put("password",passwordSignUp);
//                            userdata.put("confirmPassword",confirmPasswordSignUp);
//
//                            database.getReference("users")
//                                    .child(auth.getCurrentUser().getUid()).setValue(userdata);
//
//                            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);
//                            finish();
//
//
//                        }
//                    }
//                });



            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


}
