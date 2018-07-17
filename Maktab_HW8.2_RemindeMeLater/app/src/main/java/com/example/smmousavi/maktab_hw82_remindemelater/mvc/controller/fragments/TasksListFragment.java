package com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smmousavi.maktab_hw82_remindemelater.R;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.activities.TaskDetailActivity;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.activities.TaskDetailPagerActivity;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.model.Task;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.model.TaskList;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import static com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.activities.TabLayoutActivity.mSubtitleVisiable;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {

  public static final String ARGS_TASK_LIST_ID = "args_task_list_id";

  private RecyclerView mRecyclerView;
  private List<Task> mTasksList;
  private TaskAdapter mTaskAdapter;
  private FloatingActionButton mFab;
  private ImageView mEmptyListImg;
  private TextView mEmptyListTxt;
  private int mTaskListId;

  public TasksListFragment() {
    /* Required empty public constructor */

  } // end of TaskListFragment()


  public static TasksListFragment newInstance(int taskListId) {
    Bundle args = new Bundle();
    args.putInt(ARGS_TASK_LIST_ID, taskListId);

    TasksListFragment fragment = new TasksListFragment();
    fragment.setArguments(args);
    return fragment;

  }// end of newInstance()


  @Override
  public void onResume() {
    super.onResume();
    updateListUI();

  }// end of onResume()


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);


  }// end of onCreate()


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    /* Inflate the layout for this fragment */
    View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);
    getViews(view);

    updateListUI();

    if (mTaskListId == 1 || mTaskListId == 2) {
      mFab.setVisibility(View.GONE);
    }

    mFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        addNewTask();

      }
    });
    return view;

  }// end of onCreateView


  private void addNewTask() {
    Intent intent = TaskDetailActivity.newIntent(getActivity(), null);
    startActivity(intent);
  }// end of addNewTask()


  public void getViews(View view) {
    mEmptyListImg = view.findViewById(R.id.empty_list);
    mEmptyListTxt = view.findViewById(R.id.empty_list_text);
    mRecyclerView = view.findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mFab = view.findViewById(R.id.fab_all_tasks);
  }// end of getViews()


  public void updateListUI() {
    Bundle bundle = getArguments();
    mTaskListId = bundle.getInt(ARGS_TASK_LIST_ID);
    switch (mTaskListId) {
      case 0:
        mTasksList = TaskList.getInstance(getActivity()).getAllTasks();
        break;
      case 1:
        mTasksList = TaskList.getInstance(getActivity()).getUndoneTasks();
        break;
      case 2:
        mTasksList = TaskList.getInstance(getActivity()).getDoneTasks();
        break;
    }
    if (mTasksList.size() > 0) {
      mEmptyListImg.setVisibility(View.GONE);
      mEmptyListTxt.setVisibility(View.GONE);


    } else {
      mEmptyListImg.setVisibility(View.VISIBLE);
      mEmptyListTxt.setVisibility(View.VISIBLE);
    }

    mTaskAdapter = new TaskAdapter(mTasksList);
    if (mRecyclerView != null)/* XXX crashes in rotation : mRecyclerView is null XXX*/
      mRecyclerView.setAdapter(mTaskAdapter);

  }// end of updateListUI()


  private class TaskViewHolder extends RecyclerView.ViewHolder {

    private TextView mTaskTitleTxt;
    private TextView mTaskDateAndTimeTxt;
    private Button mTaskIsDoneBtn;
    private TextView mInitailCirlceTxt;
    private Task mClikedTask;

    public TaskViewHolder(final View itemView) {
      super(itemView);
      getViews(itemView);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Task task = TaskList.getInstance(getActivity()).getTask(mClikedTask.getTaskId());
          Log.i("TAG1", task.toString() + " : " + mClikedTask.toString());
          Intent intent = TaskDetailPagerActivity.newIntent(getActivity(), mClikedTask.getTaskId(), mTaskListId);
          startActivity(intent);
        }
      });

      mTaskIsDoneBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          mClikedTask.setDone(true);
          TaskList.getInstance(getActivity()).updateTask(mClikedTask);
          TaskList.getInstance(getActivity()).addDoneTask(mClikedTask);
          TaskList.getInstance(getActivity()).removeUndoneTask(mClikedTask);
          makeSnackbarAlert(String.format("'%s' is set done", mClikedTask.getTitle()));
          if (mTaskListId == 0) {
            hideView(view);

          } else if (mTaskListId == 1)
            updateListUI();
        }
      });
    }/* end of TaskViewHolder() */


    private void getViews(View itemView) {
      mTaskTitleTxt = itemView.findViewById(R.id.txt_list_item_title);
      mTaskDateAndTimeTxt = itemView.findViewById(R.id.txt_list_item_subtitle);
      mTaskIsDoneBtn = itemView.findViewById(R.id.btn_list_item_done);
      mInitailCirlceTxt = itemView.findViewById(R.id.txt_initial_cirlce);
    }/* end of getViews() */


    private void hideView(View view) {
      TranslateAnimation animate = new TranslateAnimation(0, view.getWidth(), 0, 0);
      animate.setDuration(500);
      animate.setFillAfter(true);
      view.startAnimation(animate);
      view.setVisibility(View.INVISIBLE);
    }// endOfHideView()


    public void bindeTaskViewHolder(Task task) {
      mClikedTask = task;
      mTaskTitleTxt.setText(mClikedTask.getTitle());
      mInitailCirlceTxt.setText(mClikedTask.getTitle().trim().toUpperCase().charAt(0) + "");

      Date taskDate = task.getDate();
      Date taskTime = task.getTime();
      if (taskDate != null & taskTime != null) {
        String dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(taskDate);
        String timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(taskTime);
        mTaskDateAndTimeTxt.setText(dateFormat + ", " + timeFormat);

      } else if (taskDate != null && taskTime == null) {
        String dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(taskDate);
        mTaskDateAndTimeTxt.setText(dateFormat);

      } else if (taskDate == null && taskTime != null) {
        String timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(taskTime);
        mTaskDateAndTimeTxt.setText(timeFormat);

      } else
        mTaskDateAndTimeTxt.setText(R.string.task_item_subtitle_date_time_not_set);

      if (task.isDone())
        mTaskIsDoneBtn.setVisibility(View.INVISIBLE);

    }/* end of bindeTaskViewHolder() */

  }// end of TaskViewHolder{}


  private class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    private List<Task> mTasksList;

    public TaskAdapter(List<Task> tasksList) {
      mTasksList = tasksList;

    } /* end of TaskAdapter{} */


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      View view = inflater.inflate(R.layout.fragment_item_list, parent, false);
      ImageView bellImg = view.findViewById(R.id.list_item_bell);
      ImageView starImg = view.findViewById(R.id.list_item_star);

      if (viewType == 0) {
        bellImg.setVisibility(View.GONE);

      } else if (viewType == 1) {
        starImg.setVisibility(View.GONE);

      } else if (viewType == 2) {
        bellImg.setVisibility(View.GONE);
        starImg.setVisibility(View.GONE);

      }
      return new TaskViewHolder(view);
    } /* end of onCreateViewHolder() */


    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
      holder.bindeTaskViewHolder(mTasksList.get(position));

    } /* end of onBindViewHolder() */

    @Override
    public int getItemCount() {
      return mTasksList.size();
    } /* end of getItemCount() */


    @Override
    public int getItemViewType(int position) {
      if (mTasksList.get(position).isImportant() && !mTasksList.get(position).isAlarmRequired())
        return 0;

      else if (!mTasksList.get(position).isImportant() && mTasksList.get(position).isAlarmRequired())
        return 1;

      else if (!mTasksList.get(position).isAlarmRequired() && !mTasksList.get(position).isImportant())
        return 2;

      else
        return 3;

    } /* end of getItemViewType */

  }// end of TaskAdapter{}


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragement_task_list, menu);
    MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_login);

    if (mSubtitleVisiable)
      subtitleItem.setTitle(R.string.menu_item_hide_login_info);

    else
      subtitleItem.setTitle(R.string.menu_item_show_login_info);

    updateSubtitle();
  }// end of onCreateOptionMenu()


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_new_task:
        addNewTask();
        return true;
      case R.id.menu_item_show_login:
        mSubtitleVisiable = !mSubtitleVisiable;
        getActivity().invalidateOptionsMenu(); /* telling the menu to be created again */
        updateSubtitle();
        return true;
      case R.id.menu_item_clear_list:
        clearTasksList();
        return true;
      case R.id.menu_item_exit:
        showAlertDialog(R.string.exit_confirm_alert, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            getActivity().finish();

          }
        }, null);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }// end of onOptionsItemSelected()


  public void updateSubtitle() {

    /* ??? should get completed: "Mohsen" must be change by the user name ??? */
    String subtitle = getString(R.string.toolbar_subtitle_login_info, "Mohsen");
    if (!mSubtitleVisiable)
      subtitle = null;

    AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
    appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    appCompatActivity.getSupportActionBar().setIcon(R.drawable.ic_man_with_tie_white);

  }// end of updateSubtitle()


  private void clearTasksList() {
    switch (mTaskListId) {
      case 0:
        if (TaskList.getInstance(getActivity()).getAllTasks().size() == 0)
          makeSnackbarAlert("All Tasks List Is Empty!");

        else {
          showAlertDialog(R.string.all_tasks_delete_confirm_alert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              TaskList.getInstance(getActivity()).clearAllTasks();
              makeToastAlert(getString(R.string.all_task_removed_alert));
              updateListUI();

            }
          }, null);
        }
        break;
      case 1:
        if (TaskList.getInstance(getActivity()).getUndoneTasks().size() == 0)
          makeSnackbarAlert("Undone Tasks List Is Empty!");

        else {
          showAlertDialog(R.string.undone_tasks_delete_confirm_alert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              TaskList.getInstance(getActivity()).clearUndoneTasks();
              makeToastAlert(getString(R.string.undone_tasks_removed_alert));
              updateListUI();

            }
          }, null);
        }
        break;
      case 2:
        if (TaskList.getInstance(getActivity()).getDoneTasks().size() == 0)
          makeSnackbarAlert("Done Tasks List Is Empty!");

        else {
          showAlertDialog(R.string.done_tasks_delete_confirm_alert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
              TaskList.getInstance(getActivity()).clearDoneTasks();
              makeToastAlert(getString(R.string.done_tasks_removed_alert));
              updateListUI();

            }
          }, null);
        }
        break;
    }
  }// end of clearTaskList()


  public void showAlertDialog(int titleResId, DialogInterface.OnClickListener okAction,
                              DialogInterface.OnClickListener cancelAction) {
    new AlertDialog.Builder(getActivity())
      .setTitle(titleResId)
      .setPositiveButton(android.R.string.ok, okAction)
      .setNegativeButton(android.R.string.cancel, cancelAction)
      .show();
  } // end of showAlertDialog()


  private void makeToastAlert(String alertText) {
    Toast.makeText(getActivity(), alertText, Toast.LENGTH_SHORT).show();
  }// end of makeToaskAlert()


  private void makeSnackbarAlert(String alertText) {
    Snackbar.make(getView(), alertText, Snackbar.LENGTH_SHORT).show();
  }// end of makeSnackBarAlert


}