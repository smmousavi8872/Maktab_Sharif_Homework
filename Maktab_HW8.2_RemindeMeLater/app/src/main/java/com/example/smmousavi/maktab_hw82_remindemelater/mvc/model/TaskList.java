package com.example.smmousavi.maktab_hw82_remindemelater.mvc.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskList {

  private static TaskList mTasks;
  private List<Task> allTasksList;
  private List<Task> doneTaskList;
  private List<Task> undoneTaskList;


  private TaskList() {
    allTasksList = new ArrayList<>();
    doneTaskList = new ArrayList<>();
    undoneTaskList = new ArrayList<>();

  }// end of TasList()


  public static TaskList getInstanc() {
    if (mTasks == null) {
      mTasks = new TaskList();

    }
    return mTasks;
  }// end of getInstance()


  public Task getTask(UUID taskId) {
    for (Task task : allTasksList) {
      if (task.getId().equals(taskId))
        return task;

    }
    return null;
  }// end of getTask()


  public void addTask(Task task) {
    if (!taskExists(task, allTasksList)) {
      Log.i("TaskListTag", "added to all tasks");
      allTasksList.add(task);
    }
  }

  public List<Task> getAllTasks() {
    return allTasksList;
  }

  public void removeTask(Task task) {
    if (taskExists(task, allTasksList)) {
      Log.i("TaskListTag", "removed from all tasks");
      allTasksList.remove(task);
    }

  }


  public void addDoneTask(Task task) {
    if (!taskExists(task, doneTaskList)) {
      Log.i("TaskListTag", "added to done task");
      doneTaskList.add(task);
    }
  }


  public List<Task> getDoneTasks() {
    return doneTaskList;
  }


  public void removeDoneTask(Task task) {
    if (taskExists(task, doneTaskList)) {
      Log.i("TaskListTag", "removed from done task");
      doneTaskList.remove(task);
    }

  }


  public void addUndoneTask(Task task) {
    if (!taskExists(task, undoneTaskList)) {
      Log.i("TaskListTag", "added to undone task");
      undoneTaskList.add(task);
    }
  }


  public List<Task> getUndoneTasks() {
    return undoneTaskList;
  }


  public void removeUndoneTask(Task task) {
    if (taskExists(task, undoneTaskList)) {
      Log.i("TaskListTag", "removed from undone task");
      undoneTaskList.remove(task);
    }

  }

  private boolean taskExists(Task targetTask, List<Task> tasksList) {
    for (Task task : tasksList) {
      if (task.equals(targetTask))
        return true;

    }
    return false;

  }

}// end of TaskList{}