package org.the_chance.honeymart.domain.repository

import org.the_chance.honeymart.domain.model.CartEntity
import org.the_chance.honeymart.domain.model.CategoryEntity
import org.the_chance.honeymart.domain.model.CouponEntity
import org.the_chance.honeymart.domain.model.GetRecentProductsEntity
import org.the_chance.honeymart.domain.model.MarketDetailsEntity
import org.the_chance.honeymart.domain.model.MarketEntity
import org.the_chance.honeymart.domain.model.OrderDetailsEntity
import org.the_chance.honeymart.domain.model.OrderEntity
import org.the_chance.honeymart.domain.model.ProductEntity
import org.the_chance.honeymart.domain.model.ValidCouponEntity
import org.the_chance.honeymart.domain.model.WishListEntity


interface HoneyMartRepository {

    suspend fun getAllMarkets(): List<MarketEntity>?

    suspend fun getMarketDetails(marketId: Long): MarketDetailsEntity
    suspend fun getCategoriesInMarket(marketId: Long): List<CategoryEntity>?
    suspend fun getAllProductsByCategory(categoryId: Long): List<ProductEntity>?
    suspend fun getCategoriesForSpecificProduct(productId: Long): List<CategoryEntity>?
    suspend fun addToWishList(productId: Long): String
    suspend fun deleteFromWishList(productId: Long): String
    suspend fun getWishList(): List<WishListEntity>
    suspend fun getCart(): CartEntity

    suspend fun addToCart(productId: Long,count:Int): String
    suspend fun deleteFromCart(productId: Long): String
    suspend fun getOrderDetails(orderId: Long): OrderDetailsEntity

    suspend fun getAllOrders(orderState:Int): List<OrderEntity>
    suspend fun updateOrderState(id: Long?, state: Int):Boolean
    suspend fun checkout(): String

    suspend fun getProductDetails(productId: Long): ProductEntity

    suspend fun deleteAllCart(): String


    suspend fun getUSerCoupons(): List<CouponEntity>

    suspend fun getValidUSerCoupons(): List<ValidCouponEntity>

    suspend fun getRecentProducts(): List<GetRecentProductsEntity>

    suspend fun getAllProducts(): List<ProductEntity>
}