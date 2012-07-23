package com.cattsoft.coolsql.adapters;

public class GenericAdapter extends DatabaseAdapter
{

    protected GenericAdapter(String type)
    {
        super(type);
    }

    public String getShowTableQuery(String qualifier)
    {
        return null;
    }

    public String getShowViewQuery(String qualifier)
    {
        return null;
    }

    public String getShowSequenceQuery(String qualifier)
    {
        return null;
    }

	/* (non-Javadoc)
	 * @see com.coolsql.adapters.DatabaseAdapter#getTableQuery(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String getTableQuery(String catalog, String schema, String tabName) {
		return null;
	}
}
