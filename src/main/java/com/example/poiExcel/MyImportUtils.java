package com.example.poiExcel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyImportUtils {

    public static void main(String[] args) throws Exception {
        MyImportUtils myUtil = new MyImportUtils();
        FileInputStream fileInputStream = new FileInputStream(new File("/Users/bravo1988/Desktop/student_info.xlsx"));
        // 传入Excel文件流，从文件中读取数据并返回List
        List<Student> students = myUtil.importExcel(fileInputStream, 2, 0, Student.class);
        students.forEach(System.out::println);
    }

    /**
     * 第四步，引入注解绑定字段与单元格的映射关系
     *
     * @param inputStream    要导入的Excel文件流
     * @param rowStartIndex  从哪行开始读取（从0开始）
     * @param cellStartIndex 从那列开始读取（从0开始）
     * @param pojoClass      操作的Class
     * @param <T>            操作的类型
     * @return
     * @throws Exception
     */
    public <T> List<T> importExcel(InputStream inputStream, Integer rowStartIndex, Integer cellStartIndex, Class<T> pojoClass) throws Exception {
        // 获取工作薄
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        // 获取工作表。一个工作薄中可能有多个工作表，比如sheet1 sheet2，可以根据下标，也可以根据sheet名称。这里根据下标即可。
        XSSFSheet sheet = workbook.getSheetAt(0);

        // 得到Pojo所有字段
        Field[] fields = pojoClass.getDeclaredFields();

        List<T> excelDataList = new ArrayList<>();

        // 收集每一行数据，设置到Model中（跳过表头）
        for (int i = rowStartIndex; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            // 把单元格数据转为当前字段的类型，并设置
            T pojo = pojoClass.newInstance();
            // 遍历单元格（老婆组），为pojo字段赋值
            for (int j = cellStartIndex; j < row.getLastCellNum(); j++) {
                // 获取单元格的值
                XSSFCell cell = row.getCell(j);
                // 开始从老公组找出罗密欧
                for (Field field : fields) {
                    // 遍历，找到与当前单元格匹配的字段，取出单元格的值，把值设置给该字段
                    if (field.isAnnotationPresent(ExcelAttribute.class) && field.getAnnotation(ExcelAttribute.class).sort() == j) {
                        field.setAccessible(true);
                        // 把朱丽叶配给罗密欧
                        field.set(pojo, convertAttrType(field, cell));
                    }
                }

            }

            excelDataList.add(pojo);
        }

        return excelDataList;
    }

    /**
     * 类型转换 将 cell单元格数据类型 转为 Java类型
     * <p>
     * 这里其实分两步：
     * 1.通过getValue()方法得到cell对应的Java类型的字符串类型，比如Date，getValue返回的不是Date类型，而是Date的格式化字符串
     * 2.判断Pojo当前字段是什么类型，把getValue()得到的字符串往该类型转
     *
     * @param field
     * @param cell
     * @return
     * @throws Exception
     */
    private Object convertAttrType(Field field, Cell cell) throws Exception {
        Class<?> fieldType = field.getType();
        if (String.class.isAssignableFrom(fieldType)) {
            return getValue(cell);
        } else if (Date.class.isAssignableFrom(fieldType)) {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(getValue(cell));
        } else if (int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType)) {
            return Integer.parseInt(getValue(cell));
        } else if (double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType)) {
            return Double.parseDouble(getValue(cell));
        } else if (boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)) {
            return Boolean.valueOf(getValue(cell));
        } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
            return new BigDecimal(getValue(cell));
        } else {
            return null;
        }
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
                    // 无论Excel中是58还是58.0，数值类型在POI中最终都被解读为Double。这里的解决办法是通过BigDecimal先把Double先转成字符串，如果是.0结尾，把.0去掉
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
        @ExcelAttribute(sort = 0, value = "姓名")
        private String name;
        @ExcelAttribute(sort = 1, value = "年龄")
        private Integer age;
        @ExcelAttribute(sort = 2, value = "住址")
        private String address;
        @ExcelAttribute(sort = 3, value = "生日")
        private Date birthday;
        @ExcelAttribute(sort = 4, value = "身高")
        private Double height;
        @ExcelAttribute(sort = 5, value = "是否来自大陆")
        private Boolean isMainlandChina;
    }
}