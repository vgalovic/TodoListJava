package com.projekat.vgalovic.todolist;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.projekat.vgalovic.todolist.db.dbHelper;
import com.projekat.vgalovic.todolist.db.dbProperties;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String title = "New Task";
    public static final String message = "Add a new task";
    public static final String positive = "Add";
    public static final String negative = "Cancel";

    private dbHelper mHelper;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private EditText search;

    private String task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper = new dbHelper(this);
        mListView = findViewById(R.id.listView);
        search = findViewById(R.id.editText);

        updateUI();
        Search();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add_task:
                final EditText taskEditText = new EditText(this);
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(title)
                        .setMessage(message)
                        .setView(taskEditText)
                        .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogue, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                SQLiteDatabase db = mHelper.getWritableDatabase();
                                ContentValues values = new ContentValues();
                                values.put(dbProperties.TaskEntry.COL_TASK_TITLE, task);
                                db.insertWithOnConflict(dbProperties.TaskEntry.TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                                db.close();
                                updateUI();
                            }
                        })
                        .setNegativeButton(negative, null)
                        .create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(dbProperties.TaskEntry.TABLE,
                new String[] {dbProperties.TaskEntry.COL_TASK_TITLE}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(dbProperties.TaskEntry.COL_TASK_TITLE);
            taskList.add(cursor.getString(index));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this, R.layout.item, R.id.textView, taskList);
            mListView.setAdapter(mAdapter);

        } else {
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();
    }


    public void changeTask(View view){
        View parent = (View) view.getParent();
        ContentValues contentValues = new ContentValues();
        TextView taskTextView = parent.findViewById(R.id.textView);
        setContentView(R.layout.item);
        task = String.valueOf(taskTextView.getText());
        String newTask = "<font color=#8d8d8d><,strike>" + String.valueOf(task) + "</strike></font>";

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.update(dbProperties.TaskEntry.TABLE, contentValues, dbProperties.TaskEntry.COL_TASK_TITLE + " = ?", new String[] {newTask});
        db.close();
        updateUI();
    }

    public void deleteTask(View view) {
        View parent = (View) view.getParent();
        TextView taskTextView = parent.findViewById(R.id.textView);
        task = String.valueOf(taskTextView.getText());
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(dbProperties.TaskEntry.TABLE, dbProperties.TaskEntry.COL_TASK_TITLE + " = ?", new String[] {task});
        db.close();
        updateUI();
    }

    public void Search(){
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                (MainActivity.this).mAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
