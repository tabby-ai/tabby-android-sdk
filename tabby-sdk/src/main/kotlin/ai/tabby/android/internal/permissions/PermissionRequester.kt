package ai.tabby.android.internal.permissions

import android.Manifest
import android.webkit.PermissionRequest
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import kotlinx.coroutines.CompletableDeferred

internal class PermissionRequester : ActivityResultCallback<Boolean> {

    lateinit var activityResultLauncher: ActivityResultLauncher<String>

    private var currentRequest: CompletableDeferred<Boolean>? = null

    private var inProgress: Boolean = false

    suspend fun requestPermissions(permissions: List<WebViewPermissions>): List<PermissionsCheck> {
        if (inProgress) {
            return permissions.map { PermissionsCheck(it, false) }
        }

        inProgress = true

        val result = permissions.map {
            currentRequest = CompletableDeferred()
            activityResultLauncher.launch(it.androidPermission())
            val result = currentRequest?.await()
            currentRequest = null
            PermissionsCheck(it, result)
        }

        inProgress = false

        return result
    }

    override fun onActivityResult(result: Boolean) {
        currentRequest?.complete(result)
    }
}

internal data class PermissionsCheck(
    val permission: WebViewPermissions,
    var isGranted: Boolean? = null,
)

internal enum class WebViewPermissions(val permission: String) {
    VideoCapture(PermissionRequest.RESOURCE_VIDEO_CAPTURE),
    AudioCapture(PermissionRequest.RESOURCE_AUDIO_CAPTURE);

    companion object {
        fun byPermission(permission: String): WebViewPermissions? {
            return entries.find { it.permission == permission }
        }
    }
}

internal fun WebViewPermissions.androidPermission(): String = when (this) {
    WebViewPermissions.VideoCapture -> Manifest.permission.CAMERA
    WebViewPermissions.AudioCapture -> Manifest.permission.RECORD_AUDIO
}