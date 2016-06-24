package com.example.jolenam.nytimessearch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.jolenam.nytimessearch.Article;
import com.example.jolenam.nytimessearch.ArticleArrayAdapter;
import com.example.jolenam.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.jolenam.nytimessearch.R;
import com.example.jolenam.nytimessearch.SearchFilters;
import com.example.jolenam.nytimessearch.SpacesItemsDecoration;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    /*EditText etQuery;
    //GridView gvResults;
    Button btnSearch;*/

    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    RecyclerView rvArticles;
    SearchFilters searchFilter;
    String savedQuery;
    int REQUEST_CODE;
    String sort;
    boolean chkArts;
    boolean chkFashion;
    boolean chkSports;
    String spinMonth;
    String spinDay;
    String spinYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        REQUEST_CODE = 66;
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(articles);

        rvArticles = (RecyclerView) findViewById(R.id.rvArticles);

        rvArticles.clearOnScrollListeners();

        final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        rvArticles.setLayoutManager(staggeredGridLayoutManager);

        SpacesItemsDecoration decoration = new SpacesItemsDecoration(10);
        rvArticles.addItemDecoration(decoration);

        rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(staggeredGridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                customLoadMoreDataFromApi(page);
            }
        });

        rvArticles.setAdapter(adapter);
    }



    public void customLoadMoreDataFromApi(int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();

        params.put("api-key", "7075fa7943644766a780d669cacbd68b");
        params.put("page", page);
        params.put("q", savedQuery);

        // make network request

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    int curSize = adapter.getItemCount();
                    adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setQueryHint("Get news on...");


        // attempt to change text color of SearchView query
        /*int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_plate", null, null);
        View searchPlate = searchView.findViewById(searchPlateId);

        if (searchPlate!=null) {
            int searchTextId = searchPlate.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView searchText = (TextView) searchPlate.findViewById(searchTextId);
            if (searchText!=null) {
                searchText.setTextColor(Color.BLUE);
                searchText.setHintTextColor(Color.WHITE);
            }
        }*/

        // Expand the search view and request focus
        //searchItem.expandActionView();
        //searchView.requestFocus();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // allows for search results following first search to have endless scrolling


                //String query = etQuery.getText().toString();
                savedQuery = query;

                AsyncHttpClient client = new AsyncHttpClient();
                String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
                RequestParams params = new RequestParams();

                params.put("api-key", "7075fa7943644766a780d669cacbd68b");
                params.put("page", 0);
                params.put("q", query);

                // make network request

                Log.d("searchActivity",url +"?" +params);
                client.get(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        JSONArray articleJsonResults = null;

                        try {
                            articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                            Log.d("DEBUG", articleJsonResults.toString());
                            articles.clear();
                            articles.addAll(Article.fromJSONArray(articleJsonResults));
                            adapter.notifyDataSetChanged();
                            Log.d("DEBUG", articles.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void goToFilter(MenuItem item) {

            Intent i = new Intent(this, FilterActivity.class);
            // launch activity
            startActivityForResult(i, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                searchFilter = (SearchFilters) data.getSerializableExtra("filter");
                updateWithFilters(searchFilter);
            }
        }




    }

    private void updateWithFilters(SearchFilters searchFilter) {

        chkArts = searchFilter.getChkArts();
        chkFashion = searchFilter.getChkFashion();
        chkSports = searchFilter.getChkSports();
        sort = searchFilter.getSort();

        //FIX THISS!!!!!!!!!!!!!!
        spinMonth = searchFilter.getSpinnerMonth();
        spinDay = searchFilter.getSpinnerDay();
        spinYear = searchFilter.getSpinnerYear();

        String beginDate = spinYear + spinMonth + spinDay;
        ArrayList<String> newsDeskItems = new ArrayList<>();

        if (chkFashion){
            newsDeskItems.add("\"Fashion\"");
        }
        if (chkSports){
            newsDeskItems.add("\"Sports\"");
        }
        if (chkArts){
            newsDeskItems.add("\"Arts\"");
        }

        String newsDeskItemsStr = android.text.TextUtils.join(" ", newsDeskItems);
        String newsDeskParamValue = String.format("news_desk:(%s)", newsDeskItemsStr);

        /// put in new info into the api

        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();

        params.put("api-key", "7075fa7943644766a780d669cacbd68b");
        params.put("page", 0);
        params.put("begin-date",beginDate);
        params.put("sort", sort.toLowerCase());
        params.put("fq", newsDeskParamValue);
        //params.put();



        //?begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=227c750bb7714fc39ef1559ef1bd8329

        // make network request
        Log.d("searchActivity",url +"?" +params);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    Log.d("DEBUG", articleJsonResults.toString());
                        articles.clear();
                    articles.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    }
}
