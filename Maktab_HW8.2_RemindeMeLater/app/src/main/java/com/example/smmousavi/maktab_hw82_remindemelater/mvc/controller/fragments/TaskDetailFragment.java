package com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smmousavi.maktab_hw82_remindemelater.R;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.model.Task;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.model.TaskList;

import java.text.DateFormat;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskDetailFragment extends Fragment {

  public static final String ARGS_TASK_ID = "args_task_id";

  private EditText mDecriptionEdt;
  private Button mDateBtn;
  private Button mTimeBtn;
  private CheckBox mSetImportantChk;
  private CheckBox mSetDoneChk;
  private View[] mutableViews;
  private Button mEditBtn;
  private Button mDoneBtn;
  private Button mDeleteBtn;
  private UUID taskId;
  private Task mClickedTask;


  public static TaskDetailFragment newInstance(UUID taskId) {

    Bundle args = new Bundle();
    args.putSerializable(ARGS_TASK_ID, taskId);

    TaskDetailFragment fragment = new TaskDetailFragment();
    fragment.setArguments(args);
    return fragment;
  }


  public TaskDetailFragment() {
    /* Required empty public constructor */
  }


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


  }// end of onCreate()


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    /* Inflate the layout for this fragment */
    View view = inflater.inflate(R.layout.fragment_task_detail, container, false);

    mutableViews = new View[]{
      mDecriptionEdt = view.findViewById(R.id.edt_detail_description),
      mDateBtn = view.findViewById(R.id.btn_detail_date),
      mTimeBtn = view.findViewById(R.id.btn_detail_time),
      mSetImportantChk = view.findViewById(R.id.chk_detail_important),
      mSetDoneChk = view.findViewById(R.id.chk_detail_done),

    };
    mEditBtn = view.findViewById(R.id.btn_detail_edit);
    mDoneBtn = view.findViewById(R.id.btn_detail_done);
    mDeleteBtn = view.findViewById(R.id.btn_detail_delete);

    Bundle bundle = getArguments();
    taskId = (UUID) bundle.getSerializable(ARGS_TASK_ID);
    if (taskId == null) {
      mClickedTask = new Task();
      prepareForAdd();

    } else {
      mClickedTask = TaskList.getInstanc().getTask(taskId);
      prepareForUpdate();

    }
    return view;

  }// end of onCreateView()


  public void prepareForAdd() {
    mEditBtn.setVisibility(View.GONE);
    mDeleteBtn.setVisibility(View.GONE);
    mDoneBtn.setText(R.string.add_task_button_title);


    mDoneBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onCompleteTaskDetail();
        if (!mClickedTask.getTitle().equals("")) {
          TaskList.getInstanc().addTask(mClickedTask);
          if (!mClickedTask.isDone())
            TaskList.getInstanc().addUndoneTask(mClickedTask);

          else
            TaskList.getInstanc().addDoneTask(mClickedTask);

          getActivity().finish();
          Toast.makeText(getActivity(), R.string.new_task_added_toast_alert, Toast.LENGTH_SHORT).show();

        } else {
          Snackbar.make(getView(), R.string.title_is_not_set_snackbar_alert, Snackbar.LENGTH_SHORT).show();

        }
      }
    });
  }// end of prepareForAdd()


  public void prepareForUpdate() {
    setEnabledViews(false);

    mDoneBtn.setVisibility(View.GONE);
    mDecriptionEdt.setText(mClickedTask.getTitle());
    String dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(mClickedTask.getDate());
    String timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(mClickedTask.getTime());
    mDateBtn.setText(dateFormat);
    mTimeBtn.setText(timeFormat);
    mSetDoneChk.setChecked(mClickedTask.isDone());
    mSetImportantChk.setChecked(mClickedTask.isImportant());

    mEditBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        setEnabledViews(true);
        mEditBtn.setVisibility(View.GONE);
        mDeleteBtn.setVisibility(View.GONE);
        mDoneBtn.setVisibility(View.VISIBLE);
        mDoneBtn.setText(R.string.update_task_button_title);
        mSetDoneChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked)
              Snackbar.make(getView(), "Task is set done", Snackbar.LENGTH_SHORT).show();

            else
              Snackbar.make(getView(), "Task is set undone", Snackbar.LENGTH_SHORT).show();

          }
        });

        mSetImportantChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked)
              Snackbar.make(getView(), "Task is set as Important", Snackbar.LENGTH_SHORT).show();

          }
        });

        mDoneBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            onCompleteTaskDetail();
            if (!mClickedTask.getTitle().equals(""))
              getActivity().finish();

            else
              Snackbar.make(getView(), R.string.title_is_not_set_snackbar_alert, Snackbar.LENGTH_SHORT).show();
          }
        });
      }
    });// end on mEditBtn.setOnClickListener()

    mDeleteBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        popAlertDialog();
      }
    });

  }// end of prepareForUpdate()


  private void popAlertDialog() {
    new AlertDialog.Builder(getContext())
      .setTitle(R.string.delete_task_alert_dialog_title)
      .setNegativeButton(android.R.string.cancel, null)
      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
          if (mClickedTask.isDone())
            TaskList.getInstanc().removeDoneTask(mClickedTask);

          else
            TaskList.getInstanc().removeUndoneTask(mClickedTask);

          TaskList.getInstanc().removeTask(mClickedTask);
          Toast.makeText(getActivity(), R.string.task_remove_toast_alert, Toast.LENGTH_SHORT).show();
          getActivity().finish();
        }
      }).show();

  }// end of popAlertDialog()


  private void onCompleteTaskDetail() {

    mClickedTask.setTitle(mDecriptionEdt.getText().toString());

    mDateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        /* ??? should get completed_date picker dialog??? */
      }
    });

    mTimeBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        /* ??? should get completed_time picker dialog??? */
      }
    });

    if (mSetDoneChk.isChecked()) {
      mClickedTask.setDone(true);
      TaskList.getInstanc().addDoneTask(mClickedTask);
      TaskList.getInstanc().removeUndoneTask(mClickedTask);

    } else {
      mClickedTask.setDone(false);
      TaskList.getInstanc().removeDoneTask(mClickedTask);
      TaskList.getInstanc().addUndoneTask(mClickedTask);

    }

    mClickedTask.setImportant(mSetImportantChk.isChecked());

  }// end of onCompletedTaskDetialListener()

  public void setEnabledViews(boolean isEnabled) {
    for (View view : mutableViews)
      view.setEnabled(isEnabled);

  }// end of setEnabledViews()


}
