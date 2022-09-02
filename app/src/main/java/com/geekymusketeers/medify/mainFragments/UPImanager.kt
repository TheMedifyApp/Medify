package com.geekymusketeers.medify.mainFragments

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.geekymusketeers.medify.databinding.ActivityUpimanagerBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.lang.StringBuilder
import java.util.*

class UPImanager : AppCompatActivity() {

    private lateinit var binding: ActivityUpimanagerBinding
    private lateinit var upiString: StringBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpimanagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.generateUPI.setOnClickListener {
            upiString = StringBuilder()
            val upiPayeeName = binding.upiNamePn.text.toString()
            val upiID = binding.upiIDPa.text.toString()
            val upiAmount = binding.upiAmountAm.text.toString()
            val upiTransactionRemark = binding.upiMessageTn.text.toString()

            upiString.append("upi://pay?pa=").append(upiID).append("&pn=").append(upiPayeeName)
                .append("&am=").append(upiAmount).append("&cu=INR&")

            if (upiTransactionRemark.isNotEmpty() && upiTransactionRemark.isNotBlank()) {
                upiString.append(upiTransactionRemark).append("&")
            }

            binding.qrPreview.setImageBitmap(generateQrCode(upiString.toString()))
            binding.qrPreview.isVisible = true
        }

    }

    @Throws(WriterException::class)
    fun generateQrCode(value: String): Bitmap {
        val hintMap = Hashtable<EncodeHintType, ErrorCorrectionLevel>()
        hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val qrCodeWriter = QRCodeWriter()
        val size = 512
        val bitMatrix = qrCodeWriter.encode(value, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val bmp = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565)
        for (x in 0 until width)
            for (y in 0 until width)
                bmp.setPixel(y, x, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)

        return bmp
    }
}