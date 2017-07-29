package com.repcomm.swbfunmungeui;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;

/**
 *
 * @author Jon/RepComm <info@jonathancrowder.com>
 */
public class MainUI extends javax.swing.JFrame {

    public static final Color DRAG_COLOR = Color.CYAN, NORMAL_COLOR = new Color(240, 240, 240, 255);
    CustomDropTarget m_Target;
    File unmungeExecutable;
    List<File> selectedFiles;
    String command;
    StringBuilder sb;
    MainUI thiz;

    public static void main(String[] args) {
        new MainUI();
    }

    public MainUI() {
        init();
    }

    private void init() {
        initLookAndFeel();
        initComponents();

        selectedFiles = new ArrayList<>();

        unmungeExecutable = new File("swbf-unmunge.exe");
        if (unmungeExecutable.exists()) {
            m_PathToUnmungeField.setText(unmungeExecutable.getAbsolutePath());
        }

        sb = new StringBuilder();

        m_Target = new CustomDropTarget();

        m_FileDropZone.setDropTarget(m_Target);
        thiz = this;
        m_ShowFileBrowserButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                File jarfile = new File("SWBFUnmungeUI.jar");
                File jardir = jarfile.getAbsoluteFile().getParentFile();
                JFileChooser fc = new JFileChooser(jardir);
                fc.setMultiSelectionEnabled(true);
                int result = fc.showOpenDialog(thiz);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File[] sel = fc.getSelectedFiles();
                    List<String> listData = new ArrayList<>();

                    for (int i=0;i<sel.length;i++) {
                        File f = sel[i];
                        if (f == null || !f.exists()) {
                            continue;
                        }
                        if (f.getName().equals("swbf-unmunge.exe")) {
                            if (!unmungeExecutable.exists()) {
                                m_PathToUnmungeField.setText(f.getAbsolutePath());
                                unmungeExecutable = f.getAbsoluteFile();
                            }
                        } else if (f.getName().endsWith(".lvl")) {
                            selectedFiles.add(f);
                            listData.add(f.getName());
                        }
                    }

                    String[] fileNameList = new String[listData.size()];
                    m_FileListDisplay.setListData(listData.toArray(fileNameList));
                }
            }
        });

        m_Run.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                requestUnmunge();
            }
        });

        m_GetCommands.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                if (unmungeExecutable == null || !unmungeExecutable.exists() || !unmungeExecutable.canExecute()) {
                    logln("Cannot access swbf-unmunge.exe, command not generated.");
                    return;
                }
                regenerateCommandString();
                String cmd = unmungeExecutable.getAbsolutePath() + " -files ";
                File f;
                int i = 0;
                for (i = 0; i < selectedFiles.size() - 1; i++) {
                    f = selectedFiles.get(i);
                    if (f != null && f.exists()) {
                        cmd += f.getAbsolutePath() + ";";
                    }
                }
                f = selectedFiles.get(i);
                if (f != null && f.exists()) {
                    cmd += f.getAbsolutePath();
                }
                cmd += command;
                logln("Copied commands to clipboard");
                StringSelection stringSelection = new StringSelection(cmd);
                Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                clpbrd.setContents(stringSelection, null);
            }
        });

        java.awt.EventQueue.invokeLater(() -> {
            setVisible(true);
        });
    }

    private void regenerateCommandString() {
        command = " -version " + m_InTargetGame.getItemAt(m_InTargetGame.getSelectedIndex())
                + " -imgfmt " + m_ImageFormat.getItemAt(m_ImageFormat.getSelectedIndex())
                + " -platform " + m_Platform.getItemAt(m_Platform.getSelectedIndex())
                + " -outversion " + m_OutTargetGame.getItemAt(m_OutTargetGame.getSelectedIndex())
                + " -verbose";
    }

    private void requestUnmunge() {
        if (unmungeExecutable == null || !unmungeExecutable.exists() || !unmungeExecutable.canExecute()) {
            logln("Cannot access swbf-unmunge.exe, cannot process files! Safely cancelling.");
            return;
        }
        Process p;
        try {
            regenerateCommandString();
            String cmd = unmungeExecutable.getAbsolutePath() + " -files ";
            File f;
            int i = 0;
            for (i = 0; i < selectedFiles.size() - 1; i++) {
                f = selectedFiles.get(i);
                if (f != null && f.exists()) {
                    cmd += f.getAbsolutePath() + ";";
                }
            }
            f = selectedFiles.get(i);
            if (f != null && f.exists()) {
                cmd += f.getAbsolutePath();
            }
            cmd += command;
            logln(cmd);
            p = Runtime.getRuntime().exec(cmd);

            p.waitFor();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                logln(line);
            }
        } catch (IOException ex) {
            logln(ex.getMessage());
            return;
        } catch (InterruptedException ex) {
            logln("Interupted whilst trying to listen to command execution.");
            return;
        }
    }

    private void log(Object append) {
        sb.append(append.toString());
        this.m_LogDisplay.setText(sb.toString());
    }

    private void logln(Object ln) {
        log("\n" + ln.toString());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        m_LogDisplay = new javax.swing.JTextArea();
        m_PathToUnmungeField = new javax.swing.JTextField();
        jSplitPane1 = new javax.swing.JSplitPane();
        m_OptInput = new javax.swing.JPanel();
        m_lblInTargetGame = new javax.swing.JLabel();
        m_InTargetGame = new javax.swing.JComboBox<>();
        m_Platform = new javax.swing.JComboBox<>();
        m_OptOutput = new javax.swing.JPanel();
        m_lblOutTargetGame = new javax.swing.JLabel();
        m_lblOutImageFormat = new javax.swing.JLabel();
        m_ImageFormat = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        m_OutTargetGame = new javax.swing.JComboBox<>();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_FileListDisplay = new javax.swing.JList<>();
        m_FileAddPane = new javax.swing.JPanel();
        m_FileDropZone = new javax.swing.JPanel();
        m_ShowFileBrowserButton = new javax.swing.JButton();
        m_GetCommands = new javax.swing.JButton();
        m_Run = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SWBF Unmunge GUI - For v0.5.0");
        setBackground(new java.awt.Color(153, 153, 153));

        m_LogDisplay.setColumns(20);
        m_LogDisplay.setRows(5);
        jScrollPane2.setViewportView(m_LogDisplay);

        m_PathToUnmungeField.setText("Couldn't find unmunge: Drag And Drop swbf-unmunge.exe onto the Drop Zone");

        jSplitPane1.setDividerLocation(250);

        m_lblInTargetGame.setText("LVL made for");

        m_InTargetGame.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "swbf", "swbf_ii" }));
        m_InTargetGame.setBorder(null);

        m_Platform.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "pc", "ps2", "xbox" }));

        javax.swing.GroupLayout m_OptInputLayout = new javax.swing.GroupLayout(m_OptInput);
        m_OptInput.setLayout(m_OptInputLayout);
        m_OptInputLayout.setHorizontalGroup(
            m_OptInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(m_OptInputLayout.createSequentialGroup()
                .addComponent(m_lblInTargetGame)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(m_InTargetGame, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_Platform, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        m_OptInputLayout.setVerticalGroup(
            m_OptInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(m_OptInputLayout.createSequentialGroup()
                .addGroup(m_OptInputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_lblInTargetGame)
                    .addComponent(m_InTargetGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_Platform, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 41, Short.MAX_VALUE))
        );

        jSplitPane1.setLeftComponent(m_OptInput);

        m_lblOutTargetGame.setText("Decompile For");

        m_lblOutImageFormat.setText("Image File Output");

        m_ImageFormat.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "tga", "png", "dds" }));

        m_OutTargetGame.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "swbf", "swbf_ii" }));
        m_OutTargetGame.setBorder(null);

        javax.swing.GroupLayout m_OptOutputLayout = new javax.swing.GroupLayout(m_OptOutput);
        m_OptOutput.setLayout(m_OptOutputLayout);
        m_OptOutputLayout.setHorizontalGroup(
            m_OptOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(m_OptOutputLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(m_OptOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(m_lblOutImageFormat, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                    .addComponent(m_lblOutTargetGame, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(m_OptOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(m_OutTargetGame, 0, 142, Short.MAX_VALUE)
                    .addComponent(m_ImageFormat, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        m_OptOutputLayout.setVerticalGroup(
            m_OptOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(m_OptOutputLayout.createSequentialGroup()
                .addGroup(m_OptOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(m_OutTargetGame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(m_lblOutTargetGame))
                .addGroup(m_OptOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(m_OptOutputLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3))
                    .addGroup(m_OptOutputLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(m_OptOutputLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(m_ImageFormat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(m_lblOutImageFormat))))
                .addGap(0, 22, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(m_OptOutput);

        jSplitPane2.setDividerLocation(220);

        m_FileListDisplay.setBorder(javax.swing.BorderFactory.createTitledBorder("Selected File List"));
        jScrollPane1.setViewportView(m_FileListDisplay);

        jSplitPane2.setLeftComponent(jScrollPane1);

        m_FileDropZone.setBorder(javax.swing.BorderFactory.createTitledBorder("Drag 'n' Drop Zone"));

        javax.swing.GroupLayout m_FileDropZoneLayout = new javax.swing.GroupLayout(m_FileDropZone);
        m_FileDropZone.setLayout(m_FileDropZoneLayout);
        m_FileDropZoneLayout.setHorizontalGroup(
            m_FileDropZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        m_FileDropZoneLayout.setVerticalGroup(
            m_FileDropZoneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 53, Short.MAX_VALUE)
        );

        m_ShowFileBrowserButton.setText("Show File Browser");

        javax.swing.GroupLayout m_FileAddPaneLayout = new javax.swing.GroupLayout(m_FileAddPane);
        m_FileAddPane.setLayout(m_FileAddPaneLayout);
        m_FileAddPaneLayout.setHorizontalGroup(
            m_FileAddPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(m_FileDropZone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(m_ShowFileBrowserButton, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)
        );
        m_FileAddPaneLayout.setVerticalGroup(
            m_FileAddPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(m_FileAddPaneLayout.createSequentialGroup()
                .addComponent(m_FileDropZone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_ShowFileBrowserButton)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(m_FileAddPane);

        m_GetCommands.setText("Get Commands");

        m_Run.setText("Run");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSplitPane1)
                    .addComponent(jScrollPane2)
                    .addComponent(m_PathToUnmungeField)
                    .addComponent(m_GetCommands, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(m_Run, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_Run)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_GetCommands)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(m_PathToUnmungeField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void initLookAndFeel() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    private class CustomDropTarget extends DropTarget {

        @Override
        public synchronized void dragEnter(DropTargetDragEvent evt) {
            m_FileDropZone.setBackground(DRAG_COLOR);
        }

        @Override
        public synchronized void dragExit(DropTargetEvent evt) {
            m_FileDropZone.setBackground(NORMAL_COLOR);
        }

        @Override
        public synchronized void drop(DropTargetDropEvent evt) {
            try {
                m_FileDropZone.setBackground(NORMAL_COLOR);
                evt.acceptDrop(DnDConstants.ACTION_COPY);

                List<File> droppedFiles = (List<File>) evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                List<String> listData = new ArrayList<>();

                for (Iterator<File> it = droppedFiles.iterator(); it.hasNext();) {
                    File f = it.next();
                    if (f.getName().equals("swbf-unmunge.exe")) {
                        if (!unmungeExecutable.exists()) {
                            m_PathToUnmungeField.setText(f.getAbsolutePath());
                            unmungeExecutable = f.getAbsoluteFile();
                        }

                    } else if (f.getName().endsWith(".lvl")) {
                        selectedFiles.add(f);
                        listData.add(f.getName());
                    }
                }

                String[] fileNameList = new String[listData.size()];
                m_FileListDisplay.setListData(listData.toArray(fileNameList));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabel3;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JSplitPane jSplitPane1;
    public javax.swing.JSplitPane jSplitPane2;
    public javax.swing.JPanel m_FileAddPane;
    public javax.swing.JPanel m_FileDropZone;
    public javax.swing.JList<String> m_FileListDisplay;
    public javax.swing.JButton m_GetCommands;
    public javax.swing.JComboBox<String> m_ImageFormat;
    public javax.swing.JComboBox<String> m_InTargetGame;
    public javax.swing.JTextArea m_LogDisplay;
    public javax.swing.JPanel m_OptInput;
    public javax.swing.JPanel m_OptOutput;
    public javax.swing.JComboBox<String> m_OutTargetGame;
    public javax.swing.JTextField m_PathToUnmungeField;
    public javax.swing.JComboBox<String> m_Platform;
    public javax.swing.JButton m_Run;
    public javax.swing.JButton m_ShowFileBrowserButton;
    public javax.swing.JLabel m_lblInTargetGame;
    public javax.swing.JLabel m_lblOutImageFormat;
    public javax.swing.JLabel m_lblOutTargetGame;
    // End of variables declaration//GEN-END:variables
}
