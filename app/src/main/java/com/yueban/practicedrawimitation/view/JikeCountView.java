package com.yueban.practicedrawimitation.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.yueban.practicedrawimitation.util.Util;

/**
 * @author yueban
 * @date 2017/11/12
 * @email fbzhh007@gmail.com
 */
public class JikeCountView extends View {
  private final float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15f, getResources().getDisplayMetrics());
  private final int mTextColor = Color.parseColor("#cccccc");
  private String[] mTexts;
  private Point[] mTextPoints;
  private float mMinOffsetY;
  private float mMaxOffsetY;
  private int mEndTextColor;
  private Paint mTextPaint;
  private int mCount;
  private float mOldOffsetY;
  private float mNewOffsetY;
  private float mFraction;
  private boolean mCountToBigger;

  {
    mTexts = new String[3];
    mTextPoints = new Point[3];
    mTextPoints[0] = new Point();
    mTextPoints[1] = new Point();
    mTextPoints[2] = new Point();
    calculateChangeNum(0);

    mMinOffsetY = 0;
    mMaxOffsetY = mTextSize;
    mEndTextColor = mTextColor & 0x00ffffff;
    mTextPaint = new Paint();
    mTextPaint.setTextSize(mTextSize);
    mTextPaint.setColor(mTextColor);
  }

  public JikeCountView(Context context) {
    super(context);
  }

  public JikeCountView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public JikeCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public JikeCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = Util.getDefaultSize(widthMeasureSpec, getContentWidth() + getPaddingLeft() + getPaddingRight());
    int height = Util.getDefaultSize(heightMeasureSpec, getContentHeight() + getPaddingTop() + getPaddingBottom());

    setMeasuredDimension(width, height);
  }

  private int getContentWidth() {
    return (int) Math.ceil(mTextPaint.measureText(String.valueOf(mCount)));
  }

  private int getContentHeight() {
    return (int) mTextSize;
  }

  @Override
  protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    calculateLocation();
  }

  public int getCount() {
    return mCount;
  }

  public void setCount(int count) {
    mCount = count;
    calculateChangeNum(0);
    requestLayout();
  }

  public void startAnim(boolean isToBigger) {
    mCountToBigger = isToBigger;
    ObjectAnimator textOffsetY =
        ObjectAnimator.ofFloat(this, "textOffsetY", mMinOffsetY, mCountToBigger ? mMaxOffsetY : -mMaxOffsetY);
    textOffsetY.setDuration(250);
    textOffsetY.start();
  }

  public void setTextOffsetY(float offsetY) {
    mOldOffsetY = offsetY;
    if (mCountToBigger) {
      mNewOffsetY = offsetY - mMaxOffsetY;
    } else {
      mNewOffsetY = mMaxOffsetY + offsetY;
    }
    mFraction = (mMaxOffsetY - Math.abs(mOldOffsetY)) / (mMaxOffsetY - mMinOffsetY);
    calculateLocation();
    postInvalidate();
  }

  public void calculateChangeNum(int change) {
    if (change == 0) {
      mTexts[0] = String.valueOf(mCount);
      mTexts[1] = "";
      mTexts[2] = "";
      return;
    }

    String oldNum = String.valueOf(mCount);
    String newNum = String.valueOf(mCount + change);
    for (int i = 0; i < oldNum.length(); i++) {
      char oldChar = oldNum.charAt(i);
      char newChar = newNum.charAt(i);
      if (oldChar != newChar) {
        mTexts[0] = i == 0 ? "" : newNum.substring(0, i);
        mTexts[1] = oldNum.substring(i);
        mTexts[2] = newNum.substring(i);
        break;
      }
    }
    mCount += change;
    startAnim(change > 0);
  }

  private void calculateLocation() {
    String text = String.valueOf(mCount);
    float charWidth = mTextPaint.measureText(text) / text.length();
    float unChangeWidth = charWidth * mTexts[0].length();
    Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
    float y = getPaddingTop() + (getContentHeight() - fontMetricsInt.bottom - fontMetricsInt.top) / 2;

    mTextPoints[0].x = getPaddingLeft();
    mTextPoints[1].x = (int) (getPaddingLeft() + unChangeWidth);
    mTextPoints[2].x = (int) (getPaddingLeft() + unChangeWidth);

    mTextPoints[0].y = (int) y;
    mTextPoints[1].y = (int) (y - mOldOffsetY);
    mTextPoints[2].y = (int) (y - mNewOffsetY);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //不变的部分
    mTextPaint.setColor(mTextColor);
    canvas.drawText(String.valueOf(mTexts[0]), mTextPoints[0].x, mTextPoints[0].y, mTextPaint);

    //变化前的部分
    mTextPaint.setColor(Util.evaluate(mFraction, mEndTextColor, mTextColor));
    canvas.drawText(String.valueOf(mTexts[1]), mTextPoints[1].x, mTextPoints[1].y, mTextPaint);

    //变化后的部分
    mTextPaint.setColor(Util.evaluate(mFraction, mTextColor, mEndTextColor));
    canvas.drawText(String.valueOf(mTexts[2]), mTextPoints[2].x, mTextPoints[2].y, mTextPaint);
  }
}
