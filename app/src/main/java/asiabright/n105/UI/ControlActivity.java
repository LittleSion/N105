package asiabright.n105.UI;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;


import asiabright.n105.UI.Fragment.N105Second;
import asiabright.n105.Util.MyList;
import asiabright.n105.Util.ReceiveBroadcase;
import asiabright.n105.Util.ReceiveBroadcase.OnReceiveDataListener;
import asiabright.n105.Util.MySwitch;
import asiabright.n105.db.DBManager;
import asiabright.n105.dialog.CheckDialog;
import asiabright.n105.UI.Fragment.N105First;
import asiabright.n105.Util.ForBtnClickListener;
import asiabright.n105.UI.Fragment.N105Second;
import asiabright.n105.R;

public class ControlActivity extends Activity implements ForBtnClickListener {

    private BluetoothDevice mDevice;
    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattService service;
    private boolean zhen_flag,vioce_flag;
    private Vibrator vibrator;
    private SoundPool sp;
    private int music;
    public static BluetoothGatt mBluetoothGatt;


    private N105First n105First;
    private N105Second n105Second;


    private int pos;
    private String modle;

    public static BluetoothGattCharacteristic characteristicforwrite;
    private ImageView left_iv;
    private TextView right_tv;
    private TextView title_tv;
    private MySwitch mySwitch;

    private ReceiveBroadcase receiveBroadcase;

    private DBManager dbManager;
    private String btnsta = "64646464";
    private String btnone = "64";
    private String btntwo = "64";
    private String btnthe = "64";
    private String btnfor = "64";

    ProgressDialog p1 ;


    private OnReceiveDataListener onReceiveDataListener = new OnReceiveDataListener() {
        @Override
        public void OnReceive(String cmd, String para1, String para2,
                              String para3, String para4) {
            if(cmd.equals("XG")){
                MyList.settingList.get(pos).setSwitchName(para1);
                dbManager.setMySwitchList();
                title_tv.setText(para1);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();

        receiveBroadcase = new ReceiveBroadcase(ControlActivity.this);
        receiveBroadcase.onRegister();
        receiveBroadcase.setOnReceiveDataListener(onReceiveDataListener);

        dbManager = new DBManager(this);


        Intent intent = getIntent();
        pos = intent.getIntExtra("pos",0);
        zhen_flag = intent.getBooleanExtra("zhen_flag", false);
        vioce_flag = intent.getBooleanExtra("vioce_flag",false);
        mySwitch = MyList.settingList.get(pos);
        initBLE();
        initview();
        initfragment();


    }

    private void initfragment() {
        modle = mySwitch.getModle();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        switch (modle){
            case "c300":
                if(n105First==null) {
                    n105First = new N105First();
                    n105First.setForBtnClickListener(this);
                }
                transaction.replace(R.id.contorl_fragment,n105First);
                break;
            case "c200":
                if(n105Second == null) {
                    n105Second = new N105Second();
                    n105Second.setForBtnClickListener(this);
                }
                transaction.replace(R.id.contorl_fragment, n105Second);

        }
        transaction.commit();
    }

    private void initview() {
        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        music = sp.load(this, R.raw.switch_sound, 1);
        title_tv = (TextView)findViewById(R.id.tv_title);
        right_tv = (TextView)findViewById(R.id.tv_right);
        right_tv.setVisibility(View.VISIBLE);
        left_iv = (ImageView)findViewById(R.id.iv_left);

        title_tv.setText(mySwitch.getSwitchName());
        right_tv.setText("修改备注");



        left_iv.setOnClickListener(onClick);
        right_tv.setOnClickListener(onClick);


    }


    private View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.iv_left:
                    finish();
                    break;
                case R.id.tv_right:
                    Intent intent = new Intent();
                    intent.putExtra("TYPE","chg");
                    intent.putExtra("name",mySwitch.getSwitchName());
                    intent.setClass(ControlActivity.this,CheckDialog.class);
                    startActivity(intent);
                    // i_right.startAnimation(rimageAnimEnter);

                    break;
                default:
                    break;
            }
        }
    };

    private void initBLE() {
        scanAndConnect(mySwitch.getBlueMac());

    }

    private void initdailog(String tail) {
        switch (tail) {
            case "connect":
                p1 = new ProgressDialog(ControlActivity.this);
                p1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                p1.setTitle("连接设备");
                p1.setMessage("设备连接中，请稍候...");
                p1.setIndeterminate(false);
                p1.setCancelable(true);
                if (p1 != null) {
                    p1.show();
                }
                break;

            default:
                break;

        }
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    discoverservice();
                    break;
                case 2:
                    p1.dismiss();
                    service = mBluetoothGatt.getService(UUID_SERVER);
                    characteristicforwrite = service.getCharacteristic(UUID_KEY_DATA);
                    break;
            }
        }
    };

    private void discoverservice (){

        if (mBluetoothGatt != null) {
            mBluetoothGatt.discoverServices();

        }

    }
    /**
     * scan and connect to device
     */
    private void scanAndConnect(String mac) {

        mDevice = mBluetoothAdapter.getRemoteDevice(mac);//获取远程设备 蓝牙地址
        //获取 BluetoothGatt
        mBluetoothGatt = mDevice.connectGatt(this, false,//connectGatt 连接GATT【获取mBluetoothGatt！！】
                mBluetoothGattCallback);//回调mBluetoothGattCallback
        initdailog("connect");

    }
    public static final UUID UUID_SERVER = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    public static final UUID UUID_KEY_DATA = UUID.fromString("0000fff6-0000-1000-8000-00805f9b34fb");

    /**
     * write data to characteristic
     */
    public static void write(final String data) {


        characteristicforwrite.setValue(data);
        mBluetoothGatt.writeCharacteristic(characteristicforwrite);//writeString里的数据正式发送

    }
//    public static void read() {
//
//        mBluetoothGatt.readCharacteristic(characteristicfornotify);
//
//    }


    //回调
    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

        public void onConnectionStateChange(BluetoothGatt gatt, int status,//连接状态
                                            int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    showMessage("BLE Connect !!!");
                    mHandler.sendEmptyMessage(1);
                    initdailog("discover");
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    showMessage("BLE DisConnect !!!");
                    mHandler.sendEmptyMessage(2);
                }
            }
        };

        public void onServicesDiscovered(BluetoothGatt gatt, int status) {  //发现服务
            if ((status == BluetoothGatt.GATT_SUCCESS)
                    && (mBluetoothGatt.getService(UUID_SERVER) != null)) {
                showMessage("Discover services Successful");
                mHandler.sendEmptyMessage(2);
            }
        };

        public void onCharacteristicWrite(BluetoothGatt gatt,               //特征发送
                                          BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                showMessage("Characteristic write successful!");
            }
        };


        //	====================================================================
        public void onCharacteristicRead(BluetoothGatt gatt,                   //读取特征
                                         BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS){
//                showMessage("Characteristic read successful!");
//                byte a[] = characteristic.getValue();
//                String t = new String(a);//bytep[]转换为String
                //	mintent.putExtra(YY, a);
                //	String read = characteristic.getStringValue(gatt);
                //	showMessage(read);
            }
        };
        //  ====================================================================

    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
        receiveBroadcase.unRegister();
        Log.e("退出了配置", "退出了配置");
    }


    private void showMessage(final String msg) { //更新UI

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(ControlActivity.this, msg, Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void ContirlIv(){
        if(zhen_flag){
            vibrator.vibrate(200);
        }
        if(vioce_flag){
            sp.play(music, 1, 1, 0, 0, 1);
        }

    }

    @Override
    public void BtnClick(View view) {
        switch (view.getId()){
            case R.id.btnone:
                ContirlIv();
                if(btnsta.substring(0,2).equals("65")){
                    write(":K002000000FC00000;");
                    Log.d("***************************", "fragment1关");
                    btnone = "64";
                    btnsta = btnone+btntwo+btnthe+btnfor;
                    if(n105First!=null){
                        n105First.setButtonsta(btnsta);
                    }
                    if(n105Second!=null){
                        n105Second.setButtonsta(btnsta);
                    }
                }else if(btnsta.substring(0,2).equals("64")){
                    write(":K002000000FC00000;");
                    Log.d("***************************", "fragment1开");
                    btnone = "65";
                    btnsta = btnone+btntwo+btnthe+btnfor;
                    if(n105First!=null) {
                        n105First.setButtonsta(btnsta);
                    }
                    if(n105Second!=null){
                        n105Second.setButtonsta(btnsta);
                    }
                }
                break;
            case R.id.btntwo:
                ContirlIv();
                if(btnsta.substring(2,4).equals("65")){
                    write(":K002000000FC00000;");
                    Log.d("***************************", "fragment2关");
                    btntwo = "64";
                    btnsta = btnone+btntwo+btnthe+btnfor;
                    if(n105Second!=null) {
                        n105Second.setButtonsta(btnsta);
                    }
                }else if(btnsta.substring(2,4).equals("64")){
                    write(":K002000000FC00000;");
                    Log.d("***************************", "fragment2开");
                    btntwo = "65";
                    btnsta = btnone+btntwo+btnthe+btnfor;
                    if(n105Second!=null) {
                        n105Second.setButtonsta(btnsta);
                    }
                }
                break;

        }
    }
}
