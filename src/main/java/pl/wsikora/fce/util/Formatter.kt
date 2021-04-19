package pl.wsikora.fce.util

import org.json.JSONObject

fun main() {
//    val url = "https://bitbay.net/API/Public/BTCPLN/orderbook.json"
//    val c = ServerConnection()
    val json = JSONObject()
    json.put("android_id", "122")
    json.put("engine", "engine1")
    print(json.toString())

}