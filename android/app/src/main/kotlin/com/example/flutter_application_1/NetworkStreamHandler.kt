package com.example.flutter_application_1

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.flutter.plugin.common.EventChannel

class NetworkStreamHandler(var mainActivity: Activity?) : EventChannel.StreamHandler {

    private var eventSink: EventChannel.EventSink? = null
    override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
        eventSink = events
        startNetworkListening()
    }

    private fun startNetworkListening() {
        val connectivityManager =
            mainActivity?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager?.registerNetworkCallback(networkRequest, networkCallback)
    }

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            mainActivity?.runOnUiThread {
                eventSink?.success(0)
            }
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            var status = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> 1
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> 2
                else -> 0
            }
            mainActivity?.runOnUiThread {
                eventSink?.success(status)
            }
        }

    }

    override fun onCancel(arguments: Any?) {
        eventSink = null
        mainActivity = null
        stopNetworkListening()
    }

    private fun stopNetworkListening() {
        val connectivityManager =
            mainActivity?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

}
