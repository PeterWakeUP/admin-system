package com.evsoft.common.utils.excel;

import com.evsoft.common.utils.Encodes;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * java类简单作用描述
 *
 * @Description: java类作用描述
 * @Author: 作者姓名
 * @CreateDate: 2018/8/11 15:45
 * @UpdateUser:
 * @UpdateDate: 2018/8/11 15:45
 * @UpdateRemark: The modified content
 */
public class ExportToExcelAndMergeHeaderNew {

    //显示的导出表的标题
    private String[] title;

    //要进行合并单元格的表头
    private String[][][] excelHeader;

    //导出的文件
    private String upFileName;

    //要导出的数据结果集
    private List<List<Object[]>> dataList = new ArrayList<>();

    //用来装要合并的多少行多少列的坐标
    private List<List<List<Integer>>> vecList = new ArrayList<>();


    private Logger logger = LoggerFactory.getLogger(ExportToExcelAndMergeHeader.class);

    /*******
     *ExportToExcelAndMergeHeader
     * @author chenmc
     * @date 2018/8/11 15:41
     * @param title, upFileName, excelHeader, colNames, dataList
     * @return
     */
    public ExportToExcelAndMergeHeaderNew(String[] title,String upFileName,String[][][] excelHeader,List<List<Object[]>> dataList,List<List<List<Integer>>> vecList){

        this.title=title;
        this.upFileName=upFileName;
        this.excelHeader=excelHeader;
        this.dataList=dataList;
        this.vecList=vecList;

    }


    /*******
        *
        *将数据导出为Excel表格
        * @date 2018/8/11 16:00
        * @param
        * @return void
        */
    public void exportMergeXls(HttpServletResponse response) throws Exception{

        //创建工作簿对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyleUtil styleUtil = new HSSFCellStyleUtil();
        //得到表头样式和列数据样式
        HSSFCellStyle headerStyle = styleUtil.getHeaderStyle(workbook);
        HSSFCellStyle columnStyle = styleUtil.getColumnStyle(workbook);
        HSSFCell cell;
        HSSFRow row;
        for(int i=0;i<title.length;i++)
        {
            //创建工作表对象
            HSSFSheet sheet = workbook.createSheet(title[i]);
            for(int j=0;j<excelHeader[i].length;j++)
            {
                //设置表头标题
                row = sheet.createRow(j);
                for(int k=0;k<excelHeader[i][j].length;k++)
                {
                    cell = row.createCell(k);
                    cell.setCellValue(excelHeader[i][j][k]);
                    cell.setCellStyle(headerStyle);
                }
            }
            //设置合并的标题头(注意：横向合并的时候，标题头单元格必须长度和内容单元格一致否则合并时会把其他标题头单元格内容挤掉)
            //list1 质检信息，list2 模板1,list3 模板2

            for(int l=0;l<vecList.get(i).size();l++){
                sheet.addMergedRegion(new CellRangeAddress(vecList.get(i).get(l).get(0),vecList.get(i).get(l).get(1) ,
                        vecList.get(i).get(l).get(2), vecList.get(i).get(l).get(3)));
            }
            //设置列值-内容
            for (int m = 0; m < dataList.get(i).size(); m++) {
                row = sheet.createRow(m + excelHeader[i].length);
                Object[] objects = dataList.get(i).get(m);
                HSSFCell valueCell = null;   //设置单元格的数据类型
                for (int n = 0; n < objects.length; n++){
                    valueCell = row.createCell(n);
                    if(!"".equals(objects[n]) && null!=objects[n] &&!"".equals(objects[n].toString().trim())){
                        valueCell.setCellValue(objects[n].toString());
                    }else{
                        valueCell.setCellValue("");
                    }
                    valueCell.setCellStyle(columnStyle);                //设置单元格样式
                }

            }
            //让列宽随着导出的列长自动适应
            for (int colNum = 0; colNum < excelHeader[i][excelHeader[i].length-1].length; colNum++)
            {
                int columnWidth = sheet.getColumnWidth(colNum) / 256;
                for (int rowNum = excelHeader[i].length-1; rowNum <=sheet.getLastRowNum(); rowNum++) {
                    HSSFRow currentRow;
                    //当前行未被使用过
                    if (null==sheet.getRow(rowNum)) {
                        currentRow = sheet.createRow(rowNum);
                    } else {
                        currentRow = sheet.getRow(rowNum);
                    }
                    if (null!=currentRow.getCell(colNum)) {
                        HSSFCell currentCell = currentRow.getCell(colNum);
                        if (currentCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            int length = currentCell.getStringCellValue().getBytes().length;
                            if (columnWidth < length) {
                                columnWidth = length;
                            }
                            if(columnWidth>251)
                                columnWidth=251;
                        }
                    }
                }
                if (colNum == 0) {
                    sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
                } else {
                    sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
                }

            }
        }
        //输出文件
        if (null != workbook) {
            try {
                logger.info("ExportToExcelAndMergeHeader:"+workbook);
                String fileName = upFileName + ".xls";
                response.reset();
                response.setContentType("application/octet-stream; charset=utf-8");
                response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(fileName));
                OutputStream out = response.getOutputStream();
                workbook.write(out);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


























}
