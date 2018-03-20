package com.songlea.hongbao.activity

import android.content.Context
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.songlea.hongbao.R

/**
 * 对话框界面
 *
 * @author Song Lea
 */
class SeekBarPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {

    private var seekBar: SeekBar? = null
    private var textView: TextView? = null
    private var hintText: String? = null
    private var prefKind: String? = null

    // 构造函数初始化
    init {
        dialogLayoutResource = R.layout.preference_seekbar
        for (i in 0 until attrs.attributeCount) {
            val attr = attrs.getAttributeName(i)
            if ("pref_kind".equals(attr, ignoreCase = true)) {
                prefKind = attrs.getAttributeValue(i)
                break
            }
        }
        if (prefKind == "pref_open_delay")
            hintText = getContext().getString(R.string.delay_open)
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        val pref = sharedPreferences

        val delay = pref.getInt(prefKind, 0)
        this.seekBar = view.findViewById(R.id.delay_seekBar) as SeekBar
        this.seekBar?.progress = delay

        this.textView = view.findViewById(R.id.pref_seekbar_textview) as TextView
        setHintText(0)

        this.seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                setHintText(i)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            val editor = editor
            editor.putInt(prefKind, this.seekBar?.progress ?: 0)
            editor.commit()
        }
        super.onDialogClosed(positiveResult)
    }

    private fun setHintText(delay: Int) {
        if (delay == 0) {
            this.textView?.text = context.getString(R.string.delay_instantly) + hintText
        } else {
            this.textView?.text = context.getString(R.string.delay_delay) + delay +
                    context.getString(R.string.delay_sec) +
                    context.getString(R.string.delay_then) + hintText
        }
    }
}
