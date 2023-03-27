package com.example.appcardapio2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.example.appcardapio2.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    lateinit var myWebView: WebView;

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        if (!sharedPreferences.contains("loja")) {
            sharedPreferences.edit().putString("loja","https://www.site.jotaja.com/").apply()
        }
        var url = sharedPreferences.getString("loja", "")

        myWebView = binding.root.findViewById(R.id.webview)
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
        myWebView.loadUrl(url.toString())
        WebView.setWebContentsDebuggingEnabled(false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Menu"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}