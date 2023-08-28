package org.the_chance.honeymart.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import org.the_chance.honeymart.data.repository.pagingSource.ProductsPagingSource
import org.the_chance.honeymart.data.repository.pagingSource.SearchProductsPagingSource
import org.the_chance.honeymart.data.source.remote.mapper.RecentProductEntity
import org.the_chance.honeymart.data.source.remote.mapper.toCartEntity
import org.the_chance.honeymart.data.source.remote.mapper.toCategoryEntity
import org.the_chance.honeymart.data.source.remote.mapper.toCouponEntity
import org.the_chance.honeymart.data.source.remote.mapper.toMarketDetailsEntity
import org.the_chance.honeymart.data.source.remote.mapper.toMarketEntity
import org.the_chance.honeymart.data.source.remote.mapper.toMarketOrderEntity
import org.the_chance.honeymart.data.source.remote.mapper.toNotificationEntity
import org.the_chance.honeymart.data.source.remote.mapper.toOrderDetailsEntity
import org.the_chance.honeymart.data.source.remote.mapper.toUserEntity
import org.the_chance.honeymart.data.source.remote.mapper.toProductEntity
import org.the_chance.honeymart.data.source.remote.mapper.toRequestEntity
import org.the_chance.honeymart.data.source.remote.mapper.toProfileUserEntity
import org.the_chance.honeymart.data.source.remote.mapper.toWishListEntity
import org.the_chance.honeymart.data.source.remote.network.HoneyMartService
import org.the_chance.honeymart.domain.model.CartEntity
import org.the_chance.honeymart.domain.model.CategoryEntity
import org.the_chance.honeymart.domain.model.CouponEntity
import org.the_chance.honeymart.domain.model.MarketDetailsEntity
import org.the_chance.honeymart.domain.model.MarketEntity
import org.the_chance.honeymart.domain.model.MarketOrderEntity
import org.the_chance.honeymart.domain.model.NotificationEntity
import org.the_chance.honeymart.domain.model.OrderDetailsEntity
import org.the_chance.honeymart.domain.model.OrderEntity
import org.the_chance.honeymart.domain.model.ProductEntity
import org.the_chance.honeymart.domain.model.RequestEntity
import org.the_chance.honeymart.domain.model.ProfileUserEntity
import org.the_chance.honeymart.domain.model.RecentProductEntity
import org.the_chance.honeymart.domain.model.WishListEntity
import org.the_chance.honeymart.domain.repository.HoneyMartRepository
import org.the_chance.honeymart.domain.util.NotFoundException
import javax.inject.Inject


class HoneyMartRepositoryImp @Inject constructor(
    private val honeyMartService: HoneyMartService,
) : BaseRepository(), HoneyMartRepository {

    override suspend fun checkout(): String {
        return wrap { honeyMartService.checkout() }.value ?: throw NotFoundException()
    }

    override suspend fun getAllMarkets(): List<MarketEntity> {
        Log.e("Service", "getAllMarkets${honeyMartService.getAllMarkets()}")
        return wrap { honeyMartService.getAllMarkets() }.value?.map { it.toMarketEntity() }
            ?: throw NotFoundException()
    }

    override suspend fun clipCoupon(couponId: Long): Boolean {
        return wrap { honeyMartService.clipCoupon(couponId) }.value ?: throw NotFoundException()
    }
    override suspend fun addMarketImage(marketImage: ByteArray): Boolean {
        return wrap { honeyMartService.addMarketImage(marketImage = marketImage) }.value
            ?: throw NotFoundException()
    }

    override suspend fun addMarket(
        marketName: String,
        marketAddress: String,
        marketDescription: String,
    ): Boolean {
        return wrap {
            honeyMartService.addMarket(
                marketName,
                marketAddress,
                marketDescription
            )
        }.isSuccess
    }


    override suspend fun getCart(): CartEntity =
        wrap { honeyMartService.getCart() }.value?.toCartEntity() ?: throw NotFoundException()

    override suspend fun addToCart(productId: Long, count: Int): String {
        return wrap { honeyMartService.addToCart(productId, count) }.value
            ?: throw NotFoundException()
    }

    override suspend fun deleteFromCart(productId: Long): String {
        return wrap { honeyMartService.deleteFromCart(productId) }.value
            ?: throw NotFoundException()
    }


    override suspend fun getCategoriesInMarket(marketId: Long): List<CategoryEntity> =
        wrap { honeyMartService.getCategoriesInMarket(marketId) }.value?.map { it.toCategoryEntity() }
            ?: throw NotFoundException()

    override suspend fun getMarketDetails(marketId: Long): MarketDetailsEntity =
        wrap { honeyMartService.getMarketDetails(marketId) }.value?.toMarketDetailsEntity()
            ?: throw NotFoundException()

    override suspend fun getAllProductsByCategory(
        page: Int?,
        categoryId: Long
    ): Flow<PagingData<ProductEntity>> =
        getAllWithParameter(
            categoryId,
            ::ProductsPagingSource
        )

    override suspend fun getCategoriesForSpecificProduct(productId: Long): List<CategoryEntity> =
        wrap { honeyMartService.getCategoriesForSpecificProduct(productId) }.value?.map { it.toCategoryEntity() }
            ?: throw NotFoundException()

    override suspend fun getWishList(): List<WishListEntity> =
        wrap { honeyMartService.getWishList() }.value?.map { it.toWishListEntity() }
            ?: throw NotFoundException()

    override suspend fun addToWishList(productId: Long): String =
        wrap { honeyMartService.addToWishList(productId) }.value ?: throw NotFoundException()

    override suspend fun deleteFromWishList(productId: Long): String =
        wrap { honeyMartService.deleteFromWishList(productId) }.value ?: throw NotFoundException()

    override suspend fun getAllOrders(orderState: Int): List<OrderEntity> =
        wrap { honeyMartService.getAllOrders(orderState) }.value?.map { it.toUserEntity() }
            ?: throw NotFoundException()

    override suspend fun getAllMarketOrders(orderState: Int): List<MarketOrderEntity> =
        wrap { honeyMartService.getAllMarketOrders(orderState) }.value?.map { it.toMarketOrderEntity() }
            ?: throw NotFoundException()


    override suspend fun getOrderDetails(orderId: Long): OrderDetailsEntity =
        wrap { honeyMartService.getOrderDetails(orderId) }.value?.toOrderDetailsEntity()
            ?: throw NotFoundException()

    override suspend fun searchForProducts(
        query: String,
        page: Int?,
        sortOrder: String
    ): Flow<PagingData<ProductEntity>> =
        search(
            query,
            sortOrder,
            ::SearchProductsPagingSource
        )

    override suspend fun updateOrderState(id: Long?, state: Int): Boolean =
        wrap { honeyMartService.updateOrderState(id, state) }.value ?: throw NotFoundException()


    override suspend fun getProductDetails(productId: Long): ProductEntity =
        wrap { honeyMartService.getProductDetails(productId) }.value?.toProductEntity()
            ?: throw NotFoundException()

    override suspend fun deleteAllCart(): String =
        wrap { honeyMartService.deleteAllFromCart() }.value ?: throw NotFoundException()

    override suspend fun getUserCoupons(): List<CouponEntity> {
        return wrap { honeyMartService.getUserCoupons() }.value?.map { it.toCouponEntity() }
            ?: throw NotFoundException()
    }

    override suspend fun getAllValidCoupons(): List<CouponEntity> {
        return wrap { honeyMartService.getAllValidCoupons() }.value?.map { it.toCouponEntity() }
            ?: throw NotFoundException()
    }

    override suspend fun getRecentProducts(): List<RecentProductEntity> {
        return wrap { honeyMartService.getRecentProducts() }.value?.map { it.RecentProductEntity() }
            ?: throw NotFoundException()
    }

    override suspend fun getAllProducts(): List<ProductEntity> {
        return wrap { honeyMartService.getAllProducts() }.value?.map { it.toProductEntity() }
            ?: throw NotFoundException()
    }


    override suspend fun getProfileUser(): ProfileUserEntity =
        wrap { honeyMartService.getProfileUser() }.value?.toProfileUserEntity()
            ?: throw NotFoundException()

    override suspend fun getAllNotifications(notificationsState: Int): List<NotificationEntity> =
        wrap { honeyMartService.getAllNotifications(notificationsState) }.value?.map { it.toNotificationEntity() }
            ?: throw NotFoundException()



    override suspend fun addProfileImage(image: ByteArray): String {
        return wrap {
            honeyMartService.addProfileImage(
                image = image,
            )
        }.value ?: throw NotFoundException()
    }

    private fun <I : Any, P> getAllWithParameter(
        parameter: P,
        sourceFactory: (HoneyMartService, P) -> PagingSource<Int, I>,
    ): Flow<PagingData<I>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE),
            pagingSourceFactory = { sourceFactory(honeyMartService, parameter) }
        ).flow
    }

    private fun <I : Any, P, S> search(
        parameter: P,
        sortOrder: S,
        sourceFactory: (HoneyMartService, P, S) -> PagingSource<Int, I>,
    ): Flow<PagingData<I>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE),
            pagingSourceFactory = { sourceFactory(honeyMartService, parameter, sortOrder) }
        ).flow
    }

    companion object {
        private const val DEFAULT_PAGE_SIZE = 10
    }
    override suspend fun addCategory(name: String, imageId: Int): String =
        wrap { honeyMartService.addCategory(name, imageId) }.value ?: throw NotFoundException()

    override suspend fun deleteCategory(id: Long): String {
        return wrap { honeyMartService.deleteCategory(id) }.value ?: throw NotFoundException()
    }

    override suspend fun deleteProduct(productId: Long): String {
        return wrap { honeyMartService.deleteProduct(productId) }.value ?: throw NotFoundException()
    }

    override suspend fun deleteProductImage(productId: Long): String {
        TODO("Not yet implemented")
    }

    override suspend fun addProduct(
        name: String,
        price: Double,
        description: String,
        categoryId: Long
    ): ProductEntity {
        return wrap {
            honeyMartService.addProduct(
                name = name,
                price = price,
                description = description,
                categoriesId = categoryId
            )
        }.value?.toProductEntity() ?: throw NotFoundException()
    }

    override suspend fun updateProduct(
        id: Long,
        name: String,
        price: Double,
        description: String
    ): String {
        return wrap {
            honeyMartService.updateProduct(
                id = id,
                name = name,
                price = price,
                description = description,
            )
        }.value ?: throw NotFoundException()
    }

    override suspend fun addImageProduct(
        productId: Long,
        images: List<ByteArray>
    ): String {
        return wrap {
            honeyMartService.addImageProduct(
                productId = productId,
                images = images
            )
        }.value ?: throw NotFoundException()
    }

    override suspend fun updateImageProduct(productId: Long, images: List<ByteArray>): String {
        return wrap {
            honeyMartService.updateImageProduct(productId, images)
        }.value ?: throw NotFoundException()
    }

    override suspend fun updateCategory(
        id: Long,
        name: String,
        marketId: Long,
        imageId: Int
    ): String {
        return wrap {
            honeyMartService.updateCategory(
                id = id,
                name = name,
                marketID = marketId,
                imageId = imageId
            )
        }.value ?: throw NotFoundException()
    }

    //region admin
    override suspend fun getMarketRequests(isApproved: Boolean): List<RequestEntity> {
        return wrap { honeyMartService.getMarketRequests(isApproved) }.value?.map { it.toRequestEntity() }
            ?: throw NotFoundException()
    }

    override suspend fun updateMarketRequest(id: Long?, isApproved: Boolean): Boolean {
        return wrap { honeyMartService.updateMarketRequest(id, isApproved) }.value
            ?: throw NotFoundException()
    }
//endregion admin
}