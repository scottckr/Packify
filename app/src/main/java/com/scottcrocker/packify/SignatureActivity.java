package com.scottcrocker.packify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.scottcrocker.packify.helper.DrawingView;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SignatureActivity extends AppCompatActivity {

    Button saveButton;
    DrawingView dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        saveButton = (Button) findViewById(R.id.save_signature_button);
        dv = (DrawingView) findViewById(R.id.signature_drawingview);
    }

    public void cancelSignature(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void saveSignature(View view) {
        dv.setDrawingCacheEnabled(true);
        Bitmap signature = dv.getDrawingCache();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.PNG, 50, bs);
        if (signature != null) {
            Toast.makeText(getApplicationContext(), "Signature saved!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "ERROR!!!", Toast.LENGTH_SHORT).show();
        }
        //dv.destroyDrawingCache();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("SIGNATURE", bs.toByteArray());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
