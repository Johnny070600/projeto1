package com.example.projeto

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.projeto.api.EndPoints
import com.example.projeto.api.ServiceBuilder
import com.example.projeto.api.User
import com.example.projeto.api.problemas
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Response
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import javax.security.auth.callback.Callback

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var problems:List<problemas>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1 // add implement location periodic updates
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getProblemas()
        var position: LatLng

        call.enqueue(object : retrofit2.Callback<List<problemas>>{
            override fun onResponse(call: Call<List<problemas>>, response: Response<List<problemas>>){
                if(response.isSuccessful){
                    problems = response.body()!!
                    for(problem in problems) {
                        position = LatLng(problem.latitude.toString().toDouble(),
                        problem.longitude.toString().toDouble())
                        mMap.addMarker(MarkerOptions().position(position).title(problem.descr))
                    }
                }
            }
            override fun onFailure(call:Call<List<problemas>>, t:Throwable){
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setUpMap()
    }

    fun setUpMap() {
        fun setUpMap(){
            if(ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                return
            } else{
                // 1
                mMap.isMyLocationEnabled = true

                // 2
                fusedLocationClient.lastLocation.addOnSuccessListener(this){location ->
                    //Got last Known location. In some rare situation this can be null.
                    //3
                    if(location != null) {
                        lastLocation = location
                        Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    }
                }
            }
        }
    }
}