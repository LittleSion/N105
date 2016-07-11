package asiabright.n105.Util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangmin on 16/3/7.
 */
public class Toastter {
    private static Toast mToast = null;

    public static void showToast(Context context, String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }

        mToast.show();
    }
}
