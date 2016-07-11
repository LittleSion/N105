package asiabright.n105.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import asiabright.n105.R;

public class AboutActivity extends Activity {

    /*----标题控件---*/
    private ImageView title_iv1;
    private TextView title_tv, version_tv;

    private Context mContext;
    private Button button_01, button_02, button_03;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.content_about);
        mContext = this;
        initView();
    }

    private void initUI() {
        initView();
    }

    private void initView() {
        title_iv1 = (ImageView) findViewById(R.id.iv_left);
        title_tv = (TextView) findViewById(R.id.tv_title);
        version_tv = (TextView) findViewById(R.id.version_tv);
        button_01 = (Button) findViewById(R.id.dynamic_news_tmbtn);
        button_02 = (Button) findViewById(R.id.dynamic_news_jdbtn);
        button_03 = (Button) findViewById(R.id.dynamic_news_snbtn);
        title_tv.setText(this.getString(R.string.a4_about_01));
        String ver;
        try {
            ver = getPackageManager().getPackageInfo("com.asiabright.iwns.ui",
                    0).versionName;
            version_tv.setText("V" + ver);
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        title_iv1.setOnClickListener(MyOnClickListener);
        button_01.setOnClickListener(MyOnClickListener);
        button_02.setOnClickListener(MyOnClickListener);
        button_03.setOnClickListener(MyOnClickListener);
    }

    private OnClickListener MyOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            switch (v.getId()) {
                case R.id.iv_left:
                    startActivity(new Intent(mContext, SwitchListActivity.class));
                    finish();
                    break;
                case R.id.dynamic_news_tmbtn:
                    uri = Uri.parse("http://ipurayjiaju.m.tmall.com");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    break;
                case R.id.dynamic_news_jdbtn:
                    uri = Uri.parse("http://ok.jd.com/m/index-45700.html");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    break;
                case R.id.dynamic_news_snbtn:
                    uri = Uri.parse("http://shop70065463.suning.com");
                    startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(mContext, SwitchListActivity.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
