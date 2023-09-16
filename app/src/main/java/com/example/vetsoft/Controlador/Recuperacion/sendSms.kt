package com.example.vetsoft.Controlador.Recuperacion

import com.twilio.Twilio
import com.twilio.exception.TwilioException
import com.twilio.rest.api.v2010.account.Message
import com.twilio.type.PhoneNumber

/*import com.vonage.client.VonageClient
import com.vonage.client.sms.MessageStatus
import com.vonage.client.sms.SmsSubmissionResponse
import com.vonage.client.sms.messages.TextMessage*/
import org.apache.http.conn.ssl.AllowAllHostnameVerifier
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.X509HostnameVerifier
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients


class sendSms {

    val ACCOUNT_SID = "ACfb0b56fe70356e0a7d5445a49cbb233b"
    val AUTH_TOKEN = "4ee0491d6bf83efac273f2ca724ba2a9"
    val TWILIO_PHONE_NUMBER = "+16067140725"

    fun send(tel: String, code: String) {
        try {
            //val hostnameVerifier = NoopHostnameVerifier.INSTANCE

            // Crea un CloseableHttpClient con el verificador de nombre de host personalizado
            val hostnameVerifier: NoopHostnameVerifier? = NoopHostnameVerifier.INSTANCE
            var httpClient: CloseableHttpClient = HttpClients.custom()
                .setSSLHostnameVerifier(hostnameVerifier)
                .build()

            httpClient.close()

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