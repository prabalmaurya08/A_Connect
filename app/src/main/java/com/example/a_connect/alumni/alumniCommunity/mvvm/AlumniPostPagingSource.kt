import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.a_connect.alumni.alumniPost.mvvm.AlumniPostDataClass
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class AlumniPostPagingSource(
    private val postCollection: CollectionReference
) : PagingSource<DocumentSnapshot, AlumniPostDataClass>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, AlumniPostDataClass> {
        return try {
            val query: Query = if (params.key == null) {
                postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
                    .limit(params.loadSize.toLong())
            } else {
                postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
                    .startAfter(params.key)
                    .limit(params.loadSize.toLong())
            }

            val snapshot = query.get().await()
            val documents = snapshot.documents

            val posts = documents.mapNotNull { doc ->
                doc.toObject(AlumniPostDataClass::class.java)?.copy(postId = doc.id)
            }

            val nextKey = if (documents.isNotEmpty() && documents.size == params.loadSize) {
                documents.last()
            } else {
                null // No more data to load
            }

            LoadResult.Page(
                data = posts.distinctBy { it.postId },
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, AlumniPostDataClass>): DocumentSnapshot? {
        // Implementation of getRefreshKey remains the same
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { post ->
                state.pages.flatMap { it.data }
                    .firstOrNull { it.postId == post.postId }
                    ?.let { matchedPost ->
                        state.pages.flatMap { it.data }
                            .indexOf(matchedPost)
                            .let { index -> state.pages.getOrNull(index)?.nextKey }
                    }
            }
        }
    }
}