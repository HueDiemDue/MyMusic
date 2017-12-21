package com.os.music.player.fragment.basefragment;

import android.support.v4.app.Fragment;

import com.os.music.player.activity.MainActivity;

/**
 * Created by hue on 18/05/2017.
 */

public class BaseFragment extends Fragment {
    private MainActivity getMainActivity(){
        return (MainActivity) getMainActivity();
    }
}
