package com.keniobyte.bruino.minsegapp.features.section_list_missing;

import com.keniobyte.bruino.minsegapp.models.Person;

/**
 * @author bruino
 * @version 09/01/17.
 */

public interface ISectionMissingPersonPresenter {
    void setPersonsInList();
    void itemMissingPerson(Person missingPerson);
}
