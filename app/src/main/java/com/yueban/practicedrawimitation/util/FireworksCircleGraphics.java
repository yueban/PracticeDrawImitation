package com.yueban.practicedrawimitation.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author yueban
 * @date 2018/1/7
 * @email fbzhh007@gmail.com
 */
public class FireworksCircleGraphics {
  /** 圆环半径与 Canvas 宽度之比 **/
  private static final float RADIUS_SCALE = 0.32f;
  //线条相关常量
  /** 旋转速度 **/
  private static final int ROTATE_RATE = 2;
  /** 线条数量 **/
  private static final int LINE_COUNT = 30;
  /** 线条弧长(degree) **/
  private static final int LINE_ARC_DEGREE = 270;
  /** 线条宽度(dp) **/
  private static final float LINE_WIDTH = 0.5f;
  /** 线条半径偏移最大值(dp) **/
  private static final float LINE_DR_MAX = 4;
  /** 线条圆心偏移最大值(dp) **/
  private static final float LINE_DC_MAX = 4;

  //星星相关常量
  /** 星星数量 **/
  private static final int STAR_COUNT = 60;
  /** 星星大小 **/
  private static final float STAR_SIZE = 8f;
  /** 星星逃离x轴最大速度(dp) **/
  private static final float STAR_ESCAPE_VX_MAX = 2.5f;
  /** 星星逃离y轴最大速度(dp) **/
  private static final float STAR_ESCAPE_VY_MAX = 2.5f;
  /** 星星速度衰减速率(dp) **/
  private static final float STAR_DECAY_RATE = 0.003f;
  /** 星星速度衰减速率，常量(dp) **/
  private static final float STAR_DECAY_RATE_CONST = 0.001f;
  /** 星星消失临界距离(dp) **/
  private static final float STAR_DISAPPEAR_DISTANCE = 60f;
  /** 星星消失临界亮度(dp) **/
  private static final float STAR_DISAPPEAR_ALPHA = 0.05f;
  private float lineDrMax;
  private float lineDcMax;
  private float starEscapeVxMax;
  private float starEscapeVyMax;
  private float starDecayRate;
  private float starDecayRateConst;
  private float starDisappearDistance;
  //draw
  private int endColor;
  private int startColor;
  private Paint linePaint;
  private Paint starPaint;
  private Random random;
  private long rotateDegree = -90;
  //对象复用，减少 GC
  private int width = 0;
  private int height = 0;
  private int circleX = 0;
  private int circleY = 0;
  private RectF lineRectF = new RectF(0, 0, 0, 0);
  private boolean needRefresh = false;
  private LineEntity[] lineEntities;
  private StarEntity[] starEntities;

  public FireworksCircleGraphics(Context context) {
    lineDrMax = Util.dp2px(context, LINE_DR_MAX);
    lineDcMax = Util.dp2px(context, LINE_DC_MAX);
    lineRectF = new RectF(0, 0, 0, 0);

        /* 星星大小 px */
    float starSize = Util.dp2px(context, STAR_SIZE);
    starEscapeVxMax = Util.dp2px(context, STAR_ESCAPE_VX_MAX);
    starEscapeVyMax = Util.dp2px(context, STAR_ESCAPE_VY_MAX);
    starDecayRate = Util.dp2px(context, STAR_DECAY_RATE);
    starDecayRateConst = Util.dp2px(context, STAR_DECAY_RATE_CONST);
    starDisappearDistance = Util.dp2px(context, STAR_DISAPPEAR_DISTANCE);

    endColor = Color.TRANSPARENT;
    startColor = Color.WHITE;

    random = new Random(new SecureRandom().nextInt());

    lineEntities = new LineEntity[LINE_COUNT];
    for (int i = 0; i < lineEntities.length; i++) {
      lineEntities[i] = new LineEntity();
    }
    starEntities = new StarEntity[STAR_COUNT];
    for (int i = 0; i < starEntities.length; i++) {
      starEntities[i] = new StarEntity();
    }

    linePaint = new Paint();
    linePaint.setAntiAlias(true);
    linePaint.setStyle(Paint.Style.STROKE);
    linePaint.setStrokeCap(Paint.Cap.ROUND);
    linePaint.setStrokeWidth(Util.dp2px(context, LINE_WIDTH));

    starPaint = new Paint();
    starPaint.setStrokeWidth(starSize);
    starPaint.setStrokeCap(Paint.Cap.ROUND);
  }

  public void draw(Canvas canvas) {
    canvas.save();

    if (canvas.getHeight() != height || canvas.getWidth() != width) {
      needRefresh = true;
      height = canvas.getHeight();
      width = canvas.getWidth();
    }

    if (needRefresh) {
      circleX = (int) (width * 0.5f);
      circleY = (int) (height * 0.5f);
      SweepGradient lineSweepGradient = new SweepGradient(circleX, circleY, endColor, startColor);
      linePaint.setShader(lineSweepGradient);
      needRefresh = false;

      for (StarEntity starEntity : starEntities) {
        starEntity.reset();
      }
    }

    int radius = (int) (canvas.getWidth() * RADIUS_SCALE);
    canvas.rotate(rotateDegree, circleX, circleY);

    for (LineEntity lineEntity : lineEntities) {
      float dx = lineEntity.dx;
      float dy = lineEntity.dy;
      float dr = lineEntity.dr;
      lineRectF.set(circleX - radius - dr - dx, circleY - radius - dr - dy, circleX + radius + dr + dx,
          circleY + radius + dr + dy);
      // 模拟倾斜效果
      float dAngle = (-dx) < -(360 - LINE_ARC_DEGREE) ? (360 - LINE_ARC_DEGREE) : (-dx);
      canvas.drawArc(lineRectF, 360 - LINE_ARC_DEGREE + dAngle, LINE_ARC_DEGREE, false, linePaint);
    }

    for (StarEntity starEntity : starEntities) {
      float dx = starEntity.dx;
      float dy = starEntity.dy;
      int alphaMask = ((int) (starEntity.alpha * 0xff)) << 24;
      int transparentColor = (startColor & 0x00ffffff) + alphaMask;
      starPaint.setColor(transparentColor);
      canvas.drawPoint(circleX + radius + dx, circleY + dy, starPaint);
    }

    starPaint.setColor(startColor);
    canvas.drawPoint(circleX + radius, circleY, starPaint);

    canvas.restore();
  }

  public void next() {
    rotateDegree = (rotateDegree + ROTATE_RATE) % 360;
    for (LineEntity lineEntity : lineEntities) {
      lineEntity.next();
    }
    for (StarEntity starEntity : starEntities) {
      starEntity.next();
    }
  }

  private float nextRandomFloat() {
    return (random.nextBoolean() ? 1 : -1) * random.nextFloat();
  }

  class LineEntity {
    /** 圆心 x 轴偏移 **/
    float dx;
    /** 圆心 y 轴偏移 **/
    float dy;
    /** 圆半径 r 轴偏移 **/
    float dr;
    /** 圆心 x 轴偏移速度 **/
    float vx;
    /** 圆心 y 轴偏移速度 **/
    float vy;
    /** 圆半径 r 轴偏移速度 **/
    float vr;
    /** 圆心 X 轴偏移加速度 **/
    float ax;
    /** 圆心 Y 轴偏移加速度 **/
    float ay;
    /** 圆半径 r 轴偏移加速度 **/
    float ar;

    LineEntity() {
      dx = nextRandomFloat() * lineDcMax;
      dy = nextRandomFloat() * lineDcMax;
      dr = nextRandomFloat() * lineDrMax;

      vx = 0;
      vy = 0;
      vr = 0;

      ax = 0;
      ay = 0;
      ar = 0;
    }

    /**
     * 计算下一帧（每秒60帧）的各项参数值
     */
    void next() {
      // TODO: 2018/1/7 添加动态偏移逻辑
    }
  }

  class StarEntity {
    /** 距离源点 x 轴偏移 **/
    float dx;
    /** 距离源点 y 轴偏移 **/
    float dy;
    /** 逃离源点 x 轴速度 **/
    float vx;
    /** 逃离源点 y 轴速度 **/
    float vy;
    /** 逃离源点 x 轴加速度 **/
    float ax;
    /** 逃离源点 y 轴加速度 **/
    float ay;
    /** 星星透明度 **/
    float alpha;

    StarEntity() {
      reset();
    }

    void reset() {
      dx = 0;
      dy = 0;

      vx = nextRandomFloat() * starEscapeVxMax;
      // Y 轴因旋转存在初始速度
      vy = nextRandomFloat() * starEscapeVyMax + -2 * (float) Math.PI * width * RADIUS_SCALE * ((float) ROTATE_RATE / 360);

      ax = 0;
      ay = 0;

      alpha = 1;
    }

    void next() {
      ax = -(vx * Math.abs(vx) * starDecayRate - starDecayRateConst);
      ay = -(vy * Math.abs(vy) * starDecayRate - starDecayRateConst);
      if (ax < 0) {
        ax = 0;
      }
      if (ay < 0) {
        ay = 0;
      }

      dx += vx / 2;
      vx += ax;
      dx += vx / 2;

      dy += vy / 2;
      vy += ay;
      dy += vy / 2;

      alpha = 1 - (float) Math.sqrt(dx * dx + dy * dy) / starDisappearDistance;
      if (alpha < STAR_DISAPPEAR_ALPHA) {
        reset();
      }
    }
  }
}
