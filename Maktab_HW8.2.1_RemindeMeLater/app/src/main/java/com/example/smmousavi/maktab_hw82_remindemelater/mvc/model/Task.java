package com.example.smmousavi.maktab_hw82_remindemelater.mvc.model;

import java.util.Date;
import java.util.UUID;

public class Task {

  private UUID mId;
  private String mTitle;
  private Date mDate;
  private Date mTime;
  private boolean mDone;
  private boolean mImportant;


  public Task() {
    mId = UUID.randomUUID();
  }

  public UUID getId() {
    return mId;
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

  @Override
  public boolean equals(Object obj) {
    boolean superResult = super.equals(obj);
    Task taskObj = (Task) obj;
    UUID taskId = this.getId();
    UUID objId = taskObj.getId();

    return (taskId.equals(objId) && superResult);
  }
}
