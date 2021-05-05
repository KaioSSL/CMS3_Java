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
public class EstadoCivil {
    private int id ;
    private String dsc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }
    public static List<EstadoCivil> buscarEstados(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<EstadoCivil> estadosCivis = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM EstadoCivil");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                EstadoCivil estadoCivil = new EstadoCivil();
                estadoCivil.setId(rs.getInt("id"));
                estadoCivil.setDsc(rs.getString("dsc"));
                estadosCivis.add(estadoCivil);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return estadosCivis;
    }
    
    public static EstadoCivil buscarEstado(int id){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EstadoCivil estado = new EstadoCivil();
        estado.setId(id);
        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM EstadoCivil WHERE id = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            while( rs.next()){
                estado.setDsc(rs.getString("dsc"));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Estado por id"+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
        return estado;
    }

    public static EstadoCivil buscarEstado(EstadoCivil estado){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM EstadoCivil WHERE id = ?");
            stmt.setInt(1,estado.getId());
            rs = stmt.executeQuery();
            
            
            while( rs.next()){
                estado.setDsc(rs.getString("dsc"));
            }      

            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Popular"+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
        return estado;
    }
    
    
    public String toString(){
        return getDsc();
    
    }
    
}
