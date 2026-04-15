package fm.xtube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import fm.xtube.core.CallBack;
import fm.xtube.core.GodHelpMe;
import fm.xtube.core.MainManager;
import fm.xtube.core.Movie;
import fm.xtube.core.MovieAdapter;
import java.util.ArrayList;

public class ListHDMoviesActivity extends GodHelpMe {
    /* access modifiers changed from: private */
    public ListView hDMovieListView;
    private TextView hdCategoryTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_hd);
        this.hdCategoryTextView = (TextView) findViewById(R.id.hdCategoryTextView);
        this.hDMovieListView = (ListView) findViewById(R.id.hDMovieListView);
        Bundle extras = getIntent().getExtras();
        this.hdCategoryTextView.setText(extras.getString("category") + " (HD)");
        this.hdCategoryTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ListHDMoviesActivity.this.finish();
            }
        });
        this.self.movieProgress();
        MainManager.loadHdMovies(extras.getString("id"), new CallBack() {
            public void onFinished(Object result) {
                ListHDMoviesActivity.this.self.hideProgress();
                if (result != null) {
                    final ArrayList<Movie> movies = (ArrayList) result;
                    ListHDMoviesActivity.this.hDMovieListView.setAdapter(new MovieAdapter(ListHDMoviesActivity.this.self, movies));
                    ListHDMoviesActivity.this.hDMovieListView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                            Intent videoIntent = new Intent(ListHDMoviesActivity.this, VideoActivity.class);
                            videoIntent.putExtra("url", ((Movie) movies.get(position)).getMovieUrl());
                            ListHDMoviesActivity.this.startActivity(videoIntent);
                        }
                    });
                }
            }

            public void onFail(String message) {
                ListHDMoviesActivity.this.self.hideProgress();
            }
        });
    }
}
