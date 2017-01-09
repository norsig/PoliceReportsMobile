package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing;

import com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing.adapter.MissingPersonAdapterRecycler;
import com.keniobyte.bruino.minsegapp.model.Person;

import java.util.List;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface ISectionMissingPersonView {
    MissingPersonAdapterRecycler createRecyclerViewAdapter(List<Person> persons);

    void setMissingRecyclerViewAdapter(MissingPersonAdapterRecycler adapter);
    void showProgress();
    void hideProgress();
    void connectionErrorMessage();
    void navigationToMissingPerson(Person person);
}
