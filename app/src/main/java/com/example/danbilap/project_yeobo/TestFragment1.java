package com.example.danbilap.project_yeobo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

//daeun pc
public class TestFragment1 extends Fragment  {
    ImageButton ib ;
    ViewGroup rootView;

    int t_number;
    int c_number;
    int travel_number=3;
    int c_num = 0;
    ArrayList<Memo> m_arr;
    MemoAdapter memoAdapter;
    ListView lv;
    Memo m1;
    int mm_share;


    public static TestFragment1 newInstance(int t_number, int c_num ){
        TestFragment1 instance = new TestFragment1();
        instance.t_number = t_number;
        instance.c_number = c_num;
        return instance;
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_1, container, false);

        View view = inflater.inflate(R.layout.fragment_test_1, null) ;
        init();

        return rootView;

    }
    public void init() {
        travel_number = t_number;
        c_num = c_number;
        m_arr = new ArrayList<Memo>();
        lv = (ListView) rootView.findViewById(R.id.listView);
        memoAdapter = new MemoAdapter(getContext(), R.layout.memo, m_arr);
        lv.setAdapter(memoAdapter);
        show_memo(travel_number);
        memoAdapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String)parent.getItemAtPosition(position);
                Toast.makeText(getContext(),value,Toast.LENGTH_LONG).show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                mm_share = m_arr.get(position).getS_num();
                //길게 누르면 삭제 버튼 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());// 여기서 this는 Activity의 this

                // 여기서 부터는 알림창의 속성 설정
                builder.setTitle("메모 삭제")        // 제목 설정
                        .setMessage("선택한 메모를 삭제 하시겠습니까?")        // 메세지 설정
                        .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                delete_memo(mm_share);
                                m_arr.remove(position);
                                memoAdapter.notifyDataSetChanged();
                            }
                        })

                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();    // 알림창 객체 생성
                dialog.show();    // 알림창 띄우기
                return false;
            }
        });

        ib = (ImageButton) rootView.findViewById(R.id.imageButton);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //메모 화면뜨게 한다.
                Intent intent = new Intent(getActivity().getApplication(), MemoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("travel_number",""+travel_number);
                bundle.putString("c_num",""+c_num);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    void show_memo(final int travel_number) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeobo.php").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.show_memo(12, travel_number, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");

                        for (int i = 0; i < result.size(); i++) {
                            JsonObject obj = (JsonObject) result.get(i);
                            int s_num = obj.get("share_num").getAsInt();
                            int t_num = obj.get("travel_number").getAsInt();
                            String share_url = obj.get("share_url").getAsString();
                            String share_img = obj.get("share_img").getAsString();
                            String share_description = obj.get("share_description").getAsString();
                            String share_title = obj.get("share_title").getAsString();
                            String m_title = obj.get("memo_title").getAsString();
                            String m_content = obj.get("memo_content").getAsString();
                            Memo m = new Memo(s_num, t_num, share_url, share_img, share_description, share_title, m_title, m_content);

                            m_arr.add(i,m);
                            memoAdapter.notifyDataSetChanged();
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

    void delete_memo(final int share_num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://203.252.182.94/yeobo.php").build();
                Retrofit retrofit = restAdapter.create(Retrofit.class);
                retrofit.delete_memo(13, share_num, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, Response response) {
                        JsonArray result = jsonObject.getAsJsonArray("result");
                        String errcode = ((JsonObject) result.get(0)).get("errorCode").getAsString();

                        if (errcode.equals("success")) {
                            Toast.makeText(getActivity(), "메모를 삭제하였습니다!", Toast.LENGTH_SHORT).show();
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

    /**
     * Created by daeun on 2016-11-10.
     */
    public class MemoAdapter extends ArrayAdapter<Memo> {

        private static final String TAG = "LogCatTest";

        ArrayList<Memo> m_items;
        Context context;
        Bitmap urlbitmapImage;

        public MemoAdapter(Context context, int resource, ArrayList<Memo> objects) {
            super(context, resource, objects);
            this.context = context;
            this.m_items = objects;
        }
        @Override
        public View getView( int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null){//뷰가 없을 시 row 레이아웃 만들어준다.
                LayoutInflater vi =
                        (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.memo,null);
            }
            if(v != null){
                LayoutInflater vi =
                        (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.memo,null);
            }
            final Memo mi = m_items.get(position);//final로 선언해줘야 한다.
            if(mi != null) {
                final TextView titleView = (TextView) v.findViewById(R.id.titleView);
                final TextView subtitleView = (TextView) v.findViewById(R.id.subTitleView);
                final TextView urlView = (TextView) v.findViewById(R.id.urlView);
                final ImageView urlimage = (ImageView)v.findViewById(R.id.url_image);

                final String iurl=mi.getI_url();
                final String url=mi.getS_url();


                if(!mi.getM_title().equals("")){
                    titleView.setText(mi.getM_title());
                }
                if(!mi.getS_title().equals("")){
                    titleView.setText(mi.getS_title());
                }
                /*
                if(titleView!=null) {
                    titleView.setText(mi.getM_title());
                    //titleView.setText(String.valueOf(position));
                }
                if(sharetitle!=null){
                    sharetitle.setText(mi.getS_title());
                    //sharetitle.setText(String.valueOf(position));
                }
                */
                if(!mi.getM_content().equals("")){
                    subtitleView.setText(mi.getM_content());//부제목은 null값 들어오면 아무것도 보이지 않은 상태로 두면 됨.
                }
                if(!mi.getS_description().equals("")){
                    subtitleView.setText(mi.getS_description());
                }
                /*
                if(subtitleView!=null) {
                    subtitleView.setText(mi.getM_content());//부제목은 null값 들어오면 아무것도 보이지 않은 상태로 두면 됨.
                }
                if(sharecontent != null) {
                    sharecontent.setText(mi.getS_description());
                }
                */
            /* imageView에
             * imageUrl을 bitmap으로 바꾼값을 넣어준다.
             */
                if(!iurl.equals("")){
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                urlbitmapImage = getBitmap(iurl);
                            }catch(Exception e) {
                            }finally {
                                if(urlbitmapImage!=null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @SuppressLint("NewApi")
                                        public void run() {
                                            urlimage.setImageBitmap(urlbitmapImage);

                                        }
                                    });
                                }
                            }
                        }
                    }).start();

                    urlimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent myIntent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(url));
                            startActivity(myIntent);
                        }
                    });
                }

                if(!url.equals("")) {
                    urlView.setText(Html.fromHtml("<a href = \"" + url + "\">" +url + "") );
                    urlView.setMovementMethod(LinkMovementMethod.getInstance());
                    if(iurl.equals("")){
                        urlimage.setImageResource(R.drawable.travel);
                    }
                }
            }
            return v;

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
    }


}



