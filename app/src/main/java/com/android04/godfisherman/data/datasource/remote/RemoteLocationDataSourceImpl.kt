package com.android04.godfisherman.data.datasource.remote

import javax.inject.Inject

class RemoteLocationDataSourceImpl @Inject constructor()  : RemoteLocationDataSource {

    override fun loadLocationData(): Unit {
        println("this is a loadData Test")
    }
}
