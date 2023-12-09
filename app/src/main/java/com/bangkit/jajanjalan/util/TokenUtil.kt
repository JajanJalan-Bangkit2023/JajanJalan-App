package com.bangkit.jajanjalan.util

import io.jsonwebtoken.Jwts

class TokenUtil {

    companion object {
        fun getUserIdByToken(HttpHeaders: String): String {
            val token = HttpHeaders.split(" ")[1]
            val payload = token.split(".")[1]
            val payloadString = String(android.util.Base64.decode(payload, android.util.Base64.DEFAULT))
            val payloadJson = org.json.JSONObject(payloadString)

            return Jwts.parser().setSigningKey("secret").parseClaimsJws(token).body.subject
        }
    }


}