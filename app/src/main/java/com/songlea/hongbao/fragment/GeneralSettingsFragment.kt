package com.songlea.hongbao.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.songlea.hongbao.R
import com.songlea.hongbao.activity.WebViewActivity
import com.songlea.hongbao.util.LuckyMoneyUtil

/**
 * 设置界面(使用PreferenceFragment进行首选项设置)
 *
 * @author Song Lea
 */
class GeneralSettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.general_preferences)
        // 设置各节点的事件
        setPreferenceListeners()
    }

    private fun setPreferenceListeners() {
        // Open issue
        val issuePref = findPreference("pref_etc_issue")
        issuePref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val webViewIntent = Intent(activity, WebViewActivity::class.java)
            webViewIntent.putExtra(LuckyMoneyUtil.TITLE, "GitHub Issues")
            webViewIntent.putExtra(LuckyMoneyUtil.URL, getString(R.string.url_github_issues))
            webViewIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(webViewIntent)
            // true代表点击事件已成功捕捉,无须执行默认动作或返回上层调用链,例如不跳转至默认Intent;
            // false代表执行默认动作并且返回上层调用链,例如跳转至默认Intent
            false
        }


        val excludeWordsPref = findPreference("pref_watch_exclude_words")
        val value = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString("pref_watch_exclude_words", "")
        val summaryDescription = getString(R.string.pref_watch_exclude_words_summary)
        if (value?.isNotEmpty() == true)
            excludeWordsPref.summary = summaryDescription + ":" + value
        excludeWordsPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
            if (o != null && o.toString().isNotEmpty()) {
                preference.summary = summaryDescription + ":" + o.toString()
            } else {
                preference.summary = summaryDescription
            }
            // true代表将新值写入sharedPreference文件中;false 则不将新值写入sharedPreference文件.
            true
        }
    }
}
