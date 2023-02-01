package subway.line.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.business.model.Line;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public Line toLine() {
        return Line.builder()
                .name(name)
                .color(color)
                .distance(distance)
                .build();
    }

}