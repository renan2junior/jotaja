package com.example.appcardapio

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat.startActivity

class MainWebBiew : WebViewClient() {




    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {



        return super.shouldOverrideKeyEvent(view, event)

    }

}