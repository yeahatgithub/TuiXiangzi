package yescorp.com.tuixiangzi;

/**
 * Created by 612226 on 2016/6/28.
 */
public class GameData {
    private GameInitialData mInitialData;
    private int mLevel;
    private String[] mGameState;

    //TODO: 定义一个12x12的矩阵，用于存储推箱子游戏区状态 -- 定义一个游戏区状态数据类GameData
    //每当箱子或搬运工移动，就修改游戏局面；
    //当箱子全部到达目的地，就判别得出通过游戏关卡。

    public GameData(int level){
        mInitialData = new GameInitialData();
        mLevel = level;   //level从1开始计数
        mGameState = new String[mInitialData.getBoard_Row_Num()];
        String [] initialData = mInitialData.getGameLevels().get(level - 1);
        for (int r = 0; r < mInitialData.getBoard_Row_Num(); r++)
            mGameState[r] = initialData[r];
    }

    public String[] getGameState() {
        return mGameState;
    }

    public int getBoardColumnNum(){
        return mInitialData.getBoard_Column_Num();
    }

    public int getBoardRowNum(){
        return mInitialData.getBoard_Row_Num();
    }
}
