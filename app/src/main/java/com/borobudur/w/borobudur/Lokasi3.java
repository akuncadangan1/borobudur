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

public class Lokasi3 extends AppCompatActivity {

    private boolean installRequested;
    private boolean hasFinishedLoading = false;
    private ArSceneView arSceneView;
    private ViewRenderable Lr1,Lr2,Lr3,Lr4,Lr5,Lr6;
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
        spkategori.setSelection(2);
        spkategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spkategori.getSelectedItem().toString().equals("Fasilitas Umum")){
                    Intent intent = new Intent(Lokasi3.this, Lokasi.class);
                    startActivity(intent);
                    finish();
                }
                if (spkategori.getSelectedItem().toString().equals("Museum")){
                    Intent intent = new Intent(Lokasi3.this, Lokasi2.class);
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
        CompletableFuture<ViewRenderable> lyt6 = ViewRenderable.builder().setView(this, R.layout.viewrenderable2).build();

        CompletableFuture.allOf(lyt1,lyt2,lyt4,lyt5,lyt3,lyt6)
                .handle(
                        (notUsed, throwable) -> {
                            try {
                                Lr1 = lyt1.get();       Lr2 = lyt2.get();
                                Lr4 = lyt4.get();       Lr5 = lyt5.get();
                                Lr3 = lyt3.get();       Lr6 = lyt6.get();
                                hasFinishedLoading = true;

                            } catch (InterruptedException | ExecutionException ex) {
                                LokUtils.displayError(this, "Gagal mengakses renderable", ex);
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

                                LocationMarker gereja = new LocationMarker(110.180426,-7.605681, createRenderable(-7.605681,110.180426,"Bukit Rhema Gereja Ayam","Gereja Ayam merupakan sebuah tempat ibadah yang sudah tidak digunakan dan menjadi tempat wisata. Meskipun disebut Gereja Ayam, bangunan tersebut sebetulnya berbentuk burung merpati.",Lr1));
                                LocationMarker setumbu = new LocationMarker(110.17828,-7.608914, createRenderable(-7.608914,110.17828,"Puthuk Setumbu","Merupakan salah satu spot terbaik untuk menyaksikan sunrise dengan latar Gunung Merapi Merbabu. Dari tempat ini wisatawan juga bisa melihat megahnya Candi Borobudur di pagi hari yang terkurung lautan kabut.",Lr2));
                                LocationMarker camera = new LocationMarker(110.202742,-7.624482, createRenderable(-7.624482,110.202742,"Rumah Kamera","Rumah kamera merupakan salah satu tempat unik di daerah Borobudur. Ini merupakan sebuah rumah biasa, namun bentuknya luar biasa karena berbentuk kamera DSLR.",Lr3));
                                LocationMarker mendut = new LocationMarker(110.228877,-7.605043, createRenderable(-7.605043,110.228877,"Candi Mendut"," Candi Mendut adalah salah satu candi bercorak Budha yang cukup populer di Indonesia.",Lr4));
                                LocationMarker pawon = new LocationMarker(110.219607,-7.606106, createRenderable(-7.606106,110.219607,"Candi Pawon","Candi Pawon merupakan candi budha seperti candi Borobudur. Candi ini berukuran relatif kecil dengan sebuah bilik ini berada tersembunyi di tengah pemukiman penduduk desa setempat.",Lr5));
                                LocationMarker rafting = new LocationMarker(110.22877,-7.610381, createRenderable(-7.610381,110.22877,"Rafting Sungai Elo","Wisata air menyusuri sungai elo menggunakan perahu karet",Lr6));

                                gereja.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr1.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Bukit Rhema Gereja Ayam");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                setumbu.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr2.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Puthuk Setumbu");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                camera.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr3.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Rumah Kamera");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                mendut.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr4.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Candi Mendut");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                pawon.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr5.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Candi Pawon");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });
                                rafting.setRenderEvent(new LocationNodeRender() {
                                    @Override
                                    public void render(LocationNode node) {
                                        View eView = Lr6.getView();
                                        TextView tittle = eView.findViewById(R.id.textView);
                                        TextView distance = eView.findViewById(R.id.textView2);
                                        tittle.setText("Rafting Sungai Elo");
                                        distance.setText(node.getDistance() + "M");
                                    }
                                });

                                locationScene.mLocationMarkers.add(gereja);
                                locationScene.mLocationMarkers.add(setumbu);
                                locationScene.mLocationMarkers.add(camera);
                                locationScene.mLocationMarkers.add(mendut);
                                locationScene.mLocationMarkers.add(pawon);
                                locationScene.mLocationMarkers.add(rafting);

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
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + longi + "&mode=d");
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
