package br.com.phs.lockscreenimplementation

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var deviceManger: DevicePolicyManager
    private lateinit var compName: ComponentName
    private lateinit var btnEnable: Button
    private lateinit var btnLock: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.btnEnable = findViewById(R.id.btnEnable)
        this.btnLock = findViewById(R.id.btnLock)

        this.deviceManger = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        this.compName = ComponentName(this, DeviceAdmin::class.java)
        val active = deviceManger.isAdminActive(compName)
        if (active) {
            this.btnEnable.text = "Disable"
            this.btnLock.visibility = View.VISIBLE
        } else {
            btnEnable.text = "Enable"
            btnLock.visibility = View. GONE
        }

    }

    fun enablePhone(view: View) {
        val active = deviceManger.isAdminActive(compName)
        if (active) {
            deviceManger.removeActiveAdmin(compName)
            btnEnable.text = "Enable"
            btnLock.visibility = View.GONE
        } else {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!")
            resultLauncher.launch(intent)
        }
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            btnEnable.text = "Disable"
            btnLock.visibility = View. VISIBLE
        } else {
            Toast.makeText (applicationContext, "Failed!" , Toast. LENGTH_SHORT ).show()
        }
    }

    fun lockPhone(view: View) {
        this.deviceManger.lockNow()
    }

}