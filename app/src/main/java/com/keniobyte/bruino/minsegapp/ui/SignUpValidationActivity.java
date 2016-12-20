package com.keniobyte.bruino.minsegapp.ui;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.MainActivity;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.model.SmsListener;
import com.keniobyte.bruino.minsegapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SignUpValidationActivity extends ActionBarActivity {

    private static final String TAG = "Sign Up Validation";

    private Context mContext = this;
    private String urlValidation = "/endpoint/call/json/activation";
    private SharedPreferences preferences = null;
    private int MY_PERMISSIONS_REQUEST_SMS = 4;
    private Bundle bundle;

    //SMS boradcast receiver, sms comes from SmsListener
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCode.setText(intent.getStringExtra("verification_code"));
            try {
                sendCode();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    private SmsListener smsListener = new SmsListener();

    @NotEmpty()
    private EditText mCode;
    private Button btnConfirmed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_validation);
        preferences = getSharedPreferences("minsegapp", Context.MODE_PRIVATE);
        //Receiving from SignUpActivity or LoginActivity: Ssn.
        bundle = this.getIntent().getExtras();
        setToolbar();

        //Check for location permissions
        if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(mContext, Manifest.permission.RECEIVE_SMS)) {
            //No permissions. Check if user was asked previously for permissions and how he canceled it.
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, Manifest.permission.RECEIVE_SMS)) {
                //User canceled permissions, we ask again for them.
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_SMS);
            } else {
                //User rejected permissions and checked "don't ask again".
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_SMS);
            }
        }
        //BroadcastReceiver - SMS Code Verification listener
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsListener, intentFilter);

        //Finish Activity
        IntentFilter intent = new IntentFilter();
        intent.addAction("com.minsegapp.SMSverification");
        registerReceiver(receiver, intent);

        final Validator validator = new Validator(mContext);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                try {
                    sendCode();
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
        mCode = (EditText) findViewById(R.id.editTextValidation);
        btnConfirmed = (Button) findViewById(R.id.buttonConfirmed);
        btnConfirmed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        unregisterReceiver(smsListener);
    }

    private void sendCode() throws JSONException {
        JSONObject code = new JSONObject();
        code.put("verification_code", mCode.getText().toString())
                .put("ssn", bundle.getString("ssn"))
                .put("code_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date().getTime()));
        Log.i(TAG, code.toString());
        Log.i(TAG, urlValidation);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MinSegAppSingleton.URL_BASE + urlValidation
                , code, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString("status")) {
                        case "activation_successful":
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("ssn", Integer.parseInt(bundle.getString("ssn")));
                            editor.apply();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case "activation_error":
                            mCode.setError(getResources().getString(R.string.code_verificaion_error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, error.getMessage());
            }
        });
        MinSegAppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.validation));
        setSupportActionBar(toolbar);
    }
}
