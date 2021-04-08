package com.zly.android.slidetoggleview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.zlylib.slidetogglelib.SlideToggleView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlideToggleView slideToggleView = findViewById(R.id.slideToggleView);
        SlideToggleView slideToggleView2 = findViewById(R.id.slideToggleView2);
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
        slideToggleView2.setVisibility(View.VISIBLE);
        slideToggleView2.postDelayed(new Runnable() {
            @Override
            public void run() {
                slideToggleView2.setVisibility(View.VISIBLE);
            }
        },500);
    }
}