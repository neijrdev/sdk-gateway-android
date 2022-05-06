package com.paylivre.sdk.gateway.android.data.model.pixApprovalTime

import com.paylivre.sdk.gateway.android.R


data class ResponseGetDataAveragePixApprovalTime(
    val average_time_key: String = "",
    val average_unit_time_1: Int = 0,
    val average_unit_time_2: Int = 0,
    val level_key: String = "",
    val flag_color_id: Int = 0,
)

data class ResponseGetTime(
    val average_time_hours: Int = 0,
    val average_time_minutes: Int = 0,
    val average_time_seconds: Int = 0,
)


fun getFlagColorId(level: String): Int {
    return when (level) {
        "normal" -> R.color.success
        "slow" -> R.color.warning2
        else -> R.color.danger
    }
}


fun getTime(average_age: String): ResponseGetTime {
    val averageAgeArraySplit = average_age.split(":")
    val hours = averageAgeArraySplit[0]
    val minutes = averageAgeArraySplit[1]
    val seconds = averageAgeArraySplit[2]
    return ResponseGetTime(hours.toInt(), minutes.toInt(), seconds.toInt())
}

fun getLevelKey(level: String): String {
    return when (level) {
        "normal" -> "level_status_pix_time_normal"
        "slow" -> "level_status_pix_time_slow"
        else -> "level_status_pix_time_very_slow"
    }
}

data class ResponseGetAverageTimeKey(
    val average_time_key: String,
    val time_unit_value1: Int,
    val time_unit_value2: Int,
)

fun getAverageTimeKey(average_time: ResponseGetTime): ResponseGetAverageTimeKey {
    return if (average_time.average_time_hours > 0) {
        ResponseGetAverageTimeKey(
            "average_time_hours_minutes",
            average_time.average_time_hours,
            average_time.average_time_minutes
        )

    } else if (average_time.average_time_minutes > 0) {
        ResponseGetAverageTimeKey(
            "average_time_minutes_seconds",
            average_time.average_time_minutes,
            average_time.average_time_seconds
        )
    } else {
        ResponseGetAverageTimeKey(
            "average_time_seconds",
            average_time.average_time_seconds,
            average_time.average_time_seconds
        )
    }
}

//val mockData = DataPixApprovalTime(
//    "30",
//    "2",
//    "00:00:17",
//    "normal",
//    0
//)

fun getDataAveragePixApprovalTime(data: DataPixApprovalTime): ResponseGetDataAveragePixApprovalTime {

    val flag_color_id = getFlagColorId(data.level)
    val average_age_time = getTime(data.average_age)
    val average_time_key = getAverageTimeKey(average_age_time)
    val level_key = getLevelKey(data.level)

    return ResponseGetDataAveragePixApprovalTime(
        average_time_key.average_time_key,
        average_time_key.time_unit_value1,
        average_time_key.time_unit_value2,
        level_key,
        flag_color_id

    )

}