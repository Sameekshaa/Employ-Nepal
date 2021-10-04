package android.example.employnepal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.example.employnepal.models.PostJob;
import android.example.employnepal.models.SaveUserProfile;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class EditProfile extends AppCompatActivity implements View.OnClickListener{

    EditText editFirstName, editLastName, editEmail, editContact, editLocation, editDOB;

    RadioGroup u_radioGroup;
    RadioButton male, female, others;
    Button btnBack;

    ImageView editDate;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    StorageReference mStorageReference;
    DatabaseReference databaseReference,mDatabaseReference;

    private String gender = "";

//constants
    public static final String DATABASE_PATH_UPLOADS= "users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS +"/"+user.getUid()+"/");

        editFirstName = findViewById(R.id.edit_firstname_db);
        editLastName = findViewById(R.id.edit_lastname_db);
        editEmail = findViewById(R.id.edit_email_db); //email lai textView banako
        editContact = findViewById(R.id.edit_contact_db);
        editLocation = findViewById(R.id.edit_location_db);

        editDOB = findViewById(R.id.edit_dob_db);
        editDate = findViewById(R.id.edit_date);

        u_radioGroup = findViewById(R.id.radioGroupEditProfile);
        male = findViewById(R.id.male_edtProfile);
        female = findViewById(R.id.female_edtProfile);
        others = findViewById(R.id.others_edtProfile);
//        female.setChecked(true);

//        btnBack=findViewById(R.id.back_button_editProfile);
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(EditProfile.this,DashboardJobSeeker.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//            }
//        });




        findViewById(R.id.btn_editProfile).setOnClickListener(this);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName = snapshot.child("firstName").getValue(String.class);
                String lastName = snapshot.child("lastName").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String contact = snapshot.child("contact").getValue(String.class);
                String location = snapshot.child("location").getValue(String.class);
//                String gender = snapshot.child("gender").getValue(String.class);

                String dob = snapshot.child("dob").getValue(String.class);

                editFirstName.setText(firstName);
                editLastName.setText(lastName);
                editEmail.setText(email);
                editContact.setText(contact);
                editLocation.setText(location);
//                editGender.setText(gender);

                editDOB.setText(dob);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfile.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        //edit
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog=new DatePickerDialog(EditProfile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date=dayOfMonth+"/"+(month+1)+"/"+year;
                        editDOB.setText(date);

                    }
                },year,month,day);
                dialog.show();
            }
        });
    }

    //to upload user data
    private void SaveProfile(){
       // RadioGroup rg = findViewById(R.id.radioGroupEditProfile);

        String firstName = editFirstName.getText().toString().trim();
        String lastName= editLastName.getText().toString().trim();
        String email= editEmail.getText().toString().trim();
        String contact= editContact.getText().toString().trim();
        String location = editLocation.getText().toString().trim();
       // String gender =((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString().trim();
        String dob = editDOB.getText().toString();

//        if(first_name.isEmpty()){
//            u_first_name.setError("First Name Required");
//            u_first_name.requestFocus();
//            return;
//        }
//        if(last_name.isEmpty()){
//            u_last_name.setError("Last Name Required");
//            u_last_name.requestFocus();
//            return;
//        }
//        if(location.isEmpty()){
//            u_location.setError("Location Required");
//            u_location.requestFocus();
//            return;
//        }
//        if(dob.isEmpty()){
//            u_dob.setError("DOB Required");
//            u_dob.requestFocus();
//            return;
//        }
        if (female.isChecked()) {
            gender = "Female";
        } else if (male.isChecked()) {
            gender = "Male";

        } else {
            gender = "Others";
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
//        EMAIL CHANGE GARNA NADINA PARLA !!!!
       // SaveUserProfile sp = new SaveUserProfile(firstName, lastName, contact, location, gender, dob);

        //databaseReference.child("users").child(user.getUid()).setValue(sp);
        databaseReference.child("users").child(user.getUid()).child("firstName").setValue(firstName);
        databaseReference.child("users").child(user.getUid()).child("lastName").setValue(lastName);
        databaseReference.child("users").child(user.getUid()).child("email").setValue(email);
        databaseReference.child("users").child(user.getUid()).child("contact").setValue(contact);
        databaseReference.child("users").child(user.getUid()).child("location").setValue(location);
        databaseReference.child("users").child(user.getUid()).child("gender").setValue(gender);
        databaseReference.child("users").child(user.getUid()).child("dob").setValue(dob);


//        databaseReference.child("users").child(user.getUid()).setValue(sp);
        Toast.makeText(EditProfile.this, "Successfully Edited Profile", Toast.LENGTH_SHORT).show();

    }

    public void onClick(View v){
        SaveProfile();
    }


}