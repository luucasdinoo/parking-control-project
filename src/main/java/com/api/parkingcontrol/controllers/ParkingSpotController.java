package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dto.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import com.fasterxml.jackson.databind.util.BeanUtil;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

    final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    @PostMapping
    public ResponseEntity<Object> saveParkingSpot(@RequestBody @Valid ParkingSpotDto parkingSpotDto){

     /* if(parkingSpotService.existsByPlacaCarro(parkingSpotDto.getPlacaCarro())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: License Plate Car is already in use!");
        }
        if(parkingSpotService.existsByNumeroVaga(parkingSpotDto.getNumeroVaga())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot is already in use!");
        }
        if(parkingSpotService.existsByApartamentAndBlock(parkingSpotDto.getApartamento(), parkingSpotDto.getBloco())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflict: Parking Spot already registered for this apartment/block!");
        } */

        var parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setRegistroData(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED).body(parkingSpotService.save(parkingSpotModel));
    }

    @GetMapping
    public ResponseEntity<List<ParkingSpotModel>> getAllParkingSpots(){
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneParkingSpot (@PathVariable(value = "id")UUID id){
        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findByID(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada");
        }
        return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpot (@PathVariable(value = "id")UUID id){

        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findByID(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada");
        }
        parkingSpotService.delete(parkingSpotModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Vaga deletada com sucesso");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateParkingSpot(@PathVariable(value = "id")UUID id,
                                                    @RequestBody @Valid ParkingSpotDto parkingSpotDto){

        Optional<ParkingSpotModel> parkingSpotModelOptional = parkingSpotService.findByID(id);
        if(!parkingSpotModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vaga não encontrada");
        }
        //Realizado de duas formas
        //Forma 1
        /* ParkingSpotModel parkingSpotModel = parkingSpotModelOptional.get();

        parkingSpotModel.setNumeroVaga(parkingSpotDto.getNumeroVaga());
        parkingSpotModel.setPlacaCarro(parkingSpotDto.getPlacaCarro());
        parkingSpotModel.setMarcaCarro(parkingSpotDto.getMarcaCarro());
        parkingSpotModel.setModeloCarro(parkingSpotDto.getModeloCarro());
        parkingSpotModel.setCorCarro(parkingSpotDto.getCorCarro());
        parkingSpotModel.setResponsavelNome(parkingSpotDto.getResponsavelNome());
        parkingSpotModel.setApartamento(parkingSpotDto.getApartamento());
        parkingSpotModel.setBloco(parkingSpotDto.getBloco()); */

        //Forma 2
        ParkingSpotModel parkingSpotModel = new ParkingSpotModel();
        BeanUtils.copyProperties(parkingSpotDto, parkingSpotModel);
        parkingSpotModel.setId(parkingSpotModelOptional.get().getId());
        parkingSpotModel.setRegistroData(parkingSpotModelOptional.get().getRegistroData());
        return  ResponseEntity.status(HttpStatus.OK).body(parkingSpotService.save(parkingSpotModel));
    }
}
