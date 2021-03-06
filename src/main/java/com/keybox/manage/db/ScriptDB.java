/**
 * Copyright 2013 Sean Kavanagh - sean.p.kavanagh6@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.keybox.manage.db;


import com.keybox.manage.model.Script;
import com.keybox.manage.model.SortedSet;
import com.keybox.manage.util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * DAO to manage scripts
 */
public class ScriptDB {


    public static final String SORT_BY_DISPLAY_NM="display_nm";


    /**
     * returns scripts based on sort order defined
     * @param sortedSet object that defines sort order
     * @return sorted script list
     */
    public static SortedSet getScriptSet(SortedSet sortedSet) {

        ArrayList<Script> scriptList = new ArrayList<Script>();


        String orderBy = "";
        if (sortedSet.getOrderByField() != null && !sortedSet.getOrderByField().trim().equals("")) {
            orderBy = "order by " + sortedSet.getOrderByField() + " " + sortedSet.getOrderByDirection();
        }
        String sql = "select * from scripts " + orderBy;

        Connection con = null;
        try {
            con = DBUtils.getConn();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Script script = new Script();
                script.setId(rs.getLong("id"));
                script.setDisplayNm(rs.getString("display_nm"));
                script.setScript(rs.getString("script"));

                scriptList.add(script);

            }
            DBUtils.closeRs(rs);
            DBUtils.closeStmt(stmt);

        } catch (Exception e) {
            e.printStackTrace();
        }
        DBUtils.closeConn(con);

        sortedSet.setItemList(scriptList);
        return sortedSet;
    }


    /**
     * returns script base on id
     * @param scriptId script id
     * @return script object
     */
    public static Script getScript(Long scriptId) {

        Script script = null;
        Connection con = null;
        try {
            con = DBUtils.getConn();
            script = getScript(con, scriptId);


        } catch (Exception e) {
            e.printStackTrace();
        }
        DBUtils.closeConn(con);

        return script;
    }

    /**
     * returns script base on id
     * @param con DB connection
     * @param scriptId script id
     * @return script object
     */
    public static Script getScript(Connection con, Long scriptId) {

        Script script = null;
        try {
            PreparedStatement stmt = con.prepareStatement("select * from  scripts where id=?");
            stmt.setLong(1, scriptId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                script = new Script();
                script.setId(rs.getLong("id"));
                script.setDisplayNm(rs.getString("display_nm"));
                script.setScript(rs.getString("script"));
            }
            DBUtils.closeRs(rs);
            DBUtils.closeStmt(stmt);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return script;
    }

    /**
     * inserts new script
     * @param script script object
     */
    public static void insertScript(Script script) {


        Connection con = null;
        try {
            con = DBUtils.getConn();
            PreparedStatement stmt = con.prepareStatement("insert into scripts (display_nm, script) values (?,?)");
            stmt.setString(1, script.getDisplayNm());
            stmt.setString(2, script.getScript());
            stmt.execute();
            DBUtils.closeStmt(stmt);

        } catch (Exception e) {
            e.printStackTrace();
        }
        DBUtils.closeConn(con);

    }

    /**
     * updates existing script
     * @param script script object
     */
    public static void updateScript(Script script) {


        Connection con = null;
        try {
            con = DBUtils.getConn();
            PreparedStatement stmt = con.prepareStatement("update scripts set display_nm=?, script=? where id=?");
            stmt.setString(1, script.getDisplayNm());
            stmt.setString(2, script.getScript());
            stmt.setLong(3, script.getId());
            stmt.execute();
            DBUtils.closeStmt(stmt);

        } catch (Exception e) {
            e.printStackTrace();
        }
        DBUtils.closeConn(con);

    }

    /**
     * deletes script
     * @param scriptId script id
     */
    public static void deleteScript(Long scriptId) {


        Connection con = null;
        try {
            con = DBUtils.getConn();
            PreparedStatement stmt = con.prepareStatement("delete from scripts where id=?");
            stmt.setLong(1, scriptId);
            stmt.execute();
            DBUtils.closeStmt(stmt);

        } catch (Exception e) {
            e.printStackTrace();
        }
        DBUtils.closeConn(con);

    }


}
