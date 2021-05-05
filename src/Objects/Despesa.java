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
public class Despesa {
    private int id;
    private double valor;
    private Date data;
    private String dsc;
    private int Setores_idSetores;
    
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
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

    public int getSetores_idSetores() {
        return Setores_idSetores;
    }

    public void setSetores_idSetores(int Setores_idSetores) {
        this.Setores_idSetores = Setores_idSetores;
    }
    
    
    public static void cadastrarDespesa(Despesa despesa){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;
        try {

            stmt = con.prepareStatement("INSERT INTO Despesas(Setores_idSetores,dsc,valor,data) VALUES(?,?,?,?)");
            /*Insere todos os atributos da classe dentro da query */
            
            stmt.setInt(1,despesa.getSetores_idSetores());
            stmt.setString(2, despesa.getDsc());
            stmt.setDouble(3, despesa.getValor());
            stmt.setDate(4, despesa.getData());
            
            /*Executa a query SQL*/
            
            stmt.executeUpdate();
            
            
            stmt2 = con.prepareStatement("SELECT idCaixa FROM Caixas WHERE Setores_idSetores = ?");
            stmt2.setInt(1, despesa.getSetores_idSetores());
            rs = stmt2.executeQuery();
            
            
            if(rs.next()){
                int id = rs.getInt("idCaixa");
                Caixas caixa = Caixas.buscarCaixa(id);
                double novaReceita = caixa.getReceita() - despesa.getValor();
                JOptionPane.showMessageDialog(null,"Despesa cadastrada com Sucesso");
            
                stmt3 = con.prepareStatement("UPDATE Caixas SET receita = ? WHERE idCaixa = ?");
                stmt3.setDouble(1, novaReceita);
                stmt3.setInt(2, id);
                stmt3.executeUpdate();
            }else{
                JOptionPane.showMessageDialog(null, "Nenhum registro de caixa encontrado");
            }
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar Despesa "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt2);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt3, rs);
            
        }
    
    }
    public static List<Despesa> buscarDespesas(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Despesa> despesas = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Despesas");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Despesa despesa = new Despesa();
                despesa.setData(rs.getDate("data"));
                despesa.setDsc(rs.getString("dsc"));
                despesa.setId(rs.getInt("idDespesa"));
                despesa.setSetores_idSetores(rs.getInt("Setores_idSetores"));
                despesa.setValor(rs.getDouble("valor"));
                despesas.add(despesa);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Despesas! ");
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return despesas;
    }
    
    
    public static List<Despesa> buscarDespesas(int id){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Despesa> despesas = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Despesas WHERE Setores_idSetores = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Despesa despesa = new Despesa();
                despesa.setData(rs.getDate("data"));
                despesa.setDsc(rs.getString("dsc"));
                despesa.setId(rs.getInt("idDespesa"));
                despesa.setSetores_idSetores(rs.getInt("Setores_idSetores"));
                despesa.setValor(rs.getDouble("valor"));
                despesas.add(despesa);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Despesas! ");
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return despesas;
    }
}
