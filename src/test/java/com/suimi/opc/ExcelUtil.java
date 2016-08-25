package com.suimi.opc;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * excel 内容读取
 * 
 * @author euyv
 */
public class ExcelUtil {
    /**
     * 日志
     */
    private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 读取excel数据
     * 
     * @param is
     * @return
     * @throws InvalidFormatException
     * @throws java.io.IOException
     */
    public static JSONObject getExcelContext(InputStream is) {
        Workbook wb = null;
        JSONObject obj = null;
        try {
            // 创建工作薄
            wb = WorkbookFactory.create(is);
            // 解析工作薄
            obj = getSheetsContextList(wb);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IOUtils.closeQuietly(is);
        }
        log.debug("Excel resolve text:{}", obj);
        return obj;

    }

    /**
     * 解析excel页数据
     * 
     * @param wb
     * @return
     */
    private static JSONObject getSheetsContextList(Workbook wb) {
        JSONObject workBookObj = new JSONObject();
        int size = wb.getNumberOfSheets();
        workBookObj.put("size", size);
        for (int i = 0; i < size; i++) {
            Sheet sheet = wb.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            workBookObj.put(String.valueOf(i), getRowsContextList(sheet));
        }
        return workBookObj;
    }

    /**
     * 解析excel行数据
     * 
     * @param sheet
     * @return
     */
    private static JSONObject getRowsContextList(Sheet sheet) {
        Row row = null;
        JSONObject sheetObj = new JSONObject();
        int size = sheet.getLastRowNum();
        sheetObj.put("size", size);
        for (int r = 0; r <= size; r++) {
            row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            sheetObj.put(String.valueOf(r), getCellsContextList(row));
        }
        sheetObj.put("sheetName", sheet.getSheetName());
        return sheetObj;
    }

    /**
     * 获取列数据集
     * 
     * @param row
     * @return
     */
    private static JSONObject getCellsContextList(Row row) {
        Cell cell = null;
        int nullCount = 0;
        int cellCount = row.getPhysicalNumberOfCells();
        JSONObject rowObj = new JSONObject();
        rowObj.put("size", cellCount);
        for (int c = 0; c < cellCount; c++) {
            cell = row.getCell(c);
            // 过滤掉空值行
            if (cell == null) {
                continue;
            }
            Object cellValue = getConvertValue(cell);
            // 统计为空的单元格个数
            if (cellValue == null) {
                nullCount++;
            }
            rowObj.put(String.valueOf(c), cellValue);
        }
        // 剔除整行数据为空的行
        if (nullCount == cellCount) {
            return null;
        }
        return rowObj;
    }

    /**
     * @Title: getConvertValue
     * @Description: 读取列值
     * @param cell
     * @return
     */
    private static Object getConvertValue(Cell cell) {
        switch (cell.getCellType()) {
        case Cell.CELL_TYPE_NUMERIC:// 数字类型
            if (DateUtil.isCellDateFormatted(cell)) {// 日期
                return DateUtil.getJavaDate(cell.getNumericCellValue());
            } else {
                return String.valueOf(cell.getNumericCellValue());// 数字类型
            }
        case Cell.CELL_TYPE_STRING:// 字符串类型
            return cell.getStringCellValue().trim();
        case Cell.CELL_TYPE_BOOLEAN:// boolean类型
            return cell.getBooleanCellValue();
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula();
        case Cell.CELL_TYPE_BLANK:
            return null;
        default:// Cell.CELL_TYPE_ERROR
            return cell.getErrorCellValue();
        }
    }

}
