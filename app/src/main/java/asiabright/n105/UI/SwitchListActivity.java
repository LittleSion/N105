package asiabright.n105.UI;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.roamer.slidelistview.SlideListView;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import asiabright.n105.R;
import asiabright.n105.Util.MyList;
import asiabright.n105.Util.MySendMessage;
import asiabright.n105.Util.ReceiveBroadcase;
import asiabright.n105.Util.ReceiveBroadcase.OnReceiveDataListener;
import asiabright.n105.adapte.SwitchListAdapter;
import asiabright.n105.dialog.CheckDialog;
import asiabright.n105.popwindow.Header;


public class SwitchListActivity extends Activity {
    private Header myHeader;
    private Context mContext;
    public static View view;
    private final String TAG = "DeviceListActivity";
    private MySendMessage mySendMessage;
    private ReceiveBroadcase receiveBroadcase;
    private long firstime = 0;
    private ImageButton scanbtn;
    /*适配器*/
    public static SwitchListAdapter mAdapter;
    private SlideListView listView;
    private SharedPreferences sharedPreferencesp;
    private String File = "irsta";

    private boolean zhen_flag,vioce_flag;
    private int i = -1;
    private OnReceiveDataListener onReceiveDataListener = new OnReceiveDataListener() {
        @Override
        public void OnReceive(String cmd, String para1, String para2,
                              String para3, String para4) {
            if ("TJ".equals(cmd)) {// 后台消息，添加开关成功
                mAdapter.notifyDataSetChanged();
            }
            if ("SX".equals(cmd)) {// 后台发来的刷新开关消息
                mAdapter.notifyDataSetChanged();
            }
            if ("SZ".equals(cmd)) {
               if(sharedPreferencesp==null){
                   sharedPreferencesp = getSharedPreferences(File,MODE_PRIVATE);
               }
                Editor editor = sharedPreferencesp.edit();
                editor.putBoolean("zhendong",(para1.equals("zhen_yes")?true:false));
                editor.putBoolean("vioce",(para2.equals("vioce_yes")?true:false));
                editor.commit();
                zhen_flag = (para1.equals("zhen_yes")?true:false);
                vioce_flag = (para2.equals("vioce_yes")?true:false);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_2_switchlist);
        mContext = this;
        mySendMessage = new MySendMessage(mContext);
        receiveBroadcase = new ReceiveBroadcase(mContext);
        receiveBroadcase.onRegister();
        receiveBroadcase.setOnReceiveDataListener(onReceiveDataListener);
        initUI();
        if(sharedPreferencesp==null){
            sharedPreferencesp = getSharedPreferences(File,MODE_PRIVATE);
        }
        zhen_flag = sharedPreferencesp.getBoolean("zhendong",false);
        vioce_flag = sharedPreferencesp.getBoolean("vioce",false);
    }

    private void initUI() {

        initListView();
        initHeader();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        receiveBroadcase.unRegister();
    }

    /**
     * 测试listview的数据
     */
    private void initListView() {
        // dbManager = new DBManager(mContext);
        listView = (SlideListView) findViewById(R.id.listview);
        scanbtn = (ImageButton)findViewById(R.id.scan_btn);
        scanbtn.setOnClickListener(onclickListener);
        mAdapter = new SwitchListAdapter(MyList.settingList, mContext, SwitchListActivity.this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent in = new Intent();
                in.putExtra("pos",position);
                in.putExtra("zhen_flag",zhen_flag);
                in.putExtra("vioce_flag",vioce_flag);
                in.setClass(mContext, ControlActivity.class);
                startActivity(in);

            }

//            @Override
//            public void onDismiss(int[] reverseSortedPositions) {
//                super.onDismiss(reverseSortedPositions);
//                for (int i : reverseSortedPositions) {
//                    mySendMessage.sendmessage("SC", i + "", "", "", "");
//                    // MyList.settingList.remove(i);
//
//                }
//                mAdapter.notifyDataSetChanged();
//
//            }
        });
    }

    private void initHeader() {
        view = findViewById(R.id.activity_subaccount_canvers);
        myHeader = (Header) findViewById(R.id.dev_list_header);
        myHeader.setTitle(this.getString(R.string.SLA_01));
        myHeader.setMenu1(this.getString(R.string.SLA_02),
                R.drawable.btn_sezhi_selector);
        myHeader.setMenu2(this.getString(R.string.SLA_03),
                R.drawable.btn_jianchagengxing_selector);
        myHeader.setMenu3(this.getString(R.string.SLA_04),
                R.drawable.btn_guanyuwomen_selector);
        myHeader.setHeaderOnClickListener(onclickListener);
        myHeader.setLeftButtonVisibility(true);
        myHeader.setRightButtonVisibility(true);
    }

    private OnClickListener onclickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.scan_btn:
                    startActivity(new Intent(mContext, FindDev.class));
                    break;
                case R.id.header_pop_button1:

                    Intent intent = new Intent();
                    intent.putExtra("TYPE","set");
                    intent.setClass(mContext, CheckDialog.class);
                    startActivity(intent);

                    break;
                case R.id.header_pop_button2:
                     UmengUpdateAgent.forceUpdate(mContext);
				UmengUpdateAgent.setUpdateAutoPopup(false);
				UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					@Override
					public void onUpdateReturned(int updateStatus,
							UpdateResponse updateInfo) {
						switch (updateStatus) {
						case UpdateStatus.Yes: // has update
							UmengUpdateAgent.showUpdateDialog(mContext,
									updateInfo);
							break;
						case UpdateStatus.No: // has no update
							Toast.makeText(mContext,
									mContext.getString(R.string.SLA_06),
									Toast.LENGTH_SHORT).show();
							break;
						case UpdateStatus.Timeout: // time out
							Toast.makeText(mContext,
									mContext.getString(R.string.SLA_07),
									Toast.LENGTH_SHORT).show();
							break;
						}
					}
				});
				UmengUpdateAgent.update(mContext);
                    break;
                case R.id.header_pop_button3:
                    Intent in = new Intent();
                    in.setClass(SwitchListActivity.this,AboutActivity.class);
                    startActivity(in);
                    break;
                case R.id.header_right_ivw:
                    mySendMessage.sendmessage("SX", "", "", "", "");

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondtime = System.currentTimeMillis();
            if (secondtime - firstime > 3000) {
                Toast.makeText(mContext, this.getString(R.string.SLA_05),
                        Toast.LENGTH_SHORT).show();
                firstime = System.currentTimeMillis();
                return true;
            } else {
                finish();
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStart() {
        mySendMessage.sendmessage("SX", "", "", "", "");// 发送刷新的指令
        super.onStart();
    }

}
