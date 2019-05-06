package collector

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
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

interface CityRepository : CrudRepository<City, Long> {

    @Query("select new kotlin.Pair(min(o.id), max(o.id)) from City o")
    fun findMinMaxCityId(): Pair<Long, Long>
}

interface DistrictRepository : CrudRepository<District, Long>