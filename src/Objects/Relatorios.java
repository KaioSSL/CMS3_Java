/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import SqlConnection.ConnectionFactory;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author SynMcall
 */
public class Relatorios {
    
    
    public static void relatorioParametroEntreDatas(String dataInicio, String dataFim,String nomeRelatorio) throws ParseException{
        SimpleDateFormat padraoData = new SimpleDateFormat("dd/MM/yyyy");
        Connection con = ConnectionFactory.getConnection();
        Path path = FileSystems.getDefault().getPath("reports\\" + nomeRelatorio + ".jasper");
        String src = path.toString();
        path = FileSystems.getDefault().getPath("logo\\logo2.jpg");
        ImageIcon logo = new ImageIcon(path.toString());
        
        
        HashMap params = new HashMap<>();
        params.put("dataInicio", padraoData.parse(dataInicio));
        params.put("dataFim", padraoData.parse(dataFim));
        params.put("logo", logo.getImage());
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(src, params, con);
            
        } catch (JRException ex) {
            Logger.getLogger(Relatorios.class.getName()).log(Level.SEVERE, null, ex);
        }
        JasperViewer view = new JasperViewer(jasperPrint,false);
        view.setVisible(true);
    }
    public static void relatorioParametroApartir(String dataInicio,String nomeRelatorio) throws ParseException{
        SimpleDateFormat padraoData = new SimpleDateFormat("dd/MM/yyyy");
        Connection con = ConnectionFactory.getConnection();
        Path path = FileSystems.getDefault().getPath("reports\\" + nomeRelatorio + ".jasper");
        String src = path.toString();
        path = FileSystems.getDefault().getPath("logo\\logo2.jpg");
        ImageIcon logo = new ImageIcon(path.toString());
        
        HashMap params = new HashMap<>();
        params.put("dataInicio", padraoData.parse(dataInicio));
        params.put("logo", logo.getImage());
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(src, params, con);
        } catch (JRException ex) {
            Logger.getLogger(Relatorios.class.getName()).log(Level.SEVERE, null, ex);
        }
        JasperViewer view = new JasperViewer(jasperPrint,false);
        
        view.setVisible(true);
    }
    public static void relatorioParametroAte(String dataFim,String nomeRelatorio) throws ParseException{
        SimpleDateFormat padraoData = new SimpleDateFormat("dd/MM/yyyy");
        Connection con = ConnectionFactory.getConnection();
        Path path = FileSystems.getDefault().getPath("reports\\" + nomeRelatorio + ".jasper");
        String src = path.toString();
        path = FileSystems.getDefault().getPath("logo\\logo2.jpg");
        ImageIcon logo = new ImageIcon(path.toString());
        
        
        HashMap params = new HashMap<>();
        params.put("dataFim", padraoData.parse(dataFim));
        params.put("logo", logo.getImage());
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(src, params, con);
            
        } catch (JRException ex) {
            Logger.getLogger(Relatorios.class.getName()).log(Level.SEVERE, null, ex);
        }
        JasperViewer view = new JasperViewer(jasperPrint,false);
        
        view.setVisible(true);
    }
    public static void relatorioSemParametro(String nomeRelatorio){

        Connection con = ConnectionFactory.getConnection();
        Path path = FileSystems.getDefault().getPath("reports\\"+ nomeRelatorio+ ".jasper");
        String src = path.toString();
        path = FileSystems.getDefault().getPath("logo\\logo2.jpg");
        ImageIcon logo = new ImageIcon(path.toString());

        HashMap params = new HashMap<>();
        params.put("logo", logo.getImage());
        
        
        JasperPrint jasperPrint = null;
        
        try {
            jasperPrint = JasperFillManager.fillReport(src, params, con);
            
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(null, "Erro" + ex);
        }
        JasperViewer view = new JasperViewer(jasperPrint,false);
        
        view.setVisible(true);
    }
    
    
    public static void carteiraMembro(Membro membro){
        Connection con = ConnectionFactory.getConnection();
        Path path = FileSystems.getDefault().getPath("docs\\carteiraMembro.jasper");
        String src = path.toString();
        String cpf = membro.getCPF();
        path = FileSystems.getDefault().getPath("logo\\logo2.jpg");
        ImageIcon logo = new ImageIcon(path.toString());
        
        HashMap params = new HashMap<>();
        params.put("cpf",cpf);
        params.put("logo",logo.getImage());
        JasperPrint jasperPrint = null;
        try {
            jasperPrint = JasperFillManager.fillReport(src, params, con);
            
        } catch (JRException ex) {
            Logger.getLogger(Relatorios.class.getName()).log(Level.SEVERE, null, ex);
        }
        JasperViewer view = new JasperViewer(jasperPrint,false);
        
        view.setVisible(true);
    }
    
    
    
}
    

