package com.zly.android.slidetoggleview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.zlylib.slidetogglelib.SlideToggleView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlideToggleView slideToggleView = findViewById(R.id.slideToggleView);
        slideToggleView.setSlideToggleListener(new SlideToggleView.SlideToggleListener() {
            @Override
            public void onBlockPositionChanged(SlideToggleView view, int left, int total, int slide) {
            }
            @Override
            public void onSlideListener(SlideToggleView view, int leftOrRight) {
                Toast.makeText(MainActivity.this, " "+leftOrRight,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}