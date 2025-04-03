package com.example.warehouseapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.warehouseapp.data.models.Goods
import com.example.warehouseapp.data.models.User
import com.example.warehouseapp.data.models.Warehouse
import com.example.warehouseapp.utils.UserRoles.ROLE_ADMIN
import com.example.warehouseapp.utils.UserRoles.ROLE_USER
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Warehouse::class, Goods::class, User::class],
    version = 1,
    exportSchema = false
)
abstract class WarehouseDatabase : RoomDatabase() {

    abstract fun warehouseDao(): WarehouseDao
    abstract fun goodsDao(): GoodsDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: WarehouseDatabase? = null

        fun getDatabase(context: Context): WarehouseDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, WarehouseDatabase::class.java, "warehouse_database")
                    .addCallback(roomCallback)
                    .build()
                    .also { Instance = it }
            }
        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Instance?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(
                            database.warehouseDao(),
                            database.goodsDao(),
                            database.userDao()
                        )
                    }
                }
            }
        }

        private suspend fun populateDatabase(
            warehouseDao: WarehouseDao,
            goodsDao: GoodsDao,
            userDao: UserDao
        ) {
            val warehousesList = listOf(
                Warehouse(1, "Main Warehouse", "ON", "123 Main Street", 720),
                Warehouse(2, "Backup Warehouse", "BC", "456 Second Avenue", 1100)
            )
            warehousesList.forEach { warehouseDao.insert(it) }

            val goodsList = listOf( //This is the initial data to populate the database and next show some initial data in the list page.
                Goods(
                    id = 1,
                    name = "Pencil",
                    description = "Black Pencil",
                    quantity = 250,
                    image = "default.png",
                    warehouseId = 1
                ),
                Goods(
                    id = 2,
                    name = "Pen",
                    description = "Black Pen",
                    quantity = 350,
                    image = "default.png",
                    warehouseId = 1
                ),
                Goods(
                    id = 3,
                    name = "Eraser",
                    description = "Paper eraser",
                    quantity = 120,
                    image = "default.png",
                    warehouseId = 1
                ),
                Goods(
                    id = 4,
                    name = "Box",
                    description = "Square box",
                    quantity = 800,
                    image = "default.png",
                    warehouseId = 2
                ),
                Goods(
                    id = 5,
                    name = "Table",
                    description = "Rainbow table",
                    quantity = 300,
                    image = "default.png",
                    warehouseId = 2
                )
            )
            goodsList.forEach { goodsDao.insert(it) }

            val userList = listOf( //This is the initial data to create users in the database because in the business context that we proposed, this app is a complement of a robust system that efficiently manages all corporation operations.

                User(
                    id = 1,
                    userName = "svera",
                    password = "1",
                    role = ROLE_ADMIN
                ),
                User(
                    id = 2,
                    userName = "erick",
                    password = "1",
                    role = ROLE_ADMIN
                ),
                User(
                    id = 3,
                    userName = "emplo1",
                    password = "1",
                    role = ROLE_USER
                ),
                User(
                    id = 4,
                    userName = "emplo2",
                    password = "1",
                    role = ROLE_USER
                )
            )
            userList.forEach { userDao.insert(it) }
        }
    }
}