package com.example.vetsoft.Controlador.Recuperacion

import android.os.AsyncTask

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.Message.RecipientType
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


class MandarCorreo(private val destinatario:String,
                   private val asunto: String,
                   private val mensaje: String) :
AsyncTask<Void?, Void?, Void?>(){
    override fun doInBackground(vararg p0: Void?): Void? {

        val props = Properties()
        props["mail.smtp.host"] = "smtp.gmail.com"
        props["mail.smtp.socketFactory.port"] = "465"
        props["mail.smtp.socketFactory.class"] = "javax.net.ssl.SSLSocketFactory"
        props["mail.smtp.auth"]="true"
        props["mail.smtp.port"]="465"

        val session = Session.getDefaultInstance(props,
        object : Authenticator(){
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("vetsoftsoporte@gmail.com","jtsteydqiifilcpi")
            }
        })
        try {
            val message = MimeMessage(session)


            message.setFrom(InternetAddress("vetsoftsoporte@gmail.com"))

            message.addRecipient(Message.RecipientType.TO, InternetAddress(destinatario))
            message.subject = asunto

            // Crear MimeMultipart
            val multipart = MimeMultipart()

            // Crear parte MimeBodyPart para el contenido HTML
            val htmlPart = MimeBodyPart()
            htmlPart.setContent(mensaje, "text/html")

            // Agregar parte del contenido HTML al MimeMultipart
            multipart.addBodyPart(htmlPart)

            // Establecer el MimeMultipart como el contenido del MimeMessage
            message.setContent(multipart)

            // Enviar el mensaje
            Transport.send(message)
            message.addRecipient(RecipientType.TO, InternetAddress(destinatario))
            message.subject = asunto
            message.setText(this.mensaje)
            Transport.send(message)

            println("Correo enviado correctamente .;")
        }catch (e: MessagingException){
            e.printStackTrace()

            println("Correo no enviado")
        }
        return null


    }
}