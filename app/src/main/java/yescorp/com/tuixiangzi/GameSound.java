package yescorp.com.tuixiangzi;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;

/**
 * Created by 612226 on 2016/7/6.
 */
public class GameSound {
    private static SoundPool mSoundPool;
    private static int mOneSetpMusicId;
//    private static SoundPool mMoveBoxSound;
    private static  int mMoveBoxMusicId;
//    private static SoundPool mGameOverSound;
    private static int mGameOverMusicId;

    public static void loadSound(AssetManager assetManager) {
        try {
            AssetFileDescriptor oneStepFd = assetManager.openFd("onestep.ogg");
            AssetFileDescriptor moveBoxFd = assetManager.openFd("movebox.ogg");
            AssetFileDescriptor gameOverFd = assetManager.openFd("game_over.ogg");
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            mOneSetpMusicId = mSoundPool.load(oneStepFd, 1);
            mMoveBoxMusicId = mSoundPool.load(moveBoxFd, 1);
            mGameOverMusicId = mSoundPool.load(gameOverFd, 1);
        } catch (IOException e) {
            mSoundPool = null;
            mOneSetpMusicId = -1;
            mMoveBoxMusicId = -1;
        }
    }

    public static void playOneStepSound(){
        if (mSoundPool != null )
            mSoundPool.play(mOneSetpMusicId, 1.0f, 1.0f, 1, 0, 1);
    }

    public static void playMoveBoxSound(){
        if (mSoundPool != null)
            mSoundPool.play(mMoveBoxMusicId, 1.0f, 1.0f, 1, 0, 1);
    }

    public static void releaseSound(){
        if (mSoundPool == null) return;
        mSoundPool.unload(mOneSetpMusicId);
        mSoundPool.unload(mMoveBoxMusicId);
        mSoundPool.unload(mGameOverMusicId);
        mSoundPool.release();
    }

    public static void playGameOverSound(AssetManager assetManager){
        if (mSoundPool != null)
            mSoundPool.play(mGameOverMusicId, 1.0f, 1.0f, 1, 0 ,1);  //报告"sample 3 not READY"，应该是上一步的Load操作没有完成
    }
}
