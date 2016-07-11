package asiabright.n105.UI;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import com.litesuits.bluetooth.LiteBluetooth;
import com.litesuits.bluetooth.scan.PeriodScanCallback;

//import asiabright.n105.adapte.DevRecyclerAdapter;


import asiabright.n105.R;
import asiabright.n105.Util.MyList;
import asiabright.n105.Util.Utils;
import asiabright.n105.adapte.ListViewAdapter;
import asiabright.n105.Util.ReceiveBroadcase;
import asiabright.n105.Util.ReceiveBroadcase.OnReceiveDataListener;
import asiabright.n105.dialog.CheckDialog;

public class FindDev extends AppCompatActivity {



    ImageView i_left,i_right;
    Animation rimageAnimEnter;
    TextView title_txt,add_txt;
//    String ID;
    private ReceiveBroadcase receiveBroadcase;

    private ListViewAdapter adapter;
    private ListView listItem;
    private static int TIME_OUT_SCAN = 10000;


    /**
     * lite单例模式
     */
    private volatile static LiteBluetooth liteBluetooth;

    public static LiteBluetooth getInstance(Context context) {
        if (liteBluetooth == null) {
            synchronized (LiteBluetooth.class) {
                if (liteBluetooth == null) {
                    liteBluetooth = new LiteBluetooth(context);
                }
            }
        }
        return liteBluetooth;
    }



    private OnReceiveDataListener onReceiveDataListener = new OnReceiveDataListener() {
        @Override
        public void OnReceive(String cmd, String para1, String para2,
                              String para3, String para4) {
            if(cmd.equals("TJ")){
                finish();
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_find_dev);
        receiveBroadcase = new ReceiveBroadcase(this);
        receiveBroadcase.onRegister();
        receiveBroadcase.setOnReceiveDataListener(onReceiveDataListener);
        initData();
        initView();
        initadapter();

    }

    private void initadapter() {

        listItem = (ListView) findViewById(R.id.list_item);
        adapter = new ListViewAdapter(this);
        listItem.setAdapter(adapter);
        listItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent();
                in.putExtra("MAC",adapter.getItem(position).toString());
                in.putExtra("TYPE", "add");
                in.putExtra("ID",adapter.myID.get(adapter.mLeDevices.get(position).getAddress()));
                in.putExtra("RSSI",adapter.myRssi.get(adapter.mLeDevices.get(position).getAddress()));
                Bundle bundle= new Bundle();
                bundle.putParcelable("OBJ", adapter.mLeDevices.get(position));
                in.putExtras(bundle);
                in.setClass(FindDev.this, CheckDialog.class);
                startActivity(in);
                }
        });
    }

    private void initView() {

        rimageAnimEnter = AnimationUtils.loadAnimation(this,
                R.anim.addbtn_rotate_enter_r);
        i_left = (ImageView) findViewById(R.id.iv_left);
        i_left.setOnClickListener(onclickListener);
//        i_right = (ImageView) findViewById(R.id.iv_right);
//        i_right.setImageResource(R.mipmap.ic_shuaxin);
//        i_right.setVisibility(View.VISIBLE);
//        i_right.setOnClickListener(onclickListener);
        add_txt = (TextView)findViewById(R.id.tv_right);
        add_txt.setText("扫描");
        add_txt.setVisibility(View.VISIBLE);
        add_txt.setOnClickListener(onclickListener);
        title_txt = (TextView) findViewById(R.id.tv_title);
        title_txt.setText("扫描列表");
    }


    private View.OnClickListener onclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.iv_left:
                    finish();
                    break;
                case R.id.tv_right:
                   // i_right.startAnimation(rimageAnimEnter);
                    Toast.makeText(FindDev.this,"扫描成功",Toast.LENGTH_SHORT).show();
                    scanDevicesPeriod();
                    break;
                default:
                    break;
            }
        }
    };


    //搜索蓝牙设备
    private void scanDevicesPeriod() {
        liteBluetooth.startLeScan(new PeriodScanCallback(TIME_OUT_SCAN) {
            @Override
            public void onScanTimeout() {
                // dialogShow(TIME_OUT_SCAN + " Millis Scan Timeout! ");
            }

            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//                    ID = Utils.getSerial(Utils.byte2hex(scanRecord));
                int i;
                for( i =0;i< MyList.settingList.size();i++){
                    if(MyList.settingList.get(i).getBlueMac().equals(device.getAddress())){
                        break;
                    }
                }
                    if(i==MyList.settingList.size()) {
                        adapter.addDevice(device, rssi, Utils.byte2hex(scanRecord));
                    }
                    adapter.notifyDataSetChanged();
            }
        });
    }

    //打开蓝牙
    private void initData() {

        liteBluetooth = getInstance(this);
        liteBluetooth.enableBluetooth(this, 1);
        scanDevicesPeriod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        receiveBroadcase.unRegister();
    }
}


