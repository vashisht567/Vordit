package com.example.ui.components

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBusiness
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron

sealed class NavigationTab(val route: String, val labelHi: String, val labelEn: String) {
    // Customer Tabs
    object Home : NavigationTab("home", "होम", "Home")
    object Explore : NavigationTab("explore", "राजस्थान खोजें", "Explore")
    object Orders : NavigationTab("orders", "मेरे ऑर्डर", "Orders")
    object Profile : NavigationTab("profile", "प्रोफ़ाइल", "Profile")

    // Seller Tabs
    object SellerDashboard : NavigationTab("seller_dash", "डैशबोर्ड", "Dashboard")
    object SellerAddProduct : NavigationTab("seller_add", "नया उत्पाद (AI)", "Add Product")
    object SellerOrders : NavigationTab("seller_orders", "ऑर्डर सूची", "Orders")

    // Admin Tabs
    object AdminVerifications : NavigationTab("admin_verify", "सत्यापन", "Verify")
    object AdminOrders : NavigationTab("admin_orders", "ऑर्डर्स GMV", "Orders")
    object AdminSettings : NavigationTab("admin_settings", "सेटिंग्स", "Settings")
}

@Composable
fun PadharoBottomNav(
    userRole: String,
    currentTab: String,
    currentLanguage: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .navigationBarsPadding()
            .testTag("bottom_nav_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        when (userRole) {
            "SELLER" -> {
                NavigationBarItem(
                    selected = currentTab == NavigationTab.SellerDashboard.route,
                    onClick = { onTabSelected(NavigationTab.SellerDashboard.route) },
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = "Inventory") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.SellerDashboard.labelHi else NavigationTab.SellerDashboard.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalSaffron,
                        indicatorColor = RoyalSaffron.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_seller_dash")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.SellerAddProduct.route,
                    onClick = { onTabSelected(NavigationTab.SellerAddProduct.route) },
                    icon = { Icon(Icons.Default.AddBusiness, contentDescription = "Add Product") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.SellerAddProduct.labelHi else NavigationTab.SellerAddProduct.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalSaffron,
                        indicatorColor = RoyalSaffron.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_seller_add")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.SellerOrders.route,
                    onClick = { onTabSelected(NavigationTab.SellerOrders.route) },
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = "Orders") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.SellerOrders.labelHi else NavigationTab.SellerOrders.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalSaffron,
                        indicatorColor = RoyalSaffron.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_seller_orders")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.Home.route,
                    onClick = { onTabSelected(NavigationTab.Home.route) },
                    icon = { Icon(Icons.Default.Storefront, contentDescription = "Store") },
                    label = { Text(if (currentLanguage == "HI") "मार्केट देखें" else "Browse") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalSaffron,
                        indicatorColor = RoyalSaffron.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_switch_to_store")
                )
            }
            "ADMIN" -> {
                NavigationBarItem(
                    selected = currentTab == NavigationTab.AdminVerifications.route,
                    onClick = { onTabSelected(NavigationTab.AdminVerifications.route) },
                    icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Verify") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.AdminVerifications.labelHi else NavigationTab.AdminVerifications.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalIndigo,
                        indicatorColor = RoyalIndigo.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_admin_verify")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.AdminOrders.route,
                    onClick = { onTabSelected(NavigationTab.AdminOrders.route) },
                    icon = { Icon(Icons.Default.ListAlt, contentDescription = "Orders") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.AdminOrders.labelHi else NavigationTab.AdminOrders.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalIndigo,
                        indicatorColor = RoyalIndigo.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_admin_orders")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.AdminSettings.route,
                    onClick = { onTabSelected(NavigationTab.AdminSettings.route) },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.AdminSettings.labelHi else NavigationTab.AdminSettings.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalIndigo,
                        indicatorColor = RoyalIndigo.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_admin_settings")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.Home.route,
                    onClick = { onTabSelected(NavigationTab.Home.route) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(if (currentLanguage == "HI") "मार्केट" else "Market") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalIndigo,
                        indicatorColor = RoyalIndigo.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_switch_to_market")
                )
            }
            else -> {
                // CUSTOMER
                NavigationBarItem(
                    selected = currentTab == NavigationTab.Home.route,
                    onClick = { onTabSelected(NavigationTab.Home.route) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.Home.labelHi else NavigationTab.Home.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalSaffron,
                        indicatorColor = RoyalSaffron.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_home")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.Explore.route,
                    onClick = { onTabSelected(NavigationTab.Explore.route) },
                    icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.Explore.labelHi else NavigationTab.Explore.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalSaffron,
                        indicatorColor = RoyalSaffron.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_explore")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.Orders.route,
                    onClick = { onTabSelected(NavigationTab.Orders.route) },
                    icon = { Icon(Icons.Default.LocalMall, contentDescription = "Orders") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.Orders.labelHi else NavigationTab.Orders.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalSaffron,
                        indicatorColor = RoyalSaffron.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_orders")
                )
                NavigationBarItem(
                    selected = currentTab == NavigationTab.Profile.route,
                    onClick = { onTabSelected(NavigationTab.Profile.route) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text(if (currentLanguage == "HI") NavigationTab.Profile.labelHi else NavigationTab.Profile.labelEn) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = RoyalSaffron,
                        indicatorColor = RoyalSaffron.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_profile")
                )
            }
        }
    }
}
