/*
 *  Copyright (C) 2010-2015 JPEXS
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jpexs.decompiler.flash.gui;

import com.jpexs.decompiler.flash.gui.debugger.DebugListener;
import com.jpexs.decompiler.flash.gui.debugger.Debugger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author JPEXS
 */
public class DebugLogDialog extends AppDialog implements ActionListener {

    private final JTextArea logTextArea = new JTextArea();

    private final String ACTION_CLOSE = "CLOSE";

    private final String ACTION_CLEAR = "CLEAR";

    private final Debugger debug;

    public DebugLogDialog(Debugger debug) {
        setSize(800, 600);
        this.debug = debug;
        setTitle(translate("dialog.title"));
        logTextArea.setBackground(Color.white);
        logTextArea.setEditable(false);
        JScrollPane spane = new JScrollPane(logTextArea);
        spane.setPreferredSize(new Dimension(800, 500));

        debug.addMessageListener(new DebugListener() {

            @Override
            public void onMessage(String clientId, String msg) {
                log(translate("msg.header").replace("%clientid%", clientId) + msg);
            }

            @Override
            public void onFinish(String clientId) {

            }
        });
        Container cnt = getContentPane();
        cnt.setLayout(new BorderLayout());
        cnt.add(spane, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        JButton clearButton = new JButton(translate("button.clear"));
        clearButton.setActionCommand(ACTION_CLEAR);
        clearButton.addActionListener(this);

        JButton closeButton = new JButton(translate("button.close"));
        closeButton.setActionCommand(ACTION_CLOSE);
        closeButton.addActionListener(this);

        buttonsPanel.add(clearButton);
        buttonsPanel.add(closeButton);
        cnt.add(buttonsPanel, BorderLayout.SOUTH);
        View.setWindowIcon(this);
        View.centerScreen(this);
    }

    public void log(String msg) {
        Document d = logTextArea.getDocument();
        try {
            d.insertString(d.getLength(), msg + "\r\n", null);
        } catch (BadLocationException ex) {
            //ignore
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case ACTION_CLEAR:
                logTextArea.setText("");
                break;
            case ACTION_CLOSE:
                setVisible(false);
                break;
        }
    }
}
