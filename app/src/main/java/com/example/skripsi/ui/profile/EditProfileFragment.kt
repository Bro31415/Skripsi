package com.example.skripsi.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import io.ktor.http.ContentType
import kotlinx.coroutines.launch
import java.util.UUID

class EditProfileFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileImageView: ImageView
    private var currentPhotoUrl: String? = null

    // Untuk memilih gambar dari gallery
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { uploadImageToSupabase(it) }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        val etNewUsername = view.findViewById<EditText>(R.id.et_new_username)
        val btnSaveUsername = view.findViewById<Button>(R.id.btn_save_username)
        val btnBack = view.findViewById<ImageButton>(R.id.btn_back)
        val btnUploadPhoto = view.findViewById<Button>(R.id.btn_upload_photo)
        profileImageView = view.findViewById<ImageView>(R.id.iv_profile)

        val userRepository = UserRepository()
        authViewModel = ViewModelProvider(this, AuthViewModelFactory(userRepository)).get(AuthViewModel::class.java)

        // Ambil data dari arguments
        val oldUsername = arguments?.getString("oldUsername")
        currentPhotoUrl = arguments?.getString("profilePhotoUrl")
        if (oldUsername != null) {
            etNewUsername.setText(oldUsername) // Tampilkan username lama di EditText
        }
        currentPhotoUrl?.let { url ->
            Glide.with(this)
                .load(url)
                .placeholder(R.drawable.default_profile)
                .circleCrop()
                .into(profileImageView)
        }

        // Tombol upload foto
        btnUploadPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Tombol save
        btnSaveUsername.setOnClickListener {
            val newUsername = etNewUsername.text.toString().trim()

            if (newUsername.isEmpty()) {
                Toast.makeText(requireContext(), "Username cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil fungsi untuk mengupdate username
            authViewModel.updateUsername(newUsername) { success ->
                if (success) {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    parentFragmentManager.popBackStack()
                    // Update EditText dengan username baru
//                    etNewUsername.setText(newUsername)
                } else {
                    Toast.makeText(requireContext(), "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Tombol untuk kembali ke ProfileFragment
        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
    private fun uploadImageToSupabase(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                val userId = MyApp.supabase.auth.currentUserOrNull()?.id ?: return@launch

                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes() ?: throw Exception("Gagal membaca gambar")

                val fileName = "$userId/${UUID.randomUUID()}.jpg"

                MyApp.supabase.storage
                    .from("profile-picture")
                    .upload(
                        path = fileName,
                        data = bytes,
                        options = {
                            contentType = ContentType.Image.JPEG
                            upsert = true
                        }
                    )

                val publicUrl = MyApp.supabase.storage
                    .from("profile-picture")
                    .publicUrl(fileName)

                // Update tampilan gambar
                val requestOptions = RequestOptions()
                    .timeout(10000) // 10 detik timeout
                Glide.with(this@EditProfileFragment)
                    .load(publicUrl)
                    .apply(requestOptions)
                    .skipMemoryCache(true) // Abaikan cache di memori
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Abaikan cache di disk
                    .circleCrop()
                    .into(profileImageView)

                // Simpan URL baru
                currentPhotoUrl = publicUrl

                // Update database
                MyApp.supabase.postgrest
                    .from("users")
                    .update({ set("user_photo_profile", publicUrl) }) {
                        filter { eq("id", userId) }
                    }

                Toast.makeText(context, "Foto profil berhasil diupdate!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
