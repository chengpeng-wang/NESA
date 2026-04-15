package org.jivesoftware.smackx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.packet.DataForm;

public class Form {
    public static final String TYPE_CANCEL = "cancel";
    public static final String TYPE_FORM = "form";
    public static final String TYPE_RESULT = "result";
    public static final String TYPE_SUBMIT = "submit";
    private DataForm dataForm;

    public static Form getFormFrom(Packet packet) {
        PacketExtension packetExtension = packet.getExtension(GroupChatInvitation.ELEMENT_NAME, "jabber:x:data");
        if (packetExtension != null) {
            DataForm dataForm = (DataForm) packetExtension;
            if (dataForm.getReportedData() == null) {
                return new Form(dataForm);
            }
        }
        return null;
    }

    private Form(DataForm dataForm) {
        this.dataForm = dataForm;
    }

    public Form(String type) {
        this.dataForm = new DataForm(type);
    }

    public void addField(FormField field) {
        this.dataForm.addField(field);
    }

    public void setAnswer(String variable, String value) {
        FormField field = getField(variable);
        if (field == null) {
            throw new IllegalArgumentException("Field not found for the specified variable name.");
        } else if (FormField.TYPE_TEXT_MULTI.equals(field.getType()) || FormField.TYPE_TEXT_PRIVATE.equals(field.getType()) || FormField.TYPE_TEXT_SINGLE.equals(field.getType()) || FormField.TYPE_JID_SINGLE.equals(field.getType()) || FormField.TYPE_HIDDEN.equals(field.getType())) {
            setAnswer(field, (Object) value);
        } else {
            throw new IllegalArgumentException("This field is not of type String.");
        }
    }

    public void setAnswer(String variable, int value) {
        FormField field = getField(variable);
        if (field == null) {
            throw new IllegalArgumentException("Field not found for the specified variable name.");
        } else if (FormField.TYPE_TEXT_MULTI.equals(field.getType()) || FormField.TYPE_TEXT_PRIVATE.equals(field.getType()) || FormField.TYPE_TEXT_SINGLE.equals(field.getType())) {
            setAnswer(field, new Integer(value));
        } else {
            throw new IllegalArgumentException("This field is not of type int.");
        }
    }

    public void setAnswer(String variable, long value) {
        FormField field = getField(variable);
        if (field == null) {
            throw new IllegalArgumentException("Field not found for the specified variable name.");
        } else if (FormField.TYPE_TEXT_MULTI.equals(field.getType()) || FormField.TYPE_TEXT_PRIVATE.equals(field.getType()) || FormField.TYPE_TEXT_SINGLE.equals(field.getType())) {
            setAnswer(field, new Long(value));
        } else {
            throw new IllegalArgumentException("This field is not of type long.");
        }
    }

    public void setAnswer(String variable, float value) {
        FormField field = getField(variable);
        if (field == null) {
            throw new IllegalArgumentException("Field not found for the specified variable name.");
        } else if (FormField.TYPE_TEXT_MULTI.equals(field.getType()) || FormField.TYPE_TEXT_PRIVATE.equals(field.getType()) || FormField.TYPE_TEXT_SINGLE.equals(field.getType())) {
            setAnswer(field, new Float(value));
        } else {
            throw new IllegalArgumentException("This field is not of type float.");
        }
    }

    public void setAnswer(String variable, double value) {
        FormField field = getField(variable);
        if (field == null) {
            throw new IllegalArgumentException("Field not found for the specified variable name.");
        } else if (FormField.TYPE_TEXT_MULTI.equals(field.getType()) || FormField.TYPE_TEXT_PRIVATE.equals(field.getType()) || FormField.TYPE_TEXT_SINGLE.equals(field.getType())) {
            setAnswer(field, new Double(value));
        } else {
            throw new IllegalArgumentException("This field is not of type double.");
        }
    }

    public void setAnswer(String variable, boolean value) {
        FormField field = getField(variable);
        if (field == null) {
            throw new IllegalArgumentException("Field not found for the specified variable name.");
        } else if (FormField.TYPE_BOOLEAN.equals(field.getType())) {
            setAnswer(field, value ? "1" : "0");
        } else {
            throw new IllegalArgumentException("This field is not of type boolean.");
        }
    }

    private void setAnswer(FormField field, Object value) {
        if (isSubmitType()) {
            field.resetValues();
            field.addValue(value.toString());
            return;
        }
        throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }

    public void setAnswer(String variable, List values) {
        if (isSubmitType()) {
            FormField field = getField(variable);
            if (field == null) {
                throw new IllegalArgumentException("Couldn't find a field for the specified variable.");
            } else if (FormField.TYPE_JID_MULTI.equals(field.getType()) || FormField.TYPE_LIST_MULTI.equals(field.getType()) || FormField.TYPE_LIST_SINGLE.equals(field.getType()) || FormField.TYPE_HIDDEN.equals(field.getType())) {
                field.resetValues();
                field.addValues(values);
                return;
            } else {
                throw new IllegalArgumentException("This field only accept list of values.");
            }
        }
        throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }

    public void setDefaultAnswer(String variable) {
        if (isSubmitType()) {
            FormField field = getField(variable);
            if (field != null) {
                field.resetValues();
                Iterator<String> it = field.getValues();
                while (it.hasNext()) {
                    field.addValue((String) it.next());
                }
                return;
            }
            throw new IllegalArgumentException("Couldn't find a field for the specified variable.");
        }
        throw new IllegalStateException("Cannot set an answer if the form is not of type \"submit\"");
    }

    public Iterator<FormField> getFields() {
        return this.dataForm.getFields();
    }

    public FormField getField(String variable) {
        if (variable == null || variable.equals("")) {
            throw new IllegalArgumentException("Variable must not be null or blank.");
        }
        Iterator<FormField> it = getFields();
        while (it.hasNext()) {
            FormField field = (FormField) it.next();
            if (variable.equals(field.getVariable())) {
                return field;
            }
        }
        return null;
    }

    public String getInstructions() {
        StringBuilder sb = new StringBuilder();
        Iterator it = this.dataForm.getInstructions();
        while (it.hasNext()) {
            sb.append((String) it.next());
            if (it.hasNext()) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public String getTitle() {
        return this.dataForm.getTitle();
    }

    public String getType() {
        return this.dataForm.getType();
    }

    public void setInstructions(String instructions) {
        ArrayList<String> instructionsList = new ArrayList();
        StringTokenizer st = new StringTokenizer(instructions, "\n");
        while (st.hasMoreTokens()) {
            instructionsList.add(st.nextToken());
        }
        this.dataForm.setInstructions(instructionsList);
    }

    public void setTitle(String title) {
        this.dataForm.setTitle(title);
    }

    public DataForm getDataFormToSend() {
        if (!isSubmitType()) {
            return this.dataForm;
        }
        DataForm dataForm = new DataForm(getType());
        Iterator<FormField> it = getFields();
        while (it.hasNext()) {
            FormField field = (FormField) it.next();
            if (field.getValues().hasNext()) {
                dataForm.addField(field);
            }
        }
        return dataForm;
    }

    private boolean isFormType() {
        return TYPE_FORM.equals(this.dataForm.getType());
    }

    private boolean isSubmitType() {
        return TYPE_SUBMIT.equals(this.dataForm.getType());
    }

    public Form createAnswerForm() {
        if (isFormType()) {
            Form form = new Form(TYPE_SUBMIT);
            Iterator<FormField> fields = getFields();
            while (fields.hasNext()) {
                FormField field = (FormField) fields.next();
                if (field.getVariable() != null) {
                    FormField newField = new FormField(field.getVariable());
                    newField.setType(field.getType());
                    form.addField(newField);
                    if (FormField.TYPE_HIDDEN.equals(field.getType())) {
                        List values = new ArrayList();
                        Iterator<String> it = field.getValues();
                        while (it.hasNext()) {
                            values.add(it.next());
                        }
                        form.setAnswer(field.getVariable(), values);
                    }
                }
            }
            return form;
        }
        throw new IllegalStateException("Only forms of type \"form\" could be answered");
    }
}
