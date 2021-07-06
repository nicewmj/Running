package com.example.controller;

import com.example.poiExcel.PoiExcelUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class ExcelController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws Exception {
        // 模拟从数据库查询数据
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1L, "周深（web导出）", 28, "贵州", new SimpleDateFormat("yyyy-MM-dd").parse("1992-9-29"), 161.0, true));
        studentList.add(new Student(2L, "李健（web导出）", 46, "哈尔滨", new SimpleDateFormat("yyyy-MM-dd").parse("1974-9-23"), 174.5, true));
        studentList.add(new Student(3L, "周星驰（web导出）", 58, "香港", new SimpleDateFormat("yyyy-MM-dd").parse("1962-6-22"), 174.0, false));

        // 导出数据
        PoiExcelUtils<Student> poiExcelUtils = new PoiExcelUtils<>(Student.class);
        FileInputStream excelTemplateInputStream = new FileInputStream(new File("excel/student_info.xlsx"));
        poiExcelUtils.exportExcelWithTemplate(studentList, "学生信息表.xlsx", excelTemplateInputStream, 2, 0, response);
        logger.info("导出成功！");

    }

    @PostMapping("/importExcel")
    public Map importExcel(MultipartFile file) throws Exception {
        PoiExcelUtils<Student> poiExcelUtils = new PoiExcelUtils<>(Student.class);
        List<Student> studentList = poiExcelUtils.importExcel(file.getInputStream(), 2, 0);
        saveToDB(studentList);

        logger.info("导入{}成功！", file.getOriginalFilename());

        // 这里用Map偷懒了，实际项目中可以封装Result实体类返回
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", studentList);
        result.put("msg", "success");
        return result;
    }

    private void saveToDB(List<Student> studentList) {
        if (CollectionUtils.isEmpty(studentList)) {
            return;
        }
        // 直接打印，模拟插入数据库
        studentList.forEach(System.out::println);
    }

    /**
     * 提供POI数据类型 --> Java数据类型的转换
     * 由于本方法返回值设为String，所以不管转换后是什么Java类型，都要以String格式返回
     * 所以Date会被格式化为yyyy-MM-dd HH:mm:ss
     * 后面根据需要自己另外转换
     *
     * @param cell
     * @return
     */
    private String getValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    // DateUtil是POI内部提供的日期工具类，可以把原本是日期类型的NUMERIC转为Java的Data类型
                    Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
                    String dateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(javaDate);
                    return dateString;
                } else {
                    /*
                     * 无论Excel中是58还是58.0，数值类型在POI中最终都被解读为Double。
                     * 这里的解决办法是通过BigDecimal先把Double先转成字符串，如果是.0结尾，把.0去掉
                     * */
                    String strCell = "";
                    Double num = cell.getNumericCellValue();
                    BigDecimal bd = new BigDecimal(num.toString());
                    if (bd != null) {
                        strCell = bd.toPlainString();
                    }
                    // 去除 浮点型 自动加的 .0
                    if (strCell.endsWith(".0")) {
                        strCell = strCell.substring(0, strCell.indexOf("."));
                    }
                    return strCell;
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Student {
        private Long id;
        private String name;
        private Integer age;
        private String address;
        private Date birthday;
        private Double height;
        private Boolean isMainlandChina;
    }

}