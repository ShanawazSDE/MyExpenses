package com.beginning.myexpenses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.beginning.myexpenses.database.Expense;
import com.beginning.myexpenses.database.ExpenseDB;
import com.beginning.myexpenses.databinding.ActivityMainBinding;


import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    ExpenseDB expenseDB;
    RecyclerView recyclerView;
    ArrayList<Expense> expenseList ;
    ArrayList<Expense> newList = new ArrayList<>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activityMainBinding.setClickHandlers(new ClickHandlers(this));

        expenseDB = Room.databaseBuilder(getApplicationContext(),
                ExpenseDB.class, "ExpenseDB").build();

        recyclerView = activityMainBinding.recyclerView;

        loadData();


    }

    private View.OnClickListener deleteIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            MyAdapter.ExpenseViewHolder holder = ( MyAdapter.ExpenseViewHolder) view.getTag();
            //Toast.makeText(MainActivity.this, ""+holder.getAbsoluteAdapterPosition(), Toast.LENGTH_SHORT).show();
            deleteExpense(holder.getAbsoluteAdapterPosition());
        }
    };

    private void loadData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                expenseList = (ArrayList<Expense>) expenseDB.getExpenseDAO().getAllExpenses();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(MainActivity.this, ""+expenseList.size(), Toast.LENGTH_SHORT).show();
                        adapter = new MyAdapter(expenseList,deleteIconClickListener,MainActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        updateTotalPrice(expenseList);
                    }
                });

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {

            String title = data.getStringExtra("title");
            String price = data.getStringExtra("price");
            String date = data.getStringExtra("date");
            Expense expense = new Expense(0, title, price,date);
            addExpense(expense);
            //Toast.makeText(MainActivity.this, "inserted", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateTotalPrice(ArrayList<Expense> list){

        double sum = 0.0;
        for(Expense expense : list){
            sum += Double.parseDouble(expense.getPrice());
        }
        activityMainBinding.totalPrice.setText(String.valueOf("₹ "+sum));
    }
    private void addExpense(Expense expense) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                expenseDB.getExpenseDAO().insertExpense(expense);
                if(! newList.isEmpty()){
                    newList.clear();
                }
                newList.addAll((ArrayList<Expense>) expenseDB.getExpenseDAO().getAllExpenses());

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        adapter.updateRecyclerView(newList);
                        recyclerView.scrollToPosition(newList.size() - 1);
                        updateTotalPrice(newList);
                        Toast.makeText(MainActivity.this, "Inserted", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    private void deleteExpense(int position) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                expenseDB.getExpenseDAO().deleteExpense(expenseList.get(position));
                if(! newList.isEmpty()){
                    newList.clear();
                }
                newList.addAll((ArrayList<Expense>) expenseDB.getExpenseDAO().getAllExpenses());
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        adapter.updateRecyclerView(newList);
                        updateTotalPrice(newList);
                        Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }


    public class ClickHandlers {
        Context context;

        public ClickHandlers(Context context) {
            this.context = context;
        }

        public void onAddClicked(View view) {
            // Toast.makeText(context, "Add clcikde", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivityForResult(i, 1);
        }
    }

}