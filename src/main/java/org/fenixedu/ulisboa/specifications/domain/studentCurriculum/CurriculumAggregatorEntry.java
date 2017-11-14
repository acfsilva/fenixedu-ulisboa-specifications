/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 *
 * 
 * This file is part of FenixEdu fenixedu-ulisboa-specifications.
 *
 * FenixEdu Specifications is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Specifications is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Specifications.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.ulisboa.specifications.domain.studentCurriculum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;
import org.fenixedu.academic.domain.studentCurriculum.Dismissal;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.ulisboa.specifications.domain.curricularRules.CurricularRuleServices;
import org.fenixedu.ulisboa.specifications.domain.curricularRules.CurriculumAggregatorApproval;
import org.fenixedu.ulisboa.specifications.domain.exceptions.ULisboaSpecificationsDomainException;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;
import org.joda.time.YearMonthDay;

import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

public class CurriculumAggregatorEntry extends CurriculumAggregatorEntry_Base {

    protected CurriculumAggregatorEntry() {
        super();
    }

    static protected CurriculumAggregatorEntry create(final CurriculumAggregator aggregator, final Context context,
            final AggregationMemberEvaluationType evaluationType, final BigDecimal gradeFactor, final int gradeValueScale,
            final boolean optional) {

        final CurriculumAggregatorEntry result = new CurriculumAggregatorEntry();
        result.setAggregator(aggregator);
        result.setContext(context);
        result.init(evaluationType, gradeFactor, gradeValueScale, optional);

        final DegreeModule degreeModule = context.getChildDegreeModule();
        if (degreeModule.isLeaf()) {
            final List<? extends ICurricularRule> curricularRules = CurricularRuleServices.getCurricularRules(degreeModule,
                    CurriculumAggregatorApproval.class, context.getBeginExecutionPeriod());
            if (curricularRules.isEmpty()) {
                new CurriculumAggregatorApproval(degreeModule, context.getParentCourseGroup(), context.getBeginExecutionPeriod(),
                        (ExecutionSemester) null);
            }
        }

        return result;
    }

    @Atomic
    public CurriculumAggregatorEntry edit(final AggregationMemberEvaluationType evaluationType, final BigDecimal gradeFactor,
            final int gradeValueScale, final boolean optional) {

        init(evaluationType, gradeFactor, gradeValueScale, optional);

        return this;
    }

    private void init(final AggregationMemberEvaluationType evaluationType, final BigDecimal gradeFactor,
            final int gradeValueScale, final boolean optional) {

        super.setEvaluationType(evaluationType);
        super.setGradeFactor(gradeFactor);
        super.setGradeValueScale(gradeValueScale);
        super.setOptional(optional);

        checkRules();
    }

    private void checkRules() {
        if (getAggregator() == null) {
            throw new ULisboaSpecificationsDomainException("error.CurriculumAggregatorEntry.required.Aggregator");
        }

        if (getContext() == null) {
            throw new ULisboaSpecificationsDomainException("error.CurriculumAggregatorEntry.required.Context");
        }

        final CurriculumAggregatorEntry found = CurriculumAggregatorServices.findAggregatorEntry(getContext(), getSince());
        if (found != null && found != this) {
            throw new DomainException("error.CurriculumAggregatorEntry.duplicate");
        }

        if (!getContext().isValid(getSince())) {
            throw new DomainException("error.CurriculumAggregatorEntry.invalid.Context");
        }

        if (getEvaluationType() == null) {
            throw new ULisboaSpecificationsDomainException("error.CurriculumAggregatorEntry.required.EvaluationType");
        }

        if (getGradeFactor() == null || getGradeFactor().compareTo(BigDecimal.ZERO) < 0) {
            throw new ULisboaSpecificationsDomainException("error.CurriculumAggregatorEntry.required.GradeFactor");
        }

        if (getGradeValueScale() < 0) {
            throw new DomainException("error.CurriculumAggregatorEntry.required.GradeValueScale");
        }
    }

    @Atomic
    public void delete() {
        super.setAggregator(null);
        super.setContext(null);

        super.deleteDomainObject();
    }

    public ExecutionYear getSince() {
        return getAggregator().getSince();
    }

    public String getDescription() {
        return ULisboaSpecificationsUtil.bundle("CurriculumAggregatorEntry");
    }

    public String getDescriptionFull() {
        final String description = getAggregator().getCurricularCourse().getCode();
        final String since = getAggregator().getSince().getQualifiedName();

        final String gradeFactor =
                ", " + (getGradeFactor() == null || BigDecimal.ZERO.compareTo(getGradeFactor()) == 0 ? BundleUtil
                        .getLocalizedString(Bundle.ENUMERATION, GradeScale.TYPEQUALITATIVE.name())
                        .getContent() : getGradeFactor().multiply(BigDecimal.valueOf(100d)).stripTrailingZeros().toPlainString()
                                + "%");

        final GradeScale gradeScale = getGradeScale();
        String gradeScaleDescription = "";
        if (gradeScale != GradeScale.TYPE20) {
            gradeScaleDescription = " " + gradeScale.getDescription().replace(GradeScale.TYPE20.getDescription(), "");
        }

        String result = String.format("%s [%s %s%s%s]", description, BundleUtil.getString(Bundle.APPLICATION, "label.since"),
                since, gradeFactor, gradeScaleDescription);

        if (getOptional()) {
            result += " [Op]";
        }

        return result;
    }

    public boolean isLegacy() {
        return getAggregator().isLegacy();
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return getContext().getParentCourseGroup().getParentDegreeCurricularPlan();
    }

    public boolean isValid(final ExecutionYear year) {
        return year != null && getSince().isBeforeOrEquals(year);
    }

    public CurricularCourse getCurricularCourse() {
        return (CurricularCourse) getContext().getChildDegreeModule();
    }

    public GradeScale getGradeScale() {
        final GradeScale competenceScale = getCurricularCourse().getCompetenceCourse().getGradeScale();
        return competenceScale != null ? competenceScale : getCurricularCourse().getGradeScaleChain();
    }

    public boolean isCandidateForEvaluation() {
        return getEvaluationType().isCandidateForEvaluation();
    }

    protected boolean isAggregationEvaluated(final StudentCurricularPlan plan) {
        final CurriculumModule module = getCurriculumModule(plan, false);
        if (module != null) {
            if (module instanceof Dismissal) {
                return true;
            }
            if (module instanceof Enrolment) {
                final Enrolment enrolment = (Enrolment) module;
                return !enrolment.isAnnulled() && !enrolment.getGrade().isEmpty();
            }
        }

        return false;
    }

    public boolean isAggregationConcluded(final StudentCurricularPlan plan) {
        final CurriculumModule module = getCurriculumModule(plan, true);
        return module != null && module.isConcluded();
    }

    private CurriculumModule getCurriculumModule(final StudentCurricularPlan plan, final boolean approved) {
        final CurriculumModule result;

        final DegreeModule degreeModule = getCurricularCourse();
        if (degreeModule.isCurricularCourse()) {

            if (approved) {
                result = plan.getApprovedCurriculumLine((CurricularCourse) degreeModule);

            } else {

                result = plan.getAllCurriculumLines().stream().filter(i -> i.getDegreeModule() == getCurricularCourse())
                        .max((x, y) -> {
                            final int c = x.getExecutionYear().compareTo(y.getExecutionYear());
                            return c == 0 ? Comparator.comparing(CurriculumLine::getExternalId).compare(x, y) : c;
                        }).orElse(null);
            }

        } else {
            throw new ULisboaSpecificationsDomainException("error.CurriculumAggregatorEntry.unexpected.entry.type");
        }

        return result;
    }

    private Set<ICurriculumEntry> getApprovedCurriculumEntries(final StudentCurricularPlan plan) {
        return getCurriculumEntries(plan, true);
    }

    protected Set<ICurriculumEntry> getCurriculumEntries(final StudentCurricularPlan plan, final boolean approved) {
        final Set<ICurriculumEntry> result = Sets.newHashSet();

        final CurriculumModule module = getCurriculumModule(plan, approved);
        if (module != null) {

            final Collection<CurriculumLine> lines =
                    approved ? module.getApprovedCurriculumLines() : module.getAllCurriculumLines();

            result.addAll(lines.stream().filter(i -> i instanceof ICurriculumEntry).map(i -> ((ICurriculumEntry) i))
                    .collect(Collectors.toSet()));
        }

        return result;
    }

    protected BigDecimal calculateGradeValue(final StudentCurricularPlan plan) {
        BigDecimal result = BigDecimal.ZERO;

        if (isAggregationConcluded(plan)) {
            final Set<ICurriculumEntry> approvals = getApprovedCurriculumEntries(plan);

            if (approvals.size() == 1) {
                final Grade grade = approvals.iterator().next().getGrade();
                if (grade.isNumeric()) {
                    result = new BigDecimal(grade.getValue());
                }

            } else {

                final Supplier<Stream<ICurriculumEntry>> supplier =
                        () -> approvals.stream().filter(i -> i.getGrade().isNumeric());

                final BigDecimal sum =
                        supplier.get().map(i -> new BigDecimal(i.getGrade().getValue())).reduce(BigDecimal.ZERO, BigDecimal::add);
                final BigDecimal divisor = new BigDecimal(supplier.get().count());

                final BigDecimal avg =
                        sum.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : sum.divide(divisor, 10, RoundingMode.UNNECESSARY);

                result = avg.setScale(getGradeValueScale(), RoundingMode.UNNECESSARY);
            }
        }

        return result;
    }

    @SuppressWarnings("deprecation")
    protected YearMonthDay calculateConclusionDate(final StudentCurricularPlan plan) {
        YearMonthDay result = null;

        if (isAggregationConcluded(plan)) {
            final Set<ICurriculumEntry> approvals = getApprovedCurriculumEntries(plan);

            if (approvals.size() == 1) {
                result = approvals.iterator().next().getApprovementDate();

            } else {

                for (final ICurriculumEntry iter : approvals) {
                    final YearMonthDay conclusionDate = iter.getApprovementDate();
                    if (conclusionDate != null && (result == null || conclusionDate.isAfter(result))) {
                        result = conclusionDate;
                    }
                }
            }
        }

        return result;
    }

}
