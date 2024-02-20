
import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JProgressBar;
import java.awt.Component;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ImgDownloder extends javax.swing.JFrame {

    private boolean isPaused = false;
    private boolean isCancelled = false;

    public ImgDownloder() {
        initComponents();
        progressTable.getColumnModel().getColumn(2).setCellRenderer(new ProgressBarRenderer());
        progressTable.setVisible(false);
        setTitle("Image downloader");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        url_field = new javax.swing.JTextField();
        btn_downlaod = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_pause = new javax.swing.JButton();
        btn_resume = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        progressTable = new javax.swing.JTable();
        jProgressBar1 = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SUVAM'S GUI");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(173, 216, 230));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        url_field.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                url_fieldActionPerformed(evt);
            }
        });
        jPanel1.add(url_field, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 381, 30));

        btn_downlaod.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/download.png"))); // NOI18N
        btn_downlaod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_downlaodActionPerformed(evt);
            }
        });
        jPanel1.add(btn_downlaod, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, 35, 35));

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 210, -1, -1));

        jLabel1.setText("URL");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 84, -1));

        btn_pause.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/pause.png"))); // NOI18N
        btn_pause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pauseActionPerformed(evt);
            }
        });
        jPanel1.add(btn_pause, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 35, 35));

        btn_resume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/play.png"))); // NOI18N
        btn_resume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_resumeActionPerformed(evt);
            }
        });
        jPanel1.add(btn_resume, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 30, 35, 35));

        btn_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/dlt.png"))); // NOI18N
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });
        jPanel1.add(btn_delete, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 30, 35, 35));

        progressTable.setBackground(new java.awt.Color(204, 255, 255));
        progressTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "link", "size", "progess"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(progressTable);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 610, 235));
        jPanel1.add(jProgressBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 240, -1, 19));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 610, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
public class ProgressBarRenderer extends JProgressBar implements TableCellRenderer {

        public ProgressBarRenderer() {
            super(JProgressBar.HORIZONTAL);
            setStringPainted(true);
            setForeground(Color.green);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof Integer) {
                setValue((Integer) value);
            } else {
                setValue(0);
            }

            return this;
        }
    }
    private void btn_downlaodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_downlaodActionPerformed
        progressTable.setVisible(true);
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int option = chooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            String directoryPath = selectedDirectory.getAbsolutePath();
            String url = url_field.getText();
            try {
                List<String> imageLinks = extractImageLinks(url);
                System.out.println("Found Image URLs:");

//                String directory = "C:\\Users\\LEGION\\Desktop\\New folder";
                DefaultTableModel model = (DefaultTableModel) progressTable.getModel();
                model.setRowCount(0);

                for (String link : imageLinks) {
                    String fileName = link.substring(link.lastIndexOf("/") + 1);
                    File file = new File(directoryPath, fileName);

                    model.addRow(new Object[]{link, "0 KB", "", "0%"});

                    downloadImage(link, file, model, progressTable.getRowCount() - 1); // Pass row index
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_downlaodActionPerformed

    private void btn_pauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pauseActionPerformed
        isPaused = true;
        btn_resume.setVisible(true);
        btn_pause.setVisible(false);
    }//GEN-LAST:event_btn_pauseActionPerformed

    private void btn_resumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_resumeActionPerformed
        isPaused = false;
        btn_pause.setVisible(true);
        btn_resume.setVisible(false);
    }//GEN-LAST:event_btn_resumeActionPerformed

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        clearDownloads((DefaultTableModel) progressTable.getModel());
        url_field.setText("");
        progressTable.setVisible(false);
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void url_fieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_url_fieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_url_fieldActionPerformed

    private void clearDownloads(DefaultTableModel model) {
        model.setRowCount(0);
    }

    private void downloadImage(String link, File outputFile, DefaultTableModel model, int rowIndex) {
        try (InputStream in = new URL(link).openStream()) {
            String fileName = link.substring(link.lastIndexOf("/") + 1);
            fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
            fileName = fileName.replaceFirst("[.][^.]+$", ".png");

            outputFile = new File(outputFile.getParent(), fileName);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile));

            byte[] buffer = new byte[1024];
            int bytesRead;
            long totalBytesRead = 0;
            long fileSize = outputFile.length(); // Store the file size for progress calculation

            while ((bytesRead = in.read(buffer)) != -1) {
                if (isCancelled) {
                    out.close();
                    return;
                }

                if (isPaused) {

                    Thread.sleep(10000);
                    continue;
                }
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                double percent = (double) totalBytesRead / outputFile.length() * 100;
                model.setValueAt(totalBytesRead + " KB", rowIndex, 1);
                model.setValueAt((int) percent, rowIndex, 2); // Update progress value
                
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error downloading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error pausing download: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<String> extractImageLinks(String url) throws IOException {
        List<String> imageLinks = new ArrayList<>();
        Document document = Jsoup.connect(url).get();
        Elements images = document.select("img");
        for (Element image : images) {
            String imageUrl = image.absUrl("src");
            if (!imageUrl.isEmpty()) {
                imageLinks.add(imageUrl);
            }

        }

        return imageLinks;
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImgDownloder id = new ImgDownloder();
                id.setVisible(true);
                id.setResizable(false);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_delete;
    private javax.swing.JButton btn_downlaod;
    private javax.swing.JButton btn_pause;
    private javax.swing.JButton btn_resume;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable progressTable;
    private javax.swing.JTextField url_field;
    // End of variables declaration//GEN-END:variables
}
