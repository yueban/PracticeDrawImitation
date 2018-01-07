package com.yueban.practicedrawimitation;

/**
 * @author yueban
 * @date 2018/1/7
 * @email fbzhh007@gmail.com
 */
public class SportData {
  public int progress;
  public int step;
  public float distance;
  public int calories;

  public SportData() {
  }

  @Override
  protected SportData clone() throws CloneNotSupportedException {
    SportData sportData = new SportData();
    sportData.progress = progress;
    sportData.step = step;
    sportData.distance = distance;
    sportData.calories = calories;
    return sportData;
  }
}
