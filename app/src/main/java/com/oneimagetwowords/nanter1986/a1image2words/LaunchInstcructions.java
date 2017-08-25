package com.oneimagetwowords.nanter1986.a1image2words;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LaunchInstcructions extends Activity {

    ImageView instructions;
    TextView playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_instcructions);
        instructions = findViewById(R.id.instructions);
        playButton=findViewById(R.id.playButton);
        instructions.setImageResource(R.drawable.instructions);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToGame();
            }
        });

    }

    private void goToGame(){
        Intent intent=new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
