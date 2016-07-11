package asiabright.n105.popwindow;

import asiabright.n105.R;


import android.app.Activity;
import android.content.Context;

import android.graphics.drawable.ColorDrawable;

import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.ViewGroup.LayoutParams;

import android.widget.PopupWindow;
import android.widget.TextView;

public class DeletePopupWindow extends PopupWindow {

	private TextView btn_cancel,btn_ok;
	private View mMenuView;
	private OnClickListener itemsOnClick;
	public DeletePopupWindow(Activity context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.delete_popup, null);
		
		this.itemsOnClick = itemsOnClick;
		init();
		initView();
		//		mMenuView.setOnTouchListener(new OnTouchListener() {
		//			public boolean onTouch(View v, MotionEvent event) {
		//				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
		//				int y = (int) event.getY();
		//				if (event.getAction() == MotionEvent.ACTION_UP) {
		//					if (y < height) {
		//						dismiss();
		//					}
		//				}
		//				return true;
		//			}
		//		});
	}
	
	
	private void init(){
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0x20ffffff);	
		this.setBackgroundDrawable(dw);
		
	}			
	private void initView(){
		
		btn_ok=(TextView)mMenuView.findViewById(R.id.no_tv);
		btn_cancel=(TextView)mMenuView.findViewById(R.id.yes_tv);
		btn_ok.setOnClickListener(itemsOnClick);
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				dismiss();
				
			}
		});
	}

}
