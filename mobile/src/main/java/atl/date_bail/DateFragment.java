package atl.date_bail;

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

import atl.date_bail.adapters.MyDateRecyclerViewAdapter;
import atl.date_bail.model.DateInfo;
import atl.date_bail.model.DateReaderContract;
import atl.date_bail.model.DateReaderDbHelper;
import atl.date_bail.model.IdHolder;

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
        populateArray(readData(), items);
    }

    // date a cursor to all "Date" data and populate a list
    private void populateArray(Cursor data, List<DateInfo> dates) {
        while (data.moveToNext()) {
            DateInfo current = new DateInfo();
            for (int j = 1; j < data.getColumnCount(); j++) {
                switch (j) {
                    case 1: {
                        current.setId(data.getLong(j));
                        break;
                    }
                    case 2: {
                        current.setName(data.getString(j));
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
        }
        data.moveToLast();
        if (data.getPosition() > 0) {
            IdHolder.getInstance().setLastId(data.getLong(data.getColumnIndex(DateReaderContract.DateEntry.COLUMN_NAME_ID)) + 1);
        } else {
            IdHolder.getInstance().setLastId(0L);
        }
    }

    // Query and return data cursor
    private Cursor readData() {
        DateReaderDbHelper mDbHelper = new DateReaderDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sortOrder = DateReaderContract.DateEntry.COLUMN_NAME_ID + " ASC";

        return db.query(
            DateReaderContract.DateEntry.TABLE_NAME,  // The table to query
            null,                               // The columns to return
            null,                                // The columns for the WHERE clause
            null,                            // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            sortOrder                                 // The sort order
        );
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
        void onListClick(DateInfo item);
    }
}
