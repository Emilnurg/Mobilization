package ru.s4nchez.translater.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.s4nchez.translater.R;

public class FragmentMain extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        initViews(v);
        return v;
    }

    private void initViews(View root) {
        mViewPager = (ViewPager) root.findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) root.findViewById(R.id.tablayout);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private static final String[] TITLES = new String[] { "Переводчик", "Избранное", "История" };


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentTranslate();

                case 1:
                    return new FragmentFavorite();

                case 2:
                    return new FragmentHistory();

                default:
                    return new FragmentTranslate();
            }
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}