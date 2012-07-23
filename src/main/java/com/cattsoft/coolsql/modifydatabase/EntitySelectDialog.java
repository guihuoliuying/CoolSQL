/*
 * Created on 2007-1-15
 */
package com.cattsoft.coolsql.modifydatabase;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.cattsoft.coolsql.pub.component.BaseDialog;
import com.cattsoft.coolsql.pub.component.RenderButton;
import com.cattsoft.coolsql.pub.display.GUIUtil;
import com.cattsoft.coolsql.pub.display.Inputer;
import com.cattsoft.coolsql.pub.exception.UnifyException;
import com.cattsoft.coolsql.pub.parse.PublicResource;
import com.cattsoft.coolsql.pub.util.StringUtil;
import com.cattsoft.coolsql.sql.commonoperator.EntityPropertyOperator;
import com.cattsoft.coolsql.sql.commonoperator.Operatable;
import com.cattsoft.coolsql.sql.commonoperator.OperatorFactory;
import com.cattsoft.coolsql.sql.model.Entity;
import com.cattsoft.coolsql.view.bookmarkview.model.Identifier;
import com.cattsoft.coolsql.view.bookmarkview.model.TableNode;
import com.cattsoft.coolsql.view.bookmarkview.model.ViewNode;
import com.cattsoft.coolsql.view.log.LogProxy;

/**
 * @author liu_xlin ʵ��ѡ��Ի���
 */
public class EntitySelectDialog extends BaseDialog {
	private static final long serialVersionUID = 1L;

	private EntitySelectPanel entitySelector = null;

    /**
     * ��Ҫ������ݵĽӿ�
     */
    private Inputer inputer = null;

    public EntitySelectDialog(Inputer inputer) {
        this(GUIUtil.getMainFrame(),null, inputer);
    }
    public EntitySelectDialog(Entity entity,Inputer inputer) {
        this(GUIUtil.getMainFrame(),entity, inputer);
    }
    public EntitySelectDialog(Frame frame, Entity entity, Inputer inputer) {
        super(frame, true);
        createGUI(entity,inputer);
    }
    public EntitySelectDialog(Dialog dialog, Entity entity, Inputer inputer) {
        super(dialog, true);
        createGUI(entity,inputer);
    }
    private void createGUI(Entity entity, Inputer inputer)
    {
        setTitle(PublicResource.getSQLString("entityselect.dialog.title"));
        if (inputer == null) //inputer ����Ϊnull
            throw new IllegalArgumentException("no Inputer is transferred");
        this.inputer = inputer;

        JPanel pane = (JPanel) getContentPane();
        pane.setLayout(new BorderLayout());

        if (entity == null)
            entitySelector = new EntitySelectPanel();
        else
            entitySelector = new EntitySelectPanel(entity.getBookmark()
                    .getAliasName(), entity.getCatalog(),entity.getSchema(), entity.getName());
        entitySelector.setBorder(BorderFactory.createEtchedBorder());
        pane.add(entitySelector, BorderLayout.CENTER);

        JPanel btnPane = new JPanel();
        btnPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        RenderButton detailInfo = new RenderButton(PublicResource.getSQLString("entityselect.btn.detail"));
        detailInfo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    detailEntity();
                } catch (UnifyException e1) {
                    LogProxy.errorReport(EntitySelectDialog.this, e1);
                } catch (SQLException e1) {
                    LogProxy.SQLErrorReport(EntitySelectDialog.this, e1);
                }

            }

        });
        RenderButton okBtn = new RenderButton(PublicResource.getSQLString("entityselect.btn.ok"));
        okBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(!confirmSelect())
                	return;
                quitSelect();
            }

        });
        RenderButton quit = new RenderButton(PublicResource.getSQLString("entityselect.btn.quit"));
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                quitSelect();
            }

        });
        
        btnPane.add(okBtn);
        btnPane.add(detailInfo);       
        btnPane.add(quit);

        getRootPane().setDefaultButton(okBtn);
        pane.add(btnPane, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quitSelect();
            }
        });
        setSize(350,250);
        toCenter();
    }
    /**
     * ���ȷ����ť��,������Ӧ����ݴ��� 1��У����ǩ��ģʽ��ʵ����Ƶ���Ч�� 2��ִ����ݴ���
     * @return true:���ѡ�����Ϣ�Ϸ���false:ѡ�񲻺Ϸ�
     */
    private boolean confirmSelect() {
        if (!checkSelectedValidate())
            return false;

        if (inputer != null)
            inputer.setData(getSelectedEntity());
        return true;
    }

    /**
     * �Ե�ǰʵ��ѡ�����Ч�Խ���У��
     * 
     * @return --true����Ч false:��Ч
     */
    private boolean checkSelectedValidate() {
        if (StringUtil.trim(getSelectedBookmark()).equals("")) {
            JOptionPane.showMessageDialog(this, PublicResource
                    .getSQLString("entityselect.nobookmark"), "warning", 2);
            return false;
        }
        /**
         * 2007-12-8 ȥ��ģʽ�Ƿ�ѡ����жϡ���ģʽ��ݼ��ص��߼��Ͽ����������ѡ��null�������
         */
//        if (StringUtil.trim(getSelectedSchema()).equals("")) {
//            JOptionPane.showMessageDialog(this, PublicResource
//                    .getSQLString("entityselect.noschema"), "warning", 2);
//            return false;
//        }
        if (getSelectedEntity() == null
                || StringUtil.trim(getSelectedEntity().toString()).equals("")) {
            JOptionPane.showMessageDialog(this, PublicResource
                    .getSQLString("entityselect.nobookmark"), "warning", 2);
            return false;
        }
        return true;
    }

    /**
     * �鿴ʵ�����ϸ��Ϣ
     * 
     * @throws UnifyException
     * @throws SQLException
     */
    private void detailEntity() throws UnifyException, SQLException {
        if (!checkSelectedValidate())
            return;

        Entity entity = getSelectedEntity();
        String type = entity.getType();
        Identifier id = null;
        if (type == "VIEW") {
            id = new ViewNode(entity.getName(), entity.getBookmark(), entity);
        } else {
            id = new TableNode(entity.getName(), entity.getBookmark(), entity);
        }

        Operatable operator;
        try {
            operator = OperatorFactory
                    .getOperator(EntityPropertyOperator.class);
        } catch (ClassNotFoundException e) {
            LogProxy.errorReport(e);
            return;
        } catch (InstantiationException e) {

            LogProxy.errorReport(new UnifyException("internal error"));
            return;
        } catch (IllegalAccessException e) {
            LogProxy.errorReport(new UnifyException("internal error"));
            return;
        }
        List list=new ArrayList();
        list.add(id);
        list.add(this);
        list.add(new Boolean(true));
        operator.operate(list);
    }

    /**
     * �˳�ʵ��ѡ�����
     *  
     */
    private void quitSelect() {
        entitySelector.removeAll();
        entitySelector = null;
        removeAll();

        this.dispose();
    }

    /**
     * @return
     */
    public String getSelectedBookmark() {
        return entitySelector.getSelectedBookmark();
    }

    /**
     * @return
     */
    public String getSelectedSchema() {
        return entitySelector.getSelectedSchema();
    }

    /**
     * @return
     */
    public Entity getSelectedEntity() {
        return entitySelector.getSelectedEntity();
    }

    /**
     * @param bookmark
     * @param schema
     * @param entity
     */
    public void resetSelected(String bookmark, String catalog,String schema, String entity) {
        entitySelector.resetSelected(bookmark, catalog,schema, entity);
    }
}
