package com.example.admin.todolist;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.admin.todolist.adapter.FinishedAdapter;
import com.example.admin.todolist.database.DatabaseHelper;
import com.example.admin.todolist.dialogbox.DeleteAllDialog;
import com.example.admin.todolist.model.FinishedEachrow;

import java.util.ArrayList;

/**
 * Created by dell on 8/6/2017.
 * Activity/Tasks Finished
 */

public class FinishedActivity extends AppCompatActivity implements FinishedAdapter.ClickFinishedListener {
    RecyclerView finishedrecycler;
    Toolbar finished_toolbar;
    ArrayList<FinishedEachrow> finishedlist;
    DatabaseHelper helper;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    //Only be called on the given API level or higher.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished);

        //ActionBarMenu -- finished_menu
        finished_toolbar = (Toolbar) findViewById(R.id.toolbar_finished);
        setSupportActionBar(finished_toolbar);
        finishedrecycler = (RecyclerView) findViewById(R.id.recycler_finished);
        helper = new DatabaseHelper(this);

        //Setting the RecyclerView.LayoutManager that this RecyclerView will use.
        finishedrecycler.setLayoutManager(new LinearLayoutManager(this));
        finishedrecycler.setItemAnimator(new DefaultItemAnimator());
        finishedlist = helper.getfinisheditems();
        int size = finishedlist.size();
        if (size == 0) {
            Snackbar no_task = Snackbar.make(finishedrecycler, "You Don't Have Any Completed Task", Snackbar.LENGTH_LONG);
            no_task.show();
        } else {
            FinishedAdapter finished_adapter = new FinishedAdapter(FinishedActivity.this, finishedlist);
            finishedrecycler.setAdapter(finished_adapter);
            finished_adapter.setClickFinishedListener(this);
        }
    }

    @Override
    //Implementing Action Bar
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.finished_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //On Options Menu Click Event
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.Back:
                Intent intentback = new Intent(FinishedActivity.this, MainActivity.class);
                startActivity(intentback);
                break;
            case R.id.Delete_all:
                DeleteAllDialog deleteall = new DeleteAllDialog();
                deleteall.show(getFragmentManager(), "Delete All");
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    //As Per Problem Requirement Single Click not Required
    public void onsinglefinishedclick(int position) {
       /* int id=finishedlist.get(position).getId();
        DeleteBox deletealert=new DeleteBox();
        Bundle bundle=new Bundle();
        bundle.putInt("Id",id);
        bundle.putInt("Position",position);
        deletealert.setArguments(bundle);
        deletealert.show(getFragmentManager(),"Delete alert");*/
    }

    @Override
    //Performing LongClick for Deleting Completed List
    public void onlongfinishedclick(int position) {
        int id = finishedlist.get(position).getId();
        long result = helper.deleteitemfromfinishedlist(id);
        if (result > 0) {
            Snackbar task_deleted = Snackbar.make(finishedrecycler, "Event Deleted Successfully ", Snackbar.LENGTH_LONG);
            task_deleted.show();
        } else {
            Toast.makeText(FinishedActivity.this, " Not Deleted....Something went wrong", Toast.LENGTH_LONG).show();
        }

    }
}
