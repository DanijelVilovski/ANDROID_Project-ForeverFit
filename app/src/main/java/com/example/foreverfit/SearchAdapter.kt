package com.example.foreverfit

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foreverfit.data.Food
import kotlinx.android.synthetic.main.rv_search.view.*

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    private var arrSearch: MutableList<Food> = mutableListOf<Food>()
    var listener: OnItemClickListener? = null

    class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_search, parent, false))
    }

    override fun getItemCount(): Int {
        return arrSearch.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        var current = arrSearch[position]
        holder.itemView.tvFoodName.text = current.title
        Glide.with(holder.itemView.context).load(current.image)
            .centerCrop().into(holder.itemView.imageView)

        var pr: Double = 0.0
        var ch: Double = 0.0
        var f: Double = 0.0
        var kcal: Double = 0.0

        for(i in current.nutrition.nutrients) {
            if(i.name == "Calories") {
                holder.itemView.tvFoodCalories.text =
                    i.amount.toString() + "kcal"
                kcal = i.amount.toString().toDouble()
            } else if (i.name == "Protein") {
                holder.itemView.tvFoodProteins.text =
                    i.amount.toString() + "g"
                pr = i.amount.toString().toDouble()
            } else if (i.name == "Carbohydrates") {
                holder.itemView.tvFoodCarbs.text =
                    i.amount.toString() + "g"
                ch = i.amount.toString().toDouble()
            } else if (i.name == "Fat") {
                holder.itemView.tvFoodFats.text =
                   i.amount.toString() + "g"
                f = i.amount.toString().toDouble()
            }
        }
        holder.itemView.tvFoodQuantity.text =
            current.servings.size.toString() + current.servings.unit.toString()
        //Log.d("ress1", current.toString())

        holder.itemView.btnHomeApply.setOnClickListener {
            var quantity = holder.itemView.etQuantity.text.toString().toDouble()
            listener!!.onClicked(current, quantity, kcal, pr, ch, f)

        }
    }


    interface OnItemClickListener {
        fun onClicked(food: Food, quantity: Double, kcal: Double, pr: Double, ch: Double, f: Double)
    }

    fun setClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    fun setData(arrData : List<Food>) {
        //Log.d("arrdata", arrData.toString())
        for(i in arrData) {
            if(i.servings.unit != null) {
                if (i.servings.unit == "g" || i.servings.unit == "G") {
                    arrSearch.add(i)
                    //Log.d("foodd", i.toString())
                    //Log.d("fsize", arrSearch.size.toString())
                }
            }
        }
        //Log.d("ress1", arrSearch.toString())
        notifyDataSetChanged()
    }

    fun clear() {
        arrSearch.clear()
    }
}