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
public class Dizimos {
    private String Pessoa_cpfPessoa;
    private double valor;
    private Date data;

    public String getPessoa_cpfPessoa() {
        return Pessoa_cpfPessoa;
    }

    public void setPessoa_cpfPessoa(String Pessoa_cpfPessoa) {
        this.Pessoa_cpfPessoa = Pessoa_cpfPessoa;
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
    
    public static void cadastrarDizimo(Dizimos dizimo){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;

        int idCaixa = 1;

        
        try {

            stmt = con.prepareStatement("INSERT INTO Dizimos(valor,data,Pessoa_cpfPessoa) VALUES(?,?,?)");
            /*Insere todos os atributos da classe dentro da query */
            
            stmt.setDouble(1,dizimo.getValor());
            stmt.setDate(2, dizimo.getData());
            stmt.setString(3, dizimo.getPessoa_cpfPessoa());
            
            
            /*Executa a query SQL*/
            
            stmt.executeUpdate();

            Caixas caixa = Caixas.buscarCaixa(idCaixa);
            double novaReceita = caixa.getReceita() + dizimo.getValor();
            stmt2 = con.prepareStatement("UPDATE Caixas SET receita = ? WHERE idCaixa = ?");
            stmt2.setDouble(1, novaReceita);
            stmt2.setInt(2, idCaixa);
            stmt2.executeUpdate();
            JOptionPane.showMessageDialog(null,"Dizimo cadastrado com Sucesso :-)");

            
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt2);

            
        }
    
    }
    public static List<Dizimos> buscarDizimos(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Dizimos> dizimos = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Dizimos");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Dizimos dizimo = new Dizimos();
                dizimo.setData(rs.getDate("data"));
                dizimo.setPessoa_cpfPessoa(rs.getString("Pessoa_cpfPessoa"));
                dizimo.setValor(rs.getDouble("valor"));
                dizimos.add(dizimo);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Dizimos! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return dizimos;
    }
}
