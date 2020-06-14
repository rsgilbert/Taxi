package com.lokech.taxi.journeylist

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordButton
import com.devlomi.record_view.RecordView
import com.lokech.taxi.R
import com.lokech.taxi.REQUEST_AUDIO_PERMISSION_CODE
import com.lokech.taxi.databinding.JourneysListBinding
import com.lokech.taxi.util.hasRecordingPermissions
import com.lokech.taxi.util.requestRecordingPermissions
import com.lokech.taxi.util.uploadAudioFile
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber
import java.io.File
import java.io.IOException


class JourneyListFragment : Fragment() {
    private val journeysViewModel: JourneysViewModel by viewModels()
    lateinit var recorder: MediaRecorder
    lateinit var player: MediaPlayer
    private lateinit var recordView: RecordView
    lateinit var recordFilePath: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val binding: JourneysListBinding = DataBindingUtil.inflate(
            inflater, R.layout.journeys_list, container, false
        )

        setRecordFilePath()
//         binding.also {
        binding.journeysViewModel = journeysViewModel
        recordView = binding.recordView
//        }
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            journeys.adapter = JourneysAdapter(itemListClickListener)
            fabNewJourney.setOnClickListener {
                findNavController().navigate(JourneyListFragmentDirections.actionJourneysFragmentToNewJourneyFragment())
            }
            fabPlayRecording.setOnClickListener {
                playRecording()
            }

            // setup recording
            val recordButton: RecordButton = binding.recordButton
            recordButton.setRecordView(recordView)
            if (hasRecordingPermissions) {
                recordView.setOnRecordListener(recordListener)
            } else {
                requestRecordingPermissions()
            }
        }
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_journeys, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> toast("Searching")
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (permissionToRecord) {
                    toast("Permission granted")
                    recordView.setOnRecordListener(recordListener)
                }
            }
        }
    }
}


val JourneyListFragment.itemListClickListener: JourneysAdapter.OnClickListener
    get() = JourneysAdapter.OnClickListener {
        val action = JourneyListFragmentDirections.actionJourneysFragmentToJourneyFragment(it.id)
        findNavController().navigate(action)
    }

fun JourneyListFragment.startRecording() {
    if (hasRecordingPermissions) {
        val recordFileName =
            "${requireActivity().cacheDir!!.absolutePath}/audiorecordtest.3gp"
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordFileName)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                longToast("Failed to prepare: $e")
                e.printStackTrace()
            }
            toast("Started")
        }
    } else requestRecordingPermissions()
}

fun JourneyListFragment.stopRecording() {
    recorder.apply {
        stop()
        release()
    }
}

fun JourneyListFragment.setRecordFilePath() {
    recordFilePath =
        "${requireActivity().cacheDir!!.absolutePath}/audiorecordtest.3gp"
}

fun JourneyListFragment.playRecording() {
    Timber.i("RecordFilename is $recordFilePath")
    longToast(recordFilePath)
    player = MediaPlayer().apply {
        try {
            setDataSource(recordFilePath)
            prepare()
            start()
        } catch (e: IOException) {
            toast("Error playing recording: $e")
            e.printStackTrace()
        }
    }
}

fun JourneyListFragment.stopPlaying() {
    player.release()
}

val JourneyListFragment.recordListener: OnRecordListener
    get() = object : OnRecordListener {
        override fun onStart() {
            startRecording()
        }

        override fun onCancel() {
            toast("Cancelled")
            stopRecording()
        }

        override fun onFinish(recordTime: Long) {
            try {
                val audioStream = File(recordFilePath).inputStream()
                uploadAudioFile(audioStream) {
                    longToast("Link is $it")
                }
            } catch (e: IOException) {
                toast("Failed to find file")
                e.printStackTrace()
            }
            stopRecording()
        }

        override fun onLessThanSecond() {}
    }