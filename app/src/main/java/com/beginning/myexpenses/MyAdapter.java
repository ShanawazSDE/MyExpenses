package com.beginning.myexpenses;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.beginning.myexpenses.database.Expense;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ExpenseViewHolder> {

    private ArrayList<Expense> expenseList;
    private View.OnClickListener deleteIconClickListener;
     private Context context;
     private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();




    public MyAdapter(ArrayList<Expense> expenseList, View.OnClickListener deleteIconClickListener,
                    Context context) {
        this.expenseList = expenseList;
        this.deleteIconClickListener = deleteIconClickListener;
        this.context = context;

    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.recycler_item,parent,false);
    return new ExpenseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {

        holder.price.setText(expenseList.get(position).getPrice());
        holder.price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.title.setText(expenseList.get(position).getTitle());
        holder.date.setText(expenseList.get(position).getDate());
        holder.deleteIcon.setTag(holder);
        holder.deleteIcon.setOnClickListener(deleteIconClickListener);



       viewBinderHelper.setOpenOnlyOne(true);
       viewBinderHelper.bind(holder.swipeRevealLayout, String.valueOf(expenseList.get(position).getId()));

    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull List<Object> payloads) {

        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, position, payloads);
        }

        else {
            Expense expense = expenseList.get(position);
            Bundle object = (Bundle) payloads.get(0);
            for(String key : object.keySet()){
                if(key.equals("price")){
                    holder.price.setText(expense.getPrice());
                }
                else if(key.equals("title")){

                    holder.title.setText(expense.getTitle());
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return (expenseList.size() > 0) ? expenseList.size() : 0;
    }


    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
       public TextView title,price,date;
       public ImageView deleteIcon;
       public SwipeRevealLayout swipeRevealLayout;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.tv_title);
            this.price = itemView.findViewById(R.id.tv_price);
            this.date = itemView.findViewById(R.id.tv_date);
            this.deleteIcon = itemView.findViewById(R.id.deleteIcon);
            this.swipeRevealLayout = itemView.findViewById(R.id.swipeRecItem);



        }
    }

    public void updateRecyclerView( ArrayList<Expense> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallback(expenseList, newList));
        diffResult.dispatchUpdatesTo(this);
        expenseList.clear();
        expenseList.addAll(newList);
    }



}
