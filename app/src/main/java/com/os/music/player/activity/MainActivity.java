package com.os.music.player.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.mz.A;
import com.mz.ZAndroidSystemDK;
import com.os.music.player.R;
import com.os.music.player.adapter.HomeAdapter;
import com.os.music.player.fragment.AlbumFragment;
import com.os.music.player.fragment.AllSongFragment;
import com.os.music.player.fragment.AristFragment;
import com.os.music.player.fragment.DetailAlbumsFragment;
import com.os.music.player.fragment.DetailAristFragment;
import com.os.music.player.fragment.LikeSongFragment;
import com.os.music.player.shareprefrences.PreferenceUtils;
import com.os.music.player.task.GetAllMusic;
import com.os.music.player.utils.Constants;

import static com.os.music.player.R.id.albums;
import static com.os.music.player.R.id.tabLayout;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private BroadcastReceiver mBroadCastReceive;
    private int[] tabIcon = {
            R.drawable.tab_icon_song_selector,
            R.drawable.tab_album_selector,
            R.drawable.tab_play_selector,
            R.drawable.tab_arist_selector,
            R.drawable.tab_like_selector
    };
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private HomeAdapter mHomeAdapter;
    private TextView tvTitle, tvPlaying, tvReloadData;
    private TabLayout.Tab defaultTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setViewPaper();
        setTabsIcon();
        initAction();
        receiveBroadCast();

        ZAndroidSystemDK.init(this);
        A.f(this);
    }
    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewPaper);
        mTabLayout = (TabLayout) findViewById(tabLayout);
        tvTitle = (TextView) findViewById(R.id.tvTitleTab);
        tvPlaying = (TextView) findViewById(R.id.tvPlaying);
        tvReloadData = (TextView) findViewById(R.id.tvReloadData);
        tvReloadData.setText(getString(R.string.reload_data));
        mViewPager.setCurrentItem(0, true);
        tvTitle.setText(getString(R.string.songs));

    }
    private void initAction() {
        tvPlaying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PlayMusicActivity.class);
                String name_id = PreferenceUtils.getStringFromPreference(Constants.Intents.ID,getBaseContext());
                int current_position = PreferenceUtils.getIntFromPreference(Constants.Intents.CURRENT_POSITION,getBaseContext());
                intent.putExtra(Constants.Intents.ID,name_id);
                intent.putExtra(Constants.Intents.CURRENT_POSITION,current_position);
                startActivity(intent);
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvTitle.setText(getString(R.string.songs));
                        tvReloadData.setVisibility(View.VISIBLE);
                        tvReloadData.setText(getString(R.string.reload_data));
                        tvReloadData.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new GetAllMusic(getBaseContext()).execute();

                            }
                        });
                        break;
                    case 1:
                        tvTitle.setText(getString(R.string.albums));
                        tvReloadData.setVisibility(View.INVISIBLE);
                        tvTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                        break;
                    case 2:
                        tvTitle.setText(getString(R.string.artist));
                        tvReloadData.setVisibility(View.INVISIBLE);
                        tvTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
                        break;
                    case 3:
                        tvTitle.setText(getString(R.string.favorite));
                        tvReloadData.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setViewPaper() {
        mHomeAdapter = new HomeAdapter(getSupportFragmentManager(), this);
        mHomeAdapter.addFragments(new AllSongFragment(),getString(R.string.songs), tabIcon[0]);
        mHomeAdapter.addFragments(new AlbumFragment(), getString(R.string.albums), tabIcon[1]);
        mHomeAdapter.addFragments(new AristFragment(),getString(R.string.artist), tabIcon[3]);
        mHomeAdapter.addFragments(new LikeSongFragment(), getString(R.string.favorite), tabIcon[4]);
        mViewPager.setAdapter(mHomeAdapter);
        mViewPager.setOffscreenPageLimit(5);
    }

    private void setTabsIcon() {
        mTabLayout.setTabMode(TabLayout.GRAVITY_CENTER);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            mTabLayout.getTabAt(i).setCustomView(mHomeAdapter.getTabView(i));
        }
        defaultTab = mTabLayout.getTabAt(0);
        defaultTab.select();
    }

    private void receiveBroadCast() {
        IntentFilter intentFilter = new IntentFilter(Constants.Intents.SEND_FROM_ALBUMS_ADAPTER_TO_MAIN);
        intentFilter.addAction(Constants.Intents.SEND_FROM_ARIST_ADAPTER_TO_MAIN);
        mBroadCastReceive = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Constants.Intents.SEND_FROM_ALBUMS_ADAPTER_TO_MAIN:
                        mViewPager.setCurrentItem(1, true);
                        tvTitle.setText("BACK");
                        Fragment fmReplace = new DetailAlbumsFragment(intent.getStringExtra(Constants.Intents.NAME_ALBUMS));
                        getSupportFragmentManager().beginTransaction().replace(albums, fmReplace).addToBackStack(null).commit();

                        break;
                    case Constants.Intents.SEND_FROM_ARIST_ADAPTER_TO_MAIN:
                        mViewPager.setCurrentItem(3, true);
                        tvTitle.setText("BACK");
                        Fragment fmReplaceArist = new DetailAristFragment(intent.getStringExtra(Constants.Intents.NAME_ARIST));
                        getSupportFragmentManager().beginTransaction().replace(R.id.arist, fmReplaceArist).addToBackStack(null).commit();
                        break;
                    default:
                        break;
                }
            }
        };
        registerReceiver(mBroadCastReceive, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (mBroadCastReceive != null) {
            getBaseContext().unregisterReceiver(mBroadCastReceive);
        }
        super.onDestroy();
    }
}
