package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.Toolbar;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.adapter.WantedPersonAdapterRecycler;
import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.item.WantedProfileActivity;
import com.keniobyte.bruino.minsegapp.model.WantedPerson;
import com.keniobyte.bruino.minsegapp.utils.RecyclerItemClickListener;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(SectionWantedPersonPresenter.class)
public class SectionWantedPersonActivity extends AppCompatActivity implements ISectionWantedPersonView {

    @BindView(R.id.myToolbar) Toolbar toolbar;
    @BindView(R.id.downloadPersonProgressBar) ProgressBar downloadPersonWantedProgressBar;
    @BindView(R.id.wantedRecyclerView) RecyclerView wantedRecyclerView;

    @BindString(R.string.section_wanted) String titleActivity;
    @BindString(R.string.title_error) String titleError;
    @BindString(R.string.message_error_current_version) String messageConnectionError;
    @BindString(R.string.accept) String ok;

    @BindDrawable(R.drawable.ic_arrow_back_white_24dp) Drawable arrowBack;

    private Context context = this;
    private SectionWantedPersonPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_person_missing);
        ButterKnife.bind(this);

        toolbar.setTitle(titleActivity);
        toolbar.setNavigationIcon(arrowBack);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        wantedRecyclerView.setHasFixedSize(true);
        wantedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wantedRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, wantedRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                presenter.itemWantedPerson(((WantedPersonAdapterRecycler) wantedRecyclerView.getAdapter()).getItem(position));
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));

        presenter = new SectionWantedPersonPresenter(context);
        presenter.addView(this);
        presenter.setPersonsInList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public WantedPersonAdapterRecycler createRecyclerViewAdapter(List<WantedPerson> persons) {
        return new WantedPersonAdapterRecycler(persons);
    }

    @Override
    public void setWantedRecyclerViewAdapter(WantedPersonAdapterRecycler adapter){
        wantedRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgress() {
        downloadPersonWantedProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        downloadPersonWantedProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void connectionErrorMessage() {
        new AlertDialog.Builder(context)
                .setTitle(titleError)
                .setMessage(messageConnectionError)
                .setCancelable(false)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    @Override
    public void navigationToWantedPerson(WantedPerson person) {
        Log.e(getClass().getSimpleName(), person.toString());
        startActivity(new Intent(context, WantedProfileActivity.class)
                .putExtra("id", person.getId())
                .putExtra("name", person.getFullName())
                .putExtra("age", person.getAge())
                .putExtra("lastTimeSeen", new SimpleDateFormat("dd MMM yyyy", Locale.US)
                        .format(person.getLastTimeSee()))
                .putExtra("imageURL", person.getUrlProfile())
                .putExtra("crime", person.getCrime())
                .putExtra("reward", person.getReward()));
    }
}
