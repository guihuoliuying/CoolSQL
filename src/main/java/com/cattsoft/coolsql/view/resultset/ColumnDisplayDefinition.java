package com.cattsoft.coolsql.view.resultset;

/**
 * 
 * @author liu_xlin ��ؼ�����Ϣ����
 */
public class ColumnDisplayDefinition {
	public static final int COLUMN_MAXLENGTH=4096;
	
    public ColumnDisplayDefinition(int displayWidth, String label,Object headerValue) {
        init(displayWidth, null, label, 0, null, true, 0, 0, 0, true, false,headerValue);
    }

    public ColumnDisplayDefinition(int displayWidth,
            String fullTableColumnName, String label, int sqlType,
            String sqlTypeName, boolean isNullable, int columnSize,
            int precision, int scale, boolean isSigned, boolean isCurrency,Object headerValue) {
        init(displayWidth, fullTableColumnName, label, sqlType, sqlTypeName,
                isNullable, columnSize, precision, scale, isSigned, isCurrency,headerValue);
    }

    public void setDisplayWidth(int width) {
        _displayWidth = width;
    }

    public int getDisplayWidth() {
        return _displayWidth;
    }

    public String getFullTableColumnName() {
        return _fullTableColumnName;
    }

    public String getLabel() {
        return _label;
    }

    public int getSqlType() {
        return _sqlType;
    }

    public String getSqlTypeName() {
        return _sqlTypeName;
    }

    public boolean isNullable() {
        return _isNullable;
    }

    public void setIsNullable(boolean isNullable) {
        _isNullable = isNullable;
    }

    public int getColumnSize() {
        return _columnSize;
    }

    public int getPrecision() {
        return _precision;
    }

    public int getScale() {
        return _scale;
    }

    public boolean isSigned() {
        return _isSigned;
    }

    public boolean isCurrency() {
        return _isCurrency;
    }

    /**
     * @return ���� headerValue��
     */
    public Object getHeaderValue() {
        return headerValue;
    }

    /**
     * @param headerValue
     *            Ҫ���õ� headerValue��
     */
    public void setHeaderValue(Object headerValue) {
        this.headerValue = headerValue;
    }

    private void init(int displayWidth, String fullTableColumnName,
            String label, int sqlType, String sqlTypeName, boolean isNullable,
            int columnSize, int precision, int scale, boolean isSigned,
            boolean isCurrency,Object headerValue) {
        if (label == null)
            label = "";
        _displayWidth = displayWidth;
        if (_displayWidth < label.length() + 1)
            _displayWidth = label.length() + 1;
        _fullTableColumnName = fullTableColumnName;
        _label = label;
        _sqlType = sqlType;
        _sqlTypeName = sqlTypeName;
        _isNullable = isNullable;
        _columnSize = columnSize;
        _precision = precision;
        _scale = scale;
        _isSigned = isSigned;
        _isCurrency = isCurrency;
        this.headerValue=headerValue;
    }

    /**
     * ��ָ�����������ת��Ϊ���ж���
     * 
     * @param data
     * @return
     */
    public static ColumnDisplayDefinition[] transDefinition(Object[] data) {
        if (data == null)
            return null;
        ColumnDisplayDefinition[] columns = new ColumnDisplayDefinition[data.length];
        for (int i = 0; i < data.length; i++) {
            String displayName = data[i].toString();
            columns[i] = new ColumnDisplayDefinition(displayName.length(),
                    displayName,data[i]);
        }
        return columns;
    }

    private int _displayWidth;

    private String _fullTableColumnName;

    private String _label;

    private int _sqlType;

    private String _sqlTypeName;

    private boolean _isNullable;

    private int _columnSize;

    private int _precision;

    private int _scale;

    private boolean _isSigned;

    private boolean _isCurrency;

    private Object headerValue;
}
