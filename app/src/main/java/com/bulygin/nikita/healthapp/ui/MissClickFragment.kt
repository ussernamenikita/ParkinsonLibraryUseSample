package com.bulygin.nikita.healthapp.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import com.bulygin.nikita.healthapp.R
import com.bulygin.nikita.healthapp.di.AppModule

class MissClickFragment : Fragment(), MissClickEventsConsumer, SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}

    private lateinit var module: AppModule

    private lateinit var missClickConsumer: MissClickEventsConsumer

    private lateinit var missClickResults: TextView

    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.inject()
    }

    private fun inject() {
        if (activity == null) {
            return
        }
        val a = activity as MainActivity
        module = a.module
        missClickConsumer = module.createMissClickErrorConsumer()
    }

    private lateinit var trackingViewGroup: TrackingViewGroup

    private lateinit var maxDistanceTv: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.rootView = inflater.inflate(R.layout.miss_click_fragment_layout, container, false)
        val btn = rootView.findViewById<Button>(R.id.button)
        trackingViewGroup = rootView.findViewById(R.id.tracking_view)
        val sw = rootView.findViewById<Switch>(R.id.miss_click_draw_bound_rect_sw)
        val seekBar = rootView.findViewById<SeekBar>(R.id.miss_click_max_difference_seek_bar)
        this.missClickResults = rootView.findViewById(R.id.tracjing_view_miss_click_count_tv)
        this.maxDistanceTv = rootView.findViewById(R.id.missclick_max_diff_tv)
        trackingViewGroup.addTrackingView(btn)
        trackingViewGroup.consumer = this
        sw.setOnCheckedChangeListener { _, isChecked ->
            trackingViewGroup.drawRect = isChecked
            trackingViewGroup.invalidate()
        }
        seekBar.setOnSeekBarChangeListener(this)
        updateMissClickCount(0)
        //btn.setOnClickListener { Schedulers.computation().createWorker().schedule { database.typingErrorDao().getAll().forEach { item -> println(item) } } }
        return rootView
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        trackingViewGroup.maxDistanceValue = progress.toDouble()
        maxDistanceTv.text = activity?.getString(R.string.miss_click_max_diff_label_text, progress)
        trackingViewGroup.invalidate()
    }

    override fun onConsume(timestamp: Long, distance: Double, missClickCount: Int) {
        updateMissClickCount(missClickCount)
        this.missClickConsumer.onConsume(timestamp, distance, missClickCount)
    }


    private fun updateMissClickCount(missClickCount: Int) {
        this.missClickResults.text = getString(R.string.miss_click_count, missClickCount)
    }


}