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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author SynMcall
 */
public class Desligamento {
    private String cpf;
    private Date data;
    private String dsc;
    private int id;

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

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    public static void cadastrarDesligamento(Desligamento desligamento){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        
        try {

            stmt = con.prepareStatement("INSERT INTO Desligamentos(Pessoa_cpfPessoa,data,dsc) VALUES(?,?,?)");
            stmt.setString(1,desligamento.getCpf());
            stmt.setDate(2,desligamento.getData());
            stmt.setString(3,desligamento.getDsc());
            stmt2 = con.prepareStatement("UPDATE Membros SET desligado = 1 WHERE Pessoa_cpfPessoa = ?");
            stmt2.setString(1, desligamento.getCpf());
            
            /*Executa a query SQL*/
            stmt.executeUpdate();
            stmt2.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Desligamento cadastrado com Sucesso ");
            
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt2);
            
        }
    
    }
    public static List<Desligamento> buscarDesligamentos(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Desligamento> desligamentos = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Desligamentos");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Desligamento desligamento = new Desligamento();
                desligamento.setId(rs.getInt("idDesligamentos"));
                desligamento.setCpf(rs.getString("Pessoa_cpfPessoa"));
                desligamento.setDsc(rs.getString("dsc"));    
                desligamento.setData(rs.getDate("data"));
                desligamentos.add(desligamento);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return desligamentos;
    }
    public static Desligamento perfilDesligamento(String id){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Desligamento desligamento = new Desligamento();
        desligamento.setCpf(id);
        
        
        try{
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Desligamentos where idDesligamentos = ?");
            stmt.setString(1, id);
            rs = stmt.executeQuery();
        
            while(rs.next()){
                desligamento.setId(rs.getInt("idDesligamentos"));
                desligamento.setCpf(rs.getString("Pessoa_cpfPessoa"));
                desligamento.setDsc(rs.getString("dsc"));
                desligamento.setData(rs.getDate("data"));
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Disciplina.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
        return desligamento;
    }
    public static void religarMembro(String cpf){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try{
            stmt = con.prepareStatement("UPDATE Membros SET desligado = ? WHERE Pessoa_cpfPessoa = ?");
            stmt.setInt(1, 0);
            stmt.setString(2,cpf);
            stmt.executeUpdate();
        }catch(SQLException ex){
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
    
    }
    public static void atualizarDesligamento(Desligamento desligamento){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("UPDATE  Desligamentos SET dsc = ?, data = ? WHERE Pessoa_cpfPessoa = ?");
            stmt.setString(1,desligamento.getDsc());
            stmt.setDate(2,desligamento.getData());
            stmt.setString(3,desligamento.getCpf());
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Desligamento alterado com Sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao alterar Desligamento! ");
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
        
    
    
    
    
    }
}
