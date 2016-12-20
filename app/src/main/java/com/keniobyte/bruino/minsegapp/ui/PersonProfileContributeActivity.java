package com.keniobyte.bruino.minsegapp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.MainActivity;
import com.keniobyte.bruino.minsegapp.model.SendReport;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PersonProfileContributeActivity extends ActionBarActivity {

    private final String TAG = getClass().getSimpleName();

    private Context context = this;
    private Bundle bundle = null;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private String urlContribute = null;

    //TODO: internationalization
    @NotEmpty(message = "Complete la Descripción")
    private EditText editTextDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_person_profile_contribute);
        setToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bundle = this.getIntent().getExtras();

        final Validator validator = new Validator(context);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                try {
                    sendContribute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getApplicationContext());

                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        editTextDescription = (EditText) findViewById(R.id.editTextContribute);

        Button buttonSend = (Button) findViewById(R.id.buttonSendReport);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
    }

    private void sendContribute() throws JSONException {
        JSONObject report = new JSONObject();
        report.put("description", editTextDescription.getText().toString());

        if (bundle.getString("type").equals("Missing")){
            report.put("missing_data_id", bundle.getInt("id"));
            //Missing flag
            urlContribute = SendReport.URL_REPORT_MISSING;
        } else {
            report.put("wanted_data_id", bundle.getInt("id"));
            //Wanted flag
            urlContribute = SendReport.URL_REPORT_WANTED;
        }

        createProgressDialog();
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        createAlertDialog();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SendReport.URL_BASE+urlContribute,
                report, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                progressDialog.dismiss();

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getResources().getString(R.string.accept)
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                Intent intent = new Intent(context, MainActivity.class);
                                context.startActivity(intent);
                                ((Activity) context).finish();
                            }
                        });
                alertDialog.setMessage(context.getResources().getString(R.string.send_success));
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();

                alertDialog.setMessage(context.getResources().getString(R.string.send_failure));
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });
        MinSegAppSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.section_provideData));
        setSupportActionBar(toolbar);
    }

    private void createProgressDialog(){
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getResources().getString(R.string.sending));
        progressDialog.setMessage(context.getApplicationContext().getResources().getString(R.string.wait_please));
    }

    private void createAlertDialog(){
        alertDialog = new AlertDialog.Builder(PersonProfileContributeActivity.this).create();
        alertDialog.setTitle(getResources().getString(R.string.section_police_report));
    }
}