package com.example.products.data.dataSource

import com.example.products.domain.dataSource.PurchaseRemoteDataSource
import com.example.products.domain.models.Product
import com.example.products.domain.models.Purchase
import com.example.products.domain.models.PurchaseState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await

const val USER_COLLECTION_FIRESTORE = "users"
const val PURCHASES_COLLECTION_FIRESTORE = "purchases"

class PurchaseRemoteDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : PurchaseRemoteDataSource {

    override suspend fun savePurchase(product: Product, userUID: String) = flow {
        runCatching {
            val purchase = Purchase(
                id = System.currentTimeMillis().toString(),
                productId = product.id,
                productName = product.name,
                purchaseState = PurchaseState.COMPLETED
            )
            val document = firestore
                .collection(USER_COLLECTION_FIRESTORE)
                .document(userUID)
                .collection(PURCHASES_COLLECTION_FIRESTORE)
                .add(purchase)
                .await()
            if (document.id.isNotEmpty()) {
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Error al guardar la compra")))
            }
        }.onFailure { throwable ->
            when (throwable) {
                else -> throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getPurchaseHistory(userUID: String): Flow<List<Purchase>> = flow {
        runCatching {
            val snapshot = firestore
                .collection(USER_COLLECTION_FIRESTORE)
                .document(userUID)
                .collection(PURCHASES_COLLECTION_FIRESTORE)
                .orderBy("purchaseDate", Query.Direction.DESCENDING)
                .get()
                .await()
            val purchases = snapshot.documents.map { doc ->
                Purchase(
                    id = doc.id,
                    productId = doc.getLong("productId") ?: 0L,
                    productName = doc.getString("productName").orEmpty(),
                    purchaseState = PurchaseState.COMPLETED,
                    purchaseDate = doc.getLong("purchaseDate") ?: 0L
                )
            }
            emit(purchases)
        }.onFailure { throwable ->
            when (throwable) {
                else -> throw throwable
            }
        }
    }.flowOn(Dispatchers.IO)
}