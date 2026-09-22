package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
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
import com.example.ui.theme.OasisGreen
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: UserEntity?,
    currentLanguage: String,
    onRoleSelect: (String) -> Unit,
    onToggleLanguage: () -> Unit,
    onRegisterSeller: (business: String, artisan: String, cat: String, dist: String, village: String, addr: String, desc: String, story: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showRegisterSellerDialog by remember { mutableStateOf(false) }

    var businessName by remember { mutableStateOf("") }
    var artisanName by remember { mutableStateOf(user?.name ?: "") }
    var category by remember { mutableStateOf("Handicrafts & Decor") }
    var district by remember { mutableStateOf(user?.district ?: "Jaipur") }
    var village by remember { mutableStateOf("") }
    var fullAddress by remember { mutableStateOf("") }
    var businessDescription by remember { mutableStateOf("") }
    var artisanStory by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen_scrollable"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Identity Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(RoyalSaffron.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (user?.name?.take(1) ?: "U").uppercase(),
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                            color = RoyalSaffron
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user?.name ?: "User",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = user?.phone ?: "+91 98290 11223",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = RoyalSaffron, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${user?.district ?: "Jaipur"}, Rajasthan",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                when (user?.role) {
                                    "SELLER" -> RoyalSaffron.copy(alpha = 0.15f)
                                    "ADMIN" -> RoyalIndigo.copy(alpha = 0.15f)
                                    else -> OasisGreen.copy(alpha = 0.15f)
                                }
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = user?.role ?: "CUSTOMER",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = when (user?.role) {
                                "SELLER" -> RoyalSaffron
                                "ADMIN" -> RoyalIndigo
                                else -> OasisGreen
                            }
                        )
                    }
                }
            }
        }

        // Role Selector Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = if (currentLanguage == "HI") "भूमिका स्विच करें (Switch Role)" else "Switch Experience Role",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onRoleSelect("CUSTOMER") },
                            modifier = Modifier.weight(1f),
                            colors = if (user?.role == "CUSTOMER") ButtonDefaults.outlinedButtonColors(containerColor = OasisGreen.copy(alpha = 0.15f)) else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Customer")
                        }

                        OutlinedButton(
                            onClick = { onRoleSelect("SELLER") },
                            modifier = Modifier.weight(1f),
                            colors = if (user?.role == "SELLER") ButtonDefaults.outlinedButtonColors(containerColor = RoyalSaffron.copy(alpha = 0.15f)) else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Icon(Icons.Default.Storefront, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Seller")
                        }

                        OutlinedButton(
                            onClick = { onRoleSelect("ADMIN") },
                            modifier = Modifier.weight(1f),
                            colors = if (user?.role == "ADMIN") ButtonDefaults.outlinedButtonColors(containerColor = RoyalIndigo.copy(alpha = 0.15f)) else ButtonDefaults.outlinedButtonColors()
                        ) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Admin")
                        }
                    }
                }
            }
        }

        // Language & Preferences
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Language, contentDescription = null, tint = RoyalIndigo)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (currentLanguage == "HI") "भाषा (Language)" else "Language",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        Button(
                            onClick = onToggleLanguage,
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalIndigo)
                        ) {
                            Text(if (currentLanguage == "HI") "English में बदलें" else "हिंदी में बदलें")
                        }
                    }
                }
            }
        }

        // Become a Seller Onboarding Banner (if Customer)
        if (user?.role == "CUSTOMER") {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = RoyalSaffron.copy(alpha = 0.12f))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = if (currentLanguage == "HI") "क्या आप राजस्थान के कारीगर हैं? विक्रेता बनें!" else "Are you a Rajasthan Artisan? Start Selling!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = RoyalSaffron
                        )
                        Text(
                            text = if (currentLanguage == "HI") "पधारो पर सीधे अपनी कला और हस्तशिल्प को भारत और दुनिया भर के खरीदारों तक पहुँचाएँ।" else "Onboard your craft directly onto Padharo to reach global buyers with zero middlemen fees.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Button(
                            onClick = { showRegisterSellerDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron),
                            modifier = Modifier.testTag("register_seller_cta_btn")
                        ) {
                            Text(if (currentLanguage == "HI") "कारीगर पंजीकरण शुरू करें" else "Apply as Artisan Seller")
                        }
                    }
                }
            }
        }

        // Padharo Mission & Support
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = OasisGreen)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (currentLanguage == "HI") "पधारो सहायता एवं मिशन" else "Padharo Mission & Support",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Text(
                        text = if (currentLanguage == "HI") "पधारो का उद्देश्य राजस्थान के सुदूर ग्रामीण क्षेत्रों के शिल्पकारों और स्वयं सहायता समूहों को तकनीक के माध्यम से सीधे बाज़ार तक पहुँचाना है।" else "Padharo connects grassroots Rajasthani artisans directly to global buyers, enabling fair income and preserving cultural heritage.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    HorizontalDivider()

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Call, contentDescription = null, tint = RoyalSaffron, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Helpline: +91 98290 12345", style = MaterialTheme.typography.bodySmall)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = RoyalIndigo, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Email: support@padharo.rajasthan.in", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    // Register Seller Dialog
    if (showRegisterSellerDialog) {
        AlertDialog(
            onDismissRequest = { showRegisterSellerDialog = false },
            title = { Text(if (currentLanguage == "HI") "कारीगर / विक्रेता पंजीकरण" else "Artisan Onboarding") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = businessName,
                        onValueChange = { businessName = it },
                        label = { Text("Business / SHG Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = artisanName,
                        onValueChange = { artisanName = it },
                        label = { Text("Lead Artisan Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        label = { Text("District (ज़िला)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = village,
                        onValueChange = { village = it },
                        label = { Text("Village / Cluster (गाँव/स्थान)") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = businessDescription,
                        onValueChange = { businessDescription = it },
                        label = { Text("Craft Specialization / शिल्प") },
                        maxLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (businessName.isNotBlank() && artisanName.isNotBlank()) {
                            onRegisterSeller(
                                businessName,
                                artisanName,
                                category,
                                district,
                                village,
                                fullAddress,
                                businessDescription,
                                artisanStory
                            )
                            showRegisterSellerDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoyalSaffron)
                ) {
                    Text(if (currentLanguage == "HI") "आवेदन भेजें" else "Submit Application")
                }
            },
            dismissButton = {
                TextButton(onClick = { showRegisterSellerDialog = false }) {
                    Text(if (currentLanguage == "HI") "रद्द करें" else "Cancel")
                }
            }
        )
    }
}
