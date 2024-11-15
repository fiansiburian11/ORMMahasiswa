package com.example.ormmahasiswa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ormmahasiswa.R
import com.example.ormmahasiswa.data.entity.User

class UserAdapter(
    private var list: List<User>,
    private val deleteListener: (User) -> Unit,
    private val editListener: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val deleteListener: (User) -> Unit, private val editListener: (User) -> Unit) : RecyclerView.ViewHolder(view) {
        private val namamahasiswa: TextView = view.findViewById(R.id.nama_mahasiswa)
        private val nim: TextView = view.findViewById(R.id.nim)
        private val alamat: TextView = view.findViewById(R.id.alamat)
        private val asalsekolah: TextView = view.findViewById(R.id.asal_sekolah)
        private val deleteButton: Button = view.findViewById(R.id.btn_delete)
        private val editButton: Button = view.findViewById(R.id.btn_edit)

        fun bind(user: User) {
            namamahasiswa.text = user.namamahasiswa
            nim.text = user.nim
            alamat.text = user.alamat
            asalsekolah.text = user.asalsekolah

            deleteButton.setOnClickListener { deleteListener(user) }
            editButton.setOnClickListener { editListener(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
        return ViewHolder(view, deleteListener, editListener)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    fun updateData(newList: List<User>) {
        list = newList
        notifyDataSetChanged()
    }
}
