package replacecode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.util.Objects;

public class TratarArquivoPas {
    
    public void processarArquivos(File file, String encontrar, String substituir, String extensao) throws Exception {
        File[] lista = file.listFiles();
        
        processarRecursivo(lista, encontrar, substituir, extensao);
    }
    
    private void processarRecursivo(File[] file, String encontrar, String substituir, String extensao) throws Exception {
        for (File arquivo : file) {
            if (arquivo.isDirectory()) {
                processarRecursivo(arquivo.listFiles(), encontrar, substituir, extensao);
            } else if (arquivo.getName().endsWith(extensao)) {
                editarArquivo(arquivo, encontrar, substituir);
            }
        }
    }
    
    private void editarArquivo(File file, String encontrar, String substituir) throws Exception {
        StringBuilder texto = null;
        boolean achou = false;
        
        try (BufferedReader buffRead = new BufferedReader(new FileReader(file, Charset.forName("Cp1252")))) {
            texto = new StringBuilder();
            String linha;            
            while ((linha = buffRead.readLine()) != null) {
                if (linha.toLowerCase().contains(encontrar.toLowerCase()) && 
                        !linha.trim().toLowerCase().equals(substituir.trim().toLowerCase())) {
                    achou = true;
                    int index = linha.toLowerCase().indexOf(encontrar.toLowerCase());

                    linha = linha.substring(0, index) + substituir + linha.substring(index + encontrar.length());
                }
                
                texto.append(linha).append("\r\n");
            }
        }
        if (achou)
            gravarModificacao(file, texto);
    }
    
    public void gravarModificacao(File file, StringBuilder novo) throws Exception {
        if (Objects.nonNull(novo)) {
            try (BufferedWriter buffWrite = new BufferedWriter(new FileWriter(file, Charset.forName("Cp1252")))) {
                buffWrite.write(novo.toString());
            }
        }
    }
    
}
