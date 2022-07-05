package com.example.unitconverterapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {

    // region variables of unit type dropdown list
    private val length = "length"
    private val time = "time"
    private val weight = "weight"
    private val power = "power"
    // endregion

    // region variables of length units
    private val millimeter = "mm"
    private val centimeter = "cm"
    private val meter = "m"
    private val kilometer = "km"
    private val mile = "mi"
    private val foot = "ft"
    private val inch = "in"
    private val lengthUnits = mapOf(
        mile to 0.6214,
        kilometer to 1.0,
        meter to 1000.0,
        foot to 3280.8399,
        inch to 39370.0787,
        centimeter to 100000.0,
        millimeter to 1000000.0
    )
    // endregion

    // region variables of time units
    private val millisecond = "ms"
    private val second = "s"
    private val minute = "min"
    private val hour = "hr"
    private val day = "day"
    private val month = "month"
    private val year = "year"
    private val timeUnits = mapOf(
        year to 1.0,
        month to 12.0,
        day to 365.0,
        hour to 8760.0,
        minute to 525600.0,
        second to 31536000.0,
        millisecond to 31536000000.0
    )
    // endregion

    // region variables of weight units
    private val gram = "g"
    private val kilogram = "kg"
    private val ton = "t"
    private val pound = "Ib"
    private val weightUnits = mapOf(
        ton to 1.0,
        kilogram to 1000.0,
        pound to 2204.6626,
        gram to 1000000.0
    )
    // endregion

    // region variables of power units
    private val watt = "W"
    private val kilowatt = "kW"
    private val horsepower = "hp"
    private val powerUnits = mapOf(
        kilowatt to 1.0,
        horsepower to 1.3410221,
        watt to 1000.0
    )
    // endregion

    // region variables of views
    private lateinit var unitTypeDropDownList: AutoCompleteTextView
    private lateinit var inputDropDownList: AutoCompleteTextView
    private lateinit var outputDropDownList: AutoCompleteTextView
    private lateinit var finalResult: TextView
    private lateinit var input: EditText
    private lateinit var clear: ImageView
    private lateinit var reverseButton: ImageView
    // endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        populateUnitTypeDropDownMenu()
        populateLengthDropDownMenu()

        unitTypeDropDownList.setOnItemClickListener { _, _, _, _ ->
            when (unitTypeDropDownList.text.toString()) {
                length -> {
                    inputDropDownList.setText(meter)
                    outputDropDownList.setText(meter)
                }
                time -> {
                    inputDropDownList.setText(second)
                    outputDropDownList.setText(second)
                }
                weight -> {
                    inputDropDownList.setText(gram)
                    outputDropDownList.setText(gram)
                }
                power -> {
                    inputDropDownList.setText(watt)
                    outputDropDownList.setText(watt)
                }
            }
            populateSelectedUnit()
        }

        input.addTextChangedListener {
            calculateSelectedUnit()
        }

        inputDropDownList.setOnItemClickListener { _, _, _, _ ->
            calculateSelectedUnit()
        }

        outputDropDownList.setOnItemClickListener { _, _, _, _ ->
            calculateSelectedUnit()
        }

        reverseButton.setOnClickListener {
            if (input.text.toString().isNotEmpty()) {
                inputDropDownList.text =
                    outputDropDownList.text.also {
                        outputDropDownList.text = inputDropDownList.text
                    }
                input.setText(finalResult.text).also {
                    finalResult.text = input.text
                }
                populateSelectedUnit()
                calculateSelectedUnit()
            } else input.error = "Enter the numbers first."
        }

        clear.setOnClickListener {
            input.setText("")
            finalResult.text = "0.0"
        }

    }

    // region initialization
    private fun initViews() {
        input = findViewById(R.id.editText)
        finalResult = findViewById(R.id.textView)
        inputDropDownList = findViewById(R.id.from_drop)
        outputDropDownList = findViewById(R.id.to_drop)
        unitTypeDropDownList = findViewById(R.id.unit_type_drop)
        clear = findViewById(R.id.clear)
        reverseButton = findViewById(R.id.opposite)
    }
    // endregion


    // region populate
    private fun populateUnitTypeDropDownMenu() {
        val listOfUnitType = listOf(length, time, weight, power)
        val adapter = ArrayAdapter(this, R.layout.drop_down_list_item, listOfUnitType)
        unitTypeDropDownList.setAdapter(adapter)
    }

    private fun populateLengthDropDownMenu() {
        val listOfLength = listOf(millimeter, centimeter, meter, kilometer, inch, foot, mile)
        val adapter = ArrayAdapter(this, R.layout.drop_down_list_item, listOfLength)
        outputDropDownList.setAdapter(adapter)
        inputDropDownList.setAdapter(adapter)
    }

    private fun populateTimeDropDownMenu() {
        val listOfTime = listOf(millisecond, second, minute, hour, day, month, year)
        val adapter = ArrayAdapter(this, R.layout.drop_down_list_item, listOfTime)
        outputDropDownList.setAdapter(adapter)
        inputDropDownList.setAdapter(adapter)
    }

    private fun populateWeightDropDownMenu() {
        val listOfWeight = listOf(gram, kilogram, ton, pound)
        val adapter = ArrayAdapter(this, R.layout.drop_down_list_item, listOfWeight)
        outputDropDownList.setAdapter(adapter)
        inputDropDownList.setAdapter(adapter)
    }

    private fun populatePowerDropDownMenu() {
        val listOfWeight = listOf(watt, kilowatt, horsepower)
        val adapter = ArrayAdapter(this, R.layout.drop_down_list_item, listOfWeight)
        outputDropDownList.setAdapter(adapter)
        inputDropDownList.setAdapter(adapter)
    }

    private fun populateSelectedUnit() {
        when (unitTypeDropDownList.text.toString()) {
            length -> populateLengthDropDownMenu()
            time -> populateTimeDropDownMenu()
            weight -> populateWeightDropDownMenu()
            power -> populatePowerDropDownMenu()
        }
    }
    // endregion


    // region calculate
    private fun calculateLengthUnit() {
        if (input.text.toString().isNotEmpty()) {
            val amount = input.text.toString().toDouble()
            val fromValue = lengthUnits[inputDropDownList.text.toString()]
            val toValue = lengthUnits[outputDropDownList.text.toString()]
            val result = amount.times(toValue!!).div(fromValue!!)
            val formattedResult = String.format("%.2f", result)
            finalResult.text = formattedResult
        }
    }

    private fun calculateTimeUnit() {
        if (input.text.toString().isNotEmpty()) {
            val amount = input.text.toString().toDouble()
            val fromValue = timeUnits[inputDropDownList.text.toString()]
            val toValue = timeUnits[outputDropDownList.text.toString()]
            val result = amount.times(toValue!!).div(fromValue!!)
            val formattedResult = String.format("%.2f", result)
            finalResult.text = formattedResult
        }
    }

    private fun calculateWeightUnit() {
        if (input.text.toString().isNotEmpty()) {
            val amount = input.text.toString().toDouble()
            val fromValue = weightUnits[inputDropDownList.text.toString()]
            val toValue = weightUnits[outputDropDownList.text.toString()]
            val result = amount.times(toValue!!).div(fromValue!!)
            val formattedResult = String.format("%.2f", result)
            finalResult.text = formattedResult
        }
    }

    private fun calculatePowerUnit() {
        if (input.text.toString().isNotEmpty()) {
            val amount = input.text.toString().toDouble()
            val fromValue = powerUnits[inputDropDownList.text.toString()]
            val toValue = powerUnits[outputDropDownList.text.toString()]
            val result = amount.times(toValue!!).div(fromValue!!)
            val formattedResult = String.format("%.2f", result)
            finalResult.text = formattedResult
        }
    }

    private fun calculateSelectedUnit() {
        when (unitTypeDropDownList.text.toString()) {
            length -> calculateLengthUnit()
            time -> calculateTimeUnit()
            weight -> calculateWeightUnit()
            power -> calculatePowerUnit()
        }
    }
    // endregion


}