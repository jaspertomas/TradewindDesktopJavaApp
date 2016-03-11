package utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.Entity;
import org.json.JSONArray;

import org.json.JSONException;
import org.json.JSONObject;

public class ModelHelper {
//	public String TABLENAME=null;
//	public Class MODELCLASS=null;
//    protected abstract String getTable();
//    protected abstract Class getModelClass();

    public String tablename;
    public String[] fields;
    public String[] fieldtypes;

    //data types
    public static final int INTEGER = 1;
    public static final int STRING = 2;
    public static final int BOOLEAN = 3;
    public static final int DOUBLE = 4;
    public static final int DATE = 5;
    public static final int DATETIME = 6;
    public static final int DECIMAL = 7;
    public static final int LONG = 8;
    public static final int FLOAT = 9;
    public static final int SHORT = 10;
    public Integer[] datatypes;

    //field validations
    public static final Integer REQUIRED = 1;
    public static final Integer UNIQUE = 2;
    public Integer[] fieldvalidations;

    //labels
    public String[] fieldlabels;

    public ModelHelper(String tablename, String[] fields, String[] fieldtypes, Integer[] datatypes, Integer[] fieldvalidations, String[] fieldlabels) {
        this.tablename = tablename;
        this.fields = fields;
        this.fieldtypes = fieldtypes;
        this.datatypes = datatypes;
        this.fieldvalidations = fieldvalidations;
        this.fieldlabels = fieldlabels;
    }

    //database connection
    private static Connection conn;

    private static void initIfNecessary() {
        if (conn == null) {
            conn = SqliteDbHelper.getInstance().getConnection();
        }
    }

    public JSONObject toJSON(ResultSet rs) {
        /*
         int id = rs.getInt("id");
         String  name = rs.getString("name");
         int age  = rs.getInt("age");
         String  address = rs.getString("address");
         float salary = rs.getFloat("salary");

         */
        try {
            JSONObject values = new JSONObject();
            for (int i = 0; i < fields.length; i++) {
                switch (datatypes[i]) {
                    case INTEGER:
                        values.put(fields[i], rs.getInt(rs.findColumn(fields[i])));//Integer
                        break;
                    case STRING:
                        values.put(fields[i], rs.getString(rs.findColumn(fields[i])));//Integer
                        break;
                    case BOOLEAN:
                        values.put(fields[i], rs.getShort(rs.findColumn(fields[i])) == 1 ? true : false);//Integer
                        break;
                    case DOUBLE:
                        values.put(fields[i], rs.getDouble(rs.findColumn(fields[i])));//Integer
                        break;
                    case DATE:
                        values.put(fields[i], DateHelper.toDate(rs.getString(rs.findColumn(fields[i]))));//Date
                        break;
                    case DATETIME:
                        values.put(fields[i], DateTimeHelper.toDate(rs.getString(rs.findColumn(fields[i]))));//DateTime
                        break;
                    case DECIMAL:
                        values.put(fields[i], BigDecimal.valueOf(rs.getDouble(rs.findColumn(fields[i]))));//Decimal
                        break;
                    case LONG:
                        values.put(fields[i], rs.getLong(rs.findColumn(fields[i])));//long
                        break;
                    case FLOAT:
                        values.put(fields[i], rs.getFloat(rs.findColumn(fields[i])));//float
                        break;
                    case SHORT:
                        values.put(fields[i], rs.getShort(rs.findColumn(fields[i])));//float
                        break;
                }
            }
            return values;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<JSONObject> select(String criteria) {
        initIfNecessary();
        JSONObject item;
        ArrayList<JSONObject> items = new ArrayList<JSONObject>();

        try {
            conn.setAutoCommit(false);

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tablename + " " + criteria);
            while (rs.next()) {
                item = toJSON(rs);
                if (item != null) {
                    items.add(item);
                }
                /*
                 int id = rs.getInt("id");
                 String  name = rs.getString("name");
                 int age  = rs.getInt("age");
                 String  address = rs.getString("address");
                 float salary = rs.getFloat("salary");

                 */
            }
            rs.close();
            stmt.close();

            return items;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return items;
        }

    }

    public JSONObject selectOne(String criteria) {
        ArrayList<JSONObject> items = select(criteria + " limit 1");
        for (JSONObject item : items) {
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    public Integer count(String criteria) {
        try {
            initIfNecessary();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + tablename + " " + criteria);
            rs.next();
            Integer result = rs.getInt(0);
            rs.close();
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer getLastInsertId() {
        try {
            initIfNecessary();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid() FROM " + tablename + " ");
            rs.next();
            Integer result = rs.getInt(0);
            rs.close();
            return result;
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public JSONObject getById(Integer id) {
        return selectOne(" where id = '" + id.toString() + "'");
    }

    public void save(Entity entity) {
        if (entity.getId() == null || entity.getId() == 0 || getById(entity.getId()) == null) {
            entity.setId(insert(entity));
        } else {
            update(entity);
        }
    }

    /*
     public void update(String id, ContentValues values) {
     initIfNecessary();
     String where = "id=?";
     String[] whereArgs = new String[] {String.valueOf(id)};
     conn.update(tablename, values, where, whereArgs);    	
     }*/
    public int insert(Entity entity) {
        return insert(entity.getValues());
    }

    public int insert(JSONObject json) {
        try {
            initIfNecessary();
            //create query string "insert into tablename values(?,?,?,?,?);"
            String qs = "";
            for (int i = 0; i < fields.length; i++) {
                if (i != 0) {
                    qs += ",";
                }
                qs += "?";
            }
            String sql = "insert into " + tablename + " values(" + qs + ");";

            PreparedStatement prep = conn.prepareStatement(sql);
            for (int i = 0; i < fields.length; i++) {
                try {
                    addValueToPreparedStatement(prep, json, i);
                } catch (JSONException ex) {
                    Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            prep.addBatch();
            int[] updateCounts = prep.executeBatch();

            conn.commit();

            return updateCounts[0];
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int[] batchInsert(JSONArray array) {
        initIfNecessary();
        String qs = "";
        for (int i = 0; i < fields.length; i++) {
            if (i != 0) {
                qs += ",";
            }
            qs += "?";
        }
        String sql = "insert into " + tablename + " values(" + qs + ");";

        try {
            PreparedStatement prep = conn.prepareStatement(sql);
            JSONObject json;
            for (int j = 0; j < array.length(); j++) {
                json = array.getJSONObject(j);
                for (int i = 0; i < fields.length; i++) {
                    addValueToPreparedStatement(prep, json, i);
                }
            }

            prep.addBatch();
            int[] updateCounts = prep.executeBatch();

            conn.commit();

            return updateCounts;
        } catch (JSONException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public int update(Entity entity) {
        return update(entity.getValues());
    }

    public int update(JSONObject json) {
        try {
            initIfNecessary();
            //create query string "update tablename set field=?, field=?, field=? where id=?;"
            String qs = "";
            for (int i = 1; i < fields.length; i++) {
                if (i != 1) {
                    qs += ",";
                }
                qs += fields[i] + "=?";
            }
            String sql = "update " + tablename + " set " + qs + " where id = ?;";

            PreparedStatement prep = conn.prepareStatement(sql);
            try {
                for (int i = 1; i < fields.length; i++) {
                    addValueToPreparedStatement(prep, json, i);
                }
                addValueToPreparedStatement(prep, json, 0);//id
            } catch (JSONException ex) {
                Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
            }

            prep.addBatch();
            int[] updateCounts = prep.executeBatch();

            conn.commit();

            return updateCounts[0];
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    public int[] batchUpdate(JSONArray array) throws SQLException {
        initIfNecessary();
        //create query string "update tablename set field=?, field=?, field=? where id=?;"
        String qs = "";
        for (int i = 1; i < fields.length; i++) {
            if (i != 0) {
                qs += ",";
            }
            qs += fields[i] + "=?";
        }
        String sql = "update " + tablename + " set " + qs + " where id = ?;";

        PreparedStatement prep = conn.prepareStatement(sql);

        try {
            JSONObject json;
            //for each JSONObject in JSONArray
            for (int j = 0; j < array.length(); j++) {
                json = array.getJSONObject(j);
                //for each field; omit id, put it at the end instead
                for (int i = 1; i < fields.length; i++) {
                    addValueToPreparedStatement(prep, json, i);
                }
                addValueToPreparedStatement(prep, json, 0);//id
            }
        } catch (JSONException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        prep.addBatch();
        int[] updateCounts = prep.executeBatch();

        conn.commit();

        return updateCounts;
    }

    private void addValueToPreparedStatement(PreparedStatement prep, JSONObject json, Integer i) throws SQLException, JSONException {
        if (!json.has(fields[i])) {
            switch (datatypes[i]) {
                case INTEGER:
                    prep.setNull(i + 1, java.sql.Types.INTEGER);
                    break;
                case STRING:
                    prep.setNull(i + 1, java.sql.Types.VARCHAR);
                    break;
                case BOOLEAN:
                    prep.setNull(i + 1, java.sql.Types.BOOLEAN);
                    break;
                case DOUBLE:
                    prep.setNull(i + 1, java.sql.Types.DOUBLE);
                    break;
                case DATE:
                    prep.setNull(i + 1, java.sql.Types.VARCHAR);
                    break;
                case DATETIME:
                    prep.setNull(i + 1, java.sql.Types.VARCHAR);
                    break;
                case DECIMAL:
                    prep.setNull(i + 1, java.sql.Types.DECIMAL);
                    break;
                case LONG:
                    prep.setNull(i + 1, java.sql.Types.BIGINT);
                    break;
                case FLOAT:
                    prep.setNull(i + 1, java.sql.Types.FLOAT);
                    break;
                case SHORT:
                    prep.setNull(i + 1, java.sql.Types.SMALLINT);
                    break;
            }
        } else {
            switch (datatypes[i]) {
                case INTEGER:
                    prep.setInt(i + 1, json.getInt(fields[i]));
                    break;
                case STRING:
                    prep.setString(i + 1, json.getString(fields[i]));
                    break;
                case BOOLEAN:
                    prep.setBoolean(i + 1, json.getBoolean(fields[i]));
                    break;
                case DOUBLE:
                    prep.setDouble(i + 1, json.getDouble(fields[i]));
                    break;
                case DATE: {
                    java.util.Date d1 = DateHelper.toDate(json.getString(fields[i]));
                    prep.setDate(i + 1, new java.sql.Date(d1.getTime()));
                }
                break;
                case DATETIME: {
                    java.util.Date d1 = DateTimeHelper.toDate(json.getString(fields[i]));
                    prep.setDate(i + 1, new java.sql.Date(d1.getTime()));
                }
                break;
                case DECIMAL:
                    prep.setBigDecimal(i + 1, BigDecimal.valueOf(json.getDouble(fields[i])));
                    break;
                case LONG:
                    prep.setLong(i + 1, json.getLong(fields[i]));
                    break;
                case FLOAT:
                    prep.setFloat(i + 1, Double.valueOf(json.getDouble(fields[i])).floatValue());
                    break;
                case SHORT:
                    prep.setShort(i + 1, Integer.valueOf(json.getInt(fields[i])).shortValue());
                    break;
            }
        }
    }
    /*
     public E find(String id)
     throws ReflectiveOperationException {
     SQLiteDatabase conn = this.getDb();
     Cursor cursor = conn.query(this.getTable(), null, "id = ?", new String[] { id }, null, null, null, "1");

     Class[] constructorSignature = new Class[] { Cursor.class };
     Constructor constructor = this.getModelClass().getConstructor(constructorSignature);

     while (cursor.moveToNext()) {
     return (E) constructor.newInstance(cursor);
     }
     return null;
     }
    
     public E first() 
     throws ReflectiveOperationException {
     SQLiteDatabase conn = this.getDb();
     Cursor cursor = conn.query(this.getTable(), null, null, null, null, null, null, "1");

     Class[] constructorSignature = new Class[] { Cursor.class };
     Constructor constructor = this.getModelClass().getConstructor(constructorSignature);

     while (cursor.moveToNext()) {
     return (E) constructor.newInstance(cursor);
     }
     return null;
     }
    
     public HashMap<String, E> where(String where) 
     throws ReflectiveOperationException { // "id = 1"
     // needed? we're not writing to conn
     conn = this.getDb();
        
     Cursor cursor = conn.query(this.getTable(), null, where, null, null, null, null, null);

     Class[] constructorSignature = new Class[] { Cursor.class };
     Constructor constructor = this.getModelClass().getConstructor(constructorSignature);

     items = new HashMap<String, E>();
     while (cursor.moveToNext()) {
     items.put(cursor.getString(0), (E) constructor.newInstance(cursor) );
     }
     return items;
     }
     */

    public void delete(Integer id) {
        initIfNecessary();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from " + tablename + " where id=" + id.toString());
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void delete(Entity item) {
        delete(item.getId());
    }

    public void delete(String criteria) {
        //if first word in criteria is "where", remove it
        criteria = criteria.trim();
        if (criteria.isEmpty()) {
            criteria = "1";
        } else if (criteria.toLowerCase().indexOf("where") == 0) {
            criteria = criteria.replace("where", "");
        }
        initIfNecessary();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from " + tablename + " where " + criteria);
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteAll() {
        initIfNecessary();
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("delete from " + tablename);
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /*
     private static ContentValues getContentValues(JSONObject attributes) throws JSONException {
     ContentValues values = new ContentValues();
     Iterator keys = attributes.keys();
     String key;

     while (keys.hasNext()) {
     key = keys.next().toString();
     values.put(key, attributes.getString(key));
     }

     return values;
     }
     private static ContentValues getContentValues(Entity entity) throws JSONException {
     return getContentValues(entity.getValues());
     }
     */

    private String getCreateTableQuery() {
        return "CREATE TABLE IF NOT EXISTS " + tablename + " (" + implodeFieldsWithTypes() + " );";
    }

    private String implodeFieldsWithTypes() {
        String output = "";
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].contentEquals(fields[0]))//fields[0] being the primary key
            {
                output += fields[i] + " INTEGER PRIMARY KEY";
            } else {
                output += "," + fields[i] + " " + fieldtypes[i];
            }
        }
        return output;
    }

    private String getDeleteTableQuery() {
        return "DROP TABLE IF EXISTS " + tablename;
    }

    public void createTable() {
        try {
            initIfNecessary();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(getCreateTableQuery());
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteTable() {
        try {
            initIfNecessary();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(getDeleteTableQuery());
        } catch (SQLException ex) {
            Logger.getLogger(ModelHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer jsonGetInteger(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return values.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutInteger(JSONObject values, String key, Integer value) {
        try {
            values.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Boolean jsonGetBoolean(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return values.getBoolean(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutBoolean(JSONObject values, String key, Boolean value) {
        try {
            values.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Double jsonGetDouble(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return values.getDouble(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutDouble(JSONObject values, String key, Double value) {
        try {
            values.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Long jsonGetLong(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return values.getLong(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutLong(JSONObject values, String key, Long value) {
        try {
            values.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String jsonGetString(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return values.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutString(JSONObject values, String key, String value) {
        try {
            values.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Date jsonGetDate(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return DateHelper.toDate(values.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutDate(JSONObject values, String key, Date value) {
        try {
            values.put(key, DateTimeHelper.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Date jsonGetDateTime(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return DateTimeHelper.toDate(values.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutDateTime(JSONObject values, String key, Date value) {
        try {
            values.put(key, DateTimeHelper.toString(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //all the following functions need testing
    public BigDecimal jsonGetDecimal(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return BigDecimal.valueOf(values.getDouble(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutDecimal(JSONObject values, String key, BigDecimal value) {
        try {
            values.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Short jsonGetShort(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return Short.valueOf(values.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutShort(JSONObject values, String key, Short value) {
        try {
            values.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Float jsonGetFloat(JSONObject values, String key) {
        try {
            if (values.isNull(key)) {
                return null;
            } else {
                return Float.valueOf(values.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void jsonPutFloat(JSONObject values, String key, Float value) {
        try {
            values.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
