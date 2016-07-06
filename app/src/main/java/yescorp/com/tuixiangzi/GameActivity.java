package yescorp.com.tuixiangzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.GenericArrayType;

/**
 * Created by 612226 on 2016/6/27.
 */
public class GameActivity extends Activity {

    private int mGameLevel;
    private GameView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mGameLevel = intent.getIntExtra(XuanGuanQiaActivity.GUAN_QIA, 1);
        GameBitmaps.loadBitmaps(getResources());   //须在生成GameView对象前加载它用到的图片
        GameSound.loadSound(getAssets());           //须在生成GameView对象前加载它会用到的音效
        mView = new GameView(this, mGameLevel);
        setContentView(mView);
    }

    public int getGameLevel() {
        return mGameLevel;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GameBitmaps.releaseBitmaps();               //释放图片占用的内存
        GameSound.releaseSound();                   //释放音效占用的内存
    }
}
