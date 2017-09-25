package br.com.brolam.projectm;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import br.com.brolam.projectm.adapters.JobAdapter;
import br.com.brolam.projectm.adapters.holders.JobHolder;
import br.com.brolam.projectm.data.DataBaseProvider;
import br.com.brolam.projectm.data.models.Job;
import br.com.brolam.projectm.data.models.UserAccount;
import br.com.brolam.projectm.data.models.UserProperties;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ValueEventListener, JobHolder.JobHolderClickable, TabLayout.OnTabSelectedListener, View.OnClickListener {
    private static int REQUEST_CODE_PRICING_SELECT = 100;
    private static int REQUEST_CODE_JOB_SELECT = 101;

    private FirebaseAuth firebaseAuth;
    private DataBaseProvider dataBaseProvider = null;
    private HashMap<String, Object> userProperties = null;
    private HashMap<String, Object> userAccount = null;

    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        this.recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.jobAdapter = new JobAdapter(this,this);
        this.recyclerView.setAdapter(this.jobAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        starDataBaseProvider();
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.starDataBaseProvider();
        if ( this.dataBaseProvider  == null){
            SignInActivity.doLogin(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.pauseDataBaseProvider();
    }

    public void starDataBaseProvider() {
        FirebaseUser firebaseUser = this.firebaseAuth.getCurrentUser();
        if ( firebaseUser != null) {
            this.dataBaseProvider = new DataBaseProvider(firebaseUser);
            this.dataBaseProvider.addUserAccountListener(this);
            this.dataBaseProvider.addUserPropertiesListener(this);
            this.dataBaseProvider.addQueryJobsListener(this.jobAdapter);
        }
    }

    public void pauseDataBaseProvider() {
        if ( this.dataBaseProvider != null) {
            this.dataBaseProvider.removeUserAccountListener(this);
            this.dataBaseProvider.removeUserPropertiesListener(this);
            this.dataBaseProvider.removeQueryJobsListener(this.jobAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_log_off) {
            this.firebaseAuth.signOut();
            this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String fullPathReference = dataSnapshot.getRef().toString();
        if (UserAccount.isReferenceUserAccount(fullPathReference)) {
            this.userAccount = (HashMap<String, Object>) dataSnapshot.getValue();
            if (UserAccount.isValid(this.userAccount) == false ){
                PricingActivity.select(this, REQUEST_CODE_PRICING_SELECT);
            }
        } else if (UserProperties.isReferenceUserAccount(dataSnapshot.getRef())) {
            this.userProperties = (HashMap<String, Object>) dataSnapshot.getValue();
        }
        updateViewUserProfile();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private void updateViewUserProfile() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        if ( this.userProperties != null){
            FirebaseUser firebaseUser = this.firebaseAuth.getCurrentUser();
            String fullName = UserProperties.getDisplayName( this.userProperties);
            ((TextView)headerView.findViewById(R.id.userFullName)).setText(fullName);
            ((TextView)headerView.findViewById(R.id.userEmail)).setText(firebaseUser.getEmail());
        }

        if ( this.userAccount != null){
            String userAccountType = this.userAccount.get(UserAccount.TYPE).toString();
            ((TextView)headerView.findViewById(R.id.userAccountType)).setText(userAccountType);
        }
    }

    @Override
    public void onJobClick(String jobKey, HashMap job) {
        JobActivity.show(this, this.userProperties, jobKey, job, REQUEST_CODE_JOB_SELECT);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Job.JobType jobTypeForTab = Job.JobType.values()[tab.getPosition()];
        this.jobAdapter.setSelectedJobType(jobTypeForTab);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {
        MapJobsActivity.show(this);
    }
}
