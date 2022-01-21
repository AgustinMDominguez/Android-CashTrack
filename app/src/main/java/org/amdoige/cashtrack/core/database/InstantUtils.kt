package org.amdoige.cashtrack.core.database

//import java.time.Instant
//
//object InstantUtils {
//    fun toString(instant: Instant): String {
//        val epochString = instant.epochSecond.toString().padStart(19, '0')
//        val nanoString = instant.nano.toString().padStart(8, '0')
//        return "$epochString|$nanoString"
//    }
//
//    fun fromString(string: String): Instant? {
//        val split = string.split(".", limit = 2)
//        val epochSeconds = split[0].toLong()
//        val nanoSeconds = split[1].toLong()
//        return Instant.ofEpochSecond(epochSeconds, nanoSeconds)
//    }
//}
