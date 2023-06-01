package com.geekymusketeers.medify.ui.mainFragments.settings.upi

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.view.isVisible
import com.geekymusketeers.medify.databinding.ActivityUpimanagerBinding
import com.geekymusketeers.medify.utils.Encryption
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
    private lateinit var spUPI: String
    private lateinit var sharedPreference : SharedPreferences
    private lateinit var userID: String
    val encryption: Encryption = Encryption.getDefault("Key", "Salt", ByteArray(16))

    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpimanagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        getDataFromSharedPreference()

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
            val editor = sharedPreference.edit()
            editor.remove("upi")
            editor.putString("upi", encryption.encrypt(upiString.toString()))
            editor.apply()
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
    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            getDataFromSharedPreference()
        }, 1000)
    }

    @SuppressLint("SetTextI18n", "CommitPrefEdits")
    private fun getDataFromSharedPreference() {
        userID = sharedPreference.getString("uid","Not found").toString()
        spUPI = sharedPreference.getString("upi", encryption.decryptOrNull("null")).toString()

        if (spUPI != "null") {
            binding.qrPreview.setImageBitmap(generateQrCode(encryption.decryptOrNull(spUPI)))
            binding.qrPreview.isVisible = true
        }
    }
}