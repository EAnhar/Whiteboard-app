package com.example.whiteboard;
//
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import com.samsung.android.sdk.SsdkUnsupportedException;
import com.samsung.android.sdk.SsdkVendorCheck;
import com.samsung.android.sdk.pen.Spen;

public class MainActivity extends AppCompatActivity {

    private ImageView drawingCanvas;
    private TextView infoTextView;
    private Canvas canvas;
    private Paint paint;
    private float lastX, lastY;
    private long lastDrawTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize S-Pen SDK
        if (!initializeSpen()) {
            // Handle initialization failure
        }

        drawingCanvas = findViewById(R.id.drawingCanvas);
        infoTextView = findViewById(R.id.infoTextView);

        Bitmap bitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);

        drawingCanvas.setImageBitmap(bitmap);
    }

    private boolean initializeSpen() {
//        SsdkVendorCheck vendorCheck = new SsdkVendorCheck();
//        SsdkVendorCheck vendorCheck = SsdkVendorCheck.getInstance();
        if(!SsdkVendorCheck.isSamsungDevice()) {
            return false;
        }

//        try {
//            SsdkVendorCheck.checkVendor(this);
//        } catch (SsdkUnsupportedException e) {
//            return false; // The device is not a Samsung device or not supported
//        }

//        Spen spenPackage = new Spen();
//        try {
//            spenPackage.initialize(this);
//            if(!spenPackage.isFeatureEnabled(Spen.DEVICE_PENPOINT)) {
//                return false; // The device doesn't support the S-Pen.
//            }
//        } catch (SsdkUnsupportedException e) {
//            e.printStackTrace();
//            return false;
//        }
       return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float pressure = event.getPressure();
//        float tiltX = event.getAxisValue(MotionEvent.AXIS_TILT_X);
//        float tiltY = event.getAxisValue(MotionEvent.AXIS_TILT_Y);
        float tilt = event.getAxisValue(MotionEvent.AXIS_TILT);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                lastDrawTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                canvas.drawLine(lastX, lastY, x, y, paint);
                drawingCanvas.invalidate();
                lastX = x;
                lastY = y;

                float speed = calculateSpeed(x, y, lastDrawTime);
//                updateTextView(x, y, pressure, speed, tiltX, tiltY);
                updateTextView(x, y, pressure, speed, tilt);
                lastDrawTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    private float calculateSpeed(float x, float y, long lastTime) {
        double distance = Math.sqrt(Math.pow(lastX - x, 2) + Math.pow(lastY - y, 2));
        double timeElapsed = (System.currentTimeMillis() - lastTime) / 1000.0; // in seconds
        return (float) (distance / timeElapsed); // pixels per second
    }

//    private void updateTextView(float x, float y, float pressure, float speed, float tiltX, float tiltY) {
private void updateTextView(float x, float y, float pressure, float speed, float tilt) {

    String text = String.format(Locale.getDefault(),
//                "X: %.2f, Y: %.2f, Pressure: %.2f, Speed: %.2f pps, Tilt X: %.2f, Tilt Y: %.2f",
//                x, y, pressure, speed, tiltX, tiltY);
            "X: %.2f, Y: %.2f, Pressure: %.2f, Speed: %.2f pps, Tilt: %.2f",
            x, y, pressure, speed, tilt);
        infoTextView.setText(text);
    }
}
///////////////////////////////////////////////////////////////////////////////////////////////////
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.samsung.android.sdk.pen.document.SpenNoteDoc;
//import com.samsung.android.sdk.pen.document.SpenPageDoc;
//import com.samsung.android.sdk.pen.engine.SpenSurfaceView;
//import com.samsung.android.sdk.pen.engine.SpenTouchListener;
//
//public class MainActivity extends AppCompatActivity {
//
//    private ImageView drawingCanvas;
//    private SpenSurfaceView spenSurfaceView;
//    private SpenNoteDoc spenNoteDoc;
//    private SpenPageDoc spenPageDoc;
//    private Bitmap bitmap;
//    private Canvas canvas;
//    private Paint paint;
//    private TextView infoTextView;
//    private float lastX, lastY;
//    private long lastDrawTime;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        drawingCanvas = findViewById(R.id.drawingCanvas);
//        infoTextView = findViewById(R.id.infoTextView);
//
//        spenSurfaceView = new SpenSurfaceView(this);
//        drawingCanvas.addView(spenSurfaceView);
//
//        paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setStrokeWidth(5f);
//
//        spenSurfaceView.setPenListener(new SpenTouchListener() {
//            @Override
//            public boolean onTouchFinger(View view, MotionEvent event) {
//                return false;
//            }
//
//            @Override
//            public boolean onTouchPen(View view, MotionEvent event) {
//                float x = event.getX();
//                float y = event.getY();
//                float pressure = event.getPressure();
//
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        lastX = x;
//                        lastY = y;
//                        lastDrawTime = System.currentTimeMillis();
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        canvas.drawLine(lastX, lastY, x, y, paint);
//                        spenSurfaceView.update();
//                        lastX = x;
//                        lastY = y;
//
//                        float speed = calculateSpeed(x, y, lastDrawTime);
//                        float tiltX = event.getAxisValue(MotionEvent.AXIS_TILT_X);
//                        float tiltY = event.getAxisValue(MotionEvent.AXIS_TILT_Y);
//
//                        updateTextView(x, y, pressure, speed, tiltX, tiltY);
//                        lastDrawTime = System.currentTimeMillis();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        break;
//                }
//
//                return true;
//            }
//        });
//
//        spenNoteDoc = new SpenNoteDoc(this, 500, 500);
//        spenPageDoc = spenNoteDoc.appendPage();
//        spenSurfaceView.setPageDoc(spenPageDoc, true);
//    }
//
//    private float calculateSpeed(float x, float y, long lastTime) {
//        double distance = Math.sqrt(Math.pow(lastX - x, 2) + Math.pow(lastY - y, 2));
//        double timeElapsed = (System.currentTimeMillis() - lastTime) / 1000.0; // in seconds
//        return (float) (distance / timeElapsed); // pixels per second
//    }
//
//    private void updateTextView(float x, float y, float pressure, float speed, float tiltX, float tiltY) {
//        String text = String.format("X: %.2f, Y: %.2f\nPressure: %.2f\nSpeed: %.2f pps\nTilt X: %.2f, Tilt Y: %.2f", x, y, pressure, speed, tiltX, tiltY);
//        infoTextView.setText(text);
//    }
//}
