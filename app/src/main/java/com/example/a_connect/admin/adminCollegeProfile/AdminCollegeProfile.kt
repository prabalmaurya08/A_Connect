package com.example.a_connect.admin.adminCollegeProfile

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.a_connect.ProgressDialogHelper
import com.example.a_connect.R
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileRepository
import com.example.a_connect.admin.adminCollegeProfile.mvvm.CollegeProfileViewModel
import com.example.a_connect.admin.adminCollegeProfile.mvvm.EditProfileViewModelFactory
import com.example.a_connect.databinding.FragmentAdminCollegeProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream

class AdminCollegeProfile : Fragment() {

    private lateinit var binding: FragmentAdminCollegeProfileBinding
    private val IMAGE_PICK_REQUEST = 1003
    private var imageUri: Uri? = null
    private var loadingDialog: Dialog? = null
    private var progressBar: ProgressBar? = null
    private val collegeId = "collegeId123" // Replace with dynamic ID if necessary
    private var isProfileLoaded = false
    private val repository = CollegeProfileRepository()

    private lateinit var firestore: FirebaseFirestore

    private val viewModel: CollegeProfileViewModel by viewModels {
        EditProfileViewModelFactory(repository)
    }

    private var listener: OnGoToEditProfileClickListener? = null

    interface OnGoToEditProfileClickListener {
        fun onGoToEditProfileClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)


        if (parentFragment is OnGoToEditProfileClickListener) {
            listener = parentFragment as OnGoToEditProfileClickListener
        } else {
            throw RuntimeException("$context must implement OnViewPdfButtonClickListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_admin_college_profile, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        firestore = FirebaseFirestore.getInstance()

        viewModel.loadProfileData(collegeId)
        setupUI()
        setupObservers()
        return binding.root
    }

    private fun setupUI() {
        // Add Image
        binding.addimage.setOnClickListener {
            openImagePicker()
        }
        binding.goToEditProfile.setOnClickListener {
            listener?.onGoToEditProfileClicked()
        }

        // Delete Image
        binding.deleteimage.setOnClickListener {
            viewModel.deleteImage(collegeId)
            Toast.makeText(requireContext(), "Image deleted successfully", Toast.LENGTH_SHORT).show()
        }

        // Import Excel Buttons
        binding.importAlumniExcelData.setOnClickListener {
            selectExcelFile(PICK_FILE_REQUEST_CODE_ALUMNI)
        }

        binding.importStudentExcelData.setOnClickListener {
            selectExcelFile(PICK_FILE_REQUEST_CODE_STUDENT)
        }

        setupLoadingDialog()
    }

    private fun setupObservers() {
        viewModel.loadingState.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoadingDialog("Loading your image...")
            } else {
                dismissLoadingDialog()
            }
        }

        viewModel.imageUrl.observe(viewLifecycleOwner) { imageUrl ->
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.alumni_job_detail_bg)
                    .error(R.drawable.alumni_job_detail_bg)
                    .into(binding.backgroundImage)
            } else {
                binding.backgroundImage.setImageResource(R.drawable.alumni_job_detail_bg)
            }
        }
    }

    private fun setupLoadingDialog() {
        loadingDialog = Dialog(requireContext()).apply {
            setContentView(R.layout.progress_bar_dialogbox)
            setCancelable(false)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    private fun showLoadingDialog(message: String = "Loading...") {
        loadingDialog?.findViewById<TextView>(R.id.loadingMessage)?.text = message
        loadingDialog?.show()
        progressBar = loadingDialog?.findViewById(R.id.progressBar)
    }

    private fun dismissLoadingDialog() {
        loadingDialog?.dismiss()
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_REQUEST)
    }

    private fun selectExcelFile(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        startActivityForResult(Intent.createChooser(intent, "Select Excel File"), requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_PICK_REQUEST -> {
                    imageUri = data?.data
                    imageUri?.let { uri ->
                        viewModel.uploadImage(collegeId, uri)
                        Toast.makeText(requireContext(), "Image uploading...", Toast.LENGTH_SHORT).show()
                    }
                }
                PICK_FILE_REQUEST_CODE_ALUMNI, PICK_FILE_REQUEST_CODE_STUDENT -> {
                    val fileUri = data?.data
                    fileUri?.let {
                        processExcelFile(it, requestCode == PICK_FILE_REQUEST_CODE_ALUMNI)
                    } ?: Toast.makeText(context, "File selection failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun processExcelFile(fileUri: Uri, isAlumni: Boolean) {
        val progressDialog = ProgressDialogHelper(requireContext())
      //  progressDialog.showProgressDialog("Uploading data...")

        try {
            val inputStream: InputStream? = context?.contentResolver?.openInputStream(fileUri)
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)

            val totalRows = sheet.physicalNumberOfRows - 1 // Exclude header
            var completedRows = 0 // Track the number of successfully processed rows

            for (row in sheet) {
                if (row.rowNum == 0) continue // Skip header row

                val email = getCellValue(row.getCell(0))
                val yearStr = getCellValue(row.getCell(1))
                val collegeName = getCellValue(row.getCell(2))

                val year = yearStr.toIntOrNull()
                if (year == null) {
                    Log.e("Invalid Data", "Year is not a number for row ${row.rowNum}: $yearStr")
                    continue
                }

                val user = hashMapOf(
                    "email" to email,
                    (if (isAlumni) "graduationYear" else "enrollmentYear") to year,
                    "collegeName" to collegeName,


                )


                val collection = if (isAlumni) "Alumni" else "Students"

                firestore.collection("Users")
                    .document(collection)
                    .collection(email)
                    .document(email)
                    .set(user)
                    .addOnSuccessListener {
                        completedRows++


                        // Save to separate collection for Alumni or Students
                                        firestore.collection(collection)
                                            .document(email) // Document ID as email
                                            .set(user)
                                            .addOnSuccessListener {
                                                Log.d("Firestore", "Data saved successfully in $collection/$email.")

                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("Firestore", "Error saving user data in $collection: ${e.message}", e)

                                            }
                                        Log.d("Firestore", "Uploaded: $email successfully")

                        // Update progress
                       // progressDialog.showProgressDialog("Uploading: $completedRows/$totalRows rows")

                        // Dismiss when all rows are processed
                        if (completedRows == totalRows-1) {

                            Toast.makeText(requireContext(), "Data uploaded successfully!", Toast.LENGTH_SHORT).show()
                        }
                        progressDialog.dismissProgressDialog()
                    }
                    .addOnFailureListener { e ->
                        completedRows++
                        Log.e("Firestore", "Error uploading $email: ${e.message}", e)

                        // Check if all rows have been processed, even if some failed
                        if (completedRows == totalRows) {
                            progressDialog.dismissProgressDialog()
                            Toast.makeText(requireContext(), "Data upload completed with some errors.", Toast.LENGTH_SHORT).show()
                        }
                    }
               // progressDialog.dismissProgressDialog()
            }

            workbook.close()
        } catch (e: Exception) {
            Log.e("Excel Processing Error", "Error processing Excel file", e)
            progressDialog.dismissProgressDialog()
            Toast.makeText(requireContext(), "Error processing file", Toast.LENGTH_SHORT).show()
        }
    }



    private fun getCellValue(cell: org.apache.poi.ss.usermodel.Cell?): String {
        return when (cell?.cellType) {
            org.apache.poi.ss.usermodel.CellType.STRING -> cell.stringCellValue.trim()
            org.apache.poi.ss.usermodel.CellType.NUMERIC -> {
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    cell.dateCellValue.toString()
                } else {
                    cell.numericCellValue.toLong().toString()
                }
            }
            org.apache.poi.ss.usermodel.CellType.BOOLEAN -> cell.booleanCellValue.toString()
            else -> ""
        }
    }

    companion object {
        private const val PICK_FILE_REQUEST_CODE_ALUMNI = 1001
        private const val PICK_FILE_REQUEST_CODE_STUDENT = 1002
    }
}
