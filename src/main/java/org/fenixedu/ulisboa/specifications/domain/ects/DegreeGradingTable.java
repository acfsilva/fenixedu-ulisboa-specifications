package org.fenixedu.ulisboa.specifications.domain.ects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.dto.student.RegistrationConclusionBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.qubdocs.academic.documentRequests.providers.ConclusionInformationDataProvider;
import org.fenixedu.qubdocs.academic.documentRequests.providers.CurriculumEntry;
import org.fenixedu.ulisboa.specifications.domain.ects.GradingTable.GeneratorWorker;
import org.fenixedu.ulisboa.specifications.domain.student.curriculum.conclusion.RegistrationConclusionInformation;
import org.fenixedu.ulisboa.specifications.domain.student.curriculum.conclusion.RegistrationConclusionServices;

import pt.ist.fenixframework.CallableWithoutException;

public class DegreeGradingTable extends DegreeGradingTable_Base {

    public DegreeGradingTable() {
        super();
    }

    @Override
    public void delete() {
        setDegree(null);
        setProgramConclusion(null);
        setRegistration(null);
        super.delete();
    }

    public static Stream<DegreeGradingTable> findAll() {
        return Bennu.getInstance().getGradingTablesSet().stream().filter(DegreeGradingTable.class::isInstance)
                .map(DegreeGradingTable.class::cast);
    }

    public static Set<DegreeGradingTable> find(final ExecutionYear ey) {
        return find(ey, false);
    }

    public static Set<DegreeGradingTable> find(final ExecutionYear ey, boolean includeLegacy) {
        return ey.getGradingTablesSet().stream().filter(DegreeGradingTable.class::isInstance).map(DegreeGradingTable.class::cast)
                .filter(dgt -> (includeLegacy || dgt.getRegistration() == null)).collect(Collectors.toSet());
    }

    public static DegreeGradingTable find(final ExecutionYear ey, final ProgramConclusion pc, final Degree d) {
        return d.getDegreeGradingTablesSet().stream().filter(dgt -> (dgt.getRegistration() == null))
                .filter(dgt -> dgt.getProgramConclusion() == pc).filter(dgt -> dgt.getExecutionYear() == ey).findAny()
                .orElse(null);
    }

    public static DegreeGradingTable find(final ExecutionYear ey, final ProgramConclusion pc, final Registration reg) {
        return reg.getDegreeGradingTablesSet().stream().filter(dgt -> dgt.getExecutionYear() == ey)
                .filter(dgt -> dgt.getProgramConclusion() == pc).findFirst().orElse(find(ey, pc, reg.getDegree()));
    }

    public static String getEctsGrade(RegistrationConclusionBean registrationConclusionBean) {
        if (registrationConclusionBean != null && registrationConclusionBean.getFinalGrade() != null
                && registrationConclusionBean.getFinalGrade().getValue() != null) {
            DegreeGradingTable table =
                    DegreeGradingTable.find(registrationConclusionBean.getConclusionYear(),
                            registrationConclusionBean.getProgramConclusion(), registrationConclusionBean.getRegistration());
            if (table != null) {
                return table.getEctsGrade(registrationConclusionBean.getFinalGrade().getValue());
            }
        }
        return "-";
    }

    public static void registerProvider() {
        ConclusionInformationDataProvider.setDegreeEctsGradeProviderProvider(conclusion -> DegreeGradingTable
                .getEctsGrade(conclusion));
    }

    public static Set<DegreeGradingTable> generate(final ExecutionYear executionYear) {
        Set<DegreeGradingTable> allTables = new HashSet<DegreeGradingTable>();
        for (DegreeCurricularPlan dcp : executionYear.getDegreeCurricularPlans()) {
            Degree degree = dcp.getDegree();
            if (!GradingTableSettings.getApplicableDegreeTypes().contains(degree.getDegreeType())) {
                continue;
            }
            for (ProgramConclusion programConclusion : ProgramConclusion.conclusionsFor(dcp).collect(Collectors.toSet())) {
                DegreeGradingTable table = find(executionYear, programConclusion, degree);
                if (table == null) {
                    CallableWithoutException<DegreeGradingTable> workerLogic =
                            new CallableWithoutException<DegreeGradingTable>() {
                                @Override
                                public DegreeGradingTable call() {
                                    DegreeGradingTable table = new DegreeGradingTable();
                                    table.setExecutionYear(executionYear);
                                    table.setProgramConclusion(programConclusion);
                                    table.setDegree(degree);
                                    table.compileData();
                                    return table;
                                }
                            };
                    GeneratorWorker<DegreeGradingTable> worker = new GeneratorWorker<DegreeGradingTable>(workerLogic);
                    worker.start();
                    try {
                        worker.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    allTables.add(worker.getTable());
                } else {
                    allTables.add(table);
                }
            }
        }
        return allTables;
    }

    @Override
    public void compileData() {
        GradingTableData tableData = new GradingTableData();
        setData(tableData);
        List<BigDecimal> sample = harvestSample();
        if (sample != null) {
            GradingTableGenerator.generateTableData(this, sample);
        } else {
            InstitutionGradingTable.copyData(this);
            setCopied(true);
        }
    }

    private List<BigDecimal> harvestSample() {
        List<BigDecimal> sample = new ArrayList<BigDecimal>();
        int coveredYears = 0;
        boolean sampleOK = false;
        final Map<ExecutionYear, Set<RegistrationConclusionBean>> conclusionsMap = collectConclusions();
        for (ExecutionYear year = getExecutionYear().getPreviousExecutionYear(); year != null; year =
                year.getPreviousExecutionYear()) {

            if (conclusionsMap.get(year) != null) {
                for (RegistrationConclusionBean bean : conclusionsMap.get(year)) {
                    Integer finalAverage =
                            bean.getFinalGrade().getNumericValue() != null ? bean.getFinalGrade().getNumericValue()
                                    .setScale(0, RoundingMode.HALF_UP).intValue() : 0;
                    if (finalAverage == 0) {
                        continue;
                    }
                    sample.add(new BigDecimal(finalAverage));
                }
            }

            if (++coveredYears >= GradingTableSettings.getMinimumPastYears()
                    && sample.size() >= GradingTableSettings.getMinimumSampleSize()) {
                sampleOK = true;
                break;
            }

            if (coveredYears == GradingTableSettings.getMaximumPastYears()) {
                break;
            }
        }
        return sampleOK ? sample : null;
    }

    private Map<ExecutionYear, Set<RegistrationConclusionBean>> collectConclusions() {
        final Map<ExecutionYear, Set<RegistrationConclusionBean>> conclusionsMap =
                new LinkedHashMap<ExecutionYear, Set<RegistrationConclusionBean>>();

        for (final Registration registration : getDegree().getRegistrationsSet()) {
            if (registration.getStudentCurricularPlansSet().isEmpty()) {
                continue;
            }
            for (RegistrationConclusionInformation info : RegistrationConclusionServices.inferConclusion(registration)) {
                if (info.getCurriculumGroup() == null || !info.isConcluded()) {
                    continue;
                }
                final ExecutionYear conclusionYear = info.getRegistrationConclusionBean().getConclusionYear();
                if (!conclusionsMap.containsKey(conclusionYear)) {
                    conclusionsMap.put(conclusionYear, new HashSet<RegistrationConclusionBean>());
                }

                conclusionsMap.get(conclusionYear).add(info.getRegistrationConclusionBean());
            }
        }
        return conclusionsMap;
    }
}
