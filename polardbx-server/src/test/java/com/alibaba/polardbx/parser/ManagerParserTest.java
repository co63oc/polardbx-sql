/*
 * Copyright [2013-2021], Alibaba Group Holding Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.polardbx.parser;

import com.alibaba.polardbx.manager.parser.ManagerParse;
import com.alibaba.polardbx.manager.parser.ManagerParseClear;
import com.alibaba.polardbx.manager.parser.ManagerParseReload;
import com.alibaba.polardbx.manager.parser.ManagerParseRollback;
import com.alibaba.polardbx.manager.parser.ManagerParseShow;
import com.alibaba.polardbx.manager.parser.ManagerParseStop;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author xianmao.hexm
 */
public class ManagerParserTest {

    @Test
    public void testIsSelect() {
        Assert.assertEquals(ManagerParse.SELECT, 0xff & ManagerParse.parse("select * from offer limit 1"));
        Assert.assertEquals(ManagerParse.SELECT, 0xff & ManagerParse.parse("SELECT * FROM OFFER LIMIT 1"));
        Assert.assertEquals(ManagerParse.SELECT, 0xff & ManagerParse.parse("SELECT * FROM OFFER limit 1"));
    }

    @Test
    public void testIsSet() {
        Assert.assertEquals(ManagerParse.SET, ManagerParse.parse("set names utf8"));
        Assert.assertEquals(ManagerParse.SET, ManagerParse.parse("SET NAMES UTF8"));
        Assert.assertEquals(ManagerParse.SET, ManagerParse.parse("set NAMES utf8"));
    }

    @Test
    public void testIsShow() {
        Assert.assertEquals(ManagerParse.SHOW, 0xff & ManagerParse.parse("show databases"));
        Assert.assertEquals(ManagerParse.SHOW, 0xff & ManagerParse.parse("SHOW DATABASES"));
        Assert.assertEquals(ManagerParse.SHOW, 0xff & ManagerParse.parse("SHOW databases"));
    }

    @Test
    public void testShowCommand() {
        Assert.assertEquals(ManagerParseShow.COMMAND, ManagerParseShow.parse("show @@command", 5));
        Assert.assertEquals(ManagerParseShow.COMMAND, ManagerParseShow.parse("SHOW @@COMMAND", 5));
        Assert.assertEquals(ManagerParseShow.COMMAND, ManagerParseShow.parse("show @@COMMAND", 5));
    }

    @Test
    public void testShowConnection() {
        Assert.assertEquals(ManagerParseShow.CONNECTION, ManagerParseShow.parse("show @@connection", 5));
        Assert.assertEquals(ManagerParseShow.CONNECTION, ManagerParseShow.parse("SHOW @@CONNECTION", 5));
        Assert.assertEquals(ManagerParseShow.CONNECTION, ManagerParseShow.parse("show @@CONNECTION", 5));
    }

    @Test
    public void testShowConnectionSQL() {
        Assert.assertEquals(ManagerParseShow.CONNECTION_SQL, ManagerParseShow.parse("show @@connection.sql", 5));
        Assert.assertEquals(ManagerParseShow.CONNECTION_SQL, ManagerParseShow.parse("SHOW @@CONNECTION.SQL", 5));
        Assert.assertEquals(ManagerParseShow.CONNECTION_SQL, ManagerParseShow.parse("show @@CONNECTION.Sql", 5));
    }

    @Test
    public void testShowDatabase() {
        Assert.assertEquals(ManagerParseShow.DATABASE, ManagerParseShow.parse("show @@database", 5));
        Assert.assertEquals(ManagerParseShow.DATABASE, ManagerParseShow.parse("SHOW @@DATABASE", 5));
        Assert.assertEquals(ManagerParseShow.DATABASE, ManagerParseShow.parse("show @@DATABASE", 5));
    }

    @Test
    public void testShowDataNode() {
        Assert.assertEquals(ManagerParseShow.DATANODE, ManagerParseShow.parse("show @@datanode", 5));
        Assert.assertEquals(ManagerParseShow.DATANODE, ManagerParseShow.parse("SHOW @@DATANODE", 5));
        Assert.assertEquals(ManagerParseShow.DATANODE, ManagerParseShow.parse("show @@DATANODE", 5));
        Assert.assertEquals(ManagerParseShow.DATANODE, ManagerParseShow.parse("show @@DATANODE   ", 5));
        Assert.assertEquals(ManagerParseShow.DATANODE_WHERE,
            0xff & ManagerParseShow.parse("show @@DATANODE WHERE SCHEMA=1", 5));
        Assert.assertEquals(ManagerParseShow.DATANODE_WHERE,
            0xff & ManagerParseShow.parse("show @@DATANODE WHERE schema =1", 5));
        Assert.assertEquals(ManagerParseShow.DATANODE_WHERE,
            0xff & ManagerParseShow.parse("show @@DATANODE WHERE SCHEMA= 1", 5));
    }

    @Test
    public void testShowDataSource() {
        Assert.assertEquals(ManagerParseShow.DATASOURCE, ManagerParseShow.parse("show @@datasource", 5));
        Assert.assertEquals(ManagerParseShow.DATASOURCE, ManagerParseShow.parse("SHOW @@DATASOURCE", 5));
        Assert.assertEquals(ManagerParseShow.DATASOURCE, ManagerParseShow.parse(" show  @@DATASOURCE ", 5));
        Assert.assertEquals(ManagerParseShow.DATASOURCE, ManagerParseShow.parse(" show  @@DATASOURCE   ", 5));
        Assert.assertEquals(ManagerParseShow.DATASOURCE_WHERE,
            0xff & ManagerParseShow.parse(" show  @@DATASOURCE where datanode = 1", 5));
        Assert.assertEquals(ManagerParseShow.DATASOURCE_WHERE,
            0xff & ManagerParseShow.parse(" show  @@DATASOURCE where datanode=1", 5));
        Assert.assertEquals(ManagerParseShow.DATASOURCE_WHERE,
            0xff & ManagerParseShow.parse(" show  @@DATASOURCE WHERE datanode = 1", 5));
        Assert.assertEquals(ManagerParseShow.DATASOURCE_WHERE,
            0xff & ManagerParseShow.parse(" show  @@DATASOURCE where DATAnode= 1 ", 5));
    }

    @Test
    public void testShowHelp() {
        Assert.assertEquals(ManagerParseShow.HELP, ManagerParseShow.parse("show @@help", 5));
        Assert.assertEquals(ManagerParseShow.HELP, ManagerParseShow.parse("SHOW @@HELP", 5));
        Assert.assertEquals(ManagerParseShow.HELP, ManagerParseShow.parse("show @@HELP", 5));
    }

    @Test
    public void testShowHeartbeat() {
        Assert.assertEquals(ManagerParseShow.HEARTBEAT, ManagerParseShow.parse("show @@heartbeat", 5));
        Assert.assertEquals(ManagerParseShow.HEARTBEAT, ManagerParseShow.parse("SHOW @@hearTBeat ", 5));
        Assert.assertEquals(ManagerParseShow.HEARTBEAT, ManagerParseShow.parse("  show   @@HEARTBEAT  ", 6));
    }

    @Test
    public void testShowParser() {
        Assert.assertEquals(ManagerParseShow.PARSER, ManagerParseShow.parse("show @@parser", 5));
        Assert.assertEquals(ManagerParseShow.PARSER, ManagerParseShow.parse("SHOW @@PARSER", 5));
        Assert.assertEquals(ManagerParseShow.PARSER, ManagerParseShow.parse("show @@PARSER", 5));
    }

    @Test
    public void testShowProcessor() {
        Assert.assertEquals(ManagerParseShow.PROCESSOR, ManagerParseShow.parse("show @@processor", 5));
        Assert.assertEquals(ManagerParseShow.PROCESSOR, ManagerParseShow.parse("SHOW @@PROCESSOR", 5));
        Assert.assertEquals(ManagerParseShow.PROCESSOR, ManagerParseShow.parse("show @@PROCESSOR", 5));
    }

    @Test
    public void testShowRouter() {
        Assert.assertEquals(ManagerParseShow.ROUTER, ManagerParseShow.parse("show @@router", 5));
        Assert.assertEquals(ManagerParseShow.ROUTER, ManagerParseShow.parse("SHOW @@ROUTER", 5));
        Assert.assertEquals(ManagerParseShow.ROUTER, ManagerParseShow.parse("show @@ROUTER", 5));
    }

    @Test
    public void testShowServer() {
        Assert.assertEquals(ManagerParseShow.SERVER, ManagerParseShow.parse("show @@server", 5));
        Assert.assertEquals(ManagerParseShow.SERVER, ManagerParseShow.parse("SHOW @@SERVER", 5));
        Assert.assertEquals(ManagerParseShow.SERVER, ManagerParseShow.parse("show @@SERVER", 5));
    }

    @Test
    public void testShowThreadPool() {
        Assert.assertEquals(ManagerParseShow.THREADPOOL, ManagerParseShow.parse("show @@threadPool", 5));
        Assert.assertEquals(ManagerParseShow.THREADPOOL, ManagerParseShow.parse("SHOW @@THREADPOOL", 5));
        Assert.assertEquals(ManagerParseShow.THREADPOOL, ManagerParseShow.parse("show @@THREADPOOL", 5));
    }

    @Test
    public void testShowServerExecutor() {
        Assert.assertEquals(ManagerParseShow.SERVER_EXECUTOR, ManagerParseShow.parse("show @@server_executor", 5));
        Assert.assertEquals(ManagerParseShow.SERVER_EXECUTOR, ManagerParseShow.parse("SHOW @@SERVER_EXECUTOR", 5));
        Assert.assertEquals(ManagerParseShow.SERVER_EXECUTOR, ManagerParseShow.parse("show @@SERVER_EXECUTOR", 5));
    }

    @Test
    public void testShowBackend() {
        Assert.assertEquals(ManagerParseShow.BACKEND, ManagerParseShow.parse("show @@backend", 5));
        Assert.assertEquals(ManagerParseShow.BACKEND, ManagerParseShow.parse("SHOW @@BACkend;", 5));
        Assert.assertEquals(ManagerParseShow.BACKEND, ManagerParseShow.parse("show @@BACKEND ", 5));
    }

    @Test
    public void testShowTimeCurrent() {
        Assert.assertEquals(ManagerParseShow.TIME_CURRENT, ManagerParseShow.parse("show @@time.current", 5));
        Assert.assertEquals(ManagerParseShow.TIME_CURRENT, ManagerParseShow.parse("SHOW @@TIME.CURRENT", 5));
        Assert.assertEquals(ManagerParseShow.TIME_CURRENT, ManagerParseShow.parse("show @@TIME.current", 5));
    }

    @Test
    public void testShowTimeStartUp() {
        Assert.assertEquals(ManagerParseShow.TIME_STARTUP, ManagerParseShow.parse("show @@time.startup", 5));
        Assert.assertEquals(ManagerParseShow.TIME_STARTUP, ManagerParseShow.parse("SHOW @@TIME.STARTUP", 5));
        Assert.assertEquals(ManagerParseShow.TIME_STARTUP, ManagerParseShow.parse("show @@TIME.startup", 5));
    }

    @Test
    public void testShowVersion() {
        Assert.assertEquals(ManagerParseShow.VERSION, ManagerParseShow.parse("show @@version", 5));
        Assert.assertEquals(ManagerParseShow.VERSION, ManagerParseShow.parse("SHOW @@VERSION", 5));
        Assert.assertEquals(ManagerParseShow.VERSION, ManagerParseShow.parse("show @@VERSION", 5));
    }

    @Test
    public void testShowSQL() {
        Assert.assertEquals(ManagerParseShow.SQL, ManagerParseShow.parse("show @@sql where id = -1079800749", 5));
        Assert.assertEquals(ManagerParseShow.SQL, ManagerParseShow.parse("SHOW @@SQL WHERE ID = -1079800749", 5));
        Assert.assertEquals(ManagerParseShow.SQL, ManagerParseShow.parse("show @@Sql WHERE ID = -1079800749", 5));
        Assert.assertEquals(ManagerParseShow.SQL, ManagerParseShow.parse("show @@sql where id=-1079800749", 5));
        Assert.assertEquals(ManagerParseShow.SQL, ManagerParseShow.parse("show @@sql where id   =-1079800749 ", 5));
    }

    @Test
    public void testShowSQLLOG() {
        Assert.assertEquals(ManagerParseShow.SQL_LOG, ManagerParseShow.parse("show @@sql.log", 5));
        Assert.assertEquals(ManagerParseShow.SQL_LOG, ManagerParseShow.parse("show @@sql.log ", 5));
        Assert.assertEquals(ManagerParseShow.SQL_LOG, ManagerParseShow.parse("show @@sql.lOG ", 5));
        Assert.assertEquals(ManagerParseShow.SQL_LOG, ManagerParseShow.parse("show @@sql.LOG ", 5));
    }

    @Test
    public void testShowSQLDisard() {
        Assert.assertEquals(ManagerParseShow.DISCARD_COUNT, ManagerParseShow.parse("show @@sql.discard", 5));
        Assert.assertEquals(ManagerParseShow.DISCARD_COUNT, ManagerParseShow.parse("show @@sql.discard ", 5));
        Assert.assertEquals(ManagerParseShow.DISCARD_COUNT, ManagerParseShow.parse("show @@sql.discArd ", 5));
        Assert.assertEquals(ManagerParseShow.DISCARD_COUNT, ManagerParseShow.parse("show @@sql.DisCArd ", 5));
    }

    @Test
    public void testShowSQLDetail() {
        Assert.assertEquals(ManagerParseShow.SQL_DETAIL,
            ManagerParseShow.parse("show @@sql.detail where id = -1079800749", 5));
        Assert.assertEquals(ManagerParseShow.SQL_DETAIL,
            ManagerParseShow.parse("SHOW @@SQL.DETAIL WHERE ID = -1079800749", 5));
        Assert.assertEquals(ManagerParseShow.SQL_DETAIL,
            ManagerParseShow.parse("show @@SQL.DETAIL WHERE ID = -1079800749", 5));
        Assert.assertEquals(ManagerParseShow.SQL_DETAIL,
            ManagerParseShow.parse("show @@sql.detail where id=1079800749 ", 5));
        Assert.assertEquals(ManagerParseShow.SQL_DETAIL,
            ManagerParseShow.parse("show @@sql.detail where id= -1079800749", 5));
    }

    @Test
    public void testShowSQLExecute() {
        Assert.assertEquals(ManagerParseShow.SQL_EXECUTE, ManagerParseShow.parse("show @@sql.execute", 5));
        Assert.assertEquals(ManagerParseShow.SQL_EXECUTE, ManagerParseShow.parse("SHOW @@SQL.EXECUTE", 5));
        Assert.assertEquals(ManagerParseShow.SQL_EXECUTE, ManagerParseShow.parse("show @@SQL.EXECUTE", 5));
    }

    @Test
    public void testShowSQLSlow() {
        Assert.assertEquals(ManagerParseShow.SQL_SLOW, ManagerParseShow.parse("show @@sql.slow", 5));
        Assert.assertEquals(ManagerParseShow.SQL_SLOW, ManagerParseShow.parse("SHOW @@SQL.SLOW", 5));
        Assert.assertEquals(ManagerParseShow.SQL_SLOW, ManagerParseShow.parse("SHOW @@sql.slow", 5));
    }

    @Test
    public void testShowVariables() {
        Assert.assertEquals(ManagerParseShow.VARIABLES, ManagerParseShow.parse("show variables", 5));
        Assert.assertEquals(ManagerParseShow.VARIABLES, ManagerParseShow.parse("SHOW VARIABLES", 5));
        Assert.assertEquals(ManagerParseShow.VARIABLES, ManagerParseShow.parse("show VARIABLES", 5));
    }

    @Test
    public void testShowCollation() {
        Assert.assertEquals(ManagerParseShow.COLLATION, ManagerParseShow.parse("show collation", 5));
        Assert.assertEquals(ManagerParseShow.COLLATION, ManagerParseShow.parse("SHOW COLLATION", 5));
        Assert.assertEquals(ManagerParseShow.COLLATION, ManagerParseShow.parse("show COLLATION", 5));
    }

    @Test
    public void testSwitchPool() {
        Assert.assertEquals(ManagerParse.SWITCH, 0xff & ManagerParse.parse("switch @@pool offer2$0-2"));
        Assert.assertEquals(ManagerParse.SWITCH, 0xff & ManagerParse.parse("SWITCH @@POOL offer2$0-2"));
        Assert.assertEquals(ManagerParse.SWITCH, 0xff & ManagerParse.parse("switch @@pool offer2$0-2 :2"));
    }

    @Test
    public void testComment() {
        Assert.assertEquals(ManagerParse.SWITCH, 0xff & ManagerParse.parse("/* abc */switch @@pool offer2$0-2"));
        Assert.assertEquals(ManagerParse.SHOW, 0xff & ManagerParse.parse(" /** 111**/Show @@help"));
        Assert.assertEquals(ManagerParse.SELECT, 0xff & ManagerParse.parse(" /***/ select * from t "));
    }

    @Test
    public void testShowWhitComment() {
        Assert.assertEquals(ManagerParseShow.VARIABLES,
            ManagerParseShow.parse(" /** 111**/show variables", " /** 111**/show".length()));
        Assert.assertEquals(ManagerParseShow.VARIABLES,
            ManagerParseShow.parse(" /**111**/ SHOW VARIABLES", " /** 111**/show".length()));
        Assert.assertEquals(ManagerParseShow.VARIABLES,
            ManagerParseShow.parse(" /**111**/ SHOW variables", " /** 111**/show".length()));
    }

    @Test
    public void testStop() {
        Assert.assertEquals(ManagerParse.STOP, 0xff & ManagerParse.parse("stop @@"));
        Assert.assertEquals(ManagerParse.STOP, 0xff & ManagerParse.parse(" STOP "));
    }

    @Test
    public void testStopHeartBeat() {
        Assert.assertEquals(ManagerParseStop.HEARTBEAT, ManagerParseStop.parse("stop @@heartbeat ds:1000", 4));
        Assert.assertEquals(ManagerParseStop.HEARTBEAT, ManagerParseStop.parse(" STOP  @@HEARTBEAT ds:1000", 5));
        Assert.assertEquals(ManagerParseStop.HEARTBEAT, ManagerParseStop.parse(" STOP  @@heartbeat ds:1000", 5));
    }

    @Test
    public void testReload() {
        Assert.assertEquals(ManagerParse.RELOAD, 0xff & ManagerParse.parse("reload @@"));
        Assert.assertEquals(ManagerParse.RELOAD, 0xff & ManagerParse.parse(" RELOAD "));
    }

    @Test
    public void testReloadConfig() {
        Assert.assertEquals(ManagerParseReload.CONFIG, ManagerParseReload.parse("reload @@config", 7));
        Assert.assertEquals(ManagerParseReload.CONFIG, ManagerParseReload.parse(" RELOAD  @@CONFIG ", 7));
        Assert.assertEquals(ManagerParseReload.CONFIG, ManagerParseReload.parse(" RELOAD  @@config ", 7));
    }

    @Test
    public void testReloadRoute() {
        Assert.assertEquals(ManagerParseReload.ROUTE, ManagerParseReload.parse("reload @@route", 7));
        Assert.assertEquals(ManagerParseReload.ROUTE, ManagerParseReload.parse(" RELOAD  @@ROUTE ", 7));
        Assert.assertEquals(ManagerParseReload.ROUTE, ManagerParseReload.parse(" RELOAD  @@route ", 7));
    }

    @Test
    public void testReloadUser() {
        Assert.assertEquals(ManagerParseReload.USER, ManagerParseReload.parse("reload @@user", 7));
        Assert.assertEquals(ManagerParseReload.USER, ManagerParseReload.parse(" RELOAD  @@USER ", 7));
        Assert.assertEquals(ManagerParseReload.USER, ManagerParseReload.parse(" RELOAD  @@user ", 7));
    }

    @Test
    public void testRollback() {
        Assert.assertEquals(ManagerParse.ROLLBACK, 0xff & ManagerParse.parse("rollback @@"));
        Assert.assertEquals(ManagerParse.ROLLBACK, 0xff & ManagerParse.parse(" ROLLBACK "));
    }

    @Test
    public void testOnOff() {
        Assert.assertEquals(ManagerParse.ONLINE, ManagerParse.parse("online "));
        Assert.assertEquals(ManagerParse.ONLINE, ManagerParse.parse(" Online"));
        Assert.assertEquals(ManagerParse.OTHER, ManagerParse.parse(" Online2"));
        Assert.assertEquals(ManagerParse.OTHER, ManagerParse.parse("Online2 "));
        Assert.assertEquals(ManagerParse.OFFLINE, ManagerParse.parse(" Offline"));
        Assert.assertEquals(ManagerParse.OFFLINE, ManagerParse.parse("offLine\t"));
        Assert.assertEquals(ManagerParse.OTHER, ManagerParse.parse("onLin"));
        Assert.assertEquals(ManagerParse.OTHER, ManagerParse.parse(" onlin"));
    }

    @Test
    public void testRollbackConfig() {
        Assert.assertEquals(ManagerParseRollback.CONFIG, ManagerParseRollback.parse("rollback @@config", 8));
        Assert.assertEquals(ManagerParseRollback.CONFIG, ManagerParseRollback.parse(" ROLLBACK  @@CONFIG ", 9));
        Assert.assertEquals(ManagerParseRollback.CONFIG, ManagerParseRollback.parse(" ROLLBACK  @@config ", 9));
    }

    @Test
    public void testRollbackUser() {
        Assert.assertEquals(ManagerParseRollback.USER, ManagerParseRollback.parse("rollback @@user", 9));
        Assert.assertEquals(ManagerParseRollback.USER, ManagerParseRollback.parse(" ROLLBACK  @@USER ", 9));
        Assert.assertEquals(ManagerParseRollback.USER, ManagerParseRollback.parse(" ROLLBACK  @@user ", 9));
    }

    @Test
    public void testRollbackRoute() {
        Assert.assertEquals(ManagerParseRollback.ROUTE, ManagerParseRollback.parse("rollback @@route", 9));
        Assert.assertEquals(ManagerParseRollback.ROUTE, ManagerParseRollback.parse(" ROLLBACK  @@ROUTE ", 9));
        Assert.assertEquals(ManagerParseRollback.ROUTE, ManagerParseRollback.parse(" ROLLBACK  @@route ", 9));
    }

    @Test
    public void testGetWhere() {
        Assert.assertEquals("123", ManagerParseShow.getWhereParameter("where id = 123"));
        Assert.assertEquals("datanode", ManagerParseShow.getWhereParameter("where datanode =    datanode"));
        Assert.assertEquals("schema", ManagerParseShow.getWhereParameter("where schema =schema   "));
    }

    // @Test
    public void testShowSlowDataNode() {
        Assert.assertEquals(ManagerParseShow.SLOW_DATANODE,
            0xff & ManagerParseShow.parse("show @@slow where datanode= a", 5));
        Assert.assertEquals(ManagerParseShow.SLOW_DATANODE,
            0xff & ManagerParseShow.parse("SHOW @@SLOW WHERE DATANODE= A", 5));
        Assert.assertEquals(ManagerParseShow.SLOW_DATANODE,
            0xff & ManagerParseShow.parse(" show @@SLOW where DATANODE= b ", 5));
    }

    @Test
    public void testclearSlowSchema() {
        Assert.assertEquals(ManagerParseClear.SLOW_SCHEMA,
            0xff & ManagerParseClear.parse("clear @@slow where schema=s", 5));
        Assert.assertEquals(ManagerParseClear.SLOW_SCHEMA,
            0xff & ManagerParseClear.parse("CLEAR @@SLOW WHERE SCHEMA= S", 5));
        Assert.assertEquals(ManagerParseClear.SLOW_SCHEMA,
            0xff & ManagerParseClear.parse("CLEAR @@slow where SCHEMA= s", 5));
    }

    @Test
    public void testclearSlowDataNode() {
        Assert.assertEquals(ManagerParseClear.SLOW_DATANODE,
            0xff & ManagerParseClear.parse("clear @@slow where datanode=d", 5));
        Assert.assertEquals(ManagerParseClear.SLOW_DATANODE,
            0xff & ManagerParseClear.parse("CLEAR @@SLOW WHERE DATANODE= D", 5));
        Assert.assertEquals(ManagerParseClear.SLOW_DATANODE,
            0xff & ManagerParseClear.parse("clear @@SLOW where  DATANODE= d", 5));
    }

    @Test
    public void testShowDirectMemCommand() {
        Assert.assertEquals(ManagerParseShow.COMMAND, ManagerParseShow.parse("show @@direct_mem", 5));
        Assert.assertEquals(ManagerParseShow.COMMAND, ManagerParseShow.parse("SHOW @@DIRECT_MEM", 5));
        Assert.assertEquals(ManagerParseShow.COMMAND, ManagerParseShow.parse("show @@direct_MEM", 5));
        Assert.assertNotEquals(ManagerParseShow.COMMAND, ManagerParseShow.parse("show @@directMEM", 5));
    }
}
