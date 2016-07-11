package asiabright.n105.dialog;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


import asiabright.n105.R;
import asiabright.n105.Util.MySendMessage;

public class CheckDialog extends Activity {


    private String TAG = "CheckDialog";
    private Button btnsure,btncan;
    private TextView txt1,txt2,txt3,tile;
    private EditText edt1;
    private String type_num;
    private BluetoothDevice bluedev;
    private String ID;
    private Integer rssi;
    private MySendMessage mysendmessage;
    private CheckBox zhen,vioce;
    private SharedPreferences sharedPreferencesp;
    private String File = "setir";

    private Vibrator vibrator;
    private SoundPool sp;
    private int music;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.dlg0200_btn010:
                    if (type_num.equals("add")) {
                        mysendmessage.sendmessage("TJ", bluedev.getAddress(), bluedev.getName(), ID, rssi+"");
                    }
                    if (type_num.equals("chg")) {
                        mysendmessage.sendmessage("XG", edt1.getText().toString(),"","","");

                    }
                    if (type_num.equals("set")) {
                        if(sharedPreferencesp==null){
                            sharedPreferencesp = getSharedPreferences(File,MODE_PRIVATE);
                        }
                        Editor editor = sharedPreferencesp.edit();
                        editor.putBoolean("zhendong",zhen.isChecked());
                        editor.putBoolean("vioce", vioce.isChecked());
                        editor.commit();
                        mysendmessage.sendmessage("SZ",(zhen.isChecked()?"zhen_yes":"zhen_no"),
                                (vioce.isChecked()?"vioce_yes":"vioce_no"),"","");
                    }
                    if (type_num.equals("4")) {


                    }
                    if (type_num.equals("9")) {

                    }
                    if (type_num.equals("7")) {
                        // showDownloadDialog();

                    }
                    if (type_num.equals("5")) {
                        // showDownloadDialog();

                    }
                    if (type_num.equals("6")) {
                        // showDownloadDialog();

                    }
                    if (type_num.equals("8")) {
                        // showDownloadDialog();

                    }
                    if (type_num.equals("10")) {

                    }
                    finish();
                    break;
                case R.id.dlg0200_btn020:
                    finish();
                    break;
                case R.id.zhencheck:
                    if(zhen.isChecked()){
                        vibrator.vibrate(200);
                    }
                    break;
                case R.id.vioce_check:
                    if(vioce.isChecked()){
                      sp.play(music, 1, 1, 0, 0, 1);
                    }
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check_dialog);
        Intent in = getIntent();
        mysendmessage = new MySendMessage(this);
        type_num = in.getStringExtra("TYPE");
        bluedev = (BluetoothDevice)in.getParcelableExtra("OBJ");
        ID = in.getStringExtra("ID");
        rssi = in.getIntExtra("RSSI",0);
        initUI();
        initview(in);
        if(sharedPreferencesp==null){
            sharedPreferencesp = getSharedPreferences(File,MODE_PRIVATE);
        }
        zhen.setChecked(sharedPreferencesp.getBoolean("zhendong",false));
        vioce.setChecked(sharedPreferencesp.getBoolean("vioce",false));


    }

    private void initUI() {
        vibrator = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        sp = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        music = sp.load(this, R.raw.switch_sound, 1);
        btnsure = (Button) findViewById(R.id.dlg0200_btn010);
        btncan = (Button) findViewById(R.id.dlg0200_btn020);
        tile = (TextView) findViewById(R.id.dlg0200_tile);
        txt1 = (TextView) findViewById(R.id.dlg0200_tvw010);
        txt2 = (TextView) findViewById(R.id.dlg0200_tvw011);
        txt3 = (TextView) findViewById(R.id.dlg0200_tvw012);
        edt1 = (EditText)findViewById(R.id.dlg0200_ett010);
        zhen = (CheckBox)findViewById(R.id.zhencheck);
        vioce = (CheckBox)findViewById(R.id.vioce_check);


    }

    private void initview(Intent in) {
        if(type_num.equals("add")){
            String mac_num = in.getStringExtra("MAC");
            txt2.setText(mac_num);
            btnsure.setOnClickListener(onClickListener);
            btncan.setOnClickListener(onClickListener);
        }
        if(type_num.equals("chg")){
            tile.setText("修改备注");
            txt1.setText("新名字:");
            txt2.setVisibility(View.GONE);
            txt3.setVisibility(View.GONE);
            edt1.setVisibility(View.VISIBLE);
            btnsure.setOnClickListener(onClickListener);
            btncan.setOnClickListener(onClickListener);
        }
        if(type_num.equals("set")){
            tile.setText("设置");
            txt1.setText("设置振动:");
            txt2.setText("设置声音:");
            txt3.setVisibility(View.GONE);
            zhen.setVisibility(View.VISIBLE);
            vioce.setVisibility(View.VISIBLE);
            btnsure.setOnClickListener(onClickListener);
            btncan.setOnClickListener(onClickListener);
            zhen.setOnClickListener(onClickListener);
            vioce.setOnClickListener(onClickListener);
        }

    }
}
