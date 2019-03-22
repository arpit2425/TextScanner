package com.example.textscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView text;
    String str;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text=findViewById(R.id.text);
        btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CameraActivity.class));
            }
        });
        Bundle b=getIntent().getExtras();
        if(b!=null)
        {
            str=b.getString("key");
        }
        if(str!=null)
        {
            text.setText(str);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
