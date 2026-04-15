package fm.xtube.core;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import fm.xtube.R;
import java.util.ArrayList;

public class CategoryAdapter extends BaseAdapter {
    private ArrayList<Category> categories;
    private Context context;

    public static class ViewHolder {
        ImageView picIcon;
        TextView pictureName;
    }

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.categories = categories;
        this.context = context;
        BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(context.getResources(), R.drawable.adapter_icon_holder));
    }

    public int getCount() {
        return this.categories.size();
    }

    public Object getItem(int i) {
        return null;
    }

    public long getItemId(int i) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View customView = convertView;
        Category category = (Category) this.categories.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            customView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.picture, null);
            holder.picIcon = (ImageView) customView.findViewById(R.id.pictureIcon);
            holder.pictureName = (TextView) customView.findViewById(R.id.pictureName);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }
        holder.pictureName.setText(category.getName());
        BitmapManager.INSTANCE.loadBitmap(category.getUrl(), holder.picIcon);
        return customView;
    }
}
