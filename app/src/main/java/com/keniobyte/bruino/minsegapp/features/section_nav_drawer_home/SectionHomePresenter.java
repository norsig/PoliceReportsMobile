package com.keniobyte.bruino.minsegapp.features.section_nav_drawer_home;

import com.keniobyte.bruino.minsegapp.ui.base.BasePresenter;

/**
 * @author bruino
 * @version 17/01/17.
 */

public class SectionHomePresenter extends BasePresenter<SectionHomeFragment> implements ISectionHomePresenter{
    private ISectionHomeView sectionHomeView;

    public SectionHomePresenter() {
    }

    public void addView(ISectionHomeView sectionHomeView){
        this.sectionHomeView = sectionHomeView;
    }

    @Override
    public void callEmergency() {
        sectionHomeView.callEmergency();
    }
}
