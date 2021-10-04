package android.example.employnepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowProfile extends AppCompatActivity {

    TextView showFirstName, showLastName, showEmail, showContact, showLocation, showGender, showDOB;
    Button btnBack;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();

        databaseReference= FirebaseDatabase.getInstance().getReference("users"+"/"+user.getUid()+"/");

        showFirstName = findViewById(R.id.shown_firstname_db);
        showLastName = findViewById(R.id.shown_lastname_db);
        showEmail = findViewById(R.id.shown_email_db);
        showContact = findViewById(R.id.shown_contact_db);
        showLocation = findViewById(R.id.shown_location_db);
        showGender = findViewById(R.id.shown_gender_db);
        showDOB = findViewById(R.id.shown_dob_db);

//        btnBack=findViewById(R.id.back_button_showProfile);
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ShowProfile.this,DashboardJobSeeker.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = snapshot.child("firstName").getValue(String.class);
                String lastName = snapshot.child("lastName").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String contact = snapshot.child("contact").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);
                String gender = snapshot.child("gender").getValue(String.class);
                String dob = snapshot.child("dob").getValue(String.class);

                showFirstName.setText(firstName);
                showLastName.setText(lastName);
                showEmail.setText(email);
                showContact.setText(contact);
                showLocation.setText(location);
                showGender.setText(gender);
                showDOB.setText(dob);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowProfile.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });


    }
}