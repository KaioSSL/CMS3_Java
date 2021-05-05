/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import SqlConnection.ConnectionFactory;
import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author SynMcall
 */
public class Cargos {
    private int id;
    private String dscCargo;    

    public String getDscCargo() {
        return dscCargo;
    }

    public void setDscCargo(String dscCargo) {
        this.dscCargo = dscCargo;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
    
    public static List<Cargos> buscarCargos(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Cargos> cargos = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Cargos");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Cargos cargo = new Cargos();
                cargo.setId(rs.getInt("idCargo"));
                cargo.setDscCargo(rs.getString("dsc"));
                cargos.add(cargo);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return cargos;
    }
    
    public static Cargos buscarCargo(int id){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Cargos cargo = new Cargos();
        cargo.setId(id);
        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Cargos WHERE idCargo = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            while( rs.next()){
                cargo.setDscCargo(rs.getString("dsc"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Alterar Estado Civil Para Combo box "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
        return cargo;
    }

    public String toString(){
        return getDscCargo();
    }
}
