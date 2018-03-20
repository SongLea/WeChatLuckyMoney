package xyz.monkeytong.hongbao.utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment

import android.content.Context.DOWNLOAD_SERVICE

/**
 * 下载服务工具类
 *
 * @author Song Lea
 */
class DownloadUtil {

    fun enqueue(url: String, context: Context) {
        val r = DownloadManager.Request(Uri.parse(url))
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "")
        r.allowScanningByMediaScanner()
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        // 获取系统下载服务
        val dm = context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        dm.enqueue(r)
    }
}
