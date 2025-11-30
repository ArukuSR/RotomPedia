package com.duoc.rotompediabackend

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/leaders")
class GymLeaderController(private val repository: GymLeaderRepository) {

    // URL: http://localhost:8080/api/leaders
    @GetMapping
    fun getAllLeaders(): List<GymLeader> {
        return repository.findAll()
    }

    // URL: http://localhost:8080/api/leaders/region/kanto
    @GetMapping("/region/{regionName}")
    fun getLeadersByRegion(@PathVariable regionName: String): List<GymLeader> {
        return repository.findByRegionIgnoreCase(regionName)
    }

    // Carga de datos iniciales
    @Bean
    fun initDatabase() = CommandLineRunner {
        repository.deleteAll()
        repository.saveAll(listOf(
            // --- KANTO ---
            GymLeader(
                name = "Brock",
                region = "Kanto",
                typeSpecialty = "rock", // Inglés para que funcione el color
                badgeName = "Medalla Roca",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/brock.png"
            ),
            GymLeader(
                name = "Misty",
                region = "Kanto",
                typeSpecialty = "water",
                badgeName = "Medalla Cascada",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/misty.png"
            ),
            GymLeader(
                name = "Lt. Surge",
                region = "Kanto",
                typeSpecialty = "electric",
                badgeName = "Medalla Trueno",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/ltsurge.png"
            ),
            GymLeader(
                name = "Erika",
                region = "Kanto",
                typeSpecialty = "grass",
                badgeName = "Medalla Arcoíris",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/erika.png"
            ),

            // --- JOHTO ---
            GymLeader(
                name = "Pegaso (Falkner)",
                region = "Johto",
                typeSpecialty = "flying",
                badgeName = "Medalla Céfiro",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/falkner.png"
            ),
            GymLeader(
                name = "Antón (Bugsy)",
                region = "Johto",
                typeSpecialty = "bug",
                badgeName = "Medalla Colmena",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/bugsy.png"
            ),
            GymLeader(
                name = "Blanca (Whitney)",
                region = "Johto",
                typeSpecialty = "normal",
                badgeName = "Medalla Planicie",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/whitney.png"
            ),

            // --- HOENN ---
            GymLeader(
                name = "Petra (Roxanne)",
                region = "Hoenn",
                typeSpecialty = "rock",
                badgeName = "Medalla Piedra",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/roxanne.png"
            ),
            GymLeader(
                name = "Marcial (Brawly)",
                region = "Hoenn",
                typeSpecialty = "fighting",
                badgeName = "Medalla Puño",
                imageUrl = "https://play.pokemonshowdown.com/sprites/trainers/brawly.png"
            )
        ))
        println("✅ Base de datos RECARGADA con imágenes y tipos corregidos.")
    }
}