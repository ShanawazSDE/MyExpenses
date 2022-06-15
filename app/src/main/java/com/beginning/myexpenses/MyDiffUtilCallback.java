package com.beginning.myexpenses;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.beginning.myexpenses.database.Expense;

import java.util.ArrayList;

public class MyDiffUtilCallback extends DiffUtil.Callback {

    ArrayList<Expense> oldExpenses = new ArrayList<>();
    ArrayList<Expense> newExpenses = new ArrayList<>();

    public MyDiffUtilCallback(ArrayList<Expense> oldExpenses, ArrayList<Expense> newExpenses) {
        this.oldExpenses = oldExpenses;
        this.newExpenses = newExpenses;
    }

    @Override
    public int getOldListSize() {
        return Math.max(oldExpenses.size(), 0);
    }

    @Override
    public int getNewListSize() {
          return Math.max(newExpenses.size(), 0);
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {

        return oldExpenses.get(oldItemPosition).getId() == newExpenses.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = oldExpenses.get(oldItemPosition).compareTo(newExpenses.get(newItemPosition));
      return result > 0;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Expense oldItem = oldExpenses.get(oldItemPosition);
        Expense newItem = newExpenses.get(newItemPosition);
        Bundle diffBundle = new Bundle();

        if(!oldItem.getPrice().equals(newItem.getPrice())){
            diffBundle.putString("price", newItem.getPrice());
        }

        if(!oldItem.getTitle().equals(newItem.getTitle())){
            diffBundle.putString("title", newItem.getTitle());
        }
        return (diffBundle.size() == 0) ? null : diffBundle;
    }
}
