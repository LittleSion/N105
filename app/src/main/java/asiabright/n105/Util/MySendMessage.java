package asiabright.n105.Util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import asiabright.n105.Service.BluetoothLeService;



public class MySendMessage {
	private static final String TAG="MySendMessage";
	private Context context=null;
	public MySendMessage(Context context)
	{
		this.context=context;
	}
	public void sendmessage(String cmd,String para1,String para2,String para3,String para4){
		Log.i(TAG, "cmd:"+cmd);
		Log.i(TAG, "para1:"+para1);
		Log.i(TAG, "para2:"+para2);
		Log.i(TAG, "para3:"+para3);
		Log.i(TAG, "para4:"+para4);
		Intent in=new Intent(context,BluetoothLeService.class);
		in.putExtra("cmd", cmd);
		in.putExtra("para1", para1);
		in.putExtra("para2", para2);
		in.putExtra("para3", para3);
		in.putExtra("para4", para4);
		context.startService(in);
		
	}
}
