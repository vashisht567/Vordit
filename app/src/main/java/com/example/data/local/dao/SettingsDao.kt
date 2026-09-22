package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entities.PlatformSettingsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM platform_settings WHERE id = 'global'")
    fun getSettings(): Flow<PlatformSettingsEntity?>

    @Query("SELECT * FROM platform_settings WHERE id = 'global'")
    suspend fun getSettingsDirect(): PlatformSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSettings(settings: PlatformSettingsEntity)

    @Update
    suspend fun updateSettings(settings: PlatformSettingsEntity)
}
