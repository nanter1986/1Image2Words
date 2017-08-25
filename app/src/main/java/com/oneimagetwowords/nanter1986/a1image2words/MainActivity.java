package com.oneimagetwowords.nanter1986.a1image2words;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

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
import java.util.Random;

public class MainActivity extends Activity {

    private static final SecureRandom random = new SecureRandom();
    private static final int UNLOCK_LIMIT = 3;
    private InterstitialAd interstitial;
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
    MediaPlayer highscoreSound;
    MediaPlayer unlocked;

    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    int currentScorePoints;
    int unlockCounter;
    int highScorePoints;
    int unlockablesSize;
    int totalUnlockables;

    TextView currentScoreDisplay;
    TextView highscoreDisplay;
    TextView toUnlockDisplay;

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
        prepareIntAd();
        context = getApplicationContext();
        sharedPreferences = getPreferences(MODE_PRIVATE);
        editor = sharedPreferences.edit();
        setUpViews();
        currentScorePoints=getScoreFromIntent(savedInstanceState,"theScore");
        unlockCounter=getScoreFromIntent(savedInstanceState,"theCounter");
        currentScoreDisplay.setText("current\nscore:\n"+currentScorePoints+"");
        highScorePoints=sharedPreferences.getInt("highScore",0);
        highscoreDisplay.setText("HIGH\nSCORE:\n"+highScorePoints+"");
        unlockablesSize=sharedPreferences.getInt("unlockables",10);
        toUnlockDisplay.setText("New Word:\n"+unlockCounter+"/"+UNLOCK_LIMIT);
        Log.i("unlockables",unlockCounter+" "+UNLOCK_LIMIT);
        setupArraylists();
        runApp();

    }

    private void prepareIntAd() {
        //ca-app-pub-1155245883636527~3885643773
        interstitial = new InterstitialAd(MainActivity.this);
        interstitial.setAdUnitId("ca-app-pub-1155245883636527/3106736961");
        interstitial.loadAd(new AdRequest.Builder().build());
        Log.i("unlockables","into ad");
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                //interstitial.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("unlockables","display ad ready");
                displayInterstitial();
            }


        });
    }

    private void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        } else {
            interstitial.loadAd(new AdRequest.Builder().build());
        }
    }


    private int getScoreFromIntent(Bundle savedInstanceState,String key) {
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString(key);
            }
        } else {
            newString= (String) savedInstanceState.getSerializable(key);
        }
        if(newString==null){
            newString="0";
        }
        int score=Integer.parseInt(newString);
        return score;
    }

    private void setupArraylists() {
        adjectivesList = new ArrayList<Adjectives>(EnumSet.allOf(Adjectives.class));
        totalUnlockables = new ArrayList<MainWord>(EnumSet.allOf(MainWord.class)).size();
        if(unlockablesSize>totalUnlockables){
            unlockablesSize=totalUnlockables;
        }
        Log.i("unlockables",unlockablesSize+"/"+totalUnlockables);
        mainWordsList = new ArrayList<MainWord>(EnumSet.allOf(MainWord.class)).subList(0,unlockablesSize-1);
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

        Log.i("selectedboth","you won ");
        currentScorePoints++;
        unlockCounter++;
        currentScoreDisplay.setText("current\nscore:\n"+currentScorePoints+"");
        toUnlockDisplay.setText("New Word:\n"+unlockCounter+"/"+UNLOCK_LIMIT);
        //unlock display
        if(unlockCounter==UNLOCK_LIMIT && unlockablesSize<totalUnlockables){
            unlockCounter=0;
            unlockablesSize++;
            editor.putInt("unlockables",unlockablesSize);
            editor.commit();
            unlocked.start();
            imageToFind.setImageResource(R.drawable.unlock);
            for(int i=0;i<3;i++){
                arraylistOfMainwordCustomButtons.get(i).textview.setText("");
                arraylistOfMainwordCustomButtons.get(i).textview.setBackgroundColor(Color.WHITE);
                arraylistOfAdjectiveCustomButtons.get(i).textview.setText("");
                arraylistOfAdjectiveCustomButtons.get(i).textview.setBackgroundColor(Color.WHITE);
            }
            arraylistOfAdjectiveCustomButtons.get(0).textview.setText("Unlocked new Word:");
            arraylistOfAdjectiveCustomButtons.get(0).textview.setBackgroundColor(Color.YELLOW);
            arraylistOfMainwordCustomButtons.get(0).textview.setText(new ArrayList<MainWord>(EnumSet.allOf(MainWord.class)).get(unlockablesSize-1).getMainWord());
            arraylistOfMainwordCustomButtons.get(0).textview.setBackgroundColor(Color.YELLOW);
            arraylistOfAdjectiveCustomButtons.get(1).textview.setText("Unlocked Words:");
            arraylistOfAdjectiveCustomButtons.get(1).textview.setBackgroundColor(Color.LTGRAY);
            arraylistOfMainwordCustomButtons.get(1).textview.setText(unlockablesSize+"/"+new ArrayList<MainWord>(EnumSet.allOf(MainWord.class)).size());
            arraylistOfMainwordCustomButtons.get(1).textview.setBackgroundColor(Color.LTGRAY);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    moveForwardWin();

                }
            }, 4000);

        }else{
            moveForwardWin();
        }



    }

    private void moveForwardWin(){
        if(checkForHighScore()){
            highscoreSound.start();
            imageToFind.setImageResource(R.drawable.highscore);
            goToNext(4000);
        }else{
            correctPlayer.start();
            imageToFind.setImageResource(R.drawable.correct);
            goToNext(2000);
        }
    }

    private boolean checkForHighScore() {
        boolean isHighScore=false;
        if(currentScorePoints>highScorePoints){

            editor.putInt("highScore",currentScorePoints);
            editor.commit();
            isHighScore=true;
        }
        return isHighScore;
    }

    private void goToNext(int waitInMillis) {
        final Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.putExtra("theScore", currentScorePoints+"");
        myIntent.putExtra("theCounter", unlockCounter+"");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(myIntent);
                finish();
            }
        }, waitInMillis);

    }

    private void youLost() {
        Log.i("selectedboth","you lost");
        currentScorePoints=0;
        unlockCounter=0;
        wrongPlayer.start();
        imageToFind.setImageResource(R.drawable.wrong);
        goToNext(2000);
    }

    private void setUpViews(){
        correctPlayer = MediaPlayer.create(this, R.raw.correct);
        wrongPlayer = MediaPlayer.create(this, R.raw.wrong);
        highscoreSound = MediaPlayer.create(this, R.raw.highscore);
        unlocked = MediaPlayer.create(this, R.raw.unlock);
        imageToFind=findViewById(R.id.imageToFind);
        currentScoreDisplay=findViewById(R.id.currentScore);
        highscoreDisplay=findViewById(R.id.highScore);
        toUnlockDisplay=findViewById(R.id.toUnlock);
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
        wordWork();
        workDisplay();

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
            int index;
            int size=img.size();
            if(size>0){
                index=new Random().nextInt(size);
            }else{
                index=0;
            }
            Log.i("randomImageIndex",index+" "+size);
            selectedURL = img.get(index).attr("data-src");
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
