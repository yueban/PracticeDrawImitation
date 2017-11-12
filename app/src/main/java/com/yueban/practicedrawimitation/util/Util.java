package com.yueban.practicedrawimitation.util;

import android.content.Context;
import android.support.annotation.IntDef;
import android.view.View;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author yueban
 * @date 2017/11/12
 * @email fbzhh007@gmail.com
 */
public class Util {
  private Util() {
    throw new AssertionError("no instance.");
  }

  public static int dp2px(Context context, int dpValue) {
    return (int) context.getResources().getDisplayMetrics().density * dpValue;
  }

  public static int px2dp(Context context, int pxValue) {
    return (int) (pxValue / context.getResources().getDisplayMetrics().density);
  }

  public static int getDefaultSize(int measureSpec, int size) {
    @MeasureSpecMode int specMode = View.MeasureSpec.getMode(measureSpec);
    int specSize = View.MeasureSpec.getSize(measureSpec);
    switch (specMode) {
      case View.MeasureSpec.EXACTLY:
        return Math.max(size, specSize);
      case View.MeasureSpec.AT_MOST:
      case View.MeasureSpec.UNSPECIFIED:
      default:
        return size;
    }
  }

  public static int evaluate(float fraction, int startColor, int endColor) {
    float startA = ((startColor >> 24) & 0xff) / 255.0f;
    float startR = ((startColor >> 16) & 0xff) / 255.0f;
    float startG = ((startColor >> 8) & 0xff) / 255.0f;
    float startB = (startColor & 0xff) / 255.0f;

    float endA = ((endColor >> 24) & 0xff) / 255.0f;
    float endR = ((endColor >> 16) & 0xff) / 255.0f;
    float endG = ((endColor >> 8) & 0xff) / 255.0f;
    float endB = (endColor & 0xff) / 255.0f;

    // convert from sRGB to linear
    startR = (float) Math.pow(startR, 2.2);
    startG = (float) Math.pow(startG, 2.2);
    startB = (float) Math.pow(startB, 2.2);

    endR = (float) Math.pow(endR, 2.2);
    endG = (float) Math.pow(endG, 2.2);
    endB = (float) Math.pow(endB, 2.2);

    // compute the interpolated color in linear space
    float a = startA + fraction * (endA - startA);
    float r = startR + fraction * (endR - startR);
    float g = startG + fraction * (endG - startG);
    float b = startB + fraction * (endB - startB);

    // convert back to sRGB in the [0..255] range
    a = a * 255.0f;
    r = (float) Math.pow(r, 1.0 / 2.2) * 255.0f;
    g = (float) Math.pow(g, 1.0 / 2.2) * 255.0f;
    b = (float) Math.pow(b, 1.0 / 2.2) * 255.0f;

    return Math.round(a) << 24 | Math.round(r) << 16 | Math.round(g) << 8 | Math.round(b);
  }

  @IntDef({ View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.EXACTLY, View.MeasureSpec.AT_MOST })
  @Retention(RetentionPolicy.SOURCE)
  public @interface MeasureSpecMode {}
}
