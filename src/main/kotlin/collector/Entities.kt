package collector

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class City(@Id var id: Long?,
                var nameEn: String?,
                var nameAr: String?,
                var minx: Double?,
                var miny: Double?,
                var maxx: Double?,
                var maxy: Double?,
                var x: Double?,
                val y: Double?)

@Entity
data class District(@Id var id: Long?,
                    var cityId: Long?,
                    var nameEn: String?,
                    var nameAr: String?,
                    var minx: Double?,
                    var miny: Double?,
                    var maxx: Double?,
                    var maxy: Double?,
                    var x: Double?,
                    val y: Double?)