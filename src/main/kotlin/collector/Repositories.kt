package collector

import org.springframework.data.repository.CrudRepository

interface CityRepository : CrudRepository<City, Long>

interface DistrictRepository : CrudRepository<District, Long>
