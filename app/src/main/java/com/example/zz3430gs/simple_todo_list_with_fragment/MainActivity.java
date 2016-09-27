package com.example.zz3430gs.simple_todo_list_with_fragment;

import android.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {//implements AddToDoItemFragment.NewItemCreatedListener, ToDoItemDetailFragment.MarkItemAsDoneListener, ToDoListFragment.ListItemSelectedListener {


    private static final String TODO_ITEMS_KEY = "TODO ITEM ARRAY LIST";
    private static final String ADD_NEW_FRAG_TAG = "ADD NEW FRAGMENT";
    private static final String LIST_FRAG_TAG = "LIST FRAGMENT";
    private static final String DETAIL_FRAG_TAG = "DETAIL FRAGMENT";

    private ArrayList<ToDoItem> mTodoItems;

    private static final String TAG = "MAIN ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addNewButton = (Button) findViewById(R.id.add_todo_item_button);
        final EditText newToDoEditText = (EditText) findViewById(R.id.new_todo_item_edittext);

        ListView todoListView = (ListView) findViewById(R.id.to_do_listview);

        final ToDoListArrayAdapter toDoListAdapter = new ToDoListArrayAdapter(this, R.layout.todo_list_item_list_element);

        todoListView.setAdapter(toDoListAdapter);

        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItemText = newToDoEditText.getText().toString();

                if (newItemText.length() == 0){
                    Toast.makeText(MainActivity.this, "Enter a todo item", Toast.LENGTH_LONG).show();
                    return;
                }
                ToDoItem newItem = new ToDoItem(newItemText);
                toDoListAdapter.add(new ToDoItem(newItemText));
                toDoListAdapter.notifyDataSetChanged();
                newToDoEditText.getText().clear();
            }

        });


        if (savedInstanceState == null) {
            //no saved instance state - first time Activity been created
            //Create new ArrayList, and add Add and List fragments.
            Log.d(TAG, "onCreate has no instance state. Set up ArrayList, add List Fragment and Add fragment");

            mTodoItems = new ArrayList<>();

            AddToDoItemFragment addNewFragment = AddToDoItemFragment.newInstance();
            ToDoListFragment listFragment = ToDoListFragment.newInstance(mTodoItems);

            FragmentManager fm = getFragmentManager();
            android.app.FragmentTransaction ft = fm.beginTransaction();

            ft.add(R.id.add_todo_view_container, addNewFragment, ADD_NEW_FRAG_TAG);
            ft.add(R.id.todo_list_view_container, listFragment, LIST_FRAG_TAG);

            ft.commit();

        }
        else {
            //There is saved instance state, so the app has already run,
            //and the Activity should already have fragments.
            //Restore saved instance state, the ArrayList

            mTodoItems = savedInstanceState.getParcelableArrayList(TODO_ITEMS_KEY);
            Log.d(TAG, "onCreate has saved instance state ArrayList =  " + mTodoItems);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        super.onSaveInstanceState(outBundle);
        outBundle.putParcelableArrayList(TODO_ITEMS_KEY, mTodoItems);
    }



    @Override
    public void newItemCreated(ToDoItem newItem) {

        //Add item to the ArrayList
        mTodoItems.add(newItem);

        Log.d(TAG, "newItemCreated =  " + mTodoItems);

        //get reference to list Fragment from the FragmentMananger,
        // and tell this Fragment that the data set has changed
        FragmentManager fm = getFragmentManager();
        ToDoListFragment listFragment = (ToDoListFragment) fm.findFragmentByTag(LIST_FRAG_TAG);
        listFragment.notifyItemsChanged();
    }


    @Override
    public void itemSelected(ToDoItem selected) {

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
        ToDoItemDetailFragment detailFragment = ToDoItemDetailFragment.newInstance(selected);

//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//
//        //Create a new Detail fragment. Add it to the Activity.
//        ToDoItemDetailFragment detailFragment = ToDoItemDetailFragment.newInstance(selected);
        ft.add(android.R.id.content, detailFragment);
        // Add to the back stack, so if user presses back button from the Detail
        // fragment, it will revert this transaction - Activity will go back to the Add+List fragments
        ft.addToBackStack(DETAIL_FRAG_TAG);

        ft.commit();
    }


    @Override
    public void todoItemDone(ToDoItem doneItem) {

        //Remove item from list
        mTodoItems.remove(doneItem);

        Log.d(TAG, "newItemRemoved list is now  =  " + mTodoItems);

        //Find List fragment and tell it that the  data has changed
        FragmentManager fm = getFragmentManager();
        ToDoListFragment listFragment = (ToDoListFragment) fm.findFragmentByTag(LIST_FRAG_TAG);
        listFragment.notifyItemsChanged();

        // Revert the last fragment transaction on the back stack.
        // This removes the Detail fragment from the Activity, which leaves the Add+List fragments.

        android.app.FragmentTransaction ft = fm.beginTransaction();
        fm.popBackStack();
        ft.commit();
    }

}
