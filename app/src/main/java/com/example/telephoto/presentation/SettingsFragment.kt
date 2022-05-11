package com.example.telephoto.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.databinding.FragmentSettingsBinding
import com.example.telephoto.factory.SettingsViewModelFactory
import com.example.telephoto.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private val contextApp = TelegramBotApp.context
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel = ViewModelProvider(this, SettingsViewModelFactory(contextApp = contextApp))[SettingsViewModel::class.java]

        binding.tokenEditText.setText(viewModel.tokenString)
        val warning = binding.warning
        val tokenEditText = binding.tokenEditText
        tokenEditText.doAfterTextChanged { warning.visibility = View.GONE }

        val recyclerView = binding.clientRecyclerView
        recyclerView.adapter = viewModel.adapter
        recyclerView.layoutManager = LinearLayoutManager(contextApp)

        binding.btnOk.setOnClickListener {
            if (tokenEditText.text.toString() == ""){
                warning.visibility = View.VISIBLE
            }else{
                viewModel.saveToken(tokenEditText.text.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}