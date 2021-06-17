package mx.tecnm.tepic.ladm_u5_practica1_mapatec

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var baseRemota = FirebaseFirestore.getInstance()
    var posicion = ArrayList<Data>()
    var edificios = ArrayList<String>()
    lateinit var locacion : LocationManager

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //PERMISO PARA USAR LA POSICION ACTUAL
        if (ActivityCompat.checkSelfPermission(
                this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
        }

        //OBTENER TODAS LAS COORDENADAS DE LA BD
        button.setOnClickListener {
            obtenerEdificios()
        }

        //OBTENER POSICION ACTUAL
        locacion = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var oyente = Oyente(this)
        locacion.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,01f,oyente)

    }

    private fun obtenerEdificios() {
        baseRemota.collection("tecnologico").addSnapshotListener { querySnapshot, error ->
            if (error != null){
                textView2.setText("ERROR: "+error.message)
                return@addSnapshotListener
            }

            var resultado = ""
            posicion.clear()
            edificios.clear()
            for (document in querySnapshot!!){
                var data = Data()
                data.nombre = document.getString("nombre").toString()
                data.pos1 = document.getGeoPoint("pos1")!!
                data.pos2 = document.getGeoPoint("pos2")!!

                resultado += data.toString()+"\n\n"
                posicion.add(data)
                edificios.add(data.nombre)
            }
            //println(edificios)
            //println(posicion)
        }
        listaedificios.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, edificios)
        listaedificios.setOnItemClickListener { parent, view, position, id ->
            dialogoOpciones(position)
        }
    }

    private fun dialogoOpciones(position: Int) {
        AlertDialog.Builder(this).setTitle("ATENCION!!")
            .setMessage("QUE DESEAS HACER CON \n ${edificios.get(position)}?")
            .setPositiveButton("VER EN MAPA"){d, i->
                var nombre = posicion.get(position).nombre
                var puntoCentrado = posicion.get(position).centrar()
                var intent =Intent(this,MapaTec::class.java)

                //PASAR EXTRAS
                intent.putExtra("latEdi",puntoCentrado.latitude)
                intent.putExtra("longEdi",puntoCentrado.longitude)
                intent.putExtra("latPos",latituduser.text.toString().toDouble())
                intent.putExtra("longPos",longituduser.text.toString().toDouble())
                intent.putExtra("nombre",nombre)
                startActivity(intent)
            }
            .setNegativeButton("CANCELAR"){d,i->}
            .show()
    }
}

class Oyente(puntero:MainActivity) : LocationListener {

    var p = puntero

    override fun onLocationChanged(location: Location) {
        var geoPosicionGPS = GeoPoint(location.latitude,location.longitude)
        p.latituduser.setText("${location.latitude}")
        p.longituduser.setText("${location.longitude}")
        p.edificiouser.setText("")

        for (item in p.posicion){
            if (item.estoyEn(geoPosicionGPS)){
                p.edificiouser.setText("ESTAS EN ${item.nombre}")
            }
        }
    }

    override fun onProviderDisabled(provider: String) {
        super.onProviderDisabled(provider)
    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
    }
}