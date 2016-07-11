package asiabright.n105.adapte;

import com.roamer.slidelistview.SlideBaseAdapter;
import com.roamer.slidelistview.SlideListView.SlideMode;

import java.util.List;
import java.util.ArrayList;

import asiabright.n105.Util.MyList;
import asiabright.n105.Util.MySendMessage;
import asiabright.n105.Util.MySwitch;
import asiabright.n105.db.DBManager;
import asiabright.n105.popwindow.DeletePopupWindow;
import asiabright.n105.R;


import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SwitchListAdapter extends SlideBaseAdapter {
	private List<MySwitch> switchList;
	private Context mContext;
	private Activity activity;
	private String TAG = "SwitchListAdapter";
	private MySwitch mySwitch;
	private DeletePopupWindow deletePopupWindow;
	private DBManager dbManager;



	public SwitchListAdapter(List<MySwitch> switchList,Context mContext,Activity activity) {
		super(mContext);
		if (switchList == null) {
			switchList = new ArrayList<MySwitch>();
		}
		this.switchList = switchList;
		this.mContext = mContext;
		this.activity=activity;




	}



	public void changeSwitchList(List<MySwitch> switchList) {
		this.switchList = switchList;
	}


	@Override
	public SlideMode getSlideModeInPosition(int position) {
		return super.getSlideModeInPosition(position);
	}

	@Override
	public int getFrontViewId(int position) {
		return R.layout.listitem_21;
	}

	@Override
	public int getLeftBackViewId(int position) {

			return 0;

	}

	@Override
	public int getRightBackViewId(int position) {
		return R.layout.leftdelect;
	}

	@Override
	public int getItemViewType(int position) {
		if (position % 2 == 0) {
			return 0;
		}
		return 1;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (switchList != null) {
			return switchList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (switchList != null) {
			return switchList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		mySwitch = MyList.settingList.get(position);
		dbManager = new DBManager(mContext);

		ViewHolder holder =null;
		if (view == null) {

			view = createConvertView(position);
			
			holder = new ViewHolder();
			holder.iv_01 = (ImageView) view.findViewById(R.id.list_item21_iv1);
			holder.tv_01 = (TextView) view.findViewById(R.id.list_item21_tv1);
			holder.tv_02 = (TextView) view.findViewById(R.id.list_item21_tv2);
			holder.tv_03 = (TextView) view.findViewById(R.id.list_item21_tv3);
			holder.tv_04 = (TextView) view.findViewById(R.id.list_item21_tv4);
			holder.delete=(Button) view.findViewById(R.id.delete);


			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.iv_01.setVisibility(View.VISIBLE);
		holder.tv_01.setVisibility(View.VISIBLE);
		holder.tv_02.setVisibility(View.VISIBLE);
		holder.tv_03.setVisibility(View.VISIBLE);
		holder.tv_04.setVisibility(View.VISIBLE);
		holder.iv_01.setImageResource(mySwitch.getImage());

		holder.tv_01.setText(mySwitch.getSwitchName());
		holder.tv_02.setText(mySwitch.getSwitchId());

		holder.tv_01.setTextColor(mContext.getResources().getColor(R.color.tv_01));
		holder.tv_02.setTextColor(mContext.getResources().getColor(R.color.bg_list_03));
		holder.tv_03.setTextColor(mContext.getResources().getColor(R.color.green_btn));
		holder.tv_04.setTextColor(mContext.getResources().getColor(R.color.bg_list_03));

		if(mySwitch.getSwitchRssi()!=null) {
			holder.tv_03.setText(mySwitch.getSwitchRssi());
		}else{

			holder.tv_03.setText("无信号");
		}
		holder.tv_04.setText(mySwitch.getModle());

		holder.delete.setLayoutParams(new LinearLayout.LayoutParams(mContext.getResources().getDisplayMetrics().widthPixels / 3, LinearLayout.LayoutParams.MATCH_PARENT));
		holder.delete.setTag(position);
		deletePopupWindow=new DeletePopupWindow(activity, new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchList.remove(position);
				dbManager.setMySwitchList();
				notifyDataSetChanged();
				deletePopupWindow.dismiss();
			}
		});



		if (holder.delete != null) {
			holder.delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					deletePopupWindow.showAtLocation(activity
							.findViewById(R.id.activity_2_switchlist), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			});
		}
		
		

		return view;
	}

	static class ViewHolder {
		ImageView iv_01;
		TextView tv_01, tv_02, tv_03,tv_04;
		Button delete;
	}

}
