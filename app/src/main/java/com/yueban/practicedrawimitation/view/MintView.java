package com.yueban.practicedrawimitation.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.yueban.practicedrawimitation.util.Util;
import java.math.BigDecimal;

/**
 * 主体
 * 1. 单位刻度 width
 * 2. 大刻度 w / h
 * 3. 小刻度 w / h
 *
 * 下方
 * 4. 刻度文字
 *
 * 上方
 * 5. 当前值文字
 * 6. 单位文字
 * 7. 单位文字相对于当前值文字的 xOffset / yOffset
 *
 * 各部分间距
 *
 * @author yueban
 * @date 2017/11/12
 * @email fbzhh007@gmail.com
 */
public class MintView extends View {
  private final Paint mPaint;
  private final Paint mCurrentTextPaint;
  private final Paint mUnitTextPaint;
  private final Paint mCalibrationTextPaint;
  private final RectF mRectF;
  private final BigDecimal UNIT = new BigDecimal(1).divide(BigDecimal.TEN);
  private int mSpace;
  private BigDecimal mSpaceValue;
  private int mIndicatorWidth;
  private int mIndicatorHeight;
  private int mIndicatorColor;
  private int mBigWidth;
  private int mBigHeight;
  private int mSmallWidth;
  private int mSmallHeight;
  private int mColor;
  private int mTextSize;
  private int mTextColor;
  private int mTextPaddingBottom;
  private int mCurrentTextSize;
  private int mCurrentTextColor;
  private int mUnitTextSize;
  private int mUnitTextColor;
  private int mUnitOffsetX;
  private int mUnitOffsetY;
  private int mRulerBackgroundColor;
  private int mRulerPaddingTop;
  private int mRulerHeight;
  private BigDecimal mCurrentValue = new BigDecimal(50);
  private ObjectAnimator mAnimator;

  {
    //paint
    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mCurrentTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mCurrentTextPaint.setTextAlign(Paint.Align.CENTER);
    mUnitTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mUnitTextPaint.setTextAlign(Paint.Align.LEFT);
    mCalibrationTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mCalibrationTextPaint.setTextAlign(Paint.Align.CENTER);
    mRectF = new RectF();

    //size
    mSpace = Util.dp2px(getContext(), 8);
    mSpaceValue = new BigDecimal(mSpace);
    mIndicatorWidth = Util.dp2px(getContext(), 3);
    mIndicatorHeight = Util.dp2px(getContext(), 48);
    mIndicatorColor = Color.parseColor("#4EBB75");
    mBigWidth = Util.dp2px(getContext(), 1.5f);
    mBigHeight = Util.dp2px(getContext(), 40);
    mSmallWidth = Util.dp2px(getContext(), 1f);
    mSmallHeight = Util.dp2px(getContext(), 20);
    mColor = Color.parseColor("#999999");
    mTextSize = Util.sp2px(getContext(), 20);
    mTextColor = Color.BLACK;
    mTextPaddingBottom = Util.dp2px(getContext(), 10);
    mCurrentTextSize = Util.sp2px(getContext(), 36);
    mCurrentTextColor = Color.parseColor("#4EBB75");
    mUnitTextSize = Util.sp2px(getContext(), 14);
    mUnitTextColor = Color.parseColor("#4EBB75");
    mUnitOffsetX = Util.dp2px(getContext(), 8);
    mUnitOffsetY = Util.dp2px(getContext(), -4);
    mRulerBackgroundColor = Color.parseColor("#F5F5F5");
    mRulerPaddingTop = Util.dp2px(getContext(), 20);
    mRulerHeight = Util.dp2px(getContext(), 80);

    mAnimator = ObjectAnimator.ofFloat(this, "currentValue", 50.666f, 60.333f);
    mAnimator.setRepeatCount(ValueAnimator.INFINITE);
    mAnimator.setRepeatMode(ValueAnimator.REVERSE);
    mAnimator.setDuration(10000);
  }

  public MintView(Context context) {
    super(context);
  }

  public MintView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public MintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  public float getCurrentValue() {
    return mCurrentValue.floatValue();
  }

  public void setCurrentValue(float currentValue) {
    BigDecimal newValue = new BigDecimal(currentValue).setScale(2, BigDecimal.ROUND_HALF_UP);
    if (newValue.equals(mCurrentValue)) {
      return;
    }
    mCurrentValue = newValue;
    invalidate();
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    mAnimator.start();
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    mAnimator.end();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = MeasureSpec.getSize(widthMeasureSpec);
    //顶部当前值文字 + 标尺paddingTop + 标尺高度
    int defaultHeight = ((mUnitOffsetY < 0) ? -mUnitOffsetY : 0) + mCurrentTextSize + mRulerPaddingTop + mRulerHeight;
    int height = Util.getDefaultSize(heightMeasureSpec, defaultHeight);

    setMeasuredDimension(width, height);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int width = getWidth();
    int height = getHeight();
    //draw ruler background
    mPaint.setColor(mRulerBackgroundColor);
    int rulerTop = height - mRulerHeight;
    canvas.drawRect(0, rulerTop, width, height, mPaint);

    //draw calibration
    mPaint.setColor(mColor);
    mCalibrationTextPaint.setTextSize(mTextSize);
    mCalibrationTextPaint.setColor(mTextColor);
    //draw ruler top line
    canvas.drawRect(0, rulerTop, width, rulerTop + 1, mPaint);
    //find first
    BigDecimal valueOffset = mCurrentValue.remainder(UNIT);
    BigDecimal xOffset = valueOffset.divide(UNIT).multiply(mSpaceValue);
    int leftCount = (width / 2 - xOffset.intValue()) / mSpace;
    BigDecimal startValue = mCurrentValue.subtract(UNIT.multiply(new BigDecimal(leftCount))).subtract(valueOffset);
    int startX = width / 2 - leftCount * mSpace - xOffset.intValue();

    //draw calibration
    BigDecimal value = startValue.add(BigDecimal.ZERO);
    int drawExceedCount = 1;
    //draw exceed left & right for calibration completely display when it translate into view
    for (int x = startX - drawExceedCount * mSpace * 10; x < width + drawExceedCount * mSpace * 10; x += mSpace) {
      if (value.remainder(UNIT.multiply(BigDecimal.TEN)).doubleValue() == 0) {
        //draw big calibration
        float left = x - mBigWidth / 2;
        mRectF.set(left, rulerTop, left + mBigWidth, rulerTop + mBigHeight);

        //draw calibration number
        //cause draw exceed left for ten UNIT, so textValue should subtract drawExceedCount
        canvas.drawText(String.valueOf(value.intValue() - 1), x, height - mTextPaddingBottom, mCalibrationTextPaint);
      } else {
        //draw small calibration
        float left = x - mSmallWidth / 2;
        mRectF.set(left, rulerTop, left + mSmallWidth, rulerTop + mSmallHeight);
      }
      canvas.drawRect(mRectF, mPaint);
      value = value.add(UNIT);
    }

    //draw indicator
    mPaint.setColor(mIndicatorColor);
    canvas.drawRect(width / 2 - mIndicatorWidth / 2, rulerTop, width / 2 + mIndicatorWidth / 2, rulerTop + mIndicatorHeight,
        mPaint);

    //draw top current text
    mCurrentTextPaint.setColor(mCurrentTextColor);
    mCurrentTextPaint.setTextSize(mCurrentTextSize);
    String currentTextStr = mCurrentValue.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    canvas.drawText(currentTextStr, width / 2, rulerTop - mRulerPaddingTop, mCurrentTextPaint);

    //draw top unit text
    float currentTextWidth = mCurrentTextPaint.measureText(currentTextStr);
    Paint.FontMetrics metrics = mUnitTextPaint.getFontMetrics();
    float unitTextHeight = metrics.bottom - metrics.top;

    mUnitTextPaint.setColor(mUnitTextColor);
    mUnitTextPaint.setTextSize(mUnitTextSize);
    float x = width / 2 + currentTextWidth / 2 + mUnitOffsetX;
    float y = rulerTop - mRulerPaddingTop + mUnitOffsetY - mCurrentTextSize + unitTextHeight;
    canvas.drawText("kg", x, y, mUnitTextPaint);
  }
}
