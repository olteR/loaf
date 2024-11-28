package olter.loaf.game.logs.controller;

import lombok.RequiredArgsConstructor;
import olter.loaf.common.security.SecurityAnnotations;
import olter.loaf.game.logs.dto.UserStatisticsResponse;
import olter.loaf.users.model.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LogEndpoint {
    private final LogService logService;

    @GetMapping("/statistics")
    public ResponseEntity<UserStatisticsResponse> getStatistics(@SecurityAnnotations.GetLoggedInUser UserEntity user) {
        return ResponseEntity.ok().body(logService.composeStatistics(user));
    }
}
