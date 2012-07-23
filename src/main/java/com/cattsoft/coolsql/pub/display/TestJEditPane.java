package com.cattsoft.coolsql.pub.display;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;

import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Keymap;
import javax.swing.text.Segment;

import sun.awt.AppContext;

import com.cattsoft.coolsql.view.sqleditor.EditorPanel;

public class TestJEditPane extends JFrame {

	public TestJEditPane() {
		JPanel pane = (JPanel) this.getContentPane();
		// JEditTextArea t=new JEditTextArea();
		EditorPanel t = EditorPanel.createSqlEditor();
		JPopupMenu pop = new JPopupMenu();
		JMenuItem item = new JMenuItem("aa");
		pop.add(item);
		t.setRightClickPopup(pop);
		pane.add(t, BorderLayout.CENTER);
		setSize(500, 400);
		this.setVisible(true);
	}

	public static void main(String[] s) {
		 new TestJEditPane().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//		
		// SQLTokenMarker m=new SQLTokenMarker();
		// m.markTokensImpl(Token.NULL, new Segment(new
		// char[]{'\'','a','\''},0,3), 0);
		// CustomToken[] t=new CustomToken[2];
		// t[0]=new CustomToken(0,Color.red,"t1");
		// t[1]=new CustomToken(0,Color.red,"t2");
		// CustomToken[] t1=t;
		//		
		// t[0]=new CustomToken(0,Color.red,"t3");
		// System.out.println(t1[0].getKey());
	}

	public static class CustomToken {
		private String key; // �Զ���token������

		private int id; // token id

		private Color color; // color of token

		public CustomToken(int id, Color color, String key) {
			this.id = id;
			this.color = color;
			this.key = key;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

	}
}
