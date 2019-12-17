package com.updesigns.moneycircle;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

//Database class DAO, Entity, version and typeconverters are defined here..
//***You will need to increase the version number here anytime you change something in the entity...
@Database(entities = Circles.class, version = 8, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class circleDatabase extends RoomDatabase{
    public abstract circleDao circleDao();
}
