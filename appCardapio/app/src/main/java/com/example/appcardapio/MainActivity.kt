package com.example.appcardapio

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.webkit.WebViewClient
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


class MainActivity : Activity () {

    var collapseNotificationHandler: Handler? = null

    lateinit var myWebView: WebView;

    override fun onPause() {
        super.onPause()
        collapseNow();
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        // Activity's been paused
    }

    override fun onResume() {
        super.onResume()
        collapseNow();
        // Activity's been resumed

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        setContentView(R.layout.activity_main)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        collapseNow();
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        myWebView = findViewById(R.id.webview)
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.builtInZoomControls = true;
        myWebView.settings.useWideViewPort = true;
        myWebView.settings.loadWithOverviewMode = true;
        myWebView.scrollBarStyle = View.SCROLLBARS_OUTSIDE_OVERLAY;


        myWebView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url != null) {
                    view?.loadUrl(url)
                }
                return true
            }
        }
        myWebView.loadUrl("https://app.jotaja.com/suahamburgueria/mesa/1")
        WebView.setWebContentsDebuggingEnabled(false)
    }

    override fun onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }


    override fun onStart() {
        super.onStart()
        collapseNow();
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        val decorView = window.decorView

            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
            // Method that handles loss of window focus
            collapseNow();
        val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }


    fun collapseNow() {
        // Initialize 'collapseNotificationHandler'
        if (collapseNotificationHandler == null) {
            collapseNotificationHandler = Handler()
        }
        // Post a Runnable with some delay - currently set to 300 ms
            collapseNotificationHandler!!.post(object : Runnable {
                override fun run() {

                    // Use reflection to trigger a method from 'StatusBarManager'
                    val statusBarService = getSystemService(STATUS_BAR_SERVICE)
                    var statusBarManager: Class<*>? = null
                    try {
                        statusBarManager = Class.forName("android.app.StatusBarManager")
                    } catch (e: ClassNotFoundException) {
                        e.printStackTrace()
                    }
                    var collapseStatusBar: Method? = null
                    try {

                        // Prior to API 17, the method to call is 'collapse()'
                        // API 17 onwards, the method to call is `collapsePanels()`
                        collapseStatusBar = if (Build.VERSION.SDK_INT > 16) {
                            statusBarManager!!.getMethod("collapsePanels")
                        } else {
                            statusBarManager!!.getMethod("collapse")
                        }
                    } catch (e: NoSuchMethodException) {
                        e.printStackTrace()
                    }
                    collapseStatusBar?.setAccessible(false)
                    try {
                        collapseStatusBar?.invoke(statusBarService)
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    } catch (e: InvocationTargetException) {
                        e.printStackTrace()
                    }
                        collapseNotificationHandler!!.post(this)
                }
            })
    }
}
