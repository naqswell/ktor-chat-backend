package com.naqswell.security.hashing

interface HashingService {
    fun generateSaltedHash(password: String, salt: String? = null): SaltedHash
    fun verify(password: String, expectedSaltedHash: SaltedHash): Boolean
}
