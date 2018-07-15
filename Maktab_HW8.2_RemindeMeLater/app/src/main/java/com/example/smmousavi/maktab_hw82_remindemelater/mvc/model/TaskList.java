package com.example.smmousavi.maktab_hw82_remindemelater.mvc.model;

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


  public static TaskList getInstance() {
    if (mTasks == null)
      mTasks = new TaskList();

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
    if (!taskExists(task, allTasksList))
      allTasksList.add(task);
  }


  public List<Task> getAllTasks() {
    return allTasksList;
  }


  public void removeTask(Task task) {
    if (taskExists(task, allTasksList))
      allTasksList.remove(task);
  }


  public void addDoneTask(Task task) {
    if (!taskExists(task, doneTaskList))
      doneTaskList.add(task);
  }


  public List<Task> getDoneTasks() {
    return doneTaskList;
  }


  public void removeDoneTask(Task task) {
    if (taskExists(task, doneTaskList))
      doneTaskList.remove(task);
  }


  public void addUndoneTask(Task task) {
    if (!taskExists(task, undoneTaskList))
      undoneTaskList.add(task);
  }


  public List<Task> getUndoneTasks() {
    return undoneTaskList;
  }


  public void removeUndoneTask(Task task) {
    if (taskExists(task, undoneTaskList))
      undoneTaskList.remove(task);
  }


  private boolean taskExists(Task targetTask, List<Task> tasksList) {
    for (Task task : tasksList) {
      if (task.equals(targetTask))
        return true;

    }
    return false;
  }


}