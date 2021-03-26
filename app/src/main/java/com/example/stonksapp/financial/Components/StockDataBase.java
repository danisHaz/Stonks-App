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
import android.util.Log;

import com.example.stonksapp.Constants;
import com.example.stonksapp.UI.Components.OnCompleteListener;
import com.example.stonksapp.UI.Components.WorkDoneListener;

import java.lang.Thread;
import java.lang.NullPointerException;
import java.lang.Class;
import java.util.List;

public class StockDataBase {
    private StockDB mDb;
    private static StockDataBase dataBase;
    private static int initializeNumber = 0;

    public static synchronized StockDataBase createInstance(Context context) {
        if (dataBase == null) {
            dataBase = new StockDataBase();
            dataBase.mDb = Room.databaseBuilder(context, StockDB.class,
                    String.format("%dDataBase", StockDataBase.initializeNumber++)).build();
        }
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

    public void updateCurrent(final Stock stock) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                CurrentStockDao dao = mDb.currentDao();
                dao.update(DefaultStock.from(stock));
            }
        });

        thread.start();
    }

    public void insertCurrent(final Stock stock) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                 CurrentStockDao dao = mDb.currentDao();
                 dao.insert(DefaultStock.from(stock));
            }
        });

        thread.start();
    }

    // todo: RxJava
    public void getAll(final Class<? extends FavouriteObject> objClass) throws NullPointerException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                if (objClass == FavouriteStock.class) {
                    StockDao dao = mDb.stockDao();
                    FavouriteStock.currentFavourites = dao.getAll();
                    WorkDoneListener.complete(Constants.DO_FAVOURITE_DEFINITION_WORK,
                            OnCompleteListener.Result.SUCCESS);
                } else if (objClass == WatchingStocks.class) {
                    List<DefaultStock> defList = mDb.currentDao().getAll();
                    for (DefaultStock stockie: defList) {
                        WatchingStocks.watchingStocks.add(Stock.from(stockie));
                    }
                    WorkDoneListener.complete(Constants.DO_WATCHING_DEFINITION_WORK,
                            OnCompleteListener.Result.SUCCESS);
                }
            }
        });
        thread.setDaemon(true);
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

    @Dao
    public interface CurrentStockDao {
        @Query("SELECT * FROM defaultstock")
        List<DefaultStock> getAll();

        @Insert
        void insertAll(DefaultStock... stocks);

        @Insert
        void insert(DefaultStock stock);

        @Delete
        void delete(DefaultStock stock);

        @Update
        void update(DefaultStock stock);
    }


    @Database(entities = {Stock.class, DefaultStock.class}, version = 1)
    public abstract static class StockDB extends RoomDatabase {
        public abstract StockDao stockDao();
        public abstract CurrentStockDao currentDao();
    }

}
