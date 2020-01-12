package com.godarmed.core.framework.report.constant;

public interface GlobalConstants {

    /**
     * 数据库类型
     */
    interface DATABASE_TYPE {
        String MYSQL = "MYSQL"; //MYSQL
        String ORACLE = "ORACLE";   //ORACLE
        String SQL_SERVER = "SQLSERVER";    //SQL SERVER
    }

    /**
     * MYSQL数据类型
     */
    interface MYSQL_DATA_TYPE {
        String VARCHAR = "varchar";
        String CHAR = "char";
        String BLOB = "blob";
        String TEXT = "text";
        String INT = "int";
        String INTEGER = "integer";
        String TINYINT = "tinyint";
        String SMALLINT = "smallint";
        String MEDIUMINT = "mediumint";
        String BIT = "bit";
        String BIGINT = "bigint";
        String FLOAT = "float";
        String DOUBLE = "double";
        String DECIMAL = "decimal";
        String BOOLEAN = "boolean";
        String DATE = "date";
        String TIME = "time";
        String DATETIME = "datetime";
        String TIMESTAMP = "timestamp";
        String YEAR = "year";
    }

    /**
     * MYSQL数据类型
     */
    interface MYSQL_TO_JAVA_TYPE {
        Class VARCHAR = java.lang.String.class;
        Class CHAR = java.lang.String.class;
        Class BLOB = java.lang.Byte[].class;
        Class TEXT = java.lang.String.class;
        Class INTEGER = java.lang.Long.class;
        Class TINYINT = java.lang.Integer.class;
        Class SMALLINT = java.lang.Integer.class;
        Class MEDIUMINT = java.lang.Integer.class;
        Class BIT = java.lang.Boolean.class;
        Class BIGINT = java.math.BigInteger.class;
        Class FLOAT = java.lang.Float.class;
        Class DOUBLE = java.lang.Double.class;
        Class DECIMAL = java.math.BigDecimal.class;
        Class BOOLEAN = java.lang.Integer.class;
        Class DATE = java.sql.Date.class;
        Class TIME = java.sql.Time.class;
        Class DATETIME = java.sql.Timestamp.class;
        Class TIMESTAMP = java.sql.Timestamp.class;
        Class YEAR = java.sql.Date.class;
    }

    public static void main(String[] args) {
        Object obj = "aaa";
        String test = (String) MYSQL_TO_JAVA_TYPE.VARCHAR.cast(obj);
        System.out.println(obj.getClass().getTypeName());
        System.out.println(test.getClass().getTypeName());
    }


}
