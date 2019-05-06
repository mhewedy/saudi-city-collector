package collector

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

const val REGION = "https://narg.address.gov.sa/AjaxCommonWebMethods.aspx/GetCitiesByRegionID?RegionID=%s"
const val CITY = "https://narg.address.gov.sa/AjaxCommonWebMethods.aspx/GetDistrictsByCityID?CityID=%s"
const val LOCATION = "https://narg.address.gov.sa/AjaxCommonWebMethods.aspx/GetExtent?layerName=\"%s\"&featureId=%s"

@Service
class CollectorService(restTemplateBuilder: RestTemplateBuilder,
                       private val cityRepository: CityRepository,
                       private val districtRepository: DistrictRepository,
                       private val objectMapper: ObjectMapper) {

    val restTemplate: RestTemplate = restTemplateBuilder.build()

    fun doCollect() {
        city()
        district()
    }

    private fun city() {
        val numOfCities = 13        // change for number of cities
        for (i in 1..numOfCities) {
            Thread.sleep(10)
            val response = query(REGION.format(i))
            println(" *************************** start # $i ***************************")

            response?.forEach {
                Thread.sleep(10)
                val loc = geo(LOCATION.format("cities", it.ID))
                val city = City(it.ID, it.NameEnglish, it.NameArabic,
                        loc.minx?.toDouble(), loc.miny?.toDouble(),
                        loc.maxx?.toDouble(), loc.maxy?.toDouble(),
                        avg(loc.minx, loc.maxx), avg(loc.miny, loc.maxy)
                )
                cityRepository.save(city)
            }
        }
    }

    private fun district() {
        val numOfDistricts = 23420      // change for number of districts
        for (i in 2..numOfDistricts) {
            Thread.sleep(10)
            val response = query(CITY.format(i))
            println(" *************************** start # $i ***************************")

            response?.forEach {
                Thread.sleep(10)
                val loc = geo(LOCATION.format("districts", it.ID))
                val district = District(it.ID, i.toLong(), it.NameEnglish, it.NameArabic,
                        loc.minx?.toDouble(), loc.miny?.toDouble(),
                        loc.maxx?.toDouble(), loc.maxy?.toDouble(),
                        avg(loc.minx, loc.maxx), avg(loc.miny, loc.maxy)
                )
                districtRepository.save(district)
            }
        }
    }

    private data class QueryResponse(val ID: Long?, val NameArabic: String?, val NameEnglish: String?)
    private data class GeoResponse(val minx: String?, val miny: String?, val maxx: String?, val maxy: String?)

    private fun query(url: String) =
            restTemplate.exchange(url, HttpMethod.GET,
                    HttpEntity<Void>(buildHeaders()), String::class.java).run {
                objectMapper.readValue(cleanJson(this.body), Array<QueryResponse>::class.java)
            }

    private fun geo(url: String) =
            restTemplate.exchange(url, HttpMethod.GET,
                    HttpEntity<Void>(buildHeaders()), String::class.java).run {
                objectMapper.readValue(cleanJson(this.body), GeoResponse::class.java)
            }

    private fun cleanJson(json: String?) = json?.removeSurrounding("{\"d\":\"", "\"}")?.replace("\\", "")

    private fun buildHeaders() = HttpHeaders().apply {
        add("Accept", "application/json, text/javascript, */*; q=0.01")
        add("Content-Type", "application/json; charset=utf-8")
        add("DNT", "1")
        add("Referer", "https://narg.address.gov.sa/ar/individual/registration/default.aspx")
        add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36")
        add("X-Requested-With", "XMLHttpRequest")
    }

    private fun avg(min: String?, max: String?): Double? {
        if (min != null && max != null) return (min.toDouble() + max.toDouble()) / 2
        if (min != null) return min.toDouble()
        if (max != null) return max.toDouble()
        return null;
    }
}