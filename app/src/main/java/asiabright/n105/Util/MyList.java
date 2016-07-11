package asiabright.n105.Util;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import asiabright.n105.R;

import android.content.Context;
import android.text.format.Time;

public class MyList {
	/**
	 * 设定显示文字
	 */
	public static List<MySwitch> settingList = new ArrayList<MySwitch>();






	public static boolean getBitValue(int num, int bit) {
		switch (bit) {
		case 0:
			if ((num & 0x01) == 0)
				return false;
			else
				return true;
		case 1:
			if ((num & 0x02) == 0)
				return false;
			else
				return true;
		case 2:
			if ((num & 0x04) == 0)
				return false;
			else
				return true;
		case 3:
			if ((num & 0x08) == 0)
				return false;
			else
				return true;
		case 4:
			if ((num & 0x10) == 0)
				return false;
			else
				return true;
		case 5:
			if ((num & 0x20) == 0)
				return false;
			else
				return true;
		case 6:
			if ((num & 0x40) == 0)
				return false;
			else
				return true;
		case 7:
			if ((num & 0x80) == 0)
				return false;
			else
				return true;
		default:
			return false;
		}
	}

	public static int hexToInt(String hex, int bitnum) {
		int a;
		int b = 0;
		int i = 0;
		while (bitnum > i) {
			b = b * 16;
			a = hex.substring(i, i + 1).charAt(0);
			if (a <= 0x39)
				a -= 0x30;
			else
				a -= 0x37;
			b += a;
			i++;
		}
		return b;
	}

	public static String intToHex(int num, int bitnum) {
		String a = "";
		byte i = 0;
		while (i < bitnum) {
			int j = (num % 16);
			num /= 16;
			j = (j + 48);
			if (j > 57)
				j = (j + 7);
			a = String.valueOf((char) j) + a;
			i++;
		}
		return a;
	}





}
