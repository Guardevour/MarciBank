package models

import LocalDateSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Penalting(
    val id: Int,
    val employeeId: Int,
    val penaltyId: Int,
    @Serializable(with = LocalDateSerializer::class)
    val penaltyDate: LocalDateTime
)


