package com.example.vetsoft.Controlador.Recuperacion

import com.twilio.Twilio
import com.twilio.exception.TwilioException
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.HttpClients


class sendSms {
    private val TAG = "TAGO"
    val ACCOUNT_SID = "ACfb0b56fe70356e0a7d5445a49cbb233b"
    val AUTH_TOKEN = "4ee0491d6bf83efac273f2ca724ba2a9"
    val TWILIO_PHONE_NUMBER = "+16067140725"

    fun send(tel: String, code: String) {
        try {
            /*Twilio.init(ACCOUNT_SID, AUTH_TOKEN)
            val message: Message = Message.creator(
                PhoneNumber("+503$tel"),
                PhoneNumber(TWILIO_PHONE_NUMBER),
                "Tu código de recuperación es $code"
            ).create()
            System.out.println(message.getSid())*/
            val httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build()
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN)

            // Número de teléfono de WhatsApp de Twilio (debes tenerlo configurado en Twilio)
            val fromPhoneNumber = PhoneNumber("whatsapp:$TWILIO_PHONE_NUMBER")

            // Número de teléfono de destino
            val toPhoneNumber = PhoneNumber("whatsapp:+503$tel")

            // Mensaje que deseas enviar
            val messageBody = "Este es tu codigo de verificación $code"

            // Envía el mensaje de WhatsApp
            val message = Message.creator(toPhoneNumber, fromPhoneNumber, messageBody).create()

            println("Mensaje enviado con SID: ${message.sid}")

            httpClient.close();
        } catch (e: TwilioException) {
            System.err.println(e.toString())
        }
    }

    /*fun send(tel: String, code: String){
        try {
            val smsManager = SmsManager.getDefault()

            smsManager.sendTextMessage(tel, null,
                "Tu código de recuperación es $code", null, null)
        } catch (e: Exception) {
            System.err.println(e.toString())
        }

    }*/
    /*fun send(tel: String, code: String) {
        val client = VonageClient.builder().apiKey("0533ce94").apiSecret("IKrfRfxYclO4S10z").build()
        val message = TextMessage(
            "VetSoft",
            "503$tel",
            "Tu código de recuperación es $code"
        )

        val response: SmsSubmissionResponse = client.getSmsClient().submitMessage(message)

        if (response.messages[0].status == MessageStatus.OK) {
            println("Message sent successfully.")
        } else {
            println("Message failed with error: " + response.messages[0].errorText)
        }
    }*/
}