package com.songlea.hongbao.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.songlea.hongbao.R
import com.songlea.hongbao.fragment.GeneralSettingsFragment
import com.songlea.hongbao.util.LuckyMoneyUtil

/**
 * 偏好设置界面
 *
 * @author Song Lea
 */
class SettingsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        // 状态栏设置
        LuckyMoneyUtil.loadUI(this.window)
        // 偏好设置
        prepareSettings()
    }

    private fun prepareSettings() {
        val title: String?
        val fragId: String?

        val bundle = intent.extras
        if (bundle != null) {
            title = bundle.getString(LuckyMoneyUtil.TITLE)
            fragId = bundle.getString(LuckyMoneyUtil.FRAG_ID)
        } else {
            title = getString(R.string.preference)
            fragId = LuckyMoneyUtil.GENERAL_SETTING
        }
        // 设置标题
        val textView: TextView = findViewById(R.id.settings_bar)
        textView.text = title
        // 在Fragments上开启一系列的编辑操作
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (LuckyMoneyUtil.GENERAL_SETTING == fragId) {
            // 偏好设置的Fragment
            fragmentTransaction.replace(R.id.preferences_fragment,
                    GeneralSettingsFragment())
        }
        fragmentTransaction.commit()
    }

    fun performBack(view: View) {
        Log.i(SettingsActivity::class.java.name, "返回上一个Activity")
        super.onBackPressed()
    }

    fun enterAccessibilityPage(view: View) {
        Toast.makeText(this, getString(R.string.turn_on_toast), Toast.LENGTH_SHORT).show()
        // 无障碍服务开启界面
        val mAccessibleIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(mAccessibleIntent)
    }
}
