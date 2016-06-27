package yescorp.com.tuixiangzi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by 612226 on 2016/6/27.
 */
public class GameView extends View {
    private static final int BOARD_COLUMN_NUM = 12;
    public static final int BOX = 1;
    public static final int FLAG = 2;
    public static final int MAN = 3;
    public static final int WALL = 4;

    //TODO: 定义一个12x12的矩阵，用于存储推箱子游戏局面 -- 定义一个游戏局面类GameInfo
    //每当箱子或搬运工移动，就修改游戏局面；
    //当箱子全部到达目的地，就判别得出通过游戏关卡。

    private GameActivity mContext;
    private float mColumnWidth;
    private float mRowHeight;
    private Bitmap mWallBitmap;
    private Bitmap mManBitmap;
    private Bitmap mBoxBitmap;
    private Bitmap mFlagBitmap;

    public GameView(Context context) {
        super(context);
        mContext = (GameActivity) context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        mBoxBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.box_48x48);
        mManBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eggman_48x48);
        mFlagBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flag_48x48);
        mWallBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wall_48x48);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mColumnWidth = (float)w / BOARD_COLUMN_NUM;
        mRowHeight = (float)w / BOARD_COLUMN_NUM;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint background = new Paint();
        background.setColor(getResources().getColor(R.color.board_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);

        Rect destRect = new Rect(0, 200, (int)mColumnWidth, (int)(200 + mRowHeight));
        canvas.drawBitmap(mBoxBitmap, null, destRect, null);
    }
}
