package com.yueban.practicedrawimitation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
  private final List<PageModel> mPageModels = new ArrayList<>();
  private ViewPager mViewPager;
  private TabLayout mTabLayout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
    mViewPager = (ViewPager) findViewById(R.id.view_pager);

    mPageModels.add(new PageModel(R.layout.layout_flipboard, getString(R.string.flipboard_view)));
    mPageModels.add(new PageModel(R.layout.layout_jike, getString(R.string.jike_view)));

    mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
      @Override
      public Fragment getItem(int position) {
        return PageFragment.newInstance(mPageModels.get(position));
      }

      @Override
      public int getCount() {
        return mPageModels.size();
      }

      @Override
      public CharSequence getPageTitle(int position) {
        return mPageModels.get(position).title;
      }
    });
    mTabLayout.setupWithViewPager(mViewPager);
    mViewPager.setCurrentItem(1);
  }
}
