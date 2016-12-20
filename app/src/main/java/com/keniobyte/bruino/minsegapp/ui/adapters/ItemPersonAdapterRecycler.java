package com.keniobyte.bruino.minsegapp.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.ui.PersonProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter RecyclerView Wanted/Missing.
 */

public class ItemPersonAdapterRecycler extends RecyclerView.Adapter<ItemPersonAdapterRecycler.FindOutPerson> {

    private Context context;

    List<com.keniobyte.bruino.minsegapp.model.FindOutPerson> persons;

    public ItemPersonAdapterRecycler(List<com.keniobyte.bruino.minsegapp.model.FindOutPerson> persons){
        this.persons = persons;
    }

    @Override
    public FindOutPerson onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_wanted, parent, false);
        FindOutPerson findOutPerson = new FindOutPerson(view);

        return findOutPerson;
    }

    @Override
    public void onBindViewHolder(final FindOutPerson holder, final int position) {
        holder.name.setText(persons.get(position).getName());
        holder.lastTimeSeen.setText(persons.get(position).getLastTimeSeen());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.keniobyte.bruino.minsegapp.model.FindOutPerson findOutPerson = persons.get(position);

                Intent intent = new Intent(context, PersonProfileActivity.class);
                intent.putExtra("id", findOutPerson.getId());
                intent.putExtra("name", findOutPerson.getName());
                intent.putExtra("age", findOutPerson.getAge());
                intent.putExtra("lastTimeSeen", findOutPerson.getLastTimeSeen());
                intent.putExtra("imageURL", findOutPerson.getUrlProfileImage());
                intent.putExtra("crime", findOutPerson.getCrime());

                context.startActivity(intent);
            }
        });

        //Download persons picture and displays it.
        Picasso.with(context).load(persons.get(position).getUrlProfileImage()).resize(80, 80).centerCrop().into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FindOutPerson extends RecyclerView.ViewHolder {

        TextView name, lastTimeSeen;
        ImageView photo;

        public FindOutPerson(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.textViewNameWanted);
            lastTimeSeen = (TextView)itemView.findViewById(R.id.textViewDateWanted);
            photo = (ImageView)itemView.findViewById(R.id.imageViewPerson);
        }
    }
}
