package com.example.poiExcel;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class MyExportUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * @param dataList            要导出的数据
     * @param pojoClass           要操作的类型
     * @param templateInputStream Excel模板输入流
     * @param response            response响应，用来导出Excel文件
     * @param excelName           指定导出文件名
     * @param rowIndex            模板数据起始行
     * @param cellIndex           模板数据起始列
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void exportExcel(List<T> dataList, Class<T> pojoClass, InputStream templateInputStream, HttpServletResponse response,
                            String excelName, Integer rowIndex, Integer cellIndex) throws IOException, IllegalAccessException {

        // 读取模板
        XSSFWorkbook workbook = new XSSFWorkbook(templateInputStream);
        // 获取模板sheet，默认第一张sheet
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 从指定行收集单元格样式，方便复用，类似格式刷
        CellStyle[] templateStyles = getTemplateStyles(rowIndex, cellIndex, sheet);
        // 得到所有字段
        Field[] fields = pojoClass.getDeclaredFields();

        // 创建单元格，并设置样式和数据
        for (int i = 0; i < dataList.size(); i++) {
            Object pojo = dataList.get(i);
            XSSFRow row = sheet.createRow(i + rowIndex);
            // 为当前行创建单元格（创建老婆组）
            for (int k = cellIndex; k < templateStyles.length + cellIndex; k++) {
                // 当前新建了朱丽叶，已经就位
                XSSFCell cell = row.createCell(k);
                // 找到朱丽叶的化妆盒，给朱丽叶化妆
                cell.setCellStyle(templateStyles[k - cellIndex]);
                // 遍历字段（老公组），找到罗密欧
                for (Field field : fields) {
                    if (field.isAnnotationPresent(ExcelAttribute.class) && field.getAnnotation(ExcelAttribute.class).sort() == k - cellIndex) {
                        field.setAccessible(true);
                        // 把罗密欧给朱丽叶
                        mappingValue(field, cell, pojo);
                    }
                }

            }
        }

        // 通过response响应
        String fileName = new String(excelName.getBytes("UTF-8"), "ISO-8859-1");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition", "attachment;filename=" + fileName);
        response.setHeader("filename", fileName);
        workbook.write(response.getOutputStream());
        workbook.close();
        logger.info("导出成功！");
    }

    /**
     * 根据字段类型强制转为字段数据，并设置给cell
     *
     * @param field
     * @param cell
     * @param pojo
     * @throws IllegalAccessException
     */
    private void mappingValue(Field field, Cell cell, Object pojo) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        if (Date.class.isAssignableFrom(fieldType)) {
            cell.setCellValue((Date) field.get(pojo));
        } else if (int.class.isAssignableFrom(fieldType) || Integer.class.isAssignableFrom(fieldType)) {
            cell.setCellValue((Integer) field.get(pojo));
        } else if (double.class.isAssignableFrom(fieldType) || Double.class.isAssignableFrom(fieldType)) {
            cell.setCellValue((Double) field.get(pojo));
        } else if (boolean.class.isAssignableFrom(fieldType) || Boolean.class.isAssignableFrom(fieldType)) {
            cell.setCellValue((Boolean) field.get(pojo));
        } else if (BigDecimal.class.isAssignableFrom(fieldType)) {
            cell.setCellValue(((BigDecimal) field.get(pojo)).doubleValue());
        } else {
            cell.setCellValue((String) field.get(pojo));
        }
    }


    /**
     * 收集Excel模板的样式，方便对新建单元格复用，相当于打造一把格式刷
     *
     * @param rowIndex
     * @param cellIndex
     * @param sheet
     * @return
     */
    private CellStyle[] getTemplateStyles(Integer rowIndex, Integer cellIndex, XSSFSheet sheet) {
        XSSFRow dataTemplateRow = sheet.getRow(rowIndex);
        CellStyle[] cellStyles = new CellStyle[dataTemplateRow.getLastCellNum() - cellIndex];
        for (int i = 0; i < cellStyles.length; i++) {
            cellStyles[i] = dataTemplateRow.getCell(i + cellIndex).getCellStyle();
        }
        return cellStyles;
    }

}