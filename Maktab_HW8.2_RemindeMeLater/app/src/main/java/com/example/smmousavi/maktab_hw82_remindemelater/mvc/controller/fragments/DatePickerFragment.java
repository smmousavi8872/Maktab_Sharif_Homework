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
import android.widget.DatePicker;

import com.example.smmousavi.maktab_hw82_remindemelater.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * A simple {@link Fragment} subclass.
 */


public class DatePickerFragment extends DialogFragment {


  public static final String ARGS_DATE = "args_date";
  public static final String EXTRA_DATE =
    "com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments.extra_date";

  private DatePicker mDatePicker;
  private Date mTaskDate;


  public static DatePickerFragment newInstance(Date taskDate) {

    android.os.Bundle args = new Bundle();
    args.putSerializable(ARGS_DATE, taskDate);

    DatePickerFragment fragment = new DatePickerFragment();
    fragment.setArguments(args);
    return fragment;

  } // end of newInstance()


  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    View view = inflater.inflate(R.layout.fragment_date_picker, null);
    mDatePicker = view.findViewById(R.id.date_picker);

    mTaskDate = (Date) getArguments().getSerializable(ARGS_DATE);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(mTaskDate);

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

    mDatePicker.init(year, month, dayOfMonth, null);

    return new AlertDialog.Builder(getActivity())
      .setView(view)
      .setTitle(R.string.date_picker_title)
      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          int year = mDatePicker.getYear();
          int month = mDatePicker.getMonth();
          int dayOfMonth = mDatePicker.getDayOfMonth();

          Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();

          sendResult(Activity.RESULT_OK, date);
        }
      })
      .setNegativeButton(android.R.string.cancel, null)
      .create();

  }


  private void sendResult(int resultCode, Date date) {
    Fragment target = getTargetFragment();
    if (target == null)
      return;

    Intent intent = new Intent();
    intent.putExtra(EXTRA_DATE, date);
    target.onActivityResult(getTargetRequestCode(), resultCode, intent);
  }


}
