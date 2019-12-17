package com.updesigns.moneycircle;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

//Database class DAO, Entity, version and typeconverters are defined here..
//***You will need to increase the version number here anytime you change something in the entity...
@Database(entities = User.class, version = 7, exportSchema = false)
@TypeConverters({Converters.class,GithubTypeConverters.class})
public abstract class database extends RoomDatabase{
    public abstract MyDao MyDao();
}
