package com.example.danbilap.project_yeobo;

<<<<<<< HEAD
=======

>>>>>>> d38fb5ca352d4784be3429f867999aaae2ad5d8a
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class TestFragment4 extends Fragment {
    int n_id;//나라순서

    public static TestFragment4 newInstance(int n_id){
        TestFragment4 instance = new TestFragment4();
        instance.n_id = n_id;//나라 순서
        return instance;
    }
    TextView tv;
    ProgressDialog m_pdlg;//다운중이면 계속 돌아가는것
    Context context;
    int count=0;
    String addr=null;
    String real_addr=null;

    String data=null;
    String data2=null;

    int nation_id=1;//은슬

    TextView degree1;//섭씨
    TextView degree2;//화씨
    TextView high;//최고온도
    TextView low;//최저온도
    TextView state;//상태
    ImageView img;//이미지

    Button btn1;

    String s_degree1=null;//섭씨
    String s_degree2=null;//화씨
    String s_high=null;//최고온도
    String s_low=null;//최저온도
    String s_state=null;//상태

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_test_4, container, false);

        nation_id=n_id;//넘어온 나라번호 넘겨주기
        context=getActivity();
        //tv=(TextView)rootView.findViewById(R.id.tv);
        degree1=(TextView)rootView.findViewById(R.id.degree1);
        degree2=(TextView)rootView.findViewById(R.id.degree2);
        high=(TextView)rootView.findViewById(R.id.high);
        low=(TextView)rootView.findViewById(R.id.low);
        //state=(TextView)rootView.findViewById(R.id.state);
        img=(ImageView)rootView.findViewById(R.id.img);
        btn1=(Button)rootView.findViewById(R.id.btn1);
        //init();
        real_addr=xmlparsing();//xml파싱 위치를 가져온다.
        //addr="http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1114055000";
        new DownloadWebPageTask().execute(real_addr);//날씨를 가져온다.

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity().getApplication(), Tour_info.class);
                Bundle bundle = new Bundle();
                bundle.putString("nation_id",""+nation_id);//은슬
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });


        return rootView;
    }
    String xmlparsing(){
        String file = "basic_info.xml";
        String result = "";
        try{
            InputStream is = getActivity().getAssets().open(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            result = new String(buffer, "utf-8");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));
            int eventType = xpp.getEventType();
            boolean aSet = false;
            boolean bSet = false;


            while(eventType != XmlPullParser.END_DOCUMENT){
                if(eventType==XmlPullParser.START_DOCUMENT){
                    ;

                }
                else if(eventType== XmlPullParser.START_TAG){
                    String tag_name = xpp.getName();
                    if(tag_name.equals("code")) {
                        bSet=true;
                    }
                    if(tag_name.equals("addr")) {
                        aSet=true;
                    }
                }else if(eventType==XmlPullParser.TEXT){
                    if(bSet){
                        data = xpp.getText();
                        bSet = false;
                    }
                    if(aSet){
                        if(data.equals(""+nation_id)) {//코드번호에 따른 정보를 가져옴.은슬
                            data2 = xpp.getText();
                            real_addr=data2;
                            //tv.append(data2 + "\n");
                            aSet = false;
                        }
                    }
                }
<<<<<<< HEAD


=======


>>>>>>> d38fb5ca352d4784be3429f867999aaae2ad5d8a
                else if(eventType==XmlPullParser.END_TAG){
                    ;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            tv.setText(e.getMessage());
        }
        return real_addr;
    }
<<<<<<< HEAD
//xml파싱
=======
    //xml파싱
>>>>>>> d38fb5ca352d4784be3429f867999aaae2ad5d8a
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

<<<<<<< HEAD
    @Override
    protected void onPostExecute(String result) {//작업이 끝난후
        //   tv.append(result+"\n");
        String word ="";
        int pt_start=-1;
        int pt_end=-1;

        //1.현재온도(섭씨) 2.현재온도(화씨)
        String tag_start="<temp>";
        String tag_end="</temp>";
        pt_start=result.indexOf(tag_start);
        if(pt_start != -1){
            pt_end = result.indexOf(tag_end);
            if(pt_end!= -1){
                word = result.substring(pt_start+tag_start.length(), pt_end);
                double word2 = Double.parseDouble(word);
                int cel=(int)word2;
                int cel2=(int)(word2*1.8)+32;//섭씨->화씨

                degree1.setText(""+cel);//1.섭씨
                degree2.setText(""+cel2);//2.화씨
            }else
                degree1.setText("데이터가 없습니다.");
        }else
            degree1.setText("데이터가 없습니다.");

        //3.최고온도
        tag_start="<tmx>";
        tag_end="</tmx>";
        pt_start=result.indexOf(tag_start);
        if(pt_start != -1){
            pt_end = result.indexOf(tag_end);
            if(pt_end!= -1){
                word = result.substring(pt_start+tag_start.length(), pt_end);
                double word2 = Double.parseDouble(word);
                if(word2==-999.0)
                    high.setText("-");//3.최고온도
                else {
                    word2 = Double.parseDouble(word);
                    int h = (int) word2;
                    high.setText(""+h);//3.최고온도
                }
            }else
                high.setText("데이터가 없습니다.");
        }else
            high.setText("데이터가 없습니다.");


        //4.최저온도
        tag_start="<tmn>";
        tag_end="</tmn>";
        pt_start=result.indexOf(tag_start);
        if(pt_start != -1){
            pt_end = result.indexOf(tag_end);
            if(pt_end!= -1){
                word = result.substring(pt_start+tag_start.length(), pt_end);
                double word2 = Double.parseDouble(word);
                if(word2==-999.0)
                    low.setText("-");//4.최저온도
                else {
                    word2 = Double.parseDouble(word);
                    int l = (int) word2;
                    low.setText(""+l);//4.최저온도
                }
            }else
                low.setText("데이터가 없습니다.");
        }else
            low.setText("데이터가 없습니다.");

        //5.상태
        tag_start="<wfEn>";
        tag_end="</wfEn>";
        pt_start=result.indexOf(tag_start);
        if(pt_start != -1){
            pt_end = result.indexOf(tag_end);
            if(pt_end!= -1){
                word = result.substring(pt_start+tag_start.length(), pt_end);
                // state.setText(word);//5.상태
                if(word.equals("Clear")){
                    img.setImageResource(R.drawable.w1);
                }
                if(word.equals("Partly Cloudy")||word.equals("Mostly Cloudy")){
                    img.setImageResource(R.drawable.w2);
                }
                if(word.equals("Cloudy")){
                    img.setImageResource(R.drawable.w4);
                }
                if(word.equals("Rain")){
                    img.setImageResource(R.drawable.w5);
                }
                if(word.equals("Snow")){
                    img.setImageResource(R.drawable.w6);
                }
            }else
                state.setText("데이터가 없습니다.");
        }else
            state.setText("데이터가 없습니다.");


        m_pdlg.dismiss();//ProgressDialog를 없애는것
    }
=======
        @Override
        protected void onPostExecute(String result) {//작업이 끝난후
            //   tv.append(result+"\n");
            String word ="";
            int pt_start=-1;
            int pt_end=-1;

            //1.현재온도(섭씨) 2.현재온도(화씨)
            String tag_start="<temp>";
            String tag_end="</temp>";
            pt_start=result.indexOf(tag_start);
            if(pt_start != -1){
                pt_end = result.indexOf(tag_end);
                if(pt_end!= -1){
                    word = result.substring(pt_start+tag_start.length(), pt_end);
                    double word2 = Double.parseDouble(word);
                    int cel=(int)word2;
                    int cel2=(int)(word2*1.8)+32;//섭씨->화씨

                    degree1.setText(""+cel);//1.섭씨
                    degree2.setText(""+cel2);//2.화씨
                }else
                    degree1.setText("데이터가 없습니다.");
            }else
                degree1.setText("데이터가 없습니다.");

            //3.최고온도
            tag_start="<tmx>";
            tag_end="</tmx>";
            pt_start=result.indexOf(tag_start);
            if(pt_start != -1){
                pt_end = result.indexOf(tag_end);
                if(pt_end!= -1){
                    word = result.substring(pt_start+tag_start.length(), pt_end);
                    double word2 = Double.parseDouble(word);
                    if(word2==-999.0)
                        high.setText("-");//3.최고온도
                    else {
                        word2 = Double.parseDouble(word);
                        int h = (int) word2;
                        high.setText(""+h);//3.최고온도
                    }
                }else
                    high.setText("데이터가 없습니다.");
            }else
                high.setText("데이터가 없습니다.");


            //4.최저온도
            tag_start="<tmn>";
            tag_end="</tmn>";
            pt_start=result.indexOf(tag_start);
            if(pt_start != -1){
                pt_end = result.indexOf(tag_end);
                if(pt_end!= -1){
                    word = result.substring(pt_start+tag_start.length(), pt_end);
                    double word2 = Double.parseDouble(word);
                    if(word2==-999.0)
                        low.setText("-");//4.최저온도
                    else {
                        word2 = Double.parseDouble(word);
                        int l = (int) word2;
                        low.setText(""+l);//4.최저온도
                    }
                }else
                    low.setText("데이터가 없습니다.");
            }else
                low.setText("데이터가 없습니다.");

            //5.상태
            tag_start="<wfEn>";
            tag_end="</wfEn>";
            pt_start=result.indexOf(tag_start);
            if(pt_start != -1){
                pt_end = result.indexOf(tag_end);
                if(pt_end!= -1){
                    word = result.substring(pt_start+tag_start.length(), pt_end);
                    // state.setText(word);//5.상태
                    if(word.equals("Clear")){
                        img.setImageResource(R.drawable.w1);
                    }
                    if(word.equals("Partly Cloudy")||word.equals("Mostly Cloudy")){
                        img.setImageResource(R.drawable.w2);
                    }
                    if(word.equals("Cloudy")){
                        img.setImageResource(R.drawable.w4);
                    }
                    if(word.equals("Rain")){
                        img.setImageResource(R.drawable.w5);
                    }
                    if(word.equals("Snow")){
                        img.setImageResource(R.drawable.w6);
                    }
                }else
                    state.setText("데이터가 없습니다.");
            }else
                state.setText("데이터가 없습니다.");


            m_pdlg.dismiss();//ProgressDialog를 없애는것
        }
>>>>>>> d38fb5ca352d4784be3429f867999aaae2ad5d8a

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
