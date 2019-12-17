package com.updesigns.moneycircle;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

//Lists cannot be stored directly in room. so we need a type converter for it. This particular on is for a list of Circles.class and Transaction.class
// it changes the list into a single string

public class GithubTypeConverters {

    static Gson gson = new Gson();

    //For Circle List

    @TypeConverter
    public static List<Circles> stringToCircleList(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Circles>>() {}.getType();

        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String circleListToString(List<Circles> user) {
        return gson.toJson(user);
    }

    //For transaction list

    @TypeConverter
    public static List<Transaction> stringToTransList(String value) {
        if (value == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<Transaction>>() {}.getType();

        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String transListToString(List<Transaction> user) {
        return gson.toJson(user);
    }
}