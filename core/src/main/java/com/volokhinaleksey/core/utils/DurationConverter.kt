package com.volokhinaleksey.core.utils

import java.util.concurrent.TimeUnit

fun durationConvert(duration: Long): String {
    return String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(duration),
        TimeUnit.MILLISECONDS.toSeconds(duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
    )
}