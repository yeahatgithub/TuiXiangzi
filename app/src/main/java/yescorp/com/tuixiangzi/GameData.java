package yescorp.com.tuixiangzi;

import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 612226 on 2016/6/28.
 */
public class GameData {
//    private static GameInitialData mAllLevelsInitialData;         //所有关卡的数据
    private int mSelectedLevel;
    private int mRowNum;
    private int mColumnNum;
    private StringBuffer[] mGameState;
    private TCell mManPostion = new TCell();
    private LevelInitialData mSelectedInitialData;    //当前所选的关卡的初始数据，与mSelectedLevel对应
    private List<TCell> mFlagCells = new ArrayList<>();             //记住所有红旗所在的位置

    public GameData(Resources res, int level) throws IOException {
        if (GameInitialData.GameLevels.size() == 0)
            //GameInitialData.addInitGameData();
            GameInitialData.readInitialData(res, GameInitialData.CONFIG_FILE_NAME);
        mSelectedLevel = level;   //level从1开始计数
        mSelectedInitialData = GameInitialData.GameLevels.get(level - 1);
        initializeGameState();
    }

    private void initializeGameState() {
        mRowNum = mSelectedInitialData.mRowNum;
        mColumnNum = mSelectedInitialData.mColumnNum;
        if (mRowNum < GameInitialData.DEFAULT_ROW_NUM)
            mGameState = new StringBuffer[GameInitialData.DEFAULT_ROW_NUM];  //尾部将添加若干空行
        else
            mGameState = new StringBuffer[mRowNum];

        StringBuffer leftBlanks = new StringBuffer("");
        StringBuffer rightBlanks = new StringBuffer("");
        //游戏区域不足11列，左右两边加上若干空白列，凑足11列
        if (mColumnNum < GameInitialData.DEFAULT_COLUMN_NUM){
            int leftBlankCnt = (GameInitialData.DEFAULT_COLUMN_NUM - mColumnNum) / 2;
            for (int i = 0; i < leftBlankCnt; i++ )
                leftBlanks.append(" ");
            for (int i = 0; i < GameInitialData.DEFAULT_COLUMN_NUM - mColumnNum - leftBlankCnt; i++)
                rightBlanks.append(" ");
            mColumnNum = GameInitialData.DEFAULT_COLUMN_NUM;
        }

        for (int r = 0; r < mRowNum; r++) {
            mGameState[r] = new StringBuffer(leftBlanks);
            mGameState[r].append(mSelectedInitialData.mInitialState[r]);
            mGameState[r].append(rightBlanks);
            //Log.d("GameData", "initializeGameState(), mGameState[" + r + "].length=" + mGameState[r].length());
            for (int c = 0; c < mColumnNum; c++) {
                if (mGameState[r].charAt(c) == GameInitialData.MAN || mGameState[r].charAt(c) == GameInitialData.MAN_FLAG) {
                    mManPostion.set(r, c);
                }
                if (mGameState[r].charAt(c) == GameInitialData.FLAG || mGameState[r].charAt(c) == GameInitialData.MAN_FLAG
                        || mGameState[r].charAt(c) == GameInitialData.BOX_FLAG){
                    TCell cell = new TCell(r, c);
                    mFlagCells.add(cell);
                }
            }
        }
        //行数不足11行，使得墙体图片看起来偏大，故添加若干空行
        if (mRowNum < GameInitialData.DEFAULT_ROW_NUM){
            for (int i = mRowNum; i < GameInitialData.DEFAULT_ROW_NUM; i++) {
                StringBuffer blankLine = new StringBuffer();
                for (int c = 0; c < mColumnNum; c++)
                    blankLine.append(" ");
                mGameState[i] = blankLine;
            }
            mRowNum = GameInitialData.DEFAULT_ROW_NUM;
        }
    }

    public StringBuffer[] getGameState() {
        return mGameState;
    }

    public TCell getmManPostion(){
        return  mManPostion;
    }

    public int getBoardColumnNum(){
        return mColumnNum;
    }

    public int getBoardRowNum(){
        return mRowNum;
    }

    public void goUp() {
        if (mManPostion.row <= 0) return;
        char upCell = mGameState[mManPostion.row - 1].charAt(mManPostion.column);
        if (upCell == GameInitialData.BOX) {
            moveBoxUp(mManPostion.row - 1, mManPostion.column);
            upCell = mGameState[mManPostion.row - 1].charAt(mManPostion.column);
        }

        if (upCell == GameInitialData.NOTHING || upCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.row--;
            mGameState[mManPostion.row].setCharAt(mManPostion.column, GameInitialData.MAN);
        }
    }

    private void restoreOldState(int row, int column) {
        if (hasFlag(row, column))
            mGameState[row].setCharAt(column, GameInitialData.FLAG);
        else
            mGameState[row].setCharAt(column, GameInitialData.NOTHING);
    }

    private void manGoAway() {
        restoreOldState(mManPostion.row, mManPostion.column);
    }

    private void moveBoxUp(int row, int column) {
        if (row <= 0) return;
        moveBox(row, column, row - 1, column);
    }

    //把箱子从(srcRow, srcColumn)移动到(destRow, destColumn)
    private void moveBox(int srcRow, int srcColumn, int destRow, int destColumn){
        char cell = mGameState[destRow].charAt(destColumn);
        if (cell  == GameInitialData.NOTHING || cell == GameInitialData.FLAG){
            restoreOldState(srcRow, srcColumn);
            mGameState[destRow].setCharAt(destColumn, GameInitialData.BOX);
        }
    }

    public void goDown() {
        if (mManPostion.row >= mRowNum - 1) return;
        char downCell = mGameState[mManPostion.row + 1].charAt(mManPostion.column);
        if (downCell == GameInitialData.BOX) {
            moveBoxDown(mManPostion.row + 1, mManPostion.column);
            downCell = mGameState[mManPostion.row + 1].charAt(mManPostion.column);
        }
        if (downCell == GameInitialData.NOTHING || downCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.row++;
            mGameState[mManPostion.row].setCharAt(mManPostion.column, GameInitialData.MAN);
        }
    }

    private void moveBoxDown(int row, int column) {
        if (row >= mRowNum - 1) return;
        moveBox(row, column, row + 1, column);
    }

    public void goRight() {
        if (mManPostion.column >= mColumnNum - 1) return;
        char rightCell = mGameState[mManPostion.row].charAt(mManPostion.column + 1);
        if (rightCell == GameInitialData.BOX) {
            moveBoxRight(mManPostion.row, mManPostion.column + 1);
            rightCell = mGameState[mManPostion.row].charAt(mManPostion.column + 1);
        }
//        Log.d("GameData", "goRight(): rightCell=" + rightCell);
        if (rightCell == GameInitialData.NOTHING || rightCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.column++;
            mGameState[mManPostion.row].setCharAt(mManPostion.column, GameInitialData.MAN);
        }
    }

    private void moveBoxRight(int row, int column) {
        if (column >= mColumnNum - 1) return;
        moveBox(row, column, row, column + 1);
    }

    public void goLeft() {
        if (mManPostion.column <= 0) return;
        char leftCell = mGameState[mManPostion.row].charAt(mManPostion.column - 1);
        if (leftCell == GameInitialData.BOX) {
            moveBoxLeft(mManPostion.row, mManPostion.column - 1);
            leftCell = mGameState[mManPostion.row].charAt(mManPostion.column - 1);
        }
        if (leftCell == GameInitialData.NOTHING || leftCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.column--;
            mGameState[mManPostion.row].setCharAt(mManPostion.column, GameInitialData.MAN);
        }
    }

    private void moveBoxLeft(int row, int column) {
        if (column <= 0) return;
        moveBox(row, column, row, column - 1);
    }

    //据所选关卡的初始数据处获取单元格(row, column)是否有红旗
    public boolean hasFlag(int row, int column) {
        for (int i = 0; i < mFlagCells.size(); i++) {
            TCell cell = mFlagCells.get(i);
            if (cell.row == row && cell.column == column)
                return true;
        }
        return  false;
    }

    //所有箱子到达目的地了么？是的话，返回true, 否则返回false。
    public boolean isGameOver() {
        for (int i = 0; i < mFlagCells.size(); i++){
            TCell cell = mFlagCells.get(i);
//            Log.d("GameData", "isGameOver(), Flag " + i + "=(" + cell.row + ", " + cell.column + ")");
            if (mGameState[cell.row].charAt(cell.column) != 'B')
                return false;
        }
        return true;
    }

    public void recordPassed(){

    }
}
