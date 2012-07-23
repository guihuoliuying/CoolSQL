package com.cattsoft.coolsql.pub.component;

import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

import sun.awt.AppContext;

public class SubTheme extends MetalLookAndFeel {
     public static MetalTheme getCurrentTheme() {
    	 AppContext context = AppContext.getAppContext();
          return (MetalTheme)context.get( "currentMetalTheme" );
     }
}
