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
public class Consagracao {
    private  String dsc;
    private Date data;
    private int Cargos_idCargo;
    private String cpf;

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }


    public int getCargos_idCargo() {
        return Cargos_idCargo;
    }

    public void setCargos_idCargo(int Cargos_idCargo) {
        this.Cargos_idCargo = Cargos_idCargo;
    }

    public static void cadastrarConsagracao(Consagracao consagracao){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        
        try {

            stmt = con.prepareStatement("INSERT INTO Consagração(Pessoa_cpfPessoa,data,dsc,Cargos_idCargo) VALUES(?,?,?,?)");
            stmt.setString(1,consagracao.getCpf());
            stmt.setDate(2,consagracao.getData());
            stmt.setString(3,consagracao.getDsc());
            stmt.setInt(4, consagracao.getCargos_idCargo());
            
            stmt2 = con.prepareStatement("UPDATE Membros SET cargo = ? WHERE Pessoa_cpfPessoa = ?");
            stmt2.setInt(1,consagracao.getCargos_idCargo());
            stmt2.setString(2,consagracao.getCpf());
            /*Executa a query SQL*/
            stmt.executeUpdate();
            stmt2.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Consagração registrada com Sucesso ");
            
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt2);
        }
    
    }
    public static List<Consagracao> buscarConsagracoes(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Consagracao> consagracoes = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Consagração");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Consagracao consagracao = new Consagracao();
                consagracao.setCpf(rs.getString("Pessoa_cpfPessoa"));
                consagracao.setDsc(rs.getString("dsc"));    
                consagracao.setData(rs.getDate("data"));
                consagracao.setCargos_idCargo(rs.getInt("Cargos_idCargo"));
                consagracoes.add(consagracao);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return consagracoes;
    }
    public static Consagracao perfilConsagracao(String cpf){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Consagracao consagracao = new Consagracao();
        consagracao.setCpf(cpf);
        
        
        try{
            stmt = (PreparedStatement) con.prepareStatement("SELECT * FROM Consagração where Pessoa_cpfPessoa = ?");
            stmt.setString(1, cpf);
            rs = stmt.executeQuery();
        
            while(rs.next()){
                consagracao.setCpf(cpf);
                consagracao.setDsc(rs.getString("dsc"));
                consagracao.setData(rs.getDate("data"));
                consagracao.setCargos_idCargo(rs.getInt("Cargos_idCargo"));
            }
        
        } catch (SQLException ex) {
            Logger.getLogger(Disciplina.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
        return consagracao;
    }
    public static void atualizarConsagracao(Consagracao consagracao){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        
        try {
            stmt = (PreparedStatement) con.prepareStatement("UPDATE  Consagração SET dsc = ?, data = ?,Cargos_idCargo = ? WHERE Pessoa_cpfPessoa = ?");
            stmt.setString(1,consagracao.getDsc());
            stmt.setDate(2,consagracao.getData());
            stmt.setInt(3, consagracao.getCargos_idCargo());
            stmt.setString(4,consagracao.getCpf());
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null, "Consagração alterada com Sucesso");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao atualizar Desligamento! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt,rs);
    
    }
        
    
    
    
    
    }
}
