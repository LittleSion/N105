package asiabright.n105.popwindow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

public class PopWindow {


	private NxPopWindow mPopupWindow;
	private View mPopView;
	private long durationMillis = 400;

	public PopWindow(Context context, View contentView) {

		this.mPopView = contentView;
		init();
	}

	@SuppressLint("NewApi")
	private void init() {
		// mPopView = LayoutInflater.from(mContext).inflate(mContentViewResID,
		// null);
		mPopView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		mPopView.setLayoutParams(lp);
		mPopupWindow = new NxPopWindow(mPopView, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);

		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#50000000")));
		mPopupWindow.setFocusable(true);

		mPopupWindow.setTouchInterceptor(new OnTouchListener() {		
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getY()>mPopView.getMeasuredHeight()&&mPopupWindow.isShowing())
				mPopupWindow.dismiss();
				return false;
			}
		});
		
	}

	public void show(View anchor, int xoff, int yoff) {
		TranslateAnimation enterAnimation = new TranslateAnimation(0, 0,
				-mPopView.getMeasuredHeight(), 0);
		enterAnimation.setDuration(durationMillis);
		mPopupWindow.showAsDropDown(anchor, xoff, yoff);
		mPopupWindow.update();
		mPopView.startAnimation(enterAnimation);
	}

	public void dissmiss() {
		mPopupWindow.dismiss();
	}

	class NxPopWindow extends PopupWindow {

		public NxPopWindow(View contentView, int width, int height,
				boolean focusable) {
			super(contentView, width, height, focusable);
		}



		@Override
		public void dismiss() {
			TranslateAnimation outAnimation = new TranslateAnimation(0, 0, 0,
					-mPopView.getMeasuredHeight());
			outAnimation.setDuration(durationMillis);
			outAnimation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					if (null != animationDissmissListener)
						animationDissmissListener.onAnimationDissmissStart();
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					NxPopWindow.super.dismiss();
				}
			});
			mPopView.startAnimation(outAnimation);
		}

	}

	private AnimationDissmissListener animationDissmissListener;

	public interface AnimationDissmissListener {
		public void onAnimationDissmissStart();
	}

	public void setAnimationDissmissListener(
			AnimationDissmissListener animationDissmissListener) {
		this.animationDissmissListener = animationDissmissListener;
	}
}
