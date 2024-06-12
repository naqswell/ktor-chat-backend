package com.naqswell.security.hashing

import com.naqswell.config.HashingHocon
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import java.security.spec.KeySpec
import java.util.HexFormat
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.text.toCharArray

class HashingServiceImpl(private val config: HashingHocon): HashingService {

    override fun generateSaltedHash(password: String, salt: String?): SaltedHash {
        try {
            val newSalt = salt?.decodeHex() ?: generateRandomSalt()
            val spec: KeySpec = PBEKeySpec(password.toCharArray(), newSalt, config.iterations, config.keyLength)

            val factory: SecretKeyFactory = SecretKeyFactory.getInstance(config.algorithm)
            val key: SecretKey = factory.generateSecret(spec)
            val hash: ByteArray = key.encoded

            return SaltedHash(
                hash = hash.toHexString(),
                salt = newSalt.toHexString(),
            )
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } catch (e: InvalidKeySpecException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } catch (e: IllegalStateException) {
            throw AssertionError("Error while decoding a salt: " + e.message, e)
        }
    }

    override fun verify(password: String, expectedSaltedHash: SaltedHash): Boolean {
        val passwordSaltedHash = generateSaltedHash(password, expectedSaltedHash.salt)
        return passwordSaltedHash.hash.indices.all { passwordSaltedHash.hash[it] == expectedSaltedHash.hash[it] }
    }

    private fun generateRandomSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(config.saltLength)
        random.nextBytes(salt)
        return salt
    }

    private fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) { "Must have an even length" }

        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }

    private fun ByteArray.toHexString(): String =
        HexFormat.of().formatHex(this)
}