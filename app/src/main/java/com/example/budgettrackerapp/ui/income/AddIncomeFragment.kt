package com.example.budgettrackerapp.ui.income

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.budgettrackerapp.databinding.FragmentAddIncomeBinding
import com.example.budgettrackerapp.utils.DateUtils
import com.example.budgettrackerapp.utils.SessionManager
import com.example.budgettrackerapp.viewmodels.IncomeViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddIncomeFragment : Fragment() {

    private var _binding: FragmentAddIncomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IncomeViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private var selectedDate: Long = System.currentTimeMillis()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddIncomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        setupDatePickers()
        setupClickListeners()
        observeIncomeOperationResult() // Call the observation function
    }

    private fun setupDatePickers() {
        binding.btnDatePicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(selectedDate)
                .build()

            datePicker.addOnPositiveButtonClickListener { selectedDateMillis ->
                selectedDate = selectedDateMillis
                binding.btnDatePicker.text = DateUtils.formatDate(selectedDate)
            }

            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }

        // Set initial date
        binding.btnDatePicker.text = DateUtils.formatDate(selectedDate)
    }

    private fun setupClickListeners() {
        binding.btnSaveIncome.setOnClickListener {
            saveIncome()
        }
    }

    private fun saveIncome() {
        val amount = binding.etAmount.text.toString().toDoubleOrNull()
        val description = binding.etDescription.text.toString().trim()
        val sourceId = 1L // Get selected income source ID from spinner

        if (amount == null || amount <= 0) {
            binding.tilAmount.error = "Enter valid amount"
            return
        }

        if (description.isEmpty()) {
            binding.tilDescription.error = "Enter description"
            return
        }

        viewModel.addIncome(
            amount = amount,
            description = description,
            date = selectedDate,
            sourceId = sourceId,
            userId = sessionManager.getUserId()
        )
    }

    private fun observeIncomeOperationResult() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.incomeOperationResult.collectLatest { result ->
                result?.onSuccess {
                    findNavController().navigateUp()
                }
                result?.onFailure { throwable ->
                    // Show error
                    Toast.makeText(requireContext(), "Failed to save income: ${throwable.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}