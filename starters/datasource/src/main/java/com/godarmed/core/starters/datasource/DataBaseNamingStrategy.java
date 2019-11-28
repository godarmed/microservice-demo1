package com.godarmed.core.starters.datasource;


import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class DataBaseNamingStrategy implements PhysicalNamingStrategy {
    private DataBaseProperties dataBaseProperties;
    private PhysicalNamingStrategy proxy;

    public DataBaseNamingStrategy(DataBaseProperties config, PhysicalNamingStrategy proxy) {
        this.proxy = proxy;
        this.dataBaseProperties = config;
    }

    protected String addUnderscores(String name) {
        if (name == null) {
            return null;
        } else {
            StringBuffer stringBuffer = new StringBuffer(name.replace('.', '_'));

            for(int i = 1; i < stringBuffer.length() - 1; ++i) {
                if (Character.isLowerCase(stringBuffer.charAt(i - 1)) && Character.isUpperCase(stringBuffer.charAt(i)) && Character.isLowerCase(stringBuffer.charAt(i + 1))) {
                    stringBuffer.insert(i++, '_');
                }
            }

            return stringBuffer.toString().toLowerCase();
        }
    }

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.proxy.toPhysicalCatalogName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.proxy.toPhysicalSchemaName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        if (this.dataBaseProperties.getIsopen()) {
            identifier = Identifier.toIdentifier(this.dataBaseProperties.getPrefix() + this.addUnderscores(identifier.getText()));
        }

        return this.proxy.toPhysicalTableName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.proxy.toPhysicalSequenceName(identifier, jdbcEnvironment);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return this.proxy.toPhysicalColumnName(identifier, jdbcEnvironment);
    }
}
