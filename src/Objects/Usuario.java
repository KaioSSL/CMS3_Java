/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import SqlConnection.ConnectionFactory;
import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author SynMcall
 */
public class Usuario  {
    private String login;
    private String password;
    private String cpf;
    private Date data;
    private int modoUsuario;

    public int getModoUsuario() {
        return modoUsuario;
    }

    public void setModoUsuario(int modoUsuario) {
        this.modoUsuario = modoUsuario;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public static boolean validarUsuario(Usuario usuario){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            stmt = (PreparedStatement) con.prepareStatement("SELECT login,senha FROM Usuario WHERE login = ?");
            stmt.setString(1,usuario.getLogin());
            rs = stmt.executeQuery();
            
            while(rs.next()){
                String senhaBD = rs.getString("senha");
                String loginBD = rs.getString("login");
                String senhaFream = usuario.getPassword();
                String loginFream = usuario.getLogin();
                if((loginBD == null ? loginFream == null : loginBD.equals(loginFream)) && (senhaBD == null ? senhaFream == null : senhaBD.equals(senhaFream))){
                        return true;
                    
                }
            
            }

        } catch (SQLException ex) {
            Logger.getLogger(Usuario.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
        
        return false;
    }
    
    public static void cadastrarUsuario(Usuario usuario){
        Connection con = ConnectionFactory.getConnection();
        java.sql.PreparedStatement stmt = null;
        java.sql.PreparedStatement stmt2 = null;


        
        try {

            stmt = con.prepareStatement("INSERT INTO Usuario(Pessoa_cpfPessoa,login,senha,data,modoUsuario) VALUES(?,?,?,?,?)");
            stmt.setString(1,usuario.getCpf());
            stmt.setString(2,usuario.getLogin());
            stmt.setString(3,usuario.getPassword());
            stmt.setDate(4,usuario.getData());
            stmt.setInt(5,usuario.getModoUsuario());
            
            
            stmt2 = con.prepareStatement("UPDATE Membros SET usuario = ? WHERE Pessoa_cpfPessoa = ?");
            stmt2.setInt(1, 1);
            stmt2.setString(2, usuario.getCpf());
            
            /*Executa a query SQL*/
            stmt.executeUpdate();
            stmt2.executeUpdate();

            
            JOptionPane.showMessageDialog(null,"Usuario cadastrado com Sucesso");
            
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt2);
        }
    
    }
    
    public static Usuario perfilUsuario(String login){
        Connection con = ConnectionFactory.getConnection();
        java.sql.PreparedStatement stmt = null;
        ResultSet rs = null;
        Usuario usuario = new Usuario();
        usuario.setLogin(login);
        
        
        try{
            stmt = (java.sql.PreparedStatement) con.prepareStatement("SELECT * FROM Usuario where login = ?");
            stmt.setString(1, login);
            rs = stmt.executeQuery();
        
            while(rs.next()){
                usuario.setCpf(rs.getString("Pessoa_cpfPessoa"));
                usuario.setData(rs.getDate("data"));
                usuario.setLogin(rs.getString("login"));
                usuario.setPassword(rs.getString("senha"));
                usuario.setModoUsuario(rs.getInt("modoUsuario"));
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Disciplina.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
        return usuario;
    }
    
    public static List<Usuario> buscarUsuarios(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Usuario> usuarios = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Usuario");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Usuario usuario = new Usuario();
                usuario.setCpf(rs.getString("Pessoa_cpfPessoa"));
                usuario.setData(rs.getDate("data"));
                usuario.setModoUsuario(rs.getInt("modoUsuario"));
                usuario.setLogin(rs.getString("login"));
                usuario.setPassword(rs.getString("senha"));
                
                usuarios.add(usuario);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return usuarios;
    }

    public static void alterarUsuario(Usuario usuario){
        Connection con = ConnectionFactory.getConnection();
        java.sql.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        
        try {
            stmt = (java.sql.PreparedStatement) con.prepareStatement("UPDATE  Usuario SET login = ?, senha = ?, modoUsuario = ? WHERE login = ?");
            stmt.setString(1,usuario.getLogin());
            stmt.setString(2,usuario.getPassword());
            stmt.setInt(3,usuario.getModoUsuario());
            stmt.setString(4,usuario.getLogin());
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Usuario alterado Com Sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao atualizar Usuario! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
        
    
    
    
    
    }
}
