import okhttp3.*
import okio.ByteString

class WebSocketManager(
    private val onMessageReceived: (String) -> Unit
) {

    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    fun connect() {
        val request = Request.Builder()
            .url("ws://10.0.2.2:8080") // emulator → localhost
            .build()

        webSocket = client.newWebSocket(request, socketListener)
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }

    private val socketListener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            println("Connected to server")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            onMessageReceived(text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {}

        override fun onFailure(
            webSocket: WebSocket,
            t: Throwable,
            response: Response?
        ) {
            t.printStackTrace()
        }
    }
}
