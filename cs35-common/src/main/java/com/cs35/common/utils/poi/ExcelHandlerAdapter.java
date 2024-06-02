package com.cs35.common.utils.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel
 * 
 * @author cs35
 */
public interface ExcelHandlerAdapter
{
    /**
     *
     * 
     * @param value
     * @param args excelargs
     * @param cell
     * @param wb
     *
     * @return
     */
    Object format(Object value, String[] args, Cell cell, Workbook wb);
}
