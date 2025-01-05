package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculator.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var openBracketCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    fun onButtonClick(view: View) {
        val button = view as Button
        val enterValue = button.text.toString()
        when (enterValue) {
            "=" -> {
                calculate()
                return
            }

            "AC" -> {
                allClear()
                return
            }

            "" -> {
                removeLastExpression()
                return
            }
        }
        showEnterExpression(enterValue)
    }

    private fun calculate() {
        var result: Double = 0.0
        val currentExpression = binding?.input?.text.toString()
        val numberRegex = Regex("\\d+(\\.\\d+)?") // Matches integers or decimals
        val operatorRegex = Regex("[+\\-x/]") // Matches +, -, x, or /

        // Extract numbers and operators
        val numbers = numberRegex.findAll(currentExpression).map { it.value.toDouble() }.toList()
        val operators = operatorRegex.findAll(currentExpression).map { it.value }.toList()

        if (numbers.isEmpty() || numbers.size - 1 != operators.size) {
            binding?.input?.text = "Error"
            return
        }

        // Initialize result with the first number
        result = numbers[0]

        // Loop through operators and calculate the result
        for (i in operators.indices) {
            val operator = operators[i]
            val nextNumber = numbers[i + 1]

            when (operator) {
                "+" -> result += nextNumber
                "-" -> result -= nextNumber
                "x", "*" -> result *= nextNumber
                "/" -> {
                    if (nextNumber == 0.0) {
                        binding?.input?.text = "Error: Division by zero"
                        return
                    }
                    result /= nextNumber
                }
            }
        }

        // Display the result
        binding?.input?.text = result.toString()
    }

    private fun addBracket() {
        binding?.input?.apply {
            val currentText = text.toString()

            if (openBracketCount > 0 && currentText.isNotEmpty() && currentText.last().isDigit()) {
                text = "$currentText)"
                openBracketCount--
            } else {
                text = "($currentText"
                openBracketCount++
            }
        }
    }

    private fun showEnterExpression(enterValue: String) {
        if (enterValue == "( )") {
            addBracket()
            return
        }
        val currentText = binding?.input?.text.toString()
        binding?.input?.text = "${currentText}${enterValue}"
    }

    private fun allClear() {
        binding?.input?.text = ""
    }

    private fun removeLastExpression() {
        binding?.input?.apply {
            val newString = text.toString().dropLast(1)
            text = newString
        }
    }
}