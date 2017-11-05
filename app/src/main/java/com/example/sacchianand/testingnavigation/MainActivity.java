package com.example.sacchianand.testingnavigation;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sacchianand.testingnavigation.DbHelper.DbHelperActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MyMainCatogeryAdapter_video myMainCatogeryAdapter_video;
    String selectedsubtopic, selectedsutopicid;
    private RecyclerView recyclerView_main_video;
    private LinearLayoutManager linearLayoutManager_data_recycler_video;
    Video videomodel;
    List<Video> videoList = new ArrayList<>();
    DbHelperActivity dbHelperActivity;

    private EditText editText_search_videos;
    private     List <Video> filterlist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        data();
        onfind();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void onfind() {
        TextView textViewprevioussubtopicname = (TextView) findViewById(R.id.previoussubtopicname);
        textViewprevioussubtopicname.setText(selectedsubtopic);
        editText_search_videos = (EditText) findViewById(R.id.search_videos);
        recyclerView_main_video = (RecyclerView) findViewById(R.id.recyclerviewdata_video);
        editText_search_videos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //after the change calling the method and passing the search input
                filter(editable.toString());
            }
        });

    }

    private void filter(String text) {

        ArrayList<Video> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (Video s : videoList) {
            //if the existing elements contains the search input
            if (text.equals(s.getName())) {
                //adding the element to filtered list
                filterdNames.add(s);
            }
        }

        //calling a method of the adapter class and passing the filtered list
//        MyMainCatogeryAdapter_video.filterList(filterdNames);
        myMainCatogeryAdapter_video.filterList(filterdNames);

    }


    private void data () {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://sagcl.arckons.com/apis/check.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("onResponse", String.valueOf(response));
                    try {
                        if (new JSONObject(response).getString("status").equals("true")) {
                            JSONObject jsonObject = new JSONObject(response);


                            JSONArray jsonArray = jsonObject.getJSONArray("topic");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String _id = jsonObject1.getString("Id");
                                String name = jsonObject1.getString("name");
                                String topic_courseid = jsonObject1.getString("course_id");
                                String topic_batch_id = jsonObject1.getString("batch_id");


                            }
                            JSONArray jsonArray1 = jsonObject.getJSONArray("subtopic");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
                                String _idsub = jsonObject2.getString("Id");
                                String namesub = jsonObject2.getString("name");
                                String topicsub = jsonObject2.getString("topic_id");

                            }
                            JSONArray jsonArray2 = jsonObject.getJSONArray("video");
                            for (int k = 0; k < jsonArray2.length(); k++) {
                                JSONObject jsonObject3 = jsonArray2.getJSONObject(k);
                                String video_id = jsonObject3.getString("Id");
                                String video_name = jsonObject3.getString("name");
                                String video_subtopic_id = jsonObject3.getString("subtopic_id");
                                String video_count = jsonObject3.getString("view_count");
                                videomodel = new Video();
                                videomodel.setId(video_id);
                                videomodel.setName(video_name);
                                videomodel.setSubtopicId(video_subtopic_id);
                                videomodel.setViewCount(video_count);

                                videoList.add(videomodel);


                            }
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    linearLayoutManager_data_recycler_video = new LinearLayoutManager(MainActivity.this);
                                    recyclerView_main_video.setLayoutManager(linearLayoutManager_data_recycler_video);
                                    myMainCatogeryAdapter_video = new MyMainCatogeryAdapter_video(videoList);
                                    recyclerView_main_video.setAdapter(myMainCatogeryAdapter_video);

                                }
                            });


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("onerror", String.valueOf(error));

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("u_name", "admin1");
                    params.put("passcode", "pass1");
                    params.put("d_id", "sdfsdvsdv1");
                    return params;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = "root:root";
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    return headers;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
            stringRequest.setShouldCache(false);
        }

    private class MyMainCatogeryAdapter_video extends RecyclerView.Adapter<MyMainCatogeryAdapter_video.ViewHolder> {

        List <Video> videoList=new ArrayList<>();




        public MyMainCatogeryAdapter_video(  List <Video> videoList) {


            this.videoList=videoList;
        }


        @Override
        public MyMainCatogeryAdapter_video.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_listdata, parent, false);
            return new MyMainCatogeryAdapter_video.ViewHolder(itemView);

        }

        @Override
        public void onBindViewHolder(MyMainCatogeryAdapter_video.ViewHolder holder, int position) {
            String videodata;
            Video video = videoList.get(position);


            holder.textView_textviewdata_video.setText(video.getName());
        }
        @Override
        public int getItemCount() {

            return videoList.size();
        }

        public void filterList(ArrayList<Video> filterdNames) {
            this.videoList = filterdNames;
            notifyDataSetChanged();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textView_textviewdata_video;
            private RelativeLayout relativeLayout_relativelayout_video;

            public ViewHolder(View itemView)

            {

                super(itemView);
                textView_textviewdata_video = (TextView) itemView.findViewById(R.id.textviewdata_video);
                relativeLayout_relativelayout_video=(RelativeLayout) itemView.findViewById(R.id.relativelayout_video);


            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
