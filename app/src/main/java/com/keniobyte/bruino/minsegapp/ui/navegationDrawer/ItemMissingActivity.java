package com.keniobyte.bruino.minsegapp.ui.navegationDrawer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.model.FindOutPerson;
import com.keniobyte.bruino.minsegapp.ui.adapters.ItemPersonAdapterRecycler;
import com.keniobyte.bruino.minsegapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Missing/Wanted person's list.
 */
public class ItemMissingActivity extends ActionBarActivity {

    private final String TAG = getClass().getSimpleName();

    private static final String urlMissing = "/endpoint/call/json/missing";
    private static final String urlInit = "https://keniobyte.com/minsegbe/static/missing/";

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private JSONObject missingList = null;

    public ItemMissingActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_missing);
        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewMissing);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progressIndeterminateMissing);
        progressBar.setVisibility(View.VISIBLE);

        try {
            setAdapterListPerson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to previous activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAdapterListPerson() throws JSONException {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , MinSegAppSingleton.URL_BASE + urlMissing
                , null
                , new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        missingList = response;

                        try {
                            ItemPersonAdapterRecycler adapterRecycler = new ItemPersonAdapterRecycler(getListPerson());
                            recyclerView.setAdapter(adapterRecycler);
                            adapterRecycler.notifyDataSetChanged();

                            progressBar.setVisibility(View.INVISIBLE);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error Volley: "+error.toString());
                        new CountDownTimer(5000, 1000){
                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                try {
                                    setAdapterListPerson();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                });
        MinSegAppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private List<FindOutPerson> getListPerson() throws JSONException, ParseException {
        List<FindOutPerson> persons = new ArrayList<>();

        for (int i = 0; i < missingList.getJSONArray("r").length(); i++){
            String last_time_see = ConvertFormatDate(missingList.getJSONArray("r").getJSONObject(i).get("last_time_seen").toString());
            String name = missingList.getJSONArray("r").getJSONObject(i).get("last_name").toString()+" "+
                    missingList.getJSONArray("r").getJSONObject(i).get("name").toString();
            int id = missingList.getJSONArray("r").getJSONObject(i).getInt("id");
            String urlProfileImage = urlInit + missingList.getJSONArray("r").getJSONObject(i).get("picture").toString();
            String age = missingList.getJSONArray("r").getJSONObject(i).get("age").toString() + " " +getResources().getString(R.string.year);

            persons.add(new FindOutPerson(id, name, last_time_see, age, null, urlProfileImage));
        }

        return persons;
    }

    private String ConvertFormatDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormatInput = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date date = dateFormatInput.parse(dateString);
        SimpleDateFormat dateFormatOutput = new SimpleDateFormat("dd MMM yyyy", Locale.US);
        return dateFormatOutput.format(date);
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.section_missing));
        setSupportActionBar(toolbar);
    }
}
