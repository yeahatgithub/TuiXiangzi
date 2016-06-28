package yescorp.com.tuixiangzi;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 612226 on 2016/6/27.
 */
public class GameView extends View {

    private GameActivity mGameActivity;
    private float mColumnWidth;
    private float mRowHeight;
    private Bitmap mWallBitmap;
    private Bitmap mManBitmap;
    private Bitmap mBoxBitmap;
    private Bitmap mFlagBitmap;
    private Bitmap mUpBitmap;
    private Bitmap mDownBitmap;
    private Bitmap mRightBitmap;
    private Bitmap mLeftBitmap;
    private GameData mGameData;
    private int mLevel;
    private int TOP_LEFT_X = 0;
    private int TOP_LEFT_Y = 40;
    private Rect mUpArrowRect;
    private Rect mRightArrowRect;
    private Rect mDownArrowRect;
    private Rect mLeftArrowRect;

    public GameView(Context context, int level) {
        super(context);
        mGameActivity = (GameActivity) context;
        mLevel = level;
        setFocusable(true);
        setFocusableInTouchMode(true);
        Resources res = getResources();
        mBoxBitmap = BitmapFactory.decodeResource(res, R.drawable.box_48x48);
        mManBitmap = BitmapFactory.decodeResource(res, R.drawable.eggman_48x48);
        mFlagBitmap = BitmapFactory.decodeResource(res, R.drawable.flag_48x48);
        mWallBitmap = BitmapFactory.decodeResource(res, R.drawable.wall_48x48);
        mUpBitmap= BitmapFactory.decodeResource(res, R.drawable.up_48x48);
        mDownBitmap = BitmapFactory.decodeResource(res, R.drawable.down_48x48);
        mRightBitmap = BitmapFactory.decodeResource(res, R.drawable.right_48x48);
        mLeftBitmap = BitmapFactory.decodeResource(res, R.drawable.left_48x48);
        mGameData = new GameData(mLevel);
        mUpArrowRect = new Rect();
        mRightArrowRect = new Rect();
        mDownArrowRect = new Rect();
        mLeftArrowRect = new Rect();
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
    }

    private void drawArrows(Canvas canvas) {
        final int ARROW_WIDTH = 72;         //方向键是72x72规格的
        int boardBottom_y = TOP_LEFT_Y + (int)(mRowHeight * mGameData.getBoardRowNum());
        int arrowTop_y = boardBottom_y + 8;      //8是游戏区底部与方向键顶部的间隔
        int upTopLeft_x = (getWidth() - ARROW_WIDTH) / 2;
        int upTopLeft_y = arrowTop_y;
        mUpArrowRect.set(upTopLeft_x, upTopLeft_y, upTopLeft_x + ARROW_WIDTH, upTopLeft_y + ARROW_WIDTH);
        canvas.drawBitmap(mUpBitmap, null, mUpArrowRect, null);

        int rightTopLeft_x = upTopLeft_x + ARROW_WIDTH;
        int rightTopLeft_y = upTopLeft_y + ARROW_WIDTH;
        mRightArrowRect.set(rightTopLeft_x, rightTopLeft_y, rightTopLeft_x + ARROW_WIDTH, rightTopLeft_y + ARROW_WIDTH);
        canvas.drawBitmap(mRightBitmap, null, mRightArrowRect, null);

        int downTopLeft_x = upTopLeft_x;
        int downTopLeft_y = rightTopLeft_y + ARROW_WIDTH;
        mDownArrowRect.set(downTopLeft_x, downTopLeft_y, downTopLeft_x + ARROW_WIDTH, downTopLeft_y + ARROW_WIDTH);
        canvas.drawBitmap(mDownBitmap, null, mDownArrowRect, null);

        int leftTopLeft_x = upTopLeft_x - ARROW_WIDTH;
        int leftTopLeft_y = upTopLeft_y + ARROW_WIDTH;
        mLeftArrowRect.set(leftTopLeft_x, leftTopLeft_y, leftTopLeft_x + ARROW_WIDTH, leftTopLeft_y + ARROW_WIDTH);
        canvas.drawBitmap(mLeftBitmap, null, mLeftArrowRect, null);
    }

    private void drawGameBoard(Canvas canvas) {
        Rect destRect = new Rect();
        for (int r = 0; r < mGameData.getBoardRowNum(); r++ )
            for (int c = 0; c < mGameData.getBoardColumnNum(); c++){
                int topleft_x = (int)(TOP_LEFT_X + c * mColumnWidth);
                int topleft_y = (int)(TOP_LEFT_Y + r * mRowHeight);
                destRect.set(topleft_x, topleft_y,(int)(topleft_x + mColumnWidth), (int)(topleft_y + mRowHeight));
                StringBuffer []gameState = mGameData.getGameState();
                switch (gameState[r].charAt(c)){
                    case GameInitialData.BOX:
                        canvas.drawBitmap(mBoxBitmap, null, destRect, null);
                        break;
                    case GameInitialData.FLAG:
                        canvas.drawBitmap(mFlagBitmap, null, destRect, null);
                        break;
                    case GameInitialData.NOTHING:
                        break;
                    case GameInitialData.MAN:
                        canvas.drawBitmap(mManBitmap, null, destRect, null);
                        break;
                    case GameInitialData.WALL:
                        canvas.drawBitmap(mWallBitmap, null, destRect, null);
                        break;
                }
            }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

        int touch_x = (int)event.getX();
        int touch_y = (int)event.getY();
//        Log.d("GameView", "onTouchEvent()...touch_x=" + touch_x + ", touch_y=" + touch_y);
        if (mUpArrowRect.contains(touch_x, touch_y)) {
//            Log.d("GameView", "You have pressed the UP arrow.");
            mGameData.goUp();
            invalidate();
        }
        return true;
    }
}
