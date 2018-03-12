package com.projekat.vgalovic.todolist.db;

import android.provider.BaseColumns;

/**
 * Created by vgalovic on 11.02.2018..
 */

public class dbProperties {
    public static final String DB_NAME = "com.projekat.vgalovic.todolist.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "todoDB";
        public static final String COL_TASK_TITLE = "task";
    }
}
