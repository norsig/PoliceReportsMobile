package com.keniobyte.bruino.minsegapp.features.section_list_missing.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.views.base.BasePresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author bruino
 * @version 10/01/17.
 */

public class MissingProfilePresenter extends BasePresenter<MissingProfileActivity> implements IMissingProfilePresenter {
    private Context context;
    private MissingProfileActivity missingProfileView;

    public MissingProfilePresenter(Context context) {
        this.context = context;
    }

    public void addView(MissingProfileActivity missingProfileView){
        this.missingProfileView = missingProfileView;
    }

    @Override
    public void displayMissingPerson() {
        missingProfileView.showProgress();

        String url = missingProfileView.getUrlProfile();
        String name = missingProfileView.getMissingName();
        String lastTimeSee = context.getResources().getString(R.string.last_time_see) + " " + missingProfileView.getLastTimeSee();
        String age = String.valueOf(missingProfileView.getMissingAge()) + " " + context.getResources().getString(R.string.year);
        String gender = missingProfileView.getGender();
        String description = missingProfileView.getDescription();

        missingProfileView.setMissingProfileImage(url);
        missingProfileView.setMissingName(name);
        missingProfileView.setMissingLastTimeSee(lastTimeSee);
        missingProfileView.setMissingAge(age);
        missingProfileView.setMissingGender(gender);
        if (description != null){
            missingProfileView.setMissingDescription(description);
        } else {
            missingProfileView.hideDescriptionView();
        }
    }

    @Override
    public void createMissingReport() {
        missingProfileView.navigationToMissingReport();
    }

    @Override
    public void shareMissing() {
        Uri uri = getLocalBitmapUri(missingProfileView.getMissingProfileImage());
        if (uri != null){
            String body = new StringBuilder().append(context.getResources().getString(R.string.app_name))
                    .append(": ").append(context.getResources().getString(R.string.section_missing))
                    .append("\n" + context.getResources().getString(R.string.name) + ": ")
                    .append(missingProfileView.getMissingName())
                    .append("\n" + context.getResources().getString(R.string.age) + ": ")
                    .append(missingProfileView.getMissingAge())
                    .append("\n" + context.getResources().getString(R.string.section_provideData))
                    .append(": ").append(context.getResources().getString(R.string.call_911))
                    .toString();
            missingProfileView.sharedMissingPerson(body, uri);
        } else {
            missingProfileView.sharedMessageError();
        }

    }

    //@link http://stackoverflow.com/questions/29638747/share-image-with-shareactionprovider-from-picasso?rq=1
    private Uri getLocalBitmapUri(ImageView imageView) {
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
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "person_" + System.currentTimeMillis() + ".png");
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
