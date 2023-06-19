package com.volokhinaleksey.core.ui.widgets

import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import com.volokhinaleksey.models.ui.TrackUI

@Composable
fun SongPopupMenu(state: Boolean, track: TrackUI, onChangeState: () -> Unit) {
    val context = LocalContext.current
    DropdownMenu(expanded = state, onDismissRequest = onChangeState) {
        DropdownMenuItem(
            text = { Text(text = stringResource(com.volokhinaleksey.core.R.string.share)) },
            onClick = {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "audio/*"
                share.putExtra(Intent.EXTRA_STREAM, Uri.parse(track.path))
                startActivity(context, Intent.createChooser(share, "Share Sound File"), null)
                onChangeState()
            },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Share,
                    contentDescription = stringResource(com.volokhinaleksey.core.R.string.share)
                )
            })
    }
}