package com.example.payment

import android.content.Context
import android.util.Log
import network.celer.celersdk.*

object CelerClientAPIHelper {

    private val TAG = "CelerClientAPIHelper"
    private var client: Client? = null

    fun getProfile(context:Context):String{
        return context.getString(R.string.cprofile_private_testnet, KeyStoreHelper.generateFilePath(context))
    }

    fun initCelerClient(keyStoreString: String, passwordStr: String, profile: String): String {
        // Init Celer Client
        try {
            client = Celersdk.newClient(keyStoreString, passwordStr, profile)
            Log.d(TAG, "Celer client created")
            return "Celer client created"
        } catch (e: Exception) {
            Log.d(TAG, e.localizedMessage)
            return e.localizedMessage
        }
    }

    fun joinCeler(clientSideDepositAmount: String, serverSideDepositAmount: String): String {
        // Join Celer Network
        try {
            client?.joinCeler("0x0", clientSideDepositAmount, serverSideDepositAmount)
            return "joinCeler: successful"
        } catch (e: Exception) {
            Log.d(TAG, "Join Celer Network Error: ${e.localizedMessage}")
            return "Join Celer Network Error: ${e.localizedMessage}"
        }

    }

    @JvmStatic fun checkBalance(): String {
        // check has joined Celer
        try {
            val balance = client?.getBalance(1L)?.available
            Log.d(TAG, "current Balance: $balance")
            return "$balance wei"
        } catch (e: Exception) {
            Log.d(TAG, "Check Balance Error: ${e.localizedMessage}")
            return "Check Balance Error: ${e.localizedMessage}"
        }

    }

    @JvmStatic fun sendPayment(receiverAddress: String, transferAmount: String): String {
        try {
            client?.sendPay(receiverAddress, transferAmount)
            return "Send payment: successful"
        } catch (e: Exception) {
            Log.d(TAG, "send PaymentError: ${e.localizedMessage}")
            return "Send payment Error: ${e.localizedMessage}"
        }
    }
    @JvmStatic fun verifyReceiver(receiverAddress: String): String? {
        Log.d("verifyReceiver", client?.hasJoinedCeler(receiverAddress))
        return client?.hasJoinedCeler(receiverAddress)
    }
}