package ir.fassih.workshopautomation.core.datamanagment;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AbstractEnumConverter<T extends Enum<T>> implements AttributeConverter<Set<T>, String> {

	private final String seprator;
	private final Class<T> klass;

	@Override
	public String convertToDatabaseColumn(Set<T> attribute) {
		if (attribute == null || attribute.isEmpty()) {
			return null;
		}
		return attribute.stream().map(T::name).collect(Collectors.joining(seprator));
	}

	@Override
	public Set<T> convertToEntityAttribute(String dbData) {
		LinkedHashSet<T> data = new LinkedHashSet<>();
		if (StringUtils.hasText(dbData)) {
			for (String en : dbData.split(seprator)) {
				data.add(Enum.valueOf(klass, en));
			}
		}
		return data;
	}

}
