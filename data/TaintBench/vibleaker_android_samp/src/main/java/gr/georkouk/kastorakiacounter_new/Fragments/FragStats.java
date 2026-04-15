package gr.georkouk.kastorakiacounter_new.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import gr.georkouk.kastorakiacounter_new.MyDB;
import gr.georkouk.kastorakiacounter_new.MyVariables;
import gr.georkouk.kastorakiacounter_new.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragStats extends Fragment {
    Activity activity;
    MyDB db;
    String[] from = new String[]{MyVariables.KEY_NAME, "wins"};
    int pos;
    String[] tabs;
    int[] to = new int[]{R.id.tvName, R.id.tvSkor};
    TextView tvTitle;
    String type;
    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.frag_stats, null);
        this.activity = getActivity();
        this.type = getArguments().getString("type");
        this.pos = getArguments().getInt("pos");
        ((AdView) this.view.findViewById(R.id.adView)).loadAd(new Builder().build());
        this.tabs = this.activity.getResources().getStringArray(R.array.stats_tabs);
        this.tvTitle = (TextView) this.view.findViewById(R.id.tvTitle);
        this.tvTitle.setText(this.tabs[this.pos]);
        fillList();
        return this.view;
    }

    private HashMap<String, String> putRow(String name, String wins) {
        HashMap<String, String> row = new HashMap();
        row.put(MyVariables.KEY_NAME, name);
        row.put("wins", wins);
        return row;
    }

    public static FragStats newInstance(String type_, int pos_) {
        FragStats f = new FragStats();
        Bundle bundle = new Bundle(2);
        bundle.putString("type", type_);
        bundle.putInt("pos", pos_);
        f.setArguments(bundle);
        return f;
    }

    public void fillList() {
        this.db = MyDB.getInstance().open();
        List<List<String>> data = this.db.getStatsData(this.type);
        MyDB.getInstance().close();
        ArrayList<Map<String, String>> list = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            list.add(putRow((String) ((List) data.get(i)).get(0), (String) ((List) data.get(i)).get(1)));
        }
        ((ListView) this.view.findViewById(R.id.list)).setAdapter(new SimpleAdapter(this.activity, list, R.layout.stats_list_grid, this.from, this.to));
    }
}
