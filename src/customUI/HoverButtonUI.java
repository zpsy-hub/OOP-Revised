package customUI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HoverButtonUI extends BasicButtonUI {
    private Color normalBackgroundColor;
    private Color hoverBorderColor;
    private Color pressedBackgroundColor;
    private int borderRadius;

    public HoverButtonUI(Color normalBackgroundColor, Color hoverBorderColor, Color pressedBackgroundColor, int borderRadius) {
        this.normalBackgroundColor = normalBackgroundColor;
        this.hoverBorderColor = hoverBorderColor;
        this.pressedBackgroundColor = pressedBackgroundColor;
        this.borderRadius = borderRadius;
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        AbstractButton button = (AbstractButton) c;
        button.setOpaque(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setBackground(normalBackgroundColor);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(pressedBackgroundColor);
                button.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(normalBackgroundColor);
                button.repaint();
            }
        });
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        ButtonModel model = button.getModel();
        int width = button.getWidth();
        int height = button.getHeight();

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fill background
        if (model.isPressed()) {
            g2d.setColor(pressedBackgroundColor);
            g2d.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);
        } else {
            g2d.setColor(normalBackgroundColor);
            g2d.fillRoundRect(0, 0, width, height, borderRadius, borderRadius);
            if (model.isRollover()) {
                g2d.setColor(hoverBorderColor);
                g2d.setStroke(new BasicStroke(2)); // Set border thickness
                g2d.drawRoundRect(1, 1, width - 2, height - 2, borderRadius, borderRadius);
            }
        }

        // Paint the button's children (text and icon)
        super.paint(g2d, c);
        g2d.dispose();
    }
}
