package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted.item;

import android.net.Uri;
import android.widget.ImageView;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface IWantedProfileView {
    int getId();
    String getUrlProfile();
    String getWantedName();
    String getLastTimeSee();
    int getWantedReward();
    String getWantedCrime();
    int getWantedAge();
    String getGender();
    String getDescription();
    ImageView getWantedProfileImage();

    void setWantedProfileImage(String url);
    void setWantedName(String name);
    void setWantedLastTimeSee(String missingLastTimeSee);
    void setWantedReward(String reward);
    void setWantedCrime(String crime);
    void setWantedAge(String age);
    void setWantedGender(String gender);
    void setWantedDescription(String description);
    void showProgress();
    void hideProgress();
    void onClickShare();
    void onClickSendWantedReport();
    void navigationToWantedReport();
    void sharedWantedPerson(String body, Uri uriImage);
    void sharedMessageError();
    void hideDescriptionView();
    void hideWantedReward();
}
