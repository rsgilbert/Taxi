package com.lokech.taxi.util

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.lokech.taxi.data.Journey
import com.lokech.taxi.data.Place
import com.lokech.taxi.data.Search
import com.lokech.taxi.data.places
import com.lokech.taxi.getNetworkService
import timber.log.Timber
import java.io.InputStream
import java.util.*


val db: FirebaseFirestore = Firebase.firestore

val journeyCollection: CollectionReference = db.collection("journeys")

val searchCollection: CollectionReference = db.collection("searches")

fun saveJourney(journey: Journey) {
    journeyCollection.document(journey.id)
        .set(journey, SetOptions.merge())
        .addOnSuccessListener { Timber.i("Saved journey: $journey") }
}

fun saveSearch(search: Search) {
    searchCollection.document(search.searchWord)
        .set(search, SetOptions.merge())
        .addOnSuccessListener { Timber.i("Saved search: $search") }
}


suspend fun performNewSearch(searchWord: String) {
    val places: List<Place> = getNetworkService().searchPlaces(searchWord).places
    val search = Search(searchWord = searchWord, places = places)
    saveSearch(search)
}


fun uploadPicture(
    stream: InputStream,
    onUpload: (pictureUrl: String) -> Unit
) {
    val storage = Firebase.storage
    val name = Date().time.toString()
    val uploadRef: StorageReference = storage.reference.child("images").child(name)
    uploadRef
        .putStream(stream)
        .continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            uploadRef.downloadUrl
        }
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onUpload(task.result.toString())
            }
        }
}
