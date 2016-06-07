/*
 * Created by ttdevs at 16-5-6 下午4:23.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.deeplinking.database;

/**
 * Created by simister on 10/24/14.
 */
public class RecipeInstructionsTable {
    public static final String TABLE = "recipe_instructions";
    public static final String ID_COLUMN = "_id";
    public static final String ID = TABLE + "." + ID_COLUMN;
    public static final String RECIPE_ID_COLUMN = "recipe_id";
    public static final String RECIPE_ID = TABLE + "." + RECIPE_ID_COLUMN;
    public static final String NUM_COLUMN = "num";
    public static final String NUM = TABLE + "." + NUM_COLUMN;
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DESCRIPTION = TABLE + "." + DESCRIPTION_COLUMN;
    public static final String PHOTO_COLUMN = "photo";
    public static final String PHOTO = TABLE + "." + PHOTO_COLUMN;
}
