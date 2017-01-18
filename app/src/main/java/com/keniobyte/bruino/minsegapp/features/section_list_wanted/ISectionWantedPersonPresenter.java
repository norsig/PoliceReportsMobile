package com.keniobyte.bruino.minsegapp.features.section_list_wanted;

import com.keniobyte.bruino.minsegapp.models.WantedPerson;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface ISectionWantedPersonPresenter {
    void setPersonsInList();
    void itemWantedPerson(WantedPerson wantedPerson);
}
