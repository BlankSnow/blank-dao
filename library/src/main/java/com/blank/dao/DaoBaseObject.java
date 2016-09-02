package com.blank.dao;


import com.blank.dao.annotations.BlankId;
import com.blank.dao.annotations.BlankTransient;

public abstract class DaoBaseObject {

    // region static attributes
    public static final String ID = "id";
    public static final String ORDER_BY_DEFAULT = ID;
    public static final String ORDER_TYPE_ASC = "ASC";
    public static final String ORDER_TYPE_DESC = "DESC";
    public static final String FILTER_TYPE_AND = "AND";
    public static final String FILTER_TYPE_OR = "OR";
    // endregion

    // region attributes
    // Id
    @BlankId(autoincrement = true) public Integer id;

    // Custom queries
    @BlankTransient
    public String customSelect;
    @BlankTransient public String customFrom;
    @BlankTransient public String customWhere;

    // Filter type: AND by default
    @BlankTransient public String filterType;

    // Order: ID ASC by default
    @BlankTransient public String orderBy;
    @BlankTransient public String orderType;

    // Limit query in X firts elements.
    @BlankTransient public Integer limit;
    // endregion

    // region constructor
    public DaoBaseObject() {
        orderBy = ORDER_BY_DEFAULT;
        orderType = ORDER_TYPE_ASC;
        filterType = FILTER_TYPE_AND;
    }
    // endregion

    // region methods
    public void setOrderTypeAsc() {
        orderType = ORDER_TYPE_ASC;
    }

    public void setOrderTypeDesc() {
        orderType = ORDER_TYPE_DESC;
    }

    public void setFilterTypeAnd() {
        filterType = FILTER_TYPE_AND;
    }

    public void setFilterTypeOr() {
        filterType = FILTER_TYPE_OR;
    }

    public void removeLimit() {
        this.limit = null;
    }
    // endregion

    // region comparator
    public boolean equals(Object object) {
        if (object != null && object instanceof DaoBaseObject && id != null) {
            DaoBaseObject other = (DaoBaseObject)object;
            if (other.id != null && id.intValue() == other.id.intValue()) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    // endregion
}
