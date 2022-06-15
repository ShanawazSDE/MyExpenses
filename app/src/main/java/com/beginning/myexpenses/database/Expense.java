package com.beginning.myexpenses.database;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.beginning.myexpenses.BR;

@Entity
public class Expense extends BaseObservable implements Comparable, Cloneable {
    @PrimaryKey(autoGenerate = true)
    int id;
    String title;
    String price;
    @Bindable
    String date;

    public Expense(int id, String title, String price, String date) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.date = date;
    }

    @Ignore
    public Expense() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Override
    public int compareTo(Object o) {
        Expense exp = (Expense) o;
        return  ( exp.getTitle().equals(this.getTitle()) && exp.getPrice().equals(this.getPrice())
                && exp.getDate().equals(this.getDate()) ) ? 1 : 0;

    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return (Expense) super.clone();
    }
}
