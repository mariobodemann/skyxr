@file:OptIn(ExperimentalMaterial3Api::class)

package jetzt.jfdi.skyxr.ui.classic

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.xr.compose.platform.LocalHasXrSpatialFeature
import jetzt.jfdi.skyxr.R
import jetzt.jfdi.skyxr.ui.shared.MainContent
import jetzt.jfdi.skyxr.ui.shared.viewmodel.SkyVM

@SuppressLint("RestrictedApi")
@Composable
fun ClassicContent(
    vm: SkyVM,
    onRequestFullSpaceMode: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    if (LocalHasXrSpatialFeature.current) {
                        FullSpaceModeIconButton(
                            onClick = onRequestFullSpaceMode,
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MainContent(
                modifier = Modifier.padding(horizontal = 8.dp),
                posts = vm.posts.collectAsState().value,
                searchTerm = vm.searchTerm.collectAsState().value,
                loading = vm.loading.collectAsState().value,
                onSearchClicked = {
                    vm.search(it)
                }
            )
        }
    }
}
