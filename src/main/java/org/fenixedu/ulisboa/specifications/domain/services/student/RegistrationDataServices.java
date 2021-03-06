package org.fenixedu.ulisboa.specifications.domain.services.student;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear;
import org.fenixedu.ulisboa.specifications.domain.RegistrationObservations;
import org.fenixedu.ulisboa.specifications.domain.services.RegistrationServices;
import org.fenixedu.ulisboa.specifications.domain.student.RegistrationDataByExecutionYearExtendedInformation;
import org.fenixedu.ulisboa.specifications.domain.student.curriculum.CurriculumConfigurationInitializer.CurricularYearResult;
import org.fenixedu.ulisboa.specifications.ui.administrativeOffice.studentEnrolment.ManageRegistrationDataByExecutionYearDA.RegistrationDataEditBean;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;

public class RegistrationDataServices {

    static public void edit(final RegistrationDataEditBean input) {
        final RegistrationDataByExecutionYear data = input.getData();
        data.edit(input.getEnrolmentDate());
        setCurricularYear(data, input.getOverridenCurricularYear());
    }

    static public void delete(final RegistrationDataByExecutionYear input) {
        if (input.getRegistration().hasAnyEnrolmentsIn(input.getExecutionYear())) {
            throw new DomainException("error.RegistrationDataByExecutionYear.checkDeletion.hasAnyEnrolments",
                    input.getExecutionYear().getQualifiedName());
        }

        input.delete();
    }

    static public void setCurricularYear(final RegistrationDataByExecutionYear data, final Integer update) {
        final RegistrationDataByExecutionYearExtendedInformation info =
                RegistrationDataByExecutionYearExtendedInformation.findOrCreate(data);

        final Integer overriden = info.getCurricularYear();
        if ((overriden == null && update != null) || (overriden != null && !overriden.equals(update))) {

            final Registration registration = data.getRegistration();
            final CurricularYearResult current = RegistrationServices.getCurricularYear(registration, data.getExecutionYear());

            final RegistrationObservations observation = new RegistrationObservations(registration);

            String value;
            if (update == null) {
                value = ULisboaSpecificationsUtil.bundle("label.curricularYear.overriden.observation.removed");
            } else {
                value = ULisboaSpecificationsUtil.bundle("label.curricularYear.overriden.observation.updated", update.toString(),
                        String.valueOf(current.getResult()), current.getJustificationPresentation());
            }
            value = value + " " + ULisboaSpecificationsUtil.bundle("label.curricularYear.overriden.observation.suffix",
                    String.valueOf(current.getResult()), current.getJustificationPresentation());
            observation.setValue(value);

            info.setCurricularYear(update);
            RegistrationServices.invalidateCacheCurricularYear(registration, data.getExecutionYear());
        }
    }

    static private Integer getCurricularYear(final RegistrationDataByExecutionYear data) {
        return data == null || data.getExtendedInformation() == null ? null : data.getExtendedInformation().getCurricularYear();
    }

    static public Integer getOverridenCurricularYear(Registration registration, ExecutionYear executionYear) {

        final RegistrationDataByExecutionYear data;
        if (executionYear != null) {
            data = getRegistrationData(registration, executionYear);
        } else {
            data = null;
        }

        final Integer curricularYear = getCurricularYear(data);
        if (curricularYear != null) {
            return curricularYear;
        }

        return null;
    }

    static public RegistrationDataByExecutionYear getRegistrationData(final Registration registration, final ExecutionYear year) {

        for (final RegistrationDataByExecutionYear iter : registration.getRegistrationDataByExecutionYearSet()) {
            if (iter.getExecutionYear() == year) {
                return iter;
            }
        }

        return null;
    }

    static public RegistrationDataByExecutionYear getFirstRegistrationData(final Registration registration) {
        return registration == null ? null : registration.getRegistrationDataByExecutionYearSet().stream()
                .min((i, j) -> i.getExecutionYear().compareTo(j.getExecutionYear())).orElse(null);
    }

    static public RegistrationDataByExecutionYear getLastRegistrationData(final Registration registration) {
        return registration == null ? null : registration.getRegistrationDataByExecutionYearSet().stream()
                .max((i, j) -> i.getExecutionYear().compareTo(j.getExecutionYear())).orElse(null);
    }

}
