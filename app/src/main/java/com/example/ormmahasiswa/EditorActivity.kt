package com.example.ormmahasiswa

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ormmahasiswa.data.AppDatabase
import com.example.ormmahasiswa.data.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditorActivity : AppCompatActivity() {

    private lateinit var namamahasiswa: EditText
    private lateinit var nim: EditText
    private lateinit var alamat: EditText
    private lateinit var asalsekolah: EditText
    private lateinit var btnsubmit: Button
    private lateinit var database: AppDatabase
    private var userId: Long? = null // Untuk edit data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)


        namamahasiswa = findViewById(R.id.nama_mahasiswa)
        nim = findViewById(R.id.nim)
        alamat = findViewById(R.id.alamat)
        asalsekolah = findViewById(R.id.asal_sekolah)
        btnsubmit = findViewById(R.id.btn_submit)


        database = AppDatabase.getInstance(applicationContext)


        val user = intent.getSerializableExtra("user") as? User
        user?.let {

            namamahasiswa.setText(it.namamahasiswa)
            nim.setText(it.nim)
            alamat.setText(it.alamat)
            asalsekolah.setText(it.asalsekolah)
            userId = it.uid
        }

        btnsubmit.setOnClickListener {

            val nama = namamahasiswa.text.toString()
            val nimText = nim.text.toString()
            val alamatText = alamat.text.toString()
            val asalSekolahText = asalsekolah.text.toString()

            if (nama.isNotEmpty() && nimText.isNotEmpty() && alamatText.isNotEmpty() && asalSekolahText.isNotEmpty()) {

                if (userId != null) {

                    updateUserData(nama, nimText, alamatText, asalSekolahText)
                } else {

                    saveUserToDatabase(nama, nimText, alamatText, asalSekolahText)
                }
            } else {
                Toast.makeText(this, "Silahkan isi semua data dengan benar", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveUserToDatabase(nama: String, nim: String, alamat: String, asalSekolah: String) {
        lifecycleScope.launch {
            try {

                withContext(Dispatchers.IO) {
                    database.UserDao().insertAll(
                        User(
                            namamahasiswa = nama,
                            nim = nim,
                            alamat = alamat,
                            asalsekolah = asalSekolah
                        )
                    )
                }

                Toast.makeText(applicationContext, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {

                Toast.makeText(applicationContext, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserData(nama: String, nim: String, alamat: String, asalSekolah: String) {
        lifecycleScope.launch {
            try {

                withContext(Dispatchers.IO) {
                    val user = User(
                        uid = userId,
                        namamahasiswa = nama,
                        nim = nim,
                        alamat = alamat,
                        asalsekolah = asalSekolah
                    )
                    database.UserDao().update(user)
                }

                Toast.makeText(applicationContext, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show()
                finish()
            } catch (e: Exception) {

                Toast.makeText(applicationContext, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
