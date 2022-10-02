package com.example.sbma_graph

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.sbma_graph.ui.theme.SBMA_GraphTheme
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    private val vals = mutableListOf<Entry>()
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineData: MutableState<LineData>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vals.add(Entry(1f, 1f))
        vals.add(Entry(2f, 2f))
        vals.add(Entry(3f, 3f))

        lineDataSet = LineDataSet(vals, "My Label")

        lineData = mutableStateOf(LineData(lineDataSet))

        val repeatAddData = addRandomData()


        setContent {
            SBMA_GraphTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    PlotChart()
                }
            }
        }
    }

    @Composable
    fun PlotChart() {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context: Context ->
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.graph, null)
                val graph = view.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.graph)
                graph.data = lineData.value
                view
            },
            update = { view ->
                Log.d("pengb", "UPDATED")
                view.invalidate()
            }
        )
    }

    private fun addRandomData(): Job {
        return GlobalScope.launch(Dispatchers.IO){
            while(isActive){
                vals.add(Entry(4f, 4f))
                lineDataSet.notifyDataSetChanged()
                lineData.value.notifyDataChanged()
                Log.d("pengb", lineData.value.entryCount.toString())
                delay(1000)
            }
        }
    }
}