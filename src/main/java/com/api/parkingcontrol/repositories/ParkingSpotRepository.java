package com.api.parkingcontrol.repositories;

import com.api.parkingcontrol.models.ParkingSpotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ParkingSpotRepository extends  JpaRepository<ParkingSpotModel, UUID>{

    /*boolean existsByPlacaCarro(String placaCarro);
      boolean existsByNumeroVaga(String numeroVaga);
      boolean existsByApartamentAndBlock(String apartamento, String bloco); */

}
