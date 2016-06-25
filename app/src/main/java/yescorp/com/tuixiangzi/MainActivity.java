package yescorp.com.tuixiangzi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGameIntro = (Button) findViewById(R.id.btn_game_intro);
        btnGameIntro.setOnClickListener(new BtnGameIntro_ClickListener());

        Button btnGameStart = (Button) findViewById(R.id.btn_start_game);
        btnGameStart.setOnClickListener(new BtnStart_ClickListener());

        Button btnExit = (Button) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new BtnExit_ClickListener());
    }

    private class BtnGameIntro_ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, GameIntroActivity.class);
            startActivity(i);
        }
    }

    private class BtnStart_ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent xgq = new Intent(MainActivity.this, XuanGuanQiaActivity.class);
            startActivity(xgq);

        }
    }

    private class BtnExit_ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            finish();
        }
    }
}
