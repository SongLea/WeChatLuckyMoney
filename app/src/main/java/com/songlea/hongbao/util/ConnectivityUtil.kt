package com.songlea.hongbao.util

import android.content.Context
import android.net.ConnectivityManager

/**
 * 网络连接工具类
 *
 * @author Song Lea
 */
object ConnectivityUtil {

    // 常量
    const val TITLE = "title"
    const val URL = "url"
    const val FRAG_ID = "frag_id"
    const val HTTP_TYPE = "http"
    const val APK_TYPE = "apk"
    const val GENERAL_SETTING = "GeneralSettingsFragment"

    // 判断网络连接是否可用且连接类型是否为WIFI
    fun isWifi(context: Context): Boolean {
        // getSystemService是Android很重要的一个API,它是Activity的一个方法；
        // 根据传入的NAME来取得对应的Object,然后转换成相应的服务对象：
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
                && activeNetwork.type == ConnectivityManager.TYPE_WIFI
    }
}
