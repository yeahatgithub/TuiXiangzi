package yescorp.com.tuixiangzi;

/**
 * Created by 612226 on 2016/6/30.
 */
//游戏区单元格坐标
public class TCell {
    public int row;
    public int column;

    public TCell(){
        row = 0;
        column = 0;
    }

    public TCell(int r, int c){
        row = r;
        column = c;
    }

    public void set(int r, int c){
        row = r;
        column = c;
    }

}
