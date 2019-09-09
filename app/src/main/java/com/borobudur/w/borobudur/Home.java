package com.borobudur.w.borobudur;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;


public class Home extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout1,linearLayout2,linearLayout3;
    private ImageView imagesplash;
    private TextView textView;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            relativeLayout.setVisibility(View.VISIBLE);
            linearLayout1.setVisibility(View.VISIBLE);
            linearLayout2.setVisibility(View.VISIBLE);
            linearLayout3.setVisibility(View.VISIBLE);
            imagesplash.setVisibility(View.GONE);
        }
    };

    private CarouselView carouselView;
    private int[] sampleslide = {R.drawable.slide4,R.drawable.slide3,R.drawable.slide2,R.drawable.slide1};

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        relativeLayout = (RelativeLayout) findViewById(R.id.top);
        linearLayout1 = (LinearLayout) findViewById(R.id.middle);
        linearLayout2 = (LinearLayout) findViewById(R.id.bottom1);
        linearLayout3 = (LinearLayout) findViewById(R.id.bottom2);
        imagesplash = (ImageView) findViewById(R.id.imagesplash);

        handler.postDelayed(runnable, 1000);

        carouselView = findViewById(R.id.carview);
        carouselView.setPageCount(sampleslide.length);
        carouselView.setImageListener(imageListener);

        ImageButton imageButton = (ImageButton)findViewById(R.id.buttonsetting);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Setting.class);
                startActivity(intent);
            }
        });

        CardView cardscan = (CardView) findViewById(R.id.scan);
        cardscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ExploreRelief.class);
                startActivity(intent);
            }
        });

        CardView cardpanduan = (CardView) findViewById(R.id.panduan);
        cardpanduan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Panduan.class);
                startActivity(intent);
            }
        });

        CardView cardinfo = (CardView) findViewById(R.id.info);
        cardinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Info.class);
                startActivity(intent);
            }
        });
        CardView cardlokasi = (CardView) findViewById(R.id.lokasi);
        cardlokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Lokasi.class);
                startActivity(intent);
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
