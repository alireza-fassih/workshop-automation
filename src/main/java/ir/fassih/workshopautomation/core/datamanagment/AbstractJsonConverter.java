package ir.fassih.workshopautomation.core.datamanagment;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AbstractJsonConverter<T> implements AttributeConverter<T, String> {

	private final static ObjectMapper mapper = new ObjectMapper(); 
	private final Class<T> clas;
	
	@Override
	public String convertToDatabaseColumn(T attribute) {
		if( attribute == null ) {
			return null;
		}
		try {
			return mapper.writeValueAsString( attribute );
		} catch (JsonProcessingException e) {
			log.error( "cannot serialize {} as json", attribute, e );
			return null;
		}
	}

	@Override
	public T convertToEntityAttribute(String dbData) {
		if( StringUtils.hasText( dbData ) ) {
			try {
				return mapper.readValue(dbData, clas);
			} catch (IOException e) {
				log.error( "cannot deserialize {} as {}", dbData, clas, e );
			}
		}
		return null;
	}

}
