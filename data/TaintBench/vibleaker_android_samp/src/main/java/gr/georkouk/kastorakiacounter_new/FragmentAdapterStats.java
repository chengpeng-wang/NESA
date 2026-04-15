package gr.georkouk.kastorakiacounter_new;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import gr.georkouk.kastorakiacounter_new.Fragments.FragStats;
import java.util.ArrayList;
import java.util.List;

public class FragmentAdapterStats extends FragmentPagerAdapter {
    Context context;
    List<Fragment> fragmentPages = new ArrayList();

    public FragmentAdapterStats(FragmentManager fm, Context context_) {
        super(fm);
        this.context = context_;
        this.fragmentPages.add(FragStats.newInstance("wins", 0));
        this.fragmentPages.add(FragStats.newInstance("points", 1));
        this.fragmentPages.add(FragStats.newInstance("lastWin", 2));
        this.fragmentPages.add(FragStats.newInstance("smallerWin", 3));
    }

    public Fragment getItem(int pos) {
        return (Fragment) this.fragmentPages.get(pos);
    }

    public int getCount() {
        return this.fragmentPages.size();
    }
}
