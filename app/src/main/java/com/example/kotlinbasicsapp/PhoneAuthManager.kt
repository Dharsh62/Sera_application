package com.example.kotlinbasicsapp.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

object PhoneAuthManager {

    fun sendOtp(
        phone: String,
        activity: Activity,
        onCodeSent: (String) -> Unit,
        onVerificationCompleted: (PhoneAuthCredential) -> Unit,
        onError: (String) -> Unit
    ) {

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    onVerificationCompleted(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    onError(e.message ?: "Verification Failed")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onCodeSent(verificationId)
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }
    fun verifyOtp(
        verificationId: String,
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {

        val credential = PhoneAuthProvider.getCredential(verificationId, otp)

        FirebaseAuth.getInstance()
            .signInWithCredential(credential)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onError("OTP verification failed")
                }
            }
    }
}