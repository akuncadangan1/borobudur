package com.borobudur.w.borobudur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class Panduan extends AppCompatActivity {

    private CarouselView carouselView;
    private int[] sampleslide = {R.drawable.guide11,R.drawable.guide22,R.drawable.lokasi1,R.drawable.lokasi2,R.drawable.lokasi3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panduan);

        carouselView = findViewById(R.id.carview);
        carouselView.setPageCount(sampleslide.length);
        carouselView.setImageListener(imageListener);

        Button kembali = (Button) findViewById(R.id.panduankembali);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleslide[position]);
        }
    };
}
