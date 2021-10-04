package android.example.employnepal;

import android.content.Intent;
import android.example.employnepal.databinding.ActivityDashboardJobSeekerBinding;
import android.example.employnepal.models.PostJob;

import android.example.employnepal.search.MyAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class DashboardJobSeeker extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityDashboardJobSeekerBinding binding;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
//    private EditText searchHereJobSeeker;
//    private ImageView btnSearchJobSeeker;

    DatabaseReference ref;
    FirebaseAuth auth;
    RecyclerViewAdapter adapter;
    ArrayList<PostJob> jobList = new ArrayList<>();

    RecyclerView recyclerView;

    SearchView searchView;


    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardJobSeekerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarDashboardJobSeeker.toolbarSeeker);

        mDrawer = findViewById(R.id.drawer_layout_seeker);
        nvDrawer = findViewById(R.id.nav_view_seeker);
        drawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.string.start, R.string.close);
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        nvDrawer.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        //search
        searchView = findViewById(R.id.searchView);


        auth = FirebaseAuth.getInstance();

        ref = FirebaseDatabase.getInstance().getReference();

        recyclerView = findViewById(R.id.recycler_view_job_seeker);

        adapter = new RecyclerViewAdapter(jobList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(DashboardJobSeeker.this);
        recyclerView.setLayoutManager(manager);
        try {
            //ref.child("JobProvider").child("JobPosts").child(auth.getCurrentUser().getUid()).push().addChildEventListener(new ChildEventListener()
            ref.child("JobProvider").child("JobPosts").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.hasChildren()) {

                        //for Searching
                        String jobTitle = snapshot.child("jobTitle").getValue(String.class);
                        String jobLocation = snapshot.child("jobLocation").getValue(String.class);
                        String jobDescription = snapshot.child("jobDescription").getValue(String.class);
                        //other Details
                        String numberOfEmployees = snapshot.child("numberOfEmployees").getValue(String.class);
                        String jobPostDate = snapshot.child("jobPostDate").getValue(String.class);
                        String jobCategory = snapshot.child("jobCategory").getValue(String.class);
                        String approximateWorkingHours = snapshot.child("approximateWorkingHours").getValue(String.class);
                        String jobSalary = snapshot.child("jobSalary").getValue(String.class);
                        String applyBefore = snapshot.child("applyBefore").getValue(String.class);
                        String contactInformation = snapshot.child("contactInformation").getValue(String.class);


                        PostJob data = new PostJob();

                        data.setJobTitle(jobTitle);
                        data.setJobLocation(jobLocation);
                        data.setJobDescription(jobDescription);

                        //details added
                        data.setNumberOfEmployees(numberOfEmployees);
                        data.setJobPostDate(jobPostDate);
                        data.setJobCategory(jobCategory);
                        data.setApproximateWorkingHours(approximateWorkingHours);
                        data.setJobSalary(jobSalary);
                        data.setApplyBefore(applyBefore);
                        data.setContactInformation(contactInformation);

                        jobList.add(data);
                    }
                    if (jobList.size() > 0) {
                        recyclerView.setAdapter(adapter);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(DashboardJobSeeker.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }


    private void search(String str) {
        ArrayList<PostJob> myList = new ArrayList<>();
        if (!str.equals("")) {
            for (PostJob object : jobList) {
                if (object.getJobTitle().toLowerCase().contains(str.toLowerCase())) {
                    myList.add(object);
                }

                if (object.getJobLocation().toLowerCase().contains(str.toLowerCase())) {
                    myList.add(object);
                }
                if (object.getJobDescription().toLowerCase().contains(str.toLowerCase())) {
                    myList.add(object);
                }
            }
            MyAdapter adapter = new MyAdapter(myList);
            recyclerView.setAdapter(adapter);
        }
    }
    public  class MyViewHolder extends RecyclerView.ViewHolder {

        TextView job_title, job_providers_location, job_providers_description, cardView;//cardholder ko lai

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            job_title = itemView.findViewById(R.id.job_title_rcv);
            job_providers_location = itemView.findViewById(R.id.job_providers_location);
            job_providers_description = itemView.findViewById(R.id.job_providers_description);

        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {


        private final ArrayList<PostJob> list;

        public RecyclerViewAdapter(ArrayList<PostJob> list) {
            this.list=list;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=getLayoutInflater();
            View v=inflater.inflate(R.layout.job_lists_layout,parent,false);
            MyViewHolder holder=new MyViewHolder(v);

            return holder;

//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_lists_layout, parent, false);
//            return new MyViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final PostJob list=jobList.get(position);
            holder.job_title.setText(list.getJobTitle());
            holder.job_providers_location.setText(list.getJobLocation());
            holder.job_providers_description.setText(list.getJobDescription());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jobPostDate=list.getJobPostDate();
                    String jobTitle=list.getJobTitle();
                    String jobDescription=list.getJobDescription();
                    String numberOfEmployees=list.getNumberOfEmployees();
                    String jobLocation=list.getJobLocation();
                    String jobCategory=list.getJobCategory();
                    String approximateWorkingHours=list.getApproximateWorkingHours();
                    String jobSalary=list.getJobSalary();
                    String applyBefore=list.getApplyBefore();
                    String contactInformation=list.getContactInformation();

                    Intent intent=new Intent(DashboardJobSeeker.this,ApplyJobActivity.class);
                    intent.putExtra("jobPostDate",jobPostDate);
                    intent.putExtra("jobTitle",jobTitle);
                    intent.putExtra("jobDescription",jobDescription);
                    intent.putExtra("numberOfEmployees",numberOfEmployees);
                    intent.putExtra("jobLocation",jobLocation);
                    intent.putExtra("jobCategory",jobCategory);
                    intent.putExtra("approximateWorkingHours",approximateWorkingHours);
                    intent.putExtra("jobSalary",jobSalary);
                    intent.putExtra("applyBefore",applyBefore);
                    intent.putExtra("contactInformation",contactInformation);

                    startActivity(intent);
                }
            });

        }

        // array list creates list of array which parameter is defined in our model class
//        public RecyclerViewAdapter(ArrayList<PostJob> list) {
//
//            this.list = list;
//        }


        @Override
        public int getItemCount() {
            return jobList.size(); //Recycler view ma data aauna ko lagi
        }

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(this, DashboardJobSeeker.class));
                break;
            case R.id.nav_aboutus:
                startActivity(new Intent(this, AboutUsActivity.class));
                break;
            case R.id.nav_editProfile:
                startActivity(new Intent(this, EditProfile.class));
                break;
            case R.id.nav_showProfile:
                startActivity(new Intent(this, ShowProfile.class));
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_seeker);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}