package com.beginning.myexpenses.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Expense.class},version = 1)
public abstract class ExpenseDB extends RoomDatabase {
    public abstract ExpenseDAO getExpenseDAO();
}
