package Ventanas;
import java.awt.Color;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java_cup.runtime.Symbol;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
/**
 *
 * @author fenix
 */
public class Compilador extends javax.swing.JFrame{
    public static ArrayList<Object[]> t;
    public static ArrayList<Object[]> id;
    private String r = "";
    private DefaultStyledDocument doc;
    //private static DefaultTableModel dtm;
    //public static DefaultTableModel modelo;
    public static ArrayList<ErrorLexSint> listaErrores;
    static ArrayList<String> listaLexemas;
    static String DireccionPath = "";
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("BRAILLE PRINT", "bp");

    public Compilador() {
        initComponents();
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
 
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent evt) {
                salir();
            }
        });
        iconos();
        numerOf();
        inicializar();
    }
    
    private void inicializar(){
        t = new ArrayList();
        id = new ArrayList();
        listaLexemas = new ArrayList<>();
        listaErrores = new ArrayList<>();
        final StyleContext cont = StyleContext.getDefaultStyleContext();
        final AttributeSet red = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.RED);
        final AttributeSet Black = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
        final AttributeSet blue = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.blue);
        final AttributeSet green = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.green);
        final AttributeSet yellow = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.yellow);
        final AttributeSet orange = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.orange);
        final AttributeSet pink = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.pink);
        this.setTitle("BraillePrint");        
        doc = new DefaultStyledDocument() {

            public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offset, str, a);

                String text = MENU.getText(0, getLength());
                int before = findLastNonWordChar(text, offset);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offset + str.length());
                int wordL = before;
                int wordR = before;

                while (wordR <= after) {
                    if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {

                        if (text.substring(wordL, wordR).matches("(\\W)*(ciclomientras|CICLOMIENTRAS|"
                                + "ciclofor|CICLOFOR")) {
                            setCharacterAttributes(wordL, wordR - wordL, red, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(caso|CASO|select|SELECT)")) {
                            setCharacterAttributes(wordL, wordR - wordL, green, false);
                        } else if (text.substring(wordL, wordR).matches("(\\W)*(flot|FLOT|entero|ENTERO|varcar|VARCAR)")) {
                            setCharacterAttributes(wordL, wordR - wordL, orange, false);
                        } else {
                            setCharacterAttributes(wordL, wordR - wordL, Black, false);
                        }
                        wordL = wordR;
                    }
                    wordR++;
                }
            }
            
            public void remove(int offs, int len) throws BadLocationException {
                super.remove(offs, len);
                String text = MENU.getText(0, getLength());
                int before = findLastNonWordChar(text, offs);
                if (before < 0) {
                    before = 0;
                }
                int after = findFirstNonWordChar(text, offs);

                if (text.substring(before, after).matches("(\\W)*(function|private)")) {
                    setCharacterAttributes(before, after - before, red, false);
                } else if (text.substring(before, after).matches("(\\W)*(for|while)")) {
                    setCharacterAttributes(before, after - before, blue, false);
                } else if (text.substring(before, after).matches("(\\W)*(if|else)")) {
                    setCharacterAttributes(before, after - before, green, false);
                } else if (text.substring(before, after).matches("(\\W)*(int|string)")) {
                    setCharacterAttributes(before, after - before, orange, false);
                } else if (text.substring(before, after).matches("(\\W)*(>|<)")) {
                    setCharacterAttributes(before, after - before, yellow, false);
                } else if (text.substring(before, after).matches("(\\W)*(begin|end)")) {
                    setCharacterAttributes(before, after - before, pink, false);
                } else {
                    setCharacterAttributes(before, after - before, Black, false);
                }
            }
        };
    }
    
    //-----------------------------------------------Metodos Necesarios para el color------------------------------------------------------
    private int findLastNonWordChar(String text, int index) {
        while (--index >= 0) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
        }
        return index;
    }
    
    private int findFirstNonWordChar(String text, int index) {
        while (index < text.length()) {
            if (String.valueOf(text.charAt(index)).matches("\\W")) {
                break;
            }
            index++;
        }
        return index;
    }
//--------------------------------------------------------------FIN--------------------------------------------------------------------
    
    private void salir(){
        if ((JOptionPane.showOptionDialog(this, "Los cambios no guardados se perderán,¿salir?",
                "Cerrar programa", YES_NO_OPTION, QUESTION_MESSAGE,null,new Object[] { "Salir", "Cancelar" }, "opcion 2"))==0){
            System.exit(0);
        }
    }
    
    private void iconos(){
        this.setIconImage(new ImageIcon(getClass().getResource("/Iconos/icono.png")).getImage());
        
        ImageIcon icoArchivo = new ImageIcon(getClass().getResource("/Iconos/carpeta.png"));
        icoArchivo = new ImageIcon(icoArchivo.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT));
        menuArchivo.setIcon(icoArchivo);
        
        ImageIcon icoNuevo = new ImageIcon(getClass().getResource("/Iconos/file.png"));
        icoNuevo = new ImageIcon(icoNuevo.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT));
        itemNuevo.setIcon(icoNuevo);
        
        ImageIcon icoAbrir = new ImageIcon(getClass().getResource("/Iconos/folder.png"));
        icoAbrir = new ImageIcon(icoAbrir.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT));
        itemAbrir.setIcon(icoAbrir);
        
        ImageIcon icoGuardar = new ImageIcon(getClass().getResource("/Iconos/save.png"));
        icoGuardar = new ImageIcon(icoGuardar.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT));
        itemGuardar.setIcon(icoGuardar);
        
        ImageIcon icoGuardarComo = new ImageIcon(getClass().getResource("/Iconos/update.png"));
        icoGuardarComo = new ImageIcon(icoGuardarComo.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT));
        itemGuardarComo.setIcon(icoGuardarComo);
        
        ImageIcon icoSalir = new ImageIcon(getClass().getResource("/Iconos/exit-symbol.png"));
        icoSalir = new ImageIcon(icoSalir.getImage().getScaledInstance(24, 24, Image.SCALE_DEFAULT));
        itemSalir.setIcon(icoSalir);
        
    }

    private void numerOf() {
        NumeroLinea lineArea = new NumeroLinea(MENU);
        jScrollPane1.setRowHeaderView(lineArea);
        MENU.requestFocus();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lblLexico = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        MENU = new javax.swing.JTextArea();
        lineaErrores = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        Errores = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        itemNuevo = new javax.swing.JMenuItem();
        itemAbrir = new javax.swing.JMenuItem();
        itemGuardar = new javax.swing.JMenuItem();
        itemGuardarComo = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        itemSalir = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FORyCOM");
        setIconImage(getIconImage());
        setMinimumSize(new java.awt.Dimension(1100, 650));

        jPanel1.setBackground(java.awt.Color.darkGray);
        jPanel1.setMinimumSize(new java.awt.Dimension(1100, 650));
        jPanel1.setPreferredSize(new java.awt.Dimension(1100, 650));

        lblLexico.setBackground(new java.awt.Color(230, 248, 254));
        lblLexico.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        lblLexico.setForeground(new java.awt.Color(255, 255, 255));
        lblLexico.setText("Análisis léxico");
        lblLexico.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblLexico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblLexicoMouseClicked(evt);
            }
        });

        lblTitulo.setBackground(new java.awt.Color(255, 255, 255));
        lblTitulo.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(45, 66, 81));
        lblTitulo.setText(" ~ Sin título ~ ");
        lblTitulo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        lblTitulo.setOpaque(true);

        jScrollPane1.setBorder(null);

        MENU.setColumns(20);
        MENU.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        MENU.setForeground(new java.awt.Color(18, 37, 54));
        MENU.setRows(5);
        jScrollPane1.setViewportView(MENU);

        lineaErrores.setBackground(new java.awt.Color(129, 139, 149));

        javax.swing.GroupLayout lineaErroresLayout = new javax.swing.GroupLayout(lineaErrores);
        lineaErrores.setLayout(lineaErroresLayout);
        lineaErroresLayout.setHorizontalGroup(
            lineaErroresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        lineaErroresLayout.setVerticalGroup(
            lineaErroresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 9, Short.MAX_VALUE)
        );

        jPanel2.setBackground(java.awt.Color.darkGray);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 161, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(1, 1, 1));
        jPanel3.setPreferredSize(new java.awt.Dimension(129, 35));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        btnLimpiar.setFont(new java.awt.Font("Ubuntu", 1, 14)); // NOI18N
        btnLimpiar.setText("Limpiar");
        btnLimpiar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jScrollPane3.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane3.setBorder(null);

        Errores.setEditable(false);
        Errores.setBackground(new java.awt.Color(254, 254, 254));
        Errores.setColumns(20);
        Errores.setRows(5);
        Errores.setBorder(null);
        jScrollPane3.setViewportView(Errores);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
            .addComponent(lineaErrores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(174, 174, 174)
                .addComponent(lblLexico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnLimpiar)
                .addGap(227, 227, 227)
                .addComponent(lblTitulo)
                .addContainerGap(422, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLexico)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lineaErrores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(1, 1, 1));

        menuArchivo.setText("Archivo");

        itemNuevo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        itemNuevo.setText("Nuevo");
        itemNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemNuevoActionPerformed(evt);
            }
        });
        menuArchivo.add(itemNuevo);

        itemAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        itemAbrir.setText("Abrir");
        itemAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemAbrirActionPerformed(evt);
            }
        });
        menuArchivo.add(itemAbrir);

        itemGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        itemGuardar.setText("Guardar");
        itemGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarActionPerformed(evt);
            }
        });
        menuArchivo.add(itemGuardar);

        itemGuardarComo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        itemGuardarComo.setText("Guardar como...");
        itemGuardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemGuardarComoActionPerformed(evt);
            }
        });
        menuArchivo.add(itemGuardarComo);
        menuArchivo.add(jSeparator1);

        itemSalir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        itemSalir.setText("Salir");
        itemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itemSalirActionPerformed(evt);
            }
        });
        menuArchivo.add(itemSalir);

        jMenuBar1.add(menuArchivo);

        jMenu1.setText("Tablas de Simbolos");

        jMenuItem1.setText("Tabla de Tokens");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setText("Tabla de Funciones");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Tabla de Palabras Reservadas");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuItem4.setText("Tabla de Identificadores");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void itemGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarActionPerformed
        guardarArchivo();
    }//GEN-LAST:event_itemGuardarActionPerformed

    private void itemGuardarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemGuardarComoActionPerformed
       GuardarComo();
    }//GEN-LAST:event_itemGuardarComoActionPerformed

    public void GuardarComo(){
        if(MENU.getText().equals("")){
            javax.swing.JOptionPane.showMessageDialog(this, "No es posible guardar un archivo vacío","ADVERTENCIA",JOptionPane.ERROR_MESSAGE);
        }else{
            try{
                JFileChooser guardarA = new JFileChooser();
                guardarA.showSaveDialog(this);
                File guardar = guardarA.getSelectedFile();
                
                if(guardar != null){
                    DireccionPath = guardar + ".BP";
                    FileWriter  save=new FileWriter(guardar+".BP");
                    save.write(MENU.getText());
                    save.close();
                    lblTitulo.setText(guardar.getName() + ".BP");
                }
            }catch(IOException ex){
                System.out.println(ex);
            }
        }
    }
    
    private void itemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemSalirActionPerformed
        salir();
    }//GEN-LAST:event_itemSalirActionPerformed

    public void guardarArchivo(){
        if(DireccionPath.equals("")){
            if(MENU.getText().equals("")){
                javax.swing.JOptionPane.showMessageDialog(this, "No es posible guardar","ADVERTENCIA",JOptionPane.ERROR_MESSAGE);
            }else{
                try{
                    JFileChooser guardarA = new JFileChooser();
                    guardarA.showSaveDialog(this);
                    
                    File guardar = guardarA.getSelectedFile();

                    if(guardar != null){
                        DireccionPath = guardar + ".bp";
                        FileWriter save=new FileWriter(guardar+".bp");
                        save.write(MENU.getText());
                        save.close();
                        lblTitulo.setText(guardar.getName() + ".bp");
                    }
                }catch(IOException ex){
                    System.out.println(ex);
                }
            }        
        }else{
            File fichero = new File(DireccionPath);
            PrintWriter writer;
            try{
                writer = new PrintWriter(fichero);
                writer.print(MENU.getText());
                writer.close();
            }catch(FileNotFoundException e){
                System.out.println(e);
            }
        }
    }
    
    private void itemAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemAbrirActionPerformed
        abrirArchivo();
    }//GEN-LAST:event_itemAbrirActionPerformed

    public void abrirArchivo(){
        Errores.setText("");
//        LimpiarTabla();
        JFileChooser AbrirA = new JFileChooser();
        AbrirA.setFileFilter(filter);
        int opcion = AbrirA.showOpenDialog(this);
        if(opcion==JFileChooser.APPROVE_OPTION){
            MENU.setText("");
            MENU.setEditable(true);
            DireccionPath = AbrirA.getSelectedFile().getPath();
            
            File archivo = new File(DireccionPath);
            try{
                BufferedReader leer = new BufferedReader(new FileReader(archivo));
                String linea = leer.readLine();
                String total = "";
                while(linea != null){
                    total = total + linea + "\n";
                    linea = leer.readLine();
                }
                MENU.setText(total);
                lblTitulo.setText(AbrirA.getSelectedFile().getName());
                
            }catch(Exception e){
                System.out.println(e);
            }   
        }
    }
    
    private void itemNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itemNuevoActionPerformed
        if ((JOptionPane.showOptionDialog(this, "¿Guardar cambios?",
                "Se perderá el documento", YES_NO_OPTION, QUESTION_MESSAGE,null,new Object[] { "Salir", "Cancelar" }, "opcion 2"))==0){
            guardarArchivo();
        }
        Errores.setText("");
        DireccionPath = "";
        MENU.setText("");
        //LimpiarTabla();
        lblTitulo.setText(" ~ Sin título ~ ");
    }//GEN-LAST:event_itemNuevoActionPerformed
        
    /*public void LimpiarTabla(){
        //DefaultTableModel dtm = (DefaultTableModel) Tabla.getModel();
        System.out.println(dtm.getRowCount());
        int cont =dtm.getRowCount()-1;
        while(cont >= 0){
            dtm.removeRow(cont);
            cont--;
        }
    }*/
  public void probarsintaxfile() throws FileNotFoundException,IOException{    
    String ST = MENU.getText();
     Sintax s = new Sintax(new Ventanas.LexerCup(new StringReader(ST)));
    try{ 
       s.parse();
       Errores.setText("Análisis realizado correctamente");
       Errores.setForeground(Color.green);
    }catch(Exception ex){
        Symbol sym = s.getS();
        //Errores.append("\nError de sintaxis Linea"+ (sym.right + 1 )+" Columna" + (sym.left + 1)+ ", Texto: \""+ sym.value + "\"");
        //Errores.setForeground(Color.red);
    }
}
private void quicksortError(ArrayList<ErrorLexSint> E, int izq, int der) {
        ErrorLexSint pivote = E.get(izq); // tomamos primer elemento como pivote
        int i = izq; // i realiza la búsqueda de izquierda a derecha
        int j = der; // j realiza la búsqueda de derecha a izquierda
        while(i<j){ // mientras no se crucen las búsquedas
            while(E.get(i).getLinea() <= pivote.getLinea() && i<j) 
                i++; // busca elemento mayor que pivote
            while(E.get(j).getLinea() > pivote.getLinea()) 
                j--; // busca elemento menor que pivote
            if (i<j){ // si no se han cruzado                      
                ErrorLexSint e = E.get(i); // los intercambia
                E.set(i, E.get(j));
                E.set(j, e);
            }
        }
        E.set(izq, E.get(j)); // se coloca el pivote en su lugar de forma que tendremos
        E.set(j, pivote); // los menores a su izquierda y los mayores a su derecha
        if(izq < j-1)
            quicksortError(E, izq, j-1); // ordenamos subarray izquierdo
        if(j+1 <der)
            quicksortError(E, j+1, der); // ordenamos subarray derecho
    }

   public void probarlexerfile() throws FileNotFoundException, IOException{
        int cont = 1;
        Errores.setText("\t-COMPILACION-\n");
        //DefaultTableModel modelo = (DefaultTableModel) Tabla.getModel();
        File fichero = new File("fichero.txt");
        PrintWriter writer;
        try{
            writer = new PrintWriter(fichero);
            writer.print(MENU.getText());
            writer.close();
        }catch(FileNotFoundException e){
            System.out.println("Fichero no encontrado");
            return;
        }
        Reader reader = new BufferedReader(new FileReader("fichero.txt")); 
        Lexer lexer = new Lexer(reader);
        String resultado = "Línea " + cont + "\t\tSIMBOLO\n";
        while(true){
            Token token = lexer.yylex();
            System.out.println(token);
            if(token == null){
                return;
            }
            switch(token){
                case Linea:
                  cont++;
                 resultado = "Linea: "+ cont + "\n";
                break;
                case Num_Entero:
                    t.add(new Object[]{token,lexer.Lexeme}); 
                    break;
                case Comillas:
                    t.add(new Object[]{token,lexer.Lexeme}); 
                    break;
                case Texto:
                    t.add(new Object[]{token,lexer.Lexeme}); 
                    break;
                case Op_Logico:
                    t.add(new Object[]{token,lexer.Lexeme}); 
                    break;  
                case Op_Relacional:
                    t.add(new Object[]{token,lexer.Lexeme}); 
                    break;
                case Op_Booleano:
                    t.add(new Object[]{token,lexer.Lexeme}); 
                    break;
                case Op_Atribucion:
                    t.add(new Object[]{token,lexer.Lexeme}); 
                    break;
                case Asignacion:
                    t.add(new Object[]{token,lexer.Lexeme});
                    break;
                case Adicion:
                    t.add(new Object[]{"Op_Aritmetico","+"});
                    break;
                case Sustraccion:
                    t.add(new Object[]{"Op_Aritmetico","-"});
                    break;
                case Producto:
                    t.add(new Object[]{"Op_Aritmetico","*"});
                    break;
                case Cociente:
                    t.add(new Object[]{"Op_Aritmetico","/"});
                    break;
                case Parentesis_a:
                    t.add(new Object[]{"Signo_Agrupacion","("});
                    break;
                case Parentesis_c:
                    t.add(new Object[]{"Signo_Agrupacion",")"});
                    break;
                case Corchete_a:
                    t.add(new Object[]{"Signo_Agrupacion","["});
                    break;
                case Corchete_c:
                    t.add(new Object[]{"Signo_Agrupacion","]"});
                    break;
                case Llave_a:
                    t.add(new Object[]{"Signo_Agrupacion","{"});
                    break;
                case Llave_c:
                    t.add(new Object[]{"Signo_Agrupacion","}"});
                    break;
                case Punto_Coma:
                    t.add(new Object[]{"Signo_Puntuacion",";"});
                    break;
                case Coma:
                    t.add(new Object[]{"Signo_Puntuacion",lexer.Lexeme});
                    break;
                case Identificador:
                    id.add(new Object[] {lexer.Lexeme, lexer.Line()});
                    t.add(new Object[]{"Identificador",lexer.Lexeme}); 
                    break;
                case CicloMientras:
                    t.add(new Object[]{"Palabra reservada",lexer.Lexeme}); 
                    break;
                case CicloFor:
                    t.add(new Object[]{"Palabra reservada",lexer.Lexeme}); 
                    break;
                case Declarar:
                    t.add(new Object[]{"Palabra reservada",lexer.Lexeme}); 
                    break;
                case Campotrabajo:
                    t.add(new Object[]{"Palabra reservada",lexer.Lexeme}); 
                    break;
                case Si:
                    t.add(new Object[]{"Palabra reservada",lexer.Lexeme}); 
                    break;
                case Alter:
                    t.add(new Object[]{"Palabra reservada",lexer.Lexeme}); 
                    break;
                case Entero:
                    t.add(new Object[]{"Tipo de dato",lexer.Lexeme}); 
                    break;
                case Flotante:
                    t.add(new Object[]{"Tipo de dato",lexer.Lexeme}); 
                    break;
                case VarCar:
                    t.add(new Object[]{"Tipo de dato",lexer.Lexeme}); 
                    break;
                case func_Ver:case func_ContCarac:case func_Imprimir: case func_ValCad:
                    t.add(new Object[]{"Funcion",token});
                    break; 
                case ERR_NUM:
                    listaErrores.add(new ErrorLexSint(1,lexer.yytext(),
                    "Error léxico", lexer.Line(), lexer.Column(), "No es reconocido como un número válido"));
                    lineaErrores.setBackground(Color.red);
                    break;  
                case ERR_ID:
                    listaErrores.add(new ErrorLexSint(2,lexer.yytext(),
                    "Error léxico", lexer.Line(), lexer.Column(), "No es reconocido como un ID válido"));
                    lineaErrores.setBackground(Color.red);
                    break;
                default: 
                    listaErrores.add(new ErrorLexSint(3,lexer.yytext(),
                    "Error léxico", lexer.Line(), lexer.Column(), "No forma parte del lenguaje BraillePrint"));
                    lineaErrores.setBackground(Color.red);
            }
        }
    }
    
    private void lblLexicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblLexicoMouseClicked
        try {
            t.clear();
            id.clear();
            //DefaultTableModel dtm = (DefaultTableModel) Tabla.getModel();
            /*System.out.println(dtm.getRowCount());
            int cont =dtm.getRowCount()-1;
            if(cont>0){
                    while(cont >= 0){
                dtm.removeRow(cont);
                cont--;
                }
            }
            LimpiarTabla();*/
            lineaErrores.setBackground(new Color(129,139,149));
            listaErrores.clear();
            probarlexerfile();
            probarsintaxfile();
            if(!listaErrores.isEmpty()){
            quicksortError(listaErrores, 0, listaErrores.size() - 1);
            Errores.setForeground(Color.red);
                for(ErrorLexSint error: listaErrores){
                    Errores.append("\n"+error.toString());
                }
            }else{
                Errores.append("\n-COMPILACIÓN CORRECTA-");
                Errores.setForeground(Color.blue);
            } 
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_lblLexicoMouseClicked

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
         //LimpiarTabla();
        Errores.setText(null);
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        new Tablas.Tokens().setVisible(true);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        new Tablas.Funciones().setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        new Tablas.PalabrasReservadas().setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        new Tablas.TablaIdentificadores().setVisible(true);
    }//GEN-LAST:event_jMenuItem4ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Compilador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea Errores;
    private javax.swing.JTextArea MENU;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JMenuItem itemAbrir;
    private javax.swing.JMenuItem itemGuardar;
    private javax.swing.JMenuItem itemGuardarComo;
    private javax.swing.JMenuItem itemNuevo;
    private javax.swing.JMenuItem itemSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel lblLexico;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel lineaErrores;
    private javax.swing.JMenu menuArchivo;
    // End of variables declaration//GEN-END:variables
}
