package com.diksha.employeedata

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.diksha.employeedata.Adapter.EmployeeAdapter
import com.diksha.employeedata.Modal.Employee
import com.diksha.employeedata.ModelClass.EmployeeModel
import com.diksha.employeedata.ModelClass.Maindata
import com.diksha.employeedata.Network.Retrofit
import com.diksha.employeedata.Repository.ActorRespository
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
    private var actorRespository: ActorRespository? = null
    private var actorAdapter: EmployeeAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var jsonb: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initilization()

        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        if (isNetworkConnected) {
            networkRequest()
        } else {
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show()
        }

        jsonb!!.setOnClickListener { showFileChooser() }

        mSwipeRefreshLayout!!.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({
                if (mSwipeRefreshLayout!!.isRefreshing) {
                    if (isNetworkConnected) {
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
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_SHORT)
                    .show()
            } else {
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

    private fun initilization() {
        recyclerView = findViewById(R.id.recycler)
        jsonb = findViewById(R.id.jsonfile)
        mSwipeRefreshLayout = findViewById(R.id.pullToRefresh)
        var layoutManager = LinearLayoutManager(this@MainActivity)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        actorRespository = ActorRespository(application)
        actorList = ArrayList()
        actorAdapter = EmployeeAdapter(this, actorList as ArrayList<Employee>)
        actorViewModal = ViewModelProvider(this).get(ActorViewModal::class.java)
    }

    private fun networkRequest() {
        val retrofit = Retrofit()
        val call = retrofit.api.allActors
        if (call != null) {
            call.enqueue(object : Callback<EmployeeModel?> {
                override fun onResponse(call: Call<EmployeeModel?>, response: Response<EmployeeModel?>) {
                    if (response.isSuccessful) {
                        mSwipeRefreshLayout!!.isRefreshing = false
                        actorRespository!!.insert(response.body()!!.banner1?.let { savetoparentmodel(it) })
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

    fun getRealPathFromURI(context: MainActivity, contentUri: Uri?): String {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            FILE_SELECT_CODE -> if (resultCode == RESULT_OK && data!!.data != null) {
                val uri = data.data
                val path = getRealPathFromURI(this, uri)
                ReadFile(path)
                Toast.makeText(this, "Selected file path is:  $path", Toast.LENGTH_SHORT).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun ReadFile(path: String?) {
        mSwipeRefreshLayout!!.isRefreshing = true
        val gson = Gson()
        var text = ""
        try {
            val yourFile = File(path)
            val inputStream: InputStream = FileInputStream(yourFile)
            val stringBuilder = StringBuilder()
            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                var receiveString: String? = ""
                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append(receiveString)
                }
                inputStream.close()
                text = stringBuilder.toString()
                Parsetest(text)
                Log.d("TAG", text)
            }
        } catch (e: FileNotFoundException) {
            Log.e("path", e.message!!)
        } catch (e: IOException) {
            Log.e("path", e.message!!)
        }
    }

    private fun Parsetest(text: String) {
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


