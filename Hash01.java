import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.Scanner;

public class Hash01 {
    Scanner sc = new Scanner(System.in);
    ArrayList <String> usuaris = new ArrayList<>();
    ArrayList <String> contrseñas = new ArrayList<>();
    ArrayList<String> salts = new ArrayList<>();
    
    public static void main(String[] args) {
        Hash01 p = new Hash01();
        p.principal();
       
    }

    public void principal(){
        int tancar = -1;

        do {
            System.out.println("1. Càlcul de resums");
            System.out.println("2. Verificació de la integritat dels missatges");
            System.out.println("3. Registre d'usuaris amb salt");
            System.out.println("4. Inici de sessió i verificació de contrasenyes");
            System.out.println("5. Sortir");
            System.out.print("Intrdueix el que vols fer: ");
            int eleccio = sc.nextInt();
            sc.nextLine();

            switch (eleccio) {
                case 1:
                    exercici1();
                    break;
                
                case 2:
                    exercici2();
                    break;

                case 3:
                    exercici3();
                    break;

                case 4:
                    exercici4();
                    break;

                case 5: 
                    tancar = 5;
                    break;
            
                default:
                    break;
            }

        } while (tancar != 5);
        sc.close();
    }

    public void exercici1(){
        System.out.println();
        System.out.println("Ara has de introduir dos missatges nous.");
        System.out.print("Primer missatge:");
        String missatge1 = sc.nextLine();
        String missatgeFinal1 = demanarHash(missatge1);
        System.out.println("El teu hash es: " + missatgeFinal1);

        System.out.println();
        System.out.print("Ara segon missatge:");
        String missatge2 = sc.nextLine();
        String missatgeFinal2 = demanarHash(missatge2);
        System.out.println("El teu hash es: " + missatgeFinal2);
        System.out.println();

        if(missatgeFinal1.equals(missatgeFinal2)){
            System.out.println("Els dos missatges que has introduit son iguals");
        }
        else{
            System.out.println("Els dos missatges son diferents.");
        }
        System.out.println();

        System.out.println("Ara hauras de introduir un nou missatge i el editaras");
        System.out.print("Introdueix el missatge: ");
        String missatgeModificat = sc.nextLine();
        String missatgeModificatFinal = demanarHash(missatgeModificat);
        System.out.println("El teu hash es: " + missatgeModificatFinal);

        System.out.println();
        System.out.println("Ara el has de modificar: ");
        missatgeModificat = sc.nextLine();
        missatgeModificatFinal = demanarHash(missatgeModificat);
        System.out.println("El teu hash es nou es : " + missatgeModificatFinal);
        
        System.out.println();

        System.out.println("Ara introduiras un missatge i les vegades que vols comprobar el resultat.");
        System.out.print("Introdueix el missatge: ");
        String missatgePerComprobar = sc.nextLine();
        System.out.println();
        System.out.print("Introdueix els cops que el vols repetir: ");
        int quantitat = sc.nextInt();

        for(int i = 0; i < quantitat; i++){
            System.out.println("Comprobacio " + (i+1));
            System.out.println("El teu hash es: " + demanarHash(missatgePerComprobar));
            System.out.println();
        }

    }

    public void exercici2(){
    try {
        System.out.println("En aquest exercici farem una comprobacio de missatges modificats, per aixo necesito que hem donis un missatge.");
        System.out.print("Introdueix el missatge: ");
        String missatge = sc.nextLine();
        Files.writeString(Path.of("missatge.txt"), missatge);
        
        String missatgeHash = demanarHash(missatge);
        Files.writeString(Path.of("missatge.hash"), missatgeHash);
       
        System.out.println("Missatge guardat...");
        System.out.println("Acabes de rebre un missatge...");

        String missatgeRebut;
        System.out.print("Introdueix el missatge que vols rebre:");
        String missatgeEsperat = sc.nextLine();
        missatgeRebut = Files.readString(Path.of(missatgeEsperat));
        System.out.println(missatgeRebut);
        String missatgeRebutFinal =demanarHash(missatgeRebut);


        String missatgeRebutHash;
        missatgeRebutHash = Files.readString(Path.of("missatge.hash"));

        System.out.println("Comprobacio del missatge rebut i missatge enviat:");
        System.out.println("Missatge enviat: " + missatge + " el hash " + missatgeRebutHash);
        System.out.println("Missatge rebut: " + missatgeRebut + " el hash " + missatgeRebutFinal);

        if(missatgeRebutFinal.equals(missatgeRebutHash)){
            System.out.println("Els dos missatges son iguals.");
            System.out.println();
        }
        else{
            System.out.println("Els dos missatges son diferents.");
            System.out.println();
        }
    } catch (NoSuchFileException e) {
        System.out.println("No s'ha trobat el fitxer: " + e.getMessage());
    } catch (IOException e) {
        System.out.println("Error de lectura/escriptura: " + e.getMessage());
    }
}

    public void exercici3(){
        System.out.println();

        boolean usuariExistent = false;

        System.out.print("Introdueix el teu usuari:");
        String usuari = sc.nextLine();

        for(int i = 0; i < usuaris.size(); i ++){
            if(usuaris.get(i).equals(usuari)){
                usuariExistent = true;
            }
        }

        if(usuariExistent){
            System.out.println("El usuari ja existeix.");
            System.out.println("Tornant al menu...");
            System.out.println();
        }
        else{
            System.out.print("Introdueix la nova contrasenya: ");
            String contrasenya = sc.nextLine(); 
            
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            String saltHex = HexFormat.of().formatHex(salt);

            String contrasenyaFinal = saltHex + contrasenya;
            String contrasenyaAmbHash = demanarHash(contrasenyaFinal);

            usuaris.add(usuari);
            contrseñas.add(contrasenyaAmbHash);
            salts.add(saltHex);


            System.out.println("Usuari creat.");
            System.out.println();
        }
        
    }
   
    public void exercici4(){
        int usuariTrobat = -1;
        String salt;
        String contrasenyaFinal;
        String resultatHash;

        System.out.println();
        System.out.println("Vols iniciar sesio!");
        System.out.print("Introdueix el teu usuari: ");
        String usuari = sc.nextLine();

        System.out.print("Introdueix la teva contrasenya: ");
        String contrsenya = sc.nextLine();

        for(int i = 0; i < usuaris.size(); i++){
            if(usuaris.get(i).equals(usuari)){
                usuariTrobat = i;
            }
        }

        if(usuariTrobat != -1){
                salt = salts.get(usuariTrobat);
                contrasenyaFinal = contrseñas.get(usuariTrobat);

                salt = salt + contrsenya;

                resultatHash = demanarHash(salt);

                if(resultatHash.equals(contrasenyaFinal)){
                    System.out.println("Sesio iniciada.");
                    System.out.println("Tornant al menu.");
                    System.out.println();
                }
                else{
                    System.out.println("Contrasenya incorrecta.");
                    System.out.println("Tornant al menu.");
                    System.out.println();
                }
        }
        else{
            System.out.println("Usuari no trobat.");
            System.out.println("Tornant al menu.");
            System.out.println();
        }
    }

     public String demanarHash(String textIn){
        StringBuilder textOut = new StringBuilder();

        try {
            // Convertim el text a un array de bytes utilitzant UTF-8
            byte[] bytesIn = textIn.getBytes(StandardCharsets.UTF_8);

            // Creem l'objecte per calcular el hash SHA-512
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

            // Calculem el hash
            byte[] hashResult = sha256.digest(bytesIn);

            // Convertim el resultat a hexadecimal i el mostrem per pantalla
            textOut = new StringBuilder();
            for (int i = 0; i < hashResult.length; i++) {
                if (i > 0) {
                    textOut.append("-");
                }
                textOut.append(String.format("%02X", hashResult[i] & 0xFF));
            }

        } catch (NoSuchAlgorithmException e) {
            System.out.println("No s'ha trobat l'algoritme SHA-256: " + e.getMessage());
        }
            
            return textOut.toString();
        }
        
}
