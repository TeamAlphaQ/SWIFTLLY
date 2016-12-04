package campusshark.dhruv.swiftly;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dhruv on 4/12/16.
 */

public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {

    public static final String TAG = "myRecViewAdapter";
    private ArrayList<CardObject> mCardSet;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        ImageView imageDrawable;
        TextView service;

        public DataObjectHolder(View itemView) {
            super(itemView);
            imageDrawable = (ImageView) itemView.findViewById(R.id.img_card);
            service = (TextView) itemView.findViewById(R.id.txt_card);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {

        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<CardObject> myCardSet) {
        mCardSet = myCardSet;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.imageDrawable.setImageResource(mCardSet.get(position).getmDrawableImage());
        holder.service.setText(mCardSet.get(position).getmService());
    }

    public void addItem(CardObject cardObject, int index) {
        mCardSet.add(index, cardObject);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mCardSet.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mCardSet.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}
