package rs.stefanlezaic.zeleznice.srbije.server.database;

import rs.stefanlezaic.zeleznice.srbije.lib.domen.GeneralEntity;
import rs.stefanlezaic.zeleznice.srbije.lib.exception.DeleteEntityException;
import rs.stefanlezaic.zeleznice.srbije.lib.exception.EntityNotFoundException;
import rs.stefanlezaic.zeleznice.srbije.lib.exception.InsertEntityException;
import rs.stefanlezaic.zeleznice.srbije.lib.exception.UpdateEntityException;
import rs.stefanlezaic.zeleznice.srbije.lib.exception.UpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import rs.stefanlezaic.zeleznice.srbije.server.storage.database.connection.DatabaseConnection;

public class DatabaseBroker implements IDatabaseBroker {

    @Override
    public List<GeneralEntity> getAllRecord(GeneralEntity entity) throws SQLException {
        List<GeneralEntity> objects = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ").append(entity.getClassName()).append(" order by ").append(entity.getSort());
        String query = sb.toString();
        System.out.println(query);
        PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            objects.add(entity.getNewRecord(rs));
        }
        return objects;
    }

    @Override
    public GeneralEntity findRecord(GeneralEntity entity) throws SQLException, EntityNotFoundException {
        ResultSet rs;
        Statement st;
        String query = "SELECT * FROM " + entity.getClassName() + " WHERE " + entity.getWhereCondition();
        System.out.println(query);
        boolean signal;
        st = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        rs = st.executeQuery(query);
        signal = rs.next();
        if (signal == true) {
            return entity.getNewRecord(rs);
        }
        throw new EntityNotFoundException("Entity does not exist");
    }

    @Override
    public GeneralEntity findRecordNoPrimaryKey(GeneralEntity entity) throws SQLException, EntityNotFoundException {
        ResultSet rs;
        Statement st;
        String query = "SELECT * FROM " + entity.getClassName() + " WHERE " + entity.getWhereNoPrimaryKey();
        System.out.println(query);
        boolean signal;
        st = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        rs = st.executeQuery(query);
        signal = rs.next();
        if (signal == true) {
            return entity.getNewRecord(rs);
        }
        throw new EntityNotFoundException("Entity does not exist");
    }

    @Override
    public List<GeneralEntity> findRecords(GeneralEntity entity, GeneralEntity parent) throws SQLException {
        List<GeneralEntity> objects = new LinkedList<>();
        String query = "SELECT * FROM " + entity.getClassName() + " WHERE " + parent.getWhereCondition() + " order by " + entity.getSort();
        System.out.println(query);
        PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            objects.add(entity.getNewRecord(rs));
        }
        return objects;
    }

    @Override
    public void insertRecord(GeneralEntity entity) throws InsertEntityException, SQLException {
        String upit = "INSERT INTO " + entity.getClassName() + " VALUES (" + entity.getAtrValue() + ")";
        try {
            executeUpdate(upit);
        } catch (UpdateException ex) {
            throw new InsertEntityException("Insert query not executed");
        }
    }

    @Override
    public void deleteRecord(GeneralEntity entity) throws SQLException, DeleteEntityException {
        String upit = "DELETE FROM " + entity.getClassName() + " WHERE " + entity.getWhereCondition();
        try {
            executeUpdate(upit);
        } catch (UpdateException ex) {
            throw new DeleteEntityException("Delete query not executed");
        }
    }

    @Override
    public void updateRecord(GeneralEntity entity, GeneralEntity entityId) throws SQLException, UpdateEntityException {
        String upit = "UPDATE " + entity.getClassName() + " SET " + entity.setAtrValue() + " WHERE " + entityId.getWhereCondition();
        try {
            executeUpdate(upit);
        } catch (UpdateException ex) {
            throw new UpdateEntityException("Update query not executed");
        }
    }

    @Override
    public void updateRecord(GeneralEntity entity) throws SQLException, UpdateEntityException {
        String upit = "UPDATE " + entity.getClassName() + " SET " + entity.setAtrValue() + " WHERE " + entity.getWhereCondition();
        try {
            executeUpdate(upit);
        } catch (UpdateException ex) {
            throw new UpdateEntityException("Update query not executed");
        }
    }

    private void executeUpdate(String query) throws SQLException, UpdateException {
        System.out.println(query);
        Statement st;
        st = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        int rowcount = st.executeUpdate(query);
        if (rowcount <= 0) {
            throw new UpdateException("Update query not executed");
        }

    }

    @Override
    public List<GeneralEntity> findRecordsWithObject(GeneralEntity entity, Object o) throws SQLException {
        List<GeneralEntity> objects = new LinkedList<>();
        String query = "SELECT * FROM " + entity.getClassName() + " WHERE " + entity.getExtraCondition(o) + " order by " + entity.getSort();
        System.out.println(query);
        PreparedStatement statement = DatabaseConnection.getInstance().getConnection().prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            objects.add(entity.getNewRecord(rs));
        }
        return objects;
    }

}
