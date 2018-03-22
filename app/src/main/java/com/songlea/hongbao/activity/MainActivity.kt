package com.songlea.hongbao.activity

import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.songlea.hongbao.R

import com.songlea.hongbao.util.LuckyMoneyUtil

/**
 * 应用主界面
 *
 * @author Song Lea
 */
class MainActivity : Activity(), AccessibilityManager.AccessibilityStateChangeListener {

    // 开关切换按钮
    private lateinit var pluginStatusText: TextView
    private lateinit var pluginStatusIcon: ImageView

    // AccessibilityService管理
    private lateinit var accessibilityManager: AccessibilityManager

    // 获取HongbaoService 是否启用状态
    private val isServiceEnabled: Boolean
        get() {
            val accessibilityServices = accessibilityManager
                    .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
            return accessibilityServices.any { it.id == packageName + "/.services.LuckyMoneyService" }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pluginStatusText = findViewById(R.id.main_layout_control_accessibility_text)
        pluginStatusIcon = findViewById(R.id.main_layout_control_accessibility_icon)
        // 适配MI UI沉浸状态栏
        LuckyMoneyUtil.loadUI(this.window)
        // 偏好设置
        explicitlyLoadPreferences()
        accessibilityManager = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        accessibilityManager.addAccessibilityStateChangeListener(this)
        // 更新红包服务状态
        updateServiceStatus()
    }

    private fun explicitlyLoadPreferences() {
        // android下配置偏好信息的管理
        PreferenceManager.setDefaultValues(this, R.xml.general_preferences, false)
    }

    override fun onResume() {
        super.onResume()
        updateServiceStatus()
    }

    override fun onDestroy() {
        //移除监听服务
        accessibilityManager.removeAccessibilityStateChangeListener(this)
        super.onDestroy()
    }

    override fun onAccessibilityStateChanged(enabled: Boolean) {
        updateServiceStatus()
    }

    // 开启系统服务界面
    fun openAccessibility(view: View) {
        try {
            Toast.makeText(this, getString(R.string.turn_on_toast)
                    + pluginStatusText.text, Toast.LENGTH_SHORT).show()
            val accessibleIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(accessibleIntent)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.turn_on_error_toast),
                    Toast.LENGTH_LONG).show()
            Log.e(MainActivity::class.java.name, e.message)
        }
    }

    // 打开设置界面
    fun openSettings(view: View) {
        val settingsIntent = Intent(this, SettingsActivity::class.java)
        settingsIntent.putExtra(LuckyMoneyUtil.TITLE, getString(R.string.preference))
        settingsIntent.putExtra(LuckyMoneyUtil.FRAG_ID, LuckyMoneyUtil.GENERAL_SETTING)
        startActivity(settingsIntent)
    }

    // 打开对应的github主页
    fun openGitHub(view: View) {
        val webViewIntent = Intent(this, WebViewActivity::class.java)
        webViewIntent.putExtra(LuckyMoneyUtil.TITLE, getString(R.string.webview_github_title))
        webViewIntent.putExtra(LuckyMoneyUtil.URL, getString(R.string.webview_github_url))
        startActivity(webViewIntent)
    }

    /**
     * 更新当前 HongbaoService 显示状态
     */
    private fun updateServiceStatus() {
        if (isServiceEnabled) {
            pluginStatusText.setText(R.string.service_off)
            pluginStatusIcon.setBackgroundResource(R.mipmap.ic_stop)
        } else {
            pluginStatusText.setText(R.string.service_on)
            pluginStatusIcon.setBackgroundResource(R.mipmap.ic_start)
        }
    }

}
