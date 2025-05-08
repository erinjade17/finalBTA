package com.example.budgettrackerapp.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import  com.example.budgettrackerapp.data.model.Transaction
import com.example.budgettrackerapp.repository.TransactionRepository // Assuming you have this
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.sql.Date
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository // Inject your repository
) : ViewModel() {

    private val _transactions = MutableLiveData<List<Transaction>>()
    val transactions: LiveData<List<Transaction>> = _transactions

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            // Replace with your actual data fetching using the repository
            val result = transactionRepository.getAllTransactions()
            _transactions.value = result
        }
    }
}