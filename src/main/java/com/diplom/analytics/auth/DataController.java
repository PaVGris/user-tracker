package com.diplom.analytics.auth;

import com.diplom.analytics.tool.model.ClickHeatmapDto;
import com.diplom.analytics.tool.repository.ClickEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final ClickEventRepository repository;

    public DataController(ClickEventRepository repository) {
        this.repository = repository;
    }


    @GetMapping("/get-url/{username}")
    public ResponseEntity<String> checkAuth(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(user -> ResponseEntity.ok(user.getUrl())) // возвращаем строку с именем пользователя
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found"));
    }

    @GetMapping("/heatmap/{clientId}/{pageUrl}")
    public ResponseEntity<List<ClickHeatmapDto>> getClickHeatmapByClientAndPage(
            @PathVariable String clientId,
            @PathVariable String pageUrl) {
        List<ClickHeatmapDto> heatmapData = repository.getClickHeatmapDataByClientAndPage(clientId, pageUrl);
        return ResponseEntity.ok(heatmapData);
    }
}
