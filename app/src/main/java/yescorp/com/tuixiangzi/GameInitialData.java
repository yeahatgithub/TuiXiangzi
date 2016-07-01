package yescorp.com.tuixiangzi;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by 612226 on 2016/6/28.
 */
public class GameInitialData {
//    private int Board_Column_Num = 12;    //游戏区规格，默认为12x12
//    private int Board_Row_Num = 12;
    public static ArrayList<String[]> GameLevels = new ArrayList<>();

    //游戏区单元格放了什么
    public static final char NOTHING = ' ';         //该单元格啥也没有
    public static final char BOX = 'B';             //该单元格放的是箱子
    public static final char FLAG = 'F';            //红旗，表示箱子的目的地
    public static final char MAN = 'M';              //推箱子的人
    public static final char WALL = 'W';             //墙

    //游戏区域的配置信息
    public static final String COLUMN_NUM_LABEL = "Number_of_Columns";
    public static final String ROW_NUM_LABEL = "Number_of_Rows";

    public static final String [] LEVEL_1 = {
            "  WWWW ",
            "  WF W ",
            "  WB W ",
            "  WM W ",
            "  WWWW ",
            "       ",
            "       "
    };
    public static final String [] LEVEL_2 = {
            "            ",
            "            ",
            "  WWWWWWW   ",
            "  W FFB W   ",
            "  W W B W   ",
            "  W W W W   ",
            "  W BMW W   ",
            "  WFB   W   ",
            "  WFWWWWW   ",
            "  WWW       ",
            "            ",
            "            "
    };

//    public GameInitialData() {
//        GameLevels.add(LEVEL_1);
//        GameLevels.add(LEVEL_2);
//    }

    public static void addInitGameData(){
        GameLevels.add(LEVEL_1);
        GameLevels.add(LEVEL_2);
    }

//    public GameInitialData(Context context, String confgFileName) throws IOException {
//        try {
//            InputStreamReader inputReader = null;
//            inputReader = new InputStreamReader(context.getResources().getAssets().open(confgFileName) );
//            BufferedReader bufReader = new BufferedReader(inputReader);
//            readConfig(bufReader);
//        } catch (IOException e) {
//            Toast.makeText(context, "无法打开配置文件，程序不得不退出。", Toast.LENGTH_LONG).show();
//            throw e;
//        }
//    }

  /*  private void readConfig(BufferedReader bufReader) throws IOException {
            String line = "";
            while ((line = bufReader.readLine()) != null) {

                line = line.trim();
                if (line.length() < 3) return;   //该行内容无效
                if (line.charAt(0) == '\\' && line.charAt(1) == '\\') return;  //注释行
                if (line.charAt(0) == '[' && line.charAt(line.length() - 1) == ']') {
                    String label = line.substring(1, line.length() - 1);
                    if (label.equalsIgnoreCase(COLUMN_NUM_LABEL)) {
                        String strNum = "";
                        strNum = bufReader.readLine();
                        if (strNum == null) throw new IOException("无法从配置文件读到游戏区的列数。");
                        Board_Column_Num = Integer.parseInt(strNum);
                        return;
                    }
                    if (label.equalsIgnoreCase(ROW_NUM_LABEL)) {
                        String strNum = "";
                        strNum = bufReader.readLine();
                        if (strNum == null) throw new IOException("无法从配置文件读到游戏区的行数。");
                        Board_Row_Num = Integer.parseInt(strNum);
                        return;
                    }
                    if (Character.isDigit(label.charAt(0))) {       //关卡
                        int level = Integer.parseInt(label);
                        String[] gameLevel = new String[Board_Row_Num];
                        for (int r = 0; r < Board_Row_Num; r++) {
                            gameLevel[r] = bufReader.readLine();
                            if (gameLevel[r] == null)
                                throw new IOException("配置文件中，第" + level + "关的行数不足。");
                            if (gameLevel[r].length() < Board_Column_Num)
                                throw new IOException("配置文件中，第" + level + "关第" + r + "行的列数不足。");
                        }
                    }
                }
            }
    }*/

//    public int getBoard_Column_Num() {
//        return Board_Column_Num;
//    }
//
//    public int getBoard_Row_Num() {
//        return Board_Row_Num;
//    }

    public ArrayList<String[]> getGameLevels() {
        return GameLevels;
    }


}
