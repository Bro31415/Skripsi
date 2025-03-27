package com.example.skripsi.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.MyApp
import com.example.skripsi.R
import com.example.skripsi.data.model.User
import com.example.skripsi.data.repository.UserRepository
import com.example.skripsi.viewmodel.AuthViewModel
import com.example.skripsi.viewmodel.factory.AuthViewModelFactory
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.launch
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.UploadData
import io.ktor.http.ContentType
import java.util.UUID

class ProfileFragment : Fragment() {

    private lateinit var authViewModel: AuthViewModel
    private lateinit var profileImageView: ImageView

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
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profileImageView = view.findViewById(R.id.iv_profile)
        val userRepository = UserRepository()
        authViewModel = ViewModelProvider(this, AuthViewModelFactory(userRepository)).get(AuthViewModel::class.java)

        val currentUser = MyApp.supabase.auth.currentUserOrNull()
        val userId = currentUser?.id

        if (userId != null) {
            loadUserProfile(userId, view)

            // Tombol upload foto
            view.findViewById<Button>(R.id.btn_upload_photo).setOnClickListener {
                pickImageLauncher.launch("image/*")
            }
        }

        return view
    }

    private fun uploadImageToSupabase(imageUri: Uri) {
        lifecycleScope.launch {
            try {
                val userId = MyApp.supabase.auth.currentUserOrNull()?.id ?: return@launch

                // 1. Baca file dari URI
                val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                val bytes = inputStream?.readBytes() ?: throw Exception("Gagal membaca gambar")

                // 2. Generate nama file unik
                val fileName = "$userId/${UUID.randomUUID()}.jpg"

                // 3. Upload ke Supabase Storage
                MyApp.supabase.storage
                    .from("profile-picture")
                    .upload(
                        path = fileName,
                        data = bytes,
                        options = {
                            // Opsional: tambahkan konfigurasi upload di sini
                            contentType = ContentType.Image.JPEG
                            upsert = true
                        }
                    )


                // 4. Dapatkan URL publik
                val publicUrl = MyApp.supabase.storage
                    .from("profile-picture")
                    .publicUrl(fileName)


                // 5. Update URL di database user
                MyApp.supabase.postgrest
                    .from("users")
                    .update({
                        set("user_photo_profile", publicUrl)
                    }) {
                        filter{
                            eq("id", userId)
                        }
                    }


                // 6. Tampilkan gambar baru
                Log.d("ProfileFragment", "Image uploaded at: $publicUrl")
                val requestOptions = RequestOptions()
                    .timeout(10000) // 10 detik timeout
                Glide.with(this@ProfileFragment)
                    .load(publicUrl)
                    .apply(requestOptions)
                    .skipMemoryCache(true) // Abaikan cache di memori
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Abaikan cache di disk
                    .circleCrop()
                    .into(profileImageView)


                Toast.makeText(context, "Foto profil berhasil diupdate!", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("ProfileFragment", "Upload error", e)
            }
        }
    }

    private fun loadUserProfile(userId: String, view: View) {
        Log.d("ProfileFragment", "User ID: $userId")
        authViewModel.getUserProfile(userId) { user ->
            if (user != null) {
                // Tampilkan username
                view.findViewById<TextView>(R.id.tv_username).text = user.username

                // Tampilkan tahun join
                user.created_at?.let { instant ->
                    val joinYear = formatInstantToYear(instant)
                    view.findViewById<TextView>(R.id.tv_join_date).text = "Anggota sejak $joinYear"
                }

                // Tampilkan foto profil jika ada
                user.user_photo_profile?.let { imageUrl ->
                    Glide.with(this@ProfileFragment)
                        .load(imageUrl)
                        .placeholder(R.drawable.default_profile)
                        .circleCrop()
                        .into(profileImageView)
                }

                // Tombol edit username
                view.findViewById<Button>(R.id.btn_edit_username).setOnClickListener {
                    val editUsernameFragment = EditUsernameFragment().apply {
                        arguments = Bundle().apply {
                            putString("oldUsername", user.username)
                        }
                    }
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, editUsernameFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }


    private fun formatInstantToYear(instant: Instant): String { //nampilin tahun aja
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return localDateTime.year.toString()
    }
}