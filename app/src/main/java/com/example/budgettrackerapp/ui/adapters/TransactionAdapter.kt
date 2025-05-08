package com.example.budgettrackerapp.ui.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettrackerapp.R
import com.example.budgettrackerapp.databinding.ItemTransactionBinding
import com.example.budgettrackerapp.data.model.Transaction
import com.example.budgettrackerapp.data.model.TransactionType
import java.text.SimpleDateFormat
import java.util.Locale

class TransactionAdapter(private var transactionList: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(transaction.date)
            binding.tvCategory.text = transaction.category
            val amountText = "${if (transaction.type == TransactionType.EXPENSE) "-" else "+"} R ${String.format("%.2f", transaction.amount)}"
            binding.tvAmount.text = amountText

            if (transaction.type == TransactionType.EXPENSE) {
                binding.tvAmount.setTextColor(ContextCompat.getColor(binding.root.context, R.color.expenseColor))
            } else {
                binding.tvAmount.setTextColor(ContextCompat.getColor(binding.root.context, R.color.incomeColor))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val currentTransaction = transactionList[position]
        holder.bind(currentTransaction)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    fun updateList(newList: List<Transaction>) {
        transactionList = newList
        notifyDataSetChanged()
    }
}