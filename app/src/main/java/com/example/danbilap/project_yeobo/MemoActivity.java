package com.example.danbilap.project_yeobo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by daeun on 2016-11-10.
 */
public class MemoActivity extends Activity implements View.OnClickListener {

    EditText title, content;
    Button saveButton;
    String travel_number;
    int itravel_number=0;
    String c_num;
    int ic_num=0;
    /////////
    Bitmap urlbitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);
        init();
    }
    public void init(){
        title = (EditText)findViewById(R.id.title);
        content = (EditText)findViewById(R.id.content);
        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(this);

        //지역번호만 넘어오기
        Bundle bundle = getIntent().getExtras(); // intent로 페이지 넘어옴.
        travel_number = bundle.getString("travel_number");
        itravel_number = Integer.parseInt(travel_number);
        c_num = bundle.getString("c_num");
        ic_num = Integer.parseInt(c_num);
    }

    @Override
    public void onClick(View v) {
        int t_travelnumber = itravel_number;
        String t_title = title.getText().toString();
        String t_content = content.getText().toString();

        if (TextUtils.isEmpty(t_title)  || TextUtils.isEmpty(t_title) ) {
            Toast.makeText(MemoActivity.this, "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else
            MemoActivity.this.finish();{

            create_memo(t_travelnumber, t_title, t_content);
        }
    }

    void create_memo(final int t_travelnumber, final String t_title, final String t_content){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeobo.php").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.create_memo(8 ,t_travelnumber, t_title, t_content, ic_num, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errcode = ((JsonObject)result.get(0)).get("errorCode").getAsString();

                        if (errcode.equals("success")) {
                            Toast.makeText(MemoActivity.this, "새로운 메모가 추가되었습니다!", Toast.LENGTH_SHORT).show();
                            MemoActivity.this.finish();
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


    public Bitmap getBitmap(String src) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(src);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally{
            if(connection!=null)connection.disconnect();
        }
    }

    public void onBackPressed() { // 뒤로 가기 했을 때 ShowListActivity로 돌아가도록
        super.onBackPressed();
        MemoActivity.this.finish();
    }
}
