package com.keniobyte.bruino.minsegapp.features.police_report.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.keniobyte.bruino.minsegapp.R;
import com.keniobyte.bruino.minsegapp.features.police_report.IPoliceReportPresenter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author kbibek
 * @version 02/01/17.
 * @link https://github.com/coomar2841/android-multipicker-library/blob/dev/app/src/main/java/com/kbeanie/multipicker/sample/adapters/MediaResultsAdapter.java
 */

public class MediaResultAdapter extends BaseAdapter{

    private final String TAG = getClass().getSimpleName();

    private final static int TYPE_IMAGE = 0;
    private final static int TYPE_VIDEO = 1;
    private final static int TYPE_AUDIO = 4;

    private final static String FORMAT_IMAGE_VIDEO_DIMENSIONS = "%sw x %sh";
    private final static String FORMAT_ORIENTATION = "Ortn: %s";
    private final static String FORMAT_DURATION = "%s";

    private List<? extends ChosenFile> files;
    private final Context context;
    private IPoliceReportPresenter.onListAttachmentsListener listener;

    public MediaResultAdapter(List<? extends ChosenFile> files, Context context, IPoliceReportPresenter.onListAttachmentsListener listener) {
        this.files = files;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ChosenFile file = (ChosenFile) getItem(position);
        int itemViewType = getItemViewType(position);
        if (convertView == null) {
            switch (itemViewType) {
                case TYPE_IMAGE:
                    convertView = LayoutInflater.from(context).inflate(R.layout.adapter_images, null);
                    break;
                /*case TYPE_VIDEO:
                    convertView = LayoutInflater.from(context).inflate(R.layout.adapter_images, null);
                    break;
                case TYPE_AUDIO:
                    convertView = LayoutInflater.from(context).inflate(R.layout.adapter_images, null);
                    break;*/
            }
        }

        switch (itemViewType) {
            case TYPE_IMAGE:
                showImage(file, convertView, position);
                break;
            /*case TYPE_VIDEO:
                showVideo(file, convertView);
                break;
            case TYPE_AUDIO:
                showAudio(file, convertView);
                break;*/
        }
        return convertView;
    }

    private void showAudio(ChosenFile file, View convertView) {
        //TODO: Pending.
    }

    private void showVideo(ChosenFile file, View convertView) {
        //TODO: Pending.
    }

    private void showImage(ChosenFile file, View convertView, final int position) {
        final ChosenImage image = (ChosenImage) file;

        ImageView previewImageView = ButterKnife.findById(convertView, R.id.previewImageView);
        Picasso.with(context).load(new File(image.getOriginalPath())).fit().centerCrop().into(previewImageView);

        TextView nameTextView = ButterKnife.findById(convertView, R.id.nameFileTextView);
        nameTextView.setText(file.getDisplayName());

        TextView mimeTypeTextView = ButterKnife.findById(convertView, R.id.mimeTypeTextView);
        mimeTypeTextView.setText(file.getMimeType());

        TextView sizeTextView = ButterKnife.findById(convertView, R.id.sizeTextView);
        sizeTextView.setText(file.getHumanReadableSize(false));

        ImageButton clearFileButton = ButterKnife.findById(convertView, R.id.clearFileButton);
        clearFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemDelete(position);
            }
        });
    }


}
