package com.beginning.myexpenses;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.beginning.customcalendar.CustomCalendar;
import com.beginning.customcalendar.OnDateSelectedListener;
import com.beginning.customcalendar.Property;
import com.beginning.myexpenses.database.Expense;
import com.beginning.myexpenses.databinding.ActivityAddExpenseBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class AddExpenseActivity extends AppCompatActivity {
    private ActivityAddExpenseBinding activityAddExpenseBinding;
  private    Expense expense;
    private Date dateObject;
    private CustomCalendar customCalendar  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        activityAddExpenseBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_add_expense);


        expense = new Expense();
        dateObject = new Date();
        expense.setDate(new SimpleDateFormat("dd-MMM-yyyy").format(dateObject));
        activityAddExpenseBinding.setExpense(expense);

        ClickHandlers clickHandlers = new ClickHandlers();

        activityAddExpenseBinding.setClickHandlers(clickHandlers);

         customCalendar = activityAddExpenseBinding.customCalendar ;
         customCalendar.setNavigationButtonEnabled(CustomCalendar.PREVIOUS, false);
         customCalendar.setNavigationButtonEnabled(CustomCalendar.NEXT,false);
         customCalendar.animate();
         customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
             @Override
             public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                 if( selectedDate.get(Calendar.DAY_OF_MONTH) > Calendar.getInstance().get(Calendar.DAY_OF_MONTH)){
                     Toast.makeText(AddExpenseActivity.this,
                             "What!!! How can you add future date for today's purchasement", Toast.LENGTH_LONG).show();
                 return;
                 }

                 activityAddExpenseBinding.etTitle.setVisibility(View.VISIBLE);
                 activityAddExpenseBinding.etPrice.setVisibility(View.VISIBLE);
                 activityAddExpenseBinding.etDate.setVisibility(View.VISIBLE);
                 activityAddExpenseBinding.btnAdd.setVisibility(View.VISIBLE);
                 activityAddExpenseBinding.customCalendar.setVisibility(View.GONE);

                 expense.setDate(new SimpleDateFormat("dd-MMM-yyyy").format(selectedDate.getTime()));
                 //Toast.makeText(AddExpenseActivity.this, ""+selectedDate.get(Calendar.DAY_OF_MONTH), Toast.LENGTH_SHORT).show();
             }
         });

        HashMap<Object, Property> descMap = new HashMap<>();
        //currentdayLook
        Property currentDayProperty = new Property();
        currentDayProperty.layoutResource = R.layout.current_day_view;
        currentDayProperty.dateTextViewResource = R.id.text_view;
        descMap.put("currentDay", currentDayProperty);
        //normaldayLook
        Property normalDayProperty = new Property();
        normalDayProperty.layoutResource = R.layout.normal_day_view;
        normalDayProperty.dateTextViewResource = R.id.text_view;
        descMap.put("normalDay", normalDayProperty);

        customCalendar.setMapDescToProp(descMap);

        HashMap<Integer,Object> dateToDesc = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        //int daysOfThisMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for(int i = 1; i < 32; i++){
            if(i != calendar.get(Calendar.DAY_OF_MONTH))
            dateToDesc.put(i,"normalDay");
        }
        dateToDesc.put(calendar.get(Calendar.DAY_OF_MONTH), "currentDay");

        customCalendar.setDate(calendar,dateToDesc );






    }

    public class ClickHandlers{
        public void addClickHandler(View view){

            if(expense.getTitle()== null || expense.getPrice() == null || expense.getDate() == null){
                Toast.makeText(AddExpenseActivity.this, "All fields should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

                Intent i = new Intent();
                i.putExtra("title", expense.getTitle());
                i.putExtra("price", expense.getPrice());
                i.putExtra("date", expense.getDate());
                setResult(RESULT_OK, i);
                finish();

        }


        public void editTextDateClickHandler(View view){

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            activityAddExpenseBinding.etTitle.setVisibility(View.GONE);
            activityAddExpenseBinding.etPrice.setVisibility(View.GONE);
            activityAddExpenseBinding.etDate.setVisibility(View.GONE);
            activityAddExpenseBinding.btnAdd.setVisibility(View.GONE);
            activityAddExpenseBinding.customCalendar.setVisibility(View.VISIBLE);

        }
    }
}