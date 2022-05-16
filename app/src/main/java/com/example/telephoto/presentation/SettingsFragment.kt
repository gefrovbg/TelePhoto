package com.example.telephoto.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.telephoto.R
import com.example.telephoto.TelegramBotApp
import com.example.telephoto.databinding.FragmentSettingsBinding
import com.example.telephoto.factory.SettingsViewModelFactory
import com.example.telephoto.presentation.usecase.ShowCustomDialogUseCase
import com.example.telephoto.viewmodel.SettingsViewModel


class SettingsFragment : Fragment() {

    private val contextApp = TelegramBotApp.context
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SettingsViewModel
    private val showCustomDialogUseCase = ShowCustomDialogUseCase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this, SettingsViewModelFactory(contextApp = contextApp, this))[SettingsViewModel::class.java]

        if (!viewModel.showDescriptionBoolean.value!!) {
            val view = layoutInflater.inflate(R.layout.dialog_for_description, null)
            val string = R.string.description
            showCustomDialogUseCase.execute(view, string)
        }
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.tokenEditText.setText(viewModel.tokenString.value)
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