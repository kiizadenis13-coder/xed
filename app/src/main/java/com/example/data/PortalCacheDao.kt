package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PortalCacheDao {

    // ==========================================
    // PORTAL METADATA CACHE
    // ==========================================
    @Query("SELECT * FROM portal_cache")
    fun getAllPortals(): Flow<List<PortalCacheEntity>>

    @Query("SELECT * FROM portal_cache WHERE portalKey = :key LIMIT 1")
    suspend fun getPortalByKey(key: String): PortalCacheEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortals(portals: List<PortalCacheEntity>)

    @Query("SELECT COUNT(*) FROM portal_cache")
    suspend fun getPortalCacheCount(): Int

    // ==========================================
    // MARKET PRODUCTS CACHE
    // ==========================================
    @Query("SELECT * FROM market_products ORDER BY price DESC")
    fun getAllMarketProducts(): Flow<List<MarketProductEntity>>

    @Query("SELECT * FROM market_products WHERE category = :category ORDER BY price DESC")
    fun getProductsByCategory(category: String): Flow<List<MarketProductEntity>>

    @Query("SELECT * FROM market_products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: String): MarketProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarketProducts(products: List<MarketProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: MarketProductEntity)

    @Update
    suspend fun updateProduct(product: MarketProductEntity)

    @Query("UPDATE market_products SET stockLeft = :newStock WHERE id = :productId")
    suspend fun updateProductStock(productId: String, newStock: Int)

    @Query("SELECT COUNT(*) FROM market_products")
    suspend fun getMarketProductCount(): Int

    // ==========================================
    // PORTAL CAROUSEL MEDIA ITEMS
    // ==========================================
    @Query("SELECT * FROM portal_carousel_media ORDER BY portalType ASC, orderIndex ASC")
    fun getAllCarouselMedia(): Flow<List<PortalCarouselMediaEntity>>

    @Query("SELECT * FROM portal_carousel_media WHERE portalType = :portalType ORDER BY orderIndex ASC")
    fun getCarouselMediaForPortal(portalType: String): Flow<List<PortalCarouselMediaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarouselMedia(mediaItems: List<PortalCarouselMediaEntity>)

    @Query("SELECT COUNT(*) FROM portal_carousel_media")
    suspend fun getCarouselMediaCount(): Int
}
