package collector

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class City(@Id val id: Long?,
                val nameEn: String?,
                val nameAr: String?,
                val latitude: Double?,
                val longitude: Double?)

@Entity
data class District(@Id val id: Long?,
                    val cityId: Long?,
                    val nameEn: String?,
                    val nameAr: String?,
                    val latitude: Double?,
                    val longitude: Double?)

interface CityRepository : CrudRepository<City, Long> {

    @Query("select new kotlin.Pair(min(o.id), max(o.id)) from City o")
    fun findMinMaxCityId(): Pair<Long, Long>
}

interface DistrictRepository : CrudRepository<District, Long>