package yescorp.com.tuixiangzi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by 612226 on 2016/6/27.
 */
public class GameActivity extends Activity {

    private int mLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mLevel = intent.getIntExtra(XuanGuanQiaActivity.GUAN_QIA, 1);
        setContentView(new GameView(this, mLevel));
    }

    public int getLevel() {
        return mLevel;
    }
}
