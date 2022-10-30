package com.shuvo.ttit.petukfund.monthlyHistory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.monthlyHistory.lists.InOutBalanceSheetList;

import java.util.ArrayList;

public class InOutBalanceAdapter extends RecyclerView.Adapter<InOutBalanceAdapter.IOBHolder> {

    private final ArrayList<InOutBalanceSheetList> mCategoryItem;
    private final Context myContext;

    public InOutBalanceAdapter(ArrayList<InOutBalanceSheetList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public IOBHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.in_out_balance_sheet_layout, parent, false);
        return new IOBHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IOBHolder holder, int position) {

        InOutBalanceSheetList inOutBalanceSheetList = mCategoryItem.get(position);

        holder.slNo.setText(inOutBalanceSheetList.getSl_no());
        holder.date.setText(inOutBalanceSheetList.getDate_balance());
        holder.name.setText(inOutBalanceSheetList.getName());
        holder.inAmount.setText(inOutBalanceSheetList.getIn_amount());
        holder.outAmount.setText(inOutBalanceSheetList.getOut_amount());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class IOBHolder extends RecyclerView.ViewHolder {

        TextView slNo;
        TextView date;
        TextView name;
        TextView inAmount;
        TextView outAmount;

        public IOBHolder(@NonNull View itemView) {
            super(itemView);
            slNo = itemView.findViewById(R.id.sl_no_month_history);
            date = itemView.findViewById(R.id.date_month_history);
            name = itemView.findViewById(R.id.user_name_month_history);
            inAmount = itemView.findViewById(R.id.in_amount_month_history);
            outAmount = itemView.findViewById(R.id.out_amount_month_history);

        }
    }
}
