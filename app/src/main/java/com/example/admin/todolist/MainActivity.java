package com.example.admin.todolist;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.admin.todolist.adapter.MyAdapter;
import com.example.admin.todolist.database.DatabaseHelper;
import com.example.admin.todolist.dialogbox.AlertdialogWithInfo;
import com.example.admin.todolist.dialogbox.InformationDialog;
import com.example.admin.todolist.model.EachRow;
import com.example.admin.todolist.adapter.MyAdapter;
import com.example.admin.todolist.database.DatabaseHelper;
import com.example.admin.todolist.model.EachRow;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements MyAdapter.ClickInterface {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ArrayList<EachRow> arrayList;
    DatabaseHelper helper;
    String currentdate;
    private static final int NOTIFY_ID = 100;
    MyAdapter adapter;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting Toolbar -- menu_items
        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        helper = new DatabaseHelper(MainActivity.this);

        //Providing View that represent items in a data set.
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        arrayList = helper.getalldetails();

        //Sorting the list
        Collections.sort(arrayList);
        adapter = new MyAdapter(MainActivity.this,arrayList);
        adapter = new MyAdapter(MainActivity.this, arrayList);
        recyclerView.setAdapter(adapter);
        adapter.setClickInterface(this);
        currentdate = adapter.getcurrentdate();

        // Save state
        Parcelable recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();

        // Restore state
        recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    @Override
    //Creating OptionsMenu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    //On OptionsMenu Click Event -- Action will performed .
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.Add_menu:
                InformationDialog infodialog = new InformationDialog();
                infodialog.show(getFragmentManager(), "Information");
                break;
            case R.id.completed_menu:
                Intent intent = new Intent(MainActivity.this, FinishedActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onsingleclick(int position) {

        String title = arrayList.get(position).getTitle();
        String description = arrayList.get(position).getDescription();
        String date = arrayList.get(position).getDate();
        int id = arrayList.get(position).getId();
        AlertdialogWithInfo alertdialogWithInfo = new AlertdialogWithInfo();
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        bundle.putString("Description", description);
        bundle.putString("Date", date);
        bundle.putInt("Id", id);
        alertdialogWithInfo.setArguments(bundle);
        alertdialogWithInfo.show(getFragmentManager(), "AlertDialogInfo");
    }

    @Override
    //On Long Click List/task will be moved to finished_activity
    public void onLongclick(int position) {
        int id = arrayList.get(position).getId();
        long updatestatus = helper.updatestatus(id);
        if (updatestatus > 0) {
            Snackbar task_snack = Snackbar.make(recyclerView, "Task Finished. Thumbs up for Finished Task", Snackbar.LENGTH_LONG);
            task_snack.show();
        } else {
            Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

}