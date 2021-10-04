package android.example.employnepal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.example.employnepal.models.CheckInternet;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdatePasswordActivity extends AppCompatActivity {
    private ImageView setNewPasswordIcon;
    private TextView  setNewPasswordTitle,setNewPasswordDescription;
    private EditText newPassword,confirmNewPassword;
    private Button updatePasswordBtn;
    private Animation animation;
    ConstraintLayout progressBar;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_password);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setNewPasswordIcon=findViewById(R.id.set_new_password_icon);
        setNewPasswordTitle=findViewById(R.id.set_new_password_title);
        setNewPasswordDescription=findViewById(R.id.set_new_password_description);
        newPassword=findViewById(R.id.new_password);
        confirmNewPassword=findViewById(R.id.confirm_password);
        updatePasswordBtn=findViewById(R.id.set_new_password_btn);
        progressBar = findViewById(R.id.constraintLayout1);

//        animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
//
//        setNewPasswordIcon.setAnimation(animation);
//        setNewPasswordTitle.setAnimation(animation);
//        setNewPasswordDescription.setAnimation(animation);
//        newPassword.setAnimation(animation);
//        confirmNewPassword.setAnimation(animation);
//        updatePasswordBtn.setAnimation(animation);


    }
    public void setNewPasswordBtn(View view){
        CheckInternet checkInternet = new CheckInternet();
        if (!checkInternet.isConnected(this)) {
            //showCustomDialog();
            Toast.makeText(this, "You are not connected to internet!", Toast.LENGTH_LONG).show();
            return;
        }
        if (!validateNewPassword() | !validateNewConfirmPassword()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //GET DATA FROM FIELDS
        String _newPassword=newPassword.getText().toString().trim();
        String confirmPassword=confirmNewPassword.getText().toString().trim();
        String _phoneNumber=getIntent().getStringExtra("contact");
        String _phoneNumberForUpdate=getIntent().getStringExtra("contactForUpdate");

        //update data in firebase and in sessions
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users").child(user.getUid()).child("password").setValue(_newPassword);
        reference.child("users").child(user.getUid()).child("confirmPassword").setValue(confirmPassword);

        startActivity(new Intent(UpdatePasswordActivity.this,PasswordUpdateSuccessActivity.class));


    }
    private boolean validateNewPassword(){
        String _newPassword=newPassword.getText().toString().trim();
        if (_newPassword.isEmpty()){
            newPassword.setError("Please enter password");
            return false;

        }else if ((_newPassword.length()<6)){
            newPassword.setError("Password too short, enter minimum 6 characters!");
            return false;

        }
        else{
            newPassword.setError(null);
            return true;
        }

    }
    private boolean validateNewConfirmPassword(){
        String _newPassword=newPassword.getText().toString().trim();
        String confirmPassword=confirmNewPassword.getText().toString().trim();
        if (confirmPassword.isEmpty()){
            confirmNewPassword.setError("Please enter the required field");
            return false;

        }else if (!_newPassword.equals(confirmPassword)){
            confirmNewPassword.setError("Passwords didn't match");
            return false;

        }
        else{
            confirmNewPassword.setError(null);
            return true;
        }

    }
}