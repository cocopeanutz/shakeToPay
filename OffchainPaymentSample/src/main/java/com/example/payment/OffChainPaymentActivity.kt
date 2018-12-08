package com.example.payment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.text.method.ScrollingMovementMethod


class OffChainPaymentActivity : AppCompatActivity() {

    private val TAG = "OffChainPaymentActivity"
    private val clientSideDepositAmount = "5" // 5 WEI
    private val serverSideDepositAmount = "15" // 15 WEI
    var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initActions()
    }

    private fun showLog(str: String) {
        Log.d(TAG, str)
        handler.post {
            logTextView?.movementMethod = ScrollingMovementMethod()
            logTextView?.append("\n\n" + str)
        }
    }

    private fun initActions() {
        //step 1: Create new wallet
        createWalletButton?.setOnClickListener {
            KeyStoreHelper.generateAccount(this)
            showLog("Step 1: createWallet success : ${KeyStoreHelper.getAddress()}")
            Log.d("KeyStoreaddress:",KeyStoreHelper.getAddress())
        }

        //step 2: Get token from faucet
        getTokenFromFaucetButton?.setOnClickListener {
            FaucetHelper().getTokenFromPrivateNetFaucet(context = this,
                    faucetURL = "http://54.188.217.246:3008/donate/",
                    walletAddress = KeyStoreHelper.getAddress(),
                    faucetCallBack = object : FaucetHelper.FaucetCallBack {
                        override fun onSuccess() {
                            showLog("Step 2: getTokenFromFaucet success, wait for transaction to complete")
                        }

                        override fun onFailure() {
                            showLog("getTokenFromFaucet error")
                        }
                    })
        }

        //step 3: Create Celer Client
        createCelerClientButton?.setOnClickListener {
            val result = CelerClientAPIHelper.initCelerClient(
                    keyStoreString = KeyStoreHelper.getKeyStoreString(),
                    passwordStr = KeyStoreHelper.getPassword(),
                    profile = CelerClientAPIHelper.getProfile(this))
            showLog("Step 3: $result")
        }

        //step 4: Join Celer
        joinCelerButton?.setOnClickListener {
            val result = CelerClientAPIHelper.joinCeler(clientSideDepositAmount, serverSideDepositAmount)
            showLog("Step 4: $result")
            if (result.contains("successful")) {
                sendPaymentButton?.visibility = View.VISIBLE
            } else {
                sendPaymentButton?.visibility = View.INVISIBLE
            }
        }

//        //step 5: Check balance
//        checkBalanceButton?.setOnClickListener {
//            val result = CelerClientAPIHelper.checkBalance()
//            showLog("Current balance: $result")
//        }

        //step 6: Send payment
        sendPaymentButton?.setOnClickListener {
            val intent = Intent(this, secondActivity::class.java).apply {
                putExtra("myAddress", KeyStoreHelper.getAddress())
            }
            startActivity(intent)

        }
    }

}
