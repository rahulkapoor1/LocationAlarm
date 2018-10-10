package com.rahul.locationalarm.common;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.rahul.locationalarm.R;

public class BaseActivity extends AppCompatActivity {

    protected void startActivity(final Class<?> classToStart, final boolean shouldFinish) {
        final Intent intent = new Intent(this, classToStart);
        startActivity(intent);
        if (shouldFinish) {
            finish();
        }
    }

    protected void makeUINonInteracting() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void makeUIInteracting() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    protected void showAlertWithOkButton(final String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setPositiveButton(getString(R.string.ok), null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setMessage(message);
        alertDialog.show();
    }

}
