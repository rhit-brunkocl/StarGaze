package edu.rosehulman.stargaze.ui.Camera

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import edu.rosehulman.stargaze.Constants

class SensorFusionManager(val parent: CameraFragment, val controller: PointerController) {
    val sensorManager = parent.requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val gravitySensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
    val gyroSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    val gravitySensorListener = SensorCallback(SensorType.GRAVITY.ordinal)
    val gyroSensorListener = SensorCallback(SensorType.GYRO.ordinal)

    fun vectorLength(vec: FloatArray) : Float {
        return Math.sqrt((vec[0] * vec[0] + vec[1] * vec[1] + vec[2] * vec[2]).toDouble()).toFloat()
    }
    fun pushUpdate(event: SensorEvent?, type: Int){
        if(type == SensorType.GYRO.ordinal){
            val rq = FloatArray(4)
            SensorManager.getQuaternionFromVector(rq, event!!.values)
            Log.d(Constants.TAG, "${event!!.values[0]} : ${event!!.values[1]} : ${event!!.values[2]}")
            controller.updatePointer(rq)
        }
    }

    fun register(){
        //sensorManager.registerListener(gravitySensorListener, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
        sensorManager.registerListener(gyroSensorListener, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregister(){
        //sensorManager.unregisterListener(gravitySensorListener)
        sensorManager.unregisterListener(gyroSensorListener)
    }

    companion object{
        enum class SensorType {
            GRAVITY,
            GYRO
        }
    }

    inner class SensorCallback(val Type: Int) : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            //Log.d(Constants.TAG, "sensorUpdate")
            pushUpdate(event, Type)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

        }


    }
}