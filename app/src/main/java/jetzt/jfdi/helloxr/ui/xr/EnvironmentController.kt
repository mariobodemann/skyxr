package jetzt.jfdi.skyxr.ui.xr
/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Log
import androidx.concurrent.futures.await
import androidx.xr.scenecore.GltfModel
import androidx.xr.scenecore.Session
import androidx.xr.scenecore.SpatialEnvironment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class EnvironmentController(
    private val xrSession: Session,
    private val coroutineScope: CoroutineScope
) {

    private val assetCache: HashMap<String, Any> = HashMap()
    private var activeEnvironmentModelName: String? = null

    fun requestHomeSpaceMode() = xrSession.requestHomeSpaceMode()

    fun requestFullSpaceMode() = xrSession.requestFullSpaceMode()

    fun requestPassthrough() = xrSession.spatialEnvironment.setPassthroughOpacityPreference(1f)

    /**
     * Request the system load a custom Environment
     */
    fun requestCustomEnvironment(environmentModelName: String) {
        coroutineScope.launch {
            try {

                if (activeEnvironmentModelName == null ||
                    activeEnvironmentModelName != environmentModelName
                ) {

                    val environmentModel = assetCache[environmentModelName] as GltfModel

                    SpatialEnvironment.SpatialEnvironmentPreference(
                        skybox = null,
                        geometry = environmentModel
                    ).let {
                        xrSession.spatialEnvironment.setSpatialEnvironmentPreference(
                            it
                        )
                    }

                    activeEnvironmentModelName = environmentModelName
                }
                xrSession.spatialEnvironment.setPassthroughOpacityPreference(0f)

            } catch (e: Exception) {
                Log.e(
                    "SkyXR",
                    "Failed to update Environment Preference for $environmentModelName: $e"
                )
            }

        }
    }

    fun loadModelAsset(modelName: String) {
        coroutineScope.launch {
            //load the asset if it hasn't been loaded previously
            if (!assetCache.containsKey(modelName)) {
                try {
                    val gltfModel =
                        xrSession.createGltfResourceAsync(modelName).await()

                    assetCache[modelName] = gltfModel

                } catch (e: Exception) {
                    Log.e(
                        "SkyXR",
                        "Failed to load model for $modelName: $e"
                    )
                }
            }
        }
    }
}
