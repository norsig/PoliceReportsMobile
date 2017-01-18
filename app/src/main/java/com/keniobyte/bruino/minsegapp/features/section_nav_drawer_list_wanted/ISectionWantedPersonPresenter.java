package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_wanted;

import com.keniobyte.bruino.minsegapp.model.WantedPerson;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface ISectionWantedPersonPresenter {
    void setPersonsInList();
    void itemWantedPerson(WantedPerson wantedPerson);
}
