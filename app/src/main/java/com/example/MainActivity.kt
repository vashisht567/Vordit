package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.entities.ProductEntity
import com.example.ui.viewmodel.PadharoViewModel
import com.example.ui.components.NavigationTab
import com.example.ui.components.PadharoBottomNav
import com.example.ui.components.PadharoTopBar
import com.example.ui.screens.admin.AdminPanelScreen
import com.example.ui.screens.cart.CartScreen
import com.example.ui.screens.checkout.CheckoutScreen
import com.example.ui.screens.explore.ExploreRajasthanScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.orders.OrdersScreen
import com.example.ui.screens.product.ProductDetailScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.seller.AddProductScreen
import com.example.ui.screens.seller.SellerDashboardScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                PadharoApp()
            }
        }
    }
}

@Composable
fun PadharoApp(viewModel: PadharoViewModel = viewModel()) {
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val currentSellerProfile by viewModel.currentSellerProfile.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedDistrict by viewModel.selectedDistrict.collectAsStateWithLifecycle()
    val products by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val featuredProducts by viewModel.featuredProducts.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItemsWithProducts.collectAsStateWithLifecycle()
    val wishlistIds by viewModel.wishlistProductIds.collectAsStateWithLifecycle()
    val userOrders by viewModel.userOrders.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrdersAdmin.collectAsStateWithLifecycle()
    val allSellers by viewModel.allSellers.collectAsStateWithLifecycle()
    val pendingSellers by viewModel.pendingSellers.collectAsStateWithLifecycle()
    val platformSettings by viewModel.platformSettings.collectAsStateWithLifecycle()
    val aiState by viewModel.aiState.collectAsStateWithLifecycle()
    val userFeedback by viewModel.userFeedback.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var currentTab by remember { mutableStateOf(NavigationTab.Home.route) }
    var selectedProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var isCartOpen by remember { mutableStateOf(false) }
    var isCheckoutOpen by remember { mutableStateOf(false) }

    // When role switches, sync current tab
    LaunchedEffect(currentUser?.role) {
        when (currentUser?.role) {
            "SELLER" -> currentTab = NavigationTab.SellerDashboard.route
            "ADMIN" -> currentTab = NavigationTab.AdminVerifications.route
            else -> if (currentTab !in listOf(NavigationTab.Home.route, NavigationTab.Explore.route, NavigationTab.Orders.route, NavigationTab.Profile.route)) {
                currentTab = NavigationTab.Home.route
            }
        }
    }

    LaunchedEffect(userFeedback) {
        userFeedback?.let {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearFeedback()
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (selectedProduct == null && !isCartOpen && !isCheckoutOpen) {
                PadharoTopBar(
                    currentUser = currentUser,
                    currentLanguage = currentLanguage,
                    cartItemCount = cartItems.sumOf { it.first.quantity },
                    onLanguageToggle = { viewModel.toggleLanguage() },
                    onRoleSelect = { role -> viewModel.switchRole(role) },
                    onCartClick = { isCartOpen = true }
                )
            }
        },
        bottomBar = {
            if (selectedProduct == null && !isCartOpen && !isCheckoutOpen) {
                PadharoBottomNav(
                    userRole = currentUser?.role ?: "CUSTOMER",
                    currentTab = currentTab,
                    currentLanguage = currentLanguage,
                    onTabSelected = { currentTab = it }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // Checkout Screen
                isCheckoutOpen -> {
                    CheckoutScreen(
                        cartItems = cartItems,
                        currentLanguage = currentLanguage,
                        onBackClick = { isCheckoutOpen = false },
                        onConfirmOrder = { name, phone, address, district, paymentMethod ->
                            viewModel.placeOrder(
                                customerName = name,
                                phone = phone,
                                address = address,
                                district = district,
                                paymentMethod = paymentMethod,
                                onSuccess = { orderId ->
                                    isCheckoutOpen = false
                                    isCartOpen = false
                                    currentTab = NavigationTab.Orders.route
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Order #$orderId placed successfully! / ऑर्डर सफल रहा!")
                                    }
                                },
                                onError = { err ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Error: $err")
                                    }
                                }
                            )
                        }
                    )
                }

                // Cart Screen
                isCartOpen -> {
                    CartScreen(
                        cartItems = cartItems,
                        currentLanguage = currentLanguage,
                        onBackClick = { isCartOpen = false },
                        onUpdateQuantity = { id, qty -> viewModel.updateCartQuantity(id, qty) },
                        onRemoveItem = { id -> viewModel.removeFromCart(id) },
                        onProceedToCheckout = {
                            isCheckoutOpen = true
                        },
                        onContinueShopping = {
                            isCartOpen = false
                            currentTab = NavigationTab.Home.route
                        }
                    )
                }

                // Product Detail Screen
                selectedProduct != null -> {
                    val prod = selectedProduct!!
                    val reviews by viewModel.getProductReviews(prod.id).collectAsStateWithLifecycle(emptyList())

                    ProductDetailScreen(
                        product = prod,
                        reviews = reviews,
                        currentLanguage = currentLanguage,
                        isInWishlist = wishlistIds.contains(prod.id),
                        onBackClick = { selectedProduct = null },
                        onWishlistToggle = { viewModel.toggleWishlist(prod.id) },
                        onAddToCart = {
                            viewModel.addToCart(prod.id)
                        },
                        onBuyNow = {
                            viewModel.addToCart(prod.id)
                            selectedProduct = null
                            isCartOpen = true
                        },
                        onAddReview = { rating, text ->
                            viewModel.addReview(prod.id, rating, text) {}
                        }
                    )
                }

                // Tab Based Screens
                else -> {
                    when (currentTab) {
                        NavigationTab.Home.route -> {
                            HomeScreen(
                                products = products,
                                featuredProducts = featuredProducts,
                                currentLanguage = currentLanguage,
                                searchQuery = searchQuery,
                                selectedCategory = selectedCategory,
                                selectedDistrict = selectedDistrict,
                                wishlistProductIds = wishlistIds,
                                onSearchChange = { viewModel.setSearchQuery(it) },
                                onCategorySelect = { viewModel.selectCategory(it) },
                                onDistrictSelect = { viewModel.selectDistrict(it) },
                                onProductClick = { selectedProduct = it },
                                onAddToCartClick = { viewModel.addToCart(it.id) },
                                onWishlistToggle = { viewModel.toggleWishlist(it.id) },
                                onExploreDistrictsClick = { currentTab = NavigationTab.Explore.route }
                            )
                        }

                        NavigationTab.Explore.route -> {
                            ExploreRajasthanScreen(
                                products = products,
                                currentLanguage = currentLanguage,
                                selectedDistrict = selectedDistrict,
                                wishlistProductIds = wishlistIds,
                                onSelectDistrict = { viewModel.selectDistrict(it) },
                                onProductClick = { selectedProduct = it },
                                onAddToCartClick = { viewModel.addToCart(it.id) },
                                onWishlistToggle = { viewModel.toggleWishlist(it.id) }
                            )
                        }

                        NavigationTab.Orders.route -> {
                            OrdersScreen(
                                orders = userOrders,
                                currentLanguage = currentLanguage,
                                onStartShopping = { currentTab = NavigationTab.Home.route }
                            )
                        }

                        NavigationTab.Profile.route -> {
                            ProfileScreen(
                                user = currentUser,
                                currentLanguage = currentLanguage,
                                onRoleSelect = { role -> viewModel.switchRole(role) },
                                onToggleLanguage = { viewModel.toggleLanguage() },
                                onRegisterSeller = { bName, aName, cat, dist, vill, addr, desc, story ->
                                    viewModel.registerSeller(bName, aName, cat, dist, vill, addr, desc, story) {
                                        currentTab = NavigationTab.SellerDashboard.route
                                    }
                                }
                            )
                        }

                        NavigationTab.SellerDashboard.route -> {
                            SellerDashboardScreen(
                                seller = currentSellerProfile,
                                products = products,
                                orders = allOrders,
                                currentLanguage = currentLanguage,
                                onAddProductClick = { currentTab = NavigationTab.SellerAddProduct.route },
                                onDeleteProduct = { id -> viewModel.deleteProduct(id) },
                                onUpdateOrderStatus = { orderId, status -> viewModel.updateOrderStatus(orderId, status) }
                            )
                        }

                        NavigationTab.SellerAddProduct.route -> {
                            AddProductScreen(
                                currentLanguage = currentLanguage,
                                sellerDistrict = currentSellerProfile?.district ?: currentUser?.district ?: "Banswara",
                                sellerId = currentSellerProfile?.id ?: "sel_1",
                                sellerName = currentSellerProfile?.artisanName ?: currentUser?.name ?: "Artisan",
                                aiState = aiState,
                                onBackClick = { currentTab = NavigationTab.SellerDashboard.route },
                                onGenerateAi = { prompt, district -> viewModel.generateAiListing(prompt, district) },
                                onResetAi = { viewModel.resetAiState() },
                                onPublishProduct = { prod ->
                                    viewModel.createProduct(prod) {
                                        currentTab = NavigationTab.SellerDashboard.route
                                    }
                                }
                            )
                        }

                        NavigationTab.SellerOrders.route -> {
                            SellerDashboardScreen(
                                seller = currentSellerProfile,
                                products = products,
                                orders = allOrders,
                                currentLanguage = currentLanguage,
                                onAddProductClick = { currentTab = NavigationTab.SellerAddProduct.route },
                                onDeleteProduct = { id -> viewModel.deleteProduct(id) },
                                onUpdateOrderStatus = { orderId, status -> viewModel.updateOrderStatus(orderId, status) }
                            )
                        }

                        NavigationTab.AdminVerifications.route,
                        NavigationTab.AdminOrders.route,
                        NavigationTab.AdminSettings.route -> {
                            AdminPanelScreen(
                                currentLanguage = currentLanguage,
                                allSellers = allSellers,
                                pendingSellers = pendingSellers,
                                allOrders = allOrders,
                                settings = platformSettings,
                                onApproveSeller = { viewModel.approveSeller(it) },
                                onRejectSeller = { viewModel.rejectSeller(it) },
                                onUpdateOrderStatus = { orderId, status -> viewModel.updateOrderStatus(orderId, status) },
                                onUpdateCommission = { viewModel.updateCommission(it) },
                                onClearDemoData = { viewModel.clearDemoData() }
                            )
                        }
                    }
                }
            }
        }
    }
}
