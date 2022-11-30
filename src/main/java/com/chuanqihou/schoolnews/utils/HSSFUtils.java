package com.chuanqihou.schoolnews.utils;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * excel获取数据工具类（获取每一列数据）
 * @auther 传奇后
 * @date 2022/3/19 11:26
 * @veersion 1.0
 */
public class HSSFUtils {
    public static String getCellValue(HSSFCell cell) {
        String str = "";
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            return cell.getNumericCellValue()+"";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_BOOLEAN) {
            return cell.getBooleanCellValue()+"";
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            return cell.getCellFormula();
        }
        return str;
    }
}
