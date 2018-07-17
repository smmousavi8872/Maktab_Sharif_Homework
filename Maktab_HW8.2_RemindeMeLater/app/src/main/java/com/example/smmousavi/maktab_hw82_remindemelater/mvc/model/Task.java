package com.example.smmousavi.maktab_hw82_remindemelater.mvc.model;

import java.util.Date;
import java.util.UUID;

public class Task {

  private UUID mTaskId;
  private UUID mUserId;
  private String mTitle;
  private Date mDate;
  private Date mTime;
  private boolean mDone;
  private boolean mImportant;
  private boolean mAlarmRequired;


  public Task() {
    this(UUID.randomUUID());
  }


  public Task(UUID id) {
    mTaskId = id;
  }


  public UUID getTaskId() {
    return mTaskId;
  }


  public UUID getUserId() {
    return mUserId;
  }


  public String getTitle() {
    return mTitle;
  }


  public void setTitle(String mTitle) {
    this.mTitle = mTitle;
  }


  public Date getDate() {
    return mDate;
  }


  public void setDate(Date mDate) {
    this.mDate = mDate;
  }


  public Date getTime() {
    return mTime;
  }


  public void setTime(Date mTime) {
    this.mTime = mTime;
  }


  public boolean isDone() {
    return mDone;
  }


  public void setDone(boolean mDone) {
    this.mDone = mDone;
  }


  public boolean isImportant() {
    return mImportant;
  }


  public void setImportant(boolean mImportant) {
    this.mImportant = mImportant;
  }


  public boolean isAlarmRequired() {
    return mAlarmRequired;
  }


  public void setAlarmRequired(boolean mAlarmRequired) {
    this.mAlarmRequired = mAlarmRequired;
  }


  @Override
  public boolean equals(Object obj) {
    boolean superResult = super.equals(obj);
    Task taskObj = (Task) obj;
    UUID taskId = this.getTaskId();
    UUID objId = taskObj.getTaskId();

    return (taskId.equals(objId) && superResult);
  }

  @Override
  public String toString() {
    return this.getTitle();

  }
}