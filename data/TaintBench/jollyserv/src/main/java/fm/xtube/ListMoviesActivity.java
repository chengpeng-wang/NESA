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

public class ListMoviesActivity extends GodHelpMe {
    private TextView categoryTextView;
    /* access modifiers changed from: private */
    public ListView movieListView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        this.categoryTextView = (TextView) findViewById(R.id.categoryTextView);
        this.movieListView = (ListView) findViewById(R.id.movieListView);
        Bundle extras = getIntent().getExtras();
        this.categoryTextView.setText(extras.getString("category") + " (Free)");
        this.categoryTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ListMoviesActivity.this.finish();
            }
        });
        this.self.movieProgress();
        MainManager.loadMovies(extras.getString("id"), new CallBack() {
            public void onFinished(Object result) {
                ListMoviesActivity.this.self.hideProgress();
                if (result != null) {
                    final ArrayList<Movie> movies = (ArrayList) result;
                    ListMoviesActivity.this.movieListView.setAdapter(new MovieAdapter(ListMoviesActivity.this.self, movies));
                    ListMoviesActivity.this.movieListView.setOnItemClickListener(new OnItemClickListener() {
                        public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                            Intent videoIntent = new Intent(ListMoviesActivity.this, VideoActivity.class);
                            videoIntent.putExtra("url", ((Movie) movies.get(position)).getMovieUrl());
                            ListMoviesActivity.this.startActivity(videoIntent);
                        }
                    });
                }
            }

            public void onFail(String message) {
                ListMoviesActivity.this.self.hideProgress();
            }
        });
    }
}
