package com.example.veritrustmobile.model

data class Servicio(
    val nombre: String,
    val descripcion: String,
    val precio: Int,
    val detalles: List<String>
)
// Esto es para hacer el backoffice luego
val listaDeServicios = listOf(
    Servicio(
        nombre = "Firma Electrónica Simple",
        descripcion = "Certificado Digital",
        precio = 15390,
        detalles = listOf(
            "Permite realizar Facturación en el SII.",
            "Centralización Automática del certificado en el sistema del SII.",
            "Compra en menos de 5 minutos. Renovación Express.",
            "Firma documentos tributarios electrónicos como facturas y notas de crédito.",
            "Lleva 1 año gratis en tu certificado digital. 3×2 años."
        )
    ),
    Servicio(
        nombre = "Firma Electrónica Avanzada Online",
        descripcion = "e-token",
        precio = 21990,
        detalles = listOf(
            "Permite firmar documentos ilimitados desde cualquier lugar.",
            "Compra 100% online con ClaveÚnica y Cédula Vigente.",
            "Adquisición instantánea e implementación fácil.",
            "Valida para todo tipo de documentos legales y comerciales."
        )
    ),
    Servicio(
        nombre = "Firma Electrónica Avanzada Notario",
        descripcion = "Uso profesional",
        precio = 42990,
        detalles = listOf(
            "Requiere Certificado de la Corte de Apelaciones.",
            "Firma portable en dispositivo e-token.",
            "Alta seguridad y validez legal."
        )
    )
)
