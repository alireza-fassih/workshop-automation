package ir.fassih.workshopautomation.core.datamanagment.model;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchModel {

	public enum SearchType {
		EQ, NE, LT, GT, LE, GE;
		
		public String getPrefix() {
			return new StringBuilder().append( name() ).append( ":" ).toString();
		}
	}

	private Map<String, String> filters;

	@Builder.Default
	private int pageSize = 20;

    @Builder.Default
	private int page = 1;

}
