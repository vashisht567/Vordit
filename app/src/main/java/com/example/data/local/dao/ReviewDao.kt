package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.ProductReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {
    @Query("SELECT * FROM product_reviews WHERE productId = :productId ORDER BY createdAt DESC")
    fun getReviewsForProduct(productId: String): Flow<List<ProductReviewEntity>>

    @Query("SELECT COUNT(*) FROM product_reviews WHERE productId = :productId AND userId = :userId")
    suspend fun hasUserReviewedProduct(productId: String, userId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReview(review: ProductReviewEntity)

    @Query("DELETE FROM product_reviews WHERE id = :reviewId")
    suspend fun deleteReview(reviewId: Long)
}
