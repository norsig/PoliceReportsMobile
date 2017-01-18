package com.keniobyte.bruino.minsegapp.features.section_list_wanted;

import com.keniobyte.bruino.minsegapp.features.section_list_wanted.adapter.WantedPersonAdapterRecycler;
import com.keniobyte.bruino.minsegapp.models.WantedPerson;

import java.util.List;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface ISectionWantedPersonView {
    WantedPersonAdapterRecycler createRecyclerViewAdapter(List<WantedPerson> persons);

    void setWantedRecyclerViewAdapter(WantedPersonAdapterRecycler adapter);
    void showProgress();
    void hideProgress();
    void connectionErrorMessage();
    void navigationToWantedPerson(WantedPerson person);
}
