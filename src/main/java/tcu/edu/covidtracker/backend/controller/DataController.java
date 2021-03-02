package tcu.edu.covidtracker.backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tcu.edu.covidtracker.backend.model.County;
import tcu.edu.covidtracker.backend.model.State;
import tcu.edu.covidtracker.backend.model.UnitedStates;
import tcu.edu.covidtracker.backend.repository.StateRepository;
import tcu.edu.covidtracker.backend.repository.USRepository;

import java.io.IOException;

@RestController
@RequestMapping("/api/data")
@Api(produces = "application/json", value = "Data needed to populate map of data pertaining to COVID-19")
public class DataController {

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private USRepository usRepository;

    @GetMapping("/us")
    @ApiOperation(value = "Get data for all of the United States", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved United States data.")
    }
    )
    public UnitedStates retrieveUS(@ApiParam(value = "Date of the data you would like to retrieve") String date ) {
        return usRepository.findById(date).get();
    }

    @GetMapping("/state")
    @ApiOperation(value = "Get All data for a certain state", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved State data.")
    }
    )
    public State retrieveState(@ApiParam(value = "Date of the data you would like to retrieve") String date,
                                               @ApiParam(value = "The data type needed (e.g. which button clicked)") String data,
                                               @ApiParam(value = "The id of the State") String id) {
        return stateRepository.findById(id).get();
    }

    @GetMapping("/statebydate")
    @ApiOperation(value = "Get data for a certain state", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved State data.")
    }
    )
    public State retrieveStateDataByDate(@ApiParam(value = "Date of the data you would like to retrieve") String date,
                                         @ApiParam(value = "The id of the State") String id){
        return stateRepository.findByDate(id, date).get();
    }

    @GetMapping("/county")
    @ApiOperation(value = "Get data for a certain county", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved county data.")
    }
    )
    public ResponseEntity<County> retrieveCounty(@ApiParam(value = "Date of the data you would like to retrieve") String date,
                                                 @ApiParam(value = "The data type needed (e.g. which button clicked)") String data,
                                                 @ApiParam(value = "The id of the County") String id) {
        County county = new County();
        return new ResponseEntity<>(county, HttpStatus.OK);
    }
}
