/*
 * �������� 2006-7-8
 *
 */
package com.cattsoft.coolsql.pub.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.util.Arrays;

import javax.swing.Icon;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.OceanTheme;

import com.cattsoft.coolsql.pub.parse.PublicResource;

/**
 * @author liu_xlin �Զ�������
 */
public class MyMetalTheme extends OceanTheme {
	private static final ColorUIResource PRIMARY1 =new ColorUIResource(
			0x6382BF);

	private static final ColorUIResource PRIMARY3 = new ColorUIResource(
			0xB8CFE5);

	private static final ColorUIResource SECONDARY1 = new ColorUIResource(
			0x7A8A99);

	private static final ColorUIResource SECONDARY2 = new ColorUIResource(
			0xB8CFE5);

	private static final ColorUIResource SECONDARY3 = new ColorUIResource(
			0xEEEEEE);

	private static final ColorUIResource CONTROL_TEXT_COLOR = new ColorUIResource(
			0x333333);

	private static final ColorUIResource INACTIVE_CONTROL_TEXT_COLOR = new ColorUIResource(
			0x999999);

	private static final ColorUIResource MENU_DISABLED_FOREGROUND = new ColorUIResource(
			0x999999);

	private static final ColorUIResource OCEAN_BLACK = new ColorUIResource(
			0x333333);
	
	public MyMetalTheme()
	{
	    this(null);
	}
	public MyMetalTheme(Color themeColor)
	{
	    super();
	    if(themeColor!=null)
	        DisplayPanel.setThemeColor(themeColor);
	}
	/**
	 * Ĭ��������ɫ
	 */
//	private ColorUIResource primary1 = new ColorUIResource(102, 193, 122);

	private ColorUIResource primary2 = new ColorUIResource(DisplayPanel.getThemeColor());

	protected ColorUIResource getPrimary2() {
		return primary2;
	}

	/**
	 * @param primary2
	 *            Ҫ���õ� primary2��
	 */
	public void setPrimary2(Color primary2Color) {
		this.primary2 = new ColorUIResource(primary2Color);
	}


    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        Object focusBorder = new UIDefaults.ProxyLazyValue(
                      "javax.swing.plaf.BorderUIResource$LineBorderUIResource",
                      new Object[] {getPrimary1()});
        // .30 0 DDE8F3 white secondary2
        java.util.List buttonGradient = Arrays.asList(
                 new Object[] {new Float(.3f), new Float(0f),
                		 getPrimary2(), getWhite(), getSecondary2() });

        Color cccccc = new ColorUIResource(0xCCCCCC);
        Color dadada = new ColorUIResource(0xDADADA);
        Color c8ddf2 = new ColorUIResource(0xC8DDF2);

        java.util.List sliderGradient = Arrays.asList(new Object[] {
            new Float(.3f), new Float(.2f),
            c8ddf2, getWhite(), new ColorUIResource(getPrimary2()) });
        
		Font f = new Font(PublicResource.getString("default.font.family"),
				Integer
						.parseInt(PublicResource
								.getString("default.font.style")),
				Integer.parseInt(PublicResource.getString("default.font.size")));
		Font textPaneFont=new Font(PublicResource.getString("default.font.family"),Integer
				.parseInt(PublicResource
						.getString("default.font.style")),10);
        Object[] defaults = new Object[] {
//        	"ScrollPane.background",DisplayPanel.getThemeColor(),
        	"ScrollBar.darkShadow",DisplayPanel.getThemeColor(),
            "Button.gradient", buttonGradient,
        	"Button.background",DisplayPanel.getThemeColor(),
            "Button.rollover", Boolean.TRUE,
            "Button.toolBarBorderBackground", INACTIVE_CONTROL_TEXT_COLOR, 
            "Button.disabledToolBarBorderBackground", cccccc,
            "Button.rolloverIconType", "ocean",

            "CheckBox.rollover", Boolean.TRUE,
            "CheckBox.gradient", buttonGradient,

            "CheckBoxMenuItem.gradient", buttonGradient,


            "Label.disabledForeground", getInactiveControlTextColor(),
            
            "Menu.opaque", Boolean.FALSE,

            "MenuBar.gradient", Arrays.asList(new Object[] {
                     new Float(1f), new Float(0f),
                     getWhite(), dadada, 
                     new ColorUIResource(dadada) }),
            "MenuBar.borderColor", cccccc,

            "InternalFrame.activeTitleGradient", buttonGradient,

            "List.focusCellHighlightBorder", focusBorder,

//            "MenuBarUI", "javax.swing.plaf.metal.MetalMenuBarUI",

            "RadioButton.gradient", buttonGradient,
            "RadioButton.rollover", Boolean.TRUE,

            "RadioButtonMenuItem.gradient", buttonGradient,

            "ScrollBar.gradient", buttonGradient,

            "Slider.altTrackColor", new ColorUIResource(0xD2E2EF),
            "Slider.gradient", sliderGradient,
            "Slider.focusGradient", sliderGradient,

            "SplitPane.oneTouchButtonsOpaque", Boolean.FALSE,
            "SplitPane.dividerFocusColor", c8ddf2,

            "TabbedPane.borderHightlightColor", getPrimary1(),
            "TabbedPane.contentAreaColor", c8ddf2,
            "TabbedPane.contentBorderInsets", new Insets(4, 2, 3, 3),
            "TabbedPane.selected", c8ddf2,
            "TabbedPane.tabAreaBackground", dadada,
            "TabbedPane.tabAreaInsets", new Insets(2, 2, 0, 6),
            "TabbedPane.unselectedBackground", SECONDARY3,

            "Table.focusCellHighlightBorder", focusBorder,
            "Table.gridColor", SECONDARY1,

            "ToggleButton.gradient", buttonGradient,

            "ToolBar.borderColor", cccccc,
            "ToolBar.isRollover", Boolean.TRUE,

            "Tree.selectionBorderColor", getPrimary1(),
			
            "ProgressBar.font",f,
			"TextField.font", f,
    		"Label.font", f,
    		"ComboBox.font", f,
    		"MenuBar.font", f,
    		"Menu.font", f,
    		 "ToolTip.font", f,
    		 "MenuItem.font", f,
    		 "Button.font", f,
    		 "TabbedPane.font", f,
    		 "List.font", f,
    		 "Tree.font", f,
    		 "PopupMenu.font", f,
    		 "CheckBoxMenuItem.font", f,
    		 "RadioButtonMenuItem.font",f,
    		 "TextPane.font", textPaneFont,
    		 "CheckBox.font", f,
    		 "ToolTip.font", f,
    		 "TitledBorder.font", f,
    		 "RadioButton.font",f,
    		 
			 "MenuItem.selectionBackground", DisplayPanel.getSelectionColor(),
    		 "TabbedPane.selected",  DisplayPanel.getThemeColor(),
    		 "TabbedPane.focus", DisplayPanel.getThemeColor(),
    		 "Tree.hash",  DisplayPanel.getThemeColor()
			 
        };
        table.putDefaults(defaults);
    }
    /**
     * Overriden to enable picking up the system fonts, if applicable.
     */
//    boolean isSystemTheme() {
//        return true;
//    }

    /**
     * Return the name of this theme, "Ocean".
     *
     * @return "Ocean"
     */
    public String getName() {
        return "Ocean";
    }

    /**
     * Return the color that the Metal Look and Feel should use
     * as "Primary 1". The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Primary 1" color.
     */
    protected ColorUIResource getPrimary1() {
        return PRIMARY1;
    } 

    /**
     * Return the color that the Metal Look and Feel should use
     * as "Primary 2". The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Primary 2" color.
     */
//    protected ColorUIResource getPrimary2() {
//        return PRIMARY2;
//    }

    /**
     * Return the color that the Metal Look and Feel should use
     * as "Primary 3". The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Primary 3" color.
     */
    protected ColorUIResource getPrimary3() {
        return PRIMARY3;
    }

    /**
     * Return the color that the Metal Look and Feel should use
     * as "Secondary 1". The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Secondary 1" color.
     */
    protected ColorUIResource getSecondary1() {
        return SECONDARY1;
    }

    /**
     * Return the color that the Metal Look and Feel should use
     * as "Secondary 2". The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Secondary 2" color.
     */
    protected ColorUIResource getSecondary2() {
        return SECONDARY2;
    }

    /**
     * Return the color that the Metal Look and Feel should use
     * as "Secondary 3". The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Secondary 3" color.
     */
    protected ColorUIResource getSecondary3() {
        return SECONDARY3;
    }

    /**
     * Return the color that the Metal Look and Feel should use
     * as "Black". The Look and Feel will use this color
     * in painting as it sees fit. This color does not necessarily
     * synch up with the typical concept of black, nor is
     * it necessarily used for all black items.
     *
     * @return the "Black" color.
     */
    protected ColorUIResource getBlack() {
        return OCEAN_BLACK;
    }

    /**
     * Return the color that the Metal Look and Feel should use
     * for the desktop background. The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Desktop" color.
     */
    public ColorUIResource getDesktopColor() {
        return new ColorUIResource( 255, 255, 255 );
    }

    /**
     * Return the color that the Metal Look and Feel should use as the default
     * color for inactive controls. The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Inactive Control Text" color.
     */
    public ColorUIResource getInactiveControlTextColor() {
        return INACTIVE_CONTROL_TEXT_COLOR;
    }

    /**
     * Return the color that the Metal Look and Feel should use as the default
     * color for controls. The Look and Feel will use this color
     * in painting as it sees fit.
     *
     * @return the "Control Text" color.
     */
    public ColorUIResource getControlTextColor() {
        return CONTROL_TEXT_COLOR;
    }

    /**
     * Return the color that the Metal Look and Feel should use as the
     * foreground color for disabled menu items. The Look and Feel will use
     * this color in painting as it sees fit.
     *
     * @return the "Menu Disabled Foreground" color.
     */
    public ColorUIResource getMenuDisabledForeground() {
        return MENU_DISABLED_FOREGROUND;
    }

    private Object getIconResource(String iconID) {
        return LookAndFeel.makeIcon(getClass(), iconID);
    }

    // makes use of getIconResource() to fetch an icon and then hastens it
    // - calls createValue() on it and returns the actual icon
    private Icon getHastenedIcon(String iconID, UIDefaults table) {
        Object res = getIconResource(iconID);
        return (Icon)((UIDefaults.LazyValue)res).createValue(table);
    }
}
