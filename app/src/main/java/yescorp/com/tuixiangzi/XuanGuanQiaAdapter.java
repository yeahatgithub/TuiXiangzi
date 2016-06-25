package yescorp.com.tuixiangzi;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Created by 612226 on 2016/6/25.
 */
public class XuanGuanQiaAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private String[] mGuanQiaList;

//    public XuanGuanQiaAdapter(Context context, int resource) {
//        super(context, resource);
//    }
//
//    public XuanGuanQiaAdapter(Context context, int resource, int textViewResourceId) {
//        super(context, resource, textViewResourceId);
//    }

    public XuanGuanQiaAdapter(Context context, int resource, String[] guanQiaList) {
        super(context, resource, guanQiaList);
        mContext = context;
        mGuanQiaList = guanQiaList;
    }

    @Override
    public String getItem(int position) {
        return mGuanQiaList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvGQ = null;
        if (convertView == null)
            tvGQ = new TextView(mContext);
        else
            tvGQ = (TextView) convertView;
        tvGQ.setText(mGuanQiaList[position]);
        tvGQ.setGravity(Gravity.CENTER);
        tvGQ.setBackgroundResource(R.drawable.gv_guanqia_item_tv_border);
        tvGQ.setPadding(10, 10, 10, 10);
        tvGQ.setTextSize(18f);
        return  tvGQ;
    }


}
