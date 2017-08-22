package com.oneimagetwowords.nanter1986.a1image2words;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
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
import java.util.Collections;
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
    boolean inWinOrLoseScreen=false;
    MediaPlayer correctPlayer;
    MediaPlayer wrongPlayer;

    ArrayList<CustomClickButtons>arraylistOfAdjectiveCustomButtons=new ArrayList<>();

    ArrayList<CustomClickButtons>arraylistOfMainwordCustomButtons=new ArrayList<>();

    ArrayList<String>threeAdj=new ArrayList<>();
    String selectedAdjective;

    ArrayList<String>threeMain=new ArrayList<>();
    String selectedMainWord;


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
        setUpViews();
        setupArraylists();

    }

    private void setupArraylists() {
        adjectivesList = new ArrayList<Adjectives>(EnumSet.allOf(Adjectives.class));
        mainWordsList = new ArrayList<MainWord>(EnumSet.allOf(MainWord.class));
        Collections.shuffle(adjectivesList);
        Collections.shuffle(mainWordsList);
        chooseWords();
    }

    private void chooseWords() {
        threeAdj.add(adjectivesList.get(0).getColor());
        threeAdj.add(adjectivesList.get(1).getColor());
        threeAdj.add(adjectivesList.get(2).getColor());

        threeMain.add(mainWordsList.get(0).getMainWord());
        threeMain.add(mainWordsList.get(1).getMainWord());
        threeMain.add(mainWordsList.get(2).getMainWord());

        arraylistOfAdjectiveCustomButtons.get(0).textview.setText(threeAdj.get(0));
        arraylistOfAdjectiveCustomButtons.get(1).textview.setText(threeAdj.get(1));
        arraylistOfAdjectiveCustomButtons.get(2).textview.setText(threeAdj.get(2));

        arraylistOfMainwordCustomButtons.get(0).textview.setText(threeMain.get(0));
        arraylistOfMainwordCustomButtons.get(1).textview.setText(threeMain.get(1));
        arraylistOfMainwordCustomButtons.get(2).textview.setText(threeMain.get(2));


        Collections.shuffle(threeAdj);
        Collections.shuffle(threeMain);
        selectedAdjective=threeAdj.get(0);
        selectedMainWord=threeMain.get(0);
    }

    public void checkIfBothSelected(){
        boolean adjSelected=false;
        boolean mainSelected=false;
        for(int i=0;i<3;i++){
            if(arraylistOfMainwordCustomButtons.get(i).selected==true){
                mainSelected=true;
            }
        }
        for(int i=0;i<3;i++){
            if(arraylistOfAdjectiveCustomButtons.get(i).selected==true){
                adjSelected=true;
            }
        }

        if(adjSelected && mainSelected){
            inWinOrLoseScreen=true;
            checkForCorrectAnswer();
        }
    }

    private void checkForCorrectAnswer() {
        Log.i("selectedboth","bothsselected");
        boolean isAdjectiveCorrect=false;
        boolean isMainwordCorrect=false;
        for(int i=0;i<3;i++){
            if(arraylistOfAdjectiveCustomButtons.get(i).selected==true){
                if(arraylistOfAdjectiveCustomButtons.get(i).textview.getText().toString().equals(selectedAdjective)){
                    isAdjectiveCorrect=true;
                }
            }
        }
        for(int i=0;i<3;i++){
            if(arraylistOfMainwordCustomButtons.get(i).selected==true){
                if(arraylistOfMainwordCustomButtons.get(i).textview.getText().toString().equals(selectedMainWord)){
                    isMainwordCorrect=true;
                }
            }
        }
        if(isAdjectiveCorrect && isMainwordCorrect){
            youWon();
        }else{
            youLost();
        }
    }

    private void youWon() {

        Log.i("selectedboth","you won");
        correctPlayer.start();
        imageToFind.setImageResource(R.drawable.correct);
        goToNext();
    }

    private void goToNext() {
        final Intent myIntent = new Intent(this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(myIntent);
                finish();
            }
        }, 2000);

    }

    private void youLost() {
        Log.i("selectedboth","you lost");
        wrongPlayer.start();
        imageToFind.setImageResource(R.drawable.wrong);
        goToNext();
    }

    private void setUpViews(){
        correctPlayer = MediaPlayer.create(this, R.raw.correct);
        wrongPlayer = MediaPlayer.create(this, R.raw.wrong);
        imageToFind=findViewById(R.id.imageToFind);
        arraylistOfAdjectiveCustomButtons.add(new CustomClickButtons((TextView)findViewById(R.id.adj1)));
        arraylistOfAdjectiveCustomButtons.add(new CustomClickButtons((TextView)findViewById(R.id.adj2)));
        arraylistOfAdjectiveCustomButtons.add(new CustomClickButtons((TextView)findViewById(R.id.adj3)));

        arraylistOfMainwordCustomButtons.add(new CustomClickButtons((TextView)findViewById(R.id.mainW1)));
        arraylistOfMainwordCustomButtons.add(new CustomClickButtons((TextView)findViewById(R.id.mainW2)));
        arraylistOfMainwordCustomButtons.add(new CustomClickButtons((TextView)findViewById(R.id.mainW3)));



        for(int i=0;i<3;i++){
            final int j=i;
            arraylistOfAdjectiveCustomButtons.get(j).textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(inWinOrLoseScreen){

                    }else{
                        if(arraylistOfAdjectiveCustomButtons.get(j).selected==true){
                            arraylistOfAdjectiveCustomButtons.get(j).selected=false;
                            for(int ci=0;ci<3;ci++){
                                arraylistOfAdjectiveCustomButtons.get(ci).textview.setBackgroundColor(Color.BLACK);
                            }
                        }else{
                            arraylistOfAdjectiveCustomButtons.get(j).selected=true;
                            for(int ci=0;ci<3;ci++){
                                arraylistOfAdjectiveCustomButtons.get(ci).textview.setBackgroundColor(Color.BLACK);
                            }
                            arraylistOfAdjectiveCustomButtons.get(j).textview.setBackgroundColor(Color.GREEN);
                        }
                        checkIfBothSelected();
                    }

                }
            });
        }

        for(int i=0;i<3;i++){
            final int j=i;
            arraylistOfMainwordCustomButtons.get(j).textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(inWinOrLoseScreen){

                    }else{
                        if(arraylistOfMainwordCustomButtons.get(j).selected==true){
                            arraylistOfMainwordCustomButtons.get(j).selected=false;
                            for(int ci=0;ci<3;ci++){
                                arraylistOfMainwordCustomButtons.get(ci).textview.setBackgroundColor(Color.BLACK);
                            }
                        }else{
                            arraylistOfMainwordCustomButtons.get(j).selected=true;
                            for(int ci=0;ci<3;ci++){
                                arraylistOfMainwordCustomButtons.get(ci).textview.setBackgroundColor(Color.BLACK);
                            }
                            arraylistOfMainwordCustomButtons.get(j).textview.setBackgroundColor(Color.GREEN);
                        }
                        checkIfBothSelected();
                    }


                }


            });
        }



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
        searchQuery=selectedAdjective+"+"+selectedMainWord;
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
