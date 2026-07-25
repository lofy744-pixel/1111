package com.example.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.models.OrderRequest
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object WhatsAppHelper {

    const val ORDER_PHONE_NUMBER = "201147678818"
    const val SUPPORT_PHONE_NUMBER = "201094750888"

    fun sendOrderWhatsApp(context: Context, order: OrderRequest) {
        val notesText = if (order.notes.isBlank()) "لا يوجد" else order.notes
        val message = """
            طلب جديد من NEOVA STORE 🚀
            رقم الطلب: #${order.orderId}
            اسم العميل: ${order.customerName}
            رقم الهاتف: ${order.phone}
            الخدمة: ${order.serviceName}
            الكمية: ${order.quantity}
            الرابط أو الـ ID: ${order.accountIdOrLink}
            الملاحظات: $notesText
            
            شكراً.
        """.trimIndent()

        openWhatsApp(context, ORDER_PHONE_NUMBER, message)
    }

    fun openSupportWhatsApp(context: Context) {
        val message = "مرحباً، أواجه مشكلة في استخدام تطبيق NEOVA STORE وأحتاج إلى المساعدة."
        openWhatsApp(context, SUPPORT_PHONE_NUMBER, message)
    }

    fun openWhatsApp(context: Context, phoneNumber: String, message: String) {
        try {
            val encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$encodedMessage")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "تعذر فتح تطبيق واتساب. يرجى التأكد من تثبيته.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
