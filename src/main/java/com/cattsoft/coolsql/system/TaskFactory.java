/*
 * �������� 2006-12-25
 */
package com.cattsoft.coolsql.system;

import java.util.ArrayList;
import java.util.List;

import com.cattsoft.coolsql.pub.loadlib.LoadJar;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * Define tasks in the launch and exit.
 * @author liu_xlin 
 */
public class TaskFactory {

    /**
     * 
     * Create a task object according to className. It will return null value if className is not a task type.
     */
    private static Task createTask(String className)
            throws InstantiationException, IllegalAccessException {
        Class<?> type = null;
        try {
            type = LoadJar.getInstance().getClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            LogProxy.internalError(e);
        }

        if (Task.class.isAssignableFrom(type)) {
            return (Task) type.newInstance();
        } else
            return null;
    }

    /**
     * Get tasks executed in the launch.
     */
    public static List<Task> getLaunchTasks() {
        List<Task> list = new ArrayList<Task>();

        //launch a thread responsible for collecting garbage object.
        add("com.coolsql.system.start.LaunchGarbageCollectorTask", list);
        //Load all plugins.
        add("com.coolsql.system.start.LoadPluginTask", list);
        //load system properties from file
        add("com.coolsql.system.start.LoadSystemPropertiesTask",list);
        //load drivers and else library.
        add("com.coolsql.system.start.LoadLibResourceTask", list);
        
        //init main frame
        add("com.coolsql.system.start.MainFrameInitTask", list);
        
        //load bookmark information
        add("com.coolsql.system.start.LoadBookmarkInfoTask", list);
        
        //load system menu information
        add("com.coolsql.system.start.LoadMenuInfo", list);
        //load recent sqls  in current day
        add("com.coolsql.system.start.RecentSQLLoadTask", list);
        
        //load sql editor content which last runtime had saved into local file
        add("com.coolsql.system.start.LoadContentOfSqlEditorTask", list);
        
        //load and init plugin information
        add("com.coolsql.system.start.InitializePluginTask", list);
        return list;
    }

    /**
     * Get tasks executed in the exit.
     */
    public static List<Task> getCloseTasks() {
        List<Task> list = new ArrayList<Task>();

        //Disconnect all bookmark.
        add("com.coolsql.system.close.DisconnectDBTask", list);
        //Unload all plugins
        add("com.coolsql.system.close.UnloadPlugin", list);
        //Save bookmark information into local file
        add("com.coolsql.system.close.SaveBookmarkInfo", list);

        //Save Driver information
        add("com.coolsql.system.close.SaveDriverInfo", list);

        //Save recent sqls .
        add("com.coolsql.system.close.SaveRecentSQLsTask", list);

        //Save the content of Sql editor .
        add("com.coolsql.system.close.SaveContentOfSQLEditor", list);

        //Save favorite information
        add("com.coolsql.system.close.SaveFavoriteSQLTask", list);
        
        //Save system properties .
        add("com.coolsql.system.close.SaveSystemPropertyTask", list);
        return list;
    }

    /**
     * Add Task according to className into list collection.
     */
    private static Task add(String className, List<Task> list) {
        Task task = null;
        try {
            task = createTask(className);
        } catch (InstantiationException e) {
            LogProxy.errorMessage(PublicResource
                    .getString("system.closetask.classinstanceerror")+className);
        } catch (IllegalAccessException e) {
            LogProxy.errorMessage(PublicResource
                    .getString("system.closetask.classnoaccessprivilege")+className);
        }
        if (task != null) {
            list.add(task);
            return task;
        } else {
            LogProxy.errorMessage(PublicResource
                    .getString("system.closetask.classnotfound")+className);
            return null;
        }
    }
}
