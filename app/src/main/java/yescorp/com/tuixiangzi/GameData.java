package yescorp.com.tuixiangzi;

import android.graphics.Point;

/**
 * Created by 612226 on 2016/6/28.
 */
public class GameData {
    private GameInitialData mAllLevelsInitialData;              //所有关卡的数据
    private int mLevel;
    private StringBuffer[] mGameState;
    private Point mManPostion = new Point();
    private final String[] mSelectedInitialData;    //当前所选的关卡，与mLevel对应

    public GameData(int level){
        mAllLevelsInitialData = new GameInitialData();
        mLevel = level;   //level从1开始计数
        mGameState = new StringBuffer[mAllLevelsInitialData.getBoard_Row_Num()];
        mSelectedInitialData = mAllLevelsInitialData.getGameLevels().get(level - 1);
        for (int r = 0; r < mAllLevelsInitialData.getBoard_Row_Num(); r++) {
            mGameState[r] = new StringBuffer(mSelectedInitialData[r]);
            for (int c = 0; c < mAllLevelsInitialData.getBoard_Column_Num(); c++)
                if (mSelectedInitialData[r].charAt(c) == GameInitialData.MAN){
                    mManPostion.set(c, r);
                }
        }
    }

    public StringBuffer[] getGameState() {
        return mGameState;
    }

    public int getBoardColumnNum(){
        return mAllLevelsInitialData.getBoard_Column_Num();
    }

    public int getBoardRowNum(){
        return mAllLevelsInitialData.getBoard_Row_Num();
    }

    public void goUp() {
        if (mManPostion.y <= 0) return;
        char upCell = mGameState[mManPostion.y - 1].charAt(mManPostion.x);
        if (upCell == GameInitialData.BOX) {
            moveBoxUp(mManPostion.y - 1, mManPostion.x);
            upCell = mGameState[mManPostion.y - 1].charAt(mManPostion.x);
        }

        if (upCell == GameInitialData.NOTHING || upCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.y--;
            mGameState[mManPostion.y].setCharAt(mManPostion.x, GameInitialData.MAN);
        }
    }

    private void restoreOldState(int row, int column) {
        if (hasFlag(row, column))
            mGameState[row].setCharAt(column, GameInitialData.FLAG);
        else
            mGameState[row].setCharAt(column, GameInitialData.NOTHING);
    }

    private void manGoAway() {
        restoreOldState(mManPostion.y, mManPostion.x);
    }

    private void moveBoxUp(int row, int column) {
        if (row <= 0) return;
        char upCell = mGameState[row - 1].charAt(column);
        if (upCell  == GameInitialData.NOTHING || upCell == GameInitialData.FLAG){
            restoreOldState(row, column);
            mGameState[row - 1].setCharAt(column, GameInitialData.BOX);
        }
    }

    public void goDown() {
        if (mManPostion.y >= mAllLevelsInitialData.getBoard_Row_Num() - 1) return;
        char downCell = mGameState[mManPostion.y + 1].charAt(mManPostion.x);
        if (downCell == GameInitialData.BOX) {
            moveBoxDown(mManPostion.y + 1, mManPostion.x);
            downCell = mGameState[mManPostion.y + 1].charAt(mManPostion.x);
        }
        if (downCell == GameInitialData.NOTHING || downCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.y++;
            mGameState[mManPostion.y].setCharAt(mManPostion.x, GameInitialData.MAN);
        }
    }

    private void moveBoxDown(int row, int column) {
        if (row >= mAllLevelsInitialData.getBoard_Row_Num() - 1) return;
        char downCell = mGameState[row + 1].charAt(column);
        if (downCell == GameInitialData.NOTHING || downCell == GameInitialData.FLAG){
            restoreOldState(row, column);
            mGameState[row + 1].setCharAt(column, GameInitialData.BOX);
        }
    }

    public void goRight() {
        if (mManPostion.x >= mAllLevelsInitialData.getBoard_Column_Num() - 1) return;
        char rightCell = mGameState[mManPostion.y].charAt(mManPostion.x + 1);
        if (rightCell == GameInitialData.BOX) {
            moveBoxRight(mManPostion.y, mManPostion.x + 1);
            rightCell = mGameState[mManPostion.y].charAt(mManPostion.x + 1);
        }
//        Log.d("GameData", "goRight(): rightCell=" + rightCell);
        if (rightCell == GameInitialData.NOTHING || rightCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.x++;
            mGameState[mManPostion.y].setCharAt(mManPostion.x, GameInitialData.MAN);
        }
    }

    private void moveBoxRight(int row, int column) {
        if (column >= mAllLevelsInitialData.getBoard_Column_Num() - 1) return;
        char rightCell = mGameState[row].charAt(column + 1);
        if (rightCell == GameInitialData.NOTHING || rightCell == GameInitialData.FLAG){
            restoreOldState(row, column);
            mGameState[row].setCharAt(column + 1, GameInitialData.BOX);
        }
    }

    public void goLeft() {
        if (mManPostion.x <= 0) return;
        char leftCell = mGameState[mManPostion.y].charAt(mManPostion.x - 1);
        if (leftCell == GameInitialData.BOX) {
            moveBoxLeft(mManPostion.y, mManPostion.x - 1);
            leftCell = mGameState[mManPostion.y].charAt(mManPostion.x - 1);
        }
        if (leftCell == GameInitialData.NOTHING || leftCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.x--;
            mGameState[mManPostion.y].setCharAt(mManPostion.x, GameInitialData.MAN);
        }
    }

    private void moveBoxLeft(int row, int column) {
        if (column <= 0) return;
        char leftCell = mGameState[row].charAt(column - 1);
        if (leftCell == GameInitialData.FLAG || leftCell == GameInitialData.NOTHING){
            restoreOldState(row, column);
            mGameState[row].setCharAt(column - 1, GameInitialData.BOX);
        }
    }

    //据所选关卡的初始数据处获取单元格(row, column)是否有红旗
    public boolean hasFlag(int row, int column) {
        return  mSelectedInitialData[row].charAt(column) == 'F';
    }
}
