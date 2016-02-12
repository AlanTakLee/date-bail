package atl.date_bail.model;

import android.provider.BaseColumns;

/**
 * Created by Alan on 12/02/2016.
 */
public final class DateReaderContract {

    public DateReaderContract() {
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + DateEntry.TABLE_NAME + " (" +
            DateEntry._ID + " INTEGER PRIMARY KEY," +
            DateEntry.COLUMN_NAME_ID + TEXT_TYPE + COMMA_SEP +
            DateEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            DateEntry.COLUMN_NAME_IMAGE_PATH + TEXT_TYPE + COMMA_SEP +
            DateEntry.COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
            DateEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
            DateEntry.COLUMN_NAME_LOCATION + TEXT_TYPE + COMMA_SEP +
            DateEntry.COLUMN_NAME_CONTACTS + TEXT_TYPE + COMMA_SEP +
            DateEntry.COLUMN_NAME_NOTES + TEXT_TYPE + COMMA_SEP +
            " )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + DateEntry.TABLE_NAME;

    public static abstract class DateEntry implements BaseColumns {
        public static final String TABLE_NAME = "dates";
        public static final String COLUMN_NAME_ID = "dateId";
        public static final String COLUMN_NAME_NAME = "dateName";
        public static final String COLUMN_NAME_IMAGE_PATH = "dateImgPath";
        public static final String COLUMN_NAME_TIME = "dateTime";
        public static final String COLUMN_NAME_DATE = "dateDate";
        public static final String COLUMN_NAME_LOCATION = "dateLocation";
        public static final String COLUMN_NAME_CONTACTS = "dateBailouts";
        public static final String COLUMN_NAME_NOTES = "dateNotes";
    }

    public String getUpString() {
        return SQL_CREATE_ENTRIES;
    }

    public String getDownString() {
        return SQL_DELETE_ENTRIES;
    }
}
