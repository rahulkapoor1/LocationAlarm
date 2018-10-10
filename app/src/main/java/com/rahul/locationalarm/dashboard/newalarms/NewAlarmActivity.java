package com.rahul.locationalarm.dashboard.newalarms;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.rahul.locationalarm.R;
import com.rahul.locationalarm.common.BaseActivity;
import com.rahul.locationalarm.dashboard.alarms.AlarmModel;
import com.rahul.locationalarm.dashboard.alarms.AlarmsPresenter;
import com.rahul.locationalarm.dashboard.newalarms.geofence.GeofenceHelper;
import com.rahul.locationalarm.injectors.DaggerAlarmsComponent;
import com.rahul.locationalarm.utils.PathUtil;
import com.rahul.locationalarm.utils.Utils;

import java.io.File;
import java.net.URISyntaxException;

import javax.inject.Inject;

public class NewAlarmActivity extends BaseActivity implements View.OnClickListener, NewAlarmView {

    private static final String TAG = NewAlarmActivity.class.getSimpleName();

    private final int REQUEST_CODE_PICK_RINGTONE = 1001;

    private final int REQUEST_CODE_PICK_LOCATION = 1002;

    private final int REQUEST_CODE_READ_STORAGE_PERMISSION = 1003;

    private final int REQUEST_CODE_LOCATION_PERMISSION = 1004;

    public static final String KEY_ADDED_ALARM_DETAIL = "key_added_alarm";

    private EditText mEdtAlarmName;

    private CheckBox mCheckboxVibration;

    private ProgressBar mProgressBar;

    private String mRingtonePath;

    private double mLatitude;

    private double mLongitude;

    @Inject
    AlarmsPresenter mPresenter;

    @Inject
    GeofenceHelper mGeofenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        findViews();
        initDependency();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attachView(this);
    }

    private void findViews() {
        mEdtAlarmName = findViewById(R.id.edt_alarm_name);
        findViewById(R.id.btn_ringtone).setOnClickListener(this);
        findViewById(R.id.btn_location).setOnClickListener(this);
        findViewById(R.id.btn_add_alarm).setOnClickListener(this);
        mCheckboxVibration = findViewById(R.id.checkbox_vibration);
        mProgressBar = findViewById(R.id.progress_bar);
    }

    /**
     * To init dependency, as of now this Activity needs Alarms Presenter
     */
    private void initDependency() {
        DaggerAlarmsComponent.builder().build().inject(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_ringtone:
                if (Utils.checkPermissionForReadExternalStorage(this)) {
                    pickRingTone();
                } else {
                    Utils.requestPermissionForReadExtertalStorage(this, REQUEST_CODE_READ_STORAGE_PERMISSION);
                }
                break;

            case R.id.btn_location:
                if (Utils.checkLocationPermission(this)) {
                    pickLocation();
                } else {
                    Utils.requestPermissionForLocation(this, REQUEST_CODE_LOCATION_PERMISSION);
                }

                break;

            case R.id.btn_add_alarm:

                final AlarmModel alarm = new AlarmModel(mEdtAlarmName.getText().toString(), mRingtonePath, mCheckboxVibration.isChecked(), true,
                        mLatitude, mLongitude);
                mPresenter.saveAlarmDetail(alarm);
                break;

        }
    }

    /**
     * To pick audio file path for ringtone
     */
    private void pickRingTone() {
        final Intent intentAudio = new Intent();
        intentAudio.setType("audio/*");
        intentAudio.setAction(Intent.ACTION_GET_CONTENT);
        try {
            startActivityForResult(intentAudio, REQUEST_CODE_PICK_RINGTONE);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * To pick alarm location from Map
     */
    private void pickLocation() {
        try {
            startActivityForResult(new Intent(this, LocationActivity.class), REQUEST_CODE_PICK_LOCATION);
        } catch (ActivityNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CODE_PICK_LOCATION && data != null) {

                final double[] location = data.getDoubleArrayExtra(LocationActivity.KEY_LOCATION);
                mLatitude = location[0];
                mLongitude = location[1];

            } else if (requestCode == REQUEST_CODE_PICK_RINGTONE && data != null) {

                final String realPath;
                try {
                    realPath = PathUtil.getPath(this, data.getData());

                    if (realPath != null) {

                        final Uri uriFromPath = Uri.fromFile(new File(realPath));
                        mRingtonePath = uriFromPath.getPath();

                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_READ_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickRingTone();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(NewAlarmActivity.this);
                    builder.setTitle(getString(R.string.request_permission_title));
                    builder.setMessage(R.string.request_permission_message);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(NewAlarmActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    REQUEST_CODE_READ_STORAGE_PERMISSION);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    // TODO Open Settings from here. For now just show an Alert
                    showAlertWithOkButton(getString(R.string.allow_read_data));
                    Log.d(TAG, "Didn't received read data permission");
                }
            }
        } else if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickLocation();
            } else {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(NewAlarmActivity.this);
                    builder.setTitle(getString(R.string.request_permission_title));
                    builder.setMessage(R.string.request_fine_location_permission_message);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(NewAlarmActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_CODE_LOCATION_PERMISSION);
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    // TODO Open Settings from here. For now just show an Alert
                    showAlertWithOkButton(getString(R.string.allow_fine_location));
                    Log.d(TAG, "Didn't received read data permission");
                }

            }
        }
    }

    @Override
    public void onAlarmDetailSaved(AlarmModel alarm) {
        mGeofenceHelper.registerForGeofence(this, alarm);

        final Intent intent = new Intent();
        intent.putExtra(KEY_ADDED_ALARM_DETAIL, alarm);
        // Navigate to previous screen
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onReceivedErrorInSavingAlarm(int messageResId) {
        showAlertWithOkButton(getString(messageResId));
        hideProgressBar();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        makeUINonInteracting();
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
        makeUIInteracting();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
        mGeofenceHelper.disconnectApiClient();
    }
}
