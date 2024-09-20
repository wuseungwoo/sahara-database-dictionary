package com.sahara.dictionary.service.sqlserver;

import com.github.houbb.markdown.toc.util.StringUtil;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.DottedLineSeparator;
import com.sahara.dictionary.bean.sqlserver.SqlserverColumnInfo;
import com.sahara.dictionary.bean.sqlserver.SqlserverIndexInfo;
import com.sahara.dictionary.bean.sqlserver.SqlserverTabelInfo;
import com.sahara.dictionary.dao.sqlserver.SqlserverConnectionFactory;
import com.sahara.dictionary.service.BuildPDF;
import com.sahara.dictionary.service.ContentEvent;
import com.sahara.dictionary.service.IndexEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.sahara.dictionary.service.BuildPDF.getChineseFontAsStyle;

public class BuildSqlserverPDF {

    static Logger logger = LoggerFactory.getLogger(BuildSqlserverPDF.class);

    /**
     * 生成PDF
     *
     * @param ip       ：数据库连接的IP  例如：127.0.0.1 或者 localhost
     * @param dbName   例如: test
     * @param port     例如: 3306
     * @param userName 例如: root
     * @param passWord 例如: root
     */
    public static void MakePdf(String ip, String dbName, String port, String userName, String passWord, HttpServletResponse response) {
        Connection connection = null;
        try {

            String dbURL = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbName + ";";
            //得到生成数据
            connection = SqlserverConnectionFactory.getConnection(dbURL, userName, passWord);

            String sqltabel = "SELECT DISTINCT d.name,f.value FROM syscolumns a LEFT JOIN systypes b ON a.xusertype=b.xusertype INNER JOIN sysobjects d ON a.id=d.id AND d.xtype='U' AND d.name<> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault=e.id LEFT JOIN sys.extended_properties g ON a.id=G.major_id AND a.colid=g.minor_id LEFT JOIN sys.extended_properties f ON d.id=f.major_id AND f.minor_id=0 ORDER BY d.name;";
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            List<SqlserverTabelInfo> list_table = GenerateDataBaseInfo.getTableInfo(connection, sqltabel);
            stopWatch.stop();
            System.out.println("获取SQLSERVER表结构列表--->执行时间：" + stopWatch.getTotalTimeMillis() + "毫秒");
            if (list_table.size() == 0) {
                return;
            }
            long startTime = System.currentTimeMillis();
            List<CompletableFuture<SqlserverTabelInfo>> allCompletableFuture = new ArrayList<>();
            for (SqlserverTabelInfo tabelInfo : list_table) {
                //通过多线程异步进行并行执行
                //logger.info(tabelInfo.getTableName());
                Connection finalConnection = connection;
                CompletableFuture<SqlserverTabelInfo> completableFuture = CompletableFuture.supplyAsync(() -> {
                    //获取表属性结构信息
                    return againTableAllInfo(finalConnection, tabelInfo);
                });
                //收集任务结果
                allCompletableFuture.add(completableFuture);
            }
            // allOf 所有线程执行完成
            CompletableFuture<Void> allFuture = CompletableFuture.allOf(allCompletableFuture.toArray(new CompletableFuture[allCompletableFuture.size()]));
            CompletableFuture<List<SqlserverTabelInfo>> resultFuture = allFuture.thenApply(v -> allCompletableFuture.stream().map(future -> future.join()).collect(Collectors.toList()));
            System.out.println("Sqlserver---获取表信息以及索引信息--->执行耗时：" + (System.currentTimeMillis() - startTime) + " 毫秒");//69 毫秒
                /*logger.info(Ta.getTableName());
                List<SqlserverColumnInfo> list_column = new ArrayList<SqlserverColumnInfo>();
                List<SqlserverIndexInfo> list_index = new ArrayList<SqlserverIndexInfo>();
                String sqlcolumn = "SELECT (CASE WHEN a.colorder=1 THEN d.name ELSE NULL END) table_name,a.colorder column_num,a.name column_name,(CASE WHEN COLUMNPROPERTY(a.id,a.name,'IsIdentity')=1 THEN 'YES' ELSE '' END) is_identity,(CASE WHEN (\n" +
                        "SELECT COUNT (*) FROM sysobjects WHERE (name IN (\n" +
                        "SELECT name FROM sysindexes WHERE (id=a.id) AND (indid IN (\n" +
                        "SELECT indid FROM sysindexkeys WHERE (id=a.id) AND (colid IN (\n" +
                        "SELECT colid FROM syscolumns WHERE (id=a.id) AND (name=a.name))))))) AND (xtype='PK'))> 0 THEN 'YES' ELSE '' END) p_k,b.name type,a.length occupied_num,COLUMNPROPERTY(a.id,a.name,'PRECISION') AS length,isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0) AS scale,(CASE WHEN a.isnullable=1 THEN 'YES' ELSE '' END) is_null,isnull(e.text,'') default_value,isnull(g.[value],' ') AS decs,isnull(g.[class_desc],' ') AS class_desc FROM syscolumns a LEFT JOIN systypes b ON a.xtype=b.xusertype INNER JOIN sysobjects d ON a.id=d.id AND d.xtype='U' AND d.name<> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault=e.id LEFT JOIN sys.extended_properties g ON a.id=g.major_id AND a.colid=g.minor_id LEFT JOIN sys.extended_properties f ON d.id=f.class AND f.minor_id=0 WHERE d.name ='" + Ta.getTableName() + "'";
                try {
                    list_column = GenerateDataBaseInfo.getColumnInfo(connection, sqlcolumn);
                    for (SqlserverColumnInfo s : list_column) {
                        logger.info(s.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String sqlindex = "SELECT index_name,index_desc,(LEFT (ind_col,LEN(ind_col)-1)+CASE WHEN include_col IS NOT NULL THEN ' INCLUDE ('+LEFT (include_col,LEN(include_col)-1)+')' ELSE '' END) AS index_keys FROM (\n" +
                        "SELECT i.name AS index_name,(\n" +
                        "SELECT CONVERT (VARCHAR (MAX),CASE WHEN i.index_id =1 THEN 'clustered' ELSE 'nonclustered' END+CASE WHEN i.ignore_dup_key <> 0 THEN ', ignore duplicate keys' ELSE '' END+CASE WHEN i.is_unique <> 0 THEN ', unique' ELSE '' END+CASE WHEN i.is_hypothetical <> 0 THEN ', hypothetical' ELSE '' END+CASE WHEN i.is_primary_key <> 0 THEN ', primary key' ELSE '' END+CASE WHEN i.is_unique_constraint <> 0 THEN ', unique key' ELSE '' END+CASE WHEN s.auto_created <> 0 THEN ', auto create' ELSE '' END+CASE WHEN s.no_recompute <> 0 THEN ', stats no recompute' ELSE '' END+' located on '+ISNULL(name,'')+CASE WHEN i.has_filter =1 THEN ', filter={'+i.filter_definition +'}' ELSE '' END) FROM sys.data_spaces WHERE data_space_id=i.data_space_id) AS 'index_desc',(\n" +
                        "SELECT INDEX_COL(OBJECT_NAME(i.object_id),i.index_id,key_ordinal),CASE WHEN is_descending_key=1 THEN N'(-)' ELSE N'' END+',' FROM sys.index_columns WHERE object_id=i.object_id AND index_id=i.index_id AND key_ordinal<> 0 ORDER BY key_ordinal FOR XML PATH ('')) AS ind_col,(\n" +
                        "SELECT col.name +',' FROM sys.index_columns inxc JOIN sys.columns col ON col.object_id=inxc.object_id AND col.column_id =inxc.column_id WHERE inxc.object_id=i.object_id AND inxc.index_id =i.index_id AND inxc.is_included_column =1 FOR XML PATH ('')) AS include_col FROM sys.indexes i JOIN sys.stats s ON i.object_id=s.object_id AND i.index_id =s.stats_id WHERE i.object_id=object_id('" + Ta.getTableName() + "')) Ind ORDER BY index_name";
                try {
                    list_index = GenerateDataBaseInfo.getIndexInfo(connection, sqlindex);
                    for (SqlserverIndexInfo s : list_index) {
                        logger.info(s.toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Ta.setColumnList(list_column);
                Ta.setIndexInfoList(list_index);}*/
            build(resultFuture.get(), response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("生成PDF失败");
        } finally {
            try {
                if (null != connection) {
                    logger.info("关闭SQLconnection............................................");
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void build(List<SqlserverTabelInfo> tableInfos, HttpServletResponse response) throws Exception {
        //String fontDir = BuildPDF.class.getResource("/").getPath().replaceAll("target/classes/", "");
        //fontDir += "src/main/resources/";

        // 从类路径加载字体文件
        //ClassPathResource resource = new ClassPathResource("verdana.ttf");
        // String path = resource.getURI().getPath();
        //System.out.println("resource.getURI().getPath()=="+path);
        //InputStream fontStream = resource.getInputStream();
        //BaseFont bfChinese = BaseFont.createFont(path, BaseFont.MACROMAN, BaseFont.NOT_EMBEDDED);
        //BaseFont bfChinese = BaseFont.createFont(fontDir + "font" + File.separator + "verdana.ttf", BaseFont.MACROMAN, BaseFont.NOT_EMBEDDED);
        BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font font = new Font(bfChinese, 12, Font.BOLDITALIC);
        // 设置类型，加粗
        font.setStyle(Font.NORMAL);
        Font cnFont = getChineseFontAsStyle(BaseColor.BLACK, 16);
        //页面大小
        Rectangle rect = new Rectangle(PageSize.A4).rotate();
        //页面背景色
        rect.setBackgroundColor(new BaseColor(0xFF, 0xFF, 0xDE));
        //设置边框颜色
        rect.setBorderColor(new BaseColor(0xFF, 0xFF, 0xDE));
        Document doc = new Document(rect);
        PdfWriter contentWriter = PdfWriter.getInstance(doc, new ByteArrayOutputStream());
        //设置事件
        ContentEvent event = new ContentEvent();
        contentWriter.setPageEvent(event);
        //存目录监听 开始
        doc.open();
        int order = 1;
        List<Chapter> chapterList = new ArrayList<Chapter>();
        //根据chapter章节分页
        //表格
        //设置表格模板
        String[] tableHeader = {"序列", "列名", "主键", "类型", "可空", "默认值", "注释"};
        String[] indexHeader = {"序列", "索引名", "描述", "包含字段"};
        for (SqlserverTabelInfo tableInfo : tableInfos) {

            Chapter chapter = new Chapter(new Paragraph(tableInfo.getTableName()), order);
            //设置跳转地址
            Phrase point = new Paragraph("基本信息:", cnFont);
            Anchor tome = new Anchor(point);
            tome.setName(tableInfo.getTableName());
            Phrase engine = new Phrase("  ", font);
            Phrase type = new Phrase(" ", font);
            Phrase description = new Phrase(tableInfo.getValue(), getChineseFontAsStyle(BaseColor.BLACK, 16));
            //组装基本数据
            Paragraph contentInfo = new Paragraph();
            contentInfo.add(tome);
            contentInfo.add(engine);
            contentInfo.add(type);
            contentInfo.add(description);
            contentInfo.add(new Paragraph(" "));
            chapter.add(contentInfo);
            chapter.add(new Paragraph(""));
            //组装表格
            Paragraph tableParagraph = new Paragraph();
            //设置表格
            PdfPTable table = BuildPDF.setTableHeader(tableHeader, getChineseFontAsStyle(BaseColor.BLACK, 16));
            //设置列信息
            BuildColumnCell(table, font, tableInfo);
            tableParagraph.add(table);
            chapter.add(tableParagraph);
            //设置索引表
            Paragraph blankTwo = new Paragraph("\n\n");
            chapter.add(blankTwo);
            PdfPTable indexTable = BuildPDF.setTableHeader(indexHeader, getChineseFontAsStyle(BaseColor.BLACK, 16));
            table.setWidthPercentage(100);
            indexTable = BuildIndexCell(indexTable, BuildPDF.getFontAsStyle(BaseColor.RED, 10), tableInfo);
            Paragraph indexTableParagraph = new Paragraph();
            indexTableParagraph.add(indexTable);
            chapter.add(indexTableParagraph);

            //加入文档中
            doc.add(chapter);
            //保存章节内容
            chapterList.add(chapter);
            order++;
        }
        doc.close();
        //存目录监听 结束


        Document document = new Document(rect);
        PdfWriter writer = PdfWriter.getInstance(document, new ByteArrayOutputStream());
        IndexEvent indexEvent = new IndexEvent();
        writer.setPageEvent(indexEvent);
        response.setContentType("application/pdf");
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        //添加章节目录
        Chapter indexChapter = new Chapter(new Paragraph("", BuildPDF.getFontAsStyle(BaseColor.BLACK, 18)), 0);
        indexChapter.setNumberDepth(-1);
        // 设置数字深度
        int i = 1;
        for (Map.Entry<String, Integer> index : event.index.entrySet()) {
            String key = index.getKey();
            String[] keyValue = key.split(" ");
            //设置跳转显示名称
            int pageNo = index.getValue();
            Chunk pointChunk = new Chunk(new DottedLineSeparator());
            Chunk pageNoChunk = new Chunk(pageNo + "");

            String tempDescription = key;
            if (!StringUtil.isEmpty(tableInfos.get(i - 1).getValue())) {
                tempDescription += "(" + tableInfos.get(i - 1).getValue() + ")";
            }
            Paragraph jumpParagraph = new Paragraph(tempDescription, getChineseFontAsStyle(BaseColor.BLACK, 12));
            jumpParagraph.add(pointChunk);
            jumpParagraph.add(pageNoChunk);
            Anchor anchor = new Anchor(jumpParagraph);
            String jump = keyValue[keyValue.length - 1].trim();
            //设置跳转链接
            anchor.setReference("#" + jump);
            indexChapter.add(anchor);
            indexChapter.add(new Paragraph());
            i++;
        }
        document.add(indexChapter);
        document.newPage();
        //添加内容
        for (Chapter c : chapterList) {
            indexEvent.setBody(true);
            document.add(c);
        }
        document.close();
    }

    /*
     * @Author zhou
     * @Description 循环生成列信息
     * @Date 11:19 2019/8/30
     * @Param [table, font, tableInfo]
     * @return com.itextpdf.text.pdf.PdfPTable
     **/
    public static PdfPTable BuildColumnCell(PdfPTable table, Font font, SqlserverTabelInfo tableInfo) {

        List<SqlserverColumnInfo> columnList = tableInfo.getColumnList();

        for (SqlserverColumnInfo columnInfo : columnList) {
            Font cnFont = getChineseFontAsStyle(BaseColor.BLACK, 12);
            if ("YES".equals(columnInfo.getP_k())) {
                cnFont.setColor(BaseColor.RED);
            } else {
                cnFont.setColor(BaseColor.BLACK);
            }
            PdfPCell cell1 = new PdfPCell(new Paragraph(columnInfo.getColumn_num(), cnFont));
            table.addCell(cell1);
            PdfPCell cell2 = new PdfPCell(new Paragraph(columnInfo.getColumn_name(), cnFont));
            table.addCell(cell2);
            PdfPCell cell3 = new PdfPCell(new Paragraph(columnInfo.getP_k(), cnFont));
            table.addCell(cell3);
            PdfPCell cell4 = new PdfPCell(new Paragraph(columnInfo.getType() + "(" + columnInfo.getLength() + ")", cnFont));
            table.addCell(cell4);
            PdfPCell cell5 = new PdfPCell(new Paragraph(columnInfo.getIs_null(), cnFont));
            table.addCell(cell5);
            PdfPCell cell6 = new PdfPCell(new Paragraph(columnInfo.getDefault_value(), cnFont));
            table.addCell(cell6);
            PdfPCell cell7 = new PdfPCell(new Paragraph(columnInfo.getDecs(), cnFont));
            table.addCell(cell7);
        }
        return table;
    }

    /*
     * @Author zhou
     * @Description 循环生成索引信息
     * @Date 11:19 2019/8/30
     * @Param [table, font, tableInfo]
     * @return com.itextpdf.text.pdf.PdfPTable
     **/
    public static PdfPTable BuildIndexCell(PdfPTable table, Font font, SqlserverTabelInfo tableInfo) {

        List<SqlserverIndexInfo> indexInfos = tableInfo.getIndexInfoList();

        Font cnFont = getChineseFontAsStyle(BaseColor.BLACK, 12);
        int i = 1;
        for (SqlserverIndexInfo indexInfo : indexInfos) {
            PdfPCell cell1 = new PdfPCell(new Paragraph(i + ""));
            table.addCell(cell1);
            PdfPCell cell2 = new PdfPCell(new Paragraph(indexInfo.getIndex_name()));
            table.addCell(cell2);
            PdfPCell cell3 = new PdfPCell(new Paragraph(indexInfo.getIndex_desc()));
            table.addCell(cell3);
            PdfPCell cell4 = new PdfPCell(new Paragraph(indexInfo.getIndex_keys()));
            table.addCell(cell4);
            i++;
        }
        return table;
    }


    private static SqlserverTabelInfo againTableAllInfo(Connection connection, SqlserverTabelInfo tabelInfo) {
        String sql = "SELECT \n" +
                "    t.name AS table_name,\n" +
                "    c.column_id as column_num,\n" +
                "    c.name AS column_name,\n" +
                "    c.is_identity,\n" +
                "    -- 判断是否为主键\n" +
                "    CASE WHEN pk.column_id IS NOT NULL THEN 1 ELSE 0 END AS p_k,\n" +
                "    ty.name AS type,\n" +
                "    c.max_length as length,\n" +
                "    c.precision as occupied_num,\n" +
                "    c.scale,\n" +
                "    c.is_nullable as is_null,\n" +
                "    dc.definition AS default_value,\n" +
                "    ep.value AS decs,\n" +
                "\t\tep.class_desc,\n" +
                "    idx.name AS index_name,\n" +
                "    idx.type_desc AS index_desc\n" +
                "FROM \n" +
                "    sys.tables t\n" +
                "JOIN \n" +
                "    sys.columns c ON t.object_id = c.object_id\n" +
                "JOIN \n" +
                "    sys.types ty ON c.user_type_id = ty.user_type_id\n" +
                "LEFT JOIN (\n" +
                "    -- 获取主键信息\n" +
                "    SELECT \n" +
                "        ic.object_id,\n" +
                "        ic.column_id\n" +
                "    FROM \n" +
                "        sys.indexes i\n" +
                "    JOIN \n" +
                "        sys.index_columns ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id\n" +
                "    JOIN \n" +
                "        sys.key_constraints kc ON i.object_id = kc.parent_object_id AND i.name = kc.name\n" +
                "    WHERE \n" +
                "        kc.type = 'PK'\n" +
                ") pk ON c.object_id = pk.object_id AND c.column_id = pk.column_id\n" +
                "LEFT JOIN \n" +
                "    sys.default_constraints dc ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id\n" +
                "LEFT JOIN \n" +
                "    sys.extended_properties ep ON c.object_id = ep.major_id AND c.column_id = ep.minor_id AND ep.class = 1\n" +
                "LEFT JOIN (\n" +
                "    -- 获取索引信息\n" +
                "    SELECT \n" +
                "        i.object_id,\n" +
                "        ic.column_id,\n" +
                "        i.name,\n" +
                "        i.type_desc\n" +
                "    FROM \n" +
                "        sys.indexes i\n" +
                "    JOIN \n" +
                "        sys.index_columns ic ON i.object_id = ic.object_id AND i.index_id = ic.index_id\n" +
                ") idx ON c.object_id = idx.object_id AND c.column_id = idx.column_id \n" +
                "WHERE \n" +
                "    t.name = '" + tabelInfo.getTableName() + "';";
        try {
            GenerateDataBaseInfo.getTableInfoNew(connection, sql, tabelInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tabelInfo;
    }
}