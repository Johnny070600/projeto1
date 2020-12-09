package com.example.projeto

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.projeto.api.EndPoints
import com.example.projeto.api.ServiceBuilder
import com.example.projeto.api.User
import com.example.projeto.api.problemas
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Response
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import javax.security.auth.callback.Callback

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener
{

    private lateinit var mMap: GoogleMap
    private lateinit var problems:List<problemas>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private lateinit var lat : String
    private lateinit var lng : String
    private var userid : Int = 0
    private var idsharedpreferences : Int = 0
    private lateinit var nomesharedpreference : String
    private var continenteLat : Double = 0.0
    private var continenteLong : Double = 0.0

    ////SENSORES////
    var sensor : Sensor? = null
    var sensorManager : SensorManager? = null

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ////SENSORES////
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

        //Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        userid = intent.getIntExtra("userid",0)

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



        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                var token2 = getSharedPreferences("username", Context.MODE_PRIVATE)
                nomesharedpreference = token2.getString("username_login_atual"," ").toString()

                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                lat = loc.latitude.toString()
                lng = loc.longitude.toString()


                findViewById<TextView>(R.id.txtcoordenadas).setText("Lat: " + loc.latitude + " - Long: " + loc.longitude)
                Log.d("** Joao", "new location received - " + loc.latitude + " -" + loc.longitude)

            }
        }
        createLocationRequest()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        setUpMap()
    }

    fun setUpMap() {
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

                    if(location != null) {
                        lastLocation = location
                        Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    }
                }
            }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d(" Joao", "onPause - removeLocationUpdates")
    }

    public override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL)
        startLocationUpdates()
        Log.d(" Joao", "onResume - startLocationUpdates")
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.login_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.logout -> {
                var token = getSharedPreferences("nome", Context.MODE_PRIVATE)
                var editor = token.edit()
                editor.putString("nome_login_atual"," ")
                editor.commit()
                val intent = Intent(this@MapsActivity, Login::class.java)
                startActivity(intent)
                true
            }
            R.id.addpoint -> {

                val intent2 = Intent(this, ActivityAddPoint::class.java)
                intent2.putExtra("latitude",lat)
                intent2.putExtra("longitude", lng)
                intent2.putExtra("userid",userid)
                startActivity(intent2)
                true
            }
            R.id.removepoint -> {
                val intent3 = Intent(this, RemoveActivity::class.java)
                startActivity(intent3)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        try {

            if (event!!.values[0] < 30) {


                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    val success = mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.mapstyle))
                    if (!success) {
                        Log.e("MapsActivity", "Style parsing failed.")
                    }
                } catch (e: Resources.NotFoundException) {
                    Log.e("MapsActivity", "Can't find style. Error: ", e)
                }



            } else {

                findViewById<FrameLayout>(R.id.map).visibility = View.VISIBLE
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    val success = mMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.mapstyle2))
                    if (!success) {
                        Log.e("MapsActivity", "Style parsing failed.")
                    }
                } catch (e: Resources.NotFoundException) {
                    Log.e("MapsActivity", "Can't find style. Error: ", e)
                }

            }

        }
        catch (e : Exception)
        {

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}