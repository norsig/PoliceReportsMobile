package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.item;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.ui.PersonProfileContributeActivity;
import com.keniobyte.bruino.minsegapp.utils.SquareImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(WantedProfilePresenter.class)
public class WantedProfileActivity extends AppCompatActivity implements IWantedProfileView {

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.wantedProfileImageView) SquareImageView wantedProfileImageView;
    @BindView(R.id.loadImageWantedProgressBar) ProgressBar loadImageWantedProgressBar;
    @BindView(R.id.wantedNameTextView) TextView wantedNameTextView;
    @BindView(R.id.wantedLastTimeSeeTextView) TextView wantedLastTimeSeeTextView;
    @BindView(R.id.wantedRewardTextView) TextView wantedRewardTextView;
    @BindView(R.id.wantedCrimeTextView) TextView wantedCrimeTextView;
    @BindView(R.id.wantedAgeTextView) TextView wantedAgeTextView;
    @BindView(R.id.wantedGenderTextView) TextView wantedGenderTextView;
    @BindView(R.id.wantedDescriptionTextView) TextView wantedDescriptionTextView;
    @BindView(R.id.wantedDescriptionView) LinearLayout wantedDescriptionView;

    @BindString(R.string.section_wanted) String titleToolbar;
    @BindString(R.string.error_share) String sharedMessageError;
    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;
    @BindDrawable(R.drawable.ic_error_black_24dp) Drawable error;

    private Context context = this;
    private WantedProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanted_profile);
        ButterKnife.bind(this);

        toolbar.setTitle(titleToolbar);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);

        presenter = new WantedProfilePresenter(context);
        presenter.addView(this);
        presenter.displayWantedPerson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_missing_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                onClickShare();
                break;
            case R.id.action_provide_data:
                onClickSendWantedReport();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getId() {
        return getIntent().getExtras().getInt("id");
    }

    @Override
    public String getUrlProfile() {
        return getIntent().getExtras().getString("imageURL");
    }

    @Override
    public String getWantedName() {
        return getIntent().getExtras().getString("name");
    }

    @Override
    public String getLastTimeSee() {
        return getIntent().getExtras().getString("lastTimeSeen");
    }

    @Override
    public int getWantedReward() {
        return getIntent().getExtras().getInt("reward");
    }

    @Override
    public String getWantedCrime() {
        return getIntent().getExtras().getString("crime");
    }

    @Override
    public int getWantedAge() {
        return getIntent().getExtras().getInt("age");
    }

    @Override
    public String getGender() {
        return getIntent().getExtras().getString("gender");
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public ImageView getWantedProfileImage() {
        return wantedProfileImageView;
    }

    @Override
    public void setWantedProfileImage(String url) {
        Picasso.with(context).load(url).error(error).into(wantedProfileImageView, new Callback() {
            @Override
            public void onSuccess() {
                hideProgress();
            }

            @Override
            public void onError() {
                hideProgress();
            }
        });
    }

    @Override
    public void setWantedName(String name) {
        wantedNameTextView.setText(name);
    }

    @Override
    public void setWantedLastTimeSee(String missingLastTimeSee) {
        wantedLastTimeSeeTextView.setText(missingLastTimeSee);
    }

    @Override
    public void setWantedReward(String reward) {
        wantedRewardTextView.setText(reward);
    }

    @Override
    public void setWantedCrime(String crime) {
        wantedCrimeTextView.setText(crime);
    }

    @Override
    public void setWantedAge(String age) {
        wantedAgeTextView.setText(age);
    }

    @Override
    public void setWantedGender(String gender) {
        wantedGenderTextView.setText(gender);
    }

    @Override
    public void setWantedDescription(String description) {
        wantedDescriptionTextView.setText(description);
    }

    @Override
    public void showProgress() {
        loadImageWantedProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        loadImageWantedProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClickShare() {
        presenter.shareWanted();
    }

    @Override
    public void onClickSendWantedReport() {
        presenter.createWantedReport();
    }

    @Override
    public void navigationToWantedReport() {
        startActivity(new Intent(context, PersonProfileContributeActivity.class)
                .putExtra("id", getId()));
    }

    @Override
    public void sharedWantedPerson(String body, Uri uriImage) {
        startActivity(Intent.createChooser(new Intent()
                .setAction(Intent.ACTION_SEND)
                .putExtra(Intent.EXTRA_TEXT, body)
                .putExtra(Intent.EXTRA_STREAM, uriImage)
                .setType("image/*"), "Share Wanted"));
    }

    @Override
    public void sharedMessageError() {
        Toast.makeText(context, sharedMessageError, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideDescriptionView() {
        ((ViewGroup) wantedDescriptionView.getParent()).removeView(wantedDescriptionView);
    }

    @Override
    public void hideWantedReward() {
        wantedRewardTextView.setVisibility(View.INVISIBLE);
    }
}
