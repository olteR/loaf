package olter.loaf.game.districts.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.game.districts.dto.DistrictListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DistrictEndpoint {

    private final DistrictService districtService;

    @GetMapping("/game/districts")
    public ResponseEntity<List<DistrictListResponse>> getAllDistricts() {
        return ResponseEntity.ok().body(districtService.getAllDistricts());
    }
}
