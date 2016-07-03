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
        mView = new GameView(this, mGameLevel);
        setContentView(mView);
    }

    public int getGameLevel() {
        return mGameLevel;
    }

}
