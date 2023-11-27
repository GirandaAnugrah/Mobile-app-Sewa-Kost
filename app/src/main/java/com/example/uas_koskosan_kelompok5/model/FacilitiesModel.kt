package com.example.uas_koskosan_kelompok5.model

data class FacilitiesModel(
    val electricity: Boolean?,
    val bed: Boolean?,
    val desk: Boolean?,
    val cupboard: Boolean?,
    val pillow: Boolean?,
    val chair: Boolean?,
){
    constructor() : this(
        electricity = false,
        bed = false,
        desk = false,
        cupboard = false,
        pillow = false,
        chair = false
    )
}
