package org.fenixedu.ulisboa.specifications.dto.student.mobility;

import org.fenixedu.bennu.IBean;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.domain.student.mobility.MobilityProgramType;

public class MobilityProgramTypeBean implements IBean {

    private String code;
    private String name;
    private boolean active;
    
    
    public MobilityProgramTypeBean() {
        this.name = "";
        this.active = true;
    }
    
    public MobilityProgramTypeBean(final MobilityProgramType type) {
        this.code = type.getCode();
        this.name = type.getName().getContent();
        this.active = type.isActive();
    }
    
    /* *****************
     * GETTERS & SETTERS
     * *****************
     */

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
}
