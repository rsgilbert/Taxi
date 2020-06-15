package com.lokech.taxi.newjourney

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import com.lokech.taxi.data.Direction
import com.lokech.taxi.data.Journey
import com.lokech.taxi.data.Place
import com.lokech.taxi.data.Search
import com.lokech.taxi.getNetworkService
import com.lokech.taxi.util.*
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.*

class NewJourneyViewModel : ViewModel() {

    val searchWord = MutableLiveData<String>()

    val newJourneyLiveDate = MutableLiveData<Journey>().apply { value = Journey() }

    val navigateToJourneyLiveData = MutableLiveData<String>()

    val time = MutableLiveData<String>()

    val date = MutableLiveData<String>()

    val suggestions = MutableLiveData<List<Place>>()

    val startPlace = MutableLiveData<Place>()

    val endPlace = MutableLiveData<Place>()

    init {
        setSuggestions()
        setDateTime()
    }
}

fun NewJourneyViewModel.setDate(year: Int, month: Int, day: Int) {
    newJourneyLiveDate.value = newJourneyLiveDate.value!!.copy(
        dateTimeMillis = LocalDateTime.of(
            LocalDate.of(year, month, day),
            newJourneyLiveDate.value!!.getDateTime().toLocalTime()
        ).millis
    )
}

fun NewJourneyViewModel.setTime(hour: Int, minute: Int) {
    newJourneyLiveDate.value = newJourneyLiveDate.value!!.copy(
        dateTimeMillis = LocalDateTime.of(
            newJourneyLiveDate.value!!.getDateTime().toLocalDate(), LocalTime.of(hour, minute)
        ).millis
    )
}

fun NewJourneyViewModel.startNavigateToJourney(id: String) {
    navigateToJourneyLiveData.value = id
}

fun NewJourneyViewModel.completeNavigateToJourney() {
    navigateToJourneyLiveData.value = null
}

fun NewJourneyViewModel.setStartPlace(place: Place) {
    startPlace.value = place
}

fun NewJourneyViewModel.setEndPlace(place: Place) {
    endPlace.value = place
}

fun NewJourneyViewModel.setStartAudioUrl(audioUrl: String) {
    newJourneyLiveDate.value = newJourneyLiveDate.value!!.copy(startAudioUrl = audioUrl)
}

fun NewJourneyViewModel.setEndAudioUrl(audioUrl: String) {
    newJourneyLiveDate.value = newJourneyLiveDate.value!!.copy(endAudioUrl = audioUrl)
}

fun NewJourneyViewModel.setSearchWord(placeName: String) {
    searchWord.value = placeName
}

fun NewJourneyViewModel.postJourney(charge: Long, vehicle: String) {
    startPlace.value?.let { startPlace ->
        endPlace.value?.let { endPlace ->
            viewModelScope.launch {
                val direction: Direction? = try {
                    getNetworkService().requestDirection(
                        origin = flatLatLng(startPlace.latitude, startPlace.longitude),
                        destination = flatLatLng(endPlace.latitude, endPlace.longitude)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                direction?.let {
                    val journey = newJourneyLiveDate.value!!.copy(
                        startLatitude = direction.startLatitude,
                        startLongitude = direction.startLongitude,
                        startAddress = startPlace.address,
                        endLatitude = direction.endLatitude,
                        endLongitude = direction.endLongitude,
                        endAddress = endPlace.address,
                        neBoundLatitude = direction.bounds.northeast.lat,
                        neBoundLongitude = direction.bounds.northeast.lng,
                        swBoundLatitude = direction.bounds.southwest.lat,
                        swBoundLongitude = direction.bounds.southwest.lng,
                        charge = charge,
                        vehicle = vehicle,
                        picture = startPlace.icon,
                        line = direction.line,
                        duration = direction.duration,
                        distance = direction.distance
                    )
                    saveJourney(journey)
                    startNavigateToJourney(journey.id)
                }
            }
        }
    }
}

fun NewJourneyViewModel.setDateTime() {
    newJourneyLiveDate.observeForever {
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)
        time.value = timeFormatter.format(it.getDateTime())
        val dateFormatter = DateTimeFormatter.ofPattern("EEEE dd MMMM, yyyy", Locale.US)
        date.value = dateFormatter.format(it.getDateTime())
        Timber.i("New time is ${time.value} and New date is ${date.value}")
    }
}

fun NewJourneyViewModel.setSuggestions() {
    searchWord.observeForever { searchWord: String ->
        Timber.i("New searchword is $searchWord")
        searchCollection
            .document(searchWord)
            .addSnapshotListener { snapshot, e ->
                e?.let {
                    Timber.e("Error listening: $e")
                    return@addSnapshotListener
                }
                snapshot?.let {
                    if (it.exists()) {
                        suggestions.value = it.toObject<Search>()?.places
                        Timber.i("Suggestion is ${suggestions.value}")
                    } else viewModelScope.launch {
                        try {
                            performNewSearch(searchWord)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                }
            }
    }
}


