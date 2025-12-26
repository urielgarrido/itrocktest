package com.example.products.data.mappers

import com.example.products.data.remote.dto.FakePlatziProductDto
import com.example.products.data.remote.dto.FakeStoreProductDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `FakeStoreProductDto is mapped correctly to Product domain model`() {
        // Given: Un DTO de FakeStore con datos de prueba.
        val fakeStoreDto = FakeStoreProductDto(
            id = 1L,
            title = "Test Store Product",
            description = "This is a description from FakeStore.",
            price = 99.99,
            imageUrl = "http://example.com/store_image.jpg"
        )

        // When: Se llama a la función de extensión .toDomain().
        val product = fakeStoreDto.toDomain()

        // Then: Se comprueba que cada campo del objeto Product resultante es correcto.
        assertThat(product).isNotNull()
        assertThat(product.id).isEqualTo(1L)
        assertThat(product.name).isEqualTo("Test Store Product")
        assertThat(product.description).isEqualTo("This is a description from FakeStore.")
        assertThat(product.price).isEqualTo(99.99)
        assertThat(product.imageUrl).isEqualTo("http://example.com/store_image.jpg")
    }

    @Test
    fun `FakePlatziProductDto with image list is mapped correctly to Product domain model`() {
        // Given: Un DTO de FakePlatzi con una lista de imágenes.
        val fakePlatziDto = FakePlatziProductDto(
            id = 2L,
            title = "Test Platzi Product",
            description = "This is a description from Platzi.",
            price = 150.50,
            images = listOf(
                "http://example.com/platzi_image1.jpg",
                "http://example.com/platzi_image2.jpg",
                "http://example.com/platzi_image3.jpg"
            )
        )

        // When: Se llama a la función de extensión .toDomain().
        val product = fakePlatziDto.toDomain()

        // Then: Se comprueba que cada campo es correcto, especialmente que se toma la PRIMERA imagen de la lista.
        assertThat(product).isNotNull()
        assertThat(product.id).isEqualTo(2L)
        assertThat(product.name).isEqualTo("Test Platzi Product")
        assertThat(product.description).isEqualTo("This is a description from Platzi.")
        assertThat(product.price).isEqualTo(150.50)
        assertThat(product.imageUrl).isEqualTo("http://example.com/platzi_image1.jpg")
    }

    @Test
    fun `FakePlatziProductDto with empty image list is mapped with an empty imageUrl`() {
        // Given: Un DTO de FakePlatzi con una lista de imágenes vacía.
        val fakePlatziDtoWithEmptyImages = FakePlatziProductDto(
            id = 3L,
            title = "Product Without Image",
            description = "Description here.",
            price = 10.0,
            images = emptyList()
        )

        // When: Se llama a la función de extensión .toDomain().
        val product = fakePlatziDtoWithEmptyImages.toDomain()

        // Then: Se comprueba que el mapeo fue exitoso y que imageUrl es un string vacío, como se definió en la lógica robusta del mapper.
        assertThat(product).isNotNull()
        assertThat(product.id).isEqualTo(3L)
        assertThat(product.imageUrl).isEmpty()
    }
}