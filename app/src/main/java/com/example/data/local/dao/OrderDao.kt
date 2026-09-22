package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.OrderEntity
import com.example.data.local.entities.OrderItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY createdAt DESC")
    fun getOrdersByUser(userId: String): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders ORDER BY createdAt DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrderById(orderId: String): Flow<OrderEntity?>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    suspend fun getOrderByIdDirect(orderId: String): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(items: List<OrderItemEntity>)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    fun getItemsForOrder(orderId: String): Flow<List<OrderItemEntity>>

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getItemsForOrderDirect(orderId: String): List<OrderItemEntity>

    @Query("SELECT * FROM order_items WHERE sellerId = :sellerId")
    fun getOrderItemsBySeller(sellerId: String): Flow<List<OrderItemEntity>>

    @Query("UPDATE orders SET orderStatus = :status, updatedAt = :updatedAt WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String, updatedAt: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM orders")
    fun getOrderCount(): Flow<Int>

    @Query("SELECT SUM(totalAmount) FROM orders WHERE orderStatus != 'CANCELLED'")
    fun getTotalRevenue(): Flow<Double?>
}
