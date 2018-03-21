package com.songlea.hongbao.fragment

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import com.songlea.hongbao.R
import com.songlea.hongbao.activity.WebViewActivity
import com.songlea.hongbao.util.ConnectivityUtil

/**
 * 设置界面(使用PreferenceFragment进行首选项设置)
 *
 * @author Song Lea
 */
class GeneralSettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.general_preferences)
        setPrefListeners()
    }

    private fun setPrefListeners() {
        // Open issue
        val issuePref = findPreference("pref_etc_issue")
        issuePref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val webViewIntent = Intent(activity, WebViewActivity::class.java)
            webViewIntent.putExtra(ConnectivityUtil.TITLE, "GitHub Issues")
            webViewIntent.putExtra(ConnectivityUtil.URL, getString(R.string.url_github_issues))
            webViewIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(webViewIntent)
            false
        }

        val excludeWordsPref = findPreference("pref_watch_exclude_words")
        val summary = resources.getString(R.string.pref_watch_exclude_words_summary)
        val value = PreferenceManager.getDefaultSharedPreferences(activity)
                .getString("pref_watch_exclude_words", "")
        if (value?.isNotEmpty() == true) excludeWordsPref.summary = summary + ":" + value

        excludeWordsPref.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
            val summaryDescription = resources.getString(R.string.pref_watch_exclude_words_summary)
            if (o != null && o.toString().isNotEmpty()) {
                preference.summary = summaryDescription + ":" + o.toString()
            } else {
                preference.summary = summaryDescription
            }
            true
        }
    }
}
