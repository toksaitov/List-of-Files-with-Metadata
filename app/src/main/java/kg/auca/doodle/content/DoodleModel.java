package kg.auca.doodle.content;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DoodleModel extends SQLiteOpenHelper {

    private final class DoodleReaderContract {

        private DoodleReaderContract() {}

        class DoodleEntry implements BaseColumns {
            static final String TABLE_NAME = "doodle";
            static final String COLUMN_NAME_TITLE = "name";
            static final String COLUMN_NAME_PATH  = "path";
        }

        static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DoodleEntry.TABLE_NAME + " (" +
                DoodleEntry._ID + " INTEGER PRIMARY KEY," +
                DoodleEntry.COLUMN_NAME_TITLE + " TEXT," +
                DoodleEntry.COLUMN_NAME_PATH  + " TEXT)";

        static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DoodleEntry.TABLE_NAME;

        static final String SQL_SELECT_ENTRY =
            "SELECT * FROM " + DoodleEntry.TABLE_NAME + " WHERE " + DoodleEntry._ID + "=?";

        static final String SQL_SELECT_ENTRIES =
            "SELECT * FROM " + DoodleEntry.TABLE_NAME;

    }

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Doodles.db";

    public DoodleModel(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DoodleReaderContract.SQL_CREATE_ENTRIES);

        populateWithSampleData(db);
    }

    private void populateWithSampleData(SQLiteDatabase db) {
        final int SAMPLE_COUNT = 20;

        for (int i = 0; i < SAMPLE_COUNT; ++i) {
            insertDoodle(db, new DoodleItem("File " + i, "/tmp/image_" + i + ".png"));
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DoodleReaderContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public long insertDoodle(DoodleItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        return insertDoodle(db, item);
    }

    private long insertDoodle(SQLiteDatabase db, DoodleItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DoodleReaderContract.DoodleEntry.COLUMN_NAME_TITLE, item.getTitle());
        contentValues.put(DoodleReaderContract.DoodleEntry.COLUMN_NAME_PATH, item.getPath());

        long id = db.insert(DoodleReaderContract.DoodleEntry.TABLE_NAME, null, contentValues);
        item.setId(id);

        return id;
    }

    public DoodleItem getDoodle(long id) {
        DoodleItem doodleItem = null;

        SQLiteDatabase db = this.getReadableDatabase();

        String[] params = new String[] { String.valueOf(id) };
        Cursor cursor = db.rawQuery(DoodleReaderContract.SQL_SELECT_ENTRY, params);

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            int columnIndex;

            columnIndex = cursor.getColumnIndex(DoodleReaderContract.DoodleEntry.COLUMN_NAME_TITLE);
            String title = cursor.getString(columnIndex);

            columnIndex = cursor.getColumnIndex(DoodleReaderContract.DoodleEntry.COLUMN_NAME_PATH);
            String path = cursor.getString(columnIndex);

            doodleItem = new DoodleItem(id, title, path);
        }
        cursor.close();

        return doodleItem;
    }

    public long doodleCount(){
        SQLiteDatabase db = this.getReadableDatabase();

        return DatabaseUtils.queryNumEntries(db, DoodleReaderContract.DoodleEntry.TABLE_NAME);
    }

    public int updateDoodle(DoodleItem item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DoodleReaderContract.DoodleEntry.COLUMN_NAME_TITLE, item.getTitle());
        contentValues.put(DoodleReaderContract.DoodleEntry.COLUMN_NAME_PATH, item.getPath());

        String[] params = new String[] { String.valueOf(item.getId()) };
        return db.update(
            DoodleReaderContract.DoodleEntry.TABLE_NAME,
            contentValues,
            DoodleReaderContract.DoodleEntry._ID + "=?",
            params
        );
    }

    public long deleteDoodle(long id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] params = new String[] { String.valueOf(id) };
        return db.delete(
            DoodleReaderContract.DoodleEntry.TABLE_NAME,
            DoodleReaderContract.DoodleEntry._ID + "=?",
            params
        );
    }

    public List<DoodleItem> getDoodles() {
        ArrayList<DoodleItem> doodles = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(DoodleReaderContract.SQL_SELECT_ENTRIES, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int columnIndex;

            columnIndex = cursor.getColumnIndex(DoodleReaderContract.DoodleEntry._ID);
            long id = cursor.getLong(columnIndex);

            columnIndex = cursor.getColumnIndex(DoodleReaderContract.DoodleEntry.COLUMN_NAME_TITLE);
            String title = cursor.getString(columnIndex);

            columnIndex = cursor.getColumnIndex(DoodleReaderContract.DoodleEntry.COLUMN_NAME_PATH);
            String path = cursor.getString(columnIndex);

            DoodleItem doodleItem = new DoodleItem(id, title, path);
            doodles.add(doodleItem);

            cursor.moveToNext();
        }
        cursor.close();

        return doodles;
    }

}