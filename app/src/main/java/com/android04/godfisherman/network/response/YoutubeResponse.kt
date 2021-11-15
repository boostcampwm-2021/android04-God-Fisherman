package com.android04.godfisherman.network.response

data class YoutubeResponse(
    val items : List<YoutubeItem>
)

data class YoutubeItem(
    val id: YoutubeId,
    val snippet: YoutubeSnippet
)

data class YoutubeId(
    val videoId: String
)

data class YoutubeSnippet(
    val title: String,
    val thumbnails: YoutubeThumbnails
)

data class YoutubeThumbnails(
    val default: YoutubeThumbnail,
    val medium: YoutubeThumbnail,
    val high: YoutubeThumbnail
)

data class YoutubeThumbnail(
    val url: String,
    val width: Int,
    val height: Int
)