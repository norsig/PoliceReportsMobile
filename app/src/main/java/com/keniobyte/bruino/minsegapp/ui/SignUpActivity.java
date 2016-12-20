package com.keniobyte.bruino.minsegapp.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keniobyte.bruino.minsegapp.requestHandler.MinSegAppSingleton;
import com.keniobyte.bruino.minsegapp.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

public class SignUpActivity extends ActionBarActivity {

    private static final String TAG = "Sign Up";

    private Context mContext = this;
    private final String urlRegister = "/endpoint/call/json/register_citizen";

    private Spinner spinnerMaritalStatus;
    private Button btnDate;
    @NotEmpty
    private EditText editTextName, editTextLastName, editTextSsn, editTextNacionality, editTextFatherName, editTextMotherName, editTextAddress, editTextPhone, editTextOccupation;
    @NotEmpty
    @Email
    private EditText editTextEmail;
    private Calendar myCalendar = Calendar.getInstance();
    //Date popup method
    private DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            btnDate.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(myCalendar.getTime()));
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setToolbar();

        final Validator validator = new Validator(mContext);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                if (!spinnerMaritalStatus.getSelectedItem().toString().equals(getResources().getString(R.string.marital_status))) {
                    Log.i(TAG, "Validation Succeded");
                    try {
                        //Send sign up data
                        sendRegister();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(mContext, getResources().getString(R.string.error_marital_status)
                            , Toast.LENGTH_LONG).show();
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

        //EditText instance
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLastName = (EditText) findViewById(R.id.editTextLastName);
        editTextSsn = (EditText) findViewById(R.id.editTextSsn);
        editTextNacionality = (EditText) findViewById(R.id.editTextNacionality);
        editTextFatherName = (EditText) findViewById(R.id.editTextFatherName);
        editTextMotherName = (EditText) findViewById(R.id.editTextMotherName);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextPhone = (EditText) findViewById(R.id.editTextPhone);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextOccupation = (EditText) findViewById(R.id.editTextOccupation);


        final String[] status = new String[]{getResources().getString(R.string.single)
                , getResources().getString(R.string.marrial)
                , getResources().getString(R.string.widow)
                , getResources().getString(R.string.divorced)};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, status);

        btnDate = (Button) findViewById(R.id.btnBirthday);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Date popup call
                new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        spinnerMaritalStatus = (MaterialSpinner) findViewById(R.id.spinnerMarginalStatus);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaritalStatus.setAdapter(adapter);
        spinnerMaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, (String) spinnerMaritalStatus.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button buttonSendRegister = (Button) findViewById(R.id.buttonSendRegister);
        buttonSendRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });

    }

    private void sendRegister() throws JSONException {
        JSONObject registration = new JSONObject();
        registration.put("name", editTextName.getText().toString())
                .put("last_name", editTextLastName.getText().toString())
                .put("ssn", editTextSsn.getText().toString())
                .put("nationality", editTextNacionality.getText().toString())
                .put("birth_date", new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(myCalendar.getTime()))
                .put("father_name", editTextFatherName.getText().toString())
                .put("mother_name", editTextMotherName.getText().toString())
                .put("address", editTextAddress.getText().toString())
                .put("phone", editTextPhone.getText().toString())
                .put("email", editTextEmail.getText().toString())
                .put("marital_status", spinnerMaritalStatus.getSelectedItem().toString())
                .put("occupation", editTextOccupation.getText().toString());

        Log.i(TAG, registration.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MinSegAppSingleton.URL_BASE + urlRegister,
                registration, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    switch (response.getString("status")) {
                        case "already_registered":
                            Toast.makeText(mContext, getResources().getString(R.string.already_registered)
                                    , Toast.LENGTH_LONG).show();
                            Intent intentLogin = new Intent(mContext, LoginActivity.class);
                            startActivity(intentLogin);
                            finish();
                            break;
                        case "registered":
                            Intent intentSignUpValidation = new Intent(getApplicationContext(), SignUpValidationActivity.class);
                            intentSignUpValidation.putExtra("ssn", editTextSsn.getText().toString());
                            startActivity(intentSignUpValidation);
                            finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Error Response: " + error.getMessage());
            }
        });
        MinSegAppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(getResources().getString(R.string.title_form));
        setSupportActionBar(toolbar);
    }
}
