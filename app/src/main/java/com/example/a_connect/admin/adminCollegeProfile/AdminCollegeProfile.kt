package com.example.a_connect.admin.adminCollegeProfile
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.a_connect.R
import com.example.a_connect.databinding.FragmentAdminCollegeProfileBinding
import com.google.firebase.firestore.FirebaseFirestore
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.InputStream


class AdminCollegeProfile : Fragment() {
    private lateinit var binding: FragmentAdminCollegeProfileBinding

    companion object {
        private const val PICK_FILE_REQUEST_CODE_ALUMNI = 1001
        private const val PICK_FILE_REQUEST_CODE_STUDENT = 1002
    }

    private lateinit var firestore: FirebaseFirestore



    private var listener: OnAdminEditProfileClickListener? = null

    interface OnAdminEditProfileClickListener {
        fun onAdminEditProfileClicked()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnAdminEditProfileClickListener) {
            listener = parentFragment as OnAdminEditProfileClickListener
        } else {
            throw RuntimeException("Parent fragment must implement OnClassClickListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAdminCollegeProfileBinding.inflate(inflater,container,false)

        firestore = FirebaseFirestore.getInstance()

        binding.importAlumniExcelData.setOnClickListener { selectFile(PICK_FILE_REQUEST_CODE_ALUMNI) }
       binding.importStudentExcelData.setOnClickListener { selectFile(PICK_FILE_REQUEST_CODE_STUDENT) }


        //for testing i am using add button to open edit profie
        binding.addimage.setOnClickListener {

            listener?.onAdminEditProfileClicked()
        }


        return binding.root
    }
    private fun selectFile(requestCode: Int) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // Excel files only
        startActivityForResult(Intent.createChooser(intent, "Select Excel File"), requestCode)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == PICK_FILE_REQUEST_CODE_ALUMNI || requestCode == PICK_FILE_REQUEST_CODE_STUDENT)
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            val fileUri = data.data
            if (fileUri != null) {
                processExcelFile(fileUri, requestCode == PICK_FILE_REQUEST_CODE_ALUMNI)
            } else {
                Toast.makeText(context, "File selection failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun processExcelFile(fileUri: Uri, isAlumni: Boolean) {
        try {
            val inputStream: InputStream? = context?.contentResolver?.openInputStream(fileUri)
            val workbook = XSSFWorkbook(inputStream)
            val sheet = workbook.getSheetAt(0)

            for (row in sheet) {
                try {
                    if (row.rowNum == 0) {
                        // Skip header row
                        continue
                    }

                    val email = getCellValue(row.getCell(0))
                    val name = getCellValue(row.getCell(1))
                    val yearStr = getCellValue(row.getCell(2))
                    val collegeName = getCellValue(row.getCell(3))

                    // Validate year
                    val year = yearStr.toIntOrNull()
                    if (year == null) {
                        Log.e("Invalid Data", "Year is not a number for row ${row.rowNum}: $yearStr")
                        continue // Skip this row
                    }

                    // Prepare data to upload
                    val user = hashMapOf(
                        "email" to email,
                        "name" to name,
                        (if (isAlumni) "graduationYear" else "enrollmentYear") to year,
                        "collegeName" to collegeName,
                        "bio" to "",
                        "profilePictureUrl" to "",
                        "connections" to hashMapOf<String, Any>()
                    )

                    if (isAlumni) {
                        user["milestones"] = hashMapOf<String, Any>()
                    }

                    // Upload to Firestore
                    val collection = if (isAlumni) "Alumni" else "Students"
                    firestore.collection("Users")
                        .document(collection)
                        .collection(email)
                        .document(email)
                        .set(user)
                        .addOnSuccessListener {
                            Log.d("FireStore", "Uploaded: $email successfully")
                            Toast.makeText(requireContext(), "Data uploaded successfully!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error uploading $email: ${e.message}", e)
                            Toast.makeText(requireContext(), "Data uploaded Failed", Toast.LENGTH_SHORT).show()
                        }
                } catch (e: Exception) {
                    Log.e("Excel Row Error", "Error processing row ${row.rowNum}", e)
                    Toast.makeText(requireContext(), "Data uploaded Failed", Toast.LENGTH_SHORT).show()

                }
            }

            workbook.close()
          //  Toast.makeText(requireContext(), "Data uploaded successfully!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("Excel Processing Error", "Error processing Excel file", e)
            Toast.makeText(requireContext(), "Error processing file", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * Safely retrieves the cell value as a string, regardless of the cell type.
     */
    private fun getCellValue(cell: org.apache.poi.ss.usermodel.Cell?): String {
        return when (cell?.cellType) {
            org.apache.poi.ss.usermodel.CellType.STRING -> cell.stringCellValue.trim()
            org.apache.poi.ss.usermodel.CellType.NUMERIC -> {
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    cell.dateCellValue.toString()
                } else {
                    cell.numericCellValue.toLong().toString() // Convert numeric to long to avoid scientific notation
                }
            }
            org.apache.poi.ss.usermodel.CellType.BOOLEAN -> cell.booleanCellValue.toString()
            org.apache.poi.ss.usermodel.CellType.FORMULA -> cell.cellFormula
            else -> ""
        }
    }

}

