package com.songlea.hongbao.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.webkit.*
import android.widget.TextView
import android.widget.Toast
import com.songlea.hongbao.R
import com.songlea.hongbao.util.ConnectivityUtil
import xyz.monkeytong.hongbao.utils.DownloadUtil
import android.view.ViewGroup


/**
 * WebView页面,可用于打开网页
 *
 * @author Song Lea
 */
class WebViewActivity : Activity() {

    /*
    WebView是一个基于webkit引擎、展现web页面的控件：
    webView.onResume():激活WebView为活跃状态，能正常执行网页的响应
    webView.onPause():当页面被失去焦点被切换到后台不可见状态,需要执行onPause,通过onPause动作通知内核暂停所有的动作,比如DOM的解析、plugin的执行、JavaScript执行
    webView.pauseTimers():当应用程序(存在WebView)被切换到后台时,这个方法不仅仅针对当前的WebView而是全局的全应用程序的WebView,它会暂停所有WebView的layout/parsing/javascript/timer,降低CPU功耗
    webView.resumeTimers():恢复pauseTimers状态
    webView.canGoBack():是否可以后退
    webView.goBack():后退网页
    webView.canGoForward():是否可以前进
    webView.goForward():前进网页
    webView.goBackOrForward(steps):以当前的index为起始点前进或者后退到历史记录中指定的steps,如果steps为负数则为后退,正数则为前进
    webView.clearCache(true):清除网页访问留下的缓存,由于内核缓存是全局的因此这个方法不仅仅针对WebView而是针对整个应用程序
    webView.clearHistory():清除当前WebView访问的历史记录,只会清除WebView访问历史记录里的所有记录而除了当前访问记录
    webView.clearFormData():这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
     */
    private var webView: WebView? = null
    private var webViewUrl: String? = null
    private var webViewTitle: String? = null
    private var webViewBar: TextView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadUI()  // 加载UI界面
        val bundle = intent.extras
        if (bundle != null && !bundle.isEmpty) {
            webViewTitle = bundle.getString(ConnectivityUtil.TITLE)
            webViewUrl = bundle.getString(ConnectivityUtil.URL)
            webViewBar = findViewById(R.id.webView_bar)
            /** 设置WebView控件 */
            webView = findViewById(R.id.webView)
            webViewBar?.text = webViewTitle
            /** WebSettings类：对WebView进行配置和管理 */
            // 这个方法可以让JS代码控制宿主程序,但同时也存在安全问题,因为JS代码可以通过反射访问到注入对象的公有域
            webView?.settings?.javaScriptEnabled = true
            //设置自适应屏幕
            // webView?.settings?.useWideViewPort = true // 将图片调整到适合WebView的大小
            // webView?.settings?.loadWithOverviewMode = true // 缩放至屏幕的大小
            // 缩放操作
            // webView?.settings?.supportZoom() // 支持缩放
            // webView?.settings?.displayZoomControls = false // 隐藏原生的缩放控件
            webView?.settings?.builtInZoomControls = false // 设置内置的缩放控件,若为false,则该WebView不可缩放
            // 使我们能够有选择的缓冲web浏览器中所有的东西,从页面、图片到脚本、css等
            webView?.settings?.domStorageEnabled = true
            // 缓存模式
            // LOAD_CACHE_ONLY: 不使用网络,只读取本地缓存数据
            // LOAD_DEFAULT:(默认)根据cache-control决定是否从网络上取数据
            // LOAD_NO_CACHE: 不使用缓存,只从网络获取数据
            // LOAD_CACHE_ELSE_NETWORK:只要本地有,无论是否过期,或者no-cache,都使用缓存中的数据
            webView?.settings?.cacheMode = WebSettings.LOAD_DEFAULT
            // webView?.settings?.defaultTextEncodingName = "utf-8"  // 设置编码格式
            webView?.settings?.loadsImagesAutomatically = true // 支持自动加载图片
            webView?.settings?.javaScriptCanOpenWindowsAutomatically = true // 支持通过JS打开新窗口
            // webView?.settings?.allowFileAccess = true // 设置可以访问文件
            // webView?.settings?.domStorageEnabled = true // 开启 DOM storage API 功能
            // webView?.settings?.databaseEnabled = true // 开启 database storage API 功能
            // webView?.settings?.setAppCacheEnabled(true) // 开启 Application Caches 功能
            /** WebViewClient类：处理各种通知&请求事件 */
            webView?.webViewClient = object : WebViewClient() {
                // 如果主机应用程序要离开当前的WebView而自身处理URL时返回true,否则返回false
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    return if (url.contains(ConnectivityUtil.APK_TYPE)) {
                        Toast.makeText(applicationContext, R.string.download_backend, Toast.LENGTH_SHORT).show()
                        DownloadUtil().enqueue(url, applicationContext)
                        true
                    } else if (!url.contains(ConnectivityUtil.HTTP_TYPE)) {
                        Toast.makeText(applicationContext, R.string.download_redirect, Toast.LENGTH_LONG).show()
                        webViewBar?.text = getString(R.string.download_hint)
                        false
                    } else {
                        view.loadUrl(url)
                        false
                    }
                }

                // 在页面加载结束时调用,们可以关闭loading 条,切换程序动作
                override fun onPageFinished(view: WebView, url: String) {
                    CookieManager.getInstance().flush()
                }

                // 开始载入页面调用的,我们可以设定一个loading的页面,告诉用户程序在等待网络响应
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    Log.i(WebViewActivity::class.java.name, "开始载入页面URL：$url")
                    super.onPageStarted(view, url, favicon)
                }

                // 在加载页面资源时会调用,每一个资源（比如图片）的加载都会调用一次
                override fun onLoadResource(view: WebView?, url: String?) {
                    Log.i(WebViewActivity::class.java.name, "在加载页面资源时会调用！URl：$url")
                    super.onLoadResource(view, url)
                }

                // 加载页面的服务器出现错误时（如404）调用
                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    val url = request?.url?.toString()
                    val code = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) error?.errorCode else 0
                    Log.i(WebViewActivity::class.java.name, "加载页面的服务器出现错误：Url：$url;Code:$code")
                    super.onReceivedError(view, request, error)
                }

                // 处理https请求
                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                    super.onReceivedSslError(view, handler, error)
                    // handler?.proceed() // 表示等待证书响应
                    handler?.cancel() //表示挂起连接,为默认方式
                    // handler.handleMessage(null) //可做其他处理
                }

            }
            /** WebChromeClient类：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等 */
            webView?.webChromeClient = object : WebChromeClient() {
                // 获得网页的加载进度并显示
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    Log.i(WebViewActivity::class.java.name, "获得网页的加载进度：$newProgress 并显示！")
                    super.onProgressChanged(view, newProgress)
                }

                // 获取Web页中的标题
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    Log.i(WebViewActivity::class.java.name, "获得网页的标题：$title")
                    super.onReceivedTitle(view, title)
                }
            }
            webView?.loadUrl(webViewUrl)
        }
    }

    // 加载UI界面
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun loadUI() {
        setContentView(R.layout.activity_webview)
        // if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return  // always true
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = 0xffE46C62.toInt()
    }

    // 在当前Activity中处理并消费掉该 Back 事件,点击返回键后,是网页回退而不是退出应用
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK
                && webView != null && webView!!.canGoBack()) {
            webView!!.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    // 销毁WebView,在关闭了Activity时,如果WebView的音乐或视频,还在播放,就必须销毁WebView
    // WebView调用destroy时,WebView仍绑定在Activity上,这是由于自定义WebView构建时传入了该Activity的context对象,
    // 因此需要先从父容器中移除WebView,然后再销毁WebView
    override fun onDestroy() {
        webView?.loadDataWithBaseURL(null, "",
                "text/html", "utf-8", null)
        webView?.clearHistory()
        webView?.destroy()
        super.onDestroy()
    }

    // 界面的事件函数
    fun openLink(view: View) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webViewUrl))
        startActivity(intent)
    }

    fun performBack(view: View) {
        Log.i(WebViewActivity::class.java.name, "返回上一个Activity")
        super.onBackPressed()
    }
}
