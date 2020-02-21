package ca.csf.mobile2.tp1.weather

import android.os.Parcel
import android.os.Parcelable

enum class WeatherType {
    SUNNY,
    PARTLY,
    CLOUDLY,
    RAIN,
    SNOW
}


class Weather(val weatherType: WeatherType, val temperature: Int, val city: String): Parcelable {
    constructor(parcel: Parcel) : this(
        WeatherType.valueOf(parcel.readString()),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.weatherType.name)
        parcel.writeInt(temperature)
        parcel.writeString(city)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Weather> {
        override fun createFromParcel(parcel: Parcel): Weather {
            return Weather(parcel)
        }

        override fun newArray(size: Int): Array<Weather?> {
            return arrayOfNulls(size)
        }
    }

}