package com.android04.godfisherman.utils

import android.os.Environment
import android.os.StatFs

object StorageManager {

    fun getInternalRemainMemory(): Long {
        val stat = StatFs(Environment.getDataDirectory().path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong

        return blockSize * availableBlocks
    }

}