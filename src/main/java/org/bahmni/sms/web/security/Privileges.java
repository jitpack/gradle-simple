package org.bahmni.sms.web.security;

import java.util.ArrayList;

public class Privileges extends ArrayList<Privilege> {
    boolean hasSendSMSPrivilege() {
        for (Privilege privilege : this) {
            if (privilege.isSendSMSPrivilege()) return true;
        }
        return false;
    }
}
