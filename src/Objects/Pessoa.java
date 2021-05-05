package Objects;

import SqlConnection.ConnectionFactory;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import javax.swing.JOptionPane;

/**
 *
 * @author SynMcall
 */
public class Pessoa {
    private String CPF;
    private String endereco;
    private Date dataNascimento;
    private String RG;
    private String nome;
    private int estadoCivil;
    private String naturalidade;
    

    public String getNaturalidade() {
        return naturalidade;
    }

    public void setNaturalidade(String naturalidade) {
        this.naturalidade = naturalidade;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Date getdataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(int estadoCivil) {
        this.estadoCivil = estadoCivil;
    }
    public static boolean cadastrarPessoa(Pessoa pessoa){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("INSERT INTO Pessoa (cpf,nome,endereço,dataNascimento,estadoCivil,RG,naturalidade) VALUES(?,?,?,?,?,?,?)");
            /* Coloca em cada posição um atributo da classe */
            stmt.setString(1,pessoa.getCPF());
            stmt.setString(2,pessoa.getNome());
            stmt.setString(3, pessoa.getEndereco());
            stmt.setDate(4, (java.sql.Date) pessoa.getdataNascimento());
            stmt.setInt(5,pessoa.getEstadoCivil());
            stmt.setString(6,pessoa.getRG());
            stmt.setString(7,pessoa.getNaturalidade());
            
            /* Executa a query SQL */
            stmt.executeUpdate();
            
            } catch (SQLException ex) {
            if(ex instanceof SQLIntegrityConstraintViolationException){
                JOptionPane.showMessageDialog(null, "Já existe este CPF cadastrado");
                return false;
            }
            else{
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar, tente novamente!");
                return false;
            }
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
        }
        return true;
    }
    public static void alterarPessoa(Pessoa pessoa){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("UPDATE Pessoa SET  cpf = ?, nome = ?, dataNascimento = ? , estadoCivil = ?,  RG = ?, naturalidade = ? WHERE cpf = ?");
            stmt.setString(1, pessoa.getCPF());
            stmt.setString(2,pessoa.getNome());
            stmt.setDate(3,pessoa.getdataNascimento());
            stmt.setInt(4, pessoa.getEstadoCivil());
            stmt.setString(5,pessoa.getRG());
            stmt.setString(6, pessoa.getNaturalidade());
            stmt.setString(7,pessoa.getCPF());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Erro ao alterar"+ex);
        }finally{
            ConnectionFactory.closeConnection(con, (com.mysql.jdbc.PreparedStatement) stmt);
        }
    
    }
    
}
