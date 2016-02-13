package atl.date_bail;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import atl.date_bail.model.DateInfo;
import atl.date_bail.model.DateReaderContract;
import atl.date_bail.model.DateReaderDbHelper;

public class DateFragment extends Fragment {

    private DateFragmentInteractionListener mListener;
    private List<DateInfo> items;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DateFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<>();
        //TODO: remove fake data

        populateFakeData();
        Cursor data = readData();
        populateArray(data, items);
    }

    private void populateArray(Cursor data, List<DateInfo> dates) {
        for (int i = 0; i < 3; i++) {
            DateInfo current = new DateInfo();
            for (int j = 0; j < data.getColumnCount(); j++) {
                switch (j) {
                    case 0: {
                        current.setId(data.getLong(j));
                        break;
                    }
                    case 1: {
                        current.setName(data.getString(j));
                        break;
                    }
                    case 2: {
                        current.setImg(data.getString(j));
                        break;
                    }
                    case 3: {
                        current.setTime(data.getString(j));
                        break;
                    }
                    case 4: {
                        current.setDate(data.getString(j));
                        break;
                    }
                    case 5: {
                        current.setLocation(data.getString(j));
                        break;
                    }
                    case 6: {
                        current.setBailouts(data.getString(j));
                        break;
                    }
                    case 7: {
                        current.setNotes(data.getString(j));
                        break;
                    }
                }
            }
            dates.add(current);
            data.moveToNext();
        }
    }

    private Cursor readData() {
        DateReaderDbHelper mDbHelper = new DateReaderDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
            DateReaderContract.DateEntry.COLUMN_NAME_ID,
            DateReaderContract.DateEntry.COLUMN_NAME_NAME,
            DateReaderContract.DateEntry.COLUMN_NAME_IMAGE_PATH,
            DateReaderContract.DateEntry.COLUMN_NAME_TIME,
            DateReaderContract.DateEntry.COLUMN_NAME_DATE,
            DateReaderContract.DateEntry.COLUMN_NAME_LOCATION,
            DateReaderContract.DateEntry.COLUMN_NAME_CONTACTS,
            DateReaderContract.DateEntry.COLUMN_NAME_NOTES

        };

        String sortOrder = DateReaderContract.DateEntry.COLUMN_NAME_DATE + " DESC";

        Cursor data = db.query(
            DateReaderContract.DateEntry.TABLE_NAME,  // The table to query
            projection,                               // The columns to return
            null,                                // The columns for the WHERE clause
            null,                            // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            sortOrder                                 // The sort order
        );
        data.moveToFirst();
        return data;
    }

    private void populateFakeData() {
        // Gets the data repository in write mode
        // todo:remove
        DateReaderDbHelper mDbHelper = new DateReaderDbHelper(getContext());
        DateReaderContract x = new DateReaderContract();
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL(x.getDownString());
        db.execSQL(x.getUpString());
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);

        ArrayList<String> names = new ArrayList<>();
        names.add("A");
        names.add("B");
        names.add("C");

        ArrayList<String> dates = new ArrayList<>();
        dates.add("2016-2-14");
        dates.add("2016-3-14");
        dates.add("2016-4-14");

        ArrayList<String> times = new ArrayList<>();
        times.add("14:00:14");
        times.add("14:00:14");
        times.add("14:00:14");


        // Create a new map of values, where column names are the keys
        for (int i = 0; i < 3; i++) {
            ContentValues values = new ContentValues();
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_ID, ids.get(i));
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_NAME, names.get(i));
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_IMAGE_PATH, names.get(i));
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_TIME, times.get(i));
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_DATE, dates.get(i));
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_LOCATION, names.get(i));
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_CONTACTS, names.get(i));
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_NOTES, names.get(i));
            // Insert the new row, returning the primary key value of the new row
            db.insert(
                DateReaderContract.DateEntry.TABLE_NAME,
                null,
                values);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_date_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyDateRecyclerViewAdapter(items, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DateFragmentInteractionListener) {
            mListener = (DateFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface DateFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListClick(DateInfo item);
    }
}
