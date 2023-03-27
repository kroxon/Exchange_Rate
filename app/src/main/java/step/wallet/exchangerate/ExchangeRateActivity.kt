package step.wallet.exchangerate

import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONException
import org.json.JSONObject
import step.wallet.exchangerate.Model.Currency
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class ExchangeRateActivity : AppCompatActivity() {

    var currencySpinnerFrom: Spinner? = null
    var currencySpinnerTo: Spinner? = null
    var currenciesList: ArrayList<Currency> = ArrayList()
    var currencyFrom: Currency? = null
    var currencyTo: Currency? = null
    var valueFrom: Double = 0.0
    var valueTo: Double = 0.0
    var etCurrencyFrom: TextInputEditText? = null
    var etCurrencyTo: TextInputEditText? = null
    var rvCurrencies: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_echange_rate)

        rvCurrencies = findViewById(R.id.rvCurrenciesList)
        currencySpinnerFrom = findViewById(R.id.spinnerCurrencyFrom)
        currencySpinnerTo = findViewById(R.id.spinnerCurrencyTo)
        etCurrencyFrom = findViewById(R.id.et_Firstconversion)
        etCurrencyTo = findViewById(R.id.currencyToTextInputEditText)
        JsonTask().execute("http://www.floatrates.com/daily/usd.json")
    }

    inner class JsonTask : AsyncTask<String?, String?, String?>() {

        override fun onPreExecute() {
            super.onPreExecute()
//            pd?.setMessage("Please wait")
//            pd?.setCancelable(false)
//            pd?.show()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObject = JSONObject(result)
                val currenciesNames = jsonObject.names()
                val keys = jsonObject.keys()

                val currencyUSD = Currency(
                    "USD",
                    "USD",
                    "840",
                    "United States dollar",
                    1.0,
                    jsonObject.getJSONObject(currenciesNames.get(0) as String).getString("date"),
                    1.0
                )
                currenciesList.add(currencyUSD)

                for (i in 0..(jsonObject.length() - 1)) {
                    val code =
                        jsonObject.getJSONObject(currenciesNames.get(i) as String).getString("code")
                    val alphaCode =
                        jsonObject.getJSONObject(currenciesNames.get(i) as String)
                            .getString("alphaCode")
                    val numericCode =
                        jsonObject.getJSONObject(currenciesNames.get(i) as String)
                            .getString("numericCode")
                    val name =
                        jsonObject.getJSONObject(currenciesNames.get(i) as String).getString("name")
                    val rate =
                        jsonObject.getJSONObject(currenciesNames.get(i) as String).getDouble("rate")
                    val date =
                        jsonObject.getJSONObject(currenciesNames.get(i) as String).getString("date")
                    val inverseRate =
                        jsonObject.getJSONObject(currenciesNames.get(i) as String)
                            .getDouble("inverseRate")
                    val currencyDetails =
                        Currency(code, alphaCode, numericCode, name, rate, date, inverseRate)
                    currenciesList.add(currencyDetails)
                }

//                val llm = LinearLayoutManager(this@ExchangeRateActivity)
//                llm.orientation = LinearLayoutManager.VERTICAL
//                list.setLayoutManager(llm)
//                list.setAdapter(adapter)


                // Set the LayoutManager that this RecyclerView will use.
                rvCurrencies?.layoutManager = LinearLayoutManager(this@ExchangeRateActivity)
                // Adapter class is initialized and list is passed in the param.
                val itemAdapter = CurrencyAdapter(this@ExchangeRateActivity, currenciesList)
                // adapter instance is set to the recyclerview to inflate the items.
                rvCurrencies?.adapter = itemAdapter

                etCurrencyFrom?.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {}

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                        if (s.length == 0 && etCurrencyFrom?.hasFocus() == true) {
                            etCurrencyTo?.setText("")
                        } else
                            if (etCurrencyFrom?.hasFocus() == true) {
                                valueFrom = s.toString().toDouble()
                                val value = valueFrom * currencyTo!!.rate / currencyFrom!!.rate
                                etCurrencyTo?.setText(
                                    String.format("%.3f", value).toDouble().toString()
                                )
                            }
                    }
                })

                etCurrencyTo?.addTextChangedListener(object : TextWatcher {

                    override fun afterTextChanged(s: Editable) {}

                    override fun beforeTextChanged(
                        s: CharSequence, start: Int,
                        count: Int, after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence, start: Int,
                        before: Int, count: Int
                    ) {
                        if (s.length == 0 && etCurrencyTo?.hasFocus() == true) {
                            etCurrencyFrom?.setText("")
                        } else
                            if (etCurrencyTo?.hasFocus() == true) {
                                valueTo = s.toString().toDouble()
                                val value = valueTo * currencyFrom!!.rate / currencyTo!!.rate
                                etCurrencyFrom?.setText(
                                    String.format("%.3f", value).toDouble().toString()
                                )
                            }
                    }
                })


                loadSpinners()

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        override fun doInBackground(vararg p0: String?): String? {
            var connection: HttpURLConnection? = null
            var reader: BufferedReader? = null
            try {
                val url = URL(p0[0])
                connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val stream: InputStream = connection.getInputStream()
                reader = BufferedReader(InputStreamReader(stream))
                val buffer = StringBuffer()
                buffer.append(reader.readLine())
                return buffer.toString()
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
                try {
                    if (reader != null) {
                        reader.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return null
        }
    }

    private fun loadSpinners() {
        var currencyCodes: ArrayList<String> = ArrayList()
        for (item in currenciesList) {
            currencyCodes.add(item.code)
        }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, currencyCodes
        )
        currencySpinnerFrom?.adapter = adapter
        currencySpinnerTo?.adapter = adapter
        currencySpinnerTo?.setSelection(1)

        currencyFrom = currenciesList.get(0)
        currencyTo = currenciesList.get(1)

        currencySpinnerFrom?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                currencyFrom = currenciesList.get(p2)
                convertCurrency()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
//        currencySpinnerTo!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                currencyTo = currenciesList.get(p2)
////                convertCurrency("from")
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//        }
    }

    private fun convertCurrency() {
            if (!etCurrencyFrom?.text?.toString().equals(""))
                etCurrencyTo?.setText(valueFrom.toString())
    }

}