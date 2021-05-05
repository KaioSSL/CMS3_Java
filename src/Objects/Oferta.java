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
public class Oferta {
    private double valor;
    private Date data;
    private int Setores_idSetores;
    private String dsc;
    private int id;

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

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
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
    public static void cadastrarOferta(Oferta oferta){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        ResultSet rs = null;

        
        try {

            stmt = con.prepareStatement("INSERT INTO Ofertas(valor,dsc,data,Setores_idSetores) VALUES(?,?,?,?)");
            /*Insere todos os atributos da classe dentro da query */
            
            stmt.setDouble(1,oferta.getValor());
            stmt.setString(2, oferta.getDsc());
            stmt.setDate(3, oferta.getData());
            stmt.setInt(4, oferta.getSetores_idSetores());
            
            /*Executa a query SQL*/
            
            stmt.executeUpdate();
            
            stmt2 = con.prepareStatement("SELECT idCaixa FROM Caixas WHERE Setores_idSetores = ?");
            stmt2.setInt(1, oferta.getSetores_idSetores());
            rs = stmt2.executeQuery();
            if(rs.next()){
                int id = rs.getInt("idCaixa");
                Caixas caixa = Caixas.buscarCaixa(id);
                double novaReceita = caixa.getReceita() + oferta.getValor();
                
            
                stmt3 = con.prepareStatement("UPDATE Caixas SET receita = ? WHERE idCaixa = ?");
                stmt3.setDouble(1, novaReceita);
                stmt3.setInt(2, id);
                stmt3.executeUpdate();
                JOptionPane.showMessageDialog(null,"Oferta cadastrada com Sucesso :-)");
            }else{
            
            JOptionPane.showMessageDialog(null, "Nenhum registro encontrado");}

            
            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao Cadastrar "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt2);
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt3, rs);
            
        }
    
    }
    public static List<Oferta> buscarOfertas(){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Oferta> ofertas = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Ofertas");
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Oferta oferta = new Oferta();
                oferta.setData(rs.getDate("data"));
                oferta.setDsc(rs.getString("dsc"));
                oferta.setId(rs.getInt("idOfertas"));
                oferta.setSetores_idSetores(rs.getInt("Setores_idSetores"));
                oferta.setValor(rs.getDouble("valor"));
                ofertas.add(oferta);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Ofertas! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return ofertas;
    }
    
    public static List<Oferta> buscarOfertas(int id){
        Connection con = ConnectionFactory.getConnection();
        com.mysql.jdbc.PreparedStatement stmt = null;
        ResultSet rs = null;
        
        List<Oferta> ofertas = new ArrayList<>();        
        try {
            stmt = (com.mysql.jdbc.PreparedStatement) con.prepareStatement("SELECT * FROM Ofertas WHERE Setores_idSetores = ?");
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            
            
            while(rs.next()){
                Oferta oferta = new Oferta();
                oferta.setData(rs.getDate("data"));
                oferta.setDsc(rs.getString("dsc"));
                oferta.setId(rs.getInt("idOfertas"));
                oferta.setSetores_idSetores(rs.getInt("Setores_idSetores"));
                oferta.setValor(rs.getDouble("valor"));
                ofertas.add(oferta);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao buscar Ofertas! "+ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }

        return ofertas;
    }
}
