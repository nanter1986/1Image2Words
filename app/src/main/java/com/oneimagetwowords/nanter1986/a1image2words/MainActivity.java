package com.oneimagetwowords.nanter1986.a1image2words;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MainActivity extends Activity {

    private static final SecureRandom random = new SecureRandom();
    String searchQuery;
    Document doc;
    Document doc2;
    Elements img;
    String selectedURL;
    Bitmap bitmap;
    ImageView imageToFind;
    TextView adj1;
    TextView adj2;
    TextView adj3;
    TextView mainW1;
    TextView mainW2;
    TextView mainW3;
    String adjectiveToShow1;
    String adjectiveToShow2;
    String adjectiveToShow3;
    String mainWordToShow1;
    String mainWordToShow2;
    String mainWordToShow3;
    List<Adjectives> adjectivesList;
    List<MainWord> mainWordsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        generalSetup();
        runApp();

    }

    public void generalSetup(){
        setupArraylists();
        setUpViews();
    }

    private void setupArraylists() {
        adjectivesList = new ArrayList<Adjectives>(EnumSet.allOf(Adjectives.class));
        mainWordsList = new ArrayList<MainWord>(EnumSet.allOf(MainWord.class));
    }

    private void setUpViews(){
        imageToFind=findViewById(R.id.imageToFind);
        adj1=findViewById(R.id.adj1);
        adj2=findViewById(R.id.adj2);
        adj3=findViewById(R.id.adj3);
        mainW1=findViewById(R.id.mainW1);
        mainW2=findViewById(R.id.mainW2);
        mainW3=findViewById(R.id.mainW3);
    }

    private void runApp(){
        //dictionaries
        //combine words randomly
        wordWork();
        //search image and display
        //8 words to choose answers from
        //record streaks
        //maybe leaderboards
        netWork();
    }

    private void netWork() {
        workDisplay();
        workData();
    }

    private void workData() {

    }


    private void workDisplay() {
        new GetTheImage().execute();
    }

    private void wordWork() {
        searchQuery=randomEnum(Adjectives.class).getColor()+"+"+randomEnum(MainWord.class).getMainWord();
        Log.i("query",searchQuery);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public class GetTheImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            imageToFind.setImageBitmap(bitmap);
            super.onPostExecute(aVoid);
        }

        protected void grabImageURL(){
            String urlForJsoup = "https://www.google.gr/search?q=" + searchQuery +"&client=ubuntu&hs=QMm&channel=fs&source=lnms&tbm=isch&sa=X&ved=0ahUKEwiCqbiZvpnVAhWsI8AKHVSpD4kQ_AUICigB&biw=1301&bih=323";
            try {
                doc = Jsoup.connect(urlForJsoup).get();
            } catch (IOException e) {
                e.printStackTrace();
                //containers[0].temp.setText("Failed to connect");
            }
            img = doc.select("img[data-src]");
            selectedURL = img.get(0).attr("data-src");
        }

        protected void grabImageFromURL(){
            try {
                doc2 = Jsoup.connect(selectedURL).ignoreContentType(true).get();
            } catch (IOException e) {
                e.printStackTrace();

            }
            bitmap = getBitmapFromURL(selectedURL);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            grabImageURL();
            grabImageFromURL();
            return null;
        }
    }
}
