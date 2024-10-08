package com.sahara.dictionary.service.sqlserver;

import com.sahara.dictionary.bean.sqlserver.SqlserverColumnInfo;
import com.sahara.dictionary.bean.sqlserver.SqlserverIndexInfo;
import com.sahara.dictionary.bean.sqlserver.SqlserverTabelInfo;
import com.sahara.dictionary.dao.sqlserver.SqlserverConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class WriteSqlserverMarkDown {

    static Logger logger = LoggerFactory.getLogger(WriteSqlserverMarkDown.class);

    /**
     * 生成Markdown
     *
     * @param ip       ：数据库连接的IP  例如：127.0.0.1 或者 localhost
     * @param dbName   例如: test
     * @param port     例如: 3306
     * @param userName 例如: root
     * @param passWord 例如: root
     * @param filePath 例如:  D:\ideaspace\export_dbInfo\src\main\resources\
     */
    public static void MakeMarkdown(String ip, String dbName, String port, String userName, String passWord, String filePath) {
        Connection connection = null;
        try {

            String dbURL = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbName + ";";
            //得到生成数据
            connection = SqlserverConnectionFactory.getConnection(dbURL, userName, passWord);
            String sqltabel = "SELECT t.name AS name,ep.value AS value FROM sys.tables t LEFT JOIN sys.extended_properties ep ON t.object_id = ep.major_id AND ep.minor_id = 0 AND ep.class = 1 ORDER BY t.name;";
            //String sqltabel="SELECT DISTINCT d.name,f.value FROM syscolumns a LEFT JOIN systypes b ON a.xusertype=b.xusertype INNER JOIN sysobjects d ON a.id=d.id AND d.xtype='U' AND d.name<> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault=e.id LEFT JOIN sys.extended_properties g ON a.id=G.major_id AND a.colid=g.minor_id LEFT JOIN sys.extended_properties f ON d.id=f.major_id AND f.minor_id=0 ORDER BY d.name;";
            long startTime = System.currentTimeMillis();
            List<SqlserverTabelInfo> list_table = GenerateDataBaseInfo.getTableInfo(connection, sqltabel);
            System.out.println("Sqlserver---getTableInfo--->执行耗时：" + (System.currentTimeMillis() - startTime) + " 毫秒");
            if (list_table.size() == 0) {
                return;
            }
            for (SqlserverTabelInfo tabelInfo : list_table) {
                //通过多线程异步进行并行执行
                Connection finalConnection = connection;
                CompletableFuture.runAsync(() -> {
                    //logger.info(tabelInfo.getTableName());
                    List<SqlserverColumnInfo> list_column = new ArrayList<SqlserverColumnInfo>();
                    String sqlcolumn = "SELECT (CASE WHEN a.colorder=1 THEN d.name ELSE NULL END) table_name,a.colorder column_num,a.name column_name,(CASE WHEN COLUMNPROPERTY(a.id,a.name,'IsIdentity')=1 THEN 'YES' ELSE '' END) is_identity,(CASE WHEN (\n" +
                            "SELECT COUNT (*) FROM sysobjects WHERE (name IN (\n" +
                            "SELECT name FROM sysindexes WHERE (id=a.id) AND (indid IN (\n" +
                            "SELECT indid FROM sysindexkeys WHERE (id=a.id) AND (colid IN (\n" +
                            "SELECT colid FROM syscolumns WHERE (id=a.id) AND (name=a.name))))))) AND (xtype='PK'))> 0 THEN 'YES' ELSE '' END) p_k,b.name type,a.length occupied_num,COLUMNPROPERTY(a.id,a.name,'PRECISION') AS length,isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0) AS scale,(CASE WHEN a.isnullable=1 THEN 'YES' ELSE '' END) is_null,isnull(e.text,'') default_value,isnull(g.[value],' ') AS decs,isnull(g.[class_desc],' ') AS class_desc FROM syscolumns a LEFT JOIN systypes b ON a.xtype=b.xusertype INNER JOIN sysobjects d ON a.id=d.id AND d.xtype='U' AND d.name<> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault=e.id LEFT JOIN sys.extended_properties g ON a.id=g.major_id AND a.colid=g.minor_id LEFT JOIN sys.extended_properties f ON d.id=f.class AND f.minor_id=0 WHERE d.name ='" + tabelInfo.getTableName() + "'";
                    try {
                        list_column = GenerateDataBaseInfo.getColumnInfo(finalConnection, sqlcolumn);
                        for (SqlserverColumnInfo s : list_column) {
                            //logger.info(s.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tabelInfo.setColumnList(list_column);
                });

                Connection finalConnection1 = connection;
                CompletableFuture.runAsync(() -> {
                    List<SqlserverIndexInfo> list_index = new ArrayList<SqlserverIndexInfo>();
                    String sqlindex = "SELECT index_name,index_desc,(LEFT (ind_col,LEN(ind_col)-1)+CASE WHEN include_col IS NOT NULL THEN ' INCLUDE ('+LEFT (include_col,LEN(include_col)-1)+')' ELSE '' END) AS index_keys FROM (\n" +
                            "SELECT i.name AS index_name,(\n" +
                            "SELECT CONVERT (VARCHAR (MAX),CASE WHEN i.index_id =1 THEN 'clustered' ELSE 'nonclustered' END+CASE WHEN i.ignore_dup_key <> 0 THEN ', ignore duplicate keys' ELSE '' END+CASE WHEN i.is_unique <> 0 THEN ', unique' ELSE '' END+CASE WHEN i.is_hypothetical <> 0 THEN ', hypothetical' ELSE '' END+CASE WHEN i.is_primary_key <> 0 THEN ', primary key' ELSE '' END+CASE WHEN i.is_unique_constraint <> 0 THEN ', unique key' ELSE '' END+CASE WHEN s.auto_created <> 0 THEN ', auto create' ELSE '' END+CASE WHEN s.no_recompute <> 0 THEN ', stats no recompute' ELSE '' END+' located on '+ISNULL(name,'')+CASE WHEN i.has_filter =1 THEN ', filter={'+i.filter_definition +'}' ELSE '' END) FROM sys.data_spaces WHERE data_space_id=i.data_space_id) AS 'index_desc',(\n" +
                            "SELECT INDEX_COL(OBJECT_NAME(i.object_id),i.index_id,key_ordinal),CASE WHEN is_descending_key=1 THEN N'(-)' ELSE N'' END+',' FROM sys.index_columns WHERE object_id=i.object_id AND index_id=i.index_id AND key_ordinal<> 0 ORDER BY key_ordinal FOR XML PATH ('')) AS ind_col,(\n" +
                            "SELECT col.name +',' FROM sys.index_columns inxc JOIN sys.columns col ON col.object_id=inxc.object_id AND col.column_id =inxc.column_id WHERE inxc.object_id=i.object_id AND inxc.index_id =i.index_id AND inxc.is_included_column =1 FOR XML PATH ('')) AS include_col FROM sys.indexes i JOIN sys.stats s ON i.object_id=s.object_id AND i.index_id =s.stats_id WHERE i.object_id=object_id('" + tabelInfo.getTableName() + "')) Ind ORDER BY index_name";
                    try {
                        //logger.info(sqlindex);
                        list_index = GenerateDataBaseInfo.getIndexInfo(finalConnection1, sqlindex);
                        for (SqlserverIndexInfo s : list_index) {
                            //logger.info(s.toString());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    tabelInfo.setIndexInfoList(list_index);
                });
            }
            startTime = System.currentTimeMillis();
            writeMarkdown(list_table, filePath);
            System.out.println("Sqlserver---writeMarkdown--->执行耗时：" + (System.currentTimeMillis() - startTime) + " 毫秒");

        } catch (Exception e) {
            e.printStackTrace();
            //logger.info("生成Markdown失败");
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 写markdown文件
     */
    public static void writeMarkdown(List<SqlserverTabelInfo> list, String filePath) {
        StringBuffer markdown = new StringBuffer();
        String res1 = "|:------:|:------:|:------:|:------:|:------:|:------:|:------:|" + "\n";
        int i = 1;
        for (SqlserverTabelInfo info : list) {
            StringBuffer oneTble = new StringBuffer();
            oneTble.append("##" + i + "." + info.getTableName() + "\n" + "基本信息:" + info.getValue() + " "
                    + "\n\n" + "|序列|列名|主键|类型|可空|默认值|注释|" + "\n");
            oneTble.append(res1);
            List<SqlserverColumnInfo> columnInfos = info.getColumnList();
            //拼接列
            for (SqlserverColumnInfo Column : columnInfos) {
                oneTble.append("|").append(Column.getColumn_num()).append("|").
                        append(Column.getColumn_name()).append("|").
                        append(Column.getP_k()).append("|").
                        append(Column.getType()).append("|").
                        append(Column.getIs_null()).append("|").
                        append(Column.getDefault_value()).append("|").
                        append(Column.getDecs()).append("|").
                        append("\n");
            }
            //拼接索引
            oneTble.append("\n");
            oneTble.append("|序列|索引名|描述|包含字段|" + "\n");
            oneTble.append("|:------:|:------:|:------:|:------:|" + "\n");
            List<SqlserverIndexInfo> indexInfolist = info.getIndexInfoList();
            int j = 1;
            for (SqlserverIndexInfo indexInfo : indexInfolist) {
                oneTble.append("|").append(j).append("|").
                        append(indexInfo.getIndex_name()).append("|").
                        append(indexInfo.getIndex_desc()).append("|").
                        append(indexInfo.getIndex_keys()).append("|").
                        append("\n");
                j++;
            }
            i++;
            oneTble.append("\n");
            markdown.append(oneTble);
            createDir(filePath + "\\" + info.getTableName() + ".txt", oneTble.toString());
        }
        //目录
        markdown.insert(0, "[TOC]\n");
        //logger.info("表信息\n" + markdown.toString());
        createDir(filePath + "\\allTable.txt", markdown.toString());
    }

    public static String writeMarkdownString(List<SqlserverTabelInfo> list) {
        long startTime = System.currentTimeMillis();
        StringBuffer markdown = new StringBuffer();
        String res1 = "|:------:|:------:|:------:|:------:|:------:|:------:|:------:|" + "\n";
        int i = 1;
        for (SqlserverTabelInfo info : list) {
            StringBuffer oneTble = new StringBuffer();
            oneTble.append("##" + i + "." + info.getTableName() + " " + info.getValue() + "\n" + "基本信息:" + info.getValue() + " "
                    + "\n\n" + "|序列|列名|主键|类型|可空|默认值|注释|" + "\n");
            oneTble.append(res1);
            List<SqlserverColumnInfo> columnInfos = info.getColumnList();
            //拼接列
            for (SqlserverColumnInfo Column : columnInfos) {
                oneTble.append("|").append(Column.getColumn_num()).append("|").
                        append(Column.getColumn_name()).append("|").
                        append(Column.getP_k()).append("|").
                        append(Column.getType()).append("|").
                        append(Column.getIs_null()).append("|").
                        append(Column.getDefault_value()).append("|").
                        append(Column.getDecs()).append("|").
                        append("\n");
            }
            //拼接索引
            oneTble.append("\n");
            oneTble.append("|序列|索引名|描述|包含字段|" + "\n");
            oneTble.append("|:------:|:------:|:------:|:------:|" + "\n");
            List<SqlserverIndexInfo> indexInfolist = info.getIndexInfoList();
            int j = 1;
            for (SqlserverIndexInfo indexInfo : indexInfolist) {
                oneTble.append("|").append(j).append("|").
                        append(indexInfo.getIndex_name()).append("|").
                        append(indexInfo.getIndex_desc()).append("|").
                        append(indexInfo.getIndex_keys()).append("|").
                        append("\n");
                j++;
            }
            i++;
            oneTble.append("\n");
            markdown.append(oneTble);
        }
        //目录
        markdown.insert(0, "[TOC]\n");
        //logger.info("表信息\n" + markdown.toString());
        System.out.println("Sqlserver---writeMarkdownString--->执行耗时：" + (System.currentTimeMillis() - startTime) + " 毫秒");
        return markdown.toString();

    }

    /**
     * 创建文件夹
     *
     * @param fileName
     * @param content
     */
    public static void createDir(String fileName, String content) {
        File file = new File(fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                //写文件
                FileWriter writer = new FileWriter(fileName);
                writer.write(content);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * @Author zhou
     * @Description //TODO
     * @Date 16:47 2019/9/5
     * @Param [ip, dbName, port, userName, passWord]
     * @return void
     **/
    public static String MakeMarkdownString(String ip, String dbName, String port, String userName, String passWord) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Connection connection = null;
        try {
            String dbURL = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbName + ";";
            //得到生成数据
            connection = SqlserverConnectionFactory.getConnection(dbURL, userName, passWord);
            String sqltabel = "SELECT t.name AS name,ep.value AS value FROM sys.tables t LEFT JOIN sys.extended_properties ep ON t.object_id = ep.major_id AND ep.minor_id = 0 AND ep.class = 1 ORDER BY t.name;";
            //String sqltabel="SELECT DISTINCT d.name,f.value FROM syscolumns a LEFT JOIN systypes b ON a.xusertype=b.xusertype INNER JOIN sysobjects d ON a.id=d.id AND d.xtype='U' AND d.name<> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault=e.id LEFT JOIN sys.extended_properties g ON a.id=G.major_id AND a.colid=g.minor_id LEFT JOIN sys.extended_properties f ON d.id=f.major_id AND f.minor_id=0 ORDER BY d.name;";
            long startTime = System.currentTimeMillis();
            List<SqlserverTabelInfo> list_table = GenerateDataBaseInfo.getTableInfo(connection, sqltabel);
            System.out.println("Sqlserver---getTableInfo--->执行耗时：" + (System.currentTimeMillis() - startTime) + " 毫秒");//69 毫秒
            if (list_table.size() == 0) {
                return "## 数据库无数据";
            }
            startTime = System.currentTimeMillis();
            List<CompletableFuture<SqlserverTabelInfo>> allCompletableFuture = new ArrayList<>();
            for (SqlserverTabelInfo tabelInfo : list_table) {
                //通过多线程异步进行并行执行
                //logger.info(tabelInfo.getTableName());
                Connection finalConnection = connection;
                CompletableFuture<SqlserverTabelInfo> completableFuture = CompletableFuture.supplyAsync(() -> {
                    //获取表属性结构信息
                    return againTableAllInfo(finalConnection, tabelInfo);
                });

                /*CompletableFuture<SqlserverTabelInfo> completableFuture = CompletableFuture.supplyAsync(() -> {
                    //获取表属性结构信息
                    return againColumnInfo(finalConnection, tabelInfo);
                }).thenCompose(result -> CompletableFuture.supplyAsync(() -> {
                    //获取表索引信息
                    return againIndexInfo(finalConnection, result);
                }));*/
                //收集任务结果
                allCompletableFuture.add(completableFuture);
            }
            // allOf 所有线程执行完成
            CompletableFuture<Void> allFuture = CompletableFuture.allOf(allCompletableFuture.toArray(new CompletableFuture[allCompletableFuture.size()]));
            CompletableFuture<List<SqlserverTabelInfo>> resultFuture = allFuture.thenApply(v -> allCompletableFuture.stream().map(future -> future.join()).collect(Collectors.toList()));
            System.out.println("Sqlserver---获取表信息以及索引信息--->执行耗时：" + (System.currentTimeMillis() - startTime) + " 毫秒");//69 毫秒
            //组装markdown文本内容
            return writeMarkdownString(resultFuture.get());


        } catch (Exception e) {
            e.printStackTrace();
            //logger.info("生成Markdown失败");
        } finally {
            try {
                if (null != connection) {
                    logger.info("关闭SQLconnection............................................");
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stopWatch.stop();
            logger.info("MakeMarkdownString 主方法执行时间：" + stopWatch.getTotalTimeMillis() + "毫秒");
        }
        return "";
    }

    /*
     * @Author zhou
     * @Description 数据库名称
     * @Date 11:00 2019/9/9
     * @Param [ip, dbName, port, userName, passWord]
     * @return java.util.List<java.lang.String>
     **/
    public static List<String> getDatabasesList(String ip, String dbName, String port, String userName, String passWord) {
        Connection connection = null;
        List<String> list = new ArrayList<>();
        try {
            String dbURL = "jdbc:sqlserver://" + ip + ":" + port + ";DatabaseName=" + dbName + ";";
            connection = SqlserverConnectionFactory.getConnection(dbURL, userName, passWord);
            String sqltabel = "select name from sysdatabases where name not in('master','model','msdb','tempdb');";
            list = GenerateDataBaseInfo.getDataBaseList(connection, sqltabel);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    /*********************************************提炼方法******************************************************/
    /**
     * 获取表属性结构信息
     *
     * @param finalConnection
     * @param tabelInfo
     */
    private static SqlserverTabelInfo againColumnInfo(Connection finalConnection, SqlserverTabelInfo tabelInfo) {
        //logger.info("againColumnInfo--->TableName:{}",tabelInfo.getTableName());
        List<SqlserverColumnInfo> list_column = new ArrayList<SqlserverColumnInfo>();
        String sqlcolumn = "SELECT (CASE WHEN a.colorder=1 THEN d.name ELSE NULL END) table_name,a.colorder column_num,a.name column_name,(CASE WHEN COLUMNPROPERTY(a.id,a.name,'IsIdentity')=1 THEN 'YES' ELSE '' END) is_identity,(CASE WHEN (\n" +
                "SELECT COUNT (*) FROM sysobjects WHERE (name IN (\n" +
                "SELECT name FROM sysindexes WHERE (id=a.id) AND (indid IN (\n" +
                "SELECT indid FROM sysindexkeys WHERE (id=a.id) AND (colid IN (\n" +
                "SELECT colid FROM syscolumns WHERE (id=a.id) AND (name=a.name))))))) AND (xtype='PK'))> 0 THEN 'YES' ELSE '' END) p_k,b.name type,a.length occupied_num,COLUMNPROPERTY(a.id,a.name,'PRECISION') AS length,isnull(COLUMNPROPERTY(a.id,a.name,'Scale'),0) AS scale,(CASE WHEN a.isnullable=1 THEN 'YES' ELSE '' END) is_null,isnull(e.text,'') default_value,isnull(g.[value],' ') AS decs,isnull(g.[class_desc],' ') AS class_desc FROM syscolumns a LEFT JOIN systypes b ON a.xtype=b.xusertype INNER JOIN sysobjects d ON a.id=d.id AND d.xtype='U' AND d.name<> 'dtproperties' LEFT JOIN syscomments e ON a.cdefault=e.id LEFT JOIN sys.extended_properties g ON a.id=g.major_id AND a.colid=g.minor_id LEFT JOIN sys.extended_properties f ON d.id=f.class AND f.minor_id=0 WHERE d.name ='" + tabelInfo.getTableName() + "'";
        try {
            list_column = GenerateDataBaseInfo.getColumnInfo(finalConnection, sqlcolumn);
            //for (SqlserverColumnInfo s : list_column) {
            //logger.info(s.toString());
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabelInfo.setColumnList(list_column);
        return tabelInfo;
    }

    /**
     * 获取表索引信息
     *
     * @param finalConnection
     * @param tabelInfo
     */
    private static SqlserverTabelInfo againIndexInfo(Connection finalConnection, SqlserverTabelInfo tabelInfo) {
        //logger.info("againIndexInfo--->TableName:{}",tabelInfo.getTableName());
        List<SqlserverIndexInfo> list_index = new ArrayList<SqlserverIndexInfo>();
        String sqlindex = "SELECT index_name,index_desc,(LEFT (ind_col,LEN(ind_col)-1)+CASE WHEN include_col IS NOT NULL THEN ' INCLUDE ('+LEFT (include_col,LEN(include_col)-1)+')' ELSE '' END) AS index_keys FROM (\n" +
                "SELECT i.name AS index_name,(\n" +
                "SELECT CONVERT (VARCHAR (MAX),CASE WHEN i.index_id =1 THEN 'clustered' ELSE 'nonclustered' END+CASE WHEN i.ignore_dup_key <> 0 THEN ', ignore duplicate keys' ELSE '' END+CASE WHEN i.is_unique <> 0 THEN ', unique' ELSE '' END+CASE WHEN i.is_hypothetical <> 0 THEN ', hypothetical' ELSE '' END+CASE WHEN i.is_primary_key <> 0 THEN ', primary key' ELSE '' END+CASE WHEN i.is_unique_constraint <> 0 THEN ', unique key' ELSE '' END+CASE WHEN s.auto_created <> 0 THEN ', auto create' ELSE '' END+CASE WHEN s.no_recompute <> 0 THEN ', stats no recompute' ELSE '' END+' located on '+ISNULL(name,'')+CASE WHEN i.has_filter =1 THEN ', filter={'+i.filter_definition +'}' ELSE '' END) FROM sys.data_spaces WHERE data_space_id=i.data_space_id) AS 'index_desc',(\n" +
                "SELECT INDEX_COL(OBJECT_NAME(i.object_id),i.index_id,key_ordinal),CASE WHEN is_descending_key=1 THEN N'(-)' ELSE N'' END+',' FROM sys.index_columns WHERE object_id=i.object_id AND index_id=i.index_id AND key_ordinal<> 0 ORDER BY key_ordinal FOR XML PATH ('')) AS ind_col,(\n" +
                "SELECT col.name +',' FROM sys.index_columns inxc JOIN sys.columns col ON col.object_id=inxc.object_id AND col.column_id =inxc.column_id WHERE inxc.object_id=i.object_id AND inxc.index_id =i.index_id AND inxc.is_included_column =1 FOR XML PATH ('')) AS include_col FROM sys.indexes i JOIN sys.stats s ON i.object_id=s.object_id AND i.index_id =s.stats_id WHERE i.object_id=object_id('" + tabelInfo.getTableName() + "')) Ind ORDER BY index_name";
        try {
            //logger.info(sqlindex);
            list_index = GenerateDataBaseInfo.getIndexInfo(finalConnection, sqlindex);
            //for (SqlserverIndexInfo s : list_index) {
            //logger.info(s.toString());
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }
        tabelInfo.setIndexInfoList(list_index);
        return tabelInfo;
    }


    private static SqlserverTabelInfo againTableAllInfo(Connection connection, SqlserverTabelInfo tabelInfo){
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