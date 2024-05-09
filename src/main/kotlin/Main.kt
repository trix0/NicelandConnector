package niceland.vpn
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.Socket
import java.security.cert.X509Certificate
import javax.net.SocketFactory

import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

fun main() {
    val host = "64.52.81.78"
    val port = 443

    val client = OkHttpClient.Builder()
        .socketFactory(CombinedSocketFactory(SSLSocketFactory.getDefault(),host,port))
        .build()


    val request = Request.Builder()
        .url("https://$host:$port/v3/user/login")
        .build()


    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        for ((name, value) in response.headers) {
            println("$name: $value")
        }

        println(response.body!!.string())
    }
}

private fun trustedCertificatesInputStream(): InputStream {
    return InputStream.nullInputStream()
}

private fun trustManagerForCertificates(inputStream: InputStream): X509TrustManager {
    return object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }
    }
}

class CombinedSocketFactory(
    private val sslSocketFactory: SocketFactory,
    private val host: String?,private val  port: Int
) : SocketFactory() {
    private var socket:Socket? = null
    override fun createSocket(): Socket {
        val socket=sslSocketFactory.createSocket(host, port)
        val outputStream: OutputStream = socket.getOutputStream()
        val byteArray = byteArrayOf(255.toByte(), 255.toByte(), 255.toByte(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        outputStream.write(byteArray)
        outputStream.flush()
        return socket
    }

    override fun createSocket(host: String?, port: Int): Socket {
        socket=sslSocketFactory.createSocket(host, port)
        enableSocket()
        return socket!!
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        socket= sslSocketFactory.createSocket(host, port)
        enableSocket()
        return socket!!
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        socket= sslSocketFactory.createSocket(host?.hostName, port)
        enableSocket()
        return socket!!
    }

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        socket= sslSocketFactory.createSocket(address?.hostName, port)
        enableSocket()
        return socket!!
    }
    private fun enableSocket(){
        val outputStream: OutputStream = socket!!.getOutputStream()
        val byteArray = byteArrayOf(255.toByte(), 255.toByte(), 255.toByte(), 0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        outputStream.write(byteArray)
        outputStream.flush()
    }
}

