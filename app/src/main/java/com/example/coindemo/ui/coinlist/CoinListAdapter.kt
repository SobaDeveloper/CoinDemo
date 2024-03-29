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
import com.example.coindemo.utils.FormatterExtensions.formatCurrency
import com.example.coindemo.utils.FormatterExtensions.roundTwoPlaces
import com.example.coindemo.utils.ViewExtensions.appendPercentage
import com.example.coindemo.utils.ViewExtensions.dp
import com.example.coindemo.utils.ViewExtensions.setColor
import java.util.*

typealias OnCoinSelected = ((Coin) -> Unit)

class CoinListAdapter(private val onCoinSelected: OnCoinSelected) :
    ListAdapter<Coin, CoinListAdapter.CoinListViewHolder>(CoinListDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListViewHolder =
        CoinListViewHolder.from(onCoinSelected, parent)

    override fun onBindViewHolder(holder: CoinListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CoinListViewHolder(
        private val onCoinSelected: OnCoinSelected,
        private val binding: RowCoinListBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coin: Coin) {
            itemView.setOnClickListener {
                onCoinSelected(coin) }

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
            fun from(onCoinSelected: OnCoinSelected, parent: ViewGroup): CoinListViewHolder {
                val binding = RowCoinListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return CoinListViewHolder(onCoinSelected, binding)
            }
        }
    }

    class CoinListDiffUtil : DiffUtil.ItemCallback<Coin>() {
        override fun areItemsTheSame(oldItem: Coin, newItem: Coin): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Coin, newItem: Coin): Boolean =
            oldItem == newItem
    }
}