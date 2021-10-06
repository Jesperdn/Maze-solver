
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.*;
import java.util.ArrayList;

public class LabyrintSystem3000 {

    static File fil;
    static Color bakgrunnsFargeP = Color.decode("#1b2838"); 
    static Color bakgrunnsFargeS = Color.decode("#71e24");
    static Color bakgrunnsFargeO = Color.decode("#1c2a35");
    static Color tekstFarge = Color.decode("#b0b8bf"); 
    static Color tallFarge = Color.decode("#67c1f5");
    static Color veggFarge = Color.decode("#71e24"); 
    static Color aapenFarge = Color.decode("#a9c4f1");
    static Color hoverFarge = Color.decode("#dce7f9");
    static Color utveiFarge = Color.decode("#3be33b");
    static Color rammeFarge = Color.decode("#ED1E79");
    static Font infoFont = new Font("Calibri", Font.PLAIN, 20);

    static JFrame vinduet;
    static JPanel inneholderLabyrintPanel;
    static LabyrintPanel labyrintPanel;
    static SideBoks sideBoks;
    static ListeBoks listeBoks;
    static Labyrint labyrint;
    static YtrePanel ytrePanel;
    static Rute[][] ruter;
    static int rStr;    //Rute-størrelse
    static int rStrH;    //Rute-størrelse ved Hover
    static ArrayList<ArrayList<Tuppel>> utveier;
    static ArrayList<ArrayList<Tuppel>> utveierSortert;
    static ArrayList<Tuppel> kortesteUtvei;
    static ArrayList<Tuppel> lengsteUtvei;
    static int utveiIndex;
    static int utveierIListe;

    public static void main(String[] args) {
        
        JFileChooser velger = new JFileChooser(new File(System.getProperty("user.dir")));
        int resultat = velger.showOpenDialog(null);
        if (resultat == JFileChooser.APPROVE_OPTION) {
            fil = velger.getSelectedFile();
        } else {
            System.exit(0);
        }

        vinduet = new JFrame("LabyrintSystem 3000");
        vinduet.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ytrePanel = new YtrePanel();
        ytrePanel.initGUI();
        vinduet.add(ytrePanel);

        vinduet.pack(); 
        vinduet.setLocationRelativeTo(null);
        vinduet.setVisible(true);

    }

    public static class YtrePanel extends JPanel {
        
       void initGUI() {
            setBackground(bakgrunnsFargeP);
            sideBoks = new SideBoks();
            inneholderLabyrintPanel = new JPanel();
            inneholderLabyrintPanel.setBackground(bakgrunnsFargeP);
            labyrintPanel = new LabyrintPanel(fil, sideBoks);
            sideBoks.initGUI();
            labyrintPanel.initGUI();
            
            listeBoks = new ListeBoks();
            listeBoks.initGUI();
            inneholderLabyrintPanel.add(labyrintPanel);
            add(inneholderLabyrintPanel);
            add(sideBoks);
            add(listeBoks);
            
        }
    }

    static class ListeBoks extends JPanel {

        static JPanel listeOmrade;

        void initGUI() {
            setBackground(bakgrunnsFargeS);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.black, 2), 
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
            ));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(150, 380));

            JLabel overskrift = new JLabel("Alle utveier");
            overskrift.setFont(infoFont);
            overskrift.setForeground(tekstFarge);
            listeOmrade = new JPanel();
            listeOmrade.setAlignmentX(CENTER_ALIGNMENT);
            listeOmrade.setBackground(bakgrunnsFargeO);
            listeOmrade.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,2,1, Color.black),
                BorderFactory.createEmptyBorder(2,5,2,5)
            ));
            overskrift.setAlignmentX(CENTER_ALIGNMENT);
            add(overskrift);
            add(listeOmrade);
        }

        void lagListe(ArrayList<ArrayList<Tuppel>> utveier) {
            JPanel liste = new JPanel();
            liste.setBackground(Color.decode("#242525")); 
            liste.setLayout(new BoxLayout(liste, BoxLayout.Y_AXIS));
            for (int index = 0; index < utveier.size(); index++) {
                ListeKnapp nyttValg = new ListeKnapp("Utvei ",index);
                nyttValg.initGUI();
                nyttValg.setAlignmentX(CENTER_ALIGNMENT);
                liste.add(nyttValg);
            }
            listeOmrade.removeAll();
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setBackground(Color.decode("#242525")); 
            scrollPane.setViewportView(liste);
            scrollPane.setAlignmentX(CENTER_ALIGNMENT);
            scrollPane.setPreferredSize(new Dimension(120, 315));
            scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            listeOmrade.add(scrollPane);
            listeOmrade.revalidate();
        }
    }

    static class ListeKnapp extends JButton {
        String tekst;
        int index;

        ListeKnapp(String tekst, int index) {
            super(tekst + index);
            this.index = index;
        }

        class Interact implements MouseListener {

            @Override 
            public void mouseEntered(MouseEvent e) {
                setBorderPainted(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorderPainted(false);
            }

            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            
            public void mouseClicked(MouseEvent e) {
                labyrintPanel.visUtvei(utveier.get(index), false);
                setBorderPainted(false);
                sideBoks.nummer.setText(String.valueOf(index));
            }

        }

        void initGUI() {
            setMinimumSize(new Dimension(70, 50));
            setBackground(Color.decode("#242525")); 
            setForeground(tekstFarge);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 0, 0, 0), 
                BorderFactory.createMatteBorder(0, 0, 2, 0, Color.decode("#383c40"))));
            setBorderPainted(false);
            addMouseListener(new Interact());
        }
    }

    public static class InfoTekst extends JLabel {
        String s;

        InfoTekst(String s, Color tekstFarge) {
            super(s);
            setBackground(bakgrunnsFargeO);
            setForeground(tekstFarge);
            setFont(infoFont);
            setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        }

    }

    public static class InfoLinje extends JPanel {

        InfoTekst v;
        InfoTekst h;

        InfoLinje(InfoTekst v, InfoTekst h) {
            this.v = v;
            this.h = h;
        }

        void initGUI() {
            setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
            setFont(infoFont);
            setBackground(bakgrunnsFargeO);
            setForeground(tekstFarge);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(4,0,0,0),
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#283641")) 
            ));

            add(v);
            add(Box.createHorizontalGlue());
            add(h);
        }
    }

    public static class TallViser extends InfoTekst {

        TallViser(String s, Color farge) {
            super(s, farge);
        }

        void initGUI() {
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 5, 0, 5), 
                BorderFactory.createLineBorder(Color.decode("#283641"), 2)
            ));
            setHorizontalAlignment(JLabel.CENTER);
            setVerticalAlignment(JLabel.CENTER);
        }
    }

    public static class SideBoks extends JPanel {

        InfoLinje radOgKolonne;
        InfoLinje antUtveier;
        InfoLinje antSteg;
        InfoLinje antStegKorteste;
        InfoLinje antStegLengste;

        InfoTekst rad;
        InfoTekst kolonne;
        InfoTekst valgtRute;
        InfoTekst utveier;
        InfoTekst steg;
        InfoTekst stegKorteste;
        InfoTekst stegLengste;
        
        static TallViser nummer;
        PilKnapp hoyre;
        PilKnapp venstre;

        VelgFil nyLabyrintKnapp;

        void initGUI() {
            setBackground(bakgrunnsFargeS);
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(305,380));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.black, 2), 
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
            ));

            venstre = new PilKnapp("Forrige", 0);
            hoyre = new PilKnapp("Neste", 1);
            nummer = new TallViser("", tekstFarge);
            nummer.initGUI();
            PilBoks pilboks = new PilBoks(venstre, hoyre, nummer);
            pilboks.initGUI();

            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.setAlignmentX(Component.CENTER_ALIGNMENT);
            info.setBackground(bakgrunnsFargeO);
            info.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,2,1, Color.black),
                BorderFactory.createEmptyBorder(2,5,2,5)
            ));
            rad = new InfoTekst("Rader: ", tekstFarge);
            kolonne = new InfoTekst("Kolonner: ", tekstFarge);
            valgtRute = new InfoTekst("Utveier fra (x,y): ", tekstFarge);
            utveier = new InfoTekst("", tallFarge);
            stegKorteste = new InfoTekst("", tallFarge);
            stegLengste = new InfoTekst("", tallFarge);
            steg = new InfoTekst("", tallFarge);

            radOgKolonne = new InfoLinje(rad, kolonne);
            radOgKolonne.initGUI();
            antUtveier = new InfoLinje(valgtRute, utveier);
            antUtveier.initGUI();
            antStegKorteste = new InfoLinje(new InfoTekst("Steg i korteste: ", tekstFarge), stegKorteste);
            antStegKorteste.initGUI();
            antStegLengste = new InfoLinje(new InfoTekst("Steg i lengste: ", tekstFarge), stegLengste);
            antStegLengste.initGUI();
            antSteg = new InfoLinje(new InfoTekst("Steg i denne: ", tekstFarge), steg);
            antSteg.initGUI();

            info.add(radOgKolonne);
            info.add(antUtveier);
            info.add(antStegKorteste);
            info.add(antStegLengste);
            info.add(antSteg);

            nyLabyrintKnapp = new VelgFil("Velg annen labyrint");
            nyLabyrintKnapp.initGUI();

            AvsluttKnapp avslutt = new AvsluttKnapp("Avslutt");
            avslutt.initGUI();
            
            add(info);
            add(Box.createRigidArea(new Dimension(20, 20)));
            pilboks.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(pilboks);
            add(Box.createRigidArea(new Dimension(10,10)));
            nyLabyrintKnapp.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(nyLabyrintKnapp);
            add(Box.createRigidArea(new Dimension(5,5)));
            avslutt.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(avslutt); 

        }

        static class PilBoks extends JPanel {
            PilKnapp venstre;
            PilKnapp hoyre;
            JLabel midt;

            PilBoks(PilKnapp v, PilKnapp h, JLabel m) {
                venstre = v;
                hoyre = h;
                midt = m;
            }
            
            void initGUI() {
                setBackground(bakgrunnsFargeO);
                TitledBorder rammeTittel = BorderFactory.createTitledBorder("  Bla gjennom utveier  ");
                rammeTittel.setTitleColor(tekstFarge);
                setBorder(BorderFactory.createCompoundBorder(
                    rammeTittel,
                    BorderFactory.createEmptyBorder(10, 2, 2, 2)
                ));
                GridLayout layout = new GridLayout(2,3);
                layout.setVgap(2);
                setLayout(layout);
                venstre.initGUI();
                hoyre.initGUI();
                add(venstre);
                add(midt);
                add(hoyre);
                PilKnapp kortest = new PilKnapp("Korteste", 2);
                kortest.initGUI();
                PilKnapp lengst = new PilKnapp("Lengste", 3);
                lengst.initGUI();
                add(kortest);
                add(new JLabel(""));    //Tom celle
                add(lengst);
            }

        }
    }

    static class KnappHover implements MouseListener {
        JButton knapp;
        KnappHover(JButton knapp) {
            this.knapp = knapp;
        }
        @Override 
        public void mouseEntered(MouseEvent e) {
            knapp.setBackground(Color.decode("#65bdef"));
            knapp.setForeground(Color.decode("#ffffff"));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            knapp.setBackground(Color.decode("#2c4d6f"));
            knapp.setForeground(Color.decode("#67c1f5"));
        }

        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseClicked(MouseEvent e) {}
    }


    public static class Knapp extends JButton {
        Knapp(String s) {
            super(s);
        }

        void initGUI() {
            setFocusPainted(false);
            setBackground(Color.decode("#2c4d6f"));
            setForeground(Color.decode("#67c1f5"));
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0,0,2,1, Color.black),
                BorderFactory.createEmptyBorder(2,5,2,5)
            ));
        }
    }

    public static class PilKnapp extends Knapp {
        int vei;
        PilKnapp(String s, int vei) {
            super(s);
            this.vei = vei;
        }

        class Trykk implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (vei == 0) {
                    labyrintPanel.visForrige();
                } else if (vei == 1) {
                    labyrintPanel.visNeste();
                } else if (vei == 2) {
                    labyrintPanel.visKorteste(kortesteUtvei);
                } else if (vei == 3) {
                    labyrintPanel.visLengste(lengsteUtvei);
                }
            }
        }

        void initGUI() {
            super.initGUI();
            addActionListener(new Trykk());
            addMouseListener(new KnappHover(this));
        }
    }

    public static class VelgFil extends Knapp {
        VelgFil(String s) {
            super(s);
        }

        class Trykk implements ActionListener {
            @Override
            public void actionPerformed (ActionEvent e) {
                JFileChooser velger = new JFileChooser(new File(System.getProperty("user.dir")));
                int resultat = velger.showOpenDialog(null);
                if (resultat == JFileChooser.APPROVE_OPTION) {
                    fil = velger.getSelectedFile();
                    listeBoks.listeOmrade.removeAll();
                    listeBoks.listeOmrade.revalidate();
                } else {
                }
                LabyrintPanel nyLabyrint = new LabyrintPanel(fil, sideBoks);
                nyLabyrint.initGUI();
                inneholderLabyrintPanel.removeAll();
                inneholderLabyrintPanel.add(nyLabyrint);
                inneholderLabyrintPanel.revalidate();
                vinduet.pack();
                vinduet.revalidate();
            }
        }

        void initGUI() {
            super.initGUI();
            addActionListener(new Trykk());
            addMouseListener(new KnappHover(this));
        }

    }

    public static class AvsluttKnapp extends Knapp {
        AvsluttKnapp(String s) {
            super(s);
        }

        class Trykk implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }

        void initGUI() {
            super.initGUI();
            addActionListener(new Trykk());
            addMouseListener(new KnappHover(this));
        }
    }


    public static class LabyrintPanel extends JPanel {

        static SideBoks kobletBoks;
        static GUIRute[][] guiRuter;
        static InfoTekst antallUtveier;
        static int utveiNummer;
        static LabyrintPanel denne;

        LabyrintPanel(File fil, SideBoks boks) {
            try {
                labyrint = new Labyrint(fil);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            ruter = labyrint.hentLabyrint();
            kobletBoks = boks;
            denne = this;

            if (labyrint.antRader() > 40 || labyrint.antKolonner() > 40) {
                rStr = 10;
                rStrH = 13;
            } else {
                rStr = 17;
                rStrH = 20;
            }
        }

        void initGUI() {   
            setLayout(new GridLayout(labyrint.antRader(), labyrint.antKolonner(), 0,0));
            guiRuter = new GUIRute[labyrint.antRader()][labyrint.antKolonner()];
            for (int x = 0; x < labyrint.antRader(); x++) {
                for (int y = 0; y < labyrint.antKolonner(); y++) {
                    GUIRute rute = null;
                    if (ruter[x][y] instanceof Aapning) {
                        rute = new GUIRute("X",x,y);
                        guiRuter[x][y] = rute;
                    } else if (ruter[x][y] instanceof HvitRute) {
                        rute = new GUIRute(".",x,y);
                        guiRuter[x][y] = rute;
                    } else if (ruter[x][y] instanceof SortRute) {
                        rute = new GUIRute("#",x,y);
                        guiRuter[x][y] = rute;
                    }
                    rute.initGUI();
                    add(rute);
                }
            }
            setBorder(BorderFactory.createRaisedSoftBevelBorder());
            kobletBoks.rad.setText("<html>Rader: <font color='#67c1f5'>"+labyrint.antRader()+"</font></html>");
            kobletBoks.kolonne.setText("<html>Kolonner: <font color='#67c1f5'>"+labyrint.antKolonner()+"</font></html>"); 
        }

        void ryddAlle() {
            for (Component c : denne.getComponents()) {
                if (c instanceof GUIRute) ((GUIRute)c).revert();
            }
        }

        void visNeste() {
            ryddAlle();
            utveiIndex += 1;
            if (utveiIndex >= utveier.size()) {
                utveiIndex = 0;
            }
            ArrayList<Tuppel> utvei = utveier.get(utveiIndex);
            visUtvei(utvei, false);
        }

        void visForrige() {
            ryddAlle();
            utveiIndex -= 1;
            if (utveiIndex < 0) {
                utveiIndex = utveier.size()-1;
            }
            ArrayList<Tuppel> utvei = utveier.get(utveiIndex);
            visUtvei(utvei, false);
        }

        void visUtvei(ArrayList<Tuppel> utvei, boolean start) {
            ryddAlle();
            kobletBoks.steg.setText(String.valueOf(utvei.size()));
            if (start) {
                kobletBoks.nummer.setText("Korteste");
            } else {
                kobletBoks.nummer.setText(String.valueOf(utveiIndex));
            }
            for (Tuppel t : utvei) {
                int x = t.hentx();
                int y = t.henty();
                guiRuter[y][x].settFarge(utveiFarge);
            }
        }

        void visKorteste(ArrayList<Tuppel> utvei) {
            ryddAlle();
            kobletBoks.steg.setText(String.valueOf(utvei.size()));
            visUtvei(utvei, false);
            kobletBoks.nummer.setText("Korteste");
        }

        void visLengste(ArrayList<Tuppel> utvei) {
            ryddAlle();
            kobletBoks.steg.setText(String.valueOf(utvei.size()));
            visUtvei(utvei, false);
            kobletBoks.nummer.setText("Lengste");
        }

        void visForste() {
            kortesteUtvei = labyrint.finnKortesteUtvei();
            lengsteUtvei = labyrint.finnLengsteUtvei();
            utveiIndex = labyrint.hentKortesteIndex();
            kobletBoks.stegKorteste.setText(String.valueOf(kortesteUtvei.size()));
            kobletBoks.stegLengste.setText(String.valueOf(lengsteUtvei.size()));
            kobletBoks.steg.setText(String.valueOf(kortesteUtvei.size()));
            visUtvei(kortesteUtvei, true);
        }

        static class GUIRute extends JLabel {
            String tegn;
            int x;
            int y;
            boolean markert;

            public GUIRute(String tegn, int x, int y) {
                super("");
                this.tegn = tegn;
                this.x = x;
                this.y = y;
                markert = false;
            }

            void initGUI() {
                if (tegn.equals("#")) {
                    setBackground(veggFarge);
                    setBorder(BorderFactory.createLineBorder(veggFarge));
                } else {
                    setBackground(aapenFarge);
                    setBorder(BorderFactory.createLineBorder(Color.decode("#a9abf1"))); 
                }

                setPreferredSize(new Dimension(rStr,rStr));
                setOpaque(true);
                addMouseListener(new Interact());
            }

            void revert() {
                if (!(tegn.equals("#"))) {
                    markert = false;
                    setBackground(aapenFarge);
                    setBorder(BorderFactory.createLineBorder(Color.decode("#a9abf1")));
                }
            }
            void settFarge(Color farge) {
                markert = true;
                setBackground(farge);
            }

            class Interact implements MouseListener {
                @Override 
                public void mouseEntered(MouseEvent e) {
                    if (!(tegn.equals("#"))) {
                        setBorder(BorderFactory.createLineBorder(Color.decode("#c0f0c3")));
                        setSize(new Dimension(rStrH,rStrH));
                    } 
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (!(tegn.equals("#"))) {
                        setBorder(BorderFactory.createLineBorder(Color.decode("#a9abf1")));
                        setSize(new Dimension(rStr,rStr));
                    } else {
                        setBorder(BorderFactory.createLineBorder(veggFarge));
                    }
                }
                
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (!(tegn.equals("#"))) {
                        labyrintPanel.ryddAlle();
                        utveier = labyrint.finnUtveiFra(y,x,false);
                        labyrintPanel.kobletBoks.utveier.setText(String.valueOf(utveier.size()));
                        labyrintPanel.kobletBoks.valgtRute.setText("<html>Utveier fra <font color='#67c1f5'>("+y+","+x+"): </font></html>");
                        
                        labyrintPanel.visForste();
                        listeBoks.lagListe(utveier);
                    }
                }
                public void mousePressed(MouseEvent e) {}
                public void mouseReleased(MouseEvent e) {}
            }

        }
    }
}
