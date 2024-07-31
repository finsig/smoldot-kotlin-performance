package io.finsig.smoldotkotlinperformance

import ChainSpecificationAsset
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request
import io.finsig.smoldotkotlin.Chain
import io.finsig.smoldotkotlin.ChainSpecification
import io.finsig.smoldotkotlin.Client
import io.finsig.smoldotkotlin.readFromAsset
import io.finsig.smoldotkotlinperformance.ui.theme.SmoldotkotlinperformanceTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmoldotkotlinperformanceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_memory_profiler),
                            contentDescription = null
                        )
                        Text(
                            text = "View Android Studio Memory Profiler...",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    //Baseline()
                    //AddSingleChain()
                    AddChainRemoveChain()
                    //Subscription()
                }
            }
        }
    }
}

@Composable
fun Baseline() {
    // do nothing
}

@Composable
fun AddSingleChain() {
    val specification = ChainSpecification()
        .readFromAsset(ChainSpecificationAsset.POLKADOT.fileName, LocalContext.current)
    val chain = Chain(specification)
    Client.instance().add(chain)
}

@Composable
fun AddChainRemoveChain() {
    val specification = ChainSpecification()
        .readFromAsset(ChainSpecificationAsset.POLKADOT.fileName, LocalContext.current)
    val chain = Chain(specification)

    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            Log.d("smoldotkotlinperformance", "add chain")
            Client.instance().add(chain)
            Log.d("smoldotkotlinperformance", "sleep 10_000...")
            Thread.sleep(10_000)
            Log.d("smoldotkotlinperformance", "remove chain")
            Client.instance().remove(chain)
        }
    }
}

@Composable
fun Subscription() {
    val specification = ChainSpecification()
        .readFromAsset(ChainSpecificationAsset.POLKADOT.fileName, LocalContext.current)
    val chain = Chain(specification)

    val client = Client.instance()
    client.add(chain)

    val request = JSONRPC2Request.parse("{\"id\":1,\"jsonrpc\":\"2.0\",\"method\":\"chain_subscribeNewHeads\",\"params\":[]}")
    client.send(request,chain)

    val scope = rememberCoroutineScope()
    LaunchedEffect(true) {
        scope.launch(Dispatchers.IO) {
            client.responses(chain).collect { response ->
                Log.d("smoldotkotlinperformance", response)
            }
        }
    }
}