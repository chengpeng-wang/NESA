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

public class MovieAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Movie> movies;

    public static class ViewHolder {
        TextView description;
        ImageView movieImage;
    }

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.movies = movies;
        this.context = context;
        BitmapManager.INSTANCE.setPlaceholder(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_holder));
    }

    public int getCount() {
        return this.movies.size();
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
        Movie movie = (Movie) this.movies.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            customView = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.list_item, null);
            holder.description = (TextView) customView.findViewById(R.id.movieDescription);
            holder.movieImage = (ImageView) customView.findViewById(R.id.movieImage);
            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }
        holder.description.setText(movie.getDescription());
        BitmapManager.INSTANCE.loadBitmap(movie.getPictureUrl(), holder.movieImage);
        return customView;
    }
}
