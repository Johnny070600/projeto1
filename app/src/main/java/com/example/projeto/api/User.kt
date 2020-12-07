package com.example.projeto.api

data class User(
        val id: Int,
        val name: String,
        val password: String
)

data class problemas(
        val id: Int,
        val descr: String,
        val latitude: String,
        val longitude: String,
        val user_id: Int
)