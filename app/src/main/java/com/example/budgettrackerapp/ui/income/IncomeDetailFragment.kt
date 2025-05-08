package com.example.budgettrackerapp.ui.income

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgettrackerapp.R
import com.example.budgettrackerapp.databinding.FragmentIncomeDetailBinding
import com.example.budgettrackerapp.utils.DateUtils
import com.example.budgettrackerapp.viewmodels.IncomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IncomeDetailFragment<IncomeDetailFragmentArgs> : Fragment() {

    private var _binding: FragmentIncomeDetailBinding? = null
    private val binding get() = _binding!!
    private val args: IncomeDetailFragmentArgs by navArgs()
    private val viewModel: IncomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncomeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        loadIncomeDetails()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_edit -> {
                    // Navigate to edit screen
                    true
                }
                R.id.action_delete -> {
                    showDeleteConfirmationDialog()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadIncomeDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val income = viewModel.getIncomeById(args.incomeId)
                if (income != null) {
                    binding.tvAmount.text = getString(R.string.price_format, income.amount)
                    binding.tvDescription.text = income.description
                    binding.tvDate.text = DateUtils.formatDate(income.date)
                    binding.tvSource.text = income.sourceId.toString() // Replace with actual source name retrieval if available
                }
            } catch (e: Exception) {
                Log.e("IncomeDetailFragment", "Error loading income details: ${e.message}", e)
                Toast.makeText(requireContext(), "Failed to load income details.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showDeleteConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Income")
            .setMessage("Are you sure you want to delete this income?")
            .setPositiveButton("Delete") { dialog, which ->
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        val incomeToDelete = viewModel.getIncomeById(args.incomeId)
                        if (incomeToDelete != null) {
                            viewModel.deleteIncome(incomeToDelete)
                            findNavController().navigateUp()
                        }
                    } catch (e: Exception) {
                        Log.e("IncomeDetailFragment", "Error deleting income: ${e.message}", e)
                        Toast.makeText(
                            requireContext(),
                            "Failed to delete income.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

