package com.shuvo.ttit.petukfund.inouthistory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.inouthistory.lists.TransactionInfoList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TRNSHolder> {

    private final ArrayList<TransactionInfoList> mCategoryItem;
    private final Context myContext;

    public TransactionAdapter(ArrayList<TransactionInfoList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public TRNSHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.trans_history_list_view_layout, parent, false);
        return new TRNSHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TRNSHolder holder, int position) {

        TransactionInfoList transactionInfoList = mCategoryItem.get(position);

        holder.amount.setText(transactionInfoList.getAmount());
        holder.name.setText(transactionInfoList.getpName());
        String tDate = transactionInfoList.gettDate();
        tDate = tDate.toUpperCase(Locale.ROOT);

//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
//        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd MMMM, yyyy",Locale.getDefault());
//        Date date = null;
//        try {
//            date = simpleDateFormat.parse(tDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (date != null) {
//            tDate = simpleDateFormat1.format(date);
//        }

        holder.date.setText(tDate);

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class TRNSHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView name;
        TextView amount;

        public TRNSHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date_of_transaction);
            name = itemView.findViewById(R.id.name_of_transactor);
            amount = itemView.findViewById(R.id.amount_of_transaction);

        }
    }
}
