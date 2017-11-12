package com.yueban.practicedrawimitation.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author yueban
 * @date 2017/10/29
 * @email fbzhh007@gmail.com
 */
public class JikeView extends LinearLayout implements View.OnClickListener {
  private JikeThumbView mThumbView;
  private JikeCountView mCountView;
  private boolean mIsThumbUp;

  public JikeView(Context context) {
    this(context, null);
  }

  public JikeView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public JikeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public JikeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    setOrientation(HORIZONTAL);
    setGravity(Gravity.CENTER);

    //add thumbView
    mThumbView = new JikeThumbView(getContext());
    addView(mThumbView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    //add countView
    mCountView = new JikeCountView(getContext());
    addView(mCountView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    setOnClickListener(this);

    // TODO: 2017/11/12 mock
    mCountView.setCount(98);
  }

  @Override
  public void onClick(View v) {
    mIsThumbUp = !mIsThumbUp;
    if (mIsThumbUp) {
      mCountView.calculateChangeNum(1);
    } else {
      mCountView.calculateChangeNum(-1);
    }
    mThumbView.startThumbUpAnim();
  }
}
