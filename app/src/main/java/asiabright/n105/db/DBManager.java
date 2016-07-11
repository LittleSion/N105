package asiabright.n105.db;

/**
 * Created by zhangmin on 16/5/26.
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import asiabright.n105.Util.MyList;
import asiabright.n105.Util.MySwitch;


public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    // myList.mySettingList;
    public DBManager(Context context) {
        helper = DBHelper.getInstance(context);
        db = helper.getWritableDatabase();

    }

    public void setMySwitchList() {
        ExecSQL("DELETE FROM switchlist");
        db.beginTransaction();
        try {
            // myList.mySettingList
            for (int i = 0; i < MyList.settingList.size(); i++) {
                db.execSQL("INSERT INTO switchlist VALUES(null,?,?,?)",
                        new Object[]{MyList.settingList.get(i).getSwitchId(),
                                MyList.settingList.get(i).getSwitchName(),
                                MyList.settingList.get(i).getBlueMac()});
            }
            db.setTransactionSuccessful();
        } finally {

            db.endTransaction();
        }
    }

//    public void setSceneList() {
//        ExecSQL("DELETE FROM scene");
//        db.beginTransaction();
//        try {
//            // myList.mySettingList
//            for (int i = 0; i < MyList.settingList.size(); i++) {
//                db.execSQL("INSERT INTO scene VALUES(null,?,?,?,?,?)",
//                        new Object[]{MyList.settingList.get(i).getSwitchId(),
//                                MyList.settingList.get(i).getItem1(),
//                                MyList.settingList.get(i).getItem2(),
//                                MyList.settingList.get(i).getItem3(),
//                                MyList.settingList.get(i).getItem4()});
//            }
//            db.setTransactionSuccessful();
//        } finally {
//
//            db.endTransaction();
//        }
//    }


    /**
     * 閿熸枻鎷疯info閿熸枻鎷烽敓锟�
     */
    public void getMySwitchList() {
        Log.i("getMySwitchList", "getMySwitchList");
        MyList.settingList.clear();
        Cursor c = db.rawQuery("select * from switchlist", new String[]{});
        if (c.moveToFirst()) {
            do {
                MySwitch mySwitch = new MySwitch(c.getString(c
                        .getColumnIndex("switchid")));
                mySwitch.setSwitchName(c.getString(c
                        .getColumnIndex("switchname")));
                mySwitch.setBlueMac(c.getString(c
                        .getColumnIndex("switchmac")));

                MyList.settingList.add(mySwitch);
            } while (c.moveToNext());
        }
        c.close();
    }

//    public void getSceneList() {
//        // MyList.sceneList.clear();
//        Cursor c = db.rawQuery("select * from scene", new String[]{});
//        if (c.moveToFirst()) {
//            do {
//                String switchId = c.getString(c.getColumnIndex("switchid"));
//                String item1 = c.getString(c.getColumnIndex("item1"));
//                String item2 = c.getString(c.getColumnIndex("item2"));
//                String item3 = c.getString(c.getColumnIndex("item3"));
//                String item4 = c.getString(c.getColumnIndex("item4"));
//                for (int i = 0; i < MyList.settingList.size(); i++) {
//                    if (MyList.settingList.get(i).getSwitchId()
//                            .equals(switchId)) {
//                        MyList.settingList.get(i).setItem1(item1);
//                        MyList.settingList.get(i).setItem2(item2);
//                        MyList.settingList.get(i).setItem3(item3);
//                        MyList.settingList.get(i).setItem4(item4);
//                    }
//                }
//            } while (c.moveToNext());
//        }
//        c.close();
//    }


    private void ExecSQL(String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Log.e("ExecSQL Exception", e.getMessage());
            e.printStackTrace();
        }
    }
}
