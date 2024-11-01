package models

import kotlinx.serialization.Serializable

@Serializable
data class Department(val id: Int, val name: String){
    override fun toString(): String = name
}
