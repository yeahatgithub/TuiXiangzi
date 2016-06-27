package yescorp.com.tuixiangzi;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class XuanGuanQiaActivity extends Activity {

    public static final String GUAN_QIA = "GuanQia";

    public final  static String [] GUANQIA_LIST = {"第1关", "第2关", "第3关",
            "第4关", "第5关", "第6关",
            "第7关", "第8关", "第9关",
            "第10关", "第11关", "第12关"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_xuan_guan_qia);

        XuanGuanQiaAdapter xgqAdapter = new XuanGuanQiaAdapter(this, R.layout.item_guan_qia_gridview, GUANQIA_LIST);
        GridView gv_GuanQia = (GridView) findViewById(R.id.gv_xuan_guan_qia);
        gv_GuanQia.setAdapter(xgqAdapter);
//        gv_GuanQia.setOnItemSelectedListener(new GuanQia_ItemSelectedListener());
        gv_GuanQia.setOnItemClickListener(new GV_ItemClickListener());
    }


    private class GuanQia_ItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int itemIndex, long l) {
            Toast.makeText(XuanGuanQiaActivity.this, "选中了第" + itemIndex + "关", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class GV_ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
            int gq = itemIndex + 1;
//            Toast.makeText(XuanGuanQiaActivity.this, "选中了第" + gq + "关", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(XuanGuanQiaActivity.this, GameActivity.class);
            intent.putExtra(GUAN_QIA, gq);
            startActivity(intent);
        }
    }
}
