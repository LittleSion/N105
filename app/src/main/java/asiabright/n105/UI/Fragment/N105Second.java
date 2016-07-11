package asiabright.n105.UI.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.widget.TextView;

import org.w3c.dom.Text;

import asiabright.n105.R;
import asiabright.n105.Util.ForBtnClickListener;
import asiabright.n105.dialog.CheckDialog;


public class N105Second extends Fragment implements OnClickListener{

    private ImageButton btnone,btntwo;
    private ForBtnClickListener forBtnClickListener;
    private TextView txtone,txttwo;

    public void setButtonsta(String btnsta){
        if(btnsta.substring(0,2).equals("64")){
            btnone.setImageResource(R.mipmap.button_off);
        }else if(btnsta.substring(0,2).equals("65")){
            btnone.setImageResource(R.mipmap.button_on);
        }
        if(btnsta.substring(2,4).equals("64")){
            btntwo.setImageResource(R.mipmap.button_off);
        }else if(btnsta.substring(2,4).equals("65")){
            btntwo.setImageResource(R.mipmap.button_on);
        }
    }

    public void setForBtnClickListener(ForBtnClickListener forBtnClickListener)
    {
        this.forBtnClickListener = forBtnClickListener;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_n105_second, container, false);
        btnone = (ImageButton) view.findViewById(R.id.btnone);
        btnone.setOnClickListener(this);
        btntwo = (ImageButton) view.findViewById(R.id.btntwo);
        btntwo.setOnClickListener(this);
        txtone = (TextView) view.findViewById(R.id.devone);
        txtone.setOnClickListener(this);
        txttwo = (TextView) view.findViewById(R.id.devtwo);
        txttwo.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if(forBtnClickListener != null){
            forBtnClickListener.BtnClick(v);
        }
        switch (v.getId()){
            case R.id.devone:

                break;
            case R.id.devtwo:

                break;
            default:

                break;
        }
    }
}
