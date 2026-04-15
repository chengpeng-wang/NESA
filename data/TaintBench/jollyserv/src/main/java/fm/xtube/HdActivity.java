package fm.xtube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import fm.xtube.core.CallBack;
import fm.xtube.core.Category;
import fm.xtube.core.CategoryAdapter;
import fm.xtube.core.GodHelpMe;
import fm.xtube.core.MainManager;
import fm.xtube.core.Server;
import java.util.ArrayList;

public class HdActivity extends GodHelpMe {
    GridView hdGridView;
    Button normalButton;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hd);
        this.hdGridView = (GridView) findViewById(R.id.hdGridView);
        this.normalButton = (Button) findViewById(R.id.normalButton);
        this.normalButton.setText(Server.getRiggedTime());
        this.self.categoryProgress();
        MainManager.loadAllCategories(new CallBack() {
            public void onFinished(Object result) {
                HdActivity.this.self.hideProgress();
                final ArrayList<Category> categories = (ArrayList) result;
                HdActivity.this.hdGridView.setAdapter(new CategoryAdapter(HdActivity.this.self, categories));
                HdActivity.this.hdGridView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                        Intent moviesIntent = new Intent(HdActivity.this.self, ListHDMoviesActivity.class);
                        moviesIntent.putExtra("category", ((Category) categories.get(position)).getName());
                        moviesIntent.putExtra("id", ((Category) categories.get(position)).getId());
                        HdActivity.this.startActivity(moviesIntent);
                    }
                });
            }

            public void onFail(String message) {
                HdActivity.this.self.hideProgress();
            }
        });
    }
}
