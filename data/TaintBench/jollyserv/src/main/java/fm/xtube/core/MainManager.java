package fm.xtube.core;

import com.google.analytics.tracking.android.ModelFields;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainManager {
    public static void loadAllCategories(final CallBack callBack) {
        new AsyncHttpClient().get("http://partnerslab.com/-/tube/loadcategories", new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject response) {
                ArrayList<Category> categories = new ArrayList();
                try {
                    JSONArray valArray = response.toJSONArray(response.names());
                    for (int i = 0; i < valArray.length(); i++) {
                        JSONObject json_data = valArray.getJSONObject(i);
                        String name = json_data.getString("tag");
                        String url = json_data.getString("thumb");
                        String id = json_data.getString("category");
                        Category category = new Category();
                        category.setName(name);
                        category.setUrl(url);
                        category.setId(id);
                        categories.add(category);
                    }
                } catch (JSONException e) {
                }
                callBack.onFinished(categories);
            }

            public void onFailure(Throwable error) {
                callBack.onFinished(null);
            }
        });
    }

    public static void loadMovies(String id, final CallBack callBack) {
        new AsyncHttpClient().get("http://partnerslab.com/-/tube/loadbycategory/category/" + id, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject response) {
                ArrayList<Movie> movies = new ArrayList();
                try {
                    JSONArray valArray = response.toJSONArray(response.names());
                    for (int i = 0; i < valArray.length(); i++) {
                        JSONObject json_data = valArray.getJSONObject(i);
                        String pictureUrl = json_data.getString("thumb");
                        String movieUrl = json_data.getString("videofile");
                        String id = json_data.getString("id");
                        String description = json_data.getString(ModelFields.TITLE);
                        Movie movie = new Movie();
                        movie.setDescription(description);
                        movie.setMovieUrl(movieUrl);
                        movie.setPictureUrl(pictureUrl);
                        movie.setId(id);
                        movies.add(movie);
                    }
                } catch (JSONException e) {
                }
                callBack.onFinished(movies);
            }

            public void onFailure(Throwable error) {
                callBack.onFinished(null);
            }
        });
    }

    public static void loadHdMovies(String id, final CallBack callBack) {
        new AsyncHttpClient().get("http://partnerslab.com/-/tube/loadhdbycategory/category/" + id, new JsonHttpResponseHandler() {
            public void onSuccess(JSONObject response) {
                ArrayList<Movie> movies = new ArrayList();
                try {
                    JSONArray valArray = response.toJSONArray(response.names());
                    for (int i = 0; i < valArray.length(); i++) {
                        JSONObject json_data = valArray.getJSONObject(i);
                        String pictureUrl = json_data.getString("thumb");
                        String movieUrl = json_data.getString("videofile");
                        String id = json_data.getString("id");
                        String description = json_data.getString(ModelFields.TITLE);
                        Movie movie = new Movie();
                        movie.setDescription(description);
                        movie.setMovieUrl(movieUrl);
                        movie.setPictureUrl(pictureUrl);
                        movie.setId(id);
                        movies.add(movie);
                    }
                } catch (JSONException e) {
                }
                callBack.onFinished(movies);
            }

            public void onFailure(Throwable error) {
                callBack.onFinished(null);
            }
        });
    }

    public static void checkPaiment(final CallBack callBack) {
        new AsyncHttpClient().get("http://partnerslab.com/-/tube/checkpayment/uid/" + Server.getDeviceId(), new AsyncHttpResponseHandler() {
            public void onSuccess(String response) {
                if (response.indexOf("EXPIRES IN:") >= 0) {
                    Server.setRiggedTime("HD (" + response.replace("EXPIRES IN:", "") + " days left)");
                    callBack.onFinished(Boolean.valueOf(true));
                    return;
                }
                callBack.onFinished(Boolean.valueOf(false));
            }

            public void onFailure(Throwable error) {
                callBack.onFinished(Boolean.valueOf(false));
            }
        });
    }
}
