package mx.tecnm.tepic.ladm_u5_practica1_mapatec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapaTec : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    //EDIFICIOS
    var nombre = ""
    var posEdiLat : Double = 0.0
    var posEdiLong : Double = 0.0
    //PERSONA
    var latUser : Double = 0.0
    var longUser : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapa_tec)
        //OBTENER LOS PUNTOS
        var extras = intent.extras
        //EDIFICIOS
        nombre = extras!!.getString("nombre")!!
        posEdiLat = extras!!.getDouble("latEdi")
        posEdiLong = extras!!.getDouble("longEdi")
        //PERSONA
        latUser = extras!!.getDouble("latPos")
        longUser = extras!!.getDouble("longPos")


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val edificio = LatLng(posEdiLat,posEdiLong)
        val posicionActual = LatLng(latUser,longUser)
        mMap.addMarker(MarkerOptions().position(edificio).title(nombre))
        mMap.addMarker(MarkerOptions().position(posicionActual).title("TU POSICION ACTUAL"))
        var zoomLevel = 14f
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(edificio,zoomLevel))
    }
}