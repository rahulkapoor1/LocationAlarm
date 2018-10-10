package com.rahul.locationalarm.dashboard.alarms;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rahul.locationalarm.R;
import com.rahul.locationalarm.dashboard.alarms.update.AlarmUpdateCallback;
import com.rahul.locationalarm.utils.Utils;

import java.util.List;

public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.AlarmsViewHolder> {

    private final List<AlarmModel> mAlarms;

    private final AlarmUpdateCallback mAlarmUpdateCallback;

    public AlarmsAdapter(@NonNull final List<AlarmModel> alarms, @NonNull final AlarmUpdateCallback callback) {
        this.mAlarms = alarms;
        this.mAlarmUpdateCallback = callback;
    }

    @NonNull
    @Override
    public AlarmsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.items_alarm, viewGroup, false);

        return new AlarmsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmsViewHolder alarmsViewHolder, int i) {

        final AlarmModel detail = mAlarms.get(i);
        alarmsViewHolder.mTvAlarmName.setText(detail.getName());
        alarmsViewHolder.mSwitchActive.setChecked(detail.isActive());
        alarmsViewHolder.mSwitchActive.setOnCheckedChangeListener(getCheckChangeListener(alarmsViewHolder));
        alarmsViewHolder.mTvAlarmRingtone.setText(Utils.getNameOfRingtone(detail.getRingtone()));
        alarmsViewHolder.mIvDelete.setOnClickListener(getDeleteButtonClickListener(alarmsViewHolder));
    }

    private View.OnClickListener getDeleteButtonClickListener(@NonNull final AlarmsViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlarmUpdateCallback.onDeleteAlarm(holder.getAdapterPosition());
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener getCheckChangeListener(@NonNull final AlarmsViewHolder holder) {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mAlarmUpdateCallback.onUpdateAlarmState(holder.getAdapterPosition(), b);
            }
        };
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public class AlarmsViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvAlarmName;

        private TextView mTvAlarmRingtone;

        private SwitchCompat mSwitchActive;

        private ImageView mIvDelete;

        AlarmsViewHolder(View view) {
            super(view);
            mTvAlarmName = view.findViewById(R.id.tv_alarm_name);
            mSwitchActive = view.findViewById(R.id.switch_active);
            mTvAlarmRingtone = view.findViewById(R.id.tv_alarm_ringtone);
            mIvDelete = view.findViewById(R.id.iv_delete);
        }
    }


}
