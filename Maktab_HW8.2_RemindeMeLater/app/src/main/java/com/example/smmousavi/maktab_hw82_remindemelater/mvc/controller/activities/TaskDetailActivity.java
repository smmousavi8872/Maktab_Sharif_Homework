package com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments.TaskDetailFragment;

import java.util.UUID;

public class TaskDetailActivity extends SingleFragmentActivity {

  public static final String EXTRA_TASK_ID =
    "com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.activities.extra_task_id";


  public static Intent newIntent(Context orgin, UUID taskId) {
    Intent intent = new Intent(orgin, TaskDetailActivity.class);
    intent.putExtra(EXTRA_TASK_ID, taskId);
    return intent;
  }

  @Override
  protected Fragment createFragment() {
    UUID taskId = (UUID) getIntent().getExtras().getSerializable(EXTRA_TASK_ID);
    return TaskDetailFragment.newInstance(taskId);
  }


}
