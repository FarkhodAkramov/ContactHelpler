package com.example.contacthelpler

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.contacthelpler.adapter.ContactAdapter
import com.example.contacthelpler.databinding.ActivityMainBinding
import com.example.contacthelpler.models.Contact
import com.github.florent37.runtimepermission.kotlin.askPermission


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var contactList: ArrayList<Contact>? = null
    lateinit var contactAdapter: ContactAdapter
    private val REQUEST_READ_CONTACTS = 79

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getPermission()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.whitee, resources.newTheme()))
        }

    }

    override fun onStart() {
        super.onStart()
        getPermission()
    }

    private fun getPermission() {
        askPermission(
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.SEND_SMS,
            android.Manifest.permission.CALL_PHONE
        ) {

            loadData()

        }.onDeclined {


            if (it.hasDenied()) {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("Ushbu ilovadan foydalanish uchun quyidagi so\'rovga ruxsat berishingiz kerak!")
                alertDialog.setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, which ->
                        it.askAgain()
                    })
                alertDialog.show()
            }

            if (it.hasForeverDenied()){
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setMessage("Ushbu ilovadan foydalanish uchun sozlamalardagi imkoniyatlarni yoqishingiz yoki ilovani qaytadan o\'rnatib olishingiz kerak!")
                alertDialog.setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, which ->
                        it.goToSettings()
                    })

                alertDialog.show()
            }

        }
    }

    fun loadData(){
        if (contactList==null){
            contactList = getAllContact()
        }
            contactAdapter = ContactAdapter(
                contactList!!,
                object : ContactAdapter.OnItemClickListener {
                    override fun onItemCallClick(number: String) {
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.setData(Uri.parse("tel:$number"))
                        startActivity(intent)
                    }

                    override fun onItemSendClick(contact: Contact) {
                        val intent = Intent(this@MainActivity, SecondActivity::class.java)
                        intent.putExtra("contact", contact)
                        startActivity(intent)
                    }

                })
            binding.rv.adapter = contactAdapter


    }



    fun getAllContact(): ArrayList<Contact> {
        val arrayList = ArrayList<Contact>()
        val contentResolver = contentResolver
        val cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if ((if (cur != null) cur.count else 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id = cur.getString(
                    cur.getColumnIndex(ContactsContract.Contacts._ID)
                )
                val name = cur.getString(
                    cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id), null
                    )
                    while (pCur!!.moveToNext()) {
                        val phoneNo = pCur.getString(
                            pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        arrayList.add(Contact(name, phoneNo))
                    }
                    pCur.close()
                }

            }
            if (cur != null) cur.close()

        }
        return arrayList
    }


}