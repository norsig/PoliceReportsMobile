package com.keniobyte.bruino.minsegapp.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *  Login activity. Only used if you want citizens to send law compliance police reports.
 *  Fully functional but not yet implemented. This allows to verify user accounts through SMS
 *  and they need to concur to the corresponding police station one they submitted their report within 72hs.
 */

public class LoginActivity extends ActionBarActivity {

    private static final String TAG = "Login Activity";

    private Context mContext = this;
    private String urlLogin = "/endpoint/call/json/login";

    //TODO: internationalization
    @NotEmpty(message = "Su Número de Documento es requerido")
    private EditText editTextSsn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setToolbar();

        final Validator validator = new Validator(mContext);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                try {
                    verifyUser(editTextSsn.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(mContext);

                    // Display error messages ;)
                    if (view instanceof EditText) {
                        ((EditText) view).setError(message);
                    } else {
                        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        editTextSsn = (EditText) findViewById(R.id.editTextSsn);
        Button buttonSignUp = (Button) findViewById(R.id.buttonSignUP);

        Button buttonLogin = (Button) findViewById(R.id.btnLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void verifyUser(final String ssn) throws JSONException {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST
                , MinSegAppSingleton.URL_BASE + urlLogin, new JSONObject().put("ssn", ssn)
                .put("code_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date().getTime()))
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("login_successful")) {
                        Intent intent = new Intent(getApplicationContext(), SignUpValidationActivity.class);
                        intent.putExtra("ssn", editTextSsn.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Usuario no Registrado", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error Volley: " + error.toString());
            }
        });
        MinSegAppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);
    }
}