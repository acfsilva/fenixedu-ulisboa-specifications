package org.fenixedu.ulisboa.specifications.domain.serviceRequests.processors;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.ServiceRequestProperty;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.ServiceRequestSlot;
import org.fenixedu.ulisboa.specifications.domain.serviceRequests.ULisboaServiceRequest;
import org.fenixedu.ulisboa.specifications.util.ULisboaConstants;

import pt.ist.fenixframework.Atomic;

public class FillExtracurricularEnrolmentsByYearPropertyProcessor
        extends FillExtracurricularEnrolmentsByYearPropertyProcessor_Base {

    protected FillExtracurricularEnrolmentsByYearPropertyProcessor() {
        super();
    }

    protected FillExtracurricularEnrolmentsByYearPropertyProcessor(final LocalizedString name,
            final Boolean exclusiveTransation) {
        this();
        super.init(name, exclusiveTransation);
    }

    @Atomic
    public static ULisboaServiceRequestProcessor create(final LocalizedString name, final Boolean exclusiveTransation) {
        return new FillExtracurricularEnrolmentsByYearPropertyProcessor(name, exclusiveTransation);
    }

    @Override
    @Atomic
    public void process(final ULisboaServiceRequest request, final boolean forceUpdate) {
        if (forceUpdate && request.hasExtracurricularEnrolmentsByYear()) {
            request.findProperty(ULisboaConstants.EXTRACURRICULAR_ENROLMENTS_BY_YEAR).delete();
        }

        if (!request.hasExtracurricularEnrolmentsByYear()) {
            ExecutionYear executionYear =
                    request.hasExecutionYear() ? request.getExecutionYear() : ExecutionYear.readCurrentExecutionYear();
            List<ICurriculumEntry> enrolments = request.getRegistration().getStudentCurricularPlan(executionYear)
                    .getEnrolmentsByExecutionYear(executionYear).stream().filter(ULisboaConstants.isExtraCurricular)
                    .map(ICurriculumEntry.class::cast).collect(Collectors.toList());
            if (validate(enrolments)) {
                ServiceRequestProperty.create(request,
                        ServiceRequestSlot.getByCode(ULisboaConstants.EXTRACURRICULAR_ENROLMENTS_BY_YEAR), enrolments);
            }
        }
    }

    private boolean validate(final List<ICurriculumEntry> enrolments) {
        return !enrolments.isEmpty();
    }

}
