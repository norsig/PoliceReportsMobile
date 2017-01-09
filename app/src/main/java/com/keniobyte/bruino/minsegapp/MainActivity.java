package com.keniobyte.bruino.minsegapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing.SectionMissingPersonActivity;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.SectionWantedPersonActivity;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_police_report.SectionPoliceReportActivity;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.ui.navegationDrawer.ItemInitial;
import com.keniobyte.bruino.minsegapp.ui.navegationDrawer.ItemStationPoliceActivity;

import org.json.JSONObject;

/**
 * Main activity: checks if user is logged in(fully functional but not implemented yet) and downloads police stations kml
 *
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Main Activity";

    private static String stationsUrl = "/endpoint/call/json/police_stations";
    private SharedPreferences preferences = null;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        // Fully functional but not implemented: Login option to allow citizens to send law compliance police reports.

        /*
        //Adds citizen's SSN
        if (preferences.getInt("ssn", 0) == 0){
            //Not logged in, showing sign up until then.
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }*/

        preferences = getSharedPreferences("minsegapp", Context.MODE_PRIVATE);
        //Downloads police station's locations from KML file
        if (preferences.getString("police_station", "").isEmpty()){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                    , MinSegAppSingleton.URL_BASE+ stationsUrl
                    , null
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("police_station", response.toString());
                    editor.apply();
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.i(TAG, "Error Volley: "+error.toString());
                }
            });
            MinSegAppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_init);
        navigationView.getMenu().performIdentifierAction(R.id.nav_init, 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_init) {
            // Handle the camera action
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_main2, new ItemInitial())
                    .commit();

        } else if (id == R.id.nav_station_police) {
            startActivity(new Intent(getApplicationContext(), ItemStationPoliceActivity.class));

        } else if (id == R.id.nav_report_police) {
            startActivity(new Intent(getApplicationContext(), SectionPoliceReportActivity.class));

        } else if (id == R.id.nav_wanted) {
            startActivity(new Intent(getApplicationContext(), SectionWantedPersonActivity.class));

        } else if (id == R.id.nav_missing) {
            startActivity(new Intent(getApplicationContext(), SectionMissingPersonActivity.class));
        }
        navigationView.setCheckedItem(R.id.nav_init);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
