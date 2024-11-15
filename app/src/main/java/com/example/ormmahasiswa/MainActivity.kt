package com.example.ormmahasiswa

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        database = AppDatabase.getInstance(applicationContext)
        recyclerView = findViewById(R.id.recycler_view)
        fab = findViewById(R.id.fab)


        recyclerView.layoutManager = LinearLayoutManager(this)


        fab.setOnClickListener {
            val intent = Intent(this, EditorActivity::class.java)
            startActivity(intent)
        }


        loadDataFromDatabase()
    }

    private fun loadDataFromDatabase() {
        lifecycleScope.launch {
            try {

                val userList = withContext(Dispatchers.IO) {
                    database.UserDao().getAll()
                }

                userAdapter = UserAdapter(userList, { deleteUser(it) }, { editUser(it) })
                recyclerView.adapter = userAdapter
            } catch (e: Exception) {

                Toast.makeText(this@MainActivity, "Gagal memuat data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteUser(user: User) {
        lifecycleScope.launch {
            try {

                withContext(Dispatchers.IO) {
                    database.UserDao().delete(user)
                }

                loadDataFromDatabase()
            } catch (e: Exception) {

                Toast.makeText(this@MainActivity, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun editUser(user: User) {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("user", user)
        startActivity(intent)
    }
}
