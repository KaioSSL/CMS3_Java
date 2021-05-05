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
import javax.swing.JOptionPane;

/**
 *
 * @author SynMcall
 */
public class Caixas {
    private int Setores_idSetores;
    private double receita;
    private String dsc;
    private Date data;
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSetores_idSetores() {
        return Setores_idSetores;
    }

    public void setSetores_idSetores(int Setores_idSetores) {
        this.Setores_idSetores = Setores_idSetores;
    }

    public double getReceita() {
        return receita;
    }

    public void setReceita(double receita) {
        this.receita = receita;
    }
    
    public static void cadastrarCaixa(Caixas caixa){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {

            stmt = con.prepareStatement("INSERT INTO Caixas(Setores_idSetores,dsc,receita,data) VALUES(?,?,?,?)");
            /*Insere todos os atributos da classe dentro da query */
            
            stmt.setInt(1,caixa.getSetores_idSetores());
            stmt.setString(2, caixa.getDsc());
            stmt.setDouble(3, caixa.getReceita());
            stmt.setDate(4, caixa.getData());
            
            /*Executa a query SQL*/
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(null,"Caixa cadastrado com Sucesso :-)");
            
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar Caixa "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            
        }
    
    }
    
    public static List<Caixas> buscarCaixas(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Caixas> caixas = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Caixas");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Caixas caixa = new Caixas();
                caixa.setData(rs.getDate("data"));
                caixa.setDsc(rs.getString("dsc"));
                caixa.setId(rs.getInt("idCaixa"));
                caixa.setSetores_idSetores(rs.getInt("Setores_idSetores"));
                caixa.setReceita(rs.getDouble("receita"));
                caixas.add(caixa);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Caixas! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return caixas;
    }
    
    public static Caixas buscarCaixa(int id){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        Caixas caixa = new Caixas();
        caixa.setId(id);
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Caixas WHERE idCaixa = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.next()){
                caixa.setData(rs.getDate("data"));
                caixa.setDsc(rs.getString("dsc"));
                caixa.setId(rs.getInt("idCaixa"));
                caixa.setSetores_idSetores(rs.getInt("Setores_idSetores"));
                caixa.setReceita(rs.getDouble("receita"));
                
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return caixa;
    }
    
    
}
