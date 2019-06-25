package yc.com.homework.read.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.kk.utils.security.Md5;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yc.com.base.ObservableManager;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.PathUtils;
import yc.com.homework.read.listener.OnAVManagerListener;

/**
 * Created by wanglin  on 2018/11/3 09:26.
 * 发音练习管理类
 */
public class PpAudioManager implements OnAVManagerListener {

    private Context mContext;


    private MediaPlayer mMediaPlayer;//播放MP3

    private Handler mHandler;

    private ExecutorService service;//创建下载服务
    private OnUIChangeListener onUIChangeListener;

    private final String DIRECTORY_NAME = "book_read";


    private float mLastEndTime;//音频数据中最大时间
    private float maxDuration;//音频通过播放器获取最大时间

    private float mStartTime;//音频开始时间
    private float mEndTime;//音频结束时间

    public PpAudioManager(Context context, final OnUIChangeListener uiChangeListener) {
        this.mContext = context.getApplicationContext();
        this.onUIChangeListener = uiChangeListener;
        service = Executors.newSingleThreadExecutor();

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int duration = (int) msg.obj;

                if (mEndTime != 0) {
                    if (duration >= mEndTime) {
                        stopMusic();
                        LogUtil.msg("mendTime  " + mEndTime + "  mLastTime  " + mLastEndTime + "  duration  " + duration + "  maxDuration " + maxDuration);
                        if (mLastEndTime != 0 && duration >= mLastEndTime) {
                            if (duration + 25000 <= maxDuration) {
                                ObservableManager.get().notifyMyObserver("next");
                            } else {
                                playComplete();
                            }
                        }
                    }
                    if (mEndTime == mLastEndTime)
                        onUIChangeListener.onPlayChangeListener(duration);
                }
//                LogUtil.msg("duration: " + duration + "    mEndTime: " + mEndTime);

            }
        };
        AVMediaManager.getInstance().addAudioManager(this);

    }


    /**
     * @param musicUrl
     * @param startTime 开始时间
     * @param endTime   截止时间
     */
    @Override
    public void playMusic(String musicUrl, float startTime, float endTime, float lastEndtime) {
        this.mStartTime = startTime;
        this.mEndTime = endTime;
        this.mLastEndTime = lastEndtime;

        AVMediaManager.getInstance().releaseAudioManager();


        if (TextUtils.isEmpty(musicUrl)) return;
        if (null == mMediaPlayer) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        mMediaPlayer.setOnPreparedListener(newPreparedListener);
        mMediaPlayer.setOnCompletionListener(newCompletionListener);

        mMediaPlayer.setOnErrorListener(newErrorListener);

        try {
            String name = getFileName(musicUrl);
            File file = new File(PathUtils.makeDir(mContext, DIRECTORY_NAME), name);
            if (file.exists()) {//设置播放file文件
                LogUtils.e("from file");
                try {
//                    LogUtil.msg("path: " + file.getAbsolutePath());
//                    mMediaPlayer.setDataSource(musicUrl);
                    mMediaPlayer.setDataSource(file.getAbsolutePath());
                    mMediaPlayer.prepareAsync();
                } catch (Exception e) {
//                    mediaPlayer.setDataSource(path);
                }
            } else {
                LogUtils.e("from path");
                service.submit(new DownloadTask(musicUrl));
                onUIChangeListener.show();
            }


        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());

        }

    }


    @Override
    public void playMusic(String musicUrl) {
        playMusic(musicUrl, 0, 0, 0);
    }


    @Override
    public void stopMusic() {
        //停止ItemView缩放动画播放
//        controllerListener.playPracticeAfterUpdateUI();
        //停止音乐播放
        if (null != mMediaPlayer) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
//            mMediaPlayer.release();
            mMediaPlayer.reset();
            mMediaPlayer = null;
        }
    }


    @Override
    public void pauseMusic() {
        if (null != mMediaPlayer && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        }
    }

    @Override
    public void playMusic() {
        if (null != mMediaPlayer) {
            mMediaPlayer.start();
            mHandler.postDelayed(myRunnable, 1000);
        }
    }

    private Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {

                    int duration = mMediaPlayer.getCurrentPosition();

                    Message message = mHandler.obtainMessage();
                    message.obj = duration;

                    mHandler.sendMessage(message);

                    mHandler.postDelayed(this, 100);
                }
            } catch (Exception e) {
                LogUtils.e(e.getMessage());
            }
        }
    };


    private MediaPlayer.OnPreparedListener newPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

            preparePlay(mp);
        }
    };

    private void preparePlay(MediaPlayer mp) {
        mp.seekTo((int) mStartTime);
        mp.start();
        maxDuration = mp.getDuration();
        LogUtil.msg("mendTime  " + mEndTime + "  mLastTime  " + mLastEndTime + "  duration  " + maxDuration);

        mHandler.postDelayed(myRunnable, 1000);
    }


    private MediaPlayer.OnCompletionListener newCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            playComplete();
        }
    };

    private void playComplete() {
        onUIChangeListener.onComplete();
        ObservableManager.get().notifyMyObserver("play_stop");
    }

    private MediaPlayer.OnErrorListener newErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            ToastUtil.toast2(mContext, "播放失败！");
            LogUtil.msg("error->" + what + "  extra->" + extra);

            return false;
        }
    };


    @Override
    public void startRecordAndSynthesis(String word, boolean isWord) {

    }

    @Override
    public void stopRecord() {

    }

    @Override
    public boolean isRecording() {
        return false;
    }


    @Override
    public void playRecordFile() {
        try {

        } catch (Exception e) {
            LogUtils.e("prepare() failed");
        }
    }


    @Override
    public void playAssetFile(String assetFilePath, final int step) {
        stopMusic();

        if (TextUtils.isEmpty(assetFilePath)) return;

        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        }
        AssetManager assetManager = mContext.getAssets();
        AssetFileDescriptor afd = null;
        try {
            afd = assetManager.openFd(assetFilePath);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(newPreparedListener);
            mMediaPlayer.setOnCompletionListener(newCompletionListener);

        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.toast2(mContext, "播放文件有误！");
        }
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer != null && mMediaPlayer.isPlaying();
    }


    private class DownloadTask implements Runnable {
        private String mPath;

        private DownloadTask(String path) {
            this.mPath = path;
        }

        @Override
        public void run() {
            download(mPath);
        }
    }


    private void download(String path) {
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String name = getFileName(path);
            File file = new File(PathUtils.makeDir(mContext, DIRECTORY_NAME), name);
            fileOutputStream = new FileOutputStream(file);
            if (file.exists() && file.length() == urlConnection.getContentLength()) {
                return;
            }
            InputStream inputStream = urlConnection.getInputStream();


            int maxLength = urlConnection.getContentLength();//文件的最大进度

            byte[] buffer = new byte[10];
            int bufferLength = 0;

            int progress = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
//                if (currentState == STATE_DESTROY && file.length() < urlConnection.getContentLength()) {
//                    file.delete();
//                    break;
//                }
                progress += bufferLength;

                onUIChangeListener.onDownLoadChangeListener(maxLength, (int) (progress / (maxLength * 1f) * 100));

                fileOutputStream.write(buffer, 0, bufferLength);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private String getFileName(String path) {

        String name = Md5.md5(path);
        if (path.lastIndexOf("/") != -1) {
            name = path.substring(path.lastIndexOf("/") + 1);
        }
        return name;
    }

    @Override
    public void destroy() {
        if (mHandler != null) {
            mHandler.removeCallbacks(myRunnable);
        }
        stopMusic();
    }
}
