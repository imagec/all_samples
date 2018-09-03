package com.example.image.all_samples;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView AweRVdemo = findViewById(R.id.awe_rv_demo);
        TextView cplayer = findViewById(R.id.cplayer_demo);

        AweRVdemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AweRVdemoActivity.class);
                startActivity(intent);
            }
        });

        cplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CplayerActivity.class);
                startActivity(intent);
            }
        });
    }
}
