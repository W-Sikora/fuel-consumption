package pl.wsikora.fce.util

import android.util.Log
import com.google.gson.Gson
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class ServerConnection {
    private val gson = Gson()
    private val mainUrl = "/api"
    private val create = "$mainUrl/add"
    private val read = "$mainUrl/predict"
    private val finish = "$mainUrl/end"


    fun createAction(id: String, engineId: Int): String {
        val engine = "engine$engineId"
        val json = JSONObject()
        json.put("android_id", id)
        json.put("engine", engine)
        print(json.toString())
        return ""
    }

    fun getUrlString(url: String): String {
        return String(getUrlBytes(url))
    }

    private fun getUrlBytes(url: String): ByteArray {
        val connection = URL(url).openConnection() as HttpsURLConnection
        try {
            val input = connection.inputStream
            val output = ByteArrayOutputStream()
            if (connection.responseCode != HttpsURLConnection.HTTP_OK) {
                throw IOException(connection.responseMessage)
            }
            val buffer = ByteArray(1024)
            var bytesRead: Int
            do {
                bytesRead = input.read(buffer)
                output.write(buffer, 0, bytesRead)
            } while (input.read(buffer) > 0)
            output.close()
            return output.toByteArray()

        } catch (e: IOException) {
            Log.e("error httpConnection", e.message.toString())
            return ByteArray(0)

        } finally {
            connection.disconnect()
        }
    }


}