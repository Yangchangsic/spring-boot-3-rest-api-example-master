package com.bezkoder.spring.restapi.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.restapi.model.Tutorial;
import com.bezkoder.spring.restapi.service.ApiService;
import com.bezkoder.spring.restapi.service.TutorialService;

@RestController
@RequestMapping("/api")
public class TutorialController {
	@Autowired
	ApiService apiService;
	
	
	
	// static Map 선언 및 초기화
    private static final Map<String, String> STATIC_DATA = new HashMap<>();

    // static 블록을 사용하여 초기 데이터 삽입
    static {
        STATIC_DATA.put("Cafe24(신) 유튜브쇼핑", "100091");
        STATIC_DATA.put("고도몰5", "100108");
        STATIC_DATA.put("스마트스토어", "100113");
        STATIC_DATA.put("아임웹", "00698");
        STATIC_DATA.put("카카오톡스토어", "00425");
        STATIC_DATA.put("쿠팡", "00194");
        STATIC_DATA.put("펫프렌즈", "100152");
        STATIC_DATA.put("카카오톡선물하기", "004251");
        
    }
	
	
	

	@GetMapping("/tutorials")
	@ResponseBody
	public String getAllTutorials(@RequestParam(required = false) String title) {
		System.out.println("test");

		List<Map<String, Object>> excelDataList = new ArrayList<>();
		int index = 1;

		while (true) {

			// 최상위 Map
			Map<String, Object> body = new HashMap<>();

			// 첫 번째 레벨 데이터 추가
			body.put("company_code", "B201");
			body.put("warehouse_code", "AFAX");
			body.put("warehouse_type_code", "0000");
			body.put("seller_code", "B201");
			body.put("job_type", "search");
			body.put("type", "out");

			// "data" 키에 들어갈 중첩 Map 생성
			Map<String, Object> data = new HashMap<>();
			data.put("begin_date", "20241205");
			data.put("end_date", "20241205");
			data.put("ord_kind1", "0100");
			data.put("warehouse_list", "AFAX");
			data.put("category1", "큐어라벨");
			data.put("page", String.valueOf(index));

			// "data" Map을 최상위 Map에 추가
			body.put("data", data);

			Map<String, Object> result = apiService.fetchData(body);

			String r_code = (String) result.get("r_code");
			if (!"0".equals(r_code)) {
				return "FAIL";
			}

			List<Map<String, Object>> resultDataList = (List<Map<String, Object>>) result.get("data");

			if (resultDataList.isEmpty()) {
				break;
			}

			List<Map<String, Object>> test = resultDataList.stream().filter(map -> {
				Object itemCode = map.get("item_code");
				return itemCode != null && itemCode.toString().getBytes().length > 9;
			}).map(map -> {
				Map<String, Object> transformedMap = new HashMap<>();
				transformedMap.put("row_id", map.get("row_id"));				
				transformedMap.put("ord_name", map.get("ord_name"));
				
				String ordName = (String) map.get("ord_name");
				if(ordName == null) {
					transformedMap.put("ch_order_name", null);
				}else {
					transformedMap.put("ch_order_name", STATIC_DATA.get(ordName));	
				}

				String expireDay = "*";

				transformedMap.put("lot_no", map.get("lot_no"));
				if (map.get("lot_no") == null || "".equals(map.get("lot_no"))) {
					transformedMap.put("lot_no", "-");
					expireDay = "-";
				} else {
					String sheifLiftUnit = (String) map.get("SheifLift_Unit");
					Integer sheifLift = (Integer) map.get("SheifLift");

					String lotNo = (String) map.get("lot_no");

					// 날짜 형식 지정
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

					// lot_no를 LocalDate로 변환
					LocalDate lotDate = LocalDate.parse(lotNo, formatter);

					// SheifLift_Unit이 "M"인 경우
					if ("M".equalsIgnoreCase(sheifLiftUnit)) {
						lotDate = lotDate.plusMonths(sheifLift).minusDays(1);
						expireDay = lotDate.format(formatter);
					} else if ("D".equals(sheifLiftUnit)) {
						lotDate = lotDate.plusDays(sheifLift).minusDays(1);
						expireDay = lotDate.format(formatter);
					}
				}

				transformedMap.put("expireDay", expireDay);
				
				
				String itemCode= (String) map.get("item_code");
				transformedMap.put("item_code", itemCode.substring(0, Math.min(11, itemCode.length())));
				transformedMap.put("qty", map.get("qty"));
				return transformedMap;
			}).collect(Collectors.toList());
			excelDataList.addAll(test);
			index++;
		}
		excelDataList.forEach(System.out::println);

		System.out.println(excelDataList.size());

		return "OK";
	}
}
