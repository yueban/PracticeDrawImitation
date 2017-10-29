package com.yueban.practicedrawimitation;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.LayoutRes;

/**
 * @author yueban
 * @date 2017/10/29
 * @email fbzhh007@gmail.com
 */
public class PageModel implements Parcelable {
  public static final Parcelable.Creator<PageModel> CREATOR = new Parcelable.Creator<PageModel>() {
    @Override
    public PageModel createFromParcel(Parcel source) {
      return new PageModel(source);
    }

    @Override
    public PageModel[] newArray(int size) {
      return new PageModel[size];
    }
  };
  @LayoutRes public int layoutId;
  public String title;

  public PageModel(int layoutId, String title) {
    this.layoutId = layoutId;
    this.title = title;
  }

  protected PageModel(Parcel in) {
    this.layoutId = in.readInt();
    this.title = in.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.layoutId);
    dest.writeString(this.title);
  }
}