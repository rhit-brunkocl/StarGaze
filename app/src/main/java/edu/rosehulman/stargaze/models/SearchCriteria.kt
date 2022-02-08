package edu.rosehulman.stargaze.models

class SearchCriteria(var WDS_name: String = "",
                     var min_RA: String = "",
                     var max_RA: String = "",
                     var min_Dec: String = "",
                     var max_Dec: String = "",
                     var min_deltaSep: Double = 0.0,
                     var max_deltaSep: Double = 10.0,
                     var min_deltaMag: Double = 0.0,
                     var max_deltaMag: Double = 10.0,
                     var min_obs: String = "0",
                     var max_obs: String = "1000",
                     var min_sep: Double = 0.0,
                     var max_sep: Double = 50.0,
                     var min_mag: Double = 1.0,
                     var max_mag: Double = 20.0,
                     var firstObs: String = "1000",
                     var lastObs: String = "2060"
) {

}
