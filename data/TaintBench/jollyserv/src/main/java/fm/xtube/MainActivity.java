package fm.xtube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import fm.xtube.core.CallBack;
import fm.xtube.core.Category;
import fm.xtube.core.CategoryAdapter;
import fm.xtube.core.GodHelpMe;
import fm.xtube.core.MainManager;
import java.util.ArrayList;

public class MainActivity extends GodHelpMe {
    private Button hdButton;
    /* access modifiers changed from: private */
    public GridView mainGridView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.mainGridView = (GridView) findViewById(R.id.mainGridView);
        this.hdButton = (Button) findViewById(R.id.hdButton);
        this.self.categoryProgress();
        MainManager.loadAllCategories(new CallBack() {
            public void onFinished(Object result) {
                MainActivity.this.self.hideProgress();
                final ArrayList<Category> categories = (ArrayList) result;
                MainActivity.this.mainGridView.setAdapter(new CategoryAdapter(MainActivity.this.self, categories));
                MainActivity.this.mainGridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                        Intent moviesIntent = new Intent(MainActivity.this.self, ListMoviesActivity.class);
                        moviesIntent.putExtra("category", ((Category) categories.get(position)).getName());
                        moviesIntent.putExtra("id", ((Category) categories.get(position)).getId());
                        MainActivity.this.startActivity(moviesIntent);
                    }
                });
            }

            public void onFail(String message) {
                MainActivity.this.self.hideProgress();
            }
        });
        this.hdButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.self.hdProgress();
                MainManager.checkPaiment(new CallBack() {
                    public void onFinished(Object result) {
                        if (((Boolean) result).booleanValue()) {
                            MainActivity.this.self.hideProgress();
                            MainActivity.this.startActivity(new Intent(MainActivity.this.self, HdActivity.class));
                            return;
                        }
                        MainActivity.this.self.hideProgress();
                        MainActivity.this.startActivity(new Intent(MainActivity.this.self, PayActivity.class));
                    }

                    public void onFail(String message) {
                    }
                });
            }
        });
    }
}
