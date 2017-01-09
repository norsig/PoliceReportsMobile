package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_list_missing;

import com.keniobyte.bruino.minsegapp.model.Person;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface ISectionMissingPersonPresenter {
    void setPersonsInList();
    void itemMissingPerson(Person missingPerson);
}
