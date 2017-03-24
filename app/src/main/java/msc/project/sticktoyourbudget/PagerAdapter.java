package msc.project.sticktoyourbudget;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ExpensesFragment tab1 = new ExpensesFragment();
                return tab1;
            case 1:
                CategoriesFragment tab2 = new CategoriesFragment();
                return tab2;
            case 2:
                InspectionFragment tab3 = new InspectionFragment();
                return tab3;
            case 3:
                AnalysisFragment tab4 = new AnalysisFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}