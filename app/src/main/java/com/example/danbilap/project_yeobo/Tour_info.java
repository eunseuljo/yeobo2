package com.example.danbilap.project_yeobo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Tour_info extends AppCompatActivity {
    String nation_id;
    int ination_id=0;
    TextView tv;
    ImageView img;


    ArrayList<info> i_arr;
    ListView listView1;
    ListAdapter listAdapter;
    ProgressDialog m_pdlg;//다운중이면 계속 돌아가는것
    Context context;
    int count=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour_info1);
        init();
        context=this;

        new DownloadWebPageTask().execute("http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=5011065000");

        /*
        Bitmap bitmap;
        String baseshoppingURL="http://tong.visitkorea.or.kr/cms/resource/85/1876185_image3_1.jpg";
        //url 이미지 가져오기
        Thread mThread=new Thread(){
            @Override
            public void run(){
                try{
                    URL url=new URL(baseshoppingURL);
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
        */



    }

    void init() {
        tv=(TextView)findViewById(R.id.tv);
        img=(ImageView)findViewById(R.id.img);

        //지역번호만 넘어오기
        Bundle bundle = getIntent().getExtras(); // intent로 페이지 넘어옴.
        nation_id = bundle.getString("nation_id");
        ination_id = Integer.parseInt(nation_id);
       // tv.setText("" + ination_id);//test
        //지역번호 넘어오기

        i_arr = new ArrayList<info>();
        listView1 = (ListView) findViewById(R.id.listview1);
        listAdapter=new com.example.danbilap.project_yeobo.ListAdapter(this,R.layout.listview1_item,i_arr);
       // listAdapter =new com.example.jo_eunseul.project12.ListAdapter(this,R.layout.listview1_item,i_arr);
        listView1.setAdapter(listAdapter);

        info i =new info( "String title", "String addr", "String tel", "http://tong.visitkorea.or.kr/cms/resource/85/1876185_image2_1.jpg", "String img2");
        i_arr.add(i);
    }

    public class DownloadWebPageTask extends AsyncTask<String, Integer, String> {//원래는 integer아니고 void였음.데이터타입에따라

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            m_pdlg=new ProgressDialog(context);
            m_pdlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            m_pdlg.setMax(100);
            m_pdlg.setMessage("다운로드중입니다.");
            m_pdlg.show();
        }

        // onPreExecute() 실행 후에 백그라운드 쓰레드로 수행됨
        @Override
        protected String doInBackground(String... urls) {//백그라운드
            try{
                return (String)downloadUrl((String)urls[0]);
            }catch (IOException e){
                return "다운로드 실패";
                //e.printStackTrace();
            }
            //return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            m_pdlg.setProgress(values[0]);//처음값
        }

        @Override
        protected void onPostExecute(String result) {//작업이 끝난후

            String word ="";
            int pt_start=-1;
            int pt_end=-1;
            int r_start=-1;
            int r_end=-1;

            int line=0;

            String r_tag_start="<body>";
            String r_tag_end="</body>";

            r_start=result.indexOf(r_tag_start);//큰 tag안 시작
            r_end=result.indexOf(r_tag_end);//큰 tag안 끝

            String tag_start="<temp>";
            String tag_end="</temp>";

            // tv.append("  파싱결과 :" + r_end);
        /*    pt_end = result.indexOf(tag_end, 0);//작은 tag안 끝

            tv.append("  파싱결과 :" + pt_end);
            pt_end = result.indexOf(tag_end, 746);//작은 tag안 끝
            tv.append("  파싱결과 :" + pt_end);*/


            while(line<r_end) {


                pt_start = result.indexOf(tag_start, line);//작은 tag안 시작
                pt_end = result.indexOf(tag_end, line);//작은 tag안 끝
                if(pt_start==-1)
                    break;

                word = result.substring(pt_start + tag_start.length(), pt_end);
                tv.append("  파싱결과 :" + word);

                line = pt_end + 1;


            }

            m_pdlg.dismiss();//ProgressDialog를 없애는것
        }

        private String downloadUrl(String myurl) throws IOException{
            HttpURLConnection conn = null;
            try{
                URL url = new URL(myurl);
                conn = (HttpURLConnection)url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));//bufferReader
                String line = null;
                String page="";
                while((line=bufreader.readLine())!=null){//라인단위
                    page += line;
                    count++;
                    publishProgress(count);
                }
                return page;
            }finally {
                conn.disconnect();//성공이 되던 안되던 disconnect
            }
        }

    }//public class DownloadWebPageTask extends AsyncTask<String, Void, String>



}