package org.top10downloader;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnParse;
    ListView listApps;
String xmlData;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParse=(Button) findViewById(R.id.btnParse);
        listApps=(ListView) findViewById(R.id.listApps);

        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnParse.setVisibility(View.INVISIBLE);
                final ParseApplications parse=new ParseApplications(xmlData);
                boolean operationStatus=parse.process();

                if(operationStatus){

                    ArrayList<Application> allApps=parse.getApplications();
                    listApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                                long id) {

                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse( parse.getCurrentLink()));
                            startActivity(browserIntent);
                        }
                    });


                    ArrayAdapter<Application> adapter=new ArrayAdapter<Application>(MainActivity.this,R.layout.list_item,allApps);
                    listApps.setVisibility(listApps.VISIBLE);
                    listApps.setAdapter(adapter);

                }else{
                    Log.d("MainActivity","Error parsing file");
                }
            }
        });

        new DownloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
    }


private class DownloadData  extends AsyncTask<String,Void,String> {


    String myXmlData;
    protected String doInBackground(String... urls) {
        try {

            myXmlData = downloadXML(urls[0]);
        } catch (IOException e) {
            return "Unable to download XML file.";
        }

        return "";
    }

    protected void onPostExecute(String result){
        Log.d("OnPostExecute", myXmlData);
xmlData=myXmlData;

    }
    private String downloadXML(String theUrl) throws IOException {
        int BUFFER_SIZE = 2000;
        InputStream is = null;
        String xmlContents = "";
        try {

            URL url=new URL(theUrl);
            HttpURLConnection conn=(HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            int response=conn.getResponseCode();
            Log.d("DownloadXML","The response returned is: "+response);
            is=conn.getInputStream();

            InputStreamReader isr=new InputStreamReader(is);
             int charRead;
            char [] inputBuffer=new char[BUFFER_SIZE];
            try{
    while((charRead=isr.read(inputBuffer))>0){

        String readString=String.copyValueOf(inputBuffer,0,charRead);
         xmlContents +=readString;
         inputBuffer=new char[BUFFER_SIZE];

    }
return xmlContents;
            }
catch(IOException e){
    e.printStackTrace();
    return null;

}

        } finally {
            if (is != null)
                is.close();
        }
    }


}

}
