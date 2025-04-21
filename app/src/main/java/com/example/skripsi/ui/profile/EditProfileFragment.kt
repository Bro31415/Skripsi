//package com.example.skripsi.ui
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import com.example.skripsi.R
//import com.example.skripsi.data.repository.UserRepository
//import com.example.skripsi.viewmodel.AuthViewModel
//import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
//
//class EditUsernameFragment : Fragment() {
//
//    private lateinit var authViewModel: AuthViewModel
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
//
//        val etNewUsername = view.findViewById<EditText>(R.id.et_new_username)
//        val btnSaveUsername = view.findViewById<Button>(R.id.btn_save_username)
//        val btnBack = view.findViewById<Button>(R.id.btn_back)
//
//        val userRepository = UserRepository()
//        authViewModel = ViewModelProvider(this, AuthViewModelFactory(userRepository)).get(AuthViewModel::class.java)
//
//        // Ambil username lama dari arguments
//        val oldUsername = arguments?.getString("oldUsername")
//        if (oldUsername != null) {
//            etNewUsername.setText(oldUsername) // Tampilkan username lama di EditText
//        }
//
//        // Tombol untuk menyimpan username baru
//        btnSaveUsername.setOnClickListener {
//            val newUsername = etNewUsername.text.toString().trim()
//
//            if (newUsername.isEmpty()) {
//                Toast.makeText(requireContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Panggil fungsi untuk mengupdate username
//            authViewModel.updateUsername(newUsername) { success ->
//                if (success) {
//                    Toast.makeText(requireContext(), "Username updated successfully", Toast.LENGTH_SHORT).show()
//                    // Update EditText dengan username baru
//                    etNewUsername.setText(newUsername)
//                } else {
//                    Toast.makeText(requireContext(), "Failed to update username", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//
//        // Tombol untuk kembali ke ProfileFragment
//        btnBack.setOnClickListener {
//            parentFragmentManager.popBackStack()
//        }
//
//        return view
//    }
//}