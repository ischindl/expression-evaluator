package com.creativewidgetworks.expressionparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.creativewidgetworks.expressionparser.enums.ValueType;

public class Value {

    private String name;
    private ValueType type = ValueType.UNDEFINED;
    private Object valueObj = null;
    private String valueStr = "";
    private BigDecimal valueNum = BigDecimal.ZERO;
    private Date valueDate = null;
    private List<Value> array = null;

    public Value() { /**/ }

    public Value(String name) {
        this.name = name;
    }

    public Value(Value var) {
        set(var);
    }

    public final Value clear() {
        this.type = ValueType.UNDEFINED;
        this.valueStr = "";
        this.valueNum = BigDecimal.ZERO;
        this.valueDate = null;
        this.valueObj = null;
        if (array != null) {
            array.clear();
            array = null;
        }
        return this;
    }

    public final void set(Value var) {
        if (var != null) {
            this.name = var.name == null ? null : new String(var.name);
            this.type = var.type;
            this.valueObj = var.valueObj == null ? null : var.valueStr;  // WARNING: shallow copy
            this.valueStr = var.valueStr == null ? null : new String(var.valueStr);
            this.valueNum = var.valueNum == null ? null : new BigDecimal(var.valueNum.toPlainString());
            this.valueDate = var.valueDate == null ? null : new Date(var.valueDate.getTime());
            if (var.array != null) {
                this.array = new ArrayList<Value>(var.array.size());
                for (Value v : var.array) {
                    this.array.add(new Value(v));
                }
            } else {
                this.array = null;
            }
        }
    }

   /*----------------------------------------------------------------------------*/

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /*----------------------------------------------------------------------------*/

    public ValueType getType() {
        return type;
    }

    public void setType(ValueType type) {
        this.type = type;
    }

    /*----------------------------------------------------------------------------*/
    
    public void addValueToArray(Value value) {
        if (array == null) {
            array = new ArrayList<Value>();
        }
        array.add(value);
    }
    
    public List<Value> getArray() {
        return array;
    }

    public final void unsetArray() {
        if (array != null) {
            array.clear();
            array = null;
        }
    }    
    
    /*----------------------------------------------------------------------------*/

    public Boolean asBoolean() {
        return Boolean.valueOf(BigDecimal.ONE.equals(valueNum));
    }

    public Date asDate() {
        return valueDate;
    }    
    
    public BigDecimal asNumber() {
        return valueNum;
    }

    public Object asObject() {
        return valueObj;
    }
    
    public String asString() {
        return valueStr;
    }
    
    public Value setValue(BigDecimal value) {
        this.valueObj = value;
        this.valueNum = value;
        this.valueStr = (value == null) ? null : value.toPlainString();
        this.valueDate = (value == null) ? null : new Date(value.longValue());
        setType(ValueType.NUMBER);
        return this;
    }    
    
    public Value setValue(Boolean value) {
        this.valueObj = value;
        boolean val = (value == null) ? false : value.booleanValue();
        this.valueStr = val ? "1" : "0";
        this.valueNum = val ? BigDecimal.ONE : BigDecimal.ZERO;
        this.valueDate = null;
        setType(ValueType.BOOLEAN);        
        return this;
    }

    public Value setValue(Date value) {
        this.valueObj = value;
        this.valueDate = value;
        if (valueDate != null) {
            this.valueNum = new BigDecimal(valueDate.getTime());
            this.valueStr = valueDate.toString();
        } else {
            this.valueNum = null;
            this.valueStr = null;
        }
        setType(ValueType.DATE);
        return this;
    }    
    
    public Value setValue(Object value) {
        this.valueObj = value;
        this.valueStr = value == null ? null : value.toString();
        this.valueNum =  (value != null && value instanceof BigDecimal) ? (BigDecimal)value : null;
        this.valueDate = (value != null && value instanceof Date) ? (Date)value : null;
        setType(ValueType.OBJECT);
        return this;
    }
    
    public Value setValue(String value) {
        this.valueObj = value;
        this.valueStr = value;
        this.valueNum = BigDecimal.ZERO;
        this.valueDate = null;
        setType(ValueType.STRING);
        return this;
    }

    /*----------------------------------------------------------------------------*/    
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name=").append(name == null ? "n/a" : name).append(" type=");
        switch (type) {
            case BOOLEAN :
                sb.append("BOOLEAN (");
                sb.append(asBoolean().booleanValue() ? "TRUE" : "FALSE");
                sb.append(")");
                break;
            case NUMBER :
            case OBJECT:
            case STRING :
            case DATE :
                sb.append(type.name());
                break;
            default :
                sb.append("UNDEFINED");
                break;
        }
        sb.append(" str=").append(valueStr);
        sb.append(" num=").append(valueNum);
        return sb.toString();
    }
    
}
