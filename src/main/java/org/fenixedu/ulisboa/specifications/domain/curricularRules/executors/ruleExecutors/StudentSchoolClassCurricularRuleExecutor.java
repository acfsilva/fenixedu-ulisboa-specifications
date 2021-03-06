package org.fenixedu.ulisboa.specifications.domain.curricularRules.executors.ruleExecutors;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutor;
import org.fenixedu.academic.domain.enrolment.EnroledOptionalEnrolment;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.enrolment.OptionalDegreeModuleToEnrol;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.ulisboa.specifications.domain.curricularRules.StudentSchoolClassCurricularRule;
import org.fenixedu.ulisboa.specifications.domain.services.RegistrationServices;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class StudentSchoolClassCurricularRuleExecutor extends CurricularRuleExecutor {

    @Override
    protected RuleResult executeEnrolmentInEnrolmentEvaluation(final ICurricularRule curricularRule,
            final IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
    }

    @Override
    protected boolean canBeEvaluated(ICurricularRule curricularRule, IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate,
            EnrolmentContext enrolmentContext) {
        return true;
    }

    // author = "legidio", comment = "Deprecated: grades must be set before enrolment periods"
    @Override
    @Deprecated
    protected RuleResult executeEnrolmentWithRulesAndTemporaryEnrolment(final ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, final EnrolmentContext enrolmentContext) {
        return executeEnrolmentVerificationWithRules(curricularRule, sourceDegreeModuleToEvaluate, enrolmentContext);
    }

    @Override
    protected RuleResult executeEnrolmentVerificationWithRules(ICurricularRule curricularRule,
            IDegreeModuleToEvaluate sourceDegreeModuleToEvaluate, EnrolmentContext enrolmentContext) {

        if (sourceDegreeModuleToEvaluate.isEnroled() || !canApplyRule(enrolmentContext, curricularRule)
                || !sourceDegreeModuleToEvaluate.isLeaf()
                || sourceDegreeModuleToEvaluate.getExecutionPeriod() != enrolmentContext.getExecutionPeriod()) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }
        
        if (sourceDegreeModuleToEvaluate instanceof OptionalDegreeModuleToEnrol
                || sourceDegreeModuleToEvaluate instanceof EnroledOptionalEnrolment) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final StudentSchoolClassCurricularRule schoolClassCurricularRule = (StudentSchoolClassCurricularRule) curricularRule;
        final Registration registration = enrolmentContext.getRegistration();
        final ExecutionSemester executionSemester = sourceDegreeModuleToEvaluate.getExecutionPeriod();
        final CurricularCourse curricularCourse = (CurricularCourse) sourceDegreeModuleToEvaluate.getDegreeModule();

        if (schoolClassCurricularRule.getSchoolClassMustContainCourse()) {

            int curricularYear =
                    RegistrationServices.getCurricularYear(registration, executionSemester.getExecutionYear()).getResult();
            if (sourceDegreeModuleToEvaluate.getContext().getCurricularYear().equals(curricularYear)) {

                if (registration.getSchoolClassesSet().stream().filter(sc -> sc.getExecutionPeriod() == executionSemester)
                        .flatMap(sc -> sc.getAssociatedShiftsSet().stream().map(s -> s.getExecutionCourse())
                                .flatMap(ec -> ec.getAssociatedCurricularCoursesSet().stream()))
                        .noneMatch(cc -> cc == curricularCourse)) {

                    return RuleResult.createFalseWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                            ULisboaSpecificationsUtil.bundle(
                                    "curricularRules.ruleExecutors.StudentSchoolClassCurricularRuleExecutor.error.schoolClassMustContainCourse",
                                    executionSemester.getName(), curricularCourse.getCode(), curricularCourse.getName()));
                }
            }
        }

        if (schoolClassCurricularRule.getCourseMustHaveFreeShifts()) {

            final Set<SchoolClass> registrationSchoolClasses = registration.getSchoolClassesSet().stream()
                    .filter(sc -> sc.getExecutionPeriod() == executionSemester).collect(Collectors.toSet());
            if (!registrationSchoolClasses.isEmpty()) {
                for (final SchoolClass schoolClass : registrationSchoolClasses) {
                    final DegreeCurricularPlan degreeCurricularPlan = schoolClass.getExecutionDegree().getDegreeCurricularPlan();
                    final Set<Shift> shifts =
                            schoolClass.getAssociatedShiftsSet().stream()
                                    .filter(s -> s.getExecutionCourse()
                                            .getCurricularCourseFor(degreeCurricularPlan) == curricularCourse)
                                    .collect(Collectors.toSet());

                    final Multimap<ShiftType, Shift> shiftsTypesByShift = ArrayListMultimap.create();
                    shifts.forEach(s -> s.getTypes().forEach(st -> shiftsTypesByShift.put(st, s)));

                    for (final ShiftType shiftType : shiftsTypesByShift.keySet()) {
                        final Collection<Shift> shiftsForType = shiftsTypesByShift.get(shiftType);
                        if (shiftsForType.stream().noneMatch(s -> s.getStudentsSet().contains(registration))) {
                            if (shiftsForType.stream().noneMatch(s -> isFree(s))) {
                                return RuleResult.createFalseWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                                        ULisboaSpecificationsUtil.bundle(
                                                "curricularRules.ruleExecutors.StudentSchoolClassCurricularRuleExecutor.error.courseMustHaveFreeShiftsInSchoolClass",
                                                curricularCourse.getCode(), curricularCourse.getName()));
                            }
                        }
                    }

                }
            } else {
                final Set<Shift> allShifts = curricularCourse.getExecutionCoursesByExecutionPeriod(executionSemester).stream()
                        .flatMap(ec -> ec.getAssociatedShifts().stream()).collect(Collectors.toSet());
                final Set<ShiftType> allShiftsTypes =
                        allShifts.stream().flatMap(s -> s.getTypes().stream()).collect(Collectors.toSet());
                for (final ShiftType shiftType : allShiftsTypes) {
                    if (allShifts.stream().filter(s -> s.getTypes().contains(shiftType)).noneMatch(s -> isFree(s))) {
                        return RuleResult.createFalseWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                                ULisboaSpecificationsUtil.bundle(
                                        "curricularRules.ruleExecutors.StudentSchoolClassCurricularRuleExecutor.error.courseMustHaveFreeShifts",
                                        curricularCourse.getCode(), curricularCourse.getName()));
                    }
                }
            }

        }

        if (StringUtils.isNotBlank(schoolClassCurricularRule.getSchoolClassNames()) && schoolClassCurricularRule
                .getSchoolClasses(executionSemester).stream().noneMatch(sc -> sc.getRegistrationsSet().contains(registration))) {

            return RuleResult.createFalseWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    ULisboaSpecificationsUtil.bundle(
                            "curricularRules.ruleExecutors.StudentSchoolClassCurricularRuleExecutor.error.registrationNotForSchoolClass",
                            curricularCourse.getCode(), curricularCourse.getName()));
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());

    }

    private static boolean isFree(final Shift shift) {
        return (shift.getLotacao() - shift.getStudentsSet().size()) > 0;
    }

}
