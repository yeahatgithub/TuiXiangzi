package yescorp.com.tuixiangzi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
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
    private Rect mUpArrowRect;
    private Rect mRightArrowRect;
    private Rect mDownArrowRect;
    private Rect mLeftArrowRect;
    private Rect mBtnNextLevel = new Rect();
    private Rect mBtnReset = new Rect();
    private Rect mBtnExit = new Rect();

    public GameView(Context context, int level) {
        super(context);
        mGameActivity = (GameActivity) context;
        mGameLevel = level;
        mUpArrowRect = new Rect();
        mRightArrowRect = new Rect();
        mDownArrowRect = new Rect();
        mLeftArrowRect = new Rect();
        setFocusable(true);
        setFocusableInTouchMode(true);
        Resources res = getResources();
        loadBitmaps(res);
        try {
            mGameData = new GameData(res, mGameLevel);
        } catch (IOException e) {
            Toast.makeText(mGameActivity, "无法打开或读取配置文件。程序退出。", Toast.LENGTH_LONG).show();
            System.exit(-1);
        }
    }


//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//    }

    private void loadBitmaps(Resources res) {
        if (GameBitmaps.mBoxBitmap == null)
            GameBitmaps.mBoxBitmap = BitmapFactory.decodeResource(res, R.drawable.box_48x48);
        if (GameBitmaps.mManBitmap == null)
            GameBitmaps.mManBitmap = BitmapFactory.decodeResource(res, R.drawable.eggman_48x48);
        if (GameBitmaps.mFlagBitmap == null)
            GameBitmaps.mFlagBitmap = BitmapFactory.decodeResource(res, R.drawable.flag_48x48);
        if (GameBitmaps.mWallBitmap == null)
            GameBitmaps.mWallBitmap = BitmapFactory.decodeResource(res, R.drawable.wall_48x48);
        if (GameBitmaps.mUpBitmap == null)
            GameBitmaps.mUpBitmap= BitmapFactory.decodeResource(res, R.drawable.up_48x48);
        if (GameBitmaps.mDownBitmap == null)
            GameBitmaps.mDownBitmap = BitmapFactory.decodeResource(res, R.drawable.down_48x48);
        if (GameBitmaps.mRightBitmap == null)
            GameBitmaps.mRightBitmap = BitmapFactory.decodeResource(res, R.drawable.right_48x48);
        if (GameBitmaps.mLeftBitmap == null)
            GameBitmaps.mLeftBitmap = BitmapFactory.decodeResource(res, R.drawable.left_48x48);
        if (GameBitmaps.mDoneBitmap == null)
            GameBitmaps.mDoneBitmap = BitmapFactory.decodeResource(res, R.drawable.done_72x72);
        if (GameBitmaps.mBtnNextBitmap == null)
            GameBitmaps.mBtnNextBitmap = BitmapFactory.decodeResource(res, R.drawable.btn_next_level);
        if (GameBitmaps.mBtnResetBitmap == null)
            GameBitmaps.mBtnResetBitmap = BitmapFactory.decodeResource(res, R.drawable.btn_reset);
        if (GameBitmaps.mBtnExitBitmap == null)
            GameBitmaps.mBtnExitBitmap = BitmapFactory.decodeResource(res, R.drawable.btn_exit);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mColumnWidth = (float)w / mGameData.getBoardColumnNum();
        mRowHeight = (float)w / mGameData.getBoardRowNum();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //背景色
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.board_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        //游戏区域
        drawGameBoard(canvas);
        //上下左右方向键
        drawArrows(canvas);
        //成功过关
        if (mGameData.isGameOver())
            drawDoneLabel(canvas);

        drawButtons(canvas);
    }


    private void drawArrows(Canvas canvas) {
        final int ARROW_WIDTH = 72;         //方向键是72x72规格的
        int boardBottom_y = TOP_LEFT_Y + (int)(mRowHeight * mGameData.getBoardRowNum());
        int arrowTop_y = boardBottom_y + 8;      //8是游戏区底部与方向键顶部的间隔
        int upTopLeft_x = (getWidth() - ARROW_WIDTH) / 2;
        int upTopLeft_y = arrowTop_y;
        mUpArrowRect.set(upTopLeft_x, upTopLeft_y, upTopLeft_x + ARROW_WIDTH, upTopLeft_y + ARROW_WIDTH);
        canvas.drawBitmap(GameBitmaps.mUpBitmap, null, mUpArrowRect, null);

        int rightTopLeft_x = upTopLeft_x + ARROW_WIDTH;
        int rightTopLeft_y = upTopLeft_y + ARROW_WIDTH;
        mRightArrowRect.set(rightTopLeft_x, rightTopLeft_y, rightTopLeft_x + ARROW_WIDTH, rightTopLeft_y + ARROW_WIDTH);
        canvas.drawBitmap(GameBitmaps.mRightBitmap, null, mRightArrowRect, null);

        int downTopLeft_x = upTopLeft_x;
        int downTopLeft_y = rightTopLeft_y + ARROW_WIDTH;
        mDownArrowRect.set(downTopLeft_x, downTopLeft_y, downTopLeft_x + ARROW_WIDTH, downTopLeft_y + ARROW_WIDTH);
        canvas.drawBitmap(GameBitmaps.mDownBitmap, null, mDownArrowRect, null);

        int leftTopLeft_x = upTopLeft_x - ARROW_WIDTH;
        int leftTopLeft_y = upTopLeft_y + ARROW_WIDTH;
        mLeftArrowRect.set(leftTopLeft_x, leftTopLeft_y, leftTopLeft_x + ARROW_WIDTH, leftTopLeft_y + ARROW_WIDTH);
        canvas.drawBitmap(GameBitmaps.mLeftBitmap, null, mLeftArrowRect, null);
    }

    private void drawGameBoard(Canvas canvas) {
        Rect destRect = new Rect();
        for (int r = 0; r < mGameData.getBoardRowNum(); r++ )
            for (int c = 0; c < mGameData.getBoardColumnNum(); c++){
                int topleft_x = (int)(TOP_LEFT_X + c * mColumnWidth);
                int topleft_y = (int)(TOP_LEFT_Y + r * mRowHeight);
                destRect.set(topleft_x, topleft_y,(int)(topleft_x + mColumnWidth), (int)(topleft_y + mRowHeight));
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
                        destRect.set(destRect.left, destRect.top, destRect.right+2, destRect.bottom + 2);  //+2是为了去除墙体之间的缝隙
                        canvas.drawBitmap(GameBitmaps.mWallBitmap, null, destRect, null);
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
        if (!mGameData.isGameOver()) {
//        Log.d("GameView", "onTouchEvent()...touch_x=" + touch_x + ", touch_y=" + touch_y);
            if (mUpArrowRect.contains(touch_x, touch_y)) {
//            Log.d("GameView", "You have pressed the UP arrow.");
                mGameData.goUp();
//            invalidate();
            }
            if (mRightArrowRect.contains(touch_x, touch_y)) {
                mGameData.goRight();
//            invalidate();
            }
            if (mDownArrowRect.contains(touch_x, touch_y)) {
//            Log.d("GameView", "You have pressed the DOWN arrow.");
                mGameData.goDown();
//            invalidate();
            }
            if (mLeftArrowRect.contains(touch_x, touch_y)) {
//            Log.d("GameView", "You have pressed the LEFT arrow.");
                mGameData.goLeft();
//            invalidate();
            }

            invalidate();
        }

        if (mBtnNextLevel.contains(touch_x, touch_y)){
            if (mGameLevel < GameInitialData.GameLevels.size())  //mGameLevel从1开始计数
                mGameActivity.goToNextLevel();
            else
                Toast.makeText(mGameActivity, "恭喜！你已经通关了。你好厉害。", Toast.LENGTH_LONG).show();
        }

        if (mBtnReset.contains(touch_x, touch_y)){
            mGameActivity.goToLevel(mGameLevel);
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
}
