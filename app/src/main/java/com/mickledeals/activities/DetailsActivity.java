package com.mickledeals.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;

import com.mickledeals.R;
import com.mickledeals.adapters.VerticalPagerAdapter;
import com.mickledeals.fragments.DetailsFragment;
import com.mickledeals.tests.TestDataHolder;
import com.mickledeals.utils.Utils;
import com.mickledeals.views.NotifyingScrollView;
import com.mickledeals.views.VerticalViewPager;

import java.util.List;

/**
 * Created by Nicky on 12/27/2014.
 */
public class DetailsActivity extends BaseActivity  {

    private VerticalViewPager mViewPager;
    private VerticalPagerAdapter mAdapter;

    private ViewPager mDetailsViewPager;
    private int mListType;
    private int mInitialIndex;
    private List<TestDataHolder> mList;
    private View mShadow;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.getBoolean("isKilled")) return;


//        if (Build.VERSION.SDK_INT >= 21) {
//            TransitionSet set = new TransitionSet();
//            set.addTransition(new ChangeClipBounds());
//            set.addTransition(new ChangeImageTransform());
//            set.addTransition(new ChangeTransform());
//            set.addTransition(new ChangeBounds());
//
//            set.addListener(new Transition.TransitionListener() {
//                @Override
//                public void onTransitionStart(Transition transition) {
//                    Log.e("ZZZ", "onTransitionStart");
//                }
//
//                @Override
//                public void onTransitionEnd(Transition transition) {
//                    Log.e("ZZZ", "onTransitionEnd");
//                }
//
//                @Override
//                public void onTransitionCancel(Transition transition) {
//                    Log.e("ZZZ", "onTransitionCancel");
//                }
//
//                @Override
//                public void onTransitionPause(Transition transition) {
//                    Log.e("ZZZ", "onTransitionPause");
//                }
//
//                @Override
//                public void onTransitionResume(Transition transition) {
//                    Log.e("ZZZ", "onTransitionResume");
//                }
//            });
//            getWindow().setSharedElementEnterTransition(set);
//        }

        if (Build.VERSION.SDK_INT >= 21) {
            postponeEnterTransition();
        }

        mViewPager = (VerticalViewPager) findViewById(R.id.verticalViewPager);
        mAdapter = new VerticalPagerAdapter(this, mViewPager);
        mViewPager.setPageTransformer(true, mAdapter);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(1, false);
        mViewPager.setOffscreenPageLimit(3);

        mInitialIndex = getIntent().getIntExtra("listIndex", 0);
        mListType = getIntent().getIntExtra("listType", 0);
        mList = Utils.getListFromType(mListType);
        mDetailsViewPager = (ViewPager) findViewById(R.id.viewpager);
        mShadow = findViewById(R.id.toolbarShadow);
        DetailsPagerAdapter adapter = new DetailsPagerAdapter(getSupportFragmentManager());
        mDetailsViewPager.setAdapter(adapter);
        mDetailsViewPager.setOnPageChangeListener(adapter);
        mDetailsViewPager.setCurrentItem(mInitialIndex);
        getSupportActionBar().setTitle(mList.get(mInitialIndex).getStoreName());
        setToolBarTransparency(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_search) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_details;
    }




    public class DetailsPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{


        public DetailsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mList.get(position).getStoreName();
        }

        @Override
        public Fragment getItem(int position) {
            DetailsFragment fragment = new DetailsFragment();
            fragment.setOnScrollChangeListener(new NotifyingScrollView.OnScrollChangedListener() {
                @Override
                public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                    setToolBarTransparency(t);
                }
            });
            Bundle bundle = new Bundle();
            bundle.putInt("storeId", mList.get(position).mId);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public void onPageSelected(int position) {
            DetailsFragment fragment = (DetailsFragment) instantiateItem(mDetailsViewPager, position);
            int scrollPos = 0;
            if (fragment != null) {
                scrollPos = fragment.getScrollYPosition();
            }
            setToolBarTransparency(scrollPos);

            getSupportActionBar().setTitle(mList.get(position).getStoreName());
        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }

        @Override
        public void onPageScrolled(int i, float v, int i2) {
        }

    }

    private void setToolBarTransparency(int scrollPos) {
        final int headerHeight = Utils.getDeviceWidth(DetailsActivity.this) * 9 / 16;
        final float ratio = (float) Math.min(Math.max(scrollPos, 0), headerHeight) / headerHeight;
        final int newAlpha = (int) (ratio * 255);
        mToolBar.getBackground().mutate().setAlpha(newAlpha);
        mToolBar.setTitleTextColor(Color.argb(newAlpha, 255, 255, 255));
        mShadow.setAlpha(ratio);
    }

    @Override
    public void onBackPressed() {
        //super method will apply shared element transition
        if (Build.VERSION.SDK_INT < 21 || mDetailsViewPager.getCurrentItem() == mInitialIndex) super.onBackPressed();
        else finish();
    }
}
