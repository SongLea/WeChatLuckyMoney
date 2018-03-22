package com.songlea.hongbao.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import com.songlea.hongbao.R

/**
 * 欢迎图片界面
 *
 * @author Song Lea
 */
class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 隐藏标题栏以及状态栏
        val window: Window = this.window
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // 标题是属于View的,所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_welcome)
        // 消息的延迟发送以及处理
        handler.sendEmptyMessageDelayed(what, 3000)
    }

    private val handler = Handler(Handler.Callback {
        if (it.what == WelcomeActivity.what) {
            val intent = Intent(this@WelcomeActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        false
    })

    companion object {
        // handleMessage依据what值来做不同的处理
        const val what: Int = 1
    }
}