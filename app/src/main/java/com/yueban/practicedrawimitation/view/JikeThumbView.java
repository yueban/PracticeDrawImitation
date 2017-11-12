package com.yueban.practicedrawimitation.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import com.yueban.practicedrawimitation.R;
import com.yueban.practicedrawimitation.util.Util;

/**
 * @author yueban
 * @date 2017/11/12
 * @email fbzhh007@gmail.com
 */
public class JikeThumbView extends View {
  private static final int START_COLOR = Color.parseColor("#00e24d3d");
  private static final int END_COLOR = Color.parseColor("#88e24d3d");
  private static final int SCALE_DURATION = 300;
  private static final int CIRCLE_DURATION = 500;
  private static final float SCALE_MAX = 1f;
  private static final float SCALE_MIN = 0.9f;
  private Paint mBitmapPaint;
  private Paint mCirclePaint;
  private Bitmap mThumbUpBitmap;
  private Bitmap mThumbNormalBitmap;
  private Bitmap mShiningBitmap;
  private int mThumbWidth;
  private int mThumbHeight;
  private int mShiningWidth;
  private int mShiningHeight;
  private Point mShiningStartPoint;
  private Point mThumbStartPoint;
  private Point mCirclePoint;
  private int mRadiusMax;
  private int mRadiusMin;
  private Path mClipPath;
  private boolean mIsThumbUp;
  private float mRadius;
  private float mNotThumbUpScale;
  private AnimatorSet mThumbUpAnim;

  {
    //paint
    mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mCirclePaint.setStyle(Paint.Style.STROKE);
    mCirclePaint.setStrokeWidth(Util.dp2px(getContext(), 2));

    //bitmap
    mThumbUpBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
    mThumbNormalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
    mShiningBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);

    //size
    mThumbWidth = mThumbUpBitmap.getWidth();
    mThumbHeight = mThumbUpBitmap.getHeight();
    mShiningWidth = mShiningBitmap.getWidth();
    mShiningHeight = mShiningBitmap.getHeight();

    //position (point)
    mShiningStartPoint = new Point();
    mThumbStartPoint = new Point();
    mCirclePoint = new Point();
    //this line copied from net.arvin.thumbupsample.changed.ThumbView
    mShiningStartPoint.x = getPaddingLeft() + Util.dp2px(getContext(), 2);
    mShiningStartPoint.y = getPaddingTop();
    mThumbStartPoint.x = getPaddingLeft();
    mThumbStartPoint.y = getPaddingTop() + Util.dp2px(getContext(), 8);
    mCirclePoint.x = mThumbStartPoint.x + mThumbWidth / 2;
    mCirclePoint.y = mThumbStartPoint.y + mThumbHeight / 2;

    //clip path
    mRadiusMax = Math.max(mCirclePoint.x - getPaddingLeft(), mCirclePoint.y - getPaddingTop());
    //this line copied from net.arvin.thumbupsample.changed.ThumbView
    mRadiusMin = Util.dp2px(getContext(), 8);
    mClipPath = new Path();
    mClipPath.addCircle(mCirclePoint.x, mCirclePoint.y, mRadiusMax, Path.Direction.CW);
  }

  public JikeThumbView(Context context) {
    super(context);
  }

  public JikeThumbView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public JikeThumbView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public JikeThumbView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = Util.getDefaultSize(widthMeasureSpec,
        getContentWidth() + getPaddingLeft() + getPaddingRight() + Util.dp2px(getContext(), 2) * 2);
    int height = Util.getDefaultSize(heightMeasureSpec, getContentHeight() + getPaddingTop() + getPaddingBottom());
    setMeasuredDimension(width, height);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    //draw icon
    final Bitmap bitmap;
    if (mIsThumbUp) {
      bitmap = mThumbUpBitmap;
    } else {
      bitmap = mThumbNormalBitmap;
    }
    if (mNotThumbUpScale != SCALE_MAX) {
      canvas.save();
      canvas.scale(mNotThumbUpScale, mNotThumbUpScale, getWidth() / 2, getHeight() / 2);
      canvas.drawBitmap(bitmap, mThumbStartPoint.x, mThumbStartPoint.y, mBitmapPaint);
      canvas.restore();
    } else {
      canvas.drawBitmap(bitmap, mThumbStartPoint.x, mThumbStartPoint.y, mBitmapPaint);
    }

    //draw shining & circle
    if (mIsThumbUp) {
      if (mClipPath != null) {
        canvas.save();
        canvas.clipPath(mClipPath);

        canvas.drawBitmap(mShiningBitmap, mShiningStartPoint.x, mShiningStartPoint.y, mBitmapPaint);
        canvas.restore();
        canvas.drawCircle(mCirclePoint.x, mCirclePoint.y, mRadius, mCirclePaint);
      }
    }
  }

  public void startThumbUpAnim() {
    ObjectAnimator notThumbUpScale = ObjectAnimator.ofFloat(this, "notThumbUpScale", SCALE_MAX, SCALE_MIN);
    notThumbUpScale.setDuration(SCALE_DURATION);
    notThumbUpScale.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        mIsThumbUp = true;
      }
    });

    ObjectAnimator thumbUpScale = ObjectAnimator.ofFloat(this, "notThumbUpScale", SCALE_MIN, SCALE_MAX);
    thumbUpScale.setDuration(SCALE_DURATION);
    thumbUpScale.setInterpolator(new OvershootInterpolator());

    ObjectAnimator circleScale = ObjectAnimator.ofFloat(this, "circleScale", mRadiusMin, mRadiusMax);
    circleScale.setDuration(CIRCLE_DURATION);

    mThumbUpAnim = new AnimatorSet();
    mThumbUpAnim.play(thumbUpScale).with(circleScale);
    mThumbUpAnim.play(thumbUpScale).after(notThumbUpScale);
    mThumbUpAnim.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        super.onAnimationEnd(animation);
        mThumbUpAnim = null;
        // TODO: 2017/11/12 listener callback
      }
    });
    mThumbUpAnim.start();
  }

  public void setThumbUp(boolean thumbUp) {
    mIsThumbUp = thumbUp;
    postInvalidate();
  }

  public void setNotThumbUpScale(float notThumbUpScale) {
    mNotThumbUpScale = notThumbUpScale;
    postInvalidate();
  }

  public void setCircleScale(float circleScale) {
    mRadius = circleScale;
    mClipPath = new Path();
    mClipPath.addCircle(mCirclePoint.x, mCirclePoint.y, mRadius, Path.Direction.CW);
    float fraction = (mRadiusMax - mRadius) / (mRadiusMax - mRadiusMin);
    mCirclePaint.setColor(Util.evaluate(fraction, START_COLOR, END_COLOR));
    postInvalidate();
  }

  private int getContentWidth() {
    float minLeft = Math.min(mShiningStartPoint.x, mThumbStartPoint.x);
    float maxRight = Math.max(mShiningStartPoint.x + mShiningWidth, mThumbStartPoint.x + mThumbWidth);
    return (int) (maxRight - minLeft);
  }

  private int getContentHeight() {
    float minTop = Math.min(mShiningStartPoint.y, mThumbStartPoint.y);
    float maxBottom = Math.max(mShiningStartPoint.y + mShiningHeight, mThumbStartPoint.y + mThumbHeight);
    return (int) (maxBottom - minTop);
  }
}
