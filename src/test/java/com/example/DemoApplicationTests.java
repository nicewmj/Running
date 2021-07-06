package com.example;



import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.example.easyexcel.DemoDataListener;
import com.example.entity.DemoData;
import com.example.spring监听机制.demo.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private OrderService orderService;

    /**
     * 测试spring的监听
     */
    @Test
    public void testSpringEvent() {
        orderService.order();
    }


    @Test
    public void testClosure() throws InterruptedException {
        // 在匿名内部类的外面定义一个String变量
        final String str = "hello";
        // 构造一个匿名内部类对象
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println(str);
            }
        };
        new Thread(r).start();
        TimeUnit.SECONDS.sleep(1);
    }





    //=================================================================ce===== 测试POI 导入导出开始=================================

    /**
     * 导入 第一版  Excel
     *
     * @throws IOException
     */
    @Test
    public void testSimpleRead() throws IOException {

        // 获取工作薄（把路径换成你本地的）
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\Administrator\\AppData\\Local\\Temp\\student_info.xlsx");

        // 获取工作表。一个工作薄中可能有多个工作表，比如sheet1 sheet2，可以根据下标，也可以根据sheet名称
        XSSFSheet sheet = workbook.getSheetAt(0);

        /**
         * 方式1 增强for，Row代表行，Cell代表单元格
         */

        // 遍历每一行
        for (Row row : sheet) {
            // 遍历当前行的每个单元格
            for (Cell cell : row) {
                // 获取单元格类型
                CellType cellType = cell.getCellType();
                // 根据类型选择匹配的getXxxValue()方法，比如你判断当前单元格的值是BOOLEAN类型的，你就要用getBooleanCellValue()
                if (cellType == CellType.NUMERIC) {

                    // 你可以试着把这里的getNumericCellValue()改成getStringCellValue()，观察会发生什么

                    System.out.print(cell.getStringCellValue() + "\t");

                } else if (cellType == CellType.BOOLEAN) {

                    System.out.print(cell.getBooleanCellValue() + "\t");

                } else if (cellType == CellType.STRING) {

                    System.out.print(cell.getStringCellValue() + "\t");

                }
            }
            System.out.println("\n------------------------------------------");
        }

        System.out.println("\n==========================================\n");

        /**
         * 方式2 普通for
         */

        // 获取最后一行
        int lastRowNum = sheet.getLastRowNum();

        // 从第一行开始遍历，直到最后一行

        for (int j = 0; j <= lastRowNum; j++) {

            // 获取当前行
            XSSFRow row = sheet.getRow(j);

            if (row != null) {
                // 获取当前行最后一个单元格
                short cellNum = row.getLastCellNum();

                // 从第一个单元格开始遍历，直到最后一个单元格
                for (int k = 0; k < cellNum; k++) {

                    XSSFCell cell = row.getCell(k);

                    CellType cellType = cell.getCellType();

                    // 注意，我们的EXCEL有NUMERIC、STRING和BOOLEAN三种类型，但这里省略了BOOLEAN，会发生什么呢？

                    if (cellType == CellType.NUMERIC) {

                        System.out.print(cell.getNumericCellValue() + "\t");

                    } else if (cellType == CellType.STRING) {

                        System.out.print(cell.getStringCellValue() + "\t");

                    }
                }
            }
            System.out.println("\n------------------------------------------");
        }
    }


    /**
     * 导入 第二版 把 Excel 里面的 NUMERIC 类型转化 更加详细的 类型
     *
     * @throws IOException
     */
    @Test
    public void testSimpleRead2() throws IOException {

        // 获取工作薄（把路径换成你本地的）
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\Administrator\\AppData\\Local\\Temp\\student_info.xlsx");

        // 获取工作表。一个工作薄中可能有多个工作表，比如sheet1 sheet2，可以根据下标，也可以根据sheet名称
        XSSFSheet sheet = workbook.getSheetAt(0);


        // 这里采用普通for
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum; i++) {

            XSSFRow row = sheet.getRow(i);

            if (row != null) {

                short cellNum = row.getLastCellNum();

                for (int k = 0; k < cellNum; k++) {

                    XSSFCell cell = row.getCell(k);

                    // 要判断类型并采用对应的get方法，由于比较繁琐，我们抽取成方法

                    System.out.print(getValue(cell) + "\t");

                }

            }

            System.out.println("\n------------------------------------------");

        }
    }


    /**
     * 提供POI数据类型 到 Java数据类型的转换，最终都返回String
     *
     * @param cell
     * @return
     */

    private String getValue(Cell cell) {

        if (cell == null) {

            return "";

        }


        // 常用的一般就这三大类：STRING、NUMERIC、BOOLEAN，几乎没有别的类型了。但NUMERIC要细分，特别注意
        switch (cell.getCellType()) {
            case STRING:
                return cell.getRichStringCellValue().getString().trim();
            // EXCEL的日期和数字都被POI整合为NUMERIC，这里把它们重新拆开

            case NUMERIC:
                // DateUtil是POI内部提供的日期工具类，可以把原本是日期类型的NUMERIC转为Java的Data类型
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date javaDate = DateUtil.getJavaDate(cell.getNumericCellValue());
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(javaDate);

                } else {
                    // 无论EXCEL中是58还是58.0，数值类型在POI中最终都被解读为double。这里的解决办法是通过BigDecimal先把double先转成字符串，如果是.0结尾，把.0去掉
                    String strCell = "";
                    double num = cell.getNumericCellValue();
                    BigDecimal bd = new BigDecimal(Double.toString(num));
                    strCell = bd.toPlainString();
                    // 去除 浮点型 自动加的 .0
                    if (strCell.endsWith(".0")) {
                        strCell = strCell.substring(0, strCell.indexOf("."));
                    }

                    return strCell;

                }

            case BOOLEAN:
                boolean booleanCellValue = cell.getBooleanCellValue();
                return String.valueOf(booleanCellValue);

            default:
                return "";

        }

    }

    /**
     * 第一版  导出 excel  没有把number的类型转化为时间
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSimpleWrite() throws IOException, ParseException {

        // 创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 创建工作表
        XSSFSheet sheet = workbook.createSheet("student");

        // 构造假数据
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1L, "周星驰", 58, "香港", new SimpleDateFormat("yyyy-MM-dd").parse("1962-6-22"), 174.0, false));
        studentList.add(new Student(2L, "李健", 46, "哈尔滨", new SimpleDateFormat("yyyy-MM-dd").parse("1974-9-23"), 174.5, true));
        studentList.add(new Student(3L, "周深", 28, "贵州", new SimpleDateFormat("yyyy-MM-dd").parse("1992-9-29"), 161.0, true));


        for (int i = 0; i < studentList.size(); i++) {
            Student student = studentList.get(i);
            // 创建行
            XSSFRow row = sheet.createRow(i);

            // 在当前行创建6个单元格，并设置数据（id不导出）
            row.createCell(0).setCellValue(student.getName());
            row.createCell(1).setCellValue(student.getAge());
            row.createCell(2).setCellValue(student.getAddress());
            row.createCell(3).setCellValue(student.getBirthday());
            row.createCell(4).setCellValue(student.getHeight());
            row.createCell(5).setCellValue(student.getIsMainlandChina());

        }
        FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\AppData\\Local\\Temp\\哎哎哎.xlsx");
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        System.out.println("导出成功！");
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Student {
        private Long id;
        private String name;
        private Integer age;
        private String address;
        private Date birthday;
        private Double height;
        private Boolean isMainlandChina;
    }

    /**
     * 第二版  导出
     *
     * @throws IOException
     * @throws ParseException
     */
    @Test
    public void testSimpleWrite2() throws IOException, ParseException {
        // 创建工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();

        // 创建工作表
        XSSFSheet sheet = workbook.createSheet("student");

        // 构造假数据
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1L, "周星驰", 58, "香港", new SimpleDateFormat("yyyy-MM-dd").parse("1962-6-22"), 174.0, false));
        studentList.add(new Student(2L, "李健", 46, "哈尔滨", new SimpleDateFormat("yyyy-MM-dd").parse("1974-9-23"), 174.5, true));
        studentList.add(new Student(3L, "周深", 28, "贵州", new SimpleDateFormat("yyyy-MM-dd").parse("1992-9-29"), 161.0, true));

        for (int i = 0; i < studentList.size(); i++) {

            Student student = studentList.get(i);

            // 创建行

            XSSFRow row = sheet.createRow(i);

            // 在当前行创建6个单元格，并设置数据（id不导出）

            row.createCell(0).setCellValue(student.getName());

            row.createCell(1).setCellValue(student.getAge());

            row.createCell(2).setCellValue(student.getAddress());


            // 创建cell

            XSSFCell birthdayCell = row.createCell(3);

            // 设置样式！！！指定Excel对数值的解读方式

            XSSFCellStyle cellStyle = workbook.createCellStyle();

            XSSFDataFormat dataFormat = workbook.createDataFormat();

            // 样式随意，可以是yyyy-MM-dd或yyyy/MM-dd都行
            cellStyle.setDataFormat(dataFormat.getFormat("yyyy/MM/dd"));

            birthdayCell.setCellStyle(cellStyle);

            // 上面设置完样式后，才最终设置值
            birthdayCell.setCellValue(student.getBirthday());

            row.createCell(4).setCellValue(student.getHeight());

            row.createCell(5).setCellValue(student.getIsMainlandChina());

        }

        FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\AppData\\Local\\Temp\\哎哎哎a.xlsx");
        workbook.write(out);
        out.flush();

        out.close();

        workbook.close();

        System.out.println("导出成功！");

    }

    /**
     * 按模板样式导出Excel
     */

    @Test
    public void testWriteWithStyle() throws IOException, ParseException {

        // 查询数据

        List<Student> studentList = new ArrayList<>();

        studentList.add(new Student(1L, "周深", 28, "贵州", new SimpleDateFormat("yyyy-MM-dd").parse("1992-9-29"), 161.0, true));

        studentList.add(new Student(2L, "李健", 46, "哈尔滨", new SimpleDateFormat("yyyy-MM-dd").parse("1974-9-23"), 174.5, true));

        studentList.add(new Student(3L, "周星驰", 58, "香港", new SimpleDateFormat("yyyy-MM-dd").parse("1962-6-22"), 174.0, false));

        // 读取模板
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\Administrator\\AppData\\Local\\Temp\\student_info.xlsx");

        // 获取模板sheet
        XSSFSheet sheet = workbook.getSheetAt(0);

        // 找到数据起始行（前两行是标题和表头，要跳过，所以是getRow(2)）
        XSSFRow dataTemplateRow = sheet.getRow(2);

        // 构造一个CellStyle数组，用来存放单元格样式。一行有N个单元格，长度就设置为N
        CellStyle[] cellStyles = new CellStyle[dataTemplateRow.getLastCellNum()];

        for (int i = 0; i < cellStyles.length; i++) {

            // 收集每一个格子对应的格式，你可以理解为准备了一把“格式刷”
            cellStyles[i] = dataTemplateRow.getCell(i).getCellStyle();

        }


        // 创建单元格，并设置样式和数据

        for (int i = 0; i < studentList.size(); i++) {

            // 注意是i+2，模板前两行是大标题和表头。你可能看着难受，想把上面for的i改为i+2，千万别。因为studentList必须从0开始取值

            XSSFRow row = sheet.createRow(i + 2);

            // 为每一行创建单元格并设置数据

            Student student = studentList.get(i);


            XSSFCell nameCell = row.createCell(0);// 创建单元格

            nameCell.setCellValue(student.getName());         // 设置值

            nameCell.setCellStyle(cellStyles[0]);             // 设置单元格样式


            XSSFCell ageCell = row.createCell(1);

            ageCell.setCellValue(student.getAge());

            ageCell.setCellStyle(cellStyles[1]);


            XSSFCell addressCell = row.createCell(2);

            addressCell.setCellValue(student.getAddress());

            addressCell.setCellStyle(cellStyles[2]);


            /**
             * 你可能有疑问，这里是日期类型，是不是要和上一次一样，设置单元格样式为日期类型？
             * 这回不用了，因为上面已经拷贝了模板的样式，生日一栏就是按日期类型展示的
             */
            XSSFCell birthdayCell = row.createCell(3);
            birthdayCell.setCellValue(student.getBirthday());
            birthdayCell.setCellStyle(cellStyles[3]);
            XSSFCell heightCell = row.createCell(4);
            heightCell.setCellValue(student.getHeight());
            heightCell.setCellStyle(cellStyles[4]);
            XSSFCell mainLandChinaCell = row.createCell(5);
            mainLandChinaCell.setCellValue(student.getIsMainlandChina());
            mainLandChinaCell.setCellStyle(cellStyles[5]);

        }

        // 输出
        FileOutputStream out = new FileOutputStream("C:\\Users\\Administrator\\AppData\\Local\\Temp\\哎哎哎sa.xlsx");
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        System.out.println("导出成功！");

    }


    /**
     * 测试 几种读取文件流的方式：
     *
     * @throws IOException
     */
    @Test
    public void testResourceRead() throws IOException {

        // 第一种：使用Spring提供的ClassPathResource，有没有斜杆都可以（推荐，功能都封装好了）
        ClassPathResource classPathResource = new ClassPathResource("excel/student_info.xlsx");

        InputStream inputStream = classPathResource.getInputStream();

        System.out.println(inputStream);


        // 第二种：使用Class#getResourceAsStream()，要加/
        InputStream classResource = this.getClass().getResourceAsStream("/excel/student_info.xlsx");

        System.out.println(classResource);


        // 第三种：使用ClassLoader#getResourceAsStream()，不加/
        InputStream classLoaderResource = this.getClass().getClassLoader().getResourceAsStream("excel/student_info.xlsx");

        System.out.println(classLoaderResource);

    }

    //=================================================================ce===== 测试 导入导出到此结束=================================
    /**
     * 最简单的读
     * <p>1. 创建excel对应的实体对象 参照{@link DemoData}
     * <p>2. 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器，参照{@link DemoDataListener}
     * <p>3. 直接读即可
     */
    @Test
    public void simpleRead() {
        // 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
        // 写法1：
        String fileName = "C:\\Users\\Administrator\\Desktop\\补丁\\"  + "student_info.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
        EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).sheet().doRead();

        // 写法2：
       /* fileName = TestFileUtil.getPath() + "demo" + File.separator + "demo.xlsx";
        ExcelReader excelReader = null;
        try {
            excelReader = EasyExcel.read(fileName, DemoData.class, new DemoDataListener()).build();
            ReadSheet readSheet = EasyExcel.readSheet(0).build();
            excelReader.read(readSheet);
        } finally {
            if (excelReader != null) {
                // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
                excelReader.finish();
            }
        }*/
    }



}
