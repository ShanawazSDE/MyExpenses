package com.beginning.myexpenses.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ExpenseDAO {

    @Insert
    void insertExpense(Expense expense);

    @Query("select * from Expense")
    List<Expense> getAllExpenses();

    @Delete
    void deleteExpense(Expense expense);
}
