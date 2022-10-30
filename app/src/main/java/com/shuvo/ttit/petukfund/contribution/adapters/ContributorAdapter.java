package com.shuvo.ttit.petukfund.contribution.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.contribution.lists.ContributionLIst;

import java.util.ArrayList;

public class ContributorAdapter extends RecyclerView.Adapter<ContributorAdapter.CHolder> {

    private final ArrayList<ContributionLIst> mCategoryItem;
    private final Context myContext;

    public ContributorAdapter(ArrayList<ContributionLIst> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public CHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.contribution_user_layout, parent, false);
        return new CHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CHolder holder, int position) {
        ContributionLIst contributionLIst = mCategoryItem.get(position);

        holder.amount.setText(contributionLIst.getAmount());
        holder.name.setText(contributionLIst.getName());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class CHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView amount;

        public CHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_of_contributor);
            amount = itemView.findViewById(R.id.amount_of_contribution);

        }
    }
}
