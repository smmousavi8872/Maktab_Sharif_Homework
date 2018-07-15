package com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.smmousavi.maktab_hw82_remindemelater.R;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimePickerFragment extends DialogFragment {

  public static final String ARGS_TIME = "arg_time";
  public static final String EXTRA_TIME =
    "com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments.extra_time";

  private TimePicker mTimePicker;


  public TimePickerFragment() {
     /* Required empty public constructor */

  }// end of TimePickerFragment()


  public static TimePickerFragment newInstance(Date time) {

    Bundle args = new Bundle();
    args.putSerializable(ARGS_TIME, time);

    TimePickerFragment fragment = new TimePickerFragment();
    fragment.setArguments(args);
    return fragment;
  }// end of newInstance()


  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    View view = inflater.inflate(R.layout.fragment_time_picker, null, false);
    mTimePicker = view.findViewById(R.id.time_picker);

    Date time = (Date) getArguments().getSerializable(ARGS_TIME);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(time);

    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    mTimePicker.setCurrentHour(hour);
    mTimePicker.setCurrentMinute(minute);

    return new AlertDialog.Builder(getActivity())
      .setView(view)
      .setTitle(R.string.time_picker_title)
      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          int hour = mTimePicker.getCurrentHour();
          int minute = mTimePicker.getCurrentMinute();
          Calendar calendar = Calendar.getInstance();
          calendar.set(Calendar.HOUR_OF_DAY, hour);
          calendar.set(Calendar.MINUTE, minute);
          Date date = calendar.getTime();

          sendData(Activity.RESULT_OK, date);
        }
      })
      .setNegativeButton(android.R.string.cancel, null)
      .create();

  }

  private void sendData(int resultCode, Date time) {
    Fragment target = getTargetFragment();
    if (target == null)
      return;

    Intent intent = new Intent();
    intent.putExtra(EXTRA_TIME, time);
    target.onActivityResult(getTargetRequestCode(), resultCode, intent);
  }
}
