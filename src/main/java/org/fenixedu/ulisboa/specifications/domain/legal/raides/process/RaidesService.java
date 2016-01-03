package org.fenixedu.ulisboa.specifications.domain.legal.raides.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DistrictSubdivision;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Qualification;
import org.fenixedu.academic.domain.SchoolLevelType;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.raides.DegreeClassification;
import org.fenixedu.academic.domain.raides.DegreeDesignation;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumLine;
import org.fenixedu.ulisboa.specifications.domain.legal.LegalReportContext;
import org.fenixedu.ulisboa.specifications.domain.legal.mapping.LegalMapping;
import org.fenixedu.ulisboa.specifications.domain.legal.raides.IGrauPrecedenteCompleto;
import org.fenixedu.ulisboa.specifications.domain.legal.raides.IMatricula;
import org.fenixedu.ulisboa.specifications.domain.legal.raides.Raides;
import org.fenixedu.ulisboa.specifications.domain.legal.raides.TblInscrito;
import org.fenixedu.ulisboa.specifications.domain.legal.raides.mapping.BranchMappingType;
import org.fenixedu.ulisboa.specifications.domain.legal.raides.mapping.LegalMappingType;
import org.fenixedu.ulisboa.specifications.domain.legal.report.LegalReport;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;

public class RaidesService {

    protected LegalReport report;

    public RaidesService(final LegalReport report) {
        this.report = report;
    }

    protected String anoCurricular(final Registration registration, final ExecutionYear executionYear) {
        if (Raides.isDoctoralDegree(registration)) {
            return LegalMapping.find(report, LegalMappingType.CURRICULAR_YEAR).translate(Raides.AnoCurricular.NAO_APLICAVEL_CODE);
        }
        
        if (isOnlyEnrolledOnDissertation(registration, executionYear)) {
            return LegalMapping.find(report, LegalMappingType.CURRICULAR_YEAR).translate(Raides.AnoCurricular.DISSERTACAO_CODE);
        }

        return LegalMapping.find(report, LegalMappingType.CURRICULAR_YEAR).translate(
                String.valueOf(registration.getCurricularYear(executionYear)));
    }

    protected boolean isOnlyEnrolledOnDissertation(final Registration registration, final ExecutionYear executionYear) {
        Collection<Enrolment> enrolments = registration.getEnrolments(executionYear);

        if (enrolments.size() > 1) {
            return false;
        }

        return registration.getStudentCurricularPlan(executionYear).getDissertationEnrolments()
                .contains(enrolments.iterator().next());
    }

    protected boolean isFirstTimeOnDegree(final Registration registration, final ExecutionYear executionYear) {
        if (!registration.getPrecedentDegreeRegistrations().isEmpty()) {
            return false;
        }

        return executionYear == registration.getStartExecutionYear();
    }

    /*
     * OTHER METHODS
     */

    protected void preencheInformacaoMatricula(final LegalReport report, final IMatricula bean, final Unit institutionUnit,
            final ExecutionYear executionYear, final Registration registration) {

        bean.setIdEstab(institutionUnit.getCode());
        bean.setIdAluno(registration.getStudent().getNumber().toString());

        bean.setAnoLectivo(executionYear.getQualifiedName());
        bean.setCurso(degree(registration).getMinistryCode());

        if (registration.getStudentCurricularPlan(executionYear).getInternalCyclesBranches().size() > 2) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.enrolled.more.than.one.branch",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
        } else if (registration.getStudentCurricularPlan(executionYear).getInternalCyclesBranches().isEmpty()) {
            bean.setRamo(null);
        } else {
            bean.setRamo(BranchMappingType.readMapping(report).translate(
                    registration.getStudentCurricularPlan(executionYear).getInternalCyclesBranches().iterator().next()
                            .getDegreeModule()));
        }
    }

    protected class DEGREE_VALUE_COMPARATOR implements Comparator<Degree> {

        protected Map<Degree, Integer> m;

        public DEGREE_VALUE_COMPARATOR(final Map<Degree, Integer> m) {
            this.m = m;
        }

        @Override
        public int compare(final Degree o1, final Degree o2) {
            int result = m.get(o1).compareTo(m.get(o2));

            if (result != 0) {
                return -result;
            }

            return o1.getExternalId().compareTo(o2.getExternalId());
        }

    }

    protected Degree degree(final Registration registration) {
        if (!registration.getDegree().isEmpty()) {
            return registration.getDegree();
        }

        final Map<Degree, Integer> enrolmentsByDegreeCountMap = new HashMap<Degree, Integer>();
        Collection<CurriculumLine> allCurriculumLines = registration.getAllCurriculumLines();

        for (final CurriculumLine curriculumLine : allCurriculumLines) {
            if (!curriculumLine.isEnrolment()) {
                continue;
            }

            Degree degree = ((Enrolment) curriculumLine).getDegreeModule().getDegree();

            if (!enrolmentsByDegreeCountMap.containsKey(degree)) {
                enrolmentsByDegreeCountMap.put(degree, 0);
            }

            enrolmentsByDegreeCountMap.put(degree, enrolmentsByDegreeCountMap.get(degree) + 1);
        }

        final Map<Degree, Integer> enrolmentsByDegreeCountMapSorted =
                new TreeMap<Degree, Integer>(new DEGREE_VALUE_COMPARATOR(enrolmentsByDegreeCountMap));
        enrolmentsByDegreeCountMapSorted.putAll(enrolmentsByDegreeCountMap);

        return enrolmentsByDegreeCountMapSorted.entrySet().iterator().next().getKey();

    }

    protected void preencheGrauPrecedentCompleto(final IGrauPrecedenteCompleto bean, final Unit institutionUnit,
            final ExecutionYear executionYear, final Registration registration) {
        final StudentCandidacy studentCandidacy = registration.getStudentCandidacy();
        final Qualification lastCompletedQualification = studentCandidacy.getLastCompletedQualification();

        if (lastCompletedQualification == null) {
            return;
        }

        if (lastCompletedQualification.getSchoolLevel() != null) {
            bean.setEscolaridadeAnterior(LegalMapping.find(report, LegalMappingType.PRECEDENT_SCHOOL_LEVEL).translate(
                    lastCompletedQualification.getSchoolLevel()));

            if (SchoolLevelType.OTHER.equals(lastCompletedQualification.getSchoolLevel())) {
                bean.setOutroEscolaridadeAnterior(lastCompletedQualification.getOtherSchoolLevel());
            }
        }

        if (lastCompletedQualification.getCountry() != null) {
            bean.setPaisEscolaridadeAnt(lastCompletedQualification.getCountry().getCode());
        }

        if (lastCompletedQualification.getConclusionYear() != null) {
            bean.setAnoEscolaridadeAnt(lastCompletedQualification.getConclusionYear().toString());
        }

        if (lastCompletedQualification.getInstitution() != null && lastCompletedQualification.getInstitution().isOfficial()) {
            bean.setEstabEscolaridadeAnt(lastCompletedQualification.getInstitution().getCode());
        } else if (lastCompletedQualification.getInstitution() != null) {
            bean.setEstabEscolaridadeAnt(Raides.Estabelecimentos.OUTRO);
            bean.setOutroEstabEscolarAnt(lastCompletedQualification.getInstitution().getNameI18n()
                    .getContent(Language.getDefaultLanguage()));
        }

        boolean precedentDegreeDesignationFilled = false;
        if (isPrecedentDegreePortugueseHigherEducation(lastCompletedQualification)
                && !Strings.isNullOrEmpty(lastCompletedQualification.getDegree())) {
            final DegreeDesignation degreeDesignation =
                    DegreeDesignation.readByNameAndSchoolLevel(lastCompletedQualification.getDegree(),
                            lastCompletedQualification.getSchoolLevel());

            if (degreeDesignation != null) {
                bean.setCursoEscolarAnt(degreeDesignation.getCode());
                precedentDegreeDesignationFilled = true;
            }
        }

        if (!precedentDegreeDesignationFilled && !Strings.isNullOrEmpty(lastCompletedQualification.getDegree())) {
            bean.setCursoEscolarAnt(Raides.Cursos.OUTRO);
            bean.setOutroCursoEscolarAnt(lastCompletedQualification.getDegree());
        }

        if (bean.isTipoEstabSecSpecified()) {
            if (lastCompletedQualification.getSchoolLevel() == SchoolLevelType.HIGH_SCHOOL_OR_EQUIVALENT
                    && studentCandidacy.getHighSchoolType() != null) {
                bean.setTipoEstabSec(LegalMapping.find(report, LegalMappingType.HIGH_SCHOOL_TYPE).translate(
                        studentCandidacy.getHighSchoolType()));
            }
        }

        if (Strings.isNullOrEmpty(bean.getTipoEstabSec()) && registration.getStudent().getLatestPersonalIngressionData() != null
                && registration.getStudent().getLatestPersonalIngressionData().getHighSchoolType() != null) {
            final PersonalIngressionData personalIngressionData = registration.getStudent().getLatestPersonalIngressionData();
            bean.setTipoEstabSec(LegalMapping.find(report, LegalMappingType.HIGH_SCHOOL_TYPE).translate(
                    personalIngressionData.getHighSchoolType()));
        }

        validaGrauPrecedenteCompleto(institutionUnit, executionYear, registration, lastCompletedQualification, bean);
        validaCursoOficialInstituicaoOficial(institutionUnit, executionYear, registration, lastCompletedQualification, bean);
    }

    protected boolean isPrecedentDegreePortugueseHigherEducation(final Qualification lastCompletedQualification) {
        return lastCompletedQualification.getCountry() != null && lastCompletedQualification.getCountry().isDefaultCountry()
                && lastCompletedQualification.getSchoolLevel() != null
                && lastCompletedQualification.getSchoolLevel().isHigherEducation();
    }

    protected void validaGrauPrecedenteCompleto(final Unit institutionUnit, final ExecutionYear executionYear,
            final Registration registration, final Qualification lastCompletedQualification, final IGrauPrecedenteCompleto bean) {

        if (Strings.isNullOrEmpty(bean.getEscolaridadeAnterior())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.previous.complete.school.level.missing",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }

        if (Raides.NivelCursoOrigem.OUTRO.equals(Strings.isNullOrEmpty(bean.getOutroEscolaridadeAnterior()))) {
            if (Strings.isNullOrEmpty(bean.getOutroEscolaridadeAnterior())) {
                LegalReportContext.addError(
                        "",
                        i18n("error.Raides.validation.previous.complete.other.school.level.missing",
                                String.valueOf(registration.getStudent().getNumber()),
                                registration.getDegreeNameWithDescription(), executionYear.getQualifiedName()));
                bean.markAsInvalid();
            }
        }

        if (Strings.isNullOrEmpty(bean.getPaisEscolaridadeAnt())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.previous.complete.country.missing",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }

        if (Strings.isNullOrEmpty(bean.getAnoEscolaridadeAnt())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.previous.complete.year.missing",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));

            bean.markAsInvalid();
        }

        validaEstabelecimentoAnteriorCompleto(institutionUnit, executionYear, registration, lastCompletedQualification, bean);
        validaCursoAnteriorCompleto(institutionUnit, executionYear, registration, lastCompletedQualification, bean);

        if (!bean.isTipoEstabSecSpecified()) {
            return;
        }

        if (lastCompletedQualification.getSchoolLevel() != null
                && lastCompletedQualification.getSchoolLevel().isHighSchoolOrEquivalent()) {
            if (Strings.isNullOrEmpty(bean.getTipoEstabSec())) {
                LegalReportContext.addError(
                        "",
                        i18n("error.Raides.validation.previous.complete.highSchoolType.missing",
                                String.valueOf(registration.getStudent().getNumber()),
                                registration.getDegreeNameWithDescription(), executionYear.getQualifiedName()));
            }
        }
    }

    protected void validaEstabelecimentoAnteriorCompleto(final Unit institutionUnit, final ExecutionYear executionYear,
            final Registration registration, final Qualification lastCompletedQualification, final IGrauPrecedenteCompleto bean) {
        if (lastCompletedQualification.getCountry() == null || !lastCompletedQualification.getCountry().isDefaultCountry()) {
            return;
        }

        if (lastCompletedQualification.getSchoolLevel() == null
                || !lastCompletedQualification.getSchoolLevel().isHigherEducation()) {
            return;
        }

        if (Strings.isNullOrEmpty(bean.getEstabEscolaridadeAnt())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.previous.complete.institution.missing",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }

        if (Raides.Estabelecimentos.OUTRO.equals(bean.getEstabEscolaridadeAnt())
                && Strings.isNullOrEmpty(bean.getOutroEstabEscolarAnt())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.previous.complete.other.institution.missing",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
            bean.markAsInvalid();
        } else if (Raides.Estabelecimentos.OUTRO.equals(bean.getEstabEscolaridadeAnt())) {
            LegalReportContext.addWarn(
                    "",
                    i18n("warn.Raides.validation.previous.complete.other.institution.given.instead.of.code",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }
    }

    protected void validaCursoOficialInstituicaoOficial(final Unit institutionUnit, final ExecutionYear executionYear,
            final Registration registration, final Qualification lastCompletedQualification, final IGrauPrecedenteCompleto bean) {
        if (!isPrecedentDegreePortugueseHigherEducation(lastCompletedQualification)) {
            return;
        }

        if (Strings.isNullOrEmpty(lastCompletedQualification.getDegree())) {
            return;
        }

        final DegreeDesignation degreeDesignation =
                DegreeDesignation.readByNameAndSchoolLevel(lastCompletedQualification.getDegree(),
                        lastCompletedQualification.getSchoolLevel());

        if (degreeDesignation == null) {
            return;
        }

        boolean degreeDesignationContainsInstitution = false;
        for (final DegreeDesignation it : readByNameAndSchoolLevel(lastCompletedQualification.getDegree(),
                lastCompletedQualification.getSchoolLevel())) {
            degreeDesignationContainsInstitution |=
                    it.getInstitutionUnitSet().contains(lastCompletedQualification.getInstitution());
        }

        if (!degreeDesignationContainsInstitution) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.official.precedent.degree.is.not.offered.by.institution",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
        }
    }

    protected void validaCursoAnteriorCompleto(final Unit institutionUnit, final ExecutionYear executionYear,
            final Registration registration, final Qualification lastCompletedQualification, final IGrauPrecedenteCompleto bean) {
        if (lastCompletedQualification.getCountry() == null || !lastCompletedQualification.getCountry().isDefaultCountry()) {
            return;
        }

        if (lastCompletedQualification.getSchoolLevel() == null
                || !lastCompletedQualification.getSchoolLevel().isHigherEducation()) {
            return;
        }

        if (Strings.isNullOrEmpty(bean.getCursoEscolarAnt())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.previous.complete.degree.designation.missing",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }

        if (Raides.NivelCursoOrigem.OUTRO.equals(bean.getCursoEscolarAnt())
                && Strings.isNullOrEmpty(bean.getOutroCursoEscolarAnt())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.previous.complete.other.degree.designation.missing",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }

        if (isPrecedentDegreePortugueseHigherEducation(lastCompletedQualification)
                && Raides.Cursos.OUTRO.equals(bean.getCursoEscolarAnt())) {
            LegalReportContext
                    .addError(
                            "",
                            i18n("error.Raides.validation.previous.complete.other.degree.designation.set.even.if.level.is.portuguese.higher.education",
                                    String.valueOf(registration.getStudent().getNumber()),
                                    registration.getDegreeNameWithDescription(), executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }
    }

    protected Set<DegreeDesignation> readByNameAndSchoolLevel(String degreeDesignationName, SchoolLevelType schoolLevel) {
        if ((schoolLevel == null) || (degreeDesignationName == null)) {
            return null;
        }

        List<DegreeClassification> possibleClassifications = new ArrayList<DegreeClassification>();
        for (String code : schoolLevel.getEquivalentDegreeClassifications()) {
            possibleClassifications.add(DegreeClassification.readByCode(code));
        }

        List<DegreeDesignation> possibleDesignations = new ArrayList<DegreeDesignation>();
        for (DegreeClassification classification : possibleClassifications) {
            if (!classification.getDegreeDesignationsSet().isEmpty()) {
                possibleDesignations.addAll(classification.getDegreeDesignationsSet());
            }
        }

        Set<DegreeDesignation> result = Sets.newHashSet();
        for (DegreeDesignation degreeDesignation : possibleDesignations) {
            if (degreeDesignation.getDescription().equalsIgnoreCase(degreeDesignationName)) {
                result.add(degreeDesignation);
            }
        }

        return result;
    }

    protected void preencheInformacaoPessoal(final ExecutionYear executionYear, final Registration registration,
            final TblInscrito bean) {

        if (registration.getPerson().getMaritalStatus() != null) {
            bean.setEstadoCivil(LegalMapping.find(report, LegalMappingType.MARITAL_STATUS).translate(
                    registration.getPerson().getMaritalStatus()));
        }

        PersonalIngressionData personalIngressionData = Raides.personalIngressionData(registration, executionYear);
        if (personalIngressionData != null && personalIngressionData.getDislocatedFromPermanentResidence() != null) {
            bean.setAlunoDeslocado(LegalMapping.find(report, LegalMappingType.BOOLEAN).translate(
                    personalIngressionData.getDislocatedFromPermanentResidence()));
        }

        final Country countryOfResidence = Raides.countryOfResidence(registration, executionYear);
        final DistrictSubdivision districtSubdivision = Raides.districtSubdivisionOfResidence(registration, executionYear);
        if (countryOfResidence != null && districtSubdivision != null) {
            bean.setResideConcelho(districtSubdivision.getDistrict().getCode() + districtSubdivision.getCode());
        }

        if (personalIngressionData != null) {
            if (personalIngressionData.getFatherSchoolLevel() != null) {
                bean.setNivelEscolarPai(LegalMapping.find(report, LegalMappingType.SCHOOL_LEVEL).translate(
                        personalIngressionData.getFatherSchoolLevel()));
            }

            if (personalIngressionData.getMotherSchoolLevel() != null) {
                bean.setNivelEscolarMae(LegalMapping.find(report, LegalMappingType.SCHOOL_LEVEL).translate(
                        personalIngressionData.getMotherSchoolLevel()));
            }

            if (personalIngressionData.getFatherProfessionalCondition() != null) {
                bean.setSituacaoProfPai(LegalMapping.find(report, LegalMappingType.PROFESSIONAL_SITUATION_CONDITION).translate(
                        personalIngressionData.getFatherProfessionalCondition()));
            }

            if (personalIngressionData.getMotherProfessionalCondition() != null) {
                bean.setSituacaoProfMae(LegalMapping.find(report, LegalMappingType.PROFESSIONAL_SITUATION_CONDITION).translate(
                        personalIngressionData.getMotherProfessionalCondition()));
            }

            if (personalIngressionData.getProfessionalCondition() != null) {
                bean.setSituacaoProfAluno(LegalMapping.find(report, LegalMappingType.PROFESSIONAL_SITUATION_CONDITION).translate(
                        personalIngressionData.getProfessionalCondition()));
            }

            if (personalIngressionData.getFatherProfessionType() != null) {
                bean.setProfissaoPai(LegalMapping.find(report, LegalMappingType.PROFESSION_TYPE).translate(
                        personalIngressionData.getFatherProfessionType()));
            }

            if (personalIngressionData.getMotherProfessionType() != null) {
                bean.setProfissaoMae(LegalMapping.find(report, LegalMappingType.PROFESSION_TYPE).translate(
                        personalIngressionData.getMotherProfessionType()));
            }

            if (personalIngressionData.getProfessionType() != null) {
                bean.setProfissaoAluno(LegalMapping.find(report, LegalMappingType.PROFESSION_TYPE).translate(
                        personalIngressionData.getProfessionType()));
            }
        }

        validaInformacaoPessoal(executionYear, registration, bean);
    }

    protected void validaInformacaoPessoal(final ExecutionYear executionYear, final Registration registration,
            final TblInscrito bean) {

        if (Strings.isNullOrEmpty(bean.getEstadoCivil())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.maritalStatus.missing", String.valueOf(registration.getStudent().getNumber()),
                            registration.getDegreeNameWithDescription(), executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }

        if (Strings.isNullOrEmpty(bean.getAlunoDeslocado())) {
            LegalReportContext.addError(
                    "",
                    i18n("error.Raides.validation.dislocated.from.residence.missing",
                            String.valueOf(registration.getStudent().getNumber()), registration.getDegreeNameWithDescription(),
                            executionYear.getQualifiedName()));
            bean.markAsInvalid();
        }

        if (Raides.countryOfResidence(registration, executionYear) != null
                && Raides.countryOfResidence(registration, executionYear).isDefaultCountry()) {
            if (Strings.isNullOrEmpty(bean.getResideConcelho())) {
                LegalReportContext.addError(
                        "",
                        i18n("error.Raides.validation.district.subdivision.missing",
                                String.valueOf(registration.getStudent().getNumber()),
                                registration.getDegreeNameWithDescription(), executionYear.getQualifiedName()));
                bean.markAsInvalid();
            }

            if (!Strings.isNullOrEmpty(bean.getResideConcelho())
                    && (bean.getResideConcelho().startsWith("19") || bean.getResideConcelho().startsWith("22"))) {
                LegalReportContext.addError(
                        "",
                        i18n("error.Raides.validation.district.is.island.review",
                                String.valueOf(registration.getStudent().getNumber()),
                                registration.getDegreeNameWithDescription(), executionYear.getQualifiedName()));
                bean.markAsInvalid();
            }
        }

        if (!Strings.isNullOrEmpty(bean.getProfissaoAluno()) && Raides.Profissao.NAO_DISPONIVEL.equals(bean.getProfissaoAluno())) {
            // errors.addError("error.Raides.validation.student.profession.cannot.be.not.available", registration.getStudent()
            //        .getNumber(), registration.getDegree().getNameI18N().getContent(), executionYear.getQualifiedName());
        }

        if (Strings.isNullOrEmpty(bean.getNivelEscolarPai()) || Strings.isNullOrEmpty(bean.getNivelEscolarMae())
                || Strings.isNullOrEmpty(bean.getSituacaoProfPai()) || Strings.isNullOrEmpty(bean.getSituacaoProfMae())
                || Strings.isNullOrEmpty(bean.getSituacaoProfAluno()) || Strings.isNullOrEmpty(bean.getProfissaoPai())
                || Strings.isNullOrEmpty(bean.getProfissaoMae()) || Strings.isNullOrEmpty(bean.getProfissaoAluno())) {
            // errors.addError("error.Raides.validation.profesional.situation.missing", registration.getStudent().getNumber(),
            //        registration.getDegree().getNameI18N().getContent(), executionYear.getQualifiedName());
        }
    }

    protected String regimeFrequencia(final Registration registration, final ExecutionYear executionYear) {
        if (isOnlyEnrolledOnDissertation(registration, executionYear)) {
            return LegalMapping.find(report, LegalMappingType.REGIME_FREQUENCIA).translate(Raides.RegimeFrequencia.ETD_CODE);
        }

        return LegalMapping.find(report, LegalMappingType.REGIME_FREQUENCIA).translate(registration.getDegree().getExternalId());
    }

    protected BigDecimal enrolledEcts(final ExecutionYear executionYear, final Registration registration) {
        final StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(executionYear);
        double result = 0.0;

        for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
            if (enrolment.isValid(executionYear)) {
                result += enrolment.getEctsCredits();
            }
        }

        return new BigDecimal(result);
    }

    protected BigDecimal doctoralEnrolledEcts(final ExecutionYear executionYear, final Registration registration) {
        if (BigDecimal.ZERO.compareTo(enrolledEcts(executionYear, registration)) != 0) {
            BigDecimal enrolledEcts = enrolledEcts(executionYear, registration);
            
            CurricularCourse dissertation = phdDissertation(executionYear, registration);
            if(dissertation != null && enrolledEcts.compareTo(new BigDecimal(dissertation.getEctsCredits())) >= 0 ) {
                BigDecimal result = enrolledEcts.subtract(new BigDecimal(dissertation.getEctsCredits()));
                
                if(BigDecimal.ZERO.compareTo(result) == 0) {
                    return null;
                }
            }
            
            return enrolledEcts;
        }

        return null;
    }
    
    private CurricularCourse phdDissertation(final ExecutionYear executionYear, final Registration registration) {
        final StudentCurricularPlan studentCurricularPlan = registration.getStudentCurricularPlan(executionYear);

        Collection<CurricularCourse> allDissertationCurricularCourses =
                studentCurricularPlan.getDegreeCurricularPlan().getDissertationCurricularCourses(executionYear);

        for(final CurricularCourse dissertation : allDissertationCurricularCourses) {
            if(BigDecimal.ZERO.compareTo(new BigDecimal(dissertation.getCredits())) != 0) {
                return dissertation;
            }
        }
        
        return null;
    }

    public String i18n(String key, String... arguments) {
        return ULisboaSpecificationsUtil.bundle(key, arguments);
    }

}
