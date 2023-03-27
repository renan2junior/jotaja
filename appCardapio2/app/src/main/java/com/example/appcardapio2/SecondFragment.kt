package com.example.appcardapio2

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.appcardapio2.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
        if (!sharedPreferences.contains("loja")) {
            sharedPreferences.edit().putString("loja","https://www.site.jotaja.com/").apply()
        }
        var url = sharedPreferences.getString("loja", "")
        binding.editTextTextUrl.setText(url.toString())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSecond.setOnClickListener {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(binding.root.context)
            val url = binding.editTextTextUrl.text.toString()
            sharedPreferences.edit().putString("loja", url).apply()
            findNavController().popBackStack();
        }
    }
    override fun onStart() {
        super.onStart()
        (activity as? AppCompatActivity)?.supportActionBar?.title = "Configurações"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}