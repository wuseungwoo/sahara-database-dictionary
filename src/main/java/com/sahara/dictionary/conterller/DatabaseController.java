package com.sahara.dictionary.conterller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sahara.dictionary.bean.HistoryConnet;
import com.sahara.dictionary.bean.TableInfo;
import com.sahara.dictionary.dao.ConnectionFactory;
import com.sahara.dictionary.service.db2.Db2Executor;
import com.sahara.dictionary.service.mysql.BuildMysqlPdf;
import com.sahara.dictionary.service.oracleservice.OracleDatabase;
import com.sahara.dictionary.service.postgreSQL.BuildPgSqlPdf;
import com.sahara.dictionary.service.sqlserver.BuildSqlserverPDF;
import com.sahara.dictionary.service.sqlserver.WriteSqlserverMarkDown;
import com.sahara.dictionary.service.BuildPDF;
import com.sahara.dictionary.util.SqlExecutor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class DatabaseController {
    static Logger logger = LoggerFactory.getLogger(DatabaseController.class);
    @Resource
    private BuildPgSqlPdf buildPgSqlPdf;

    @RequestMapping(value = "/history.action",method = RequestMethod.GET)
    public String history(Model model){

        /*List<List> list = new ArrayList<>();
        List<String> list_sub = new ArrayList<String>();
        list_sub.add("history 001");list_sub.add("history 002");list_sub.add("history 003");
        list.add(list_sub);


        list_sub = new ArrayList<String>();
        list_sub.add("history 005");list_sub.add("history 006");list_sub.add("history 007");
        list.add(list_sub);

        list_sub = new ArrayList<String>();
        list_sub.add("history a001");list_sub.add("history a002");list_sub.add("history a003");
        list.add(list_sub);

        list_sub = new ArrayList<String>();
        list_sub.add("history a005");list_sub.add("history a006");list_sub.add("history a007");
        list.add(list_sub);*/

        List<List<HistoryConnet>> list =  read_recordHistory();

        model.addAttribute("history", list);
        return "history";
    }



    @RequestMapping("/login.action")
    public String login(Model model, String selector, String ip, String port, String password, String username, String database) {
        List<TableInfo> tableInfo = null;
        try {
            String markdown = null;
            switch (selector) {
                case "mysql":
                    //得到生成数据
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC&characterEncoding=utf8";
                    Connection connection = ConnectionFactory.getConnection(url, username, password, "mySql");
                    tableInfo = BuildMysqlPdf.getBuildPdfTableData(BuildMysqlPdf.getTables(connection, database));
                    break;
                case "oracle":
                    tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
                    break;
                case "SQL server":
                    long startTime = System.currentTimeMillis();
                    markdown = WriteSqlserverMarkDown.MakeMarkdownString(ip, database, port, username, password);
                    System.out.println("SqlserverMarkDown--->执行耗时：" + (System.currentTimeMillis() - startTime) + " 毫秒");
                    break;

                case "PostgreSQL":
                    markdown = buildPgSqlPdf.getPgMarkdown(ip, database, port, username, password);
                    break;
                case "DB2":
                    tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database.toUpperCase(), username, password);
                    break;
            }


            if (tableInfo !=null ){

                if (tableInfo.size() == 0) {
                    model.addAttribute("markdown", "## 数据库无数据");
                    return "markdown";
                }
                long startTime = System.currentTimeMillis();
                markdown = BuildPDF.writeMarkdown(tableInfo);
                System.out.println("writeMarkdown--->执行耗时：" + (System.currentTimeMillis() - startTime) + " 毫秒");
            }


            model.addAttribute("markdown", markdown);
            //记录历史连接
            HistoryConnet historyConnet = new HistoryConnet( selector,  ip,  port,  password,  username,  database);
            recordHistory(markdown,historyConnet);
            return "markdown";
        } catch (Exception e) {
            logger.error("error==>"+e);
            model.addAttribute("markdown", "### "+e.getMessage());
            return "markdown";
        }
    }

    @RequestMapping("/getDataBaseNameList")
    @ResponseBody
    public List<String> getDataBaseNmaeList(String selector,String ip, String port, String password, String username, String database){
        List<String> list = new ArrayList<>();
        try {
            switch (selector) {
                case "mysql":
                    return BuildMysqlPdf.getDataBaseName(ip,database,port,username,password);
                case "oracle":
                    list.add(database);
                    return list;
                case "SQL server":

                    return WriteSqlserverMarkDown.getDatabasesList(ip, database, port, username, password);

                case "PostgreSQL":
                    return BuildPgSqlPdf.getDataBaseName(ip,database,port,username,password);
                case "DB2":
                    return Db2Executor.databases(SqlExecutor.newDB2Connection(ip,Integer.valueOf(port),database,username,password));
            }

        } catch (Exception e) {
            logger.error("error==>"+e);
        }
        return list;
    }

    @RequestMapping("/getMarkdownString")
    @ResponseBody
    public String getMarkdownString(Model model, String selector, String ip, String port, String password, String username, String database) {
        List<TableInfo> tableInfo = null;
        try {
            switch (selector) {
                case "mysql":
                    //得到生成数据
                    String url = "jdbc:mysql://" + ip + ":" + port + "/" + database + "?useSSL=false&serverTimezone=UTC";
                    Connection connection = ConnectionFactory.getConnection(url, username, password, "mySql");
                    tableInfo = BuildMysqlPdf.getBuildPdfTableData(BuildMysqlPdf.getTables(connection, database));
                    break;
                case "oracle":
                    tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
                    break;
                case "SQL server":
                    return WriteSqlserverMarkDown.MakeMarkdownString(ip, database, port, username, password);
                case "PostgreSQL":
                    return  buildPgSqlPdf.getPgMarkdown(ip, database, port, username, password);
                case "DB2":
                    tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database.toUpperCase(), username, password);
                    break;
            }
            if (tableInfo!=null){
                if (tableInfo.size() == 0) {
                    return "## 数据库无数据";
                }
            }

            String markdown = BuildPDF.writeMarkdown(tableInfo);

            return markdown;
        } catch (Exception e) {
            logger.error("error==>"+e);
            return "### "+e.getMessage();
        }
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void testDownload(HttpServletResponse res, String selector, String ip, String port, String password, String username, String database) {
        //1.先生成pdf文件
        String filePath = System.getProperty("user.dir");
        try {
            switch (selector) {
                case "mysql":
                    //得到生成数据
                    BuildMysqlPdf.MakeMysqlPdf(ip, database, port, username, password,res);
                    break;
                case "oracle":
                    List<TableInfo> tableInfo = OracleDatabase.getTableInfo("jdbc:oracle:thin:@//" + ip + ":" + port + "/" + database + "", username, password);
                    if (tableInfo.size() == 0) {
                        return;
                    }
                    //带目录
                    BuildPDF.getDocumentBuild(tableInfo,res);
                    break;
                case "SQL server":
                    BuildSqlserverPDF.MakePdf(ip, database, port, username, password,res);
                    break;
                case "PostgreSQL":
                    buildPgSqlPdf.buildPdf(ip, database, port, username, password,res);
                    break;
                case "DB2":
                    List<TableInfo> Db2tableInfo = Db2Executor.getDB2Tables(ip, Integer.valueOf(port), database, username, password);
                    if (Db2tableInfo.size() == 0) {
                        return;
                    }
                    //带目录
                    BuildPDF.getDocumentBuild( Db2tableInfo,res);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error==>" + e.getMessage());
        }
    }

    /****************记录连接历史****************/
    //记录连接历史
    private void  recordHistory(String markdown,HistoryConnet historyConnet){
        try {
            if(!StringUtils.isEmpty(markdown) && markdown.length() > 30){
                //MD5
                historyConnet.setMd5Str(historyConnet.MakeMd5Str());

                ObjectMapper mapper = new ObjectMapper();
                String jsonString = mapper.writeValueAsString(historyConnet);
                //这里记录连接成功的记录
                File fileToWrite1 = FileUtils.getFile("history/history.txt");
                if(!fileToWrite1.getParentFile().exists()){
                    try {
//                        logger.info("writing lines to a file.");
                        fileToWrite1.getParentFile().mkdirs();
                        fileToWrite1.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //检查是否有重复
                List<String> list_string = FileUtils.readLines(fileToWrite1, Charset.defaultCharset());
                if(null != list_string && !list_string.contains(jsonString)){
                    Collection lines = new ArrayList<>();
                    lines.add(jsonString);
                    FileUtils.writeLines(fileToWrite1, lines,true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("记录连接历史错误..................");
        }
    }

    //读取连接历史
    private List<List<HistoryConnet>>  read_recordHistory(){
        try {
            List<List<HistoryConnet>> list = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();

            //这里记录连接成功的记录
            logger.info("writing lines to a file.");
            File fileToWrite1 = FileUtils.getFile("history/history.txt");
            if(!fileToWrite1.exists()){
                return null;
            }
            List<String> list_string = FileUtils.readLines(fileToWrite1, Charset.defaultCharset());
            if(null != list_string && list_string.size() > 0){

                List<HistoryConnet> list_sub = null;
                int count = list_string.size();
                for (int i = 0; i < count; i++) {
                    if( i % 4 == 0){
                        list.add(list_sub);
                        list_sub = new ArrayList<>();
                    }

                    HistoryConnet historyConnet = mapper.readValue(list_string.get(i), HistoryConnet.class);
                    list_sub.add(historyConnet);

                    //最后一次检验
                    if(i == count - 1  && list_sub.size() > 0){
                        list.add(list_sub);
                    }

                }

            }

            return list;

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("记录连接历史错误..................");
            return null;
        }
    }
}