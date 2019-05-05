package collector

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Application(private val collectorService: CollectorService) : CommandLineRunner {


    override fun run(vararg args: String?) {
        collectorService.doCollect()
    }

}

fun main(args: Array<String>) {
    Bypass.trustAllHosts()
    runApplication<Application>(*args)
}
