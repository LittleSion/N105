package asiabright.n105.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Created by zhangmin on 16/5/26.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "n105info.db";

    private static final int DB_VERSION = 6;

    private DBHelper(Context context, String name, CursorFactory factory,
                     int version) {
        super(context, DB_NAME, factory, DB_VERSION);
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static DBHelper mInstance = null;

    public synchronized static DBHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS switchlist"
                + "(_id INTEGER PRIMARY  KEY AUTOINCREMENT,switchid varchar(50),"
                + "switchname varchar(50),switchmac varchar(50))");
//        db.execSQL("CREATE TABLE IF NOT EXISTS scene"
//                + "(_id INTEGER PRIMARY  KEY AUTOINCREMENT,switchid varchar(50),"
//                + "item1 varchar(50),item2 varchar(50),item3 varchar(50),item4 varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists timelist");
        onCreate(db);
        Log.i("WIRELESSQA", "update sqlite" + oldVersion + "---->" + newVersion);

    }

}
