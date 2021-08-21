package com.example.coindemo.ui.coinlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.coindemo.databinding.RowCoinListBinding
import com.example.coindemo.model.Coin
import com.example.coindemo.utils.FormattingUtil.formatCurrency
import com.example.coindemo.utils.FormattingUtil.roundTwoPlaces
import com.example.coindemo.utils.ViewExtensions.appendPercentage
import com.example.coindemo.utils.ViewExtensions.dp
import com.example.coindemo.utils.ViewExtensions.setColor
import java.util.*

class CoinListAdapter(private val listener: Listener? = null) :
    ListAdapter<Coin, CoinListAdapter.CoinListViewHolder>(CoinListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListViewHolder =
        CoinListViewHolder.from(listener, parent)

    override fun onBindViewHolder(holder: CoinListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CoinListViewHolder(
        private val listener: Listener?,
        private val binding: RowCoinListBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coin: Coin) {
            itemView.setOnClickListener { listener?.onCoinSelected(coin) }

            binding.coinAvatar.load(coin.imageUrl) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
            binding.tvName.text = coin.name
            binding.tvSymbol.text = coin.symbol.uppercase(Locale.getDefault())
            binding.tvPrice.text = coin.currentPrice.formatCurrency()
            binding.tvPercentChange.apply {
                text = coin.priceChangePercentage24h.roundTwoPlaces().appendPercentage()
                compoundDrawablePadding = 6.dp
                setColor(coin.priceChangePercentage24h >= 0)
            }
        }

        companion object {
            fun from(listener: Listener?, parent: ViewGroup): CoinListViewHolder {
                val binding = RowCoinListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return CoinListViewHolder(listener, binding)
            }
        }
    }

    class CoinListDiffUtil : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean =
            oldItem == newItem
    }

    interface Listener {
        fun onCoinSelected(coin: Coin)
    }
}