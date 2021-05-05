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
public class Disciplina {
    private String cpf;
    private Date dataInicio;
    private String dsc;
    private Date dataFim;
    private Date dataRegistro;

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }
    
    public static void cadastrarDisciplina(Disciplina disciplina){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        
        try {

            stmt = con.prepareStatement("INSERT INTO Disciplina(Pessoa_cpfPessoa,dataInicio,dsc,dataFim,dataRegistro) VALUES(?,?,?,?,curdate())");
            stmt.setString(1, disciplina.getCpf());
            stmt.setDate(2, disciplina.getDataInicio());
            stmt.setString(3, disciplina.getDsc());
            stmt.setDate(4, disciplina.getDataFim());

            
            stmt2 = con.prepareStatement("UPDATE Membros SET disciplinado = ? WHERE Pessoa_cpfPessoa = ?");
            stmt2.setInt(1, 1);
            stmt2.setString(2,disciplina.getCpf());
            /*Executa a query SQL*/
            
            stmt.executeUpdate();
            stmt2.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Disciplina cadastrada com Sucesso ");
            
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt2);
            
        }
    
    }
    
    public static List<Disciplina> buscarDisciplinas(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Disciplina> disciplinas = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Disciplina");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Disciplina disciplina = new Disciplina();
                disciplina.setCpf(rs.getString("Pessoa_cpfPessoa"));
                disciplina.setDsc(rs.getString("dsc"));    
                disciplina.setDataInicio(rs.getDate("dataInicio"));
                disciplina.setDataFim(rs.getDate("dataFim"));
                disciplina.setDataRegistro(rs.getDate("dataRegistro"));
                disciplinas.add(disciplina);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return disciplinas;
    }
    public static Disciplina perfilDisciplina(String cpf){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Disciplina disciplina = new Disciplina();
        
        
        try{
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Disciplina where Pessoa_cpfPessoa = ?");
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
        
            while(rs.next()){
                disciplina.setCpf(cpf);
                disciplina.setDsc(rs.getString("dsc"));
                disciplina.setDataInicio(rs.getDate("dataInicio"));
                disciplina.setDataFim(rs.getDate("dataFim"));
                disciplina.setDataRegistro(rs.getDate("dataRegistro"));
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Disciplina.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
        return disciplina;
    }
    public static void atualizarDisciplina(Disciplina disciplina){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("UPDATE  Disciplina SET dsc = ?, dataInicio = ?, dataFim = ? WHERE Pessoa_cpfPessoa = ?");
            stmt.setString(1,disciplina.getDsc());
            stmt.setDate(2,disciplina.getDataInicio());
            stmt.setDate(3,disciplina.getDataFim());
            stmt.setString(4,disciplina.getCpf());
            
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Disciplina alterada com Sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao alterar Disciplina! "+ex);
        }finally{
         ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
        }
    }
    
    
    
    
}
