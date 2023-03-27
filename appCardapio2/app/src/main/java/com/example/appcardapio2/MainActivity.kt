package com.example.appcardapio2

import android.accessibilityservice.AccessibilityService.GLOBAL_ACTION_DISMISS_NOTIFICATION_SHADE
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.appcardapio2.databinding.ActivityMainBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


class MainActivity : AppCompatActivity() {

    var collapseNotificationHandler: Handler? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS, WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        collapseNow();
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }


        setSupportActionBar(binding.toolbar)
        val actionBar = supportActionBar
        actionBar?.title = "Ola"

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
            abrirConfiguracoes()
                true
        }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun abrirConfiguracoes(){

        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("Senha de administrador")
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
        val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Confirmar") {
                _,_ ->
            if(editText.text.toString() == "123"){
                val navController = findNavController(R.id.nav_host_fragment_content_main)
                navController.navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }
        builder.setNegativeButton("Cancelar") {
            dialog,_-> dialog.dismiss()
        }
        builder.show()
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        val decorView = window.decorView

            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun onPause() {
        super.onPause()
        collapseNow();
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun onResume() {
        super.onResume()
        collapseNow();
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        collapseNow();
        val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    /*** Dismisses the notification panel when pulled down  */
//    fun dismissNotificationShade(): Boolean {
//        try {
//            if (Build.VERSION.SDK_INT >= 31) {
//                return performGlobalAction(GLOBAL_ACTION_DISMISS_NOTIFICATION_SHADE)
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return false
//    }

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