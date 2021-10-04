package android.example.employnepal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class PasswordUpdateSuccessActivity extends AppCompatActivity {
    private Button btnBackToLoginFromUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_password_update_success);

        btnBackToLoginFromUpdate=findViewById(R.id.success_message_btn);

        btnBackToLoginFromUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PasswordUpdateSuccessActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
}