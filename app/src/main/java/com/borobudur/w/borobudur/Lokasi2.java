package com.borobudur.w.borobudur;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.borobudur.w.borobudur.locsutil.LokUtils;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.core.exceptions.UnavailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ViewRenderable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import uk.co.appoly.arcorelocation.LocationMarker;
import uk.co.appoly.arcorelocation.LocationScene;
import uk.co.appoly.arcorelocation.rendering.LocationNode;
import uk.co.appoly.arcorelocation.rendering.LocationNodeRender;
import uk.co.appoly.arcorelocation.utils.ARLocationPermissionHelper;

public class Lokasi2 extends AppCompatActivity {

    private boolean installRequested;
    private boolean hasFinishedLoading = false;
    private ArSceneView arSceneView;
    private ViewRenderable Lr1,Lr2,Lr3,Lr4,Lr5;
    private LocationScene locationScene;
    private TextView texttittle, textdetail;
    private ImageButton closebutton;
    private Button navigasi;
    private RelativeLayout relativeLayout;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasinew);
        arSceneView = findViewById(R.id.scenelokasi);

        texttittle = (TextView) findViewById(R.id.texttittle);
        textdetail = (TextView) findViewById(R.id.textdetail);
        closebutton = (ImageButton) findViewById(R.id.closebutton);
        navigasi = (Button) findViewById(R.id.navigasi);

        relativeLayout = (RelativeLayout) findViewById(R.id.detaillayoutlokasi);
        relativeLayout.setVisibility(View.GONE);

        Spinner spkategori = (Spinner) findViewById(R.id.sp_name);
        spkategori.setSelection(1);
        spkategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spkategori.getSelectedItem().toString().equals("Fasilitas Umum")){
                    Intent intent = new Intent(Lokasi2.this, Lokasi.class);
                    startActivity(intent);
                    finish();
                }
                if (spkategori.getSelectedItem().toString().equals("Wisata Terdekat")){
                    Intent intent = new Intent(Lokasi2.this, Lokasi3.class);
                    startActivity(intent);
                    finish();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        CompletableFuture<ViewRenderable> lyt1 = ViewRenderable.builder().setView(this, R.layout.viewrenderable2).build();
        CompletableFuture<ViewRenderable> lyt2 = ViewRenderable.builder().setView(this, R.layout.viewrenderable2).build();
        CompletableFuture<ViewRenderable> lyt3 = ViewRenderable.builder().setView(this, R.layout.viewrenderable2).build();
        CompletableFuture<ViewRenderable> lyt4 = ViewRenderable.builder().setView(this, R.layout.viewrenderable2).build();
        CompletableFuture<ViewRenderable> lyt5 = ViewRenderable.builder().setView(this, R.layout.viewrenderable2).build();

        CompletableFuture.allOf(lyt1,lyt2,lyt4,lyt5,lyt3)
                .handle(
                        (notUsed, throwable) -> {
                            try {
                                Lr1 = lyt1.get();       Lr2 = lyt2.get();
                                Lr4 = lyt4.get();       Lr5 = lyt5.get();
                                Lr3 = lyt3.get();
                                hasFinishedLoading = true;

                            } catch (InterruptedException | ExecutionException ex) {
                                LokUtils.displayError(this, "Unable to load renderables", ex);
                            }

                            return null;
                        });

        arSceneView
                .getScene()
                .addOnUpdateListener(
                        frameTime -> {
                            if (!hasFinishedLoading) {
                                return;
                            }

                            if (locationScene == null) {
                                locationScene = new LocationScene(this, this, arSceneView);
                                locationScene.setOffsetOverlapping(true);

                                LocationMarker artgaleri = new LocationMarker(110.201502,-7.604857, createRenderable(-7.604857,110.201502,"Art Galeri","Museum yang berisi koleksi benda-benda kesenian unik",Lr1));
                                LocationMarker gajah = new LocationMarker(110.201378,-7.606622, createRenderable(-7.606622,110.201378,"Safari Gajah","Safari keliling candi Borobudur dengan menaiki gajah",Lr2));
                                LocationMarker perahu = new LocationMarker(110.203412,-7.60465, createRenderable(-7.60465,110.203412,"Museum Perahu Samudra Raksa","Museum ini menampilkan jalur perdagangan bahari antara Indonesia purba, Madagaskar, dan pesisir Afrika Timur, yang mahsyur dijuluki \"Jalur Kayumanis\". Koleksi utama pameran museum ini adalah rekonstruksi Kapal Borobudur dalam ukuran sesungguhnya.",Lr3));
                                LocationMarker karma = new LocationMarker(110.204651,-7.604288, createRenderable(-7.604288,110.204651,"Museum Karmawibangga","Museum ini menampilkan gambar relief Karmawibhangga yang terukir pada kaki tersembunyi Borobudur",Lr4));
                                LocationMarker pintukeluar = new LocationMarker(110.205314,-7.604537, createRenderable(-7.604537,110.205314,"Pintu Keluar","Pintu keluar",Lr5));

                                artgaleri.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr1.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Art Galeri");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                gajah.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr2.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Safari Gajah");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                perahu.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr3.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Museum Perahu Samudra Raksa");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                karma.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr4.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Museum Karmawibangga");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                pintukeluar.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr5.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Pintu Keluar");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });

                                locationScene.mLocationMarkers.add(pintukeluar);
                                locationScene.mLocationMarkers.add(artgaleri);
                                locationScene.mLocationMarkers.add(gajah);
                                locationScene.mLocationMarkers.add(perahu);
                                locationScene.mLocationMarkers.add(karma);

                            }

                            Frame frame = arSceneView.getArFrame();
                            if (frame == null) {
                                return;
                            }

                            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                                return;
                            }

                            if (locationScene != null) {
                                locationScene.processFrame(frame);
                            }

                        });

        ARLocationPermissionHelper.requestPermission(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (locationScene != null) {
            locationScene.resume();
        }

        if (arSceneView.getSession() == null) {
            try {
                Session session = LokUtils.createArSession(this, installRequested);
                if (session == null) {
                    installRequested = ARLocationPermissionHelper.hasPermission(this);
                    return;
                } else {
                    arSceneView.setupSession(session);
                }
            } catch (UnavailableException e) {
                LokUtils.handleSessionException(this, e);
            }
        }

        try {
            arSceneView.resume();
        } catch (CameraNotAvailableException ex) {
            LokUtils.displayError(this, "Unable to get camera", ex);
            finish();
            return;
        }

        if (arSceneView.getSession() != null) {

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (locationScene != null) {
            locationScene.pause();
        }

        arSceneView.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        arSceneView.destroy();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] results) {
        if (!ARLocationPermissionHelper.hasPermission(this)) {
            if (!ARLocationPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                ARLocationPermissionHelper.launchPermissionSettings(this);
            } else {
                Toast.makeText(
                        this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                        .show();
            }
            finish();
        }
    }

    private Node createRenderable(Double lat, Double longi, String tittle, String detail, ViewRenderable renderable) {
        Node base = new Node();
        base.setRenderable(renderable);
        Context c = this;
        View eView = renderable.getView();
        eView.setOnTouchListener((v, event) -> {
            viewdetail(lat, longi,tittle,detail);
            return false;
        });

        return base;
    }

    private void viewdetail(Double lat, Double longi, String tittle, String detail) {

        relativeLayout.setVisibility(View.VISIBLE);
        texttittle.setText(tittle);
        textdetail.setText(detail);
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.GONE);
            }
        });
        navigasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapdirection(lat, longi);
            }
        });
    }

    private void mapdirection(Double lat, Double longi) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + longi + "&mode=w");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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
    public void onBackPressed() {
        super.onBackPressed();
    }

}
