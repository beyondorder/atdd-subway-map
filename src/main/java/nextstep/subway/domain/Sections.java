package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import nextstep.subway.exception.BusinessException;
import nextstep.subway.exception.ErrorCode;

@Embeddable
public class Sections {
	private final static int MINIMUM_SECTION_SIZE = 1;

	@OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("id asc")
	private final List<Section> sections = new ArrayList<>();

	protected Sections() {

	}

	public List<Section> getSections() {
		return sections;
	}

	public void addSection(Section section) {
		if (sections.isEmpty()) {
			this.sections.add(section);
			return;
		}
		validateAddSection(section);
		this.sections.add(section);
	}

	private void validateAddSection(Section section) {
		if (!isSameLastDownStation(section.getUpStation())) {
			throw new BusinessException(ErrorCode.LAST_STATION_NOT_MATCH_UP_STATION);
		}

		if (containsStation(section.getDownStation())) {
			throw new BusinessException(ErrorCode.ALREADY_CONTAINS_STATION);
		}
	}

	private boolean isSameLastDownStation(Station station) {
		return getLastStation().equals(station);
	}

	public boolean containsStation(Station station) {
		return sections.stream()
			.anyMatch(section -> section.containsStation(station));
	}

	public List<Station> getStationList() {
		return sections.stream()
			.map(Section::getStationList)
			.flatMap(List::stream)
			.distinct()
			.collect(Collectors.toList());
	}

	private Station getLastStation() {
		return getLastSection().getDownStation();
	}

	public Section getLastSection() {
		return sections.get(sections.size() - 1);
	}

	public void removeSection(Station station) {
		validateDeleteSection(station);
		sections.remove(getLastSection());
	}

	public void validateDeleteSection(Station station) {
		if (sections.size() <= MINIMUM_SECTION_SIZE) {
			throw new BusinessException(ErrorCode.IS_ONLY_SECTION);
		}
		if (!isSameLastDownStation(station)) {
			throw new BusinessException(ErrorCode.IS_NOT_LAST_STATION);
		}
	}
}