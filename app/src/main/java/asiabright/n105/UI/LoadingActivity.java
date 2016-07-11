package asiabright.n105.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import com.litesuits.bluetooth.LiteBluetooth;

import asiabright.n105.R;

import com.litesuits.bluetooth.exception.hanlder.DefaultBleExceptionHandler;


public class LoadingActivity extends Activity implements Runnable {
    private Context mContext;

    private DefaultBleExceptionHandler bleExceptionHandler;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置常亮
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);
        mContext = this;
        initBLE();
        new Thread(this).start();

    }

    private void initBLE() {
        liteBluetooth = getInstance(mContext);
        liteBluetooth.enableBluetooth(this, 1);
        bleExceptionHandler = new DefaultBleExceptionHandler(mContext);

    }

    public void run() {
        try {

            Thread.sleep(3000);

            startActivity(new Intent(mContext, SwitchListActivity.class));

            finish();

        } catch (InterruptedException e) {

        }
    }
}
