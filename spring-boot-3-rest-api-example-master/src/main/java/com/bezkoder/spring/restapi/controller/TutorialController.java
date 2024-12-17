package com.bezkoder.spring.restapi.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/api")
public class TutorialController {
	@Autowired
	ApiService apiService;

	// static Map 선언 및 초기화
	private static final Map<String, String> STATIC_DATA = new HashMap<>();
	private static final Map<String, String> STATIC_DATA2 = new HashMap<>();

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

	// static 블록을 사용하여 초기 데이터 삽입
	static {
		STATIC_DATA2.put("Cafe24(신) 유튜브쇼핑", "100091");
		STATIC_DATA2.put("고도몰5", "100108");
		STATIC_DATA2.put("스마트스토어", "100113");
		STATIC_DATA2.put("아임웹", "00698");
		STATIC_DATA2.put("카카오톡스토어", "00425");
		STATIC_DATA2.put("쿠팡", "00194");
		STATIC_DATA2.put("펫프렌즈", "100152");
		STATIC_DATA2.put("카카오톡선물하기", "004251");
	}

	@GetMapping("/tutorials")
	@ResponseBody
	public String getAllTutorials(@RequestParam(required = false) String title) {

		String begin_date = "20241205";
		String category1 = "큐어라벨";
		List<Map<String, Object>> excelDataList = getDate(begin_date, category1);
		excelDataList.forEach(System.out::println);

		System.out.println(excelDataList.size());

		return "OK";
	}

	@GetMapping("/excel")
	public ResponseEntity<byte[]> downloadExcel(@RequestParam(required = true) String category1,
			@RequestParam(required = true) String begin_date) throws IOException {
		// Excel Workbook 생성
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Sheet1");

		List<List<String>> nestedList = new ArrayList<>();

		// 내부 리스트 생성 및 데이터 추가
		List<String> list1 = new ArrayList<>();
		list1.add("거래구분");
		list1.add("출고일자");
		list1.add("고객코드");
		list1.add("환종");
		list1.add("환율");
		list1.add("과세구분");
		list1.add("단가구분");
		list1.add("창고코드");
		list1.add("LOT번호");
		list1.add("담당자코드");
		list1.add("비고(건)");
		list1.add("품번");
		list1.add("주문단위수량");
		list1.add("재고단위수량");
		list1.add("장소코드");
		list1.add("비고(내역)");

		List<String> list2 = new ArrayList<>();
		list2.add("SO_FG");
		list2.add("ISU_DT");
		list2.add("TR_CD");
		list2.add("EXCH_CD");
		list2.add("EXCH_RT");
		list2.add("VAT_FG");
		list2.add("UMVAT_FG");
		list2.add("WH_CD");
		list2.add("LOT_NB");
		list2.add("PLN_CD");
		list2.add("REMARK_DC");
		list2.add("ITEM_CD");
		list2.add("SO_QT");
		list2.add("ISU_QT");
		list2.add("LC_CD");
		list2.add("REMARK_DC_D");

		List<String> list3 = new ArrayList<>();
		list3.add("타입 : 문자\n" + "길이 : 1\n" + "필수 : True\n"
				+ "설명 : 숫자만 입력하세요. (0.DOMESTIC, 1.LOCAL L/C, 2.구매승인서, 3.MASTER L/C, 4.T/T, 5.D/A, 6.D/P)");
		list3.add("타입 : 날짜\n" + "길이 : 8\n" + "필수 : True\n" + "설명 : 숫자 기준 8자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 10\n" + "필수 : True\n" + "설명 : 영문/숫자 기준 10자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 4\n" + "필수 : True\n" + "설명 : 영문/숫자 기준 4자리(최대)를 입력 하세요.");
		list3.add("타입 : 숫자\n" + "길이 : 17,6\n" + "필수 : True\n" + "설명 : 숫자 기준 17,6자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 1\n" + "필수 : True\n" + "설명 : 숫자 1자리(최대)를 입력 하세요.(0.매출과세 1.수출영세 2.매출면세 3. 매출기타)");
		list3.add("타입 : 문자\n" + "길이 : 1\n" + "필수 : True\n" + "설명 : 숫자 1자리(최대)를 입력 하세요.(0. 부가세미포함 1.부가세포함)");
		list3.add("타입 : 문자\n" + "길이 : 4\n" + "필수 : True\n" + "설명 : 영문/숫자 기준 4자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 50\n" + "필수 : False\n" + "설명 : 영문/숫자 기준 50자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 10\n" + "필수 : False\n" + "설명 : 영문/숫자 기준 10자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 60\n" + "필수 : False\n" + "설명 : 영문/숫자 기준 60자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 30\n" + "필수 : True\n" + "설명 : 영문/숫자 기준 30자리(최대)를 입력 하세요.");
		list3.add("타입 : 숫자\n" + "길이 : 17,6\n" + "필수 : True\n" + "설명 : 숫자 기준 17,6자리(최대)를 입력 하세요.");
		list3.add("타입 : 숫자\n" + "길이 : 17,6\n" + "필수 : True\n" + "설명 : 숫자 기준 17,6자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 4\n" + "필수 : True\n" + "설명 : 영문/숫자 기준 4자리(최대)를 입력 하세요.");
		list3.add("타입 : 문자\n" + "길이 : 60\n" + "필수 : False\n" + "설명 : 영문/숫자 기준 60자리(최대)를 입력 하세요.");

		// 내부 리스트를 nestedList에 추가
		nestedList.add(list1);
		nestedList.add(list2);
		nestedList.add(list3);

		for (int i = 0; i < nestedList.size(); i++) {
			List<String> nested = nestedList.get(i);
			Row headerRow = sheet.createRow(i);

			if (i == 2) {
				headerRow.setHeightInPoints(160); // 높이를 30포인트로 설정
			}

			for (int j = 0; j < nested.size(); j++) {
				Cell cell = headerRow.createCell(j);
				cell.setCellValue(nested.get(j));
			}
		}
		List<Map<String, Object>> excelDataList = getDate(begin_date, category1);
		excelDataList.forEach(System.out::println);
		System.out.println(excelDataList.size());

		for (int i = 0; i < excelDataList.size(); i++) {
			Row row = sheet.createRow(i + 3);

			row.createCell(0).setCellValue(0);

			row.createCell(1).setCellValue(begin_date.substring(0, 4).concat("-").concat(begin_date.substring(4, 6))
					.concat("-").concat(begin_date.substring(6, 8)));

			try {
				row.createCell(2).setCellValue(Integer.valueOf((String) excelDataList.get(i).get("ch_order_name")));
			} catch (Exception e) {
				row.createCell(2).setCellValue("");
			}

			row.createCell(3).setCellValue("KRW");

			row.createCell(4).setCellValue(1.000);

			row.createCell(5).setCellValue(0);

			row.createCell(6).setCellValue(0);

			if ("큐어라벨".equals(category1)) {
				row.createCell(7).setCellValue("0007");
			} else {
				// 퓨어 면 수정 하면 됨.
				row.createCell(7).setCellValue("0007");
			}

			try {
				row.createCell(8).setCellValue(Integer.valueOf((String) excelDataList.get(i).get("expireDay")));
			} catch (Exception e) {
				row.createCell(8).setCellValue("-");
			}

			row.createCell(9).setCellValue(24012902);

			row.createCell(10).setCellValue("B2C".concat(begin_date.substring(4, 8)));

			row.createCell(11).setCellValue((String) excelDataList.get(i).get("item_code"));

			row.createCell(12).setCellValue((Integer) excelDataList.get(i).get("qty"));

			row.createCell(13).setCellValue((Integer) excelDataList.get(i).get("qty"));

			row.createCell(14).setCellValue("008");

			row.createCell(15).setCellValue("");
		}

		// Excel 파일을 ByteArray로 변환
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		workbook.write(outputStream);
		workbook.close();
		byte[] excelData = outputStream.toByteArray();

		// HTTP Response 설정
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=" + ("큐어라벨".equals(category1) ? "Curelabel" : "Esther") + "_" + begin_date
								+ "_" + excelDataList.size() + ".xlsx")
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(excelData);
	}

	private List<Map<String, Object>> getDate(String begin_date, String category1) {
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
			data.put("begin_date", begin_date);
			data.put("end_date", begin_date);
			data.put("ord_kind1", "0100");
			data.put("warehouse_list", "AFAX");
			data.put("category1", category1);
			data.put("page", String.valueOf(index));

			// "data" Map을 최상위 Map에 추가
			body.put("data", data);

			Map<String, Object> result = apiService.fetchData(body);

			String r_code = (String) result.get("r_code");
			if (!"0".equals(r_code)) {
				return new ArrayList<>();
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
				if (ordName == null) {
					transformedMap.put("ch_order_name", null);
				} else {

					if ("큐어라벨".equals(category1)) {
						transformedMap.put("ch_order_name", STATIC_DATA.get(ordName));
					} else {
						transformedMap.put("ch_order_name", STATIC_DATA2.get(ordName));
					}
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

				String itemCode = (String) map.get("item_code");
				transformedMap.put("item_code", itemCode.substring(0, Math.min(11, itemCode.length())));
				transformedMap.put("qty", map.get("qty"));
				return transformedMap;
			}).collect(Collectors.toList());
			excelDataList.addAll(test);
			index++;
		}
		return excelDataList;
	}

}
