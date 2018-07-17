package com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smmousavi.maktab_hw82_remindemelater.R;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.model.Task;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.model.TaskList;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

  public static final String ARGS_TASK_ID = "args_task_id";
  public static final String DIALOG_DATE_TAG = "dialog_date_tag";
  public static final String DIALOG_TIME_TAG = "dialog_time_tag";
  public static final int REQUEST_DATE = 0;
  public static final int REQUEST_TIME = 1;

  private EditText mDecriptionEdt;
  private Button mDateBtn;
  private Button mTimeBtn;
  private CheckBox mSetImportantChk;
  private CheckBox mSetDoneChk;
  private CheckBox mSetAlarmChk;
  private View[] arrViews;
  private Button mEditBtn;
  private Button mDoneBtn;
  private Button mDeleteBtn;
  private UUID taskId;
  private Task mClickedTask;
  private Date mTaskDate;
  private Date mTaskTime;
  private boolean doneStatusChanged;
  private boolean isNewTask;


  public static TaskDetailFragment newInstance(UUID taskId) {

    Bundle args = new Bundle();
    args.putSerializable(ARGS_TASK_ID, taskId);

    TaskDetailFragment fragment = new TaskDetailFragment();
    fragment.setArguments(args);
    return fragment;
  } // end of newInstance()


  public TaskDetailFragment() {
    /* Required empty public constructor */

  }// end of TaskDetailFragment()


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK)
      return;

    if (requestCode == REQUEST_DATE) {
      Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
      updateDate(date);

      mTaskDate = date;

    } else if (requestCode == REQUEST_TIME) {
      Date time = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
      updateTime(time);

      mTaskTime = time;

    }
  }// end of onActivityResult()


  private void updateDate(Date taskDate) {
    String dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(taskDate);
    mDateBtn.setText(dateFormat);
  } // end of updateDate()


  private void updateTime(Date taskTime) {
    String timeFromat = DateFormat.getTimeInstance(DateFormat.SHORT).format(taskTime);
    mTimeBtn.setText(timeFromat);
  }// end of updateTime()


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }// end of onCreate()


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    /* Inflate the layout for this fragment */
    View view = inflater.inflate(R.layout.fragment_task_detail, container, false);
    getViews(view);

    Bundle bundle = getArguments();
    taskId = (UUID) bundle.getSerializable(ARGS_TASK_ID);


    if (taskId == null) {
      mClickedTask = new Task();
      prepareForAdd();
      isNewTask = true;

    } else {
      mClickedTask = TaskList.getInstance(getActivity()).getTask(taskId);
      prepareForUpdate();
      isNewTask = false;

    }

    mDateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Date taskDate = mClickedTask.getDate();
        if (taskDate == null)
          taskDate = new Date();

        FragmentManager fm = getFragmentManager();
        DatePickerFragment dialog = DatePickerFragment.newInstance(taskDate);
        dialog.setTargetFragment(TaskDetailFragment.this, REQUEST_DATE);
        dialog.show(fm, DIALOG_DATE_TAG);

      }
    });
    mTimeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Date taskTime = mClickedTask.getTime();
        if (taskTime == null)
          taskTime = new Date();

        FragmentManager fm = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(taskTime);
        dialog.setTargetFragment(TaskDetailFragment.this, REQUEST_TIME);
        dialog.show(fm, DIALOG_TIME_TAG);
      }
    });

    return view;
  }// end of onCreateView()


  public void getViews(View view) {
    arrViews = new View[]{
      mDecriptionEdt = view.findViewById(R.id.edt_detail_description),
      mDateBtn = view.findViewById(R.id.btn_detail_date),
      mTimeBtn = view.findViewById(R.id.btn_detail_time),
      mSetImportantChk = view.findViewById(R.id.chk_detail_important),
      mSetDoneChk = view.findViewById(R.id.chk_detail_done),
      mSetAlarmChk = view.findViewById(R.id.chk_detail_set_alarm)

    };
    mEditBtn = view.findViewById(R.id.btn_detail_edit);
    mDoneBtn = view.findViewById(R.id.btn_detail_done);
    mDeleteBtn = view.findViewById(R.id.btn_detail_delete);
  }// end of getViews()


  public void prepareForAdd() {
    mEditBtn.setVisibility(View.GONE);
    mDeleteBtn.setVisibility(View.GONE);
    mDoneBtn.setText(R.string.add_task_button_title);

    mDoneBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        String titleFromat = mDecriptionEdt.getText().toString().trim();
        if (!titleFromat.equals("")) {
          registerTaskDetails();

          TaskList.getInstance(getActivity()).addTask(mClickedTask);
          if (!mClickedTask.isDone())
            TaskList.getInstance(getActivity()).addUndoneTask(mClickedTask);

          else
            TaskList.getInstance(getActivity()).addDoneTask(mClickedTask);

          getActivity().finish();
          makeToastAlert(R.string.new_task_added_alert);
        } else
          makeSnackbarAlert(R.string.title_is_not_set_alert);
      }
    });
  }// end of prepareForAdd()


  public void prepareForUpdate() {
    setEnabledViews(false);

    mDoneBtn.setVisibility(View.GONE);
    mDecriptionEdt.setText(mClickedTask.getTitle());
    Date taskDate = mClickedTask.getDate();
    Date taskTime = mClickedTask.getTime();
    if (taskDate != null)
      updateDate(taskDate);

    if (taskTime != null)
      updateTime(taskTime);

    mSetDoneChk.setChecked(mClickedTask.isDone());
    mSetImportantChk.setChecked(mClickedTask.isImportant());
    mSetAlarmChk.setChecked(mClickedTask.isAlarmRequired());

    mEditBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setEnabledViews(true);
        hideAnimation(mEditBtn);
        hideAnimation(mDeleteBtn);
        mDoneBtn.setVisibility(View.VISIBLE);
        mDoneBtn.setText(R.string.update_task_button_title);

        mSetDoneChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked)
              makeSnackbarAlert(R.string.set_done_alert);

            else
              makeSnackbarAlert(R.string.set_undone_alert);
          }
        });

        mSetImportantChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked)
              makeSnackbarAlert(R.string.set_important_alert);
          }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String titleFromat = mDecriptionEdt.getText().toString().trim();
            if (!titleFromat.equals("")) {
              int doneStatusChangeId = registerTaskDetails();

              if (doneStatusChanged) {
                if (doneStatusChangeId == 1) {
                  TaskList.getInstance(getActivity()).addDoneTask(mClickedTask);
                  TaskList.getInstance(getActivity()).removeUndoneTask(mClickedTask);
                  doneStatusChanged = false;

                } else if (doneStatusChangeId == 2) {
                  TaskList.getInstance(getActivity()).addUndoneTask(mClickedTask);
                  TaskList.getInstance(getActivity()).removeDoneTask(mClickedTask);
                  doneStatusChanged = false;
                }
              } else {
                if (mClickedTask.isDone())
                  TaskList.getInstance(getActivity()).updateDoneTask(mClickedTask);

                else
                  TaskList.getInstance(getActivity()).updateUndoneTask(mClickedTask);
              }
              TaskList.getInstance(getActivity()).updateTask(mClickedTask);
              makeToastAlert(R.string.task_update_alert);
              getActivity().finish();

            } else
              makeSnackbarAlert(R.string.title_is_not_set_alert);
          }
        });
      }
    });/* end on mEditBtn.setOnClickListener() */

    mDeleteBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        popAlertDialog();
      }
    });
  }// end of prepareForUpdate()


  private int registerTaskDetails() {
    boolean doneChange = mClickedTask.isDone();
    int doneStatusChangeId = 0;

    mClickedTask.setTitle(mDecriptionEdt.getText().toString());
    mClickedTask.setDate(mTaskDate);
    mClickedTask.setTime(mTaskTime);
    mClickedTask.setImportant(mSetImportantChk.isChecked());
    mClickedTask.setDone(mSetDoneChk.isChecked());
    mClickedTask.setAlarmRequired(mSetAlarmChk.isChecked());

    if (!isNewTask) {
      if (mClickedTask.isDone() && !doneChange) {
        doneStatusChanged = true;
        doneStatusChangeId = 1;

      } else if (!mClickedTask.isDone() && doneChange) {
        doneStatusChanged = true;
        doneStatusChangeId = 2;

      }
    }
    return doneStatusChangeId;
  }// end of onCompletedTaskDetialListener()


  private void hideAnimation(View view) {
    TranslateAnimation animate = new TranslateAnimation(0, 0, 0, view.getWidth());
    animate.setDuration(500);
    animate.setFillAfter(true);
    view.startAnimation(animate);
    view.setVisibility(View.GONE);
  }


  private void popAlertDialog() {
    new AlertDialog.Builder(getContext())
      .setTitle(R.string.delete_task_dialog_title)
      .setNegativeButton(android.R.string.cancel, null)
      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          if (mClickedTask.isDone())
            TaskList.getInstance(getActivity()).removeDoneTask(mClickedTask);

          else
            TaskList.getInstance(getActivity()).removeUndoneTask(mClickedTask);

          TaskList.getInstance(getActivity()).removeTask(mClickedTask);
          makeToastAlert(R.string.task_delete_alert);
          getActivity().finish();
        }
      }).show();
  }// end of showAlertDialog()


  public void setEnabledViews(boolean isEnabled) {
    for (View view : arrViews) {
      if (isEnabled) {
        view.animate().alpha(1.0f);

      } else {
        view.setAlpha(0.5f);
      }
    }
  }// end of setEnabledViews()


  private void makeToastAlert(int alertText) {
    Toast.makeText(getActivity(), alertText, Toast.LENGTH_SHORT).show();
  }


  private void makeSnackbarAlert(int alertText) {
    Snackbar.make(getView(), alertText, Snackbar.LENGTH_SHORT).show();
  }


}