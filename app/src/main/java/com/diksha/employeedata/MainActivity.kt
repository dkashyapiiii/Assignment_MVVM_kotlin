package com.diksha.employeedata

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.diksha.employeedata.Adapter.EmployeeAdapter
import com.diksha.employeedata.Dao.EmployeeDao
import com.diksha.employeedata.Database.EmployeeDatabase
import com.diksha.employeedata.Modal.Employee
import com.diksha.employeedata.ModelClass.EmployeeModel
import com.diksha.employeedata.ModelClass.Maindata
import com.diksha.employeedata.Network.Retrofit
import com.diksha.employeedata.Repository.EmployeeRespository
import com.diksha.employeedata.ViewModal.ActorViewModal
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var actorViewModal: ActorViewModal? = null
    private var recyclerView: RecyclerView? = null
    private var actorList: List<Employee>? = null
    private var actorRespository: EmployeeRespository? = null
    private var actorAdapter: EmployeeAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var jsonb: Button? = null
    var permission: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler)
        jsonb = findViewById(R.id.jsonfile)
        mSwipeRefreshLayout = findViewById(R.id.pullToRefresh)
        actorRespository = EmployeeRespository(application)
        actorList = ArrayList()
        actorViewModal = ViewModelProvider(this).get(ActorViewModal::class.java)

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)

        if (isNetworkConnected) {
            networkRequest()
        } else {
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show()
        }

        jsonb!!.setOnClickListener {   if(permission==false)
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        else
            showFileChooser()
        }


        mSwipeRefreshLayout!!.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({
                if (mSwipeRefreshLayout!!.isRefreshing) {
                    if (isNetworkConnected) {
                       // responseRequest()
                        //addadptor()
                            networkRequest()
                        mSwipeRefreshLayout!!.isRefreshing = true
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Please check your connection",
                            Toast.LENGTH_SHORT
                        ).show()
                        mSwipeRefreshLayout!!.isRefreshing = false
                    }
                }
            }, 1000)
        }
        actorViewModal!!.allActor.observe(this, { actorList ->
            recyclerView!!.adapter = actorAdapter
            actorAdapter!!.getAllMyModels(actorList)
            Log.d("main", "onChanged: $actorList")
        })

        addadptor()

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                permission=true
                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
                permission=false
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkPermission(readExternalStorage: String, storagePermissionCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                readExternalStorage
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(readExternalStorage),
                storagePermissionCode
            )
        } else {

//            Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT)
//                .show()
        }
    }

    private fun addadptor() {

        var layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        actorList = ArrayList()
        actorAdapter = EmployeeAdapter(this, actorList as ArrayList<Employee>)
        actorAdapter!!.notifyDataSetChanged()
     //   actorViewModal = ViewModelProvider(this).get(ActorViewModal::class.java)
    }



    private fun networkRequest() {
        val retrofit = Retrofit()
        val call = retrofit.api.allActors
        if (call != null) {
            call.enqueue(object : Callback<EmployeeModel?> {
                override fun onResponse(
                    call: Call<EmployeeModel?>,
                    response: Response<EmployeeModel?>
                ) {
                    if (response.isSuccessful) {
                        mSwipeRefreshLayout!!.isRefreshing = false


                        actorRespository!!.insert(response.body()!!.banner1?.let {
                            savetoparentmodel(
                                it
                            )
                        })
                        actorViewModal!!.allActor.observe(this@MainActivity, { actorList ->
                            recyclerView!!.adapter = actorAdapter
                            actorAdapter!!.getAllMyModels(actorList)
                            Log.d("main", "onChanged: $actorList")
                        })

                        addadptor()

                        Log.d("main", "onResponse: " + response.body())
                    }
                }

                override fun onFailure(call: Call<EmployeeModel?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }


    private fun savetoparentmodel(employeeModeldata: List<Maindata>): List<Employee> {
        val data: MutableList<Employee> = ArrayList()

        for (i in employeeModeldata.indices) {
            val ld = Employee(
                employeeModeldata[i].firstname,
                employeeModeldata[i].lastname,
                employeeModeldata[i].age,
                employeeModeldata[i].gender,
                employeeModeldata[i].picture,
                employeeModeldata[i].jobholder?.exp,
                employeeModeldata[i].jobholder?.organization,
                employeeModeldata[i].jobholder?.role,
                employeeModeldata[i].education?.degree,
                employeeModeldata[i].education?.institution
//                        employeeModeldata[0].firstname,
//                employeeModeldata[0].lastname,
//                employeeModeldata[0].age,
//                employeeModeldata[0].gender,
//                employeeModeldata[0].picture,
//                employeeModeldata[0].jobholder?.exp,
//                employeeModeldata[0].jobholder?.organization,
//                employeeModeldata[0].jobholder?.role,
//                employeeModeldata[0].education?.degree,
//                employeeModeldata[0].education?.institution
            )
            data.add(ld)
        }
        Log.d("data", data.toString())
        return data
    }


    private val isNetworkConnected: Boolean
        private get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

    private fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                FILE_SELECT_CODE
            )
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this, "Please install a File Manager.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == RESULT_OK && data!!.data != null) {
                try {
                    var name :String ? =""
                    val uri = data.data
                    Log.d("data", uri.toString());
                    data.data?.let { returnUri ->
                        contentResolver.query(returnUri, null, null, null, null)
                    }?.use { cursor ->

                        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                        cursor.moveToFirst()
                        name=cursor.getString(nameIndex)
                    }
                    if(name!!.endsWith(".json")){

                        Parsetest(readTextFromUri(uri!!))
                        Toast.makeText(this,  "Data Populated Successfully!!", Toast.LENGTH_LONG).show()

                    }else{

                        Toast.makeText(this,  "Unsupported file format. Please select a .json file", Toast.LENGTH_LONG).show()
                    }

                } catch (e: FileNotFoundException) {
                    Log.d("error", e.message.toString());
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }catch (e :IOException){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }



    @Throws(IOException::class)
    private fun readTextFromUri(uri: Uri): String? {
        val stringBuilder = java.lang.StringBuilder()
        contentResolver.openInputStream(uri).use { inputStream ->
            BufferedReader(
                InputStreamReader(Objects.requireNonNull(inputStream))
            ).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
            }
        }
        return stringBuilder.toString()
    }

    private fun Parsetest(text: String?) {
        val employeeModel = Gson().fromJson(text, EmployeeModel::class.java)
        actorAdapter = EmployeeAdapter(this@MainActivity, savetoparentmodel(employeeModel.banner1!!))
        recyclerView!!.adapter = actorAdapter
        actorAdapter!!.notifyDataSetChanged()
        mSwipeRefreshLayout!!.isRefreshing = false
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
        private const val FILE_SELECT_CODE = 0
    }
}

