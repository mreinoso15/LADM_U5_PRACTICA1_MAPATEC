package mx.tecnm.tepic.ladm_u5_practica1_mapatec

import com.google.firebase.firestore.GeoPoint

class Data {


    var nombre : String = ""
    var pos1 : GeoPoint = GeoPoint(0.0,0.0)
    var pos2 : GeoPoint = GeoPoint(0.0,0.0)

    override fun toString(): String {

        return nombre + "\n" +
               pos1.latitude + " , " + pos1.longitude + "\n" +
               pos2.latitude +" , "+pos2.longitude
    }

    fun centrar() : GeoPoint{
        var latitudCentral = (pos1.latitude + pos2.latitude)/2
        var longitudCentral = (pos1.longitude + pos2.longitude)/2
        return GeoPoint(latitudCentral,longitudCentral)
    }

    fun estoyEn(posicionActual:GeoPoint):Boolean{
        if (posicionActual.latitude >= pos1.latitude &&
            posicionActual.latitude <= pos2.latitude){
            if (invertir(posicionActual.longitude) >= invertir(pos1.longitude) &&
                invertir(posicionActual.longitude) <= invertir((pos2.longitude))){
                return true
            }
        }
        return false
    }

    private fun invertir(valor:Double):Double{
        return valor * -1
    }

}