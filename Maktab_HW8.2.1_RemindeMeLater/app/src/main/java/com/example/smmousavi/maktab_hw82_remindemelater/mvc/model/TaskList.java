package com.example.smmousavi.maktab_hw82_remindemelater.mvc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskList {

  private static TaskList mTasks;
  private List<Task> allTaskList;
  private List<Task> doneTaskList;
  private List<Task> undoneTaskList;


  private TaskList() {
    allTaskList = new ArrayList<>();
    doneTaskList = new ArrayList<>();
    undoneTaskList = new ArrayList<>();
  }// end of TasList()


  public static TaskList getInstance() {
    if (mTasks == null)
      mTasks = new TaskList();

    return mTasks;
  }// end of getInstance()


  public Task getTask(UUID taskId) {
    for (Task task : allTaskList) {
      if (task.getId().equals(taskId))
        return task;

    }
    return null;
  }// end of getTask()


  public void addTask(Task task) {
    if (!taskExists(task, allTaskList))
      allTaskList.add(task);
  }// end of addTask()


  public List<Task> getAllTasks() {
    return allTaskList;
  }// end of getAllTasks()


  public void removeTask(Task task) {
    if (taskExists(task, allTaskList))
      allTaskList.remove(task);
  }// end of removeTasks()


  public void addDoneTask(Task task) {
    if (!taskExists(task, doneTaskList))
      doneTaskList.add(task);
  }// end of addDoneTasks()


  public List<Task> getDoneTasks() {
    return doneTaskList;
  }// end of getDoneTasks()


  public void removeDoneTask(Task task) {
    if (taskExists(task, doneTaskList))
      doneTaskList.remove(task);
  } // end of removeDoneTasks()


  public void addUndoneTask(Task task) {
    if (!taskExists(task, undoneTaskList))
      undoneTaskList.add(task);
  }// end of addUndoneTasks()


  public List<Task> getUndoneTasks() {
    return undoneTaskList;
  }// end of getUndoneTasks()


  public void removeUndoneTask(Task task) {
    if (taskExists(task, undoneTaskList))
      undoneTaskList.remove(task);
  }// end of removeUndoneTasks()


  private boolean taskExists(Task targetTask, List<Task> tasksList) {
    for (Task task : tasksList) {
      if (task.equals(targetTask))
        return true;

    }
    return false;
  }// end of taskExists()


  public void clearAllTasks() {
    for (int i = allTaskList.size() - 1; i >= 0; i--)
      allTaskList.remove(i);

    for (int i = doneTaskList.size() - 1; i >= 0; i--)
      doneTaskList.remove(i);

    for (int i = undoneTaskList.size() - 1; i >= 0; i--)
      undoneTaskList.remove(i);
  }// end of clearAllTasks()


  public void clearDoneTasks() {
    for (int i = doneTaskList.size() - 1; i >= 0; i--)
      doneTaskList.remove(i);

    for (int i = allTaskList.size() - 1; i >= 0; i--) {
      if (allTaskList.get(i).isDone())
        allTaskList.remove(i);
    }
  }// end of clearDoneTasks()


  public void clearUndoneTasks() {
    for (int i = undoneTaskList.size() - 1; i >= 0; i--)
      undoneTaskList.remove(i);

    for (int i = allTaskList.size() - 1; i >= 0; i--) {
      if (!allTaskList.get(i).isDone())
        allTaskList.remove(i);
    }
  }// end of clearUndoneTasks()


}