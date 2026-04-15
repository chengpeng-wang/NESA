package gr.georkouk.kastorakiacounter_new;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

public class ActStats extends AppCompatActivity {
    FragmentAdapterStats mAdapter;
    PageIndicator mIndicator;
    ViewPager mPager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.act_stats);
        setTitle(R.string.str_act_stats);
        this.mAdapter = new FragmentAdapterStats(getSupportFragmentManager(), this);
        this.mPager = (ViewPager) findViewById(R.id.pager);
        this.mPager.setAdapter(this.mAdapter);
        this.mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        ((UnderlinePageIndicator) this.mIndicator).setFades(false);
        ((UnderlinePageIndicator) this.mIndicator).setSelectedColor(getResources().getColor(R.color.colorYellow));
        this.mIndicator.setViewPager(this.mPager);
    }
}
