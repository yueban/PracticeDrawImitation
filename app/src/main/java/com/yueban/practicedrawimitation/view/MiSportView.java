package com.yueban.practicedrawimitation.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import com.yueban.practicedrawimitation.util.FireworksCircleGraphics;

/**
 * @author yueban
 * @date 2017/11/19
 * @email fbzhh007@gmail.com
 */
public class MiSportView extends View {
  public static final int INTERVAL_MILL = 17;
  private AnimateThread mAnimateThread;
  private FireworksCircleGraphics mFireworksCircleGraphics;

  {
    mAnimateThread = new AnimateThread();
    mFireworksCircleGraphics = new FireworksCircleGraphics(getContext());
    mAnimateThread.start();
  }

  public MiSportView(Context context) {
    super(context);
  }

  public MiSportView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public MiSportView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public MiSportView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
    super.onVisibilityChanged(changedView, visibility);
    switch (visibility) {
      case VISIBLE:
        if (mAnimateThread.isInterrupted()) {
          mAnimateThread.start();
        }
        break;

      case View.GONE:
      case View.INVISIBLE:
      default:
        mAnimateThread.interrupt();
        break;
    }
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    mFireworksCircleGraphics.draw(canvas);
  }

  private class AnimateThread extends Thread {
    @Override
    public void run() {
      super.run();
      while (true) {
        long startTime = System.currentTimeMillis();
        mFireworksCircleGraphics.next();
        long usedTime = System.currentTimeMillis() - startTime;
        try {
          if (usedTime > INTERVAL_MILL) {
            continue;
          }
          Thread.sleep(INTERVAL_MILL - usedTime);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        postInvalidate();
      }
    }
  }
}
