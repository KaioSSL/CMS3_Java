/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import SqlConnection.ConnectionFactory;
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
public class ModoUsuario {
    private int id;
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
    
    public static List<ModoUsuario> buscarModoUsuario(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<ModoUsuario> modos = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM ModoUsuario");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                ModoUsuario modo = new ModoUsuario();
                modo.setId(rs.getInt("id"));
                modo.setDsc(rs.getString("dsc"));
                modos.add(modo);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return modos;
    }
    public static ModoUsuario buscarModoUsuario(int id){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        ModoUsuario modo = new ModoUsuario();
        modo.setId(id);
             
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM ModoUsuario WHERE id = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                modo.setId(rs.getInt("id"));
                modo.setDsc(rs.getString("dsc"));

            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return modo;
    }
    public String toString(){
        return this.getDsc();
    }
    
}
