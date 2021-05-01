package com.nauka.timepad

import com.google.firebase.firestore.Blob
import java.sql.Time

class TimeResponse(
    var name: String = "",
    var tag: String = "",
    var time: String = ""
)