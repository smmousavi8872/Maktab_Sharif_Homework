package com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.smmousavi.maktab_hw82_remindemelater.R;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.activities.TaskDetailActivity;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.model.Task;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.model.TaskList;

import java.text.DateFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TasksListFragment extends Fragment {

  public static final String ARGS_TASK_LIST_ID = "args_task_list_id";

  private RecyclerView mRecyclerView;
  private List<Task> mTasksList;
  private TaskAdapter mTaskAdapter;
  private FloatingActionButton mFab;
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
    updateListUI(); /* XXX crashes in landscape XXX : mRecyclerView is null */
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);


  }// end of onCreate()


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    /* Inflate the layout for this fragment */
    View view = inflater.inflate(R.layout.fragment_tasks_list, container, false);

    mRecyclerView = view.findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    mFab = view.findViewById(R.id.fab_all_tasks);

    updateListUI();

    if (mTaskListId == 1 || mTaskListId == 2) {
      mFab.setVisibility(View.GONE);
    }

    mFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = TaskDetailActivity.newIntent(getActivity(), null);
        startActivity(intent);
      }
    });


    return view;

  }// end of onCreateView


  public void updateListUI() {
    Bundle bundle = getArguments();
    mTaskListId = bundle.getInt(ARGS_TASK_LIST_ID);
    switch (mTaskListId) {
      case 0:
        mTasksList = TaskList.getInstanc().getAllTasks();
        break;
      case 1:
        mTasksList = TaskList.getInstanc().getUndoneTasks();
        break;
      case 2:
        mTasksList = TaskList.getInstanc().getDoneTasks();
        break;
    }
    mTaskAdapter = new TaskAdapter(mTasksList);
    if (mRecyclerView != null) /* to avoid crashing after screen rotation*/
      mRecyclerView.setAdapter(mTaskAdapter);

  }// end of updateListUI()


  private class TaskViewHolder extends RecyclerView.ViewHolder {

    private TextView mTaskTitleTxt;
    private TextView mTaskDateAndTimeTxt;
    private Button mTaskIsDoneBtn;
    private Task mClikedTask;

    public TaskViewHolder(final View itemView) {
      super(itemView);
      mTaskTitleTxt = itemView.findViewById(R.id.txt_list_item_title);
      mTaskDateAndTimeTxt = itemView.findViewById(R.id.txt_list_item_subtitle);
      mTaskIsDoneBtn = itemView.findViewById(R.id.btn_list_item_done);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = TaskDetailActivity.newIntent(getActivity(), mClikedTask.getId());
          startActivity(intent);
        }
      });

      mTaskIsDoneBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          mClikedTask.setDone(true);
          TaskList.getInstanc().addDoneTask(mClikedTask);
          TaskList.getInstanc().removeUndoneTask(mClikedTask);
          Snackbar.make(getView(), String.format("'%s' is set done", mClikedTask.getTitle()), Snackbar.LENGTH_SHORT).show();
          updateListUI();

        }
      });

    }/* end of TaskViewHolder() */

    public void bindeTaskViewHolder(Task task) {
      mClikedTask = task;
      mTaskTitleTxt.setText(task.getTitle());
      String dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM).format(task.getDate());
      String timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(task.getTime());
      mTaskDateAndTimeTxt.setText(dateFormat + ", " + timeFormat);
      if (task.isDone())
        mTaskIsDoneBtn.setVisibility(View.GONE);

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
      View view;
      if (viewType == 0)
        view = inflater.inflate(R.layout.fragment_item_list, parent, false);

      else
        view = inflater.inflate(R.layout.fragment_item_list_important, parent, false);

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
      if (mTasksList.get(position).isImportant())
        return 1;

      return 0;
    } /* end of getItemViewType */

  }// end of TaskAdapter{}


}
