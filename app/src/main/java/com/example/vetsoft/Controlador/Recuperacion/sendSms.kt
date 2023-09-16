package com.example.vetsoft.Controlador.Recuperacion

import com.twilio.Twilio
import com.twilio.exception.TwilioException
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber


class sendSms {
    val ACCOUNT_SID = "ACfb0b56fe70356e0a7d5445a49cbb233b"
    val AUTH_TOKEN = "4ee0491d6bf83efac273f2ca724ba2a9"
    val TWILIO_PHONE_NUMBER = "+16067140725"

    fun send(tel: String, code: String) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN)
            val message: Message = Message.creator(
                PhoneNumber("+503$tel"),
                PhoneNumber(TWILIO_PHONE_NUMBER),
                "Tu código de recuperación es $code"
            ).create()
            System.out.println(message.getSid())
        } catch (e: TwilioException) {
            System.err.println(e.toString())
        }
    }
}