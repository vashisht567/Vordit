package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products WHERE isApproved = 1 ORDER BY createdAt DESC")
    fun getApprovedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isApproved = 1 AND isFeatured = 1")
    fun getFeaturedProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId")
    fun getProductById(productId: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductByIdDirect(productId: String): ProductEntity?

    @Query("SELECT * FROM products WHERE sellerId = :sellerId ORDER BY createdAt DESC")
    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE district = :district AND isApproved = 1")
    fun getProductsByDistrict(district: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category = :category AND isApproved = 1")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("""
        SELECT * FROM products 
        WHERE isApproved = 1 AND (
            name LIKE '%' || :query || '%' OR 
            hindiName LIKE '%' || :query || '%' OR 
            category LIKE '%' || :query || '%' OR 
            description LIKE '%' || :query || '%' OR 
            district LIKE '%' || :query || '%' OR 
            makerName LIKE '%' || :query || '%' OR 
            tags LIKE '%' || :query || '%'
        )
        ORDER BY createdAt DESC
    """)
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    fun getAllProductsAdmin(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun deleteProduct(productId: String)

    @Query("DELETE FROM products WHERE isDemo = 1")
    suspend fun deleteDemoProducts()
}
