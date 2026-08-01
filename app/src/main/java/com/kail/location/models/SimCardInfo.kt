package com.kail.location.models

import android.os.Parcel
import android.os.Parcelable

/**
 * SIM卡信息模型
 * 对应Nrfr的SimCardInfo
 */
data class SimCardInfo(
    val slot: Int,              // SIM卡槽位 (0或1)
    val subId: Int,             // 订阅ID
    val carrierName: String,   // 运营商名称
    val currentConfig: Map<String, String> = emptyMap()  // 当前配置
) : Parcelable {
    
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        @Suppress("UNCHECKED_CAST")
        (parcel.readHashMap(String::class.java.classLoader) as? Map<String, String>) ?: emptyMap()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(slot)
        parcel.writeInt(subId)
        parcel.writeString(carrierName)
        parcel.writeMap(currentConfig)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SimCardInfo> {
        override fun createFromParcel(parcel: Parcel): SimCardInfo = SimCardInfo(parcel)
        override fun newArray(size: Int): Array<SimCardInfo?> = arrayOfNulls(size)
    }
}
