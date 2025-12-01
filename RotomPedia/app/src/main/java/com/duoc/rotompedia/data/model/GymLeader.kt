package com.duoc.rotompedia.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GymLeader(
    val id: Long,
    val name: String,
    val region: String,
    val typeSpecialty: String,
    val badgeName: String,
    val imageUrl: String
)