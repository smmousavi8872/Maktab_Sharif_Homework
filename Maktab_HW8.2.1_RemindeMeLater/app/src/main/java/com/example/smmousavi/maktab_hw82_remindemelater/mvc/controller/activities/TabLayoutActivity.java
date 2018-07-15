package com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.smmousavi.maktab_hw82_remindemelater.R;
import com.example.smmousavi.maktab_hw82_remindemelater.mvc.controller.fragments.TasksListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabLayoutActivity extends AppCompatActivity {

  public static final String SAVE_INSTANCE_FRAGEMENTS = "save_instance_fragment";
  public static final String SAVE_INSTANCE_TITELS = "save_instance_title";

  private TabLayout tabLayout;
  private ViewPager viewPager;
  private List<Fragment> tabFragments;
  private List<String> tabTitles;
  ViewPagerAdapter adapter;


  @Override
  protected void onResume() {
    super.onResume();
  } // end of onResume()


  @Override
  public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
    super.onSaveInstanceState(outState, outPersistentState);
    outState.putSerializable(SAVE_INSTANCE_FRAGEMENTS, (Serializable) tabFragments);
    outState.putSerializable(SAVE_INSTANCE_TITELS, (Serializable) tabTitles);
  } // end of onSaveInstanceState()


  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tab_layout);

    viewPager = (ViewPager) findViewById(R.id.tab_layout_viewpager);
    tabLayout = (TabLayout) findViewById(R.id.tab_layout);
    tabFragments = new ArrayList<>();
    tabTitles = new ArrayList<>();

    addFragment(TasksListFragment.newInstance(0), "All");
    addFragment(TasksListFragment.newInstance(1), "Undone");
    addFragment(TasksListFragment.newInstance(2), "Done");

    adapter = new ViewPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(adapter);

    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override
      public void onPageSelected(int position) {
        adapter.getItem(position).onResume();

      }

      @Override
      public void onPageScrollStateChanged(int state) {

      }
    });

    tabLayout.setupWithViewPager(viewPager);

  } //end of onCreate()


  private class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      return tabFragments.get(position);
    }

    @Override
    public int getCount() {
      return tabFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return tabTitles.get(position);
    }

  }// end of ViewPagerAdapter{}


  public void addFragment(Fragment fragment, String tabTitle) {
    tabFragments.add(fragment);
    tabTitles.add(tabTitle);

  }// end of addFragment()


}// end of TabLayoutActivity{}
