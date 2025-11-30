package com.duoc.rotompediabackend

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GymLeaderRepository : JpaRepository<GymLeader, Long> {
    fun findByRegionIgnoreCase(region: String): List<GymLeader>
}