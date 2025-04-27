package com.example.a_connect.alumni.alumniExplore



import com.example.a_connect.alumni.alumniProfile.mvvm.AlumniProfileDataClass
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LocationRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val alumniCollection = firestore.collection("Alumni")

    // Function to update user location (latitude and longitude) in FireStore
    suspend fun updateUserLocation(userEmail: String, latitude: Double, longitude: Double): Boolean {
        val locationData = mapOf(
            "latitude" to latitude,
            "longitude" to longitude
        )

        return try {
            alumniCollection.document(userEmail).update("location", locationData).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Optionally, you can add a function to get the current location from FireStore
//    suspend fun getUserLocation(userEmail: String): Map<String, Double>? {
//        return try {
//            val document = alumniCollection.document(userEmail).get().await()
//            val locationData = document.get("location") as? Map<String, Double>
//            locationData
//        } catch (e: Exception) {
//            null
//        }
//    }
    // Fetch all alumni locations (using the location field of AlumniProfileDataClass)
    suspend fun getAllAlumniLocations(): List<AlumniProfileDataClass> {
        return try {
            val result = alumniCollection.get().await()
            result.documents.mapNotNull { doc ->
                val alumniProfile = doc.toObject(AlumniProfileDataClass::class.java)
                alumniProfile?.takeIf { it.location != null } // Only alumni with a location
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
//    suspend fun getAllAlumniLocations(): List<Triple<String, String, Pair<Double, Double>>> {
//        return try {
//            val result = alumniCollection.get().await()
//            result.documents.mapNotNull { doc ->
//                val name = doc.getString("name") ?: return@mapNotNull null
//                val profilePic = doc.getString("profilePic") ?: ""  // Get profile picture
//                val location = doc.get("location") as? Map<String, Any> ?: return@mapNotNull null
//                val latitude = location["latitude"] as? Double ?: return@mapNotNull null
//                val longitude = location["longitude"] as? Double ?: return@mapNotNull null
//                Triple(name, profilePic, latitude to longitude)
//            }
//        } catch (e: Exception) {
//            emptyList()
//        }
//    }

}
