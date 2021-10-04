package android.example.employnepal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.example.employnepal.models.PostJob;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class PostActivity extends AppCompatActivity {
    Spinner categorySpinner;
    MenuItem menuItem;
    RadioButton radioFixed, radioRange, radioNegotiable;
    RadioGroup radioGroup;
    PostJob jobPost;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    long i = 0;
    private EditText jobTitle, jobDescription, numberOfEmployees, jobLocation, workingHours, contactInfo;
    private TextView jobDate, applyBefore;
    ImageView postDate, applyDate;
    String salary = "";
    private Button btnPost,btnBack;
    DatePickerDialog.OnDateSetListener setListener;
    String[] category = new String[]{"Part-time", "Full-time"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        reference = FirebaseDatabase.getInstance().getReference("JobProvider");
        auth = FirebaseAuth.getInstance();

        applyBefore = findViewById(R.id.apply_before);
        jobDate = findViewById(R.id.et_date);

        radioGroup = findViewById(R.id.radio);
        categorySpinner = findViewById(R.id.category_spinner);

        jobTitle = findViewById(R.id.job_title);
        jobDescription = findViewById(R.id.job_description);
        numberOfEmployees = findViewById(R.id.number_of_employees);
        jobLocation = findViewById(R.id.job_location);
        workingHours = findViewById(R.id.working_hours);
        applyBefore = findViewById(R.id.apply_before);
        contactInfo = findViewById(R.id.contact_info);
        postDate = findViewById(R.id.post_date);
        applyDate = findViewById(R.id.apply_date);
        radioFixed = findViewById(R.id.fixed);
        radioRange = findViewById(R.id.range);
        radioNegotiable = findViewById(R.id.negotiable);
        btnPost = findViewById(R.id.btnPost);
        btnBack=findViewById(R.id.back_button_post);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PostActivity.this,DashboardJobProvider.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        postDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        jobDate.setText(date);

                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });
        applyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PostActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        applyBefore.setText(date);

                    }
                }, year, month, day);
                datePickerDialog.show();

            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = auth.getCurrentUser();

                String jobtitle = jobTitle.getText().toString().trim();
                String jobdescription = jobDescription.getText().toString().trim();
                String numberofemployees = numberOfEmployees.getText().toString().trim();
                String joblocation = jobLocation.getText().toString().trim();
                String categoryspinner = categorySpinner.getSelectedItem().toString().trim();
                String approximateworkinghours = workingHours.getText().toString().trim();
                String applybefore = applyBefore.getText().toString().trim();
                String jobdate=jobDate.getText().toString().trim();
                String contactinformation = contactInfo.getText().toString().trim();

                if (TextUtils.isEmpty(jobtitle)) {
                    jobTitle.setError("Please enter job title");
                    jobTitle.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(jobdescription)) {
                    jobDescription.setError("Please enter job description");
                    jobDescription.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(numberofemployees)) {
                    numberOfEmployees.setError("Please enter number of employees");
                    numberOfEmployees.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(joblocation)) {
                    jobLocation.setError("Please enter your job location");
                    jobLocation.requestFocus();
                    return;
                }
                if (radioFixed.isChecked()) {
                    salary = "Fixed";
                } else if (radioRange.isChecked()) {
                    salary = "Range";

                } else {
                    salary = "Negotiable";
                }
                if (TextUtils.isEmpty(applybefore)) {
                    applyBefore.setError("Deadline Required ");
                    applyBefore.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(contactinformation)) {
                    applyBefore.setError("Please enter your contact information");
                    applyBefore.requestFocus();
                    return;
                }

                jobPost = new PostJob(jobdate,jobtitle, jobdescription, numberofemployees, joblocation,categoryspinner, approximateworkinghours, salary, applybefore, contactinformation);
                try {
//                    reference.child("JobPosts").push().child("JobDetails").setValue(jobPost);
                    reference.child("JobPosts").push().setValue(jobPost);

                    Toast.makeText(PostActivity.this, "Job Posted Successfully", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(PostActivity.this, "Job Posting failed...", Toast.LENGTH_LONG).show();
                }



//                Intent intent = new Intent(PostActivity.this, SearchJobPostActivity.class);
//                startActivity(intent);


            }
        });
    }


}

