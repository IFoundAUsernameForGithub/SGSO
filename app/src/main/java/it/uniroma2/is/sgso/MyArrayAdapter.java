package it.uniroma2.is.sgso;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;


public class MyArrayAdapter<T> extends ArrayAdapter {
    private ArrayList<String> data;

    public MyArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
        data = (ArrayList<String>) objects;
    }

    public int size(){
        return data.size();
    }

}
