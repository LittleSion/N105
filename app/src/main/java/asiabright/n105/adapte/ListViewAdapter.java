package asiabright.n105.adapte;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asiabright.n105.R;
import asiabright.n105.Util.Utils;

/**
 * Created by zhangmin on 16/4/26.
 */
public class ListViewAdapter extends BaseAdapter {
    private Context context;
    public ArrayList<BluetoothDevice> mLeDevices;
    public Map<String,Integer> myRssi;
    public Map<String,String> myID;


    public ListViewAdapter(Context context) {
        this.context = context;
        mLeDevices = new ArrayList<>();
        myID = new HashMap<>();
        myRssi = new HashMap<>();
    }
    @Override
    public int getCount() {
        return mLeDevices.size();
    }
    public void addDevice(BluetoothDevice device,int rssi,String[] str) {

        if (!mLeDevices.contains(device)) {
            myRssi.put(device.getAddress(),rssi);
            myID.put(device.getAddress(), Utils.getSerial(str));
            mLeDevices.add(device);
        } else {
            myRssi.put(device.getAddress(),rssi);
            myID.put(device.getAddress(), Utils.getSerial(str));
        }
    }

    @Override
    public Object getItem(int position) {
        return mLeDevices.get(position).getAddress();
    }
    public void clear() {
        mLeDevices.clear();

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item,parent,false);
            holder = new ViewHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.list_item_ble);
            holder.tv_id = (TextView) convertView.findViewById(R.id.list_item_id);
            holder.tv_mac = (TextView) convertView.findViewById(R.id.list_item_mac);
            holder.tv_rssi = (TextView) convertView.findViewById(R.id.list_item_rssi);
            convertView.setTag(holder);   //将Holder存储到convertView中
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(mLeDevices.get(position).getName());
        holder.tv_mac.setText(mLeDevices.get(position).getAddress());
        holder.tv_id.setText(myID.get(mLeDevices.get(position).getAddress()));
        holder.tv_rssi.setText(myRssi.get(mLeDevices.get(position).getAddress())+"");
        return convertView;
    }
    static class ViewHolder{
        TextView tv_name;
        TextView tv_mac;
        TextView tv_id;
        TextView tv_rssi;
    }
}
