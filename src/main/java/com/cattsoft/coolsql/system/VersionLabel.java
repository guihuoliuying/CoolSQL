/*
 * �������� 2006-12-28
 */
package com.cattsoft.coolsql.system;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;

import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin
 */
public class VersionLabel extends JTextArea {
	private static final long serialVersionUID = 1L;

	public VersionLabel()
    {
        this("");
    }

    public VersionLabel(String title)
    {
        setEditable(false);
        setLineWrap(true);
        setWrapStyleWord(true);
        setText(title);
        setOpaque(false);
        append(PublicResource.getUtilString("system.version.appname")
                +" "+PublicResource.getUtilString("system.version.no")
                +"\n"
                +PublicResource.getUtilString("system.version.copyright"));
       append("\n");
       append(PublicResource.getUtilString("system.version.http"));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void updateUI()
    {
//        LookAndFeel.installBorder(this, "Label.border");
//        LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
        super.updateUI();
        LookAndFeel.installColorsAndFont(this, "Label.background", "Label.foreground", "Label.font");
    }
}
