package org.top10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class ParseApplications {

    private  String data;
    private String link;
    private ArrayList<Application> applications;

    public ArrayList<Application> getApplications() {
        return applications;
    }

    public ParseApplications(String xmlData){

        data=xmlData;
        applications=new ArrayList<Application>();

    }
public boolean process(){

     boolean operationStatus=true;

    Application currentRecord=null;

    boolean inEntry=false;
    String textValue="";

    try{
        XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp=factory.newPullParser();
        xpp.setInput(new StringReader(this.data ));
        int eventType=xpp.getEventType();

        while (eventType !=XmlPullParser.END_DOCUMENT){
            String tagName=xpp.getName();

            if(eventType==XmlPullParser.START_TAG){

                if(tagName.equalsIgnoreCase("entry")){
                    inEntry=true;
                    currentRecord=new Application();
                }

            }else if(eventType==XmlPullParser.TEXT){

                textValue=xpp.getText();

            }else if(eventType==XmlPullParser.END_TAG){

                if(inEntry){
                    if(tagName.equalsIgnoreCase("entry")){
                        applications.add(currentRecord);
                        inEntry=false;
                    }
                    if(tagName.equalsIgnoreCase("name")){
                        currentRecord.setName(textValue);
                    }
                   else if(tagName.equalsIgnoreCase("artist")){

                        currentRecord.setArtist(textValue);
                    }
                    else if(tagName.equalsIgnoreCase("releaseDate")){

                        currentRecord.setReleaseDate(textValue);
                    }
                    else if(tagName.equalsIgnoreCase("link")){

                        setCurrentLink(xpp.getAttributeValue(2));
                        Log.d("Log", xpp.getAttributeValue(2));
                    }
                }
            }

            eventType=xpp.next();

        }

    }
    catch (Exception e){
        e.printStackTrace();
        operationStatus=false;
    }

    for(Application app:applications){
        Log.d("LOG","*****************");
        Log.d("LOG",app.getName());
        Log.d("LOG", app.getArtist());
        Log.d("LOG", app.getReleaseDate());


    }

    return operationStatus;
}

    public String getCurrentLink() {
        return link;
    }

    public void setCurrentLink(String link) {
        this.link = link;
    }
}
