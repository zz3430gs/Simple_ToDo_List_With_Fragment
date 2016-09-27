package com.example.zz3430gs.simple_todo_list_with_fragment;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoListArrayAdapter extends ArrayAdapter<ToDoItem> {

    Activity mActivity;

    public ToDoListArrayAdapter(Context context, int resource, ArrayList<ToDoItem> items) {
        super(context, resource, items);
        this.mActivity = (Activity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.todo_list_item_list_element, parent, false);

            ToDoItem item = getItem(position);

            TextView todoText = (TextView) rowView.findViewById(R.id.todo_list_text_textview);
            TextView todoDate = (TextView) rowView.findViewById(R.id.todo_list_date_textview);
            CheckBox todoUrgent = (CheckBox) rowView.findViewById(R.id.todo_list_urgent_checkbox);

            todoText.setText(item.getText());
            todoDate.setText(item.getDateCreated().toString());
            todoUrgent.setChecked(item.isUrgent());

        }
        return rowView;
    }
}

