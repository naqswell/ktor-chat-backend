package com.naqswell.common

import io.ktor.http.*

sealed class ServerResponse<out T> {
    data class Data<T>(val data: T) : ServerResponse<T>()
    data class ErrorStatus(val status: HttpStatusCode, val message: String = "") : ServerResponse<Nothing>()
}