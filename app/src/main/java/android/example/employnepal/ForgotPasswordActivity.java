package android.example.employnepal;

import android.content.Intent;

import android.example.employnepal.models.CheckInternet;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button btnBackForgotPsw, btnNextForgotPsw;
    private TextView title, description;
    private EditText phoneNumberTextField;
    private CountryCodePicker countryCodePicker;
    private Animation animation;
    ProgressBar progressBar;
    private ImageView screenIcon;
    String updateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //to remove the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        screenIcon = findViewById(R.id.forget_password_icon);
        title = findViewById(R.id.forget_password_title);
        description = findViewById(R.id.forget_password_description);
        phoneNumberTextField = findViewById(R.id.forget_password_phone_number);
        countryCodePicker = findViewById(R.id.country_code_picker);
        btnNextForgotPsw = findViewById(R.id.forget_password_next_btn);
        progressBar = findViewById(R.id.progressbarResetPsw);
//
//        animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
//
//        //set animation to all the elements
//        screenIcon.setAnimation(animation);
//        title.setAnimation(animation);
//        description.setAnimation(animation);
//        phoneNumberTextField.setAnimation(animation);
//        countryCodePicker.setAnimation(animation);
//        btnNextForgotPsw.setAnimation(animation);
    }

    public void verifyPhoneNumber(View view) {


        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            //showCustomDialog();
            Toast.makeText(this, "You are not connected to internet!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!validatePhone()) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        String _phoneNumber = phoneNumberTextField.getText().toString().trim();
//                if (_phoneNumber.charAt(0) == '0') {
//                    _phoneNumber = _phoneNumber.substring(1);
//                }
        String _completePhoneNumber = "+" + countryCodePicker.getSelectedCountryCode() + _phoneNumber;
        Query checkUser = FirebaseDatabase.getInstance().getReference("users").orderByChild("contact").equalTo(_phoneNumber);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //if phone number exists then call OTP to verify that it is his/her phone number
                if (snapshot.exists()) {
                    phoneNumberTextField.setError(null);
                    //phoneNumberTextField.setErrorEnabled(false);
                    Intent intent = new Intent(ForgotPasswordActivity.this, OTPActivityForForgotPsw.class);
                    intent.putExtra("contact", _completePhoneNumber);
                    intent.putExtra("contactForUpdate",_phoneNumber);
//                            intent.putExtra("whatTODO", "updateData");
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    phoneNumberTextField.setError("No such user exists!");
                    phoneNumberTextField.requestFocus();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private boolean validatePhone() {
        String phoneNumber = phoneNumberTextField.getText().toString().trim();
        if (phoneNumber.isEmpty()) {
            phoneNumberTextField.setError("Please enter phone number");
            return false;

        } else if (!(phoneNumber.length() == 10)) {
            phoneNumberTextField.setError("Please enter 10 digits");
            return false;


        } else {
            phoneNumberTextField.setError(null);
            return true;
            //return false;
        }

    }
}