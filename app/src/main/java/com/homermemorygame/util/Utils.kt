package com.homermemorygame.util

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset

object Utils {

    fun loadJSONFromAsset(c: Context, file: String): String? {
        val json: String
        try {
            val mngr = c.applicationContext.assets
            val `is` = mngr.open(file)

            val size = `is`.available()

            val buffer = ByteArray(size)

            `is`.read(buffer)

            `is`.close()

            json = String(buffer, Charset.forName("UTF-8"))


        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }
}