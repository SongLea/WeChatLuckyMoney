package com.songlea.hongbao.util

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.view.Window
import android.view.WindowManager

/**
 * 网络连接工具类
 *
 * @author Song Lea
 */
object LuckyMoneyUtil {

    // 常量
    const val TITLE = "title"
    const val URL = "url"
    const val FRAG_ID = "frag_id"
    const val APK_TYPE = "apk"
    const val HTTP_TYPE = "http"
    const val GENERAL_SETTING = "GeneralSettingsFragment"

    // 判断网络连接是否可用且连接类型是否为WIFI
    fun isWIFI(context: Context): Boolean {
        // getSystemService是Android很重要的一个API,它是Activity的一个方法；
        // 根据传入的NAME来取得对应的Object,然后转换成相应的服务对象：
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
                && activeNetwork.type == ConnectivityManager.TYPE_WIFI
    }

    // 沉浸状态栏设置
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun loadUI(window: Window) {
        // if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return  // always true
        // 状态栏设置为透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        // 需要设置这个flag才能调用setStatusBarColor来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = 0xffE46C62.toInt()
    }
}
