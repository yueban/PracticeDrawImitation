package com.yueban.practicedrawimitation.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.yueban.practicedrawimitation.R;

/**
 * @author yueban
 * @date 2017/10/29
 * @email fbzhh007@gmail.com
 */
public class FlipboardView extends View {
  private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
  private final Camera mCamera = new Camera();
  private Bitmap mBitmap;
  //Z轴方向（平面内）旋转的角度
  private float degreeZ;
  //Y轴方向旋转角度
  private float degreeY;
  //不变的那一半，Y轴方向旋转角度
  private float fixDegreeY;
  private AnimatorSet mAnimatorSet;
  private ObjectAnimator mAnimator = ObjectAnimator.ofFloat(this, "degreeZ", 0, 360);

  {
    mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);

    //ObjectAnimator animator1 = ObjectAnimator.ofFloat(this, "degreeY", 0, -45);
    //animator1.setDuration(1000);
    //animator1.setStartDelay(500);
    //
    //ObjectAnimator animator2 = ObjectAnimator.ofFloat(this, "degreeZ", 0, 270);
    //animator2.setDuration(800);
    //animator2.setStartDelay(500);
    //
    //ObjectAnimator animator3 = ObjectAnimator.ofFloat(this, "fixDegreeY", 0, 30);
    //animator3.setDuration(500);
    //animator3.setStartDelay(500);
    //
    //mAnimatorSet = new AnimatorSet();
    //mAnimatorSet.addListener(new AnimatorListenerAdapter() {
    //  @Override
    //  public void onAnimationEnd(Animator animation) {
    //    super.onAnimationEnd(animation);
    //    postDelayed(new Runnable() {
    //      @Override
    //      public void run() {
    //        reset();
    //        mAnimatorSet.start();
    //      }
    //    }, 500);
    //  }
    //});
    //mAnimatorSet.playSequentially(animator1, animator2, animator3);

    mAnimator.setDuration(1500);
    mAnimator.setStartDelay(1500);
    mAnimator.setInterpolator(new LinearInterpolator());
    mAnimator.setRepeatCount(ValueAnimator.INFINITE);
    mAnimator.setRepeatMode(ValueAnimator.RESTART);
  }

  public FlipboardView(Context context) {
    super(context);
  }

  public FlipboardView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onAttachedToWindow() {
    super.onAttachedToWindow();
    //mAnimatorSet.start();
    mAnimator.start();
  }

  @Override
  public void dispatchFinishTemporaryDetach() {
    super.dispatchFinishTemporaryDetach();
    //mAnimatorSet.end();
    mAnimator.end();
  }

  public float getDegreeZ() {
    return degreeZ;
  }

  public void setDegreeZ(float degreeZ) {
    this.degreeZ = degreeZ;
    invalidate();
  }

  public void setDegreeY(float degreeY) {
    this.degreeY = degreeY;
    invalidate();
  }

  public void setFixDegreeY(float fixDegreeY) {
    this.fixDegreeY = fixDegreeY;
    invalidate();
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    int bitmapWidth = mBitmap.getWidth();
    int bitmapHeight = mBitmap.getHeight();
    int centerX = getWidth() / 2;
    int centerY = getHeight() / 2;
    int x = centerX - bitmapWidth / 2;
    int y = centerY - bitmapHeight / 2;

    //画变换的一半
    //先旋转，再裁切，再使用camera执行3D动效,**然后保存camera效果**,最后再旋转回来
    canvas.save();
    mCamera.save();
    canvas.translate(centerX, centerY);
    canvas.rotate(-degreeZ);
    //mCamera.rotateY(degreeY);
    mCamera.rotateX(-45);
    mCamera.applyToCanvas(canvas);
    //计算裁切参数时清注意，此时的canvas的坐标系已经移动
    canvas.clipRect(-centerX, -centerY, centerX, 0);
    canvas.rotate(degreeZ);
    canvas.translate(-centerX, -centerY);
    mCamera.restore();
    canvas.drawBitmap(mBitmap, x, y, mPaint);
    canvas.restore();

    //画不变换的另一半
    canvas.save();
    //mCamera.save();
    canvas.translate(centerX, centerY);
    canvas.rotate(-degreeZ);
    //计算裁切参数时清注意，此时的canvas的坐标系已经移动
    canvas.clipRect(-centerX, 0, centerX, centerY);
    //此时的canvas的坐标系已经旋转，所以这里是rotateY
    //mCamera.rotateY(fixDegreeY);
    //mCamera.applyToCanvas(canvas);
    canvas.rotate(degreeZ);
    canvas.translate(-centerX, -centerY);
    //mCamera.restore();
    canvas.drawBitmap(mBitmap, x, y, mPaint);
    canvas.restore();
  }

  public void reset() {
    degreeY = 0;
    fixDegreeY = 0;
    degreeZ = 0;
  }
}
