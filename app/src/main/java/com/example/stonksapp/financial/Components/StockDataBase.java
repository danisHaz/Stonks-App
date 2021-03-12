package com.example.stonksapp.financial.Components;

import androidx.room.Database;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.RoomDatabase;
import androidx.room.Room;
import androidx.room.Update;

import android.content.Context;

import java.util.ArrayList;
import java.lang.Thread;
import java.lang.NullPointerException;
import java.lang.Class;
import java.util.List;

public class StockDataBase {
    private StockDB mDb;
    private static int initializeNumber = 0;

    public static StockDataBase createInstance(Context context) {
        StockDataBase dataBase = new StockDataBase();
        dataBase.mDb = Room.databaseBuilder(context, StockDB.class,
                String.format("%dDataBase", StockDataBase.initializeNumber++)).build();
        return dataBase;
    }

    // todo: rewrite to more faster algorithm
    public void insertFavourite(final Stock stock) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                StockDao dao = mDb.stockDao();
                dao.insert(stock);
            }
        });

        thread.start();
    }

    public void deleteFromFavourites(final Stock stock) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                StockDao dao = mDb.stockDao();
                dao.delete(stock);
            }
        });

        thread.start();
    }

    public void updateFavourite(final Stock stock) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                StockDao dao = mDb.stockDao();
                dao.update(stock);
            }
        });

        thread.start();
    }

    // todo: RxJava
    public void getAll(final Class<? extends FavouriteObject> objClass) throws NullPointerException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                StockDao dao = mDb.stockDao();
                if (objClass == FavouriteStock.class) {
                    FavouriteStock.currentFavourites = dao.getAll();
                }
            }
        });

        thread.start();
    }

    @Dao
    public interface StockDao {
        @Query("SELECT * FROM stock")
        List<Stock> getAll();

        @Insert
        void insertAll(Stock... stocks);

        @Insert
        void insert(Stock stock);

        @Delete
        void delete(Stock stock);

        @Update
        void update(Stock stock);
    }

    @Database(entities = {Stock.class}, version = 1)
    public abstract static class StockDB extends RoomDatabase {
        public abstract StockDao stockDao();
    }

}
