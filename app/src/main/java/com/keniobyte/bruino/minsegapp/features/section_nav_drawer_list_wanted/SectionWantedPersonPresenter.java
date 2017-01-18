package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.adapter.WantedPersonAdapterRecycler;
import com.keniobyte.bruino.minsegapp.model.WantedPerson;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author bruino
 * @version 09/01/17.
 */

public class SectionWantedPersonPresenter extends BasePresenter<SectionWantedPersonActivity> implements ISectionWantedPersonPresenter {
    private Context context;
    private SectionWantedPersonActivity sectionWantedPersonView;

    public SectionWantedPersonPresenter(Context context) {
        this.context = context;
    }

    public void addView(SectionWantedPersonActivity sectionWantedPersonView){
        this.sectionWantedPersonView = sectionWantedPersonView;
    }

    @Override
    public void setPersonsInList() {
        sectionWantedPersonView.showProgress();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , context.getResources().getString(R.string.url_wanted_persons)
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    WantedPersonAdapterRecycler adapterRecycler = sectionWantedPersonView.createRecyclerViewAdapter(getListWantedPerson(response));
                    sectionWantedPersonView.setWantedRecyclerViewAdapter(adapterRecycler);
                    sectionWantedPersonView.hideProgress();
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(sectionWantedPersonView.getClass().getSimpleName(), error.toString());
                sectionWantedPersonView.connectionErrorMessage();
            }
        });

        MinSegAppSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void itemWantedPerson(WantedPerson wantedPerson) {
        sectionWantedPersonView.navigationToWantedPerson(wantedPerson);
    }

    private List<WantedPerson> getListWantedPerson(JSONObject jsonObject) throws JSONException, ParseException {
        List<WantedPerson> wantedPersons = new ArrayList<>();
        for (int i = 0; i < jsonObject.getJSONArray("r").length(); i++){
            if (!jsonObject.getJSONArray("r").getJSONObject(i).getBoolean("captured")){
                WantedPerson person = new WantedPerson();
                person.setId(jsonObject.getJSONArray("r").getJSONObject(i).getInt("id"));
                person.setFullName(jsonObject.getJSONArray("r").getJSONObject(i).getString("last_name"));
                person.setLastTimeSee(convertToDate(jsonObject.getJSONArray("r").getJSONObject(i).getString("last_time_seen")));
                person.setAge(jsonObject.getJSONArray("r").getJSONObject(i).getInt("age"));
                person.setUrlProfile(context.getResources().getString(R.string.url_base_picture_wanted)
                        + jsonObject.getJSONArray("r").getJSONObject(i).getString("picture"));
                person.setCrime(jsonObject.getJSONArray("r").getJSONObject(i).getString("crime"));
                //person.setDescription();
                //person.setReward();

                wantedPersons.add(person);
            }
        }
        return wantedPersons;
    }

    private Date convertToDate(String string) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(string);
    }
}
