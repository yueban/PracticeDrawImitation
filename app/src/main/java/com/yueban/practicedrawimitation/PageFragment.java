package com.yueban.practicedrawimitation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author yueban
 * @date 2017/10/29
 * @email fbzhh007@gmail.com
 */
public class PageFragment extends Fragment {
  private static final String KEY_PAGE_MODEL = "page_model";

  public static PageFragment newInstance(@NonNull PageModel pageModel) {
    PageFragment pageFragment = new PageFragment();
    Bundle bundle = new Bundle();
    bundle.putParcelable(KEY_PAGE_MODEL, pageModel);
    pageFragment.setArguments(bundle);
    return pageFragment;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    PageModel pageModel = getArguments().getParcelable(KEY_PAGE_MODEL);
    assert pageModel != null;
    return inflater.inflate(pageModel.layoutId, container, false);
  }
}
