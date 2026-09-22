package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RoyalIndigo
import com.example.ui.theme.RoyalSaffron

@Composable
fun CategorySelectorRow(
    categories: List<String>,
    selectedCategory: String?,
    onSelectCategory: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onSelectCategory(null) },
                label = { Text("All Crafts / सभी") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RoyalSaffron,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("category_chip_all")
            )
        }
        items(categories) { category ->
            val isSelected = selectedCategory == category
            FilterChip(
                selected = isSelected,
                onClick = { onSelectCategory(if (isSelected) null else category) },
                label = { Text(category) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = RoyalSaffron,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("category_chip_$category")
            )
        }
    }
}

@Composable
fun DistrictSelectorRow(
    districts: List<String>,
    selectedDistrict: String?,
    onSelectDistrict: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            SuggestionChip(
                onClick = { onSelectDistrict(null) },
                label = { Text("All Rajasthan (सभी ज़िले)") },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = if (selectedDistrict == null) RoyalIndigo else MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = if (selectedDistrict == null) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("district_chip_all")
            )
        }
        items(districts) { district ->
            val isSelected = selectedDistrict == district
            SuggestionChip(
                onClick = { onSelectDistrict(if (isSelected) null else district) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = if (isSelected) Color.White else RoyalSaffron
                    )
                },
                label = { Text(district) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = if (isSelected) RoyalIndigo else MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("district_chip_$district")
            )
        }
    }
}
