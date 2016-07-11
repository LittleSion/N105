//package asiabright.n105.adapte;
//
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import asiabright.c300.CustomView.listview.CustomAdapter;
//import asiabright.n105.R;
//
//
///**
// * Created by zhangmin on 16/1/20.
// */
//@SuppressWarnings("unused")
//public class DeviceListAdapter extends CustomAdapter {
//    private String[] mValues;
//    private LayoutInflater mInflater;
//    private Context mContext;
//
//
//    public DeviceListAdapter(Context context, ListView listView, String[] values) {
//        super(context, listView);
//        mContext = context;
//        mInflater = LayoutInflater.from(context);
//        mValues = values;
//    }
//
//    @Override
//    public int getCount() {
//        return mValues.length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mValues[position];
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getCustomView(int position, View convertView, ViewGroup parent) {
////        ViewHolder holder;
////        if (convertView == null) {
////            holder = new ViewHolder();
////            convertView = mInflater.inflate(R.layout.device_list_item, null);
////
////            holder.title = (TextView) convertView.findViewById(R.id.dev_lisv_tv_name);
////
////            convertView.setTag(holder);
////        } else {
////            holder = (ViewHolder) convertView.getTag();
////        }
////        holder.title.setText(mValues[position]);
//        convertView = mInflater.inflate(R.layout.device_list_item, null);
//        TextView tvTitle = (TextView)convertView.findViewById(R.id.dev_lisv_tv_name);
//        tvTitle.setText(mValues[position]);
//        return convertView;
//    }
//
//
////    static class ViewHolder {
////        public TextView title;
////
////
////    }
//
//}
//
//
