package com.nawin.navinsasynctasktutorial;

import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import com.google.android.material.snackbar.Snackbar;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    EditText e1, e2;
    TableLayout tableLayout;
    ProgressDialog dialog = null;
    TextView tv;
    Context c;
    private static final int REGISTRATION_TIMEOUT = 3 * 1000;
    private static final int WAIT_TIMEOUT = 30 * 1000;

    private String content = null;
    private boolean error = false;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout=(TableLayout)findViewById(R.id.tablelayout);
        Button submit = (Button) findViewById(R.id.button1);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             readForm(v);

            }
        });
        e1 = (EditText) findViewById(R.id.editText1);
        e2 = (EditText) findViewById(R.id.editText2);

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        c = this;

    }

    public void readForm(View view) {
        final String s1 = e1.getText().toString();
        final String s2 = e2.getText().toString();
        new MyAsyncTask().execute(s1, s2);

    }


    private class MyAsyncTask extends AsyncTask<String, Integer, String> {

        @Override
        protected  void onPreExecute()
        {
            pb.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub

            String s = postData(params);
            return s;
        }

        protected void onPostExecute(String result) {
            pb.setVisibility(View.GONE);

            if(result.equals("success"))
            {
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                i.putExtra("result", e1.getText().toString());
                startActivity(i);
            }
            else
                Snackbar.make(tableLayout, "Wrong credentials", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }

        protected void onProgressUpdate(Integer... progress) {
            pb.setProgress(progress[0]);
        }



    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();

             boolean first = true;
             for(Map.Entry<String, String> entry : params.entrySet()){
            if (first) {
                first = false;

            }
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }


        return result.toString();
    }

    public String postData(String valueIWantToSend[]) {
        // Create a new HttpClient and Post Header
        String response = "";
        int responseCode = 0;
        try {
            URL url = new URL("http://10.0.2.2:8080/ServletTutorial/AndroidServlet");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            HashMap<String,String> params=new HashMap<>();
            params.put("param1", valueIWantToSend[0]);
            params.put("param2", valueIWantToSend[1]);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            Log.d("posrtparam1",getPostDataString(params));
            writer.write(getPostDataString(params));
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
             responseCode=conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;

                }
               response= response.substring(7, response.length());
            } else {
                response = "404";
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
                return response;
    }
}
