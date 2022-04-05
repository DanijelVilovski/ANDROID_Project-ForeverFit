package com.example.recipeapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foreverfit.R
import com.example.foreverfit.data.Food
import com.example.foreverfit.data.SavedFood
import kotlinx.android.synthetic.main.rv_food.view.*
import kotlinx.android.synthetic.main.rv_food.view.tvFoodCalories
import kotlinx.android.synthetic.main.rv_food.view.tvFoodCarbs
import kotlinx.android.synthetic.main.rv_food.view.tvFoodFats
import kotlinx.android.synthetic.main.rv_food.view.tvFoodName
import kotlinx.android.synthetic.main.rv_food.view.tvFoodProteins
import kotlinx.android.synthetic.main.rv_food.view.tvFoodQuantity
import kotlinx.android.synthetic.main.rv_search.view.*

class FoodAdapter: RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    var arrFood: MutableList<SavedFood> = mutableListOf<SavedFood>()
    var listener: FoodAdapter.OnItemClickListener? = null

    class FoodViewHolder(view: View): RecyclerView.ViewHolder(view) {

    }

    fun addData(food : SavedFood) {
        Log.d("food1", food.toString())
        arrFood.add(food)
        Log.d("food2", arrFood.size.toString())
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        return FoodViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rv_food, parent, false))
    }

    override fun getItemCount(): Int {
        return arrFood.size
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.itemView.tvFoodName.text = arrFood[position].title
        holder.itemView.tvFoodQuantity.text = arrFood[position].servings.size.toString()
        holder.itemView.tvFoodProteins.text = arrFood[position].nutrition.nutrients[1].amount.toString()
        holder.itemView.tvFoodCarbs.text = arrFood[position].nutrition.nutrients[2].amount.toString()
        holder.itemView.tvFoodFats.text = arrFood[position].nutrition.nutrients[3].amount.toString()
        holder.itemView.tvFoodCalories.text = arrFood[position].nutrition.nutrients[0].amount.toString()

        holder.itemView.btnDelete.setOnClickListener {
            listener!!.onClicked(arrFood[position])

        }
    }

    interface OnItemClickListener {
        fun onClicked(food: SavedFood)
    }

    fun setClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    fun clear() {
        arrFood.clear()
    }

}