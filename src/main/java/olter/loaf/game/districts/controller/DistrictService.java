package olter.loaf.game.districts.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import olter.loaf.game.districts.DistrictMapper;
import olter.loaf.game.districts.dto.DistrictListResponse;
import olter.loaf.game.districts.model.DistrictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DistrictService {
    private final DistrictRepository districtRepository;
    private final DistrictMapper districtMapper;

    public List<DistrictListResponse> getAllDistricts() {
        return districtRepository.findAll().stream().map(districtMapper::entityToListResponse).toList();
    }
}
