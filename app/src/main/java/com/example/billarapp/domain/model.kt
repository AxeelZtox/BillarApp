package com.example.billarapp.domain


@kotlinx.serialization.Serializable
data class ProductoModel(
    val id_producto: Int,
    val id_proveedor: Int,
    val det_producto: String,
    val precio: Float,
    val Cantidad_Inv: Int?=0
)

@kotlinx.serialization.Serializable
data class ModeloProveedor(
    @kotlinx.serialization.SerialName("id_proveedor")
    val id_proveedor: Int = 0,
    @kotlinx.serialization.SerialName("nombre")
    val nombre: String,
    @kotlinx.serialization.SerialName("telefono")
    val telefono: String
)