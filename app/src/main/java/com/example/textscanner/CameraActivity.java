package com.example.textscanner;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

public class CameraActivity extends AppCompatActivity {
    CameraSource cameraSource;
    SurfaceView surfaceView;
    StringBuilder sb=new StringBuilder();
    String textdetected="No text";
    final int cameraRequestId=1001;
    boolean txtdet=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
       surfaceView=findViewById(R.id.sv);
        TextRecognizer recognizer=new TextRecognizer.Builder(getApplicationContext()).build();
        if(!recognizer.isOperational())
        {

        }
        else
        {
            cameraSource=new CameraSource.Builder(getApplicationContext(),recognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {


                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.CAMERA}, cameraRequestId);
                            return;
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();

                }
            });
            recognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items=detections.getDetectedItems();
                    if (items.size()!=0&&!txtdet)
                    {
                        txtdet=true;
                        for(int i=0;i<items.size();i++)
                        {
                            TextBlock item=items.valueAt(i);
                            sb.append(item.getValue());
                            sb.append("\n");
                        }
                        textdetected=sb.toString();
                        if (txtdet)
                        {
                            startActivity(new Intent(getApplicationContext(),MainActivity.class).putExtra("key",textdetected));
                        }
                    }
                    CameraActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),textdetected,Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case cameraRequestId:
            {
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                        return;
                    try{
                       cameraSource.start(surfaceView.getHolder());
                    }
                    catch (Exception e)
                    {

                    }
                }


            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
