package com.songlea.hongbao.activity

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.songlea.hongbao.R
import com.songlea.hongbao.fragment.GeneralSettingsFragment
import com.songlea.hongbao.util.ConnectivityUtil

/**
 * 偏好设置界面
 *
 * @author Song Lea
 */
class SettingsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)
        loadUI()
        prepareSettings()
    }

    // 加载设置界面
    private fun prepareSettings() {
        val title: String?
        val fragId: String?
        val bundle = intent.extras
        if (bundle != null) {
            title = bundle.getString(ConnectivityUtil.TITLE)
            fragId = bundle.getString(ConnectivityUtil.FRAG_ID)
        } else {
            title = getString(R.string.preference)
            fragId = ConnectivityUtil.GENERAL_SETTING
        }
        val textView: TextView = findViewById(R.id.settings_bar)
        textView.text = title
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (ConnectivityUtil.GENERAL_SETTING == fragId) {
            fragmentTransaction.replace(R.id.preferences_fragment, GeneralSettingsFragment())
        }
        fragmentTransaction.commit()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun loadUI() {
        // if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return // always true
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = 0xffE46C62.toInt()
    }

    fun performBack(view: View) {
        Log.i(SettingsActivity::class.java.name, "返回上一个Activity")
        super.onBackPressed()
    }

    fun enterAccessibilityPage(view: View) {
        Toast.makeText(this, getString(R.string.turn_on_toast), Toast.LENGTH_SHORT).show()
        val mAccessibleIntent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(mAccessibleIntent)
    }
}
