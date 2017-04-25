package cn.ebooboo.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.Record;

public class VoBuilder {
	
	public static List<Map<String, Object>> fetchColumn(List<Record> list) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(Record r:list) {
			result.add(r.getColumns());
		}
		return result;
	}
}
