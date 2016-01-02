/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: diogo.simoes@qub-it.com
 *               jnpa@reitoria.ulisboa.pt
 *
 * 
 * This file is part of FenixEdu QubDocs.
 *
 * FenixEdu QubDocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu QubDocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu QubDocs.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.ulisboa.specifications.util;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academictreasury.util.Constants;
import org.fenixedu.bennu.FenixeduUlisboaSpecificationsSpringConfiguration;
import org.fenixedu.bennu.TupleDataSourceBean;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public class ULisboaConstants {

    public static final String BUNDLE = FenixeduUlisboaSpecificationsSpringConfiguration.BUNDLE.replace('/', '.');
    /*Empty justification used in AcademicServiceRequestSituation*/
    public static final LocalizedString EMPTY_JUSTIFICATION = BundleUtil.getLocalizedString(BUNDLE, "label.empty.justification");
    /*Codes used to identify static slots (ServiceRequestSlot)*/
    public static final String LANGUAGE = "language";
    public static final String DOCUMENT_PURPOSE_TYPE = "documentPurposeType";
    public static final String OTHER_DOCUMENT_PURPOSE = "otherDocumentPurpose";
    public static final String IS_DETAILED = "isDetailed";
    public static final String IS_URGENT = "isUrgent";
    public static final String CYCLE_TYPE = "cycleType";
    public static final String PROGRAM_CONCLUSION = "programConclusion";
    public static final String NUMBER_OF_UNITS = "numberOfUnits";
    public static final String NUMBER_OF_DAYS = "numberOfDays";
    public static final String NUMBER_OF_PAGES = "numberOfPages";
    public static final String EXECUTION_YEAR = "executionYear";
    public static final String CURRICULAR_PLAN = "curricularPlan";
    public static final String APPROVED_EXTRA_CURRICULUM = "approvedExtraCurriculum";
    public static final String APPROVED_STANDALONE_CURRICULUM = "approvedStandaloneCurriculum";
    public static final String APPROVED_ENROLMENTS = "approvedEnrolments";
    public static final String CURRICULUM = "curriculum";
    public static final String ENROLMENTS_BY_YEAR = "enrolmentsByYear";
    /*Slots that use DropDown Multiple as UI Component */
    public static final List<String> DROP_DOWN_MULTIPLE_DOMAIN_OBJECTS = Arrays.asList(APPROVED_EXTRA_CURRICULUM,
            APPROVED_STANDALONE_CURRICULUM, APPROVED_ENROLMENTS, CURRICULUM, ENROLMENTS_BY_YEAR);
    /*Slots that use DropDown Simple as UI Component */
    public static final List<String> DROP_DOWN_SINGLE_DOMAIN_OBJECTS =
            Arrays.asList(PROGRAM_CONCLUSION, DOCUMENT_PURPOSE_TYPE, EXECUTION_YEAR, CURRICULAR_PLAN);
    /*Slots which is values are stored as ICurriculumEntry */
    public static final List<String> ICURRICULUM_ENTRY_OBJECTS = Arrays.asList(APPROVED_EXTRA_CURRICULUM,
            APPROVED_STANDALONE_CURRICULUM, APPROVED_ENROLMENTS, CURRICULUM, ENROLMENTS_BY_YEAR);
    /*Slots used as default */
    public static final List<String> DEFAULT_PROPERTIES = Arrays.asList(LANGUAGE, EXECUTION_YEAR);
    /*Subset of AcademicServiceRequestSituationType. This are the valid states for the ULisboa Service Request */
    public static final List<AcademicServiceRequestSituationType> USED_SITUATION_TYPES =
            Arrays.asList(AcademicServiceRequestSituationType.NEW, AcademicServiceRequestSituationType.PROCESSING,
                    AcademicServiceRequestSituationType.CONCLUDED, AcademicServiceRequestSituationType.DELIVERED,
                    AcademicServiceRequestSituationType.CANCELLED, AcademicServiceRequestSituationType.REJECTED);
    /*Label for each ULisboa Service Request Processor */
    public static final String STATE_LOGGER_PROCESSOR = "label.StateLoggerProcessor.name";
    public static final String FILL_ENROLMENTS_BY_YEAR_PROPERTY_PROCESSOR =
            "label.FillRequestPropertyProcessor.EnrolmentsByYear.name";
    public static final String FILL_STANDALONE_CURRICULUM_PROPERTY_PROCESSOR =
            "label.FillRequestPropertyProcessor.StandAloneCurriculum.name";
    public static final String FILL_APPROVED_ENROLMENTS_PROPERTY_PROCESSOR =
            "label.FillRequestPropertyProcessor.ApprovedEnrolments.name";
    public static final String AUTOMATIC_ONLINE_REQUEST_PROCESSOR = "label.AutomaticOnlineRequestProcessor.name";
    public static final String PROGRAM_CONCLUSION_PROCESSOR = "label.ProgramConclusionProcessor.name";
    
    
    public static final Locale DEFAULT_LOCALE = new Locale("PT");
    
    public static final TupleDataSourceBean SELECT_OPTION =
            new TupleDataSourceBean("", BundleUtil.getString(Constants.BUNDLE, "label.TupleDataSourceBean.select.description"));
}
