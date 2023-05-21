package com.geekymusketeers.medify.ui.mainFragments.settings.prescription

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.geekymusketeers.medify.databinding.ActivityAddPrescriptionBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener

class AddPrescriptionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPrescriptionBinding
    private val pdf: Int = 0
    lateinit var fileUri: Uri
    var databaseReference: DatabaseReference? = null
    private lateinit var sharedPreference : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPrescriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreference = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        binding.cancelfile.setOnClickListener {
            binding.filetitle.text.clear()
            binding.filelogo.visibility = View.INVISIBLE
            binding.cancelfile.visibility = View.INVISIBLE
            binding.imagebrowse.visibility = View.VISIBLE
        }


        //mStorage = FirebaseStorage.getInstance().reference
        databaseReference = FirebaseDatabase.getInstance().reference


        //Browse PDF from the file manager
        binding.imagebrowse.setOnClickListener {
            Dexter.withContext(applicationContext)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse) {
                        val intent = Intent()
                        intent.type = "application/pdf"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(
                            Intent.createChooser(intent, "Select Pdf Files"),
                            101
                        )
                    }

                    override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse) {}
                    override fun onPermissionRationaleShouldBeShown(
                        permissionRequest: PermissionRequest,
                        permissionToken: PermissionToken
                    ) {
                        permissionToken.continuePermissionRequest()
                    }
                }).check()
        }

        // Upload pdf
        binding.imageupload.setOnClickListener {
            process_upload(fileUri)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            assert(data != null)
            fileUri = data?.data!!
            binding.filelogo.visibility = View.VISIBLE
            binding.cancelfile.visibility = View.VISIBLE
            binding.imagebrowse.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun process_upload(fileUri: Uri?) {
        val userid = sharedPreference.getString("uid","").toString()
        if (fileUri == null) {
            Toast.makeText(baseContext, "Select a file first", Toast.LENGTH_SHORT).show()
        } else if (binding.filetitle.text.toString().isEmpty()) {
            Toast.makeText(baseContext, "Add file title", Toast.LENGTH_SHORT).show()
        }else{
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading PDF")
            pd.show()

            val reference: StorageReference = FirebaseStorage.getInstance().reference.child("uploads/" + System.currentTimeMillis() + ".pdf")
            reference.putFile(fileUri).addOnSuccessListener {
                reference.downloadUrl.addOnSuccessListener { uri: Uri ->
                        val editor = sharedPreference.edit()
                        editor.putString("prescription", uri.toString())
                        editor.apply()
                        databaseReference?.child("Users")?.child(userid)?.child("prescription")?.setValue(uri.toString())
                        pd.dismiss()
                        Toast.makeText(baseContext, "File Uploaded", Toast.LENGTH_SHORT).show()

                        binding.filelogo.visibility = View.INVISIBLE
                        binding.cancelfile.visibility = View.INVISIBLE
                        binding.imagebrowse.visibility = View.VISIBLE
                        binding.filetitle.setText("")
                    }
                }
                .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                    val percent =
                        (100 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount).toFloat()
                    pd.setMessage("Uploaded :" + percent.toInt() + "%")
                }

        }
    }
}

