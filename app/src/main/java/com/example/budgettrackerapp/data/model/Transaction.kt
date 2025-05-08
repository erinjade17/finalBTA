package com.example.budgettrackerapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity(tableName = "transactions")
class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val category: String,
    val amount: Double,
    val type: TransactionType,
    val description: String? = null
)

enum class TransactionType {
    EXPENSE,
    INCOME
}

//Added Type Converters
class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class TransactionTypeConverter {
    @TypeConverter
    fun toTransactionType(value: String): TransactionType {
        return when (value) {
            "EXPENSE" -> TransactionType.EXPENSE
            "INCOME" -> TransactionType.INCOME
            else -> TransactionType.EXPENSE // Default value, handle as appropriate
        }
    }

    @TypeConverter
    fun fromTransactionType(type: TransactionType): String {
        return type.name
    }
}