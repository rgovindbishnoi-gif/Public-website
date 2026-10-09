package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale

data class ShopItem(
    val id: Int,
    val name: String,
    val price: Long,
    val icon: String,
    val initialCount: Int = 0
)

val initialItems = listOf(
    ShopItem(1, "Big Mac", 5L, "🍔"),
    ShopItem(2, "Cup of Coffee", 4L, "☕"),
    ShopItem(3, "Smartphone", 1000L, "📱"),
    ShopItem(4, "Gaming PC", 2500L, "💻"),
    ShopItem(5, "Rolex", 15000L, "⌚"),
    ShopItem(6, "Sports Car", 250000L, "🏎️"),
    ShopItem(7, "Mansion", 25000000L, "🏰"),
    ShopItem(8, "Private Jet", 50000000L, "✈️"),
    ShopItem(9, "Private Island", 250000000L, "🏝️"),
    ShopItem(10, "F1 Racing Team", 1200000000L, "🏎️"),
    ShopItem(11, "Super Yacht", 900000000L, "🚢"),
    ShopItem(12, "Twitter/X", 44000000000L, "🐦")
)

data class GameState(
    val remainingMoney: Long = 250_400_000_000L,
    val itemCounts: Map<Int, Int> = emptyMap()
)

class GameViewModel : ViewModel() {
    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    fun buyItem(item: ShopItem) {
        _state.update { currentState ->
            if (currentState.remainingMoney >= item.price) {
                val currentCount = currentState.itemCounts[item.id] ?: 0
                currentState.copy(
                    remainingMoney = currentState.remainingMoney - item.price,
                    itemCounts = currentState.itemCounts + (item.id to currentCount + 1)
                )
            } else {
                currentState
            }
        }
    }

    fun sellItem(item: ShopItem) {
        _state.update { currentState ->
            val currentCount = currentState.itemCounts[item.id] ?: 0
            if (currentCount > 0) {
                currentState.copy(
                    remainingMoney = currentState.remainingMoney + item.price,
                    itemCounts = currentState.itemCounts + (item.id to currentCount - 1)
                )
            } else {
                currentState
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true, dynamicColor = false) {
                BillionaireGameScreen()
            }
        }
    }
}

@Composable
fun BillionaireGameScreen(viewModel: GameViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val formatter = remember { NumberFormat.getCurrencyInstance(Locale.US).apply { maximumFractionDigits = 0 } }
    
    val totalMoney = 250_400_000_000L
    val spentMoney = totalMoney - state.remainingMoney
    val spentPercentage = if (totalMoney > 0) (spentMoney.toFloat() / totalMoney.toFloat()) * 100 else 0f
    
    val remainingFormatted = when {
        state.remainingMoney >= 1_000_000_000 -> "$${String.format(Locale.US, "%.1f", state.remainingMoney / 1_000_000_000.0)}B"
        state.remainingMoney >= 1_000_000 -> "$${String.format(Locale.US, "%.1f", state.remainingMoney / 1_000_000.0)}M"
        else -> formatter.format(state.remainingMoney)
    }

    Scaffold(
        bottomBar = { BottomNavBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFF1A1A1A), Color(0xFF111111))
                        )
                    )
                    .border(BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)))
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL NET WORTH",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 10.sp
                    )
                    Text(
                        text = "ELON MUSK",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall,
                        letterSpacing = 2.sp,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = String.format("%,d", state.remainingMoney),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-1).sp,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(percent = 50))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = spentPercentage / 100f)
                            .fillMaxHeight()
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(percent = 50))
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${spentPercentage.toInt()}% SPENT",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "$remainingFormatted REMAINING",
                        color = MaterialTheme.colorScheme.tertiary,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LUXURY CATALOG",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 12.sp
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(50))
                                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Real Estate", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp)
                        }
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(50))
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Vehicles", color = MaterialTheme.colorScheme.tertiary, fontSize = 10.sp)
                        }
                    }
                }
                
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(initialItems) { item ->
                        val count = state.itemCounts[item.id] ?: 0
                        ShopItemCard(
                            item = item,
                            count = count,
                            formatter = formatter,
                            onBuy = { viewModel.buyItem(item) },
                            onSell = { viewModel.sellItem(item) },
                            canBuy = state.remainingMoney >= item.price
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShopItemCard(
    item: ShopItem,
    count: Int,
    formatter: NumberFormat,
    onBuy: () -> Unit,
    onSell: () -> Unit,
    canBuy: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.icon,
                    fontSize = 48.sp
                )
                if (count > 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "x$count",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item.name,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatter.format(item.price),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = onSell,
                    enabled = count > 0,
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = Color.White,
                        disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.5f)
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Sell", fontSize = 10.sp)
                }
                Button(
                    onClick = onBuy,
                    enabled = canBuy,
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.Black,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        disabledContentColor = Color.Black.copy(alpha = 0.5f)
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("Buy", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BottomNavBar() {
    Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF111111))) {
        HorizontalDivider(color = Color.White.copy(alpha = 0.1f))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text("SHOP", color = MaterialTheme.colorScheme.primary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                Text("RANK", color = MaterialTheme.colorScheme.tertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .offset(y = (-16).dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                        .padding(8.dp)
                ) {
                    Icon(Icons.Default.Casino, contentDescription = null, tint = Color.Black)
                }
                Text("LUCKY", color = MaterialTheme.colorScheme.tertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp, modifier = Modifier.offset(y = (-12).dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.QueryStats, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                Text("STATS", color = MaterialTheme.colorScheme.tertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                Text("PROFILE", color = MaterialTheme.colorScheme.tertiary, fontSize = 9.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp)
            }
        }
    }
}
