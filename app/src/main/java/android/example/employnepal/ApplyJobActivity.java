package android.example.employnepal;

import android.content.Intent;
import android.example.employnepal.models.PostJob;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ApplyJobActivity extends AppCompatActivity {
    TextView jobPostDate, jobTitle, jobDescription, numberOfEmployees, jobLocation, jobCategory, approximateWorkingHours, jobSalary, applyBefore, contactInformation;
    Button apply, btnBackApply;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        jobPostDate = findViewById(R.id.apply_job_date_db);
        jobTitle = findViewById(R.id.apply_job_title_db);
        jobDescription = findViewById(R.id.apply_job_description_db);
        numberOfEmployees = findViewById(R.id.apply_job_numberofEmp_db);
        jobLocation = findViewById(R.id.apply_job_location_db);
        jobCategory = findViewById(R.id.apply_job_category_db);
        approximateWorkingHours = findViewById(R.id.apply_job_workingHrs_db);
        jobSalary = findViewById(R.id.apply_job_salary_db);
        applyBefore = findViewById(R.id.apply_job_before_db);
        contactInformation = findViewById(R.id.apply_job_contact_info_db);

        apply = findViewById(R.id.btn_apply);

        btnBackApply = findViewById(R.id.back_button_apply);

        btnBackApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ApplyJobActivity.this,DashboardJobSeeker.class);
                startActivity(intent);
            }
        });

        final String job_post_date = getIntent().getExtras().getString("jobPostDate");
        final String job_title = getIntent().getExtras().getString("jobTitle");
        final String job_description = getIntent().getExtras().getString("jobDescription");
        final String job_noOfEmp = getIntent().getExtras().getString("numberOfEmployees");
        final String job_location = getIntent().getExtras().getString("jobLocation");
        final String job_category = getIntent().getExtras().getString("jobCategory");
        final String job_approxHour = getIntent().getExtras().getString("approximateWorkingHours");
        final String job_salary = getIntent().getExtras().getString("jobSalary");
        final String job_apply_before = getIntent().getExtras().getString("applyBefore");
        final String job_contact_info = getIntent().getExtras().getString("contactInformation");

        jobPostDate.setText(job_post_date);
        jobTitle.setText(job_title);
        jobDescription.setText(job_description);
        numberOfEmployees.setText(job_noOfEmp);
        jobLocation.setText(job_location);
        jobCategory.setText(job_category);
        approximateWorkingHours.setText(job_approxHour);
        jobSalary.setText(job_salary);
        applyBefore.setText(job_apply_before);
        contactInformation.setText(job_contact_info);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseUser user = mAuth.getCurrentUser();
//                        PostJob applyJob = new PostJob(firstName, lastName, email, contact, location, gender, dob);
                        PostJob applyJob = new PostJob();
                        applyJob.setJobPostDate(job_post_date);
                        applyJob.setJobTitle(job_title);
                        applyJob.setJobDescription(job_description);
                        applyJob.setNumberOfEmployees(job_noOfEmp);
                        applyJob.setJobLocation(job_location);
                        applyJob.setJobCategory(job_category);
                        applyJob.setApproximateWorkingHours(job_approxHour);
                        applyJob.setJobSalary(job_salary);
                        applyJob.setApplyBefore(job_apply_before);
                        applyJob.setContactInformation(job_contact_info);

                        databaseReference.child("JobProvider").child("JobAppliedBySeeker").child(mAuth.getCurrentUser().getUid()).push().setValue(applyJob);

                        Toast.makeText(ApplyJobActivity.this, "Job Applied", Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
}