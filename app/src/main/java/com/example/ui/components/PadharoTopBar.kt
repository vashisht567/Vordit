package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entities.UserEntity
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PadharoTopBar(
    currentUser: UserEntity?,
    currentLanguage: String,
    cartItemCount: Int,
    onLanguageToggle: () -> Unit,
    onRoleSelect: (String) -> Unit,
    onCartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showRoleMenu by remember { mutableStateOf(false) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand Logo & Tagline
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "PADHARO",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        ),
                        color = RoyalSaffron
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(RoyalIndigo.copy(alpha = 0.1f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (currentLanguage == "HI") "राजस्थान" else "RAJASTHAN",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = RoyalIndigo
                        )
                    }
                }
                Text(
                    text = if (currentLanguage == "HI") "राजस्थान का सामान, दुनिया के नाम" else "Local Business → Global Market",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            // Action Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Language Switcher
                OutlinedButton(
                    onClick = onLanguageToggle,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                    modifier = Modifier
                        .height(36.dp)
                        .testTag("language_toggle_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = "Language",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (currentLanguage == "HI") "हिन्दी" else "EN",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                // Cart Icon with Badge
                IconButton(
                    onClick = onCartClick,
                    modifier = Modifier.testTag("top_bar_cart_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(
                                    containerColor = RoyalSaffron,
                                    contentColor = Color.White
                                ) {
                                    Text(text = cartItemCount.toString())
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Shopping Cart",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Role Switcher / Profile Dropdown
                Box {
                    IconButton(
                        onClick = { showRoleMenu = true },
                        modifier = Modifier.testTag("role_switcher_button")
                    ) {
                        val icon = when (currentUser?.role) {
                            "SELLER" -> Icons.Default.Storefront
                            "ADMIN" -> Icons.Default.AdminPanelSettings
                            else -> Icons.Default.Person
                        }
                        val tint = when (currentUser?.role) {
                            "SELLER" -> RoyalSaffron
                            "ADMIN" -> RoyalIndigo
                            else -> MaterialTheme.colorScheme.primary
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = "Switch Role",
                            tint = tint
                        )
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text("Customer View (ग्राहक)", fontWeight = FontWeight.Bold)
                                    Text("Explore, Buy, Track Orders", style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            onClick = {
                                onRoleSelect("CUSTOMER")
                                showRoleMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = RoyalSaffron)
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text("Seller Portal (कारीगर / विक्रेता)", fontWeight = FontWeight.Bold)
                                    Text("Shanti Devi (Banswara SHG)", style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            onClick = {
                                onRoleSelect("SELLER")
                                showRoleMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Storefront, contentDescription = null, tint = RoyalSaffron)
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text("Admin Console (प्रशासक)", fontWeight = FontWeight.Bold)
                                    Text("Approvals, Orders & Platform GMV", style = MaterialTheme.typography.bodySmall)
                                }
                            },
                            onClick = {
                                onRoleSelect("ADMIN")
                                showRoleMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = RoyalIndigo)
                            }
                        )
                    }
                }
            }
        }
    }
}
