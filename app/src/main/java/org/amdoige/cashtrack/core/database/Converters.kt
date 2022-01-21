package org.amdoige.cashtrack.core.database
//
//import androidx.room.ProvidedTypeConverter
//import androidx.room.TypeConverter
//import java.time.Instant
//
//class Converters {
//    @TypeConverter
//    fun fromInstant(value: Instant?): String? {
//        return if (value != null) "${value.epochSecond}.${value.nano}" else null
//    }
//
//    @TypeConverter
//    fun toInstant(value: String?): Instant? {
//        val split = value.split(".", limit = 2)
//        val epochSeconds = split[0].toLong()
//        val nanoSeconds = split[1].toLong()
//        return Instant.ofEpochSecond(epochSeconds, nanoSeconds)
//    }
//}