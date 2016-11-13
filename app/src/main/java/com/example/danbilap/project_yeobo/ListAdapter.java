package com.example.danbilap.project_yeobo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


// gridView에 text, img를 연결해주는 GridAdapter
class ListAdapter extends BaseAdapter {

    Context context;
    int layout;
    int[] background;
    ArrayList<info> t_arr;
    LayoutInflater inf;
    String s_img="";
    Bitmap bitmap;


    public ListAdapter(Context context, int layout, ArrayList<info> t_arr) {
        this.context = context;
        this.layout = layout;
        this.background = background;
        this.t_arr = t_arr;

        // 넣어주기

        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // 배열에 있는 개수만큼 수를 셈
        return t_arr.size();
    }

    @Override
    public Object getItem(int position) {
        return t_arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inf.inflate(layout, null);

//        convertView.setBackgroundResource(background[position % 3]);


        info t = t_arr.get(position);

        TextView title_travel = (TextView)convertView.findViewById(R.id.title_travel);
        title_travel.setText(t.t_title);

        TextView addr_travel = (TextView)convertView.findViewById(R.id.addr_travel);
        addr_travel.setText(t.t_addr);

        TextView tel_travel = (TextView)convertView.findViewById(R.id.tel_travel);
        tel_travel.setText(t.t_tel);

        ImageView img = (ImageView)convertView.findViewById(R.id.t_img);
        s_img=t.t_img;

        //url 이미지 가져오기
        Thread mThread=new Thread(){
            @Override
            public void run(){
                try{
                    URL url=new URL(s_img);
                    HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is =conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                }
                catch (IOException ex){}

          }
        };
        mThread.start();
        try{
            mThread.join();
            img.setImageBitmap(bitmap);
        }catch (InterruptedException e){}
        //url이미지 가져오기


//        img.setImageResource(t.t_img);

        return convertView;
    }
}