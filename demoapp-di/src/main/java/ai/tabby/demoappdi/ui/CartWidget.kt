package ai.tabby.demoappdi.ui

import ai.tabby.android.data.Order
import ai.tabby.android.data.OrderItem
import ai.tabby.android.data.TabbyPayment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CartWidget(tabbyPayment: TabbyPayment) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Cart:", fontStyle = FontStyle.Italic, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(18.dp))
        OrderWidget(order = tabbyPayment.order)
        Spacer(modifier = Modifier.height(18.dp))
        Text(
            text = "Total: ${tabbyPayment.amount} ${tabbyPayment.currency}",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OrderWidget(order: Order?) {
    order?.items?.forEach { OrderItemWidget(orderItem = it) }
        ?: Text("Cart is empty")
}

@Composable
fun OrderItemWidget(orderItem: OrderItem) {
    Text(
        text = "Item: ${orderItem.title}  q: ${orderItem.quantity}  price: ${orderItem.unitPrice}",
        fontSize = 16.sp
    )
}
