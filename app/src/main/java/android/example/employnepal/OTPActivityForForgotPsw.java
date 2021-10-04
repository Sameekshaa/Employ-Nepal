package android.example.employnepal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.chaos.view.PinView;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OTPActivityForForgotPsw extends AppCompatActivity {
    String verificationCodeBySystem;
    private Button submitButton, resendButton, backButton;
    private PinView pinView;
    private ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference reference;
    private FirebaseAuth auth;
    private String firstName, lastName, email, contact, location, gender, password, confirmPassword;
    //    String whatToDo, whatTODO;
    NestedScrollView scrollView;
    String country_code = "977";
    //    String completePhoneNo;
    //  String phoneNo;
    String _phoneNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpfor_forgot_psw);


        submitButton = findViewById(R.id.button_submit_forgot_psw);
        resendButton = findViewById(R.id.resend_btn_forgot_psw);
        pinView = findViewById(R.id.pinView_forgot_psw);
        progressBar = findViewById(R.id.progressbarOtp_forgot_psw);
        backButton = findViewById(R.id.backButtonOtpForgotPsw);
        scrollView = findViewById(R.id.scrollViewForgotPsw);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        reference = database.getReference();

        //from forgotpassword activity
        Intent intent = getIntent();
        contact = intent.getStringExtra("contact");
        _phoneNumber = intent.getStringExtra("contactForUpdate");


        sendVerificationCodeToUser(contact);
        submitButton.setOnClickListener(view -> {
            String code = pinView.getText().toString();
            if (code.isEmpty() | code.length() < 6) {
                pinView.setError("Wrong OTP!");
                pinView.requestFocus();
                return;
            } else {

                verifyCode(code);

                //to pass email and password to login and call login
//            Intent intent2 = new Intent(OtpActivity.this, LoginActivity.class);
//            intent2.putExtra("emailToLoginFromOtp",email);
//            intent2.putExtra("passwordToLoginFromOtp",password);

//            startActivity(intent2);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        backButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(OTPActivityForForgotPsw.this, ForgotPasswordActivity.class);
            startActivity(intent1);
        });


    }

    private void sendVerificationCodeToUser(String completePhoneNo) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(completePhoneNo)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OTPActivityForForgotPsw.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {

                pinView.setText(code);
                verifyCode(code);
                progressBar.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(OTPActivityForForgotPsw.this, "Invalid Request was made", Toast.LENGTH_LONG).show();

            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(OTPActivityForForgotPsw.this, "The SMS quota for the project exceeded", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(OTPActivityForForgotPsw.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCodeBySystem = s;
            //mResendToken = forceResendingToken;
        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signInWithPhoneAuthCredential(credential);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {

                        Intent intent = new Intent(OTPActivityForForgotPsw.this, UpdatePasswordActivity.class);
                        intent.putExtra("contact", contact);
                        intent.putExtra("contactForUpdate", _phoneNumber);
                        startActivity(intent);


                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OTPActivityForForgotPsw.this, "Verification not completed! Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}