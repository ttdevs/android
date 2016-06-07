/*
 * Created by ttdevs at 16-5-6 下午4:23.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.deeplinking.database;

/**
 * Created by simister on 10/22/14.
 */
public class RecipeIngredientTable {
    public static final String TABLE = "recipe_ingredients";
    public static final String ID_COLUMN = "_id";
    public static final String ID = TABLE + "." + ID_COLUMN;
    public static final String RECIPE_ID_COLUMN ="recipe_id";
    public static final String RECIPE_ID = TABLE + "." + RECIPE_ID_COLUMN;
    public static final String AMOUNT_COLUMN = "amount";
    public static final String AMOUNT = TABLE + "." + AMOUNT_COLUMN;
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String DESCRIPTION = TABLE + "." + DESCRIPTION_COLUMN;
}
