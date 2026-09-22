package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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
import com.example.data.remote.AiGeneratedListing
import com.example.data.remote.GeminiService
import com.example.data.repository.PadharoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class AiGenerationState {
    object Idle : AiGenerationState()
    object Loading : AiGenerationState()
    data class Success(val listing: AiGeneratedListing) : AiGenerationState()
    data class Error(val message: String) : AiGenerationState()
}

class PadharoViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = PadharoRepository(database)
    private val geminiService = GeminiService()

    private val _currentUserId = MutableStateFlow("usr_customer_1")
    val currentUserId: StateFlow<String> = _currentUserId.asStateFlow()

    private val _currentLanguage = MutableStateFlow("HI") // "HI" for Hindi, "EN" for English
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _selectedDistrict = MutableStateFlow<String?>(null)
    val selectedDistrict: StateFlow<String?> = _selectedDistrict.asStateFlow()

    private val _aiState = MutableStateFlow<AiGenerationState>(AiGenerationState.Idle)
    val aiState: StateFlow<AiGenerationState> = _aiState.asStateFlow()

    private val _userFeedback = MutableStateFlow<String?>(null)
    val userFeedback: StateFlow<String?> = _userFeedback.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    val currentUser: StateFlow<UserEntity?> = _currentUserId.flatMapLatest { id ->
        repository.getUser(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentSellerProfile: StateFlow<SellerProfileEntity?> = _currentUserId.flatMapLatest { userId ->
        repository.getSellerByUserId(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allApprovedProducts: StateFlow<List<ProductEntity>> = repository.getApprovedProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val featuredProducts: StateFlow<List<ProductEntity>> = repository.getFeaturedProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allApprovedProducts,
        _searchQuery,
        _selectedCategory,
        _selectedDistrict
    ) { products, query, category, district ->
        var list = products

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.name.lowercase().contains(q) ||
                it.hindiName.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.district.lowercase().contains(q) ||
                it.makerName.lowercase().contains(q) ||
                it.tags.lowercase().contains(q)
            }
        }

        if (category != null && category != "All" && category != "सभी") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }

        if (district != null && district != "All Rajasthan" && district != "पूरा राजस्थान") {
            list = list.filter { it.district.equals(district, ignoreCase = true) }
        }

        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItemsWithProducts: StateFlow<List<Pair<CartItemEntity, ProductEntity>>> = combine(
        _currentUserId.flatMapLatest { repository.getCartItems(it) },
        allApprovedProducts
    ) { cartEntities, products ->
        val productMap = products.associateBy { it.id }
        cartEntities.mapNotNull { cart ->
            productMap[cart.productId]?.let { prod -> cart to prod }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistProductIds: StateFlow<Set<String>> = _currentUserId.flatMapLatest { userId ->
        repository.getWishlist(userId)
    }.combine(flowOf(Unit)) { items, _ ->
        items.map { it.productId }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    val userOrders: StateFlow<List<OrderEntity>> = _currentUserId.flatMapLatest { userId ->
        repository.getOrdersByUser(userId)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrdersAdmin: StateFlow<List<OrderEntity>> = repository.getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allSellers: StateFlow<List<SellerProfileEntity>> = repository.getAllSellers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pendingSellers: StateFlow<List<SellerProfileEntity>> = repository.getPendingSellers()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val platformSettings: StateFlow<PlatformSettingsEntity?> = repository.getSettings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun switchRole(role: String) {
        viewModelScope.launch {
            when (role) {
                "CUSTOMER" -> _currentUserId.value = "usr_customer_1"
                "SELLER" -> _currentUserId.value = "usr_seller_1"
                "ADMIN" -> _currentUserId.value = "usr_admin_1"
            }
            _userFeedback.value = if (_currentLanguage.value == "HI") "भूमिका बदली: $role" else "Switched role to: $role"
        }
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == "HI") "EN" else "HI"
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun selectDistrict(district: String?) {
        _selectedDistrict.value = district
    }

    fun addToCart(productId: String, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(_currentUserId.value, productId, quantity)
            _userFeedback.value = if (_currentLanguage.value == "HI") "कार्ट में जोड़ा गया" else "Added to cart"
        }
    }

    fun updateCartQuantity(cartItemId: Long, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(cartItemId, quantity)
        }
    }

    fun removeFromCart(cartItemId: Long) {
        viewModelScope.launch {
            repository.removeFromCart(cartItemId)
        }
    }

    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            val isWish = wishlistProductIds.value.contains(productId)
            repository.toggleWishlist(_currentUserId.value, productId, isWish)
            _userFeedback.value = if (isWish) {
                if (_currentLanguage.value == "HI") "विशलिस्ट से हटाया" else "Removed from wishlist"
            } else {
                if (_currentLanguage.value == "HI") "विशलिस्ट में सहेजा गया" else "Saved to wishlist"
            }
        }
    }

    fun placeOrder(
        customerName: String,
        phone: String,
        address: String,
        district: String,
        paymentMethod: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val currentCart = cartItemsWithProducts.value
        if (currentCart.isEmpty()) {
            onError("Cart is empty")
            return
        }

        viewModelScope.launch {
            try {
                val subtotal = currentCart.sumOf { (cart, prod) ->
                    val actualPrice = prod.price * (100 - prod.discountPercent) / 100.0
                    actualPrice * cart.quantity
                }
                val deliveryFee = if (subtotal > 499) 0.0 else 49.0
                val total = subtotal + deliveryFee

                val orderId = repository.createOrder(
                    userId = _currentUserId.value,
                    customerName = customerName,
                    customerPhone = phone,
                    deliveryAddress = address,
                    district = district,
                    subtotal = subtotal,
                    deliveryFee = deliveryFee,
                    totalAmount = total,
                    paymentMethod = paymentMethod,
                    cartItems = currentCart
                )
                onSuccess(orderId)
            } catch (e: Exception) {
                onError(e.message ?: "Failed to place order")
            }
        }
    }

    fun updateOrderStatus(orderId: String, status: String) {
        viewModelScope.launch {
            repository.updateOrderStatus(orderId, status)
            _userFeedback.value = if (_currentLanguage.value == "HI") "ऑर्डर स्थिति अपडेट की गई: $status" else "Order status updated: $status"
        }
    }

    fun generateAiListing(rawDescription: String, district: String) {
        viewModelScope.launch {
            _aiState.value = AiGenerationState.Loading
            try {
                val listing = geminiService.generateListing(rawDescription, district)
                _aiState.value = AiGenerationState.Success(listing)
            } catch (e: Exception) {
                _aiState.value = AiGenerationState.Error(e.message ?: "Error generating AI listing")
            }
        }
    }

    fun resetAiState() {
        _aiState.value = AiGenerationState.Idle
    }

    fun createProduct(product: ProductEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.addProduct(product)
            _userFeedback.value = if (_currentLanguage.value == "HI") "नया उत्पाद प्रकाशित किया गया" else "Product published successfully"
            onComplete()
        }
    }

    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            repository.deleteProduct(productId)
            _userFeedback.value = if (_currentLanguage.value == "HI") "उत्पाद हटाया गया" else "Product deleted"
        }
    }

    fun approveSeller(sellerId: String) {
        viewModelScope.launch {
            repository.updateSellerStatus(sellerId, "APPROVED")
            _userFeedback.value = if (_currentLanguage.value == "HI") "विक्रेता स्वीकृत किया गया" else "Seller approved successfully"
        }
    }

    fun rejectSeller(sellerId: String) {
        viewModelScope.launch {
            repository.updateSellerStatus(sellerId, "REJECTED")
            _userFeedback.value = if (_currentLanguage.value == "HI") "विक्रेता अस्वीकृत किया गया" else "Seller rejected"
        }
    }

    fun registerSeller(
        businessName: String,
        artisanName: String,
        category: String,
        district: String,
        villageOrCity: String,
        fullAddress: String,
        businessDescription: String,
        artisanStory: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val sellerId = "sel_" + (100..999).random()
            val newSeller = SellerProfileEntity(
                id = sellerId,
                userId = _currentUserId.value,
                businessName = businessName,
                artisanName = artisanName,
                category = category,
                district = district,
                villageOrCity = villageOrCity,
                fullAddress = fullAddress,
                businessDescription = businessDescription,
                artisanStory = artisanStory,
                status = "PENDING"
            )
            repository.registerSeller(newSeller)
            // also update user role to SELLER
            val user = repository.getUserDirect(_currentUserId.value)
            if (user != null) {
                repository.saveUser(user.copy(role = "SELLER"))
            }
            onSuccess()
        }
    }

    fun addReview(productId: String, rating: Int, text: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = repository.getUserDirect(_currentUserId.value)
            val name = user?.name ?: "Customer"
            val dist = user?.district ?: "Jaipur"
            repository.addReview(productId, _currentUserId.value, name, dist, rating, text)
            _userFeedback.value = if (_currentLanguage.value == "HI") "आपकी समीक्षा जोड़ी गई" else "Review posted successfully"
            onSuccess()
        }
    }

    fun updateSellerMakerProfile(
        artisanName: String,
        businessName: String,
        district: String,
        villageOrCity: String,
        fullAddress: String,
        craftOrigin: String,
        artisanStory: String,
        videoUrl: String,
        videoTitle: String,
        videoDurationSeconds: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val current = currentSellerProfile.value ?: repository.getSellerByUserIdDirect(_currentUserId.value)
            if (current != null) {
                val updated = current.copy(
                    artisanName = artisanName,
                    businessName = businessName,
                    district = district,
                    villageOrCity = villageOrCity,
                    fullAddress = fullAddress,
                    craftOrigin = craftOrigin,
                    artisanStory = artisanStory,
                    videoUrl = videoUrl,
                    videoTitle = videoTitle,
                    videoDurationSeconds = videoDurationSeconds
                )
                repository.updateSeller(updated)
                _userFeedback.value = if (_currentLanguage.value == "HI") "कारीगर कहानी व वीडियो सफलतापूर्वक अपडेट हुआ" else "Meet the Maker story & video updated successfully"
                onSuccess()
            }
        }
    }

    fun clearDemoData() {
        viewModelScope.launch {
            repository.clearDemoData()
            _userFeedback.value = if (_currentLanguage.value == "HI") "डेमो उत्पाद हटाए गए" else "Demo products cleared"
        }
    }

    fun updateCommission(percentage: Double) {
        viewModelScope.launch {
            repository.updateCommission(percentage)
            _userFeedback.value = if (_currentLanguage.value == "HI") "कमीशन दर अपडेट: $percentage%" else "Commission updated: $percentage%"
        }
    }

    fun clearFeedback() {
        _userFeedback.value = null
    }

    fun getProductReviews(productId: String) = repository.getProductReviews(productId)
    fun getOrderItems(orderId: String) = repository.getOrderItems(orderId)
}
