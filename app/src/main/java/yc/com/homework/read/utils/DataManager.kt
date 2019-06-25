package yc.com.homework.read.utils

import android.util.SparseArray
import yc.com.homework.read.domain.bean.PlayData

/**
 * Created by wanglin  on 2019/2/1 15:13.
 */
class DataManager private constructor() {

    init {
        dataMaps = SparseArray()
    }

    fun addDatas(index: Int, data: PlayData) {
        if (getValue(index) == null) {
            dataMaps!!.put(index, data)
        }
    }

    fun getValue(index: Int): PlayData? {
        return dataMaps!!.get(index)
    }


    fun clearDatas() {
        dataMaps?.let {
            for (i in 0 until dataMaps!!.size()) {
                dataMaps!!.remove(i)
            }
        }

    }

    companion object {
        private var dataMaps: SparseArray<PlayData>? = null

        val instance: DataManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataManager()
        }

    }


}
