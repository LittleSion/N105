package asiabright.n105.Util;


import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import asiabright.n105.R;

public class MySwitch implements Parcelable {
    private String switchId = "";                        //开关ID


    private String modle = "";                            //开关型号
    private boolean buttonStatue[] = new boolean[4];    //按键状态
    private int switchLoadNumber[] = new int[4];        //现阶段按钮状态的十进制储存形式
    private int statueImage = 0;
    private int[] lightImage = new int[4];
    private int[] buttonImage = new int[4];
    private int buttonVisibility[] = new int[4];
    private int lightVisibility[] = new int[4];
    //	private int Dtime[] = new int[4];
    private int layoutNum = 0;                            //控制界面编号
    private String loadStatue = "E4E4E4E4";            //现阶段按钮的状态
    private String switchName = "";                    //开关名字
    private int swidnum = 0;                            //开关ID的头两个

    private int switchIdNumber[] = new int[6];        //开关ID十进制表示
    private String item1;
    private String item2;
    private String item3;
    private String item4;
    private String bluemac;
    private String rssi;

    public MySwitch(String switchId) {
        this.switchId = switchId;
        swidnum = MyList.hexToInt(this.switchId.substring(0, 2), 2);
        this.switchName = this.switchId;

        switchIdNumber[0] = MyList.hexToInt(this.switchId.substring(0, 2), 2);
        switchIdNumber[1] = MyList.hexToInt(this.switchId.substring(3, 5), 2);
        switchIdNumber[2] = MyList.hexToInt(this.switchId.substring(6, 8), 2);
        switchIdNumber[3] = MyList.hexToInt(this.switchId.substring(9, 11), 2);
        switchIdNumber[4] = MyList.hexToInt(this.switchId.substring(12, 14), 2);
        switchIdNumber[5] = MyList.hexToInt(this.switchId.substring(15, 17), 2);
    }

    public int getSwidnum() {
        return swidnum;
    }

    public void setSwidnum(int swidnum) {

        this.swidnum = swidnum;
    }


    public void setBlueMac(String mac) {
        this.bluemac = mac;
    }

    public String getBlueMac() {
        return bluemac;
    }

    public String getSwitchName() {
        return switchName;
    }

    public void setSwitchName(String switchName) {

        this.switchName = switchName;

    }

    public int getRotate() {
        int RotateId = 0;
        getButtonStatue();
        switch (swidnum) {
            case 208:
                if (buttonStatue[0] == true) {
                    RotateId = 3;
                }

                if (buttonStatue[1] == true) {
                    RotateId = 2;
                }

                if (buttonStatue[2] == true) {
                    RotateId = 1;
                }

                if (buttonStatue[3] == true) {
                    RotateId = 0;
                    System.out.println("false" + "--4");
                }

                break;
        }
        return RotateId;

    }

    public int[] getButtonImage() {
        getButtonStatue();
        switch (swidnum) {
            case 208:
            case 0xD1:
                if (buttonStatue[0] == true) {

                    buttonImage[0] = R.mipmap.lblue;

                } else {

                    buttonImage[0] = R.mipmap.lblack;

                }
                if (buttonStatue[1] == true) {

                    buttonImage[1] = R.mipmap.mblue;

                } else {

                    buttonImage[1] = R.mipmap.mblack;

                }
                if (buttonStatue[2] == true) {

                    buttonImage[2] = R.mipmap.hblue;

                } else {

                    buttonImage[2] = R.mipmap.hblack;

                }
                if (buttonStatue[3] == true) {

                    buttonImage[3] = R.mipmap.offblack;

                } else {

                    buttonImage[3] = R.mipmap.offblack;

                }
                break;
            case 224:
                for (int i = 0; i < 4; i++) {
                    if (buttonStatue[i] == true) {
                        buttonImage[i] = R.mipmap.icon_on_press;
                    } else {
                        buttonImage[i] = R.mipmap.icon_on_nomal;
                    }
                }
                break;
            case 176:
            case 0xB4:
            case 0x94:
            case 192:
            case 193:
            case 144:
            case 145:
            case 160:
            case 161:
            case 162:
                for (int i = 0; i < 4; i++) {
                    if (buttonStatue[i] == true) {

                        buttonImage[i] = R.mipmap.icon_on_press;

                    } else {

                        buttonImage[i] = R.mipmap.icon_on_nomal;

                    }
                }

                // for (int i = 0; i < 4; i++) {
                // if (buttonStatue[i] == true) {
                // buttonImage[i] = R.mipmap.icon_on_press;
                // } else {
                // buttonImage[i] = R.mipmap.icon_on_nomal;
                // }
                // }
                break;
        }
        return buttonImage;
    }

    public int[] getButtonImage_01() {
        getButtonStatue_01();
        switch (swidnum) {
            case 208:
            case 0xD1:
                for (int i = 0; i < 4; i++) {
                    if (buttonStatue[i] == true) {
                        buttonImage[i] = R.mipmap.icon_uon;
                    } else {
                        buttonImage[i] = R.mipmap.icon_uoff;
                    }
                }
                break;
            case 176:
            case 0xB4:
            case 0x94:
            case 192:
            case 193:
            case 144:
            case 145:
            case 160:
            case 161:
            case 162:
                for (int i = 0; i < 4; i++) {
                    if (buttonStatue[i] == true) {
                        buttonImage[i] = R.mipmap.icon_on_press;
                    } else {
                        buttonImage[i] = R.mipmap.icon_on_nomal;
                    }
                }

                // for (int i = 0; i < 4; i++) {
                // if (buttonStatue[i] == true) {
                // buttonImage[i] = R.mipmap.icon_on_press;
                // } else {
                // buttonImage[i] = R.mipmap.icon_on_nomal;
                // }
                // }
                break;
        }
        return buttonImage;
    }

    private void getButtonStatue_01() {
        String cmd_1 = loadStatue.substring(0, 2);
        String cmd_2 = loadStatue.substring(2, 4);
        String cmd_3 = loadStatue.substring(4, 6);
        String cmd_4 = loadStatue.substring(6, 8);
        int cmd_num[] = new int[4];
        cmd_num[0] = MyList.hexToInt(cmd_1, 2);
        cmd_num[1] = MyList.hexToInt(cmd_2, 2);
        cmd_num[2] = MyList.hexToInt(cmd_3, 2);
        cmd_num[3] = MyList.hexToInt(cmd_4, 2);
        switch (swidnum) {
            case 208:
            case 0xD1:
            case 176:
            case 0xB4:
            case 0x94:
            case 192:
            case 193:
            case 144:
            case 145:
            case 160:
            case 161:
            case 162:
            case 224:
            case 201:
            case 0x98:
                for (int i = 0; i < 4; i++) {
                    if (cmd_num[i] < 128) {
                        buttonStatue[i] = true;
                    } else {
                        buttonStatue[i] = false;
                    }
                }
                break;
        }

    }

//	public int[] getDtime() {
//
//		String cmd_1 = loadStatue.substring(0, 2);
//		String cmd_2 = loadStatue.substring(2, 4);
//		String cmd_3 = loadStatue.substring(4, 6);
//		String cmd_4 = loadStatue.substring(6, 8);
//		Dtime[0] = MyList.hexToInt(cmd_1, 2);
//		Dtime[1] = MyList.hexToInt(cmd_2, 2);
//		Dtime[2] = MyList.hexToInt(cmd_3, 2);
//		Dtime[3] = MyList.hexToInt(cmd_4, 2);
//		return Dtime;
//	}

    private void getButtonStatue() {
        String cmd_1 = loadStatue.substring(0, 2);
        String cmd_2 = loadStatue.substring(2, 4);
        String cmd_3 = loadStatue.substring(4, 6);
        String cmd_4 = loadStatue.substring(6, 8);
        int cmd_num[] = new int[4];
        cmd_num[0] = MyList.hexToInt(cmd_1, 2);
        cmd_num[1] = MyList.hexToInt(cmd_2, 2);
        cmd_num[2] = MyList.hexToInt(cmd_3, 2);
        cmd_num[3] = MyList.hexToInt(cmd_4, 2);
        switch (swidnum) {
            case 208:
            case 0xD1:
                for (int i = 0; i < 4; i++) {
                    buttonStatue[i] = false;
                }
                if (cmd_num[1] == 60) {
                    buttonStatue[0] = true;
                }
                if (cmd_num[1] == 80) {
                    buttonStatue[1] = true;
                }
                if (cmd_num[1] == 100) {
                    buttonStatue[2] = true;
                }
                if (cmd_num[1] == 228) {
                    buttonStatue[3] = true;
                }
                break;
            case 176:
            case 0xB4:
            case 0x94:
            case 192:
            case 193:
            case 144:
            case 145:
            case 160:
            case 161:
            case 162:
            case 224:
            case 201:
            case 0x98:
                for (int i = 0; i < 4; i++) {

                    if (cmd_num[i] < 128) {
                        buttonStatue[i] = true;
                    } else {
                        buttonStatue[i] = false;
                    }
                }
                break;
        }
    }

    public String controlLoad_fan(int speed, String str_h, String str_m) {
        String statue[] = new String[4];
        statue[0] = "65";
        statue[1] = MyList.intToHex(speed, 2);
        statue[2] = str_h;
        statue[3] = str_m;
        getButtonStatue();
        return statue[0] + statue[1] + statue[2] + statue[3];
    }

    public String controlLoad(int position) {
        // String statue[] = new String[4];
        // statue[0] = "65";
        // statue[1] = "65";
        // statue[2] = "65";
        // statue[3] = "65";
        getButtonStatue_01();
        // if (buttonStatue[position] == true)

        // statue[position] = "E4";
        // else
        // statue[position] = "64";
        // return statue[0] + statue[1] + statue[2] + statue[3];
        // String
        int state[] = new int[4];
        state[0] = 101;
        state[1] = 101;
        state[2] = 101;
        state[3] = 101;
        if (buttonStatue[position])
            state[position] = switchLoadNumber[position] + 128;
        else
            state[position] = switchLoadNumber[position];
        String controlStatus = MyList.intToHex(state[0], 2)
                + MyList.intToHex(state[1], 2) + MyList.intToHex(state[2], 2)
                + MyList.intToHex(state[3], 2);
        return controlStatus;

    }

    public int[] getButtonVisibility() {
        switch (swidnum) {
            case 176:
            case 0xB4:
            case 0x94:
            case 192:
            case 193:
            case 144:
            case 145:
            case 160:
                buttonVisibility[0] = View.VISIBLE;
                buttonVisibility[1] = View.GONE;
                buttonVisibility[2] = View.GONE;
                buttonVisibility[3] = View.GONE;
                break;

            case 161:
                // modle="u106";
                buttonVisibility[0] = View.VISIBLE;
                buttonVisibility[1] = View.VISIBLE;
                buttonVisibility[2] = View.GONE;
                buttonVisibility[3] = View.GONE;
                break;

            case 162:
                // modle="u107";
                buttonVisibility[0] = View.VISIBLE;
                buttonVisibility[1] = View.VISIBLE;
                buttonVisibility[2] = View.VISIBLE;
                buttonVisibility[3] = View.GONE;
                break;

            case 224:
                // modle="u111";
                buttonVisibility[0] = View.VISIBLE;
                buttonVisibility[1] = View.VISIBLE;
                buttonVisibility[2] = View.VISIBLE;
                buttonVisibility[3] = View.VISIBLE;
        }
        return buttonVisibility;
    }

    public int[] getLightVisibility() {
        switch (swidnum) {

            case 192:
                lightVisibility[0] = View.GONE;
                lightVisibility[1] = View.GONE;
                lightVisibility[2] = View.GONE;
                lightVisibility[3] = View.GONE;
                break;
            case 193:
                lightVisibility[0] = View.GONE;
                lightVisibility[1] = View.GONE;
                lightVisibility[2] = View.GONE;
                lightVisibility[3] = View.GONE;
                break;
            case 176:
            case 0xB4:
            case 0x94:
            case 144:
            case 145:
            case 160:
                lightVisibility[0] = View.VISIBLE;
                lightVisibility[1] = View.GONE;
                lightVisibility[2] = View.GONE;
                lightVisibility[3] = View.GONE;
                break;

            case 161:
                // modle="u106";
                lightVisibility[0] = View.VISIBLE;
                lightVisibility[1] = View.VISIBLE;
                lightVisibility[2] = View.GONE;
                lightVisibility[3] = View.GONE;
                break;

            case 162:
                // modle="u107";
                lightVisibility[0] = View.VISIBLE;
                lightVisibility[1] = View.VISIBLE;
                lightVisibility[2] = View.VISIBLE;
                lightVisibility[3] = View.GONE;
                break;
            case 224:
                // modle="u111";
                lightVisibility[0] = View.VISIBLE;
                lightVisibility[1] = View.VISIBLE;
                lightVisibility[2] = View.VISIBLE;
                lightVisibility[3] = View.VISIBLE;

        }
        return lightVisibility;
    }

    public int getImage() {
//		image = R.mipmap.icon_ucf_fan;
//		if (switchStatueNum == 0) {
//			switch (swidnum) {
//			case 144:
//				// modle="u416";
//				break;
//			case 145:
//				// modle="u200";
//				break;
//			case 160:
//				// modle="u105";
//				image = R.mipmap.u605_01;
//				break;
//			case 161:
//				// modle="u106";
//				image = R.mipmap.u106_01;
//				break;
//			case 162:
//				// modle="u107";
//				image = R.mipmap.u607_01;
//				break;
//			case 176:
//			case 0xB4:
//
//				// modle="u170";
//				image = R.mipmap.u670_01;
//				break;
//			case 0x94:
//				image = R.mipmap.u171_02;
//				break;
//			case 192:
//				// modle="u190";
//				image = R.mipmap.u690_01;
//				break;
//			case 208:
//			case 0xD1:
//				image = R.mipmap.icon_ucf_fan_01;
//				break;
//			case 224:
//				image = R.mipmap.u224_01;
//				break;
//			case 201:
//
//				image = R.mipmap.u483_01;
//				break;
//			case 0x98:
//				image = R.mipmap.u307_01;
//				break;
//			}
//		} else {
//			switch (swidnum) {
//			case 144:
//				// modle="u416";
//				break;
//			case 145:
//				// modle="u200";
//
//				break;
//			case 160:
//				// modle="u105";
//				image = R.mipmap.u605;
//				break;
//			case 161:
//				// modle="u106";
//				image = R.mipmap.u106;
//				break;
//			case 162:
//				// modle="u107";
//				image = R.mipmap.u607;
//				break;
//			case 176:
//			case 0xB4:
//
//				// modle="u170";
//				image = R.mipmap.u670;
//				break;
//			case 0x94:
//				image = R.mipmap.u171_01;
//				break;
//			case 192:
//				// modle="u190";
//				image = R.mipmap.u690;
//				break;
//			case 208:
//			case 0xD1:
//				image = R.mipmap.icon_ucf_fan;
//				break;
//			case 224:
//				image = R.mipmap.u224;
//				break;
//			case 201:
//				image = R.mipmap.u483;
//				break;
//			case 0x98:
//				image = R.mipmap.u307;
//				break;
//			}
//		}
//		return image;
//	}
//
//	public int getStatueImage() {
//		if (switchStatueNum == 0) {
//			switch (swidnum) {
//			case 144:
//				// modle="u416";
//				break;
//			case 145:
//				// modle="u200";
//				break;
//			case 160:
//				// modle="u105";
//				statueImage = R.mipmap.icon_one_way;
//				break;
//			case 161:
//				// modle="u106";
//				statueImage = R.mipmap.icon_twoway;
//				break;
//			case 162:
//				// modle="u107";
//				statueImage = R.mipmap.icon_three_way;
//				break;
//			case 224:
//				// modle=u111;
//				statueImage = R.mipmap.icon_four_way;
//				break;
//			case 176:
//			case 0xB4:
//			case 0x94:
//				// modle="u170";
//				// if (loadStatue.substring(0, 2).equals("64")) {
//				// statueImage = R.mipmap.dengzuo_on;
//				// } else {
//				statueImage = R.mipmap.dengzuo_off;
//				// }
//				break;
//			case 192:
//				// modle="u190";
//				// if (loadStatue.substring(0, 2).equals("64")) {
//				// statueImage = R.mipmap.icon_chazuo_on;
//				// } else{
//				// statueImage = R.mipmap.icon_chazuo_off;
//				// }
//				// break;
//			case 201:
//
//				// modle="u190";
//				if (loadStatue.substring(0, 2).equals("64")) {
//					statueImage = R.mipmap.plug_ic_220v_on_n;
//				} else {
//					statueImage = R.mipmap.plug_ic_220v_off_n;
//				}
//				break;
//			case 0x98:
//				if (loadStatue.substring(0, 2).equals("64")) {
//					statueImage = R.mipmap.plug_ic_u_on_n;
//				} else {
//					statueImage = R.mipmap.plug_ic_u_off_n;
//				}
//				break;
//			}
//		} else {
//			switch (swidnum) {
//			case 144:
//				// modle="u416";
//				break;
//			case 145:
//				// modle="u200";
//				break;
//			case 160:
//				// modle="u105";
//				statueImage = R.mipmap.icon_one_way;
//				break;
//			case 161:
//				// modle="u106";
//				statueImage = R.mipmap.icon_twoway;
//				break;
//			case 162:
//				// modle="u107";
//				statueImage = R.mipmap.icon_three_way;
//				break;
//			case 224:
//				// modle="u111";
//				statueImage = R.mipmap.icon_four_way;
//				break;
//			case 176:
//			case 0xB4:
//			case 0x94:
//				// modle="u170";
//				if (loadStatue.substring(0, 2).equals("64")) {
//					statueImage = R.mipmap.dengzuo_on;
//				} else {
//					statueImage = R.mipmap.dengzuo_off;
//				}
//				break;
//			case 192:
//				// modle="u190";
//				// if (loadStatue.substring(0, 2).equals("64")) {
//				// statueImage = R.mipmap.icon_chazuo_on;
//				// } else {
//				// statueImage = R.mipmap.icon_chazuo_off;
//				// }
//				// break;
//			case 201:
//
//				// modle="u190";
//				if (loadStatue.substring(0, 2).equals("64")) {
//					statueImage = R.mipmap.plug_ic_220v_on_n;
//				} else {
//					statueImage = R.mipmap.plug_ic_220v_off_n;
//				}
//				break;
//			case 0x98:
//				// modle="u190";
//				if (loadStatue.substring(0, 2).equals("64")) {
//					statueImage = R.mipmap.plug_ic_u_on_n;
//				} else {
//					statueImage = R.mipmap.plug_ic_u_off_n;
//				}
//				break;
//			}
//		}
        return statueImage;
    }

    public int[] getLightImage() {
        getButtonStatue_01();
        for (int i = 0; i < 4; i++) {
            if (buttonStatue[i] == true) {
                lightImage[i] = R.mipmap.icon_d_10;
            } else {
                lightImage[i] = R.mipmap.icon_d_00;
            }
        }
        return lightImage;
    }

    public int[] getLightImage_01() {
        getButtonStatue_01();
        for (int i = 0; i < 4; i++) {
            if (buttonStatue[i] == true) {
                lightImage[i] = R.mipmap.icon_u111_on;
            } else {
                lightImage[i] = R.mipmap.icon_u111_off;
            }
        }
        return lightImage;
    }

//	public String getLoadStatue() {
//		return loadStatue;
//	}

    private void countSwitchLoadNumber() {
        for (int i = 0; i < 4; i++) {
            switchLoadNumber[i] = MyList.hexToInt(
                    loadStatue.substring(i * 2, i * 2 + 2), 2);
            if (switchLoadNumber[i] >= 128) {
                buttonStatue[i] = false;
                switchLoadNumber[i] = switchLoadNumber[i] - 128;
            } else {
                buttonStatue[i] = true;
            }
        }
    }

    // public String getControlStatus(int position) {
    //
    // }
    public int getLayoutNum() {
        switch (swidnum) {
            case 208:
                layoutNum = 1;// D0 风扇
                break;
            case 144:
            case 145:
            case 160:
            case 161:
            case 176:
            case 0xB4:
            case 162:
                layoutNum = 0;
                break;
            case 192:
            case 201:
            case 0x98:
                layoutNum = 2;
                break;
            case 224:
                layoutNum = 3;
                break;
            case 0xD1:
                layoutNum = 4;
                break;
            case 0x94:
                layoutNum = 5;
                break;

        }
        return layoutNum;
    }

//	public int[] getSwitchLoadNumber() {
//		return switchLoadNumber;
//	}
//
//	public void setLoadStatue(String loadStatue) {
//		this.loadStatue = loadStatue;
//		countSwitchLoadNumber();
//	}

    public String getSwitchId() {
        return switchId;
    }


    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem4() {
        return item4;
    }

    public void setItem4(String item4) {
        this.item4 = item4;
    }


    public String getModle() {
        switch (swidnum) {
            case 144:
                modle = "u416";
                break;
            case 145:
                modle = "u200";
                break;
            case 160:
                modle = "u105";
                break;
            case 161:
                modle = "u106";
                break;
            case 162:
                modle = "u107";
                break;
            case 176:
                modle = "u170";
                break;
            case 192:
                modle = "u190";
                break;
            case 208:
                modle = "uCF280";
                break;
            case 0xD1:
                modle = "MR99T";
                break;
            case 224:
                modle = "u111";
                break;
            case 0x98:
                modle = "u307";
                break;
            case 0xB4:
                modle = "u171";
                break;
            case 0x94:
                modle = "u172";
                break;
            case 201:
                if (switchIdNumber[1] == 0 && switchIdNumber[2] == 0
                        && switchIdNumber[3] == 1) {
                    if (switchIdNumber[4] < 0x74) {
                        modle = "u483";
                    } else {
                        modle = "u484";
                    }
                } else {
                    if (switchIdNumber[3] % 2 == 0) {
                        modle = "u483";
                    }
                    if (switchIdNumber[3] % 2 == 1) {
                        modle = "u484";
                    }
                }
                break;
            case 0xF1:
                if(switchIdNumber[5]==0x00)
                    modle = "c300";
                if(switchIdNumber[5]==0x01)
                    modle = "c200" ;
                break;


        }
        return modle;
    }


    public void setSwitchRssi(String rssi) {
        this.rssi = rssi;
    }

    public String getSwitchRssi() {
        return rssi;
    }

    public static final Parcelable.Creator<MySwitch> CREATOR =
            new Parcelable.Creator<MySwitch>() {
                public MySwitch createFromParcel(Parcel in) {
                    return new MySwitch(in);
                }

                public MySwitch[] newArray(int size) {
                    return new MySwitch[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(switchId);
        dest.writeString(modle);
        dest.writeBooleanArray(buttonStatue);
        dest.writeIntArray(switchLoadNumber);
        dest.writeInt(statueImage);
        dest.writeIntArray(lightImage);
        dest.writeIntArray(buttonImage);
        dest.writeIntArray(buttonVisibility);
        dest.writeIntArray(lightVisibility);
        dest.writeInt(layoutNum);
        dest.writeString(loadStatue);
        dest.writeString(switchName);
        dest.writeInt(swidnum);
        dest.writeIntArray(switchLoadNumber);
        dest.writeString(item1);
        dest.writeString(item2);
        dest.writeString(item3);
        dest.writeString(item4);
        dest.writeString(bluemac);
        dest.writeString(rssi);

    }

    private MySwitch(Parcel in) {
        switchId = in.readString();
        modle = in.readString();
        in.readBooleanArray(buttonStatue);
        in.readIntArray(switchLoadNumber);
        statueImage = in.readInt();
        in.readIntArray(lightImage);
        in.readIntArray(buttonImage);
        in.readIntArray(buttonVisibility);
        in.readIntArray(lightVisibility);
        layoutNum = in.readInt();
        loadStatue = in.readString();
        switchName = in.readString();
        swidnum = in.readInt();
        in.readIntArray(switchLoadNumber);
        item1 = in.readString();
        item2 = in.readString();
        item3 = in.readString();
        item4 = in.readString();
        bluemac = in.readString();
        rssi = in.readString();
    }
}
