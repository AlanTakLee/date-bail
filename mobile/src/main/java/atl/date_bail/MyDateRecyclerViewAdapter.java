package atl.date_bail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import atl.date_bail.model.DateInfo;

public class MyDateRecyclerViewAdapter extends RecyclerView.Adapter<MyDateRecyclerViewAdapter.ViewHolder> {

    private final List<DateInfo> mValues;
    private final DateFragment.DateFragmentInteractionListener mListener;

    public MyDateRecyclerViewAdapter(List<DateInfo> items, DateFragment.DateFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.date_summary_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.titleTxt.setText(mValues.get(position).getName());
        holder.timeTxt.setText(mValues.get(position).getDate());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListClick(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView titleTxt;
        public final TextView timeTxt;
        public final ImageView iconImg;
        public DateInfo mItem;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            titleTxt = (TextView) view.findViewById(R.id.dateSummaryTitleTxt);
            timeTxt = (TextView) view.findViewById(R.id.dateSummaryTimeTxt);
            iconImg = (ImageView) view.findViewById(R.id.dateSummaryImg);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + timeTxt.getText() + "'";
        }
    }
}
