package com.example.recycleviewpart10.model

import android.os.Parcel
import android.os.Parcelable

class Todo (
    var isCheck:Boolean = false,
    var time: String?,
    var title:String?,
    var desc:String?
        ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor() : this(false,"","","")


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(isCheck)
        parcel.writeString(time)
        parcel.writeString(title)
        parcel.writeString(desc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Todo> {
        override fun createFromParcel(parcel: Parcel): Todo {
            return Todo(parcel)
        }

        override fun newArray(size: Int): Array<Todo?> {
            return arrayOfNulls(size)
        }
    }
}

