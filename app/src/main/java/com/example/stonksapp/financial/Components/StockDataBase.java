package com.example.stonksapp.financial.Components;

import androidx.room.Database;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Insert;
import androidx.room.Delete;
import androidx.room.RoomDatabase;

import java.util.List;

public class StockDataBase {
    @Dao
    public interface StockDao {
        @Query("SELECT * FROM stock")
        List<String> getAll();

        @Insert
        void insertAll(Stock... stocks);

        @Insert
        void insert(Stock stock);

        @Delete
        void delete(Stock stock);
    }

    @Database(entities = (Stock.class), version = 1)
    public abstract static class StockDB extends RoomDatabase {
        public abstract StockDao stockDao();
    }

}
