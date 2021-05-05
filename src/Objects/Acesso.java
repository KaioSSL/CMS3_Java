/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import SqlConnection.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author SynMcall
 */
public class Acesso {
    private int id;
    private Date dataLogin;
    private Date dataLogout;
    private String userLogin;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataLogin() {
        return dataLogin;
    }

    public void setDataLogin(Date dataLogin) {
        this.dataLogin = dataLogin;
    }

    public Date getDataLogout() {
        return dataLogout;
    }

    public void setDataLogout(Date dataLogout) {
        this.dataLogout = dataLogout;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }
    
    public static boolean cadastrarAcesso(Acesso acesso){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO Acesso(login,Usuario_login) VALUES(now(),?)");
            stmt.setString(1,acesso.getUserLogin());
            /*Executa a query SQL*/
            stmt.executeUpdate();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,"Erro ao Registrar Acesso, contate o Administrador ");
                return false;
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
        }
        return true;
    }
    
    public static void atualizarAcesso(Acesso acesso){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = (PreparedStatement) con.prepareStatement("UPDATE  Acesso SET logout = now() WHERE Usuario_login = ?");
            stmt.setString(1,acesso.getUserLogin());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Registrar Logof, contate o Administrador");
        }finally{
         ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
    }
    
     public static List<Acesso> buscarAcessos(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        
        List<Acesso> acessos = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Acesso");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Acesso acesso = new Acesso();
                acesso.setId(rs.getInt("id"));
                acesso.setDataLogin(rs.getDate("login"));
                acesso.setDataLogout(rs.getDate("logout"));
                acesso.setUserLogin(rs.getString("Usuario_login"));
                acessos.add(acesso);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return acessos;
    }
     
     public static Acesso perfilAcesso(int id){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Acesso acesso = new Acesso();
        
        
        try{
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Acesso WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
        
            while(rs.next()){
                
                acesso.setId(rs.getInt("id"));
                acesso.setDataLogin(rs.getDate("login"));
                acesso.setDataLogout(rs.getDate("logout"));
                acesso.setUserLogin(rs.getString("Usuario_login"));
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Disciplina.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
        return acesso;
    }
     
     public static int idAtualAcesso(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try{
            stmt = con.prepareStatement("SELECT count(*) FROM Acesso");
            rs = stmt.executeQuery();
            
            if(rs.next()){
                id = rs.getInt("count(*)");
            }
        }catch(SQLException ex){
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt, rs);
        }
     
         return id;
     }
     
    public String toString(){
       return this.userLogin;
   }
     
        
}

