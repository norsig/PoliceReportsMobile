package com.keniobyte.bruino.minsegapp.features.section_list_missing;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.section_list_missing.adapter.MissingPersonAdapterRecycler;
import com.keniobyte.bruino.minsegapp.models.Person;
import com.keniobyte.bruino.minsegapp.network.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.views.base.BasePresenter;

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

public class SectionMissingPersonPresenter extends BasePresenter<SectionMissingPersonActivity> implements ISectionMissingPersonPresenter {
    private Context context;
    private SectionMissingPersonActivity sectionMissingPersonView;

    public SectionMissingPersonPresenter(Context context) {
        this.context = context;
    }

    public void addView(SectionMissingPersonActivity sectionWantedPersonView){
        this.sectionMissingPersonView = sectionWantedPersonView;
    }

    @Override
    public void setPersonsInList() {
        sectionMissingPersonView.showProgress();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , context.getResources().getString(R.string.url_missing_persons)
                , null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(sectionMissingPersonView.getClass().getSimpleName(), response.toString());
                try {
                    MissingPersonAdapterRecycler adapterRecycler = sectionMissingPersonView.createRecyclerViewAdapter(getListMissingPerson(response));
                    sectionMissingPersonView.setMissingRecyclerViewAdapter(adapterRecycler);
                    sectionMissingPersonView.hideProgress();
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(sectionMissingPersonView.getClass().getSimpleName(), error.toString());
                sectionMissingPersonView.connectionErrorMessage();
            }
        });

        MinSegAppSingleton.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void itemMissingPerson(Person missingPerson) {
        sectionMissingPersonView.navigationToMissingPerson(missingPerson);
    }

    private List<Person> getListMissingPerson(JSONObject jsonObject) throws JSONException, ParseException {
        List<Person> missingPersons = new ArrayList<>();
        for (int i = 0; i < jsonObject.getJSONArray("r").length(); i++){
            if (!jsonObject.getJSONArray("r").getJSONObject(i).getBoolean("missing_found")){
                Person person = new Person();
                person.setId(jsonObject.getJSONArray("r").getJSONObject(i).getInt("id"));
                person.setFullName(jsonObject.getJSONArray("r").getJSONObject(i).getString("last_name"));
                person.setLastTimeSee(convertToDate(jsonObject.getJSONArray("r").getJSONObject(i).getString("last_time_seen")));
                person.setAge(jsonObject.getJSONArray("r").getJSONObject(i).getInt("age"));
                person.setUrlProfile(context.getResources().getString(R.string.url_base_picture_missing)
                        + jsonObject.getJSONArray("r").getJSONObject(i).getString("picture"));
                //person.setDescription();

                missingPersons.add(person);
            }
        }
        return missingPersons;
    }

    private Date convertToDate(String string) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(string);
    }
}
