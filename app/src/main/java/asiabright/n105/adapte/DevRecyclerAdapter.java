//package asiabright.n105.adapte;
//
//import android.bluetooth.BluetoothDevice;
//import android.content.Context;
//import android.support.v7.widget.RecyclerView.Adapter;
//import android.support.v7.widget.RecyclerView.ViewHolder;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import asiabright.n105.Util.Utils;
//import asiabright.n105.R;
//
///**
// * Created by 叫我官人 on 2016/4/12.
// */
//public class DevRecyclerAdapter extends Adapter<DevRecyclerAdapter.ItemViewHolder> {
//    private static final int TYPE_ITEM = 0;
//    private static final int TYPE_FOOTER = 1;
//    private Context context;
//    private ArrayList<BluetoothDevice> mLeDevices;
//    private ArrayList<Integer> rssis;
//    private String serialNum;
//    private Map<String, Integer> mDevRssiValues;
//    private Map<String, String> mDevSerValues;
//
//    public DevRecyclerAdapter(Context context) {
//        this.context = context;
//        rssis = new ArrayList<>();
//        mLeDevices = new ArrayList<>();
//        mDevRssiValues = new HashMap<>();
//        mDevSerValues = new HashMap<>();
//    }
//
//    public void addDevice(BluetoothDevice device, int rssi, String[] str) {
//
//        if (!mLeDevices.contains(device)) {
//            mDevRssiValues.put(device.getAddress(), rssi);
//            mDevSerValues.put(device.getAddress(), Utils.getSerial(str));
//            mLeDevices.add(device);
//        } else {
//            mDevRssiValues.put(device.getAddress(), rssi);
//            mDevSerValues.put(device.getAddress(), Utils.getSerial(str));
//        }
//    }
//
//    public String getSerialNum(String mDeviceAddress) {
//        return mDevSerValues.get(mDeviceAddress).intern();
//    }
//
//    public void clear() {
//        mLeDevices.clear();
//        rssis.clear();
//    }
//
//    public BluetoothDevice getDevice(int position) {
//        BluetoothDevice devPosition = null;
//        if (mLeDevices.size() != 0) {
//            devPosition = mLeDevices.get(position);
//        }
//        return  devPosition;
//
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(View view, int position);
//
//        void onItemLongClick(View view, int position);
//    }
//
//    private OnItemClickListener onItemClickListener;
//
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
//
//    @Override
//    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_cardview_device, parent,
//                false);
//        return new ItemViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(final ItemViewHolder holder, int position) {
//        if (holder instanceof ItemViewHolder) {
//            BluetoothDevice device = mLeDevices.get(position);
//            final String deviceName = device.getName();
//            if (deviceName != null && deviceName.length() > 0) {
//                byte rssival = (byte) mDevRssiValues.get(device.getAddress()).intValue();
//                String serialVal = mDevSerValues.get(device.getAddress()).intern();
//                holder.devProductTxt.setText(context.getString(R.string.recycler_product_name) + device.getName());
//                holder.devSerialTxt.setText(context.getString(R.string.recycler_serial_name) + serialVal);
//                holder.devRssiTxt.setText(rssival + "");
//            }
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = holder.getLayoutPosition();
//                    onItemClickListener.onItemClick(holder.itemView, position);
//                }
//            });
//
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    int position = holder.getLayoutPosition();
//                    onItemClickListener.onItemLongClick(holder.itemView, position);
//                    return false;
//                }
//            });
//        }
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mLeDevices.size();
//    }
//
//    static class ItemViewHolder extends ViewHolder {
//        TextView devProductTxt;
//        TextView devSerialTxt;
//        TextView devRssiTxt;
//
//        public ItemViewHolder(View itemView) {
//            super(itemView);
//            devProductTxt = (TextView) itemView.findViewById(R.id.position);
//            devSerialTxt = (TextView) itemView.findViewById(R.id.serial);
//            devRssiTxt = (TextView) itemView.findViewById(R.id.rssival);
//        }
//    }
//}
