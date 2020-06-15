package com.lokech.taxi.newjourney

import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordButton
import com.devlomi.record_view.RecordView
import com.lokech.taxi.MapFragment
import com.lokech.taxi.R
import com.lokech.taxi.moveCameraToCurrentLocation
import com.lokech.taxi.util.hasRecordingPermissions
import com.lokech.taxi.util.requestRecordingPermissions
import com.lokech.taxi.util.uploadAudioFile
import com.mancj.materialsearchbar.MaterialSearchBar
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import timber.log.Timber
import java.io.File
import java.io.IOException

abstract class NewJourneyMapFragment : MapFragment() {
    val newJourneyViewModel: NewJourneyViewModel by viewModels(
        { requireParentFragment() }
    )

    lateinit var searchBar: MaterialSearchBar
    lateinit var recorder: MediaRecorder
    lateinit var player: MediaPlayer
    private lateinit var recordView: RecordView
    lateinit var recordFilePath: String

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        moveCameraToCurrentLocation()
        initializeSearchBar()
        observeSuggestions()
        observePlace()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recordView = view.findViewById(R.id.record_view)
        val recordButton: RecordButton = view.findViewById(R.id.record_button)
        recordButton.setRecordView(recordView)
        if (hasRecordingPermissions) {
            recordView.setOnRecordListener(recordListener)
        } else {
            requestRecordingPermissions()
        }
        setRecordFilePath()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_new_journey, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> openSearchBar()
            R.id.menu_play -> playRecording()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onRequestPermissionsGranted() {
        recordView.setOnRecordListener(recordListener)
    }

    override fun getLayout(): Int = R.layout.fragment_new_journey_map
    abstract fun observePlace()
    abstract fun setAudioUrl(audioUrl: String)
    abstract fun getAudioUrl(): String
    abstract val suggestionClickListener: PlaceSuggestionsAdapter.OnClickListener
    abstract fun getSearchBarResourceId(): Int

}


fun NewJourneyMapFragment.observeSuggestions() {
    newJourneyViewModel.suggestions.observe(this) {
        searchBar.updateLastSuggestions(it)
    }
}

fun NewJourneyMapFragment.hideSearchBar() {
    searchBar.visibility = View.GONE
}

fun NewJourneyMapFragment.openSearchBar() {
    searchBar.apply {
        visibility = View.VISIBLE
        openSearch()
    }
}

fun NewJourneyMapFragment.initializeSearchBar() {
    val inflater = requireActivity().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val placeSuggestionsAdapter = PlaceSuggestionsAdapter(inflater, suggestionClickListener)
    searchBar = requireActivity().findViewById(getSearchBarResourceId())
    searchBar.run {
        setOnSearchActionListener(searchActionListener)
        addTextChangeListener(textWatcher)
        setCustomSuggestionAdapter(placeSuggestionsAdapter)
        setCardViewElevation(20)
    }
}

fun NewJourneyMapFragment.setSearchWord(query: CharSequence?) {
    if (!query.isNullOrBlank()) newJourneyViewModel.setSearchWord(query.toString())
}

val NewJourneyMapFragment.searchActionListener: MaterialSearchBar.OnSearchActionListener
    get() = object : MaterialSearchBar.OnSearchActionListener {
        override fun onSearchStateChanged(enabled: Boolean) {
            if (!enabled) hideSearchBar()
        }

        override fun onButtonClicked(buttonCode: Int) {}
        override fun onSearchConfirmed(text: CharSequence?) {}
    }

val NewJourneyMapFragment.textWatcher: TextWatcher
    get() = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setSearchWord(s)
        }

        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    }


fun NewJourneyMapFragment.startRecording() {
    if (hasRecordingPermissions) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(recordFilePath)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                longToast("No audio present")
                e.printStackTrace()
            }
        }
    } else requestRecordingPermissions()
}

fun NewJourneyMapFragment.stopRecording() {
    recorder.apply {
        stop()
        release()
    }
}

fun NewJourneyMapFragment.setRecordFilePath() {
    recordFilePath =
        "${requireActivity().cacheDir!!.absolutePath}/audiorecord.3gp"
}

fun NewJourneyMapFragment.playRecording() {
    player = MediaPlayer().apply {
        try {
            Timber.i("Audio path is ${getAudioUrl()}")
            setDataSource(getAudioUrl())
            prepare()
            start()
        } catch (e: IOException) {
            toast("No audio recorded")
            e.printStackTrace()
        }
    }
}

val NewJourneyMapFragment.recordListener: OnRecordListener
    get() = object : OnRecordListener {
        override fun onStart() {
            startRecording()
        }

        override fun onCancel() {
            stopRecording()
        }

        override fun onFinish(recordTime: Long) {
            stopRecording()
            try {
                val audioStream = File(recordFilePath).inputStream()
                uploadAudioFile(audioStream) {
                    toast("Audio recorded")
                    setAudioUrl(it)
                }
            } catch (e: IOException) {
                toast("Failed to find audio")
                e.printStackTrace()
            }
        }

        override fun onLessThanSecond() {}
    }