package org.fenixedu.ulisboa.specifications.domain.curricularRules.executors.ruleExecutors;

import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.curricularRules.executors.RuleResult;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleExecutor;
import org.fenixedu.academic.domain.enrolment.EnrolmentContext;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.ulisboa.specifications.domain.curricularRules.StudentStatuteCurricularRule;
import org.fenixedu.ulisboa.specifications.domain.services.statute.StatuteServices;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;

public class StudentStatuteCurricularRuleExecutor extends CurricularRuleExecutor {

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

        if (!canApplyRule(enrolmentContext, curricularRule)) {
            return RuleResult.createNA(sourceDegreeModuleToEvaluate.getDegreeModule());
        }

        final StudentStatuteCurricularRule rule = (StudentStatuteCurricularRule) curricularRule;

        if (!StatuteServices.findStatuteTypes(enrolmentContext.getRegistration(), enrolmentContext.getExecutionPeriod())
                .contains(rule.getStatuteType())) {
            return RuleResult.createFalseWithLiteralMessage(sourceDegreeModuleToEvaluate.getDegreeModule(),
                    ULisboaSpecificationsUtil.bundle(
                            "curricularRules.ruleExecutors.StudentStatuteCurricularRuleExecutor.statutes.is.required.to.enrol",
                            sourceDegreeModuleToEvaluate.getName(), StatuteServices.getCodeAndName(rule.getStatuteType())));
        }

        return RuleResult.createTrue(sourceDegreeModuleToEvaluate.getDegreeModule());

    }

}
