package android.example.employnepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordByEmail extends AppCompatActivity {
    FirebaseAuth mAuth;
    EditText email;
    Button reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_by_email);


        mAuth = FirebaseAuth.getInstance();
        email=(EditText)findViewById(R.id.forget_password_email);
        reset=(Button)findViewById(R.id.reset_button);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetpwd();
            }
        });


    }

    private void resetpwd(){

        String emailid=email.getText().toString().trim();

        if (emailid.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailid).matches()){
            email.setError("Please enter valid email");
            email.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(emailid)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPasswordByEmail.this, "Email Sent", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ForgetPasswordByEmail.this,LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }else {
                            Toast.makeText(ForgetPasswordByEmail.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}