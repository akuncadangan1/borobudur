package com.borobudur.w.borobudur;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Info extends AppCompatActivity {

    private String[] listmenu = {"Sejarah", "Waktu & Tiket", "Peta", "Setting"};
    private TextView tittle,content;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        ListView listView = (ListView)findViewById(android.R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,listmenu);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    setcontentinfo();
                    tittle.setText("Sejarah");
                    image.setImageResource(R.drawable.slide1);
                    content.setText(R.string.sejarah);
                } else if (position==1){
                    setcontentinfo();
                    tittle.setText("Waktu & Tiket");
                    image.setImageResource(R.drawable.slide2);
                    content.setText(R.string.sejarah);
                } else if (position==2){
                    setcontentinfo();
                    tittle.setText("Peta");
                    image.setImageResource(R.drawable.slide1);
                    content.setText(R.string.sejarah);
                } else if (position==3){
                    Toast.makeText(getApplicationContext(),"Setting",Toast.LENGTH_SHORT).show();
                }

            }
        });

        ImageButton backq = (ImageButton) findViewById(R.id.backhome1);
        backq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView textView = (TextView) findViewById(R.id.celltittle);
        textView.setText("Info");
    }

    private void setcontentinfo() {
        setContentView(R.layout.cell_layout_info);
        tittle = (TextView)findViewById(R.id.celltittle);
        image = (ImageView)findViewById(R.id.image_cell_layout);
        content = (TextView)findViewById(R.id.text_cell_layout);
        ImageButton back = (ImageButton) findViewById(R.id.backhome1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Info.this, Info.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
