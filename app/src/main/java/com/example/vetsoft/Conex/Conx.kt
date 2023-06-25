package com.example.vetsoft.Conex

import android.os.StrictMode
import android.util.Log
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class conx {
    private val ip="10.0.2.2:1433"
    private val db="VetSoft"
    private val username="userSQL"
    private val password="pasf2"

    private val ipAlva="192.168.0.17:51150"
    private val dbAlva="VetSoft"
    private val usernameAlva="alvita"
    private val passwordAlva="ferelmejor"

    fun dbConn(): Connection?{
        val policy= StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        var conn: Connection?=null
        val connString:String
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance()
            connString="jdbc:jtds:sqlserver://" +
                    "$ip;" +
                    "databaseName=$db;" +
                    "user=$username;" +
                    "password=$password"

            conn= DriverManager.getConnection(connString)
        }
        catch (ex: SQLException){
            Log.e("Error: ",ex.message!!)
        }
        catch (ex1:ClassNotFoundException){
            Log.e("Error: ",ex1.message!!)
        }
        catch (ex2:ClassNotFoundException){
            Log.e("Error: ",ex2.message!!)
        }
        return conn
    }
}