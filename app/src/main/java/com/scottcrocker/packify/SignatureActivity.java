package com.scottcrocker.packify;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.scottcrocker.packify.helper.DrawingView;

import java.io.ByteArrayOutputStream;

/**
 * Activity for writing a signature to be attached to an order when it is delivered.
 */
public class SignatureActivity extends AppCompatActivity {

    private DrawingView dv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        dv = (DrawingView) findViewById(R.id.signature_drawingview);
    }

    /**
     * Cancels the activity and returns to previous activity.
     *
     * @param view The view component that is executed by click handler.
     */
    public void cancelSignature(View view) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    /**
     * Gets a bitmap from the DrawingView and converts and compresses it to a byte array.
     * Sends the byte array as an extra back to the calling activity.
     * Also sends back RESULT_OK to the calling activity.
     *
     * @param view The view component that is executed by click handler.
     */
    public void saveSignature(View view) {
        dv.setDrawingCacheEnabled(true);
        Bitmap signature = dv.getDrawingCache();
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        signature.compress(Bitmap.CompressFormat.PNG, 50, bs);
        if (bs.toByteArray() != null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_signature_saved), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.toast_signature_wrong), Toast.LENGTH_SHORT).show();
        }
        dv.destroyDrawingCache();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("SIGNATURE", bs.toByteArray());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
