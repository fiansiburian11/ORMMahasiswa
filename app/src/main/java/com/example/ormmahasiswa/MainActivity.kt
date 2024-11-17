package com.example.ormmahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ormmahasiswa.adapter.UserAdapter
import com.example.ormmahasiswa.data.AppDatabase
import com.example.ormmahasiswa.data.entity.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var database: AppDatabase
    private lateinit var userAdapter: UserAdapter

    // ActivityResultLauncher untuk menerima hasil dari EditorActivity
    private val editorLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            loadDataFromDatabase() // Muat ulang data setelah EditorActivity selesai
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi komponen
        database = AppDatabase.getInstance(applicationContext)
        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inisialisasi adapter dengan data kosong
        userAdapter = UserAdapter(emptyList(), { deleteUser(it) }, { editUser(it) })
        recyclerView.adapter = userAdapter

        fab.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            editorLauncher.launch(intent) // Membuka EditorActivity untuk menambahkan data baru
        }

        loadDataFromDatabase() // Muat data awal dari database
    }
    override fun onResume() {
        super.onResume()
        loadDataFromDatabase()
    }

    private fun loadDataFromDatabase() {
        lifecycleScope.launch {
            try {
                // Ambil data dari database di thread IO
                val userList = withContext(Dispatchers.IO) {
                    database.UserDao().getAll()
                }
                // Perbarui data di adapter
                userAdapter.updateData(userList)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    database.UserDao().delete(user)
                }
                loadDataFromDatabase() // Muat ulang data setelah penghapusan
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editUser(user: User) {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("user", user) // Mengirim data untuk diedit
        editorLauncher.launch(intent) // Membuka EditorActivity
    }
}
