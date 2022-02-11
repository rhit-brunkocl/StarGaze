package edu.rosehulman.stargaze.models

class SearchCriteria(var WDS_name: String = "",
                     var min_RA: Double = 0.0,
                     var max_RA: Double = 3000000.0,
                     var min_Dec: Double = -90.0,
                     var max_Dec: Double = 90.0,
                     var min_deltaSep: Double = 0.0,
                     var max_deltaSep: Double = 10.0,
                     var min_deltaMag: Double = 0.0,
                     var max_deltaMag: Double = 10.0,
                     var min_obs: Int = 0,
                     var max_obs: Int = 1000,
                     var min_sep: Double = 0.0,
                     var max_sep: Double = 50.0,
                     var min_mag: Double = 1.0,
                     var max_mag: Double = 20.0,
                     var firstObs: Int = 1000,
                     var lastObs: Int = 2060
) {

}
