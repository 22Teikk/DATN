package com.teikk.datn.data.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.teikk.datn.data.service.dao.CategoryDao
import com.teikk.datn.data.service.dao.PaymentMethodDao
import com.teikk.datn.data.service.dao.ProductDao
import com.teikk.datn.data.service.dao.RoleDao
import com.teikk.datn.data.service.dao.UserProfileDao
import com.teikk.datn.data.model.Category
import com.teikk.datn.data.model.PaymentMethod
import com.teikk.datn.data.model.Product
import com.teikk.datn.data.model.Role
import com.teikk.datn.data.model.UserProfile

@Database(entities = [Product::class, Role::class, Category::class, UserProfile::class, PaymentMethod::class], version = 1, exportSchema = false)
abstract class DatabaseApp : RoomDatabase() {
    abstract fun roleDao() : RoleDao
    abstract fun categoryDao() : CategoryDao
    abstract fun paymentMethodDao() : PaymentMethodDao
    abstract fun userProfileDao() : UserProfileDao
    abstract fun productDao() : ProductDao
    companion object{
        @Volatile
        private var INSTANCE: DatabaseApp? = null
        fun getDatabase(context: Context): DatabaseApp {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseApp::class.java,
                    "user_db"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}