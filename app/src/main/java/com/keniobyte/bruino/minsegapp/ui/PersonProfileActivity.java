package com.keniobyte.bruino.minsegapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.keniobyte.bruino.minsegapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PersonProfileActivity extends ActionBarActivity {

    private final String TAG = getClass().getSimpleName();

    private TextView textName, textAge, textView;
    private ImageView imageViewProfile;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        textName = (TextView) findViewById(R.id.tNamePerson);
        textAge = (TextView) findViewById(R.id.tAgePerson);
        textView = (TextView) findViewById(R.id.tField);


        final Bundle bundle = this.getIntent().getExtras();
        if (bundle.getString("crime") == null) {
            textView.setText(bundle.getString("lastTimeSee"));
            setToolbar(getResources().getString(R.string.section_missing));
        } else {
            textView.setText(bundle.getString("crime"));
            setToolbar(getResources().getString(R.string.section_wanted));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textName.setText(bundle.getString("name"));
        textAge.setText(bundle.getString("age"));
        imageViewProfile = (ImageView) findViewById(R.id.imagePerson);

        Picasso.with(this).load(bundle.getString("imageURL")).into(imageViewProfile);

        Button buttonContributeData = (Button) findViewById(R.id.buttonContributeData);
        buttonContributeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PersonProfileContributeActivity.class);
                //Checks if it's Missing person or criminal
                if (bundle.getString("crime") == null) {
                    intent.putExtra("type", "Missing");
                } else {
                    intent.putExtra("type", "Criminal");
                }
                intent.putExtra("id", bundle.getInt("id"));
                startActivity(intent);
            }
        });

        ImageButton buttonSharePerson = (ImageButton) findViewById(R.id.sharePerson);
        buttonSharePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareItem(imageViewProfile);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressImageView);
        progressBar.setVisibility(View.INVISIBLE);
        imageViewProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public void setToolbar(String title) {
        toolbar = (Toolbar) findViewById(R.id.myToolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    public void onShareItem(ImageView v) {
        StringBuilder txt = new StringBuilder();
        txt.append(toolbar.getTitle())
                .append("\n" + getResources().getString(R.string.name) + ": ")
                .append(textName.getText().toString())
                .append("\n" + getResources().getString(R.string.age) + ": ")
                .append(textAge.getText().toString())
                .append("\n" + textView.getText().toString())
                .append("\n" + getResources().getString(R.string.section_provideData))
                .append("\n" + getString(R.string.call_911));
        // Get access to bitmap image from view
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(v);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, txt.toString());
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_share), Toast.LENGTH_LONG).show();
        }
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp;

        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "person_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmpUri;
    }
}
