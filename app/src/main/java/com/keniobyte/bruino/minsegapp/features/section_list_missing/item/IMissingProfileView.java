package com.keniobyte.bruino.minsegapp.features.section_list_missing.item;

import android.net.Uri;
import android.widget.ImageView;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface IMissingProfileView {
    int getId();
    String getUrlProfile();
    String getMissingName();
    String getLastTimeSee();
    int getMissingAge();
    String getGender();
    String getDescription();
    ImageView getMissingProfileImage();

    void setMissingProfileImage(String url);
    void setMissingName(String name);
    void setMissingLastTimeSee(String missingLastTimeSee);
    void setMissingAge(String age);
    void setMissingGender(String gender);
    void setMissingDescription(String description);
    void showProgress();
    void hideProgress();
    void onClickShare();
    void onClickSendMissingReport();
    void navigationToMissingReport();
    void sharedMissingPerson(String body, Uri uriImage);
    void sharedMessageError();
    void hideDescriptionView();
}
