package com.os.music.player.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.os.music.player.R;
import com.os.music.player.fragment.basefragment.BaseFragment;

/**
 * Created by hue on 18/05/2017.
 */

public class HomeAdapter extends FragmentPagerAdapter {
    private Context mContext ;
    private List<BaseFragment> mListFragment = new ArrayList<>();
    public HomeAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.mContext = context;
    }
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tabTitles = new ArrayList<>();
    private ArrayList<Integer> tabIcons = new ArrayList<>();

    public void setListFragment(List<BaseFragment> mListFragment){
        this.mListFragment.clear();
        this.mListFragment.addAll(mListFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
    public void addFragments(Fragment fragment, String tabTitle, Integer tabIcon) {
        this.fragments.add(fragment);
        this.tabTitles.add(tabTitle);
        this.tabIcons.add(tabIcon);
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(mContext).inflate(R.layout.tablayout, null);
        ImageView imgvIcon = (ImageView) tab.findViewById(R.id.imgIconTabs);
        TextView txtvTitle = (TextView) tab.findViewById(R.id.tvNameTabs);
        imgvIcon.setImageResource(tabIcons.get(position));
        txtvTitle.setText(tabTitles.get(position));

        return tab;
    }
}
