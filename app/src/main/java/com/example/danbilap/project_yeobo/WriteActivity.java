package com.example.danbilap.project_yeobo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class WriteActivity extends Activity {

    private static final String TAG_TRAVEL = "travel_number";
    private static final String TAG_CITY = "c_num";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_PICTURE = "picture_name";


    Button searchButton;
    Button uploadButton;
    EditText editText;
    EditText editText2;

    int serverResponseCode = 0;
    ProgressDialog dialog = null;

    String upLoadServerUri = null;


    String path="";
    String name="";
    String picture_name="";




    int t_num;
    int c_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);
        searchButton = (Button)findViewById(R.id.searchButton);
        uploadButton = (Button)findViewById(R.id.uploadButton);
        editText=(EditText)findViewById(R.id.review);
        editText2=(EditText)findViewById(R.id.title);

        Intent i=getIntent();
        t_num=i.getIntExtra(TAG_TRAVEL,-1);
        c_num=i.getIntExtra(TAG_CITY,-1);

        /************* Php script path ****************/
        upLoadServerUri = "http://203.252.182.94/picture_upload.php";//서버컴퓨터의 ip주소

        searchButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //           messageText.setText("uploading started.....");
                            }
                        });


                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, 1);


                    }
                }).start();

            }
        });

        uploadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(WriteActivity.this, "", "Uploading file...", true);

                String title=editText2.getText().toString();
                String content=editText.getText().toString();

                insertToDatabase(title, content, picture_name);
                uploadFile(path);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri=null;
        try{
            uri=data.getData();

            path=getPath(uri);

            int pos=path.lastIndexOf("/");
            picture_name=path.substring(pos+1);

            Toast.makeText(getApplicationContext(),picture_name,Toast.LENGTH_SHORT).show();

            Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            ImageView imageView=(ImageView)findViewById(R.id.imv);
            imageView.setImageBitmap(image_bitmap);

        }catch (Exception e){

        }



//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 1;
//
//        Bitmap src = BitmapFactory.decodeFile(path, options );
//        Bitmap resized = Bitmap.createScaledBitmap( src, 5000, 5000, true );
//
//        ImageView imageView=(ImageView)findViewById(R.id.imageview);
//        //imageView.setImageBitmap(resized);
//        imageView.setImageBitmap(src);
        // uploadFile(path);
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }
    private void insertToDatabase(String title, String content, String name){

        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(WriteActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                try{

                    String title = (String)params[0];
                    String content = (String)params[1];
                    String name = (String)params[2];

                    String link="http://203.252.182.94/insert.php";
                    String data  = URLEncoder.encode(TAG_TRAVEL, "UTF-8") + "=" + URLEncoder.encode(String.valueOf(t_num), "UTF-8");
                    data += "&" + URLEncoder.encode(TAG_CITY, "UTF-8") + "=" + URLEncoder.encode(String.valueOf(c_num), "UTF-8");
                    data += "&" + URLEncoder.encode(TAG_TITLE, "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
                    data += "&" + URLEncoder.encode(TAG_CONTENT, "UTF-8") + "=" + URLEncoder.encode(content, "UTF-8");
                    data += "&" + URLEncoder.encode(TAG_PICTURE, "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");


                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception2: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(title,content,picture_name);
    }


    public int uploadFile(final String sourceFileUri) {


        final File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :");

            runOnUiThread(new Runnable() {
                public void run() {
                    //      messageText.setText("Source File not exist : ");
                }
            });

            return 0;

        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String fileName = sourceFileUri;

                        HttpURLConnection conn = null;
                        DataOutputStream dos = null;
                        String lineEnd = "\r\n";
                        String twoHyphens = "--";
                        String boundary = "*****";
                        int bytesRead, bytesAvailable, bufferSize;
                        byte[] buffer;
                        int maxBufferSize = Integer.MAX_VALUE;

                        // open a URL connection to the Servlet
                        FileInputStream fileInputStream = new FileInputStream(sourceFile);
                        URL url = new URL(upLoadServerUri);

                        // Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                        // Open a HTTP  connection to  the URL
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true); // Allow Inputs
                        conn.setDoOutput(true); // Allow Outputs
                        conn.setUseCaches(false); // Don't use a Cached Copy
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Connection", "Keep-Alive");
                        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                        conn.setRequestProperty("uploaded_file", fileName);

//                        Toast.makeText(getApplicationContext(),fileName+"2",Toast.LENGTH_SHORT).show();
                        dos = new DataOutputStream(conn.getOutputStream());

                        dos.writeBytes(twoHyphens + boundary + lineEnd);
                        dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                                + fileName + "\"" + lineEnd);

                        dos.writeBytes(lineEnd);

                        // create a buffer of  maximum size
                        bytesAvailable = fileInputStream.available();

                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        buffer = new byte[bufferSize];

                        // read file and write it into form...
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        while (bytesRead > 0) {

                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = fileInputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                        }

                        // send multipart form data necesssary after file data...
                        dos.writeBytes(lineEnd);
                        dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                        // Responses from the server (code and message)
                        serverResponseCode = conn.getResponseCode();
                        String serverResponseMessage = conn.getResponseMessage();

                        Log.i("uploadFile", "HTTP Response is : "
                                + serverResponseMessage + ": " + serverResponseCode);

                        if(serverResponseCode == 200){

                            runOnUiThread(new Runnable() {
                                public void run() {

                                    String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                            +path;

//                                    messageText.setText(msg);
                                    Toast.makeText(WriteActivity.this, "File Upload Complete.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        //close the streams //
                        fileInputStream.close();
                        dos.flush();
                        dos.close();

                    } catch (MalformedURLException ex) {

                        dialog.dismiss();
                        ex.printStackTrace();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                ///    messageText.setText("MalformedURLException Exception : check script url.");
                                Toast.makeText(WriteActivity.this, "MalformedURLException",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                        Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                    } catch (Exception e) {

                        dialog.dismiss();
                        e.printStackTrace();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                //   messageText.setText("Got Exception : see logcat ");
                                Toast.makeText(WriteActivity.this, "Got Exception : see logcat ",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.e("Upload Exception", "Exception : " + e.getMessage(), e);
                    }
                }
            }).start();

            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }





}
