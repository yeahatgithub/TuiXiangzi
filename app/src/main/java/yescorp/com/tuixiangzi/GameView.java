package yescorp.com.tuixiangzi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by 612226 on 2016/6/27.
 */
public class GameView extends View {

    private GameActivity mGameActivity;
    private float mColumnWidth;
    private float mRowHeight;
    private GameData mGameData;
    private int mGameLevel;
    private int TOP_LEFT_X = 0;
    private int TOP_LEFT_Y = 40;
    private Rect mManRect = new Rect();          //搬运工所在的位置
    private Rect mBtnNextLevel = new Rect();
    private Rect mBtnReset = new Rect();
    private Rect mBtnExit = new Rect();

    public GameView(Context context, int level) {
        super(context);
        mGameActivity = (GameActivity) context;
        mGameLevel = level;
        setFocusable(true);
        setFocusableInTouchMode(true);
        Resources res = getResources();
        GameBitmaps.loadBitmaps(res);
        try {
            mGameData = new GameData(res, mGameLevel);
        } catch (IOException e) {
            Toast.makeText(mGameActivity, "无法打开或读取配置文件。程序退出。", Toast.LENGTH_LONG).show();
            System.exit(-1);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mColumnWidth = (float)w / mGameData.getBoardColumnNum();
        mRowHeight = (float)w / mGameData.getBoardRowNum();
        getManRect(mGameData.getmManPostion(), mRowHeight, mColumnWidth);
        super.onSizeChanged(w, h, oldw, oldh);
    }


    private void getManRect(TCell tCell, float rowHeight,float columnWidth ) {
        int left = (int)(TOP_LEFT_X + tCell.column * columnWidth);
        int top = (int)(TOP_LEFT_Y + tCell.row * rowHeight);
        int right = (int)(left + columnWidth);
        int bottom = (int)(top + rowHeight);
        mManRect.set(left, top, right, bottom);
    }

    public void goToLevel(int level){
        mGameLevel = level;
        try {
            mGameData = new GameData(getResources(), mGameLevel);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mColumnWidth = getWidth() / mGameData.getBoardColumnNum();
        mRowHeight = getWidth() / mGameData.getBoardRowNum();   //正方形区域
        getManRect(mGameData.getmManPostion(), mRowHeight, mColumnWidth);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //背景色
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.board_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        //游戏区域
        drawGameBoard(canvas);

        //成功过关
        if (mGameData.isGameOver()) {
            drawDoneLabel(canvas);
            PrfsManager.setPassedLevel(mGameActivity, mGameLevel);   //记住已经通过本关卡
        }

        drawButtons(canvas);
    }


    private void drawGameBoard(Canvas canvas) {
        Rect destRect = new Rect();
        for (int r = 0; r < mGameData.getBoardRowNum(); r++ )
            for (int c = 0; c < mGameData.getBoardColumnNum(); c++){
                int topleft_x = (int)(TOP_LEFT_X + c * mColumnWidth);
                int topleft_y = (int)(TOP_LEFT_Y + r * mRowHeight);
                destRect.set(topleft_x, topleft_y,(int)(topleft_x + mColumnWidth) + 2, (int)(topleft_y + mRowHeight) + 2);//+2是为了去除墙体之间的缝隙
                if (mGameData.hasFlag(r, c))
                    canvas.drawBitmap(GameBitmaps.mFlagBitmap, null, destRect, null);
                StringBuffer []gameState = mGameData.getGameState();
                switch (gameState[r].charAt(c)){
                    case GameInitialData.BOX:
                        canvas.drawBitmap(GameBitmaps.mBoxBitmap, null, destRect, null);
                        break;
                    case GameInitialData.FLAG:
                        canvas.drawBitmap(GameBitmaps.mFlagBitmap, null, destRect, null);
                        break;
                    case GameInitialData.NOTHING:
                        break;
                    case GameInitialData.MAN:
                        canvas.drawBitmap(GameBitmaps.mManBitmap, null, destRect, null);
                        break;
                    case GameInitialData.WALL:
//                        destRect.set(destRect.left, destRect.top, destRect.right+2, destRect.bottom + 2);  //+2是为了去除墙体之间的缝隙
                        canvas.drawBitmap(GameBitmaps.mWallBitmap, null, destRect, null);
                        break;
                    case GameInitialData.BOX_FLAG:
                        canvas.drawBitmap(GameBitmaps.mFlagBitmap, null, destRect, null);
                        canvas.drawBitmap(GameBitmaps.mBoxBitmap, null, destRect, null);
                        break;
                    case GameInitialData.MAN_FLAG:
                        canvas.drawBitmap(GameBitmaps.mFlagBitmap, null, destRect, null);
                        canvas.drawBitmap(GameBitmaps.mManBitmap, null, destRect, null);
                        break;
                }
            }
    }

    private void drawDoneLabel(Canvas canvas) {
        int begin_x = TOP_LEFT_X + 120;
        int begin_y = TOP_LEFT_Y + 120;
        int end_x = canvas.getWidth() - 120;
        int end_y = begin_y + (end_x - begin_x);
        Rect label_rect = new Rect(begin_x, begin_y, end_x, end_y);
        Paint paint = new Paint();
        paint.setAlpha(125);
        canvas.drawBitmap(GameBitmaps.mDoneBitmap, null, label_rect, paint);
    }

    private void drawButtons(Canvas canvas) {
        final int BOTTOM_MARGIN = 20;
        final int LEFT_MARGIN = 106;
        final int RIGHT_MARGIN = LEFT_MARGIN;
        final int BUTTON_INTERVAL = 80;
        final int BUTTON_WIDTH = 132;    //原始图片的像素是66x26
        int Button_Height = BUTTON_WIDTH / GameBitmaps.mBtnNextBitmap.getWidth() * GameBitmaps.mBtnNextBitmap.getHeight();
        int button_y = canvas.getHeight() - BOTTOM_MARGIN - Button_Height;
        int buttion_1_x = LEFT_MARGIN;
        mBtnNextLevel.set(buttion_1_x, button_y, buttion_1_x + BUTTON_WIDTH, button_y + Button_Height);
        canvas.drawBitmap(GameBitmaps.mBtnNextBitmap, null, mBtnNextLevel, null);
        int button_2_x = buttion_1_x + BUTTON_WIDTH + BUTTON_INTERVAL;
        mBtnReset.set(button_2_x, button_y, button_2_x + BUTTON_WIDTH, button_y + Button_Height);
        canvas.drawBitmap(GameBitmaps.mBtnResetBitmap, null, mBtnReset, null);
        int button_3_x = button_2_x + BUTTON_WIDTH + BUTTON_INTERVAL;
        mBtnExit.set(button_3_x, button_y, button_3_x + BUTTON_WIDTH, button_y + Button_Height);
        canvas.drawBitmap(GameBitmaps.mBtnExitBitmap, null, mBtnExit, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

        int touch_x = (int) event.getX();
        int touch_y = (int) event.getY();
//        Log.d("onTouchEvent", "touch_x=" + touch_x + ", touch_y=" + touch_y);
        if (!mGameData.isGameOver()) {
//        Log.d("GameView", "onTouchEvent()...touch_x=" + touch_x + ", touch_y=" + touch_y);

            //用户通过在游戏区域触摸来控制搬运工的行进
            //当触摸点落在搬运工所在单元格的上、下、左、右格子n时，即意味着指示搬运工走到格子n上（阻挡问题另外考虑）
            if (touch_left_to_man(touch_x, touch_y))
                mGameData.goLeft();
            if (touch_right_to_man(touch_x, touch_y))
                mGameData.goRight();
            if (touch_above_to_man(touch_x, touch_y))
                mGameData.goUp();
            if (touch_blow_to_man(touch_x, touch_y))
                mGameData.goDown();

            getManRect(mGameData.getmManPostion(), mRowHeight, mColumnWidth);
            invalidate();
        }

        if (mBtnNextLevel.contains(touch_x, touch_y)){
//            Log.d("onTouchEvent()", "下一关按钮被按下");
            if (mGameLevel < GameInitialData.GameLevels.size())  //mGameLevel从1开始计数
//                mGameActivity.goToNextLevel();
                goToLevel(mGameLevel + 1);
            else
                Toast.makeText(mGameActivity, R.string.no_more_levels, Toast.LENGTH_LONG).show();
        }

        if (mBtnReset.contains(touch_x, touch_y)){
//            mGameActivity.goToLevel(mGameLevel);
            goToLevel(mGameLevel);
        }

        if (mBtnExit.contains(touch_x, touch_y)){
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mGameActivity.startActivity(startMain);
            System.exit(0);
        }

        return true;
    }

    private boolean touch_blow_to_man(int touch_x, int touch_y) {
        Rect belowRect = new Rect(mManRect.left, mManRect.top + (int)mRowHeight, mManRect.right, mManRect.bottom + (int)mRowHeight);
        return belowRect.contains(touch_x, touch_y);
    }

    private boolean touch_above_to_man(int touch_x, int touch_y) {
        Rect aboveRect = new Rect(mManRect.left, mManRect.top - (int)mRowHeight, mManRect.right, mManRect.bottom - (int)mRowHeight);
        return aboveRect.contains(touch_x, touch_y);
    }

    private boolean touch_right_to_man(int touch_x, int touch_y) {
        Rect rightRect = new Rect(mManRect.left + (int)mColumnWidth, mManRect.top, mManRect.right + (int)mColumnWidth, mManRect.bottom);
        return rightRect.contains(touch_x, touch_y);
    }

    private boolean touch_left_to_man(int touch_x, int touch_y) {
        Rect leftRect = new Rect(mManRect.left - (int)mColumnWidth, mManRect.top, mManRect.right - (int)mColumnWidth, mManRect.bottom);
        return leftRect.contains(touch_x, touch_y);
    }
}
