/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author SynMcall
 */
public class Utilitarios {
    
    public static String buscarFoto(JPanel panel,JLabel fotoMembro){
        //Cria o file choser
        JFileChooser fileChooser = new JFileChooser();
        //Permite que o usuario apenas selecione arquivos e nao pastas
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        //Mostra o File Chosser em cima de alguem
        int seletorAberto = fileChooser.showOpenDialog(panel);
        //Filtra as extensoes que aparecerao
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Foto",
                "jpg","png","jpeg","jfif");
        //Implementa dentro da função o filtro definido a cima
        fileChooser.setFileFilter(filter);
        //Coloca titulo do file Chooser
        fileChooser.setDialogTitle("Buscar Foto");
        Path path = FileSystems.getDefault().getPath("choosePhoto.png");
        String caminhoDefault = path.toString();

        if(seletorAberto == JFileChooser.APPROVE_OPTION){
            File file = fileChooser.getSelectedFile();
            /*
            ImageIcon img = new ImageIcon(file.getPath());
            Image img1 = img.getImage();
            Image img2 = img1.getScaledInstance(fotoMembro.getWidth(), fotoMembro.getHeight(), Image.SCALE_DEFAULT);
            ImageIcon imgFinal = new ImageIcon(img2);*/
            fotoMembro.setIcon(redimensionarFoto(file.getPath(),fotoMembro));
            caminhoDefault = file.getPath();
        }
        else{
            if(seletorAberto == JFileChooser.CANCEL_OPTION){
                ImageIcon imgDefault;
                imgDefault = new ImageIcon(caminhoDefault);
                fotoMembro.setIcon(imgDefault);
                

            }
        
        }
        return caminhoDefault;
    }
    public static void  atualizarPainel(JPanel panelPai, JPanel filho){
        panelPai.removeAll();
        panelPai.revalidate();
        panelPai.repaint();
        
        panelPai.add(filho);
        panelPai.revalidate();
        panelPai.repaint();
        
    
    
    }
    public static Date dataAtual(){
        Date d = new Date();
        return d;
    }
  
    public static void fotoDefault(JLabel fotoMembro){
           ImageIcon imgDefault;
           Path path = FileSystems.getDefault().getPath("src\\Image\\choosePhoto.png");
           String caminhoDefault = path.toString();
           imgDefault = new ImageIcon(caminhoDefault);
           fotoMembro.setIcon(imgDefault);


    }
    public static void limparField(JPanel panel){
        Component[] campos = panel.getComponents();
        for(int i =0;i<campos.length;i++){
            if(campos[i] instanceof JTextField){
                ((JTextField)campos[i]).setText("");
            }else{
                if(campos[i] instanceof JTextArea){
                    ((JTextArea)campos[i]).setText("");
                }
                else{
                    if(campos[i] instanceof JScrollPane){
                        limparField((JScrollPane)campos[i]);
                       }
                }
            }
        }
    }
        
   
    public static void limparField(JScrollPane panel){
        JViewport viewport = panel.getViewport();
        Component[] campos = viewport.getComponents();
        for(int i =0;i<campos.length;i++){
            if(campos[i] instanceof JTextField){
                    ((JTextField)campos[i]).setText("");
            }else{
                if(campos[i] instanceof JTextArea){
                    ((JTextArea)campos[i]).setText("");
                    ;
                }
            }
        }

    }
    public static ImageIcon redimensionarFoto(String caminho,JLabel recipiente){
        
        ImageIcon img = new ImageIcon(caminho);
        Image image = img.getImage();
        Image image2 = image.getScaledInstance(recipiente.getWidth(), recipiente.getHeight(), Image.SCALE_DEFAULT);
        ImageIcon imgFinal = new ImageIcon(image2);



        return imgFinal;

    }
    public static boolean validarCPF(String cpf){
        String cpf2 = cpf.replace(".", "");
        int cont = 10;
        int soma = 0;
        String c1 = "";
        String c2 = "";
        //Primeira validação
        for(int i = 0;i<9;i++){
            soma += cont * Integer.parseInt(String.valueOf(cpf2.charAt(i)));
            cont = cont -1;
        }
        if(11 - (soma%11)>9){
            c1 ="0";
        }else{
            c1 = String.valueOf(11 - (soma%11));
        }
        cont = 11;
        cpf2 = cpf2.replace("-", "");
        soma = 0;
        for(int i = 0;i<10;i++){
            soma += cont * Integer.parseInt(String.valueOf(cpf2.charAt(i)));
            cont = cont -1;
        }
        
        if(11 - soma%11 > 9){
            c2 = "0";
        }else{
            c2 = String.valueOf(11 - (soma%11));
        }
        if(c1.equals(String.valueOf(cpf2.charAt(cpf2.length()-2))) && c2.equals(String.valueOf(cpf2.charAt(cpf2.length()-1))) ){
            return true;
        }
        else{
            return false;
        }
    }
    private static boolean validarData(String data){
        String data2 = data.replace("/","");
        int dia = Integer.parseInt(data2.substring(0, 2));
        int mes = Integer.parseInt(data2.substring(2,4));
        int ano  = Integer.parseInt(data2.substring(4));
        
        if(mes>12 || dia > 31){
            return false;
        }else{
        if(ano%4!=0 && mes == 2 && dia > 28){
            return false;
        }else{
        if( (mes == 4 && dia>31) || (mes == 6 && dia>31) || (mes == 9 && dia>31) || (mes == 11 && dia>31)){
                    return false;
                }else{
                    return true;
                }
            }
        }
    }
    public static boolean validarEmail(String email){
        int cont1 = 0;
        int cont2 = 0;
        String a = "@";
        String p = ".";
        for(int i =0;i<email.length();i++){
           
            if(a.equals(String.valueOf(email.charAt(i)))){
                
                cont1++;

            }
            else if(String.valueOf(email.charAt(i)).equals(p)){
                cont2++;
            }
        }
        if(cont1 == 1 && cont2>0){
            return true;
        }else{
            JOptionPane.showMessageDialog(null,"Informe um email válido");
            return false;
        }
        
    } 
    
    public static boolean validarCampos(JPanel panel){
        Component[] campos = panel.getComponents();
        for(int i = 0;i<campos.length;i++){
                if(campos[i] instanceof JFormattedTextField){
                    JFormattedTextField campo = (JFormattedTextField) campos[i];
                    if(campo.getText().length()==14){
                        String cpf = campo.getText().replace(" ", "");
                        if(cpf.length()<14){
                            return false;
                            
                        }else{
                            if(!validarCPF(cpf)){
                                JOptionPane.showMessageDialog(null,"Preencha com CPF válido");
                                return false;
                            }
                        }
                    }
                    else{
                        if(campo.getText().length()==10){
                            String data = campo.getText().replace(" ", "");
                            if(data.length()<10){
                                return false;
                            }else{
                                if(!validarData(data)){
                                    JOptionPane.showMessageDialog(null, "Preencha com Datas Válidas");
                                    return false;
                                }
                            }
                        }
                    else{
                        if(campo.getText().length()==12){
                            String rg = campo.getText().replace(" ", "");
                            if(rg.length()<12){
                                return false;
                            }
                        }
                    else{
                        if(campo.getText().length()==16){
                            String telefone = campo.getText().replace(" ","");
                            if(telefone.length()<14){
                                return false;
                            }
                        }
                    }
                    }
                    }
                }
                else{
                    if(campos[i] instanceof JTextField){
                        JTextField campo = (JTextField) campos[i];
                        if("".equals(campo.getText())){
                            JOptionPane.showMessageDialog(null,"Não deixe Campos Vazios");
                            return false;
                        }
                    }
                }
            }
        
    return true;
    }
    
    public static String reconstruirData(String data){
        String dataAux = data.replaceAll("-", "");
        String dataFinal = "";
        dataFinal = dataAux.substring(6) + "/" + dataAux.substring(4, 6) + "/" + dataAux.substring(0, 4);
        return dataFinal;
    }
    
    
    
    public static void gerarAlteracaoBD(String nomeRelatorio,int idAcesso){
        if(nomeRelatorio=="relatorioMembros"){
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(idAcesso);
            alteracao.setAlteracao_id(16);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
        }else if(nomeRelatorio=="relatorioDesligamentos"){
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(idAcesso);
            alteracao.setAlteracao_id(17);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
        }else if(nomeRelatorio == "relatorioConsagracoes"){
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(idAcesso);
            alteracao.setAlteracao_id(18);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
        }else if(nomeRelatorio == "relatorioDisciplinas"){
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(idAcesso);
            alteracao.setAlteracao_id(19);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
        }else if(nomeRelatorio == "relatorioDizimos"){
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(idAcesso);
            alteracao.setAlteracao_id(21);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
        }else if(nomeRelatorio == "relatorioDespesas"){
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(idAcesso);
            alteracao.setAlteracao_id(22);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
        }else if(nomeRelatorio == "relatorioOfertas"){
            Acesso_Alteracao alteracao = new Acesso_Alteracao();
            alteracao.setAcesso_id(idAcesso);
            alteracao.setAlteracao_id(23);
            Acesso_Alteracao.cadastrarAcesso(alteracao);
        }
    }
    
    
    
}
    