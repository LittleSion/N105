package asiabright.n105.UI.Fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import asiabright.n105.R;
import android.view.View.OnClickListener;
import asiabright.n105.Util.ForBtnClickListener;


public class N105First extends Fragment implements OnClickListener{


    private ImageButton btnone;

    private ForBtnClickListener ForBtnClickListener ;




    public void setButtonsta(String btnsta){
        if(btnsta.substring(0,2).equals("64")){
            btnone.setImageResource(R.mipmap.button_off);
        }else if(btnsta.substring(0,2).equals("65")){
            btnone.setImageResource(R.mipmap.button_on);
        }

    }



    public void setForBtnClickListener(ForBtnClickListener forBtnClickListener)
    {
        this.ForBtnClickListener = forBtnClickListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_n105_first, container, false);
        btnone = (ImageButton) view.findViewById(R.id.btnone);
        btnone.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if(ForBtnClickListener != null)
        {
            ForBtnClickListener.BtnClick(v);
        }
    }


}
