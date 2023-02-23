package subway.line;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import subway.exception.CustomException;
import subway.section.SectionRequest;

import java.net.URI;
import java.util.List;

@RestController
public class LineController {
    private LineService lineService;

    public LineController(LineService lineService) {
        this.lineService = lineService;
    }

    @PostMapping("/lines")
    public ResponseEntity<LineResponse> createLine(@RequestBody LineRequest lineRequest) {
        LineResponse line = lineService.saveLine(lineRequest);
        return ResponseEntity.created(URI.create("/lines/" + line.getId())).body(line);
    }

    @PutMapping("/lines/{id}")
    public ResponseEntity<LineResponse> editLine(@PathVariable Long id, @RequestBody LineRequest lineRequest) throws CustomException {
        LineResponse line = lineService.updateLine(id, lineRequest);
        return ResponseEntity.ok().body(line);
    }

    @GetMapping(value = "/lines")
    public ResponseEntity<List<LineResponse>> showLines() {
        return ResponseEntity.ok().body(lineService.findAllLines());
    }

    @GetMapping("/lines/{id}")
    public ResponseEntity<LineResponse> showLine(@PathVariable Long id) throws CustomException {
        ResponseEntity<LineResponse> response = ResponseEntity.ok().body(lineService.findLineResponseById(id));
        return response;
    }

    @DeleteMapping("/lines")
    public ResponseEntity<Void> deleteLines() {
        lineService.deleteLines();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lines/{id}")
    public ResponseEntity<Void> deleteLine(@PathVariable Long id) {
        lineService.deleteLineById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> addSectionByLineId(@PathVariable Long lineId, @RequestBody SectionRequest sectionRequest) {
        lineService.addSectionByLineId(lineId, sectionRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/lines/{lineId}/sections")
    public ResponseEntity<Void> deleteSectionByLineId(@PathVariable Long lineId, @RequestParam Long stationId) {
        Boolean isDelete = lineService.deleteSection(lineId, stationId);
        return ResponseEntity.noContent().build();
    }
}