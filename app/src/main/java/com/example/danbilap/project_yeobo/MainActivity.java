package com.example.danbilap.project_yeobo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    // 해당 여행 나라의 국기 이미지
    ArrayList<Travel> t_arr;
    int[] background = {R.drawable.sample1, R.drawable.sample2, R.drawable.sample3};

    // 각 여행마다 보여주어야할 것들
    TextView title_travel, start_date, end_date;
    ImageView flag;

    String u_id;
    String url;
    String test = "test";

    GridView gridView1;
    GridAdapter gridAdapter;
   // String imageUrl;


    ShortenUrlGoogle shorten = new ShortenUrlGoogle();
    String short_url, short_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = getIntent();
        u_id = i.getStringExtra("id");
        url = i.getStringExtra("url");
        short_url = shorten.getShortenUrl(url);
        //   Toast.makeText(this,short_url,Toast.LENGTH_LONG).show();
        /*if (short_url != null) {
            ShareTask shareTask = new ShareTask();
            shareTask.execute(short_url);
            Toast.makeText(MainActivity.this, imageUrl, Toast.LENGTH_SHORT).show();
        }*/


        init();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //은슬
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //은슬

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("id", u_id);
                startActivity(intent);
            }
        });

    }

    void init() {

        t_arr = new ArrayList<Travel>();

        gridView1 = (GridView) findViewById(R.id.gridView1);


        show_travel(u_id);

        gridAdapter = new GridAdapter(this, R.layout.gridview1_item, background, t_arr);
        // 커스텀 어댑터를 GridView 에 적용
        gridView1.setAdapter(gridAdapter);
        // 클릭하면 뷰페이저로 넘어감
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Travel t = t_arr.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("t_title", t.getT_title());
                bundle.putInt("t_num", t.getT_id());
                bundle.putString("u_id", t.getU_id());
                bundle.putString("url", t.getUrl());
 //               bundle.putString("imgurl", t.getImgurl());
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    void show_travel(final String u_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeobo.php").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.show_travel(6, u_id, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");

                        for (int i = 0; i < result.size(); i++) {
                            JsonObject obj = (JsonObject) result.get(i);
                            int t_num = obj.get("travel_number").getAsInt();
                            String t_title = obj.get("travel_title").getAsString();
                            String t_start = obj.get("travel_start").getAsString();
                            String t_finish = obj.get("travel_finish").getAsString();
                            // 국기 이미지 가져와야함
                            Travel t = new Travel(R.drawable.united_states_of_america, t_num, t_title, t_start, t_finish, u_id, short_url);


                            t_arr.add(t);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("test", error.toString());
                    }
                });
            }
        }).start();
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
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            Intent intent = new Intent(SecondActivity.this, Side_1.class);
//            startActivity(intent);
//            // Handle the camera action
//        }
        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
 /*   public class ShareTask extends AsyncTask<String, Void, String> { //공유하기를 눌렀을 때 실제로 사용자에게 보여주는 데이터를 처리하는 부분.
        //태그의 파싱이 필요하다.
        HttpURLConnection conn = null;

        @Override
        protected String doInBackground(String... urls) {

            String page = "";
            try {
                URL url = new URL(urls[0]);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());//바이트 단위로 데이터 저장
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(buf, "utf-8")); //텍스트 형태로 데이터 읽어들임.
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    page += line;
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                conn.disconnect();
                return page;
            }


        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Document doc = (Document) Jsoup.parse(result);
            // Elements elements = doc.select("meta");
            Elements elements = (Elements) doc.getElementsByTag("meta");

            for (int i = 0; i < elements.size(); i++) {
                String property = elements.get(i).attr("property");

                if (property.equals("og:image")) {
                    if (imageUrl == null) { //이미지가 아무것도 들어오지 않은 상태일때만 실행.(이미지가 여러개 들어오는 경우 위해)
                        imageUrl = elements.get(i).attr("content");
                        short_image = shorten.getShortenUrl(imageUrl);
                        image(imageUrl);

                    }
                }


            }
        }
    }

    public void image(String url) {
        imageUrl = url;
    }*/
}






