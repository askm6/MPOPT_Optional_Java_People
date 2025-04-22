package view;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.commons.io.FilenameUtils;

/**
 * @author Francesc Perez
 * @version 1.1.0
 */
public class DropPhotoListener implements DropTargetListener {

    JDialog jDialog;
    JLabel label;
    String dragText = "<html><center>PHOTO</center></br><br><center> <i>Supported format: PNG.</i></center></br><br><center><i>Max. size 64KB</i></center></html>";

    public DropPhotoListener(JLabel label, JDialog jDialog) {
        this.label = label;
        this.jDialog = jDialog;
    }

    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        label.setText(dragText);
    }

    @Override
    public void dragOver(DropTargetDragEvent dtde) {
    }

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    @Override
    public void dragExit(DropTargetEvent dte) {
    }

    @Override
    public void drop(DropTargetDropEvent ev) {
        label.setIcon(null);
        ev.acceptDrop(DnDConstants.ACTION_COPY);
        Transferable t = ev.getTransferable();
        DataFlavor df[] = t.getTransferDataFlavors();
        for (DataFlavor f : df) {
            try {
                if (f.isFlavorJavaFileListType()) {
                    List<File> files = (List<File>) t.getTransferData(f);
                    for (File file : files) {
                        String extension = FilenameUtils.getExtension(file.getName());
                        if (file.length() < 64000 && extension.equals("png")) {
                            displayImage(file.getPath());
                        } else {
                            throw new Exception("Photo must be PNG format and less than 64KB.");
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(jDialog, ex.getMessage(), jDialog.getTitle(), JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void displayImage(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        img = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_DEFAULT);
        icon = new ImageIcon(img);
        label.setIcon(icon);
    }

}
