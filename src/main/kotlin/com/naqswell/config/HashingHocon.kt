package com.naqswell.config

data class HashingHocon(
    val algorithm: String,
    val iterations: Int,
    val keyLength: Int,
    val saltLength: Int
)