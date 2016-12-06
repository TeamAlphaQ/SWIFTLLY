package campusshark.dhruv.swiftly;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by dhruv on 4/12/16.
 */
public class rvAdapterType extends RecyclerView
        .Adapter<rvAdapterType
        .CardObjectHolder> {

    private Context context;
    public static final String TAG = "rvadapterType";
    private ArrayList<CardObjectTypeSelected> cardSet;
    private static MyClickListener1 myClickListener;
    public static class CardObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener
    {
        TextView name;
        TextView addr;
        TextView isOpen;
        ImageView img;

        public CardObjectHolder(View itemView)
        {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.txt_name);
            addr = (TextView) itemView.findViewById(R.id.txt_address);
            isOpen = (TextView) itemView.findViewById(R.id.txt_is_open);
            img = (ImageView) itemView.findViewById(R.id.img_card_type);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(),v);
        }
    }

    public void setOnItemClickListener(MyClickListener1 myClickListener)
    {
        this.myClickListener = myClickListener;
    }

    public rvAdapterType(ArrayList<CardObjectTypeSelected> myCardSet)
    {
        cardSet = myCardSet;
    }

    @Override
    public CardObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caedview_type_selected, parent, false);

        context = parent.getContext();
        CardObjectHolder cardObjectHolder = new CardObjectHolder(view);
        return cardObjectHolder;
    }

    @Override
    public void onBindViewHolder(CardObjectHolder holder, int position) {
        holder.name.setText(cardSet.get(position).getTvName());
        holder.addr.setText(cardSet.get(position).getTvAddress());
        holder.isOpen.setText(cardSet.get(position).getTvIsOpen());
        if(cardSet.get(position).getImgType()==null) {
            Picasso.with(context).load(cardSet.get(position).getImgType()).into(holder.img);
        }
        else
        {
            Picasso.with(context).load("http://www.freeiconspng.com/uploads/customer-service-icon-png-9.png").into(holder.img);
        }
//        holder.img.setImageResource(cardSet.get(position).getImgType());
    }

    public void addItem(CardObjectTypeSelected dataObj, int index) {
        cardSet.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        cardSet.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return cardSet.size();
    }

    public interface MyClickListener1 {
        public void onItemClick(int position, View v);
    }

}
