@file:JvmName("Extensions") //Esto es para poder llamar a este fichero desde java con el nombre Extensions.
package com.blank.dao.example.kotlin.config

import android.view.LayoutInflater
import android.view.ViewGroup

fun ViewGroup.inflate(resourceId : Int, attachToRoot : Boolean = false)
        = LayoutInflater.from(context).inflate(resourceId, this, attachToRoot)