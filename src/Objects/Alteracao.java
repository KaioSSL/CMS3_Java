/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import SqlConnection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SynMcall
 */
public class Alteracao {
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
    
    public static Alteracao perfilAlteracao(int id){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Alteracao alteracao = new Alteracao();
        
        
        try{
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Alteracao WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
        
            while(rs.next()){
                alteracao.setId(rs.getInt("id"));
                alteracao.setDsc(rs.getString("dsc"));
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Disciplina.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
        return alteracao;
    }
    
    
    @Override
    public String toString(){
        return this.dsc;
    }
    
}
