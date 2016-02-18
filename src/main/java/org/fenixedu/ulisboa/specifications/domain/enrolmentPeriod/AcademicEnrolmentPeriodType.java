package org.fenixedu.ulisboa.specifications.domain.enrolmentPeriod;

import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;

public enum AcademicEnrolmentPeriodType {

    CURRICULAR_COURSE, SHIFT, SCHOOL_CLASS;

    public boolean isCurricularCourses() {
        return this == CURRICULAR_COURSE;
    }

    public boolean isShift() {
        return this == SHIFT;
    }

    public boolean isSchoolClass() {
        return this == SCHOOL_CLASS;
    }

    public LocalizedString getDescriptionI18N() {
        return ULisboaSpecificationsUtil.bundleI18N(getClass().getSimpleName() + "." + name());
    }

}
