package com.keniobyte.bruino.minsegapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_home.SectionHomeFragment;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing.SectionMissingPersonActivity;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.SectionWantedPersonActivity;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_police_report.SectionPoliceReportActivity;
import com.keniobyte.bruino.minsegapp.features.section_police_stations.PoliceStationsActivity;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String TAG = getClass().getSimpleName();

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.nav_view) NavigationView navView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;

    @BindString(R.string.app_name) String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        //http://stackoverflow.com/questions/37491093/android-navigationdrawer-overlap-by-status-bar#37491463
        RelativeLayout navigationHeader = (RelativeLayout) navView.getHeaderView(0);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // This is make sure navigation header is below status bar
            // This only required for devices API >= 21
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) navigationHeader.getLayoutParams();
            Log.i(TAG, String.valueOf(getStatusBarHeight()));
            layoutParams.setMargins(0, getStatusBarHeight(), 0, 0);
            navigationHeader.setLayoutParams(layoutParams);
        }

        navView.setNavigationItemSelectedListener(this);
        navView.setCheckedItem(R.id.nav_home);
        navView.getMenu().performIdentifierAction(R.id.nav_home, 0);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main2, new SectionHomeFragment())
                    .commit();

        } else if (id == R.id.nav_station_police) {
            startActivity(new Intent(this, PoliceStationsActivity.class));

        } else if (id == R.id.nav_report_police) {
            startActivity(new Intent(this, SectionPoliceReportActivity.class));

        } else if (id == R.id.nav_wanted) {
            startActivity(new Intent(this, SectionWantedPersonActivity.class));

        } else if (id == R.id.nav_missing) {
            startActivity(new Intent(this, SectionMissingPersonActivity.class));
        }
        navView.setCheckedItem(R.id.nav_home);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
