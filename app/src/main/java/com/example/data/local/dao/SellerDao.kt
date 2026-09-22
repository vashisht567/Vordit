package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.SellerProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SellerDao {
    @Query("SELECT * FROM seller_profiles WHERE id = :sellerId")
    fun getSellerById(sellerId: String): Flow<SellerProfileEntity?>

    @Query("SELECT * FROM seller_profiles WHERE userId = :userId")
    fun getSellerByUserId(userId: String): Flow<SellerProfileEntity?>

    @Query("SELECT * FROM seller_profiles WHERE userId = :userId")
    suspend fun getSellerByUserIdDirect(userId: String): SellerProfileEntity?

    @Query("SELECT * FROM seller_profiles ORDER BY createdAt DESC")
    fun getAllSellers(): Flow<List<SellerProfileEntity>>

    @Query("SELECT * FROM seller_profiles WHERE status = 'APPROVED' ORDER BY rating DESC")
    fun getApprovedSellers(): Flow<List<SellerProfileEntity>>

    @Query("SELECT * FROM seller_profiles WHERE status = 'PENDING' ORDER BY createdAt DESC")
    fun getPendingSellers(): Flow<List<SellerProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeller(seller: SellerProfileEntity)

    @Update
    suspend fun updateSeller(seller: SellerProfileEntity)

    @Query("UPDATE seller_profiles SET status = :status WHERE id = :sellerId")
    suspend fun updateSellerStatus(sellerId: String, status: String)

    @Query("DELETE FROM seller_profiles WHERE id = :sellerId")
    suspend fun deleteSeller(sellerId: String)
}
