package com.android04.godfisherman.network.response

data class RegionResponse(
    val results: List<Result>
)

data class Result(
    val name: String,
    val region: Region
)

data class Region(
    val area1: Area1,
    val area2: Area2,
    val area3: Area3
)

data class Area1(
    val name: String
)

data class Area2(
    val name: String
)

data class Area3(
    val name: String
)