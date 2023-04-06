package org.bahmni.sms.web.security;

public class Privilege {
    static final String SEND_SMS_PRIVILEGE = "Send SMS";
    private String name;

    String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    boolean isSendSMSPrivilege() {
        return name.equals(SEND_SMS_PRIVILEGE);
    }
}
