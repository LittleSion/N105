package asiabright.n105.Util;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import asiabright.n105.Service.BluetoothLeService;

public class ReceiveBroadcase extends BroadcastReceiver {
    private static final String TAG = "ReceiveBroadcase";
    private Context context;
    OnReceiveDataListener onReceiveDataListener;
    IntentFilter mIntentFilter;

    public ReceiveBroadcase(Context context) {
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context != null) {
            String action = intent.getAction();
            if (action.equals(BluetoothLeService.SERVICEACTION)) {
                String cmd = intent.getStringExtra("cmd");
                String para1 = intent.getStringExtra("para1");
                String para2 = intent.getStringExtra("para2");
                String para3 = intent.getStringExtra("para3");
                String para4 = intent.getStringExtra("para4");
//				Log.i(TAG+":RECEIVE", "cmd:"+cmd);
//				Log.i(TAG+":RECEIVE", "para1:"+para1);
//				Log.i(TAG+":RECEIVE", "para2:"+para2);
//				Log.i(TAG+":RECEIVE", "para3:"+para3);
//				Log.i(TAG+":RECEIVE", "para4:"+para4);
                if (onReceiveDataListener != null)
                    onReceiveDataListener.OnReceive(cmd, para1, para2, para3, para4);
            }
        }
    }

    private IntentFilter getmIntentFilter() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BluetoothLeService.SERVICEACTION);
        return mIntentFilter;
    }

    public void onRegister() {
        context.registerReceiver(this, getmIntentFilter());
    }

    public void unRegister() {
        context.unregisterReceiver(this);
    }

    public void setOnReceiveDataListener(OnReceiveDataListener onReceiveDataListener) {
        this.onReceiveDataListener = onReceiveDataListener;
    }

    public interface OnReceiveDataListener {

        void OnReceive(String cmd, String para1, String para2, String para3, String para4);
    }

}

