/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: joao.roxo@qub-it.com 
 *               nuno.pinheiro@qub-it.com
 *
 * 
 * This file is part of FenixEdu Specifications.
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
package org.fenixedu.ulisboa.specifications.ui.firstTimeCandidacy;

import static org.fenixedu.bennu.FenixeduUlisboaSpecificationsSpringConfiguration.BUNDLE;
import static org.fenixedu.ulisboa.specifications.ui.firstTimeCandidacy.util.FiscalCodeValidation.isValidcontrib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GrantOwnerType;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.ProfessionType;
import org.fenixedu.academic.domain.ProfessionalSituationConditionType;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.domain.organizationalStructure.PartySocialSecurityNumber;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.academic.domain.person.Gender;
import org.fenixedu.academic.domain.person.IDDocumentType;
import org.fenixedu.academic.domain.person.MaritalStatus;
import org.fenixedu.academic.domain.raides.DegreeDesignation;
import org.fenixedu.academic.domain.student.PersonalIngressionData;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.ulisboa.specifications.ULisboaConfiguration;
import org.fenixedu.ulisboa.specifications.domain.PersonUlisboaSpecifications;
import org.fenixedu.ulisboa.specifications.domain.ProfessionTimeType;
import org.fenixedu.ulisboa.specifications.domain.candidacy.FirstTimeCandidacy;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import pt.ist.fenixframework.Atomic;

@BennuSpringController(value = FirstTimeCandidacyController.class)
@RequestMapping(PersonalInformationFormController.CONTROLLER_URL)
public class PersonalInformationFormController extends FirstTimeCandidacyAbstractController {

    private static final String IDENTITY_CARD_CONTROL_DIGIT_FORMAT = "[0-9]";

    private static final String CITZEN_CARD_CHECK_DIGIT_FORMAT = "[0-9][a-zA-Z][a-zA-Z][0-9]";

    private static final String SOCIAL_SECURITY_NUMBER_FORMAT = "\\d{9}";

    public static final String CONTROLLER_URL = "/fenixedu-ulisboa-specifications/firsttimecandidacy/personalinformationform";

    protected static final String _FILLPERSONALINFORMATION_URI = "/fillpersonalinformation";
    public static final String FILLPERSONALINFORMATION_URL = CONTROLLER_URL + _FILLPERSONALINFORMATION_URI;

    @RequestMapping
    public String home(Model model) {
        return "forward:" + getControllerURL() + _FILLPERSONALINFORMATION_URI;
    }

    @Override
    protected String getControllerURL() {
        return CONTROLLER_URL;
    }

    @RequestMapping(value = "/back", method = RequestMethod.GET)
    public String back(Model model, RedirectAttributes redirectAttributes) {
        return redirect(FirstTimeCandidacyController.CONTROLLER_URL, model, redirectAttributes);
    }

    @RequestMapping(value = _FILLPERSONALINFORMATION_URI, method = RequestMethod.GET)
    public String fillpersonalinformation(final Model model, final RedirectAttributes redirectAttributes) {
        if(isFormIsFilled(model)) {
            return nextScreen(model, redirectAttributes);
        }
        
        Optional<String> accessControlRedirect = accessControlRedirect(model, redirectAttributes);
        if (accessControlRedirect.isPresent()) {
            return accessControlRedirect.get();
        }
        model.addAttribute("genderValues", Gender.values());
        model.addAttribute("partial", isPartialUpdate());

        List<MaritalStatus> maritalStatusValues = new ArrayList<>();
        maritalStatusValues.addAll(Arrays.asList(MaritalStatus.values()));
        maritalStatusValues.remove(MaritalStatus.UNKNOWN);
        model.addAttribute("maritalStatusValues", maritalStatusValues);
        
        final List<Country> countryHighSchoolValues = Lists.newArrayList(Country.readDistinctCountries());
        Collections.sort(countryHighSchoolValues, Country.COMPARATOR_BY_NAME);
        model.addAttribute("countryHighSchoolValues", countryHighSchoolValues);

        FirstTimeCandidacy candidacy = FirstTimeCandidacyController.getCandidacy();
        if (candidacy != null) {
            model.addAttribute("placingOption", candidacy.getPlacingOption());
        }

        PersonalInformationForm form = fillFormIfRequired(model);
        model.addAttribute("personalInformationForm", form);

        List<IDDocumentType> idDocumentTypeValues = new ArrayList<>();
        idDocumentTypeValues.addAll(Arrays.asList(IDDocumentType.values()));
        idDocumentTypeValues.remove(IDDocumentType.CITIZEN_CARD);
        model.addAttribute("idDocumentTypeValues", idDocumentTypeValues);

        addInfoMessage(BundleUtil.getString(BUNDLE, "label.firstTimeCandidacy.fillPersonalInformation.info"), model);
        return "fenixedu-ulisboa-specifications/firsttimecandidacy/personalinformationform/fillpersonalinformation";
    }

    public PersonalInformationForm fillFormIfRequired(Model model) {
        Person person = AccessControl.getPerson();
        PersonalInformationForm form = (PersonalInformationForm) model.asMap().get("personalInformationForm");
        if (form != null) {
            if (!form.getIsForeignStudent()) {
                form.setDocumentIdNumber(person.getDocumentIdNumber());
                form.setIdDocumentType(person.getIdDocumentType());
            }
            model.addAttribute("personalInformationForm", form);
            return form;
        }

        form = new PersonalInformationForm();

        form.setDocumentIdEmissionLocation(person.getEmissionLocationOfDocumentId());
        YearMonthDay emissionDateOfDocumentIdYearMonthDay = person.getEmissionDateOfDocumentIdYearMonthDay();
        YearMonthDay expirationDateOfDocumentIdYearMonthDay = person.getExpirationDateOfDocumentIdYearMonthDay();
        form.setDocumentIdEmissionDate(emissionDateOfDocumentIdYearMonthDay != null ? new LocalDate(person
                .getEmissionDateOfDocumentIdYearMonthDay().toDateMidnight()) : null);
        form.setDocumentIdExpirationDate(expirationDateOfDocumentIdYearMonthDay != null ? new LocalDate(
                expirationDateOfDocumentIdYearMonthDay.toDateMidnight()) : null);

        form.setSocialSecurityNumber(person.getSocialSecurityNumber());
        form.setMaritalStatus(person.getMaritalStatus());

        PersonUlisboaSpecifications personUl = person.getPersonUlisboaSpecifications();
        if (personUl != null) {
            if (!StringUtils.equals(personUl.getDgesTempIdCode(), person.getDocumentIdNumber())) {
                form.setDocumentIdNumber(person.getDocumentIdNumber());
                form.setIdDocumentType(person.getIdDocumentType());
            }

            Unit institution = personUl.getFirstOptionInstitution();
            if (institution != null) {
                form.setFirstOptionInstitution(institution);

                String degreeDesignationName = personUl.getFirstOptionDegreeDesignation();
                Predicate<DegreeDesignation> matchesName = dd -> dd.getDescription().equalsIgnoreCase(degreeDesignationName);
                DegreeDesignation degreeDesignation =
                        institution.getDegreeDesignationSet().stream().filter(matchesName).findFirst().orElse(null);
                form.setFirstOptionDegreeDesignation(degreeDesignation);
            }
        } else {
            form.setDocumentIdNumber(person.getDocumentIdNumber());
            form.setIdDocumentType(person.getIdDocumentType());
        }

        PersonalIngressionData personalData = getPersonalIngressionData();

        form.setMaritalStatus(personalData.getMaritalStatus());
        if (form.getMaritalStatus() == null) {
            form.setMaritalStatus(MaritalStatus.SINGLE);
        }
        form.setCountryHighSchool(person.getCountryHighSchool());
        
        form.setFirstYearRegistration(false);
        for (final Registration registration : person.getStudent().getRegistrationsSet()) {
            if(!registration.isActive()) {
                continue;
            }
            
            if(registration.getRegistrationYear() != ExecutionYear.readCurrentExecutionYear()) {
                continue;
            }
            
            form.setFirstYearRegistration(true);
        }
        
        return form;
    }

    @RequestMapping(value = _FILLPERSONALINFORMATION_URI, method = RequestMethod.POST)
    public String fillpersonalinformation(PersonalInformationForm form, Model model, RedirectAttributes redirectAttributes) {
        Optional<String> accessControlRedirect = accessControlRedirect(model, redirectAttributes);
        if (accessControlRedirect.isPresent()) {
            return accessControlRedirect.get();
        }
        if (!validate(form, model)) {
            return fillpersonalinformation(model, redirectAttributes);
        }

        try {
            writeData(form);
            model.addAttribute("personalInformationForm", form);
            return nextScreen(model, redirectAttributes);
        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(BUNDLE, "label.error.create") + de.getLocalizedMessage(), model);
            LoggerFactory.getLogger(this.getClass()).error("Exception for user " + AccessControl.getPerson().getUsername());
            de.printStackTrace();
            return fillpersonalinformation(model, redirectAttributes);
        }
    }

    protected String nextScreen(Model model, RedirectAttributes redirectAttributes) {
        return redirect(FiliationFormController.FILLFILIATION_URL, model, redirectAttributes);
    }

    private boolean validate(PersonalInformationForm form, Model model) {
        
        final Set<String> result = validateForm(form, AccessControl.getPerson());

        for (final String message : result) {
            addErrorMessage(message, model);
        }
        
        return result.isEmpty();
    }

    public Set<String> validateForm(PersonalInformationForm form, final Person person) {
        final Set<String> result = Sets.newHashSet();
        if (!isPartialUpdate()) {
            IDDocumentType idType = form.getIdDocumentType();
            if (form.getIsForeignStudent()) {
                if (idType == null) {
                    result.add(BundleUtil.getString(BUNDLE, "error.documentIdType.required"));
                }

                if (StringUtils.isEmpty(form.getDocumentIdNumber())) {
                    result.add(BundleUtil.getString(BUNDLE, "error.documentIdNumber.required"));
                }
            }

            if (!form.getIsForeignStudent()) {
                if (StringUtils.isEmpty(form.getSocialSecurityNumber())
                        || !form.getSocialSecurityNumber().matches(SOCIAL_SECURITY_NUMBER_FORMAT)) {
                    result.add(BundleUtil.getString(BUNDLE,
                            "error.candidacy.workflow.PersonalInformationForm.incorrect.socialSecurityNumber"));
                }
            } else {
                if (!StringUtils.isEmpty(form.getSocialSecurityNumber())
                        && !form.getSocialSecurityNumber().matches(SOCIAL_SECURITY_NUMBER_FORMAT)) {
                    result.add(BundleUtil.getString(BUNDLE,
                            "error.candidacy.workflow.PersonalInformationForm.incorrect.socialSecurityNumber"));
                }
            }

            if (form.getDocumentIdExpirationDate() == null) {
                result.add(BundleUtil.getString(BUNDLE, "error.expirationDate.required"));
            }

            if (!StringUtils.isEmpty(form.getSocialSecurityNumber()) && !isValidcontrib(form.getSocialSecurityNumber())) {
                result.add(BundleUtil.getString(BUNDLE,
                        "error.candidacy.workflow.PersonalInformationForm.socialSecurityNumber.invalid"));
            }
            String defaultSocialSecurityNumber =
                    FenixEduAcademicConfiguration.getConfiguration().getDefaultSocialSecurityNumber();
            if (!defaultSocialSecurityNumber.equals(form.getSocialSecurityNumber())) {
                Party party = PartySocialSecurityNumber.readPartyBySocialSecurityNumber(form.getSocialSecurityNumber());
                if (party != null && party != person) {
                    result.add(BundleUtil.getString(BUNDLE,
                            "error.candidacy.workflow.PersonalInformationForm.socialSecurityNumber.already.exists"));
                }
            }
        }
        
        if(form.getCountryHighSchool() == null) {
            result.add(BundleUtil.getString(BUNDLE,
                    "error.candidacy.workflow.PersonalInformationForm.countryHighSchool.required"));
        }
        return result;
    }

    private boolean testsMode() {
        Boolean devMode = CoreConfiguration.getConfiguration().developmentMode();
        Boolean qualityMode = ULisboaConfiguration.getConfiguration().isQualityMode();
        return (devMode != null && devMode == true) || (qualityMode != null && qualityMode == true);
    }

    @Atomic
    private void writeData(PersonalInformationForm form) {
        Person person = AccessControl.getPerson();
        PersonUlisboaSpecifications personUl = PersonUlisboaSpecifications.findOrCreate(person);
        PersonalIngressionData personalData = getPersonalIngressionData();
        if (!isPartialUpdate()) {
            person.setEmissionLocationOfDocumentId(form.getDocumentIdEmissionLocation());
            LocalDate documentIdEmissionDate = form.getDocumentIdEmissionDate();
            LocalDate documentIdExpirationDate = form.getDocumentIdExpirationDate();
            person.setEmissionDateOfDocumentIdYearMonthDay(documentIdEmissionDate != null ? new YearMonthDay(
                    documentIdEmissionDate.toDate()) : null);
            person.setExpirationDateOfDocumentIdYearMonthDay(documentIdExpirationDate != null ? new YearMonthDay(
                    documentIdExpirationDate.toDate()) : null);

            String socialSecurityNumber = form.getSocialSecurityNumber();
            if (StringUtils.isEmpty(socialSecurityNumber)) {
                socialSecurityNumber = FenixEduAcademicConfiguration.getConfiguration().getDefaultSocialSecurityNumber();
            }
            person.setSocialSecurityNumber(socialSecurityNumber);
        }
        person.setMaritalStatus(form.getMaritalStatus());
        personalData.setMaritalStatus(form.getMaritalStatus());

        FirstTimeCandidacy candidacy = FirstTimeCandidacyController.getCandidacy();
        if (candidacy != null) {
            if (1 < candidacy.getPlacingOption()) {
                personUl.setFirstOptionInstitution(form.getFirstOptionInstitution());
                if (form.getFirstOptionDegreeDesignation() != null) {
                    personUl.setFirstOptionDegreeDesignation(form.getFirstOptionDegreeDesignation().getDescription());
                }
            }
        }

        if (!isPartialUpdate()) {
            if (form.getIsForeignStudent()) {
                person.setIdDocumentType(form.getIdDocumentType());
                person.setDocumentIdNumber(form.getDocumentIdNumber());
                personUl.setDgesTempIdCode("");
            }
        }
        
        person.setCountryHighSchool(form.getCountryHighSchool());
    }

    public boolean isPartialUpdate() {
        return false;
    }
    
    @Override
    protected boolean isFormIsFilled(final Model model) {
        return false;
    }

    public static class DegreeDesignationBean {
        private final String degreeDesignationText;
        private final String degreeDesignationId;

        public DegreeDesignationBean(String degreeDesignationText, String degreeDesignationId) {
            super();
            this.degreeDesignationText = degreeDesignationText;
            this.degreeDesignationId = degreeDesignationId;
        }

        public String getDegreeDesignationText() {
            return degreeDesignationText;
        }

        public String getDegreeDesignationId() {
            return degreeDesignationId;
        }
    }

    public static class PersonalInformationForm implements Serializable {
        private static final long serialVersionUID = 1L;

        //can be either the series number or the extra digit
        private String identificationDocumentSeriesNumber;
        private String documentIdEmissionLocation;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate documentIdEmissionDate;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate documentIdExpirationDate;
        private String socialSecurityNumber;
        private MaritalStatus maritalStatus;
        private ProfessionalSituationConditionType professionalCondition;
        private String profession;
        private ProfessionType professionType;
        private ProfessionTimeType professionTimeType;
        private GrantOwnerType grantOwnerType;
        private String grantOwnerProvider;

        private Unit firstOptionInstitution;
        private DegreeDesignation firstOptionDegreeDesignation;
        private String documentIdNumber;

        private IDDocumentType idDocumentType;

        private Country countryHighSchool;
        
        private boolean firstYearRegistration;

        public String getName() {
            return AccessControl.getPerson().getName();
        }

        public String getUsername() {
            return AccessControl.getPerson().getUsername();
        }

        public Gender getGender() {
            return AccessControl.getPerson().getGender();
        }

        public String getDocumentIdNumber() {
            return documentIdNumber;
        }

        public void setDocumentIdNumber(String documentIdNumber) {
            this.documentIdNumber = documentIdNumber;
        }

        public IDDocumentType getIdDocumentType() {
            return idDocumentType;
        }

        public void setIdDocumentType(IDDocumentType idDocumentType) {
            this.idDocumentType = idDocumentType;
        }

        public String getDocumentIdEmissionLocation() {
            return documentIdEmissionLocation;
        }

        public void setDocumentIdEmissionLocation(String documentIdEmissionLocation) {
            this.documentIdEmissionLocation = documentIdEmissionLocation;
        }

        public LocalDate getDocumentIdEmissionDate() {
            return documentIdEmissionDate;
        }

        public void setDocumentIdEmissionDate(LocalDate documentIdEmissionDate) {
            this.documentIdEmissionDate = documentIdEmissionDate;
        }

        public LocalDate getDocumentIdExpirationDate() {
            return documentIdExpirationDate;
        }

        public void setDocumentIdExpirationDate(LocalDate documentIdExpirationDate) {
            this.documentIdExpirationDate = documentIdExpirationDate;
        }

        public String getSocialSecurityNumber() {
            return socialSecurityNumber;
        }

        public void setSocialSecurityNumber(String socialSecurityNumber) {
            this.socialSecurityNumber = socialSecurityNumber;
        }

        public MaritalStatus getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(MaritalStatus maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public static long getSerialversionuid() {
            return serialVersionUID;
        }

        public Country getCountryHighSchool() {
            return countryHighSchool;
        }
        
        public void setCountryHighSchool(Country countryHighSchool) {
            this.countryHighSchool = countryHighSchool;
        }

        public Unit getFirstOptionInstitution() {
            return firstOptionInstitution;
        }

        public void setFirstOptionInstitution(Unit firstOptionInstitution) {
            this.firstOptionInstitution = firstOptionInstitution;
        }

        public DegreeDesignation getFirstOptionDegreeDesignation() {
            return firstOptionDegreeDesignation;
        }

        public void setFirstOptionDegreeDesignation(DegreeDesignation firstOptionDegreeDesignation) {
            this.firstOptionDegreeDesignation = firstOptionDegreeDesignation;
        }

        public boolean getIsForeignStudent() {
            Country nationality = AccessControl.getPerson().getCountry();
            return nationality == null || !nationality.isDefaultCountry();
        }
        
        public boolean isFirstYearRegistration() {
            return firstYearRegistration;
        }
        
        public void setFirstYearRegistration(boolean firstYearRegistration) {
            this.firstYearRegistration = firstYearRegistration;
        }
    }
}
