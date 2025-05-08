package com.example.budgettrackerapp.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettrackerapp.R
import com.example.budgettrackerapp.viewmodels.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.content.ContextCompat
import com.example.budgettrackerapp.data.model.Transaction
import com.example.budgettrackerapp.data.model.TransactionType
import com.example.budgettrackerapp.databinding.ItemTransactionBinding

@AndroidEntryPoint
class TransactionsFragment : Fragment() {

    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var rvTransactions: RecyclerView
    private lateinit var tvNoTransactions: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTransactions = view.findViewById(R.id.rvTransactions)
        tvNoTransactions = view.findViewById(R.id.tvNoTransactions)

        setupRecyclerView()
        observeTransactions()
    }

    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(emptyList())
        rvTransactions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = transactionAdapter
        }
    }

    private fun observeTransactions() {
        viewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
            if (transactions.isNotEmpty()) {
                tvNoTransactions.visibility = View.GONE
                transactionAdapter.updateList(transactions)
            } else {
                tvNoTransactions.visibility = View.VISIBLE
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
class TransactionAdapter(private var transactionList: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(transaction.date)
            binding.tvCategory.text = transaction.category
            val amountText =
                "${if (transaction.type == TransactionType.EXPENSE) "-" else "+"} R ${String.format("%.2f", transaction.amount)}"
            binding.tvAmount.text = amountText

            if (transaction.type == TransactionType.EXPENSE) {
                binding.tvAmount.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.expenseColor
                    )
                )
            } else {
                binding.tvAmount.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.incomeColor
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
}