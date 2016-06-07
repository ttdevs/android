/*
 * Created by ttdevs at 16-5-6 下午4:23.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.deeplinking.database;


/**
 * Created by simister on 10/21/14.
 */
public class RecipeTable {

    // Database table
    public static final String TABLE = "recipes";
    public static final String ID_COLUMN = "_id";
    public static final String ID = TABLE + "." + ID_COLUMN;
    public static final String TITLE_COLUMN = "title";
    public static final String TITLE = TABLE + "." + TITLE_COLUMN;
    public static final String PHOTO_COLUMN = "photo";
    public static final String PHOTO = TABLE + "." + PHOTO_COLUMN;
    public static final String PREP_TIME_COLUMN = "prep_time";
    public static final String PREP_TIME = TABLE + "." + PREP_TIME_COLUMN;
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DESCRIPTION = TABLE + "." + DESCRIPTION_COLUMN;
}
