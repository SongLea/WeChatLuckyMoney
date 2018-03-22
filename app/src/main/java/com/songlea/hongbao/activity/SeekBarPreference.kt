package com.songlea.hongbao.activity

import android.content.Context
import android.preference.DialogPreference
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.songlea.hongbao.R

/**
 * 对话框界面(红包延迟拆开设置)
 *
 * @author Song Lea
 */
class SeekBarPreference(context: Context, attrs: AttributeSet) : DialogPreference(context, attrs) {
    /*
        DialogPreference:基于Dialog,当我们点击的时候会以对话框的形式显示出来,所以除了继承自Preference的属性之外也还新增了很多自己独有的属性和方法
        dialogIcon:对话框的icon
        dialogLayout:对话框的ContentView布局
        dialogMessage:对话框的内容
        dialogTitle:对话框的标题
        negativeButtonText:对话框里按钮1名称
        positiveButtonText:对话框里按钮2名称
     */
    private var seekBar: SeekBar? = null
    private var textView: TextView? = null
    private var hintText: String? = null
    private var prefKind: String? = null

    init {
        // 对话框布局文件
        dialogLayoutResource = R.layout.preference_seekbar
        for (i in 0 until attrs.attributeCount) {
            val attr = attrs.getAttributeName(i)
            if ("pref_kind".equals(attr, ignoreCase = true)) {
                prefKind = attrs.getAttributeValue(i)
                break
            }
        }
        // 延迟拆开红包
        if (prefKind == "pref_open_delay")
            hintText = getContext().getString(R.string.delay_open)
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        val pref = sharedPreferences
        // 默认拆开延迟0
        val delay = pref.getInt(prefKind, 0)
        this.seekBar = view.findViewById(R.id.delay_seekBar) as SeekBar
        this.seekBar?.progress = delay
        this.textView = view.findViewById(R.id.pref_seekbar_textview) as TextView
        setHintText(delay) // 设置默认提示文字

        this.seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            // 进度条值变化时调用
            override fun onProgressChanged(seekBar: SeekBar, i: Int, fromUser: Boolean) {
                // 上面的fromUser这个参数,当触发这个函数是由于用户拖拽行为造成的,那么fromUser就为true
                if (fromUser) setHintText(i)
            }

            // 该方法在进度条开始拖动的时候调用
            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            // 该方法在进度条停止拖动的时候调用
            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        // positiveResult=true表示用户点击了positive按钮
        if (positiveResult) {
            val editor = editor
            editor.putInt(prefKind, this.seekBar?.progress ?: 0)
            editor.commit()
        }
        super.onDialogClosed(positiveResult)
    }

    // 对话框下的说明文字
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
