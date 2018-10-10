package com.rahul.locationalarm.dashboard.alarms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.rahul.locationalarm.R;
import com.rahul.locationalarm.common.BaseActivity;
import com.rahul.locationalarm.dashboard.alarms.update.AlarmUpdateCallback;
import com.rahul.locationalarm.dashboard.newalarms.NewAlarmActivity;
import com.rahul.locationalarm.dashboard.newalarms.geofence.GeofenceHelper;
import com.rahul.locationalarm.injectors.DaggerAlarmsComponent;
import com.rahul.locationalarm.location.LocationHelper;
import com.rahul.locationalarm.location.SavedLocationActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class AlarmsActivity extends BaseActivity implements AlarmView, View.OnClickListener, AlarmUpdateCallback {

    private static final int REQUEST_CODE_NEW_ALARM = 1001;

    private RecyclerView mRecyclerView;

    private FloatingActionButton mFabAddButton;

    private List<AlarmModel> mAlarms;

    private AlarmsAdapter mAlarmsAdapter;

    private ProgressBar mProgressBar;

    @Inject
    AlarmsPresenter mPresenter;

    // To Add or remove geofence
    @Inject
    GeofenceHelper mGeofenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);

        findViews();
        initItemList();
        initDependency();
        getSavedAlarms();

        // To start location tracking
        startLocationTrackService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarms_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.button_location) {
            startActivity(SavedLocationActivity.class, false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViews() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mFabAddButton = findViewById(R.id.fab_add);
        mFabAddButton.setOnClickListener(this);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFabAddButton.getVisibility() == View.VISIBLE) {
                    mFabAddButton.hide();
                } else if (dy < 0 && mFabAddButton.getVisibility() != View.VISIBLE) {
                    mFabAddButton.show();
                }
            }
        });
    }

    /**
     * To init dependency, as of now this Activity needs Alarms Presenter
     */
    private void initDependency() {
        DaggerAlarmsComponent.builder().build().inject(this);
        mPresenter.attachView(this);
    }

    /**
     * To start location tracking
     */
    private void startLocationTrackService() {

        LocationHelper.setAlarmService(this);
    }

    /**
     * To initialize recycler view using adapter and orientation.
     */
    private void initItemList() {
        mAlarms = new ArrayList<>();
        mAlarmsAdapter = new AlarmsAdapter(mAlarms, this);

        mRecyclerView.setAdapter(mAlarmsAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * To get saved alarms
     */
    private void getSavedAlarms() {
        mPresenter.getSavedAlarms();
    }

    @Override
    public void onSavedAlarmsReceived(List<AlarmModel> alarms) {
        mAlarms.clear();
        mAlarms.addAll(alarms);
        mAlarmsAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressBar() {
        makeUINonInteracting();
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        makeUIInteracting();
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_add) {
            final Intent intent = new Intent(this, NewAlarmActivity.class);
            startActivityForResult(intent, REQUEST_CODE_NEW_ALARM);
        }
    }

    @Override
    public void onDeleteAlarm(int positionOfAlarm) {
        // Delete from database
        final AlarmModel alarm = mAlarms.get(positionOfAlarm);
        final int alarmId = alarm.getId();
        mPresenter.deleteAlarmDetail(alarmId);

        mAlarms.remove(positionOfAlarm);
        // To avoid complete list refresh
        mAlarmsAdapter.notifyItemRemoved(positionOfAlarm);

        // remove geofence
        mGeofenceHelper.removeGeofence(this, alarm);
    }

    @Override
    public void onUpdateAlarmState(int positionOfAlarm, boolean isActivated) {
        // update state of alarm
        final AlarmModel alarm = mAlarms.get(positionOfAlarm);
        final int alarmId = alarm.getId();

        mPresenter.updateAlarmState(alarmId, isActivated);

        if (isActivated) {
            // Register geofence
            mGeofenceHelper.registerForGeofence(this, alarm);
        } else {
            // Remove geofence
            mGeofenceHelper.removeGeofence(this, alarm);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_NEW_ALARM && data != null) {

            final Bundle extras = data.getExtras();

            if (extras != null && extras.get(NewAlarmActivity.KEY_ADDED_ALARM_DETAIL) != null) {

                final AlarmModel newAddedAlarm = (AlarmModel) extras.get(NewAlarmActivity.KEY_ADDED_ALARM_DETAIL);

                // Instead of doing database call again use Activity for Result to get newly added alarm
                mAlarms.add(newAddedAlarm);
                // To avoid complete list refresh
                mAlarmsAdapter.notifyItemInserted(mAlarms.size() - 1);
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
        mGeofenceHelper.disconnectApiClient();
    }
}
