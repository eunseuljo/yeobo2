package com.example.danbilap.project_yeobo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dh814_000 on 2016-11-14.
 */
class CustomAdapter extends ArrayAdapter<Bitmap> {
    ArrayList<String> titles;
    ArrayList<String> contents;
    ArrayList<Bitmap> bitmaps;
    ArrayList<String> names;
    Context context;

    public CustomAdapter(Context context, int textViewResourceId, ArrayList<String> titles , ArrayList<String> contents, ArrayList<Bitmap> bitmaps, ArrayList<String> names ) {
        super(context, textViewResourceId, bitmaps);
        this.titles=titles;
        this.contents=contents;
        this.bitmaps = bitmaps;
        this.context = context;
        this.names=names;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        View v = view;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.custom_list, null);
        }
        String title=titles.get(position);
        String content=contents.get(position);
        Bitmap item = bitmaps.get(position);

        // 이미지 크기를 새로 설정한다.
        BitmapFactory.Options bo = new BitmapFactory.Options();
        bo.inSampleSize = 4;
        Bitmap bmp = BitmapFactory.decodeFile("http://203.252.182.94/uploads/"+names.get(position), bo);
        //      Bitmap resized = Bitmap.createScaledBitmap(bmp, 95, 95, true);

        if (item != null)
        {
            TextView textView1=(TextView)v.findViewById(R.id.title);
            textView1.setText(title);

            TextView textView2=(TextView)v.findViewById(R.id.content);
            textView2.setText(content);

            ImageView imageView=(ImageView)v.findViewById(R.id.picture_name);
            imageView.setImageBitmap(item);
        }
        return v;
    }
}
