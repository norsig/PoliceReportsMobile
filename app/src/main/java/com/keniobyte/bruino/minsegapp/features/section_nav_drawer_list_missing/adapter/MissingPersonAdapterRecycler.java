package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.model.Person;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author bruino
 * @version 09/01/17.
 */

public class MissingPersonAdapterRecycler extends RecyclerView.Adapter<MissingPersonAdapterRecycler.PersonWantedViewHolder>{
    private Context context;
    private List<Person> MissingPersonList;

    public MissingPersonAdapterRecycler(List<Person> MissingPersonList) {
        this.MissingPersonList = MissingPersonList;
    }

    @Override
    public PersonWantedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_person_missing, parent, false);
        return new PersonWantedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonWantedViewHolder holder, int position) {
        holder.nameWanted.setText(MissingPersonList.get(position).getFullName());
        holder.lastTimeSee.setText( new SimpleDateFormat("dd MMM yyyy", Locale.US)
                .format(MissingPersonList.get(position).getLastTimeSee()));
        Picasso.with(context).load(MissingPersonList.get(position).getUrlProfile())
                .resize(80, 80)
                .centerCrop()
                .into(holder.personWantedImage);
    }

    @Override
    public int getItemCount() {
        return MissingPersonList.size();
    }

    public Person getItem(int position){
        return MissingPersonList.get(position);
    }

    public static class PersonWantedViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.nameWantedTextView) TextView nameWanted;
        @BindView(R.id.lastTimeSeeTextView) TextView lastTimeSee;
        @BindView(R.id.personWantedImageView) ImageView personWantedImage;

        public PersonWantedViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


