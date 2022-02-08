package edu.rosehulman.stargaze.models

data class Setting(var parallax: Boolean = true, var harshaw: Boolean = true, var GAIA: Boolean = true, var WDS: Boolean = true, var location: Boolean = false, var camera: Boolean = false, var fav: Boolean = false, var limit_search: Boolean = false){

}
