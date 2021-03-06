package rahapeliSimulaattori;
import java.util.Arrays;
import java.util.ArrayList;

public class Ventti extends Peli {
    ArrayList<Integer> pakka = new ArrayList<Integer>();
    ArrayList<Integer> jakajanKortit = new ArrayList<Integer>();
    ArrayList<Integer> pelaajanKortit = new ArrayList<Integer>();
    
    private void alustaPakka() {
        pakka.clear();
    }
    
    /* "Kortit" ovat vain integerejä väliltä 2-14, koska ventissä maalla ei ole
    merkitystä */
    private void taytaPakka() {
        for (int i = 2; i <= 14; i++) {
            for (int j = 1; j <= 4; j++) {
                pakka.add(i);
            }
        }
    }
    
    private void alustaKadet() {
        jakajanKortit.clear();
        pelaajanKortit.clear();
    }
    
    // Apumetodi käden arvon laskemiseen
    private int kadenArvo(ArrayList<Integer> kasi) {
        int arvo = 0;
        for (Integer lisays : kasi) {
            arvo += lisays;
        }
        return arvo;
    }

    private int jako() {
        int jaetunIndeksi = arpa.nextInt( pakka.size() - 1 );
        int jaettavaKortti = pakka.get(jaetunIndeksi);
        pakka.remove(jaetunIndeksi);
        return jaettavaKortti;
    }
    
    /* "Jakajaa" simuloidaan tässä niin, että se ei tavoittele yli 17 arvoista
    kättä. Jakaja saattaa silti mennä yli, mutta pyrkii ottamaan vähän vähemmän
    riskejä */
    
    private ArrayList<Integer> jakajanKasi() {
        int jakajanArvo = 0;
        int jaettuKortti;
        do {
            jaettuKortti = jako();
            jakajanKortit.add(jaettuKortti);
            jakajanArvo = kadenArvo(jakajanKortit);
            if (jakajanArvo > 21 && jakajanKortit.contains(14)) {
                jakajanKortit = assanVaihto(jakajanKortit);
                jakajanArvo = kadenArvo(jakajanKortit);
                System.out.println("Jakajan arvo: " + jakajanArvo);
            }
        } while (jakajanArvo <= 17);
        return jakajanKortit;
    }
    
    private ArrayList<Integer> pelaajanKasi() {
        int pelaajanArvo = 0;
        int jaettuKortti;
        char jatketaan;
        do {
            jaettuKortti = jako();
            pelaajanKortit.add(jaettuKortti);
            pelaajanArvo = kadenArvo(pelaajanKortit);
            System.out.println("Sait kortin " + jaettuKortti + ", kätesi arvo on" 
                + " nyt " + pelaajanArvo);
            if (pelaajanArvo > 21) {
                if (pelaajanKortit.contains(14)) {
                    pelaajanKortit = assanVaihto(pelaajanKortit);
                    pelaajanArvo = kadenArvo(pelaajanKortit);
                    System.out.println("Ässät vaihdettu");
                    System.out.println("Kätesi arvo on nyt " + pelaajanArvo);
                }
                else {
                    System.out.println("Käden arvo on yli 21!");
                    break;
                }
            }
            System.out.print("Haluatko lisää kortteja (k/e)? ");
            jatketaan = syotaChar();
        } while (pelaajanArvo <= 21 && jatketaan != 'e');
        return pelaajanKortit;
    }
    
    /* Ventissä ässä on arvoltaan 1 tai 14, riippuen siitä kumpi on sopivampi
    Käytännössä ässän kannattaa aina olla 14, paitsi jos se aiheuttaa häviön.
    Tällöin ässästä tulee 1, ja kortteja voidaan ottaa lisää */
    private ArrayList<Integer> assanVaihto(ArrayList<Integer> vaihdettavatKortit) {
        for (int i = 0; i<=vaihdettavatKortit.size() - 1; i++) {
            if (vaihdettavatKortit.get(i) == 14) {
                vaihdettavatKortit.set(i, 1);
            }
        }
        return vaihdettavatKortit;
    }
    
    // Vertaillaan mahdollisia lopputuloksia
    private String kukaVoitti(ArrayList<Integer> pelaajanKortit,
        ArrayList<Integer> jakajanKortit) {
        int pelaajanArvo = kadenArvo(pelaajanKortit);
        int jakajanArvo = kadenArvo(jakajanKortit);
        if (pelaajanArvo > 21 && jakajanArvo > 21) {
            System.out.println("Jakaja voitti!");
            return "jakaja";
        }
        else if (pelaajanArvo > jakajanArvo && pelaajanArvo <= 21 ||
        jakajanArvo > 21) {
            System.out.println("Voitit jakajan!");
            return "pelaaja";
        }
        else if (pelaajanArvo > 21 || jakajanArvo > pelaajanArvo) {
            System.out.println("Jakaja voitti!");
            return "jakaja";
        }
        else {
            System.out.println("Tasapeli!");
            return "tasapeli";
        }
    }
    
    public double pelaa(double rahaMaara, double panos) {
        boolean pelaa = true;
        String voittaja;
        double voitto;
        while (pelaa && rahaMaara >= panos) {
            rahaMaara -= panos;
            alustaPakka();
            taytaPakka();
            alustaKadet();
            voittaja = kukaVoitti(pelaajanKasi(), jakajanKasi());
            if (voittaja.equals("pelaaja")) {
                voitto = 2 * panos;
                voitto = tuplausCheck(voitto);
                rahaMaara += voitto;
            }
            else if (voittaja.equals("jakaja")) {
                System.out.println("Hävisit rahasi!");
            }
            else {
                voitto = panos;
                rahaMaara += voitto;
                System.out.println("Saat pitää rahasi.");
            }
            System.out.println("Sinulla on nyt " + rahaMaara + "0€");
            pelaa = jatkaminen();
        }
        return rahaMaara;
    }
}
