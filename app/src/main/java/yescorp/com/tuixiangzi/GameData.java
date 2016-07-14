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
    private List<GameStepData> mGameSteps = new ArrayList<>();      //记住人走过的每一步（及其箱子的每一次移动）。用以支持“悔一步”操作
    private GameStepData mCurrentStep;

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
        mCurrentStep = null;
        char upCell = mGameState[mManPostion.row - 1].charAt(mManPostion.column);
        if (upCell == GameInitialData.BOX) {
            moveBoxUp(mManPostion.row - 1, mManPostion.column);
            upCell = mGameState[mManPostion.row - 1].charAt(mManPostion.column);
        }

        if (upCell == GameInitialData.NOTHING || upCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.row--;
            mGameState[mManPostion.row].setCharAt(mManPostion.column, GameInitialData.MAN);

            recordMoveInfo(mManPostion.row + 1, mManPostion.column, mManPostion.row, mManPostion.column);
        }
    }

    private void recordMoveInfo(int srcRow, int srcColumn, int destRow, int destColumn) {
        if (mCurrentStep == null)
            mCurrentStep = new GameStepData();
        mCurrentStep.setManPrvPosition(new TCell(srcRow, srcColumn));
        mCurrentStep.setManCurrentPosition(new TCell(destRow, destColumn));
        mGameSteps.add(mCurrentStep);
        logOneStep(mCurrentStep);
    }



    private void restoreInitialState(int row, int column) {
        if (hasFlag(row, column))
            mGameState[row].setCharAt(column, GameInitialData.FLAG);
        else
            mGameState[row].setCharAt(column, GameInitialData.NOTHING);
    }

    private void manGoAway() {
        restoreInitialState(mManPostion.row, mManPostion.column);
        if (GameSound.isSoundAllowed()) GameSound.playOneStepSound();
    }

    private void moveBoxUp(int row, int column) {
        if (row <= 0) return;
        moveBox(row, column, row - 1, column);
    }

    //把箱子从(srcRow, srcColumn)移动到(destRow, destColumn)
    private void moveBox(int srcRow, int srcColumn, int destRow, int destColumn){
        char cell = mGameState[destRow].charAt(destColumn);
        if (cell  == GameInitialData.NOTHING || cell == GameInitialData.FLAG){
            restoreInitialState(srcRow, srcColumn);
            mGameState[destRow].setCharAt(destColumn, GameInitialData.BOX);
            if (mCurrentStep == null)
                mCurrentStep = new GameStepData();
            mCurrentStep.setBoxPrvPosition(new TCell(srcRow, srcColumn));
            mCurrentStep.setBoxCurrentPosition(new TCell(destRow, destColumn));
            if (GameSound.isSoundAllowed()) GameSound.playMoveBoxSound();
        }
    }

    public void goDown() {
        if (mManPostion.row >= mRowNum - 1) return;
        mCurrentStep = null;
        char downCell = mGameState[mManPostion.row + 1].charAt(mManPostion.column);
        if (downCell == GameInitialData.BOX) {
            moveBoxDown(mManPostion.row + 1, mManPostion.column);
            downCell = mGameState[mManPostion.row + 1].charAt(mManPostion.column);
        }
        if (downCell == GameInitialData.NOTHING || downCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.row++;
            mGameState[mManPostion.row].setCharAt(mManPostion.column, GameInitialData.MAN);

            recordMoveInfo(mManPostion.row - 1, mManPostion.column, mManPostion.row, mManPostion.column);
        }
    }

    //TODO: goDown(), goRight(), goLeft(), goUp()的函数代码类似，抽取类似代码，做成一个子函数。在子函数内部，记录人和箱子的移动信息。
    // 上述做法的目的是，去除mCurrentSetp这个“全局变量”。原因是好多地方都读/写了这个变量，代码变得难以维护。
    //TODO: moveBoxDown(), moveBoxUp(), moveBoxLeft(), moveBoxRight()返回真假值，真值代表移动了箱子；假值代表没有移动箱子
    public void goRight() {
        if (mManPostion.column >= mColumnNum - 1) return;
        mCurrentStep = null;
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

            recordMoveInfo(mManPostion.row, mManPostion.column - 1, mManPostion.row, mManPostion.column);
        }
    }

    private void moveBoxDown(int row, int column) {
        if (row >= mRowNum - 1) return;
        moveBox(row, column, row + 1, column);
    }

    private void moveBoxRight(int row, int column) {
        if (column >= mColumnNum - 1) return;
        moveBox(row, column, row, column + 1);
    }

    public void goLeft() {
        if (mManPostion.column <= 0) return;
        mCurrentStep = null;
        char leftCell = mGameState[mManPostion.row].charAt(mManPostion.column - 1);
        if (leftCell == GameInitialData.BOX) {
            moveBoxLeft(mManPostion.row, mManPostion.column - 1);
            leftCell = mGameState[mManPostion.row].charAt(mManPostion.column - 1);
        }
        if (leftCell == GameInitialData.NOTHING || leftCell == GameInitialData.FLAG){
            manGoAway();
            mManPostion.column--;
            mGameState[mManPostion.row].setCharAt(mManPostion.column, GameInitialData.MAN);

            recordMoveInfo(mManPostion.row, mManPostion.column + 1, mManPostion.row, mManPostion.column);
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

    //TODO: 悔一步
    //    走第一步后，悔一步，执行成功。
    //    连续走多步，悔一步后出错了。
    public boolean undoMove(){
        if (mGameSteps.isEmpty())
            return false;
//        GameStepData step = mGameSteps.remove(0);
        GameStepData step = mGameSteps.remove(mGameSteps.size() - 1);
        logUndoOneStep(step);
        assert(mManPostion.isEqualTo(step.getManCurrentPosition()));
        restoreInitialState(step.getManCurrentPosition().row, step.getManCurrentPosition().column);
        int manRow = step.getManPrvPosition().row;
        int manColumn = step.getManPrvPosition().column;
        mManPostion.set(manRow, manColumn);
        mGameState[manRow].setCharAt(manColumn, 'M');
        TCell boxPrvPos = step.getBoxPrvPosition();
        TCell boxCurPos = step.getBoxCurrentPosition();
        if (boxPrvPos != null && boxCurPos != null){
            //assert mGameState[boxCurPos.row].charAt(boxCurPos.column) == 'B';
            restoreInitialState(boxCurPos.row, boxCurPos.column);
            mGameState[boxPrvPos.row].setCharAt(boxPrvPos.column, 'B');
        }
        return true;
    }

    private void logUndoOneStep(GameStepData step) {
        logOneSetp(step.getManCurrentPosition(), step.getManPrvPosition(), step.getBoxCurrentPosition(), step.getBoxPrvPosition());
    }

    private void logOneStep(GameStepData step) {
        TCell manPrvPos = step.getManPrvPosition();
        TCell manCurPos = step.getManCurrentPosition();
        TCell boxPrvPos = step.getBoxPrvPosition();
        TCell boxCurPos = step.getBoxCurrentPosition();
        logOneSetp(manPrvPos, manCurPos, boxPrvPos, boxCurPos);
    }

    private void logOneSetp(TCell manPrvPos, TCell manCurPos, TCell boxPrvPos, TCell boxCurPos) {
        Log.d("GameData", "一步：(" + manPrvPos.row + ", " + manPrvPos.column + ") -> (" + manCurPos.row + ", " + manCurPos.column + ")" );
        if (boxPrvPos != null && boxCurPos != null) {
            Log.d("GameData", "箱子：(" + boxPrvPos.row + ", " + boxPrvPos.column + ") -> (" + boxCurPos.row + ", " + boxCurPos.column + ")" );
        }
    }
}
