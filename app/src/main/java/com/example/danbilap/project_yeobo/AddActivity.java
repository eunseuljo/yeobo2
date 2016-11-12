package com.example.danbilap.project_yeobo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.UUID;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.widget.AdapterView.OnItemSelectedListener;

public class AddActivity extends Activity {


    ArrayList<String> countryenname;
    ArrayList<String> countryname;
    ArrayList<String> countryid;

    Button btn1;
    Button btn_start, btn_end;
    Spinner spinner1;
    ArrayAdapter<String> spinner1Adapter;

    EditText title;

    String t_title, t_city, c_id, t_start, t_finish,id;
    int num;


    //    InformationSearchTask informationSearchTask;
//    ContactSearchTask contactSearchTask;
   // String travel_nation;
    //int nationid;
    String nationbasic;   //id를 통해 받은 국가 정보
    String nationphone;   //국가이름을 통해 받은 긴급연락처 정보
   // String travelnationen;
   ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Intent i=getIntent();
        id=i.getStringExtra("id");
        String[] str=getResources().getStringArray(R.array.spinnerArray);
        adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,str);

        init();
    }

    void init(){

        title = (EditText)findViewById(R.id.title);

        final TextView start = (TextView)findViewById(R.id.start);
        final TextView end = (TextView)findViewById(R.id.end);

        spinner1 = (Spinner)findViewById(R.id.spinner1);
        spinner1.setAdapter(adapter);

        spinner1.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener(){
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    num=i;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
        );




        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strDate =
                                Integer.toString(year) + "-" +
                                Integer.toString(monthOfYear+1) + "-" +
                                Integer.toString(dayOfMonth);
                        start.setText(strDate);
                        start.setTextColor(Color.BLACK);


                    }
                }, 2016, 0, 1); // month는 -1한 값으로 지정
                datePickerDialog.show();
            }
        });

        btn_end = (Button)findViewById(R.id.btn_end);
        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String strDate =
                                Integer.toString(year) + "-" +
                                        Integer.toString(monthOfYear+1) + "-" +
                                        Integer.toString(dayOfMonth);
                        end.setText(strDate);
                        end.setTextColor(Color.BLACK);

                    }
                }, 2016, 0, 1); // month는 -1한 값으로 지정
                datePickerDialog.show();
            }
        });

        btn1 = (Button)findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력한 것들 DB에 넣기
                t_title = title.getText().toString();
                t_start = start.getText().toString();
                t_finish = end.getText().toString();
                t_city =(String)spinner1.getAdapter().getItem(spinner1.getSelectedItemPosition());
                // t_nation, n_id은 onPostExecute에서

                if (TextUtils.isEmpty(t_title)  || TextUtils.isEmpty(t_city) || TextUtils.isEmpty(t_start) || TextUtils.isEmpty(t_finish)) {
                    Toast.makeText(AddActivity.this, "모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    create_travel(id, t_title, t_city, t_start, t_finish,num);
                }

                AddActivity.this.finish();

                /*informationSearchTask = new InformationSearchTask();
                informationSearchTask.execute();

                contactSearchTask = new ContactSearchTask();
                contactSearchTask.execute();*/
            }
        });

        countryname = new ArrayList<String>();
        countryid = new ArrayList<String>();
        countryenname = new ArrayList<String>();


    }



    void create_travel(final String u_id, final String t_title, final String t_city, final String t_start, final String t_finish,final int c_num){
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeoboH.php").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.create_travel(4,u_id, t_city, t_title, t_start, t_finish,c_num, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errcode = ((JsonObject)result.get(0)).get("errorCode").getAsString();

                        if (errcode.equals("success")) {
                            Toast.makeText(AddActivity.this, "새로운 여행이 추가되었습니다!", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(AddActivity.this, MainActivity.class);
                            myIntent.putExtra("id", id);
                            startActivity(myIntent);
                            AddActivity.this.finish();
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

    public void onBackPressed() { // 뒤로 가기 했을 때 MainActivity로 돌아가도록
        super.onBackPressed();
        AddActivity.this.finish();
    }




}
