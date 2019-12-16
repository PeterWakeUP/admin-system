package com.evsoft.common.utils.excel;

import com.evsoft.common.utils.Encodes;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
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
public class ExportToExcelAndMergeHeader {

    //显示的导出表的标题
    private String title;

    //要进行合并单元格的表头
    private String[] excelHeader;

    //导出表的列名
    private String[] colNames;

    //导出的文件
    private String upFileName;

    //要导出的数据结果集
    private List<Object[]> dataList = new ArrayList<Object[]>();

    //用来装要合并的多少行多少列的坐标
    private List<List<Integer>> vecList = new ArrayList<List<Integer>>();

    //用来装要标红的数据的索引值
    private List<int[]> dataRedIndexList;

    private Logger logger = LoggerFactory.getLogger(ExportToExcelAndMergeHeader.class);

    /*******
     *ExportToExcelAndMergeHeader
     * @author chenmc
     * @date 2018/8/11 15:41
     * @param title, upFileName, excelHeader, colNames, dataList
     * @return
     */
    public ExportToExcelAndMergeHeader(String title,String upFileName,String[] excelHeader,String[] colNames,List<Object[]> dataList,List<List<Integer>> vecList){

        this.title=title;
        this.upFileName=upFileName;
        this.excelHeader=excelHeader;
        this.colNames=colNames;
        this.dataList=dataList;
        this.vecList=vecList;

    }

    /*******
     *ExportToExcelAndMergeHeader
     * @author chenmc
     * @date 2018/8/11 15:41
     * @param title, upFileName, excelHeader, colNames, dataList
     * @return
     */
    public ExportToExcelAndMergeHeader(String title,String upFileName,String[] excelHeader,String[] colNames,List<Object[]> dataList,List<List<Integer>> vecList,List<int[]> dataRedIndexList){

        this( title, upFileName, excelHeader, colNames, dataList, vecList);
        this.dataRedIndexList=dataRedIndexList;
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
        //创建工作表对象
        HSSFSheet sheet = workbook.createSheet(title);


        HSSFCellStyleUtil styleUtil = new HSSFCellStyleUtil();
        //得到表头样式和列数据样式
        HSSFCellStyle headerStyle = styleUtil.getHeaderStyle(workbook);
        HSSFCellStyle columnStyle = styleUtil.getColumnStyle(workbook);

        //设置表头标题
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell;

        for(int i=0;i<excelHeader.length;i++)
        {
            cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(headerStyle);

        }


        //设置合并的标题头(注意：横向合并的时候，标题头单元格必须长度和内容单元格一致否则合并时会把其他标题头单元格内容挤掉)
        //list1 质检信息，list2 模板1,list3 模板2

        for(int i=0;i<vecList.size();i++){
            sheet.addMergedRegion(new CellRangeAddress(vecList.get(i).get(0),vecList.get(i).get(1) ,
                    vecList.get(i).get(2), vecList.get(i).get(3)));
        }


        //设置列名 cloNames
        HSSFRow cloNamesRow = sheet.createRow(1);
        HSSFCell cloNamesCell;
        for(int j=0;j<colNames.length;j++)
        {
            cloNamesCell = cloNamesRow.createCell(j);
            cloNamesCell.setCellStyle(headerStyle);
            cloNamesCell.setCellValue(colNames[j]);

        }


        //设置列值-内容
       for (int i = 0; i < dataList.size(); i++) {
            row = sheet.createRow(i + 2);
            Object[] objects = dataList.get(i);
           HSSFCell valueCell = null;   //设置单元格的数据类型
           for (int k = 0; k < objects.length; k++){

               valueCell = row.createCell(k);

               if(!"".equals(objects[k]) && null!=objects[k] &&!"".equals(objects[k].toString().trim())){
                   valueCell.setCellValue(objects[k].toString());
               }else{
                   valueCell.setCellValue("");
               }

               valueCell.setCellStyle(columnStyle);                //设置单元格样式

           }

        }
        //单元格数据字体设置为红色
        if(dataRedIndexList!=null&&dataRedIndexList.size()>0)
        {
            HSSFCellStyle columnResStyle = styleUtil.getColumnStyle(workbook,HSSFColor.RED.index);
            for (int[] tempIndex:dataRedIndexList) {
                HSSFRow row1 = sheet.getRow(tempIndex[0]+2);// 获得数据行对象
                row1.getCell(tempIndex[1]).setCellStyle(columnResStyle);
            }
        }
        //让列宽随着导出的列长自动适应
        for (int colNum = 0; colNum < colNames.length; colNum++)
        {
            int columnWidth = sheet.getColumnWidth(colNum) / 256;
            for (int rowNum = 0; rowNum < sheet.getLastRowNum(); rowNum++) {
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
                    }
                }
            }
            if (colNum == 0) {
                sheet.setColumnWidth(colNum, (columnWidth - 2) * 256);
            } else {
                sheet.setColumnWidth(colNum, (columnWidth + 4) * 256);
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
