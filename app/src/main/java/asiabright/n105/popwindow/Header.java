package asiabright.n105.popwindow;

import android.annotation.SuppressLint;
import asiabright.n105.popwindow.PopWindow.AnimationDissmissListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import asiabright.n105.R;


public class Header extends LinearLayout {
	private ImageView i_left, i_right;
	private TextView t_title;
	private Context context;
	private View mPopView;
	private PopWindow mPopWindow;
	// private ViewGroup frameLayout;
	private OnClickListener mOnClickListener = null;
	private MyMenu menu1 = new MyMenu();
	private MyMenu menu2 = new MyMenu();
	private MyMenu menu3 = new MyMenu();
	Animation limageAnimEnter;
	Animation limageAnimOut;
	Animation rimageAnimEnter;
	Animation rimageAnimOut;

	private class MyMenu {
		public ViewGroup menu;
		public ImageView image;
		public TextView text;
	}

	public Header(Context context) {
		super(context);
		this.context = context;
		initView();
		initPopWindow();
	}

	public Header(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
		initPopWindow();
	}

	@SuppressLint("NewApi")
	public Header(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
		initPopWindow();
	}

	//动画
	private AnimationDissmissListener animationDissmissListener = new AnimationDissmissListener() {
		@Override
		public void onAnimationDissmissStart() {
			// TODO Auto-generated method stub
			Animation operatingAnim = AnimationUtils.loadAnimation(context,
					R.anim.addbtn_rotate_out);
			operatingAnim.setInterpolator(new LinearInterpolator());
			operatingAnim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					// frameLayout.setVisibility(View.GONE);
				}
			});
			i_left.startAnimation(operatingAnim);
		}
	};
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			case R.id.header_left_ivw:
				System.out.println("header_right_ivw");

				i_left.startAnimation(limageAnimEnter);
				menu1.menu.setEnabled(true);
				menu2.menu.setEnabled(true);
				menu3.menu.setEnabled(true);
				mPopWindow.show(v, 0, 1);
				break;
			case R.id.header_right_ivw:
				i_right.startAnimation(rimageAnimEnter);
				if (mOnClickListener != null)
					mOnClickListener.onClick(v);
				break;
			case R.id.header_pop_button1:
			case R.id.header_pop_button2:
			case R.id.header_pop_button3:
				if (mOnClickListener != null)
					mOnClickListener.onClick(v);
//				menu1.menu.setEnabled(false);
//				menu2.menu.setEnabled(false);
//				menu3.menu.setEnabled(false);
				mPopWindow.dissmiss();
				break;
			}
		}
	};

	private void initView() {
		LayoutInflater.from(context).inflate(R.layout.mwt0100_myheader, this);
		i_left = (ImageView) findViewById(R.id.header_left_ivw);
		i_right = (ImageView) findViewById(R.id.header_right_ivw);
		t_title = (TextView) findViewById(R.id.header_total_tvw);

		// frameLayout=(ViewGroup) findViewById(R.id.header_flt);
		rimageAnimEnter = AnimationUtils.loadAnimation(context,
				R.anim.addbtn_rotate_enter_r);
		limageAnimEnter = AnimationUtils.loadAnimation(context,
				R.anim.addbtn_rotate_enter);
		rimageAnimOut = AnimationUtils.loadAnimation(context,
				R.anim.addbtn_rotate_out);
		i_left.setOnClickListener(onClickListener);
		i_left.setVisibility(View.INVISIBLE);
		i_right.setOnClickListener(onClickListener);
		i_right.setVisibility(View.INVISIBLE);
		// frameLayout.setVisibility(View.GONE);
	}

	public void setLeftButtonVisibility(boolean leftVisibility) {
		if (leftVisibility)
			i_left.setVisibility(View.VISIBLE);
		else
			i_left.setVisibility(View.INVISIBLE);
	}

	public void setRightButtonVisibility(boolean rightVisibility) {
		if (rightVisibility)
			i_right.setVisibility(View.VISIBLE);
		else
			i_right.setVisibility(View.INVISIBLE);
	}

	public void setTitle(String Tital) {
		t_title.setText(Tital);
	}

//	public void setTitleVisiable(boolean enable) {
//
//	}

	public void setHeaderOnClickListener(OnClickListener l) {
		this.mOnClickListener = l;
	}

	public void setMenu1(String text, int Image) {
		menu1.text.setText(text);
		if (Image != 0)
			menu1.image.setImageResource(Image);
	}

	public void setMenu2(String text, int Image) {
		menu2.text.setText(text);
		if (Image != 0)
			menu2.image.setImageResource(Image);
	}

	public void setMenu3(String text, int Image) {
		menu3.text.setText(text);
		if (Image != 0)
			menu3.image.setImageResource(Image);
	}

	private void initPopWindow() {
		mPopView = LayoutInflater.from(context).inflate(
				R.layout.header_pop, null);
		mPopWindow = new PopWindow(context, mPopView);
		mPopWindow.setAnimationDissmissListener(animationDissmissListener);
		menu1.menu = (ViewGroup) mPopView.findViewById(R.id.header_pop_button1);
		menu1.image = (ImageView) mPopView
				.findViewById(R.id.header_pop_button1_image);
		menu1.text = (TextView) mPopView
				.findViewById(R.id.header_pop_button1_text);
		menu2.menu = (ViewGroup) mPopView.findViewById(R.id.header_pop_button2);
		menu2.image = (ImageView) mPopView
				.findViewById(R.id.header_pop_button2_image);
		menu2.text = (TextView) mPopView
				.findViewById(R.id.header_pop_button2_text);
		menu3.menu = (ViewGroup) mPopView.findViewById(R.id.header_pop_button3);
		menu3.image = (ImageView) mPopView
				.findViewById(R.id.header_pop_button3_image);
		menu3.text = (TextView) mPopView
				.findViewById(R.id.header_pop_button3_text);
		menu1.menu.setOnClickListener(onClickListener);
		menu2.menu.setOnClickListener(onClickListener);
		menu3.menu.setOnClickListener(onClickListener);
		menu1.menu.setEnabled(true);
		menu2.menu.setEnabled(true);
		menu3.menu.setEnabled(true);
	}
}
