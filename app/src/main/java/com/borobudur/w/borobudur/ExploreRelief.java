package com.borobudur.w.borobudur;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.borobudur.w.borobudur.expsutil.DatabaseHelper;
import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Frame;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.Collection;
import java.util.Locale;

public class ExploreRelief extends AppCompatActivity implements Scene.OnUpdateListener {
    private CarouselView carouselView;
    private int[] sampleslide = {R.drawable.guide11,R.drawable.guide22};
    private ArFragment arFragment;
    private TextView textViewSnackbar;
    private DatabaseHelper mDBHelper;
    private Cursor cursor,cursor2;
    private String datakisah1;
    private TextToSpeech textToSpeech;
    boolean checkrenderable = true;
    private ImageView imageView;
    private SwitchCompat switchCompat;
    boolean stateswitch,stateexp;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanrelief);

        carouselView = findViewById(R.id.cardlokasi);
        carouselView.setPageCount(sampleslide.length);
        carouselView.setImageListener(imageListener);

        SharedPreferences prefexp = getSharedPreferences("prefexp", MODE_PRIVATE);
        stateexp = prefexp.getBoolean("keyl", true);

        Button buttonmengerti = (Button)findViewById(R.id.buttonmengerti);
        buttonmengerti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateexp = !stateexp;
                SharedPreferences.Editor editor = prefexp.edit();
                editor.putBoolean("keyl", stateexp);
                editor.apply();
                buttonmengerti.setVisibility(View.GONE);

                carouselView.setImageListener(imageListener);
                carouselView.setVisibility(View.GONE);
            }
        });

        if (stateexp == false) {
            carouselView.setVisibility(View.GONE);
            buttonmengerti.setVisibility(View.GONE);
        } else {
            carouselView.setVisibility(View.VISIBLE);
            buttonmengerti.setVisibility(View.VISIBLE);
        }

        ImageButton imageButton = (ImageButton) findViewById(R.id.buttonsetting);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExploreRelief.this, Setting.class);
                startActivity(intent);
            }
        });

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        arFragment.getArSceneView().getScene().addOnUpdateListener(this);

        imageView = (ImageView)findViewById(R.id.imagefitscan);
        scrollView = (ScrollView)findViewById(R.id.nonar);

        SharedPreferences prefsnackbar = getSharedPreferences("prefsnackbar",MODE_PRIVATE );
        boolean statesnack = prefsnackbar.getBoolean("keysnackbar", false);
        if (statesnack) {
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.GONE);
        }

        SharedPreferences preferences = getSharedPreferences("preferences",0 );
        stateswitch = preferences.getBoolean("switchCompat", false);

        switchCompat = (SwitchCompat) findViewById(R.id.switchaudio) ;
        switchCompat.setChecked(stateswitch);

        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateswitch = !stateswitch;
                switchCompat.setChecked(stateswitch);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("switchCompat", stateswitch);
                editor.apply();
            }
        });

        setupvoice();
    }

    private void setupvoice() {
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status!=TextToSpeech.ERROR){
//                    textToSpeech.setLanguage(Locale.ENGLISH);
                    textToSpeech.setLanguage(new Locale("id","ID"));
                }
            }
        });
    }

    @Override
    protected void onPause() {
        if (textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_VISIBLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public void onUpdate(FrameTime frameTime) {
        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<AugmentedImage> images = frame.getUpdatedTrackables(AugmentedImage.class);

        textViewSnackbar= (TextView)findViewById(R.id.textrelief);
        Anchor anchor = null;

        for (AugmentedImage image : images) {
            switch (image.getTrackingState()) {
                case PAUSED:
                    checkrenderable=true;
                    int indt = image.getIndex();
                    getdatakisah1(indt);
                    textViewSnackbar.setText(datakisah1);
                    imageView.setVisibility(View.VISIBLE);
                    break;

                case TRACKING:
                    imageView.setVisibility(View.GONE);
                    if (anchor!=null){
                        anchor.detach();
                    }
                    if (checkrenderable){
                        int ind = image.getIndex();
                        getdatakisah1(ind);
                        anchor = image.createAnchor(image.getCenterPose());
                        createrenderable(anchor);
                        checkrenderable=false;
                    }
                    break;

                case STOPPED:
                    checkrenderable=true;
                    break;
            }
        }
    }

    private void getdatakisah1(int ind) {
        mDBHelper = new DatabaseHelper(this);
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM test WHERE field1 = '" + ind + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            datakisah1 = cursor.getString(1).toString();
        }
    }

    private void createrenderable(Anchor anchor) {
        ViewRenderable.builder()
                .setView(this, R.layout.viewrenderable)
                .build()
                .thenAccept(renderable -> placerenderable(renderable,anchor));
    }

    private void placerenderable(ViewRenderable renderable, Anchor anchor) {
        AnchorNode anchorNode = new AnchorNode(anchor);

        Node node = new Node();
        node.setParent(anchorNode);
        node.setLocalPosition(new Vector3(0f, 0f, 0f));
        node.setLocalRotation(Quaternion.axisAngle(new Vector3(1.0f, 0f, 0f), -90f));
        node.setLocalScale(new Vector3(1f, 1f, 0f));
        node.setRenderable(renderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);

        TextView textrenderable = (TextView)renderable.getView();
        textrenderable.setText(datakisah1);

        String utteranceId=this.hashCode() + "";
        SharedPreferences prefsound = getSharedPreferences("preferences",0 );
        boolean value = prefsound.getBoolean("switchCompat", false);
        if (value == true){
            textToSpeech.speak("Sampel 1", TextToSpeech.QUEUE_FLUSH, null, utteranceId);
            Toast.makeText(this,
                    "audio on", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,
                    "audio off", Toast.LENGTH_SHORT).show();
        }

    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleslide[position]);

        }
    };


}


