package gr.georkouk.kastorakiacounter_new;

import android.content.Context;
import android.support.v4.internal.view.SupportMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpecialAdapter extends SimpleAdapter {
    Context context;
    List<List<String>> players = new ArrayList();
    int[] skor = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] textviews_width = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    TextView[] tvPlayerPoints;

    public SpecialAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
    }

    public void addPlayers(List<List<String>> players_) {
        this.players = players_;
        super.notifyDataSetChanged();
    }

    public void checkSkor(int[] skor_) {
        this.skor = skor_;
        super.notifyDataSetChanged();
    }

    public void setTextviews_width(int[] textviews_width_) {
        this.textviews_width = textviews_width_;
        super.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        this.tvPlayerPoints = new TextView[]{(TextView) ((ViewGroup) view).getChildAt(2), (TextView) ((ViewGroup) view).getChildAt(3), (TextView) ((ViewGroup) view).getChildAt(4), (TextView) ((ViewGroup) view).getChildAt(5), (TextView) ((ViewGroup) view).getChildAt(6), (TextView) ((ViewGroup) view).getChildAt(7), (TextView) ((ViewGroup) view).getChildAt(8), (TextView) ((ViewGroup) view).getChildAt(9), (TextView) ((ViewGroup) view).getChildAt(10), (TextView) ((ViewGroup) view).getChildAt(11), (TextView) ((ViewGroup) view).getChildAt(12), (TextView) ((ViewGroup) super.getView(position, convertView, parent)).getChildAt(13)};
        for (int i = 0; i < this.players.size(); i++) {
            LayoutParams params = this.tvPlayerPoints[i].getLayoutParams();
            if (this.players.size() > 3) {
                params.width = this.textviews_width[i];
                this.tvPlayerPoints[i].setLayoutParams(params);
            } else {
                params.width = 0;
                this.tvPlayerPoints[i].setLayoutParams(params);
            }
            this.tvPlayerPoints[i].setVisibility(0);
            if (this.skor[i] >= 100) {
                this.tvPlayerPoints[i].setBackgroundColor(SupportMenu.CATEGORY_MASK);
            } else {
                this.tvPlayerPoints[i].setBackgroundColor(0);
            }
        }
        return view;
    }
}
