package jetzt.jfdi.skyxr.ui.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import jetzt.jfdi.skyxr.R
import jetzt.jfdi.skyxr.ui.shared.viewmodel.SkyPost

@Composable
fun SkyPostView(
    modifier: Modifier = Modifier,
    post: SkyPost
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (post.image.isNotBlank()) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    model = post.image,
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_frame),
                    error = painterResource(R.drawable.ic_error),
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Companion.CenterVertically,
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(4.dp)
                        .clip(CircleShape),
                    model = post.authorPic,
                    placeholder = painterResource(R.drawable.ic_person),
                    error = painterResource(R.drawable.ic_person),
                    contentDescription = null
                )
                Column(
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        style = MaterialTheme.typography.labelLarge,
                        fontStyle = FontStyle.Companion.Normal,
                        text = post.authorName
                    )
                    Text(
                        style = MaterialTheme.typography.labelSmall,
                        text = "@${post.authorHandle}"
                    )
                }
            }
            Text(
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                text = post.text
            )
        }
    }
}


@PreviewLightDark
@Composable
private fun SkyPostViewPreview() {
    SkyPost(
        authorName = "authorName",
        authorHandle = "handle",
        authorPic = "authorPic",
        text = "text",
        image = "image",
    )
}
