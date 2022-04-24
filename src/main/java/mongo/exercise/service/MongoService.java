package mongo.exercise.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import mongo.exercise.document.MongoPojo;

@Service
public class MongoService {
	
	private static final Logger logger = LoggerFactory.getLogger(MongoService.class);

	private String collectionName = "employees";
	
	@Autowired
	private MongoTemplate mongodb;  //몽고Template
	
	public final String[] tableCols = {
	                 "first_name",
	                 "last_name",
	                 "job",
	                 "location",
	                 "salary",
	                 "date"
	};

	//단일정보 가져오기
	public MongoPojo getInfor(MongoPojo vo) {
		Query query = new Query(Criteria.where("_id").lt(new ObjectId("5cd276f2d079c73e93149837")));
		MongoPojo obj = mongodb.findOne(query, MongoPojo.class, collectionName);
		return obj;
	}

	 //리스트형태 데이터 전부 가져오기
	public List<MongoPojo> getList(MongoPojo vo) {
		Query query = new Query();
		return mongodb.find(query, MongoPojo.class, collectionName);
	}
	
	//페이징 처리가 포함된 리스트 
	public HashMap<Object,Object> getPageList(HashMap<String, Object> map) {
			
		final HashMap<Object,Object> result = new HashMap<Object,Object>(2);

		int startNum = (Integer) map.get("start");
		int endNum = (Integer) map.get("end");
		int sortCol = Integer.parseInt((String) map.get("sortCol"));
		String sortDir = (String) map.get("sortDir");
		
		Query query = new Query();  //조건
		query.skip(startNum);
		query.limit(endNum);

		Sort sort = new Sort(sortDir.equals("desc") ? Sort.Direction.DESC :Sort.Direction.ASC, tableCols[sortCol]);
		query.with(sort);
		
		if ((String) map.get("job") != null && (String) map.get("job") != "") {
			
			query.addCriteria(Criteria.where("job").regex(".*" +map.get("job") +".*"));
		}
		
		
		System.out.println(query.toString());
				
		//		Pageable pageable = new PageRequest(startNum, endNum);
		//		query.with(pageable);  //페이지 정보를 포함
		//		
		//System.out.println(pageable.toString());
				
		long total = mongodb.count(query, MongoPojo.class, collectionName);
		System.out.println("Total Count: " +total);

		List<MongoPojo> obj = mongodb.find(query, MongoPojo.class, collectionName);
		System.out.println("Found: " );


//		Page<MongoPojo> list = new PageImpl<MongoPojo>(obj, pageable, total);
		
		JsonArray ja = new JsonArray();
		
		for (MongoPojo mongo : obj) {
			
			JsonObject empl = new JsonObject();
			empl.addProperty("_id", mongo.getId());
			empl.addProperty("first_name", mongo.getFisrtName());
			empl.addProperty("last_name", mongo.getLastName());
			empl.addProperty("job", mongo.getJob());
			empl.addProperty("location", mongo.getLocation());
			empl.addProperty("salary", mongo.getSalary());
			empl.addProperty("date", mongo.getDate());

			ja.add(empl);
		}
		
		
		result.put("total",total);
		result.put("list",ja);
		
		return result;
	}
	
	//등록
	public String insertData(MongoPojo vo) throws Exception {
		mongodb.insert(vo, collectionName);  //vo객체에 정보를 토대로 알아서 등록한다.
		return vo.getId();  //id가 등록된 고유 키 값이다. 등록이 성공하면 vo에 담아준다.
	}

	//수정
	public void updateData(MongoPojo vo) throws Exception {
		mongodb.save(vo, collectionName);  //vo객체의 정보를 토대로 알아서 매칭하여 수정한다.
        //update 이름으로 시작하는 메소드가 훨씬 더 사용하기 편리하다.
	}

	//삭제
	public void deleteData(MongoPojo vo) throws Exception {
		mongodb.remove(vo, collectionName);  //vo객체의 정보를 토대로 알아서 삭제한다.
	}	

}
