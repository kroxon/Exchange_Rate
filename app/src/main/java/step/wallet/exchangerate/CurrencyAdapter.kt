package step.wallet.exchangerate

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import step.wallet.exchangerate.Model.Currency

class CurrencyAdapter(val context: Context, val items: ArrayList<Currency>) :
    RecyclerView.Adapter<CurrencyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = items.get(position)

        holder.tvCode.setText(item.code)
        holder.tvAlphaCode.setText(item.alphacode)
        holder.tvNumeric.setText(item.numericCode)
        holder.tvName.setText(item.name)
        holder.tvRate.setText(item.rate.toString())
        holder.tvDate.setText(item.date)
        holder.tvInverseRate.setText(item.inverseRate.toString())

    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val tvCode: TextView = view.findViewById(R.id.tv_code)
        val tvAlphaCode: TextView = view.findViewById(R.id.tv_alphaCode)
        val tvNumeric: TextView = view.findViewById(R.id.tv_Numeric)
        val tvName: TextView = view.findViewById(R.id.tv_Name)
        val tvRate: TextView = view.findViewById(R.id.tv_Rate)
        val tvDate: TextView = view.findViewById(R.id.tv_Date)
        val tvInverseRate: TextView = view.findViewById(R.id.tv_InverseRate)

    }
}