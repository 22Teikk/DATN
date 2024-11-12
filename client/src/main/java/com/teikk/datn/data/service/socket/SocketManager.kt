package com.teikk.datn.data.service.socket

import android.util.Log
import io.socket.client.Socket
import io.socket.emitter.Emitter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor(
    private val mSocket: Socket,
    private val listeners: SocketListeners
) {

    fun socketConnect() {
        if (!mSocket.connected()) {
            DebounceUtils.debounce100(object : DebounceUtils.DebounceCallback {
                override fun run() {
                    mSocket.on(Socket.EVENT_CONNECT, onConnect)
                    mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect)
                    mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
                    mSocket.connect()
                }
            })
        }
    }

    fun socketDisconnect() {
        socketOff()
        mSocket.disconnect()
    }

    private fun socketOn() {
        socketOff()
        mSocket.on("transactionEvent", listeners.onTransactionsListening)
    }


    private fun socketOff() {
        mSocket.off("transactionEvent")
    }

    val onConnect = Emitter.Listener {
        Log.d(
            TAG,
            "SocketManager  isConnected " + mSocket.connected() + " |  isActive  " + mSocket.isActive
        )
        socketOn()
    }
    val onDisconnect = Emitter.Listener {
        Log.d(
            TAG,
            "SocketManager   Disconnected " + mSocket.connected() + " |  isActive  " + mSocket.isActive
        )
        socketOff()
    }
    val onConnectError = Emitter.Listener { args: Array<Any> ->
        Log.d(TAG, "SocketManager Error connecting..." + args[0].toString())
        socketOff()
    }

    companion object {
        private const val TAG = "SocketManager"
    }
}