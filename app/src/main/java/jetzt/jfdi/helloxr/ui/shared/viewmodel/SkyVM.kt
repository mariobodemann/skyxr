package jetzt.jfdi.skyxr.ui.shared.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import jetzt.jfdi.skyxr.atproto.Post
import jetzt.jfdi.skyxr.atproto.createBskyService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SkyVM : ViewModel() {
    // TODO THINK DI
    private val service = createBskyService()

    private var _posts = MutableStateFlow<List<SkyPost>>(listOf<SkyPost>())
    val posts: StateFlow<List<SkyPost>> = _posts.asStateFlow()

    private var _searchTerm = MutableStateFlow<String>("Cats with hats")
    val searchTerm: StateFlow<String> = _searchTerm.asStateFlow()

    private var _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()


    fun search(term: String, limit: Int = 12) {
        if (term.isBlank()) {
            // reject
            return
        }

        _loading.update { true }

        viewModelScope.launch {
            try {
                val response = service.searchPosts(
                    term,
                    limit = limit,
                )

                _posts.update { response.posts.map { it.asSkyPost() } }
            } catch (th: Throwable) {
                Log.e("SkyVM", "Error while fetching posts with tern $term.", th)
            } finally {
                _loading.update { false }
            }
        }
    }
}

data class SkyPost(
    val authorName: String,
    val authorHandle: String,
    val authorPic: String,
    val text: String,
    val image: String,
)

private fun Post.asSkyPost() = SkyPost(
    authorName = author.displayName,
    authorHandle = author.handle,
    authorPic = author.avatar ?: "",
    text = record.text,
    image = image,
)

private val Post.image: String
    get() {
        val did = author.did
        var cid = embed?.images?.firstOrNull()?.image?.reference?.link

        if (cid == null) {
            cid = record.embed?.thumb?.reference?.link
        }

        if (cid == null) {
            cid = record.embed?.images?.firstOrNull()?.image?.reference?.link
        }

        if (cid == null) {
            cid = record.embed?.thumb?.reference?.link
        }

        var url = if (cid == null) {
            embed?.images?.firstOrNull()?.fullsize
        } else {
            "https://bsky.social/xrpc/com.atproto.sync.getBlob?did=$did&cid=$cid"
        }

        val image = url ?: ""

        if (image.isNotBlank()) {
            Log.i("IMAGE", "Loading image $image")
        }

        return image
    }

