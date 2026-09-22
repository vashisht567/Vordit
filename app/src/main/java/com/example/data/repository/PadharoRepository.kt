package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DatabaseSeeder
import com.example.data.local.entities.CartItemEntity
import com.example.data.local.entities.OrderEntity
import com.example.data.local.entities.OrderItemEntity
import com.example.data.local.entities.PlatformSettingsEntity
import com.example.data.local.entities.ProductEntity
import com.example.data.local.entities.ProductReviewEntity
import com.example.data.local.entities.SellerProfileEntity
import com.example.data.local.entities.UserEntity
import com.example.data.local.entities.WishlistItemEntity
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class PadharoRepository(private val database: AppDatabase) {

    private val userDao = database.userDao()
    private val sellerDao = database.sellerDao()
    private val productDao = database.productDao()
    private val cartDao = database.cartDao()
    private val wishlistDao = database.wishlistDao()
    private val orderDao = database.orderDao()
    private val reviewDao = database.reviewDao()
    private val settingsDao = database.settingsDao()

    suspend fun ensureSeeded() {
        val existingProducts = productDao.getProductByIdDirect("prod_1")
        if (existingProducts == null) {
            DatabaseSeeder.seedDatabase(database)
        }
    }

    // User Operations
    fun getUser(userId: String): Flow<UserEntity?> = userDao.getUserById(userId)
    suspend fun getUserDirect(userId: String): UserEntity? = userDao.getUserDirect(userId)
    fun getAllUsers(): Flow<List<UserEntity>> = userDao.getAllUsers()
    suspend fun saveUser(user: UserEntity) = userDao.insertUser(user)

    // Seller Operations
    fun getAllSellers(): Flow<List<SellerProfileEntity>> = sellerDao.getAllSellers()
    fun getApprovedSellers(): Flow<List<SellerProfileEntity>> = sellerDao.getApprovedSellers()
    fun getPendingSellers(): Flow<List<SellerProfileEntity>> = sellerDao.getPendingSellers()
    fun getSellerById(sellerId: String): Flow<SellerProfileEntity?> = sellerDao.getSellerById(sellerId)
    fun getSellerByUserId(userId: String): Flow<SellerProfileEntity?> = sellerDao.getSellerByUserId(userId)
    suspend fun getSellerByUserIdDirect(userId: String): SellerProfileEntity? = sellerDao.getSellerByUserIdDirect(userId)
    suspend fun registerSeller(seller: SellerProfileEntity) = sellerDao.insertSeller(seller)
    suspend fun updateSellerStatus(sellerId: String, status: String) = sellerDao.updateSellerStatus(sellerId, status)
    suspend fun updateSeller(seller: SellerProfileEntity) = sellerDao.updateSeller(seller)

    // Product Operations
    fun getApprovedProducts(): Flow<List<ProductEntity>> = productDao.getApprovedProducts()
    fun getFeaturedProducts(): Flow<List<ProductEntity>> = productDao.getFeaturedProducts()
    fun getProductById(productId: String): Flow<ProductEntity?> = productDao.getProductById(productId)
    suspend fun getProductByIdDirect(productId: String): ProductEntity? = productDao.getProductByIdDirect(productId)
    fun getProductsBySeller(sellerId: String): Flow<List<ProductEntity>> = productDao.getProductsBySeller(sellerId)
    fun getProductsByDistrict(district: String): Flow<List<ProductEntity>> = productDao.getProductsByDistrict(district)
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>> = productDao.getProductsByCategory(category)
    fun searchProducts(query: String): Flow<List<ProductEntity>> = productDao.searchProducts(query)
    fun getAllProductsAdmin(): Flow<List<ProductEntity>> = productDao.getAllProductsAdmin()
    suspend fun addProduct(product: ProductEntity) = productDao.insertProduct(product)
    suspend fun updateProduct(product: ProductEntity) = productDao.updateProduct(product)
    suspend fun deleteProduct(productId: String) = productDao.deleteProduct(productId)
    suspend fun clearDemoData() = productDao.deleteDemoProducts()

    // Cart Operations
    fun getCartItems(userId: String): Flow<List<CartItemEntity>> = cartDao.getCartItemsByUser(userId)
    suspend fun addToCart(userId: String, productId: String, quantity: Int = 1) {
        val existing = cartDao.getCartItem(userId, productId)
        if (existing != null) {
            cartDao.updateQuantity(existing.id, existing.quantity + quantity)
        } else {
            cartDao.insertCartItem(CartItemEntity(userId = userId, productId = productId, quantity = quantity))
        }
    }
    suspend fun updateCartQuantity(cartItemId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(cartItemId)
        } else {
            cartDao.updateQuantity(cartItemId, quantity)
        }
    }
    suspend fun removeFromCart(cartItemId: Long) = cartDao.deleteCartItem(cartItemId)
    suspend fun clearCart(userId: String) = cartDao.clearCartByUser(userId)

    // Wishlist Operations
    fun getWishlist(userId: String): Flow<List<WishlistItemEntity>> = wishlistDao.getWishlistByUser(userId)
    fun isInWishlist(userId: String, productId: String): Flow<Boolean> = wishlistDao.isInWishlist(userId, productId)
    suspend fun toggleWishlist(userId: String, productId: String, isCurrentlyInWishlist: Boolean) {
        if (isCurrentlyInWishlist) {
            wishlistDao.removeFromWishlist(userId, productId)
        } else {
            wishlistDao.insertWishlist(WishlistItemEntity(userId = userId, productId = productId))
        }
    }

    // Order Operations
    fun getOrdersByUser(userId: String): Flow<List<OrderEntity>> = orderDao.getOrdersByUser(userId)
    fun getAllOrders(): Flow<List<OrderEntity>> = orderDao.getAllOrders()
    fun getOrderById(orderId: String): Flow<OrderEntity?> = orderDao.getOrderById(orderId)
    suspend fun getOrderByIdDirect(orderId: String): OrderEntity? = orderDao.getOrderByIdDirect(orderId)
    fun getOrderItems(orderId: String): Flow<List<OrderItemEntity>> = orderDao.getItemsForOrder(orderId)
    suspend fun getOrderItemsDirect(orderId: String): List<OrderItemEntity> = orderDao.getItemsForOrderDirect(orderId)
    fun getSellerOrderItems(sellerId: String): Flow<List<OrderItemEntity>> = orderDao.getOrderItemsBySeller(sellerId)

    suspend fun createOrder(
        userId: String,
        customerName: String,
        customerPhone: String,
        deliveryAddress: String,
        district: String,
        subtotal: Double,
        deliveryFee: Double,
        totalAmount: Double,
        paymentMethod: String,
        cartItems: List<Pair<CartItemEntity, ProductEntity>>
    ): String {
        val orderId = "ORD-RJ-" + (1000..9999).random()
        val trackingNo = "PADHARO-TRK-" + (100000..999999).random()

        val order = OrderEntity(
            id = orderId,
            userId = userId,
            customerName = customerName,
            customerPhone = customerPhone,
            deliveryAddress = deliveryAddress,
            district = district,
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            totalAmount = totalAmount,
            paymentMethod = paymentMethod,
            paymentStatus = if (paymentMethod.contains("Cash")) "PENDING_COD" else "PAID",
            orderStatus = "PLACED",
            trackingNumber = trackingNo,
            notes = "Order successfully registered in Rajasthan Marketplace"
        )

        val orderItems = cartItems.map { (cartItem, product) ->
            OrderItemEntity(
                orderId = orderId,
                productId = product.id,
                sellerId = product.sellerId,
                productName = product.name,
                productImageUrl = product.imageUrl,
                price = product.price * (100 - product.discountPercent) / 100.0,
                quantity = cartItem.quantity
            )
        }

        orderDao.insertOrder(order)
        orderDao.insertOrderItems(orderItems)
        cartDao.clearCartByUser(userId)

        return orderId
    }

    suspend fun updateOrderStatus(orderId: String, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    // Reviews
    fun getProductReviews(productId: String): Flow<List<ProductReviewEntity>> = reviewDao.getReviewsForProduct(productId)
    suspend fun addReview(productId: String, userId: String, userName: String, district: String, rating: Int, text: String): Boolean {
        reviewDao.insertReview(
            ProductReviewEntity(
                productId = productId,
                userId = userId,
                userName = userName,
                userDistrict = district,
                rating = rating,
                reviewText = text,
                isVerifiedPurchase = true
            )
        )
        return true
    }

    // Settings
    fun getSettings(): Flow<PlatformSettingsEntity?> = settingsDao.getSettings()
    suspend fun updateCommission(percentage: Double) {
        val current = settingsDao.getSettingsDirect() ?: PlatformSettingsEntity()
        settingsDao.updateSettings(current.copy(commissionPercentage = percentage))
    }
}
