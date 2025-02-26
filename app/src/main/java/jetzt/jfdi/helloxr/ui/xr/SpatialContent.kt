package jetzt.jfdi.skyxr.ui.xr

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.normalizedAngleCos
import androidx.compose.ui.util.normalizedAngleSin
import androidx.xr.compose.subspace.SpatialBox
import androidx.xr.compose.subspace.SpatialColumn
import androidx.xr.compose.subspace.SpatialPanel
import androidx.xr.compose.subspace.SpatialRow
import androidx.xr.compose.subspace.layout.SubspaceModifier
import androidx.xr.compose.subspace.layout.depth
import androidx.xr.compose.subspace.layout.fillMaxSize
import androidx.xr.compose.subspace.layout.height
import androidx.xr.compose.subspace.layout.movable
import androidx.xr.compose.subspace.layout.offset
import androidx.xr.compose.subspace.layout.padding
import androidx.xr.compose.subspace.layout.resizable
import androidx.xr.compose.subspace.layout.rotate
import androidx.xr.compose.subspace.layout.width
import androidx.xr.runtime.math.Vector3
import jetzt.jfdi.skyxr.ui.shared.SearchBar
import jetzt.jfdi.skyxr.ui.shared.SkyPostView
import jetzt.jfdi.skyxr.ui.shared.viewmodel.SkyVM

@SuppressLint("RestrictedApi")
@Composable
fun SpatialContent(
    vm: SkyVM,
    postRadius: Float = 350f,
    postWidth: Float = 300f,
    postHeight: Float = 300f,
    postPadding: Float = 10f,
    postsPerRound: Int = 8,
    maxPostCount: Int = 64,
    onRequestHomeSpaceMode: () -> Unit
) {
    SpatialBox(
        SubspaceModifier.width(1000.dp).height(1000.dp).depth(1000.dp).resizable().movable()
    ) {
        SpatialColumn(
            modifier = SubspaceModifier.fillMaxSize()
        ) {
            SpatialPanel(
                modifier = SubspaceModifier.weight(1f)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val previousTerm = vm.searchTerm.collectAsState().value
                    val term = if (previousTerm.isEmpty()) {
                        "3D Cats"
                    } else {
                        previousTerm
                    }

                    SearchBar(term) {
                        vm.search(it, maxPostCount)
                    }
                }
            }

            SpatialRow(modifier = SubspaceModifier.weight(9f)) {
                val posts = vm.posts.collectAsState().value

                if (posts.isNotEmpty()) {
                    SpatialBox(
                        modifier = SubspaceModifier.fillMaxSize()
                    ) {
                        for (index in posts.indices) {
                            val angle: Float = (index % postsPerRound) * (360f / postsPerRound)

                            val x = normalizedAngleCos(angle / 360f) * postRadius
                            val z = normalizedAngleSin(angle / 360f) * postRadius
                            val y = (index / postsPerRound) * (postHeight + postPadding)

                            val post = posts[index]
                            SpatialPanel(
                                SubspaceModifier
                                    .offset(
                                        x.dp, y.dp, z.dp
                                    )
                                    .width(postWidth.dp)
                                    .height(postHeight.dp)
                                    .rotate(
                                        axisAngle = Vector3(0f, 1f, 0f),
                                        rotation = 270f - angle
                                    )
                                    .padding(20.dp),
                            ) {
                                Surface(
                                    modifier = Modifier.size(
                                        width = postWidth.dp,
                                        height = postHeight.dp
                                    )
                                ) {
                                    SkyPostView(
                                        modifier = Modifier
                                            .width(postWidth.dp)
                                            .height(postHeight.dp),
                                        post = post
                                    )
                                }
                            }
                        }
                    }
                } else {
                    SpatialPanel(
                        modifier = SubspaceModifier.fillMaxSize()
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Column {
                                Spacer(Modifier.weight(1f))
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineLarge,
                                    text = "No posts available, please search for some."
                                )
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}
