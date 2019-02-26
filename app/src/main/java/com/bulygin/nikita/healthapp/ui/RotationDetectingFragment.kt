package com.bulygin.nikita.healthapp.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.domain.IRotationDetectorRepository
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import java.util.*

class RotationDetectingFragment : Fragment() {

    lateinit var xValueTv: TextView
    lateinit var yValueTv: TextView
    lateinit var zValueTv: TextView
    lateinit var mContext: Context
    lateinit var rotationRepo: IRotationDetectorRepository
    lateinit var uiScheduler: Scheduler

    fun inject() {
        mContext = context!!
        val module = (activity as MainActivity).module
        this.rotationRepo = module.getRotationDetectorRepo()
        uiScheduler = module.getUIScheduler()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    private var subscription: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.rotation_detecting_fragment, container, false)
        xValueTv = rootView.findViewById(R.id.rotation_detecting_x_tv)
        yValueTv = rootView.findViewById(R.id.rotation_detecting_y_tv)
        zValueTv = rootView.findViewById(R.id.rotation_detecting_z)
        updateValues(0.0, 0.0, 0.0)
        subscription?.dispose()
        subscription = rotationRepo.getOrientation().subscribeOn(uiScheduler).subscribe({

            updateValues(it[0].toDouble(), it[1].toDouble(), it[2].toDouble())
        }, {
            it.printStackTrace()
        })
        return rootView
    }

    private fun updateValues(x: Double, y: Double, z: Double) {
        xValueTv.text = mContext.getString(R.string.rotation_x_value, toString(x))
        yValueTv.text = mContext.getString(R.string.rotation_y_value, toString(y))
        zValueTv.text = mContext.getString(R.string.rotation_z_value, toString(z))
    }

    private fun toString(value: Double): String = String.format(Locale.ENGLISH, "%.2f", value)


    override fun onDestroyView() {
        super.onDestroyView()
        subscription?.dispose()
    }
}