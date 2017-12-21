package com.os.music.player.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.os.music.player.R;
import com.os.music.player.database.DBManager;
import com.os.music.player.service.MusicService;
import com.os.music.player.shareprefrences.PreferenceUtils;
import com.os.music.player.utils.Constants;

import static com.os.music.player.utils.TranfefUnit.formateMilliSeccond;

public class PlayMusicActivity extends AppCompatActivity {
    private TextView tvNameSongPlay, tvNameAristPlay, tvDuration, tvCurrention, tvTitleNameSong;
    private ImageView tvPrevious, tvNext;
    private ToggleButton toggleRepeat, toggleShuffle, toggleLike;
    private ImageView togglePause;
    private MediaPlayer mediaPlayer;
    private int FRAGMENT_TYPE=0;
    private SeekBar seekBarTime, seekBarVolume = null;
    private String SONG_PATH = "";
    private String SONG_ID = "";
    private int CURRENT_POSITION = 0;
    private int duration = 0;
    private Context context;
    private Handler mHandler;
    private Runnable r;
    private AudioManager audioManager = null;

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getBaseContext();
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_play_music);
        initView();
        initControls();

        receiveFromMusicAdapter();

        initAction();
        receiveFromServiceBroadCast();

        boolean requestStartService = getIntent().getBooleanExtra(Constants.Intents.REQUEST_START_SERVICE, false);
        if (requestStartService) {
            Intent intentService = new Intent(context, MusicService.class);
            stopService(intentService);
            intentService.putExtra(Constants.Intents.CURRENT_POSITION, getIntent().getIntExtra(Constants.Intents.CURRENT_POSITION, 0));
            intentService.putExtra(Constants.Intents.FRAGMENT_TYPE, PreferenceUtils.getIntFromPreference(Constants.Intents.FRAGMENT_TYPE, context));
            intentService.putExtra(Constants.Intents.ID, getIntent().getStringExtra(Constants.Intents.ID));
            Log.d(this.getClass().getSimpleName(), " song_id " + getIntent().getStringExtra(Constants.Intents.ID));
            startService(intentService);
        }
    }


    private void initView() {
        tvCurrention = (TextView) findViewById(R.id.tvCurrentPos);
        tvDuration = (TextView) findViewById(R.id.tvDuration);
        tvNameAristPlay = (TextView) findViewById(R.id.tvNameAristPlay);
        tvNameSongPlay = (TextView) findViewById(R.id.tvNameSongPlay);

        togglePause = (ImageView) findViewById(R.id.tvPause);

        tvPrevious = (ImageView) findViewById(R.id.tvPrevSong);
        tvNext = (ImageView) findViewById(R.id.tvNextSong);
        seekBarTime = (SeekBar) findViewById(R.id.seekBarTime);
        seekBarVolume = (SeekBar) findViewById(R.id.seekBarVolume);
        toggleRepeat = (ToggleButton) findViewById(R.id.toggle_repeat);
        toggleShuffle = (ToggleButton) findViewById(R.id.toggle_shuffer);
        toggleLike = (ToggleButton) findViewById(R.id.toogle_like);
        tvTitleNameSong = (TextView) findViewById(R.id.tvTitleNameSong);

        if (PreferenceUtils.getBooleanFromPreference(Constants.Intents.SHUFFER, getBaseContext())) {
            toggleShuffle.setChecked(true);
        } else {
            toggleShuffle.setChecked(false);
        }
        if (PreferenceUtils.getBooleanFromPreference(Constants.Intents.REPEAT, getBaseContext())) {
            toggleRepeat.setChecked(true);
        } else {
            toggleRepeat.setChecked(false);
        }

    }

    private void receiveFromMusicAdapter() {
        try {
            SONG_ID = getIntent().getStringExtra(Constants.Intents.ID);
            if (!SONG_ID.equals("")) {
                tvNameSongPlay.setText(DBManager.INSTANCE.getSong(SONG_ID).getNameSong());
                tvNameAristPlay.setText(DBManager.INSTANCE.getSong(SONG_ID).getNameArist());
                tvTitleNameSong.setText(DBManager.INSTANCE.getSong(SONG_ID).getNameSong());
                String durations = DBManager.INSTANCE.getSong(SONG_ID).getTime();
                long dura = Long.parseLong(durations + "");
                tvDuration.setText(formateMilliSeccond(dura));
                setToggleState(SONG_ID);
                togglePause.setImageResource(R.drawable.ic_pause);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void setToggleState(String songID) {
        if (DBManager.INSTANCE.checkLiked(songID)) {
            toggleLike.setChecked(true);
        } else {
            toggleLike.setChecked(false);
        }
    }

    private void initControls() {
        try {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            seekBarVolume.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBarVolume.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));

            seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAction() {
        tvTitleNameSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent prevIntent = new Intent(Constants.Intents.PLAY_PREV_SONG);
                prevIntent.putExtra(Constants.Intents.ID, SONG_ID);
                sendBroadcast(prevIntent);
                togglePause.setImageResource(R.drawable.ic_pause);
            }
        });

        togglePause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try { // start service
                    Intent s = new Intent(PlayMusicActivity.this, MusicService.class);
                    s.putExtra(Constants.Intents.ID, SONG_ID);
                    s.putExtra(Constants.Intents.FRAGMENT_TYPE,FRAGMENT_TYPE);
                    s.putExtra(Constants.Intents.CURRENT_POSITION,CURRENT_POSITION);
                    startService(s);
                    togglePause.setImageResource(R.drawable.ic_pause);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendBroadcast(new Intent(Constants.Intents.PAUSE));
            }
        });


        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent leftIntent = new Intent(Constants.Intents.PLAY_NEXT_SONG);
                leftIntent.putExtra(Constants.Intents.ID, SONG_ID);
                sendBroadcast(leftIntent);
                togglePause.setImageResource(R.drawable.ic_pause);
            }
        });

        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Intent intent_pos = new Intent(Constants.Intents.SEEK_POSITION_SONG);
                    intent_pos.putExtra(Constants.Intents.SEEK_POSITION_POS, progress);
                    sendBroadcast(intent_pos);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        toggleLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (toggleLike.isChecked()) {
                    if (!SONG_ID.equals(""))
                        DBManager.INSTANCE.updateLikeMusic(getBaseContext(), SONG_ID);
                } else {
                    if (!SONG_ID.equals(""))
                        DBManager.INSTANCE.updateDislikeMusic(getBaseContext(), SONG_ID);
                }
            }
        });

        toggleRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (toggleRepeat.isChecked()) {
                    PreferenceUtils.save(Constants.Intents.REPEAT, true, getBaseContext());
                } else {
                    PreferenceUtils.save(Constants.Intents.REPEAT, false, getBaseContext());
                }

            }
        });

        toggleShuffle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (toggleShuffle.isChecked()) {
                    PreferenceUtils.save(Constants.Intents.SHUFFER, true, getBaseContext());
                } else {
                    PreferenceUtils.save(Constants.Intents.SHUFFER, false, getBaseContext());
                }
            }
        });
    }

    private void receiveFromServiceBroadCast() {
        mHandler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                sendBroadcast(new Intent(Constants.Intents.REQUEST_PROGRESS));
                mHandler.postDelayed(this, 500);
            }
        };
        runOnUiThread(r);

        IntentFilter filter = new IntentFilter("Broadcast");
        filter.addAction(Constants.Intents.RECEIVE_PROGRESS);
        filter.addAction(Constants.Intents.SET_TOGGLE_OFF);
        filter.addAction(Constants.Intents.SET_TOGGLE_ON);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case Constants.Intents.RECEIVE_PROGRESS:
                        // get data
                        duration = intent.getIntExtra(Constants.Intents.DURATION, 0);
                        CURRENT_POSITION = intent.getIntExtra(Constants.Intents.CURRENT_POSITION, 0);
                        SONG_ID = intent.getStringExtra(Constants.Intents.ID);
                        SONG_PATH = DBManager.INSTANCE.getSong(SONG_ID).getData();
                        FRAGMENT_TYPE = intent.getIntExtra(Constants.Intents.FRAGMENT_TYPE,0);
                        tvTitleNameSong.setText(DBManager.INSTANCE.getSong(SONG_ID).getNameSong());
                        tvNameSongPlay.setText(DBManager.INSTANCE.getSong(SONG_ID).getNameSong());
                        tvNameAristPlay.setText(DBManager.INSTANCE.getSong(SONG_ID).getNameArist());
                        String current = formateMilliSeccond(CURRENT_POSITION);
                        tvCurrention.setText(current);
                        tvDuration.setText(formateMilliSeccond(duration));
                        seekBarTime.setMax(duration);
                        seekBarTime.setProgress(CURRENT_POSITION);

//                        String artPath = DBManager.INSTANCE.getArtAlbumPath(SONG_ID);
//                        Glide.with(PlayMusicActivity.this).load(new File(artPath)).asBitmap().into(imAlbumArt);
                        PreferenceUtils.save(Constants.Intents.ID, SONG_ID, getBaseContext());
                        PreferenceUtils.save(Constants.Intents.CURRENT_POSITION, CURRENT_POSITION, getBaseContext());
                        setToggleState(SONG_ID);
                        break;

                    case Constants.Intents.SET_TOGGLE_ON:
                        togglePause.setImageResource(R.drawable.ic_pause);
                        break;

                    case Constants.Intents.SET_TOGGLE_OFF:
                        togglePause.setImageResource(R.drawable.ic_play);
                        break;

                    default:
                        break;
                }
            }
        };
        registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public void onDestroy() {
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
