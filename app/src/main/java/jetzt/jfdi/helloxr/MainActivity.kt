package jetzt.jfdi.skyxr

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.xr.compose.platform.LocalSession
import androidx.xr.compose.platform.LocalSpatialCapabilities
import androidx.xr.compose.spatial.Subspace
import androidx.xr.scenecore.Session
import jetzt.jfdi.skyxr.ui.classic.ClassicContent
import jetzt.jfdi.skyxr.ui.shared.viewmodel.SkyVM
import jetzt.jfdi.skyxr.ui.xr.EnvironmentController
import jetzt.jfdi.skyxr.ui.xr.SpatialContent
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val vm: SkyVM by viewModels()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                val localSession = LocalSession.current
                val environmentController = remember(this) {
                    if (localSession != null) {
                        val session = Session.create(this)
                        EnvironmentController(session, lifecycleScope)
                    } else {
                        null
                    }
                }

                if (LocalSpatialCapabilities.current.isSpatialUiEnabled) {
                    Subspace {
                        SpatialContent(
                            vm = vm,
                            onRequestHomeSpaceMode = {
                                environmentController?.requestHomeSpaceMode()
                            }
                        )
                    }
                } else {
                    ClassicContent(
                        vm = vm,
                        onRequestFullSpaceMode = {
                            environmentController?.requestFullSpaceMode()
                        }
                    )
                }
            }
        }
    }
}

