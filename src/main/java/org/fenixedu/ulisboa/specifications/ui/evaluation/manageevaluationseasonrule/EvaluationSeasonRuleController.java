/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: luis.egidio@qub-it.com
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
package org.fenixedu.ulisboa.specifications.ui.evaluation.manageevaluationseasonrule;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.bennu.spring.portal.BennuSpringController;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.BlockingTreasuryEventInDebt;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.EvaluationSeasonRule;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.EvaluationSeasonShiftType;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.EvaluationSeasonStatuteType;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.GradeScaleValidator;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.PreviousSeasonBlockingGrade;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.PreviousSeasonEvaluation;
import org.fenixedu.ulisboa.specifications.domain.evaluation.season.rule.PreviousSeasonMinimumGrade;
import org.fenixedu.ulisboa.specifications.dto.evaluation.season.EvaluationSeasonRuleBean;
import org.fenixedu.ulisboa.specifications.ui.FenixeduUlisboaSpecificationsBaseController;
import org.fenixedu.ulisboa.specifications.ui.evaluation.manageevaluationseason.EvaluationSeasonController;
import org.fenixedu.ulisboa.specifications.util.ULisboaSpecificationsUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;

@BennuSpringController(value = EvaluationSeasonController.class)
@RequestMapping(EvaluationSeasonRuleController.CONTROLLER_URL)
public class EvaluationSeasonRuleController extends FenixeduUlisboaSpecificationsBaseController {

    public static final String CONTROLLER_URL =
            "/fenixedu-ulisboa-specifications/evaluation/manageevaluationseasonrule/evaluationseasonrule";

    private static final String JSP_PATH = CONTROLLER_URL.substring(1);

    private String jspPage(final String page) {
        return JSP_PATH + "/" + page;
    }

    @RequestMapping
    public String home(final Model model) {
        return "forward:" + CONTROLLER_URL + "/";
    }

    private EvaluationSeasonRuleBean getEvaluationSeasonRuleBean(final Model model) {
        return (EvaluationSeasonRuleBean) model.asMap().get("evaluationSeasonRuleBean");
    }

    private void setEvaluationSeasonRuleBean(final EvaluationSeasonRuleBean bean, final Model model) {
        model.addAttribute("evaluationSeasonRuleBeanJson", getBeanJson(bean));
        model.addAttribute("evaluationSeasonRuleBean", bean);
    }

    private EvaluationSeasonRule getEvaluationSeasonRule(final Model model) {
        return (EvaluationSeasonRule) model.asMap().get("evaluationSeasonRule");
    }

    private void setEvaluationSeasonRule(final EvaluationSeasonRule evaluationSeasonRule, final Model model) {
        model.addAttribute("evaluationSeasonRule", evaluationSeasonRule);
    }

    private static final String _SEARCH_URI = "/";
    public static final String SEARCH_URL = CONTROLLER_URL + _SEARCH_URI;

    @RequestMapping(value = _SEARCH_URI + "{oid}")
    public String search(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model) {
        final EvaluationSeasonRuleBean bean = EvaluationSeasonRuleBean.creation(evaluationSeason, null);
        this.setEvaluationSeasonRuleBean(bean, model);

        List<EvaluationSeasonRule> searchevaluationseasonruleResultsDataSet = filterSearchEvaluationSeasonRule(evaluationSeason);

        model.addAttribute("searchevaluationseasonruleResultsDataSet", searchevaluationseasonruleResultsDataSet);
        return jspPage("search");
    }

    private List<EvaluationSeasonRule> filterSearchEvaluationSeasonRule(final EvaluationSeason evaluationSeason) {
        return evaluationSeason == null ? Lists.newArrayList() : evaluationSeason.getRulesSet().stream()
                .collect(Collectors.toList());
    }

    private static final String _SEARCH_TO_DELETE_URI = "/search/delete/";
    public static final String SEARCH_TO_DELETE_URL = CONTROLLER_URL + _SEARCH_TO_DELETE_URI;

    @RequestMapping(value = _SEARCH_TO_DELETE_URI + "{oid}", method = RequestMethod.POST)
    public String processSearchToDelete(@PathVariable("oid") final EvaluationSeasonRule rule, final Model model,
            final RedirectAttributes redirectAttributes) {

        final EvaluationSeason season = rule.getSeason();

        try {
            rule.delete();
            addInfoMessage(ULisboaSpecificationsUtil.bundle("label.success.delete"), model);

        } catch (Exception ex) {
            addErrorMessage(ex.getLocalizedMessage(), model);
        }

        return redirect(SEARCH_URL + season.getExternalId(), model, redirectAttributes);
    }

    private static final String _SEARCH_TO_UPDATE_URI = "/search/update/";
    public static final String SEARCH_TO_UPDATE_URL = CONTROLLER_URL + _SEARCH_TO_UPDATE_URI;

    @RequestMapping(value = _SEARCH_TO_UPDATE_URI + "{oid}", method = RequestMethod.GET)
    public String processSearchToUpdateRule(@PathVariable("oid") final EvaluationSeasonRule rule, final Model model,
            final RedirectAttributes redirectAttributes) {

        String updateRedirect = null;
        final Class<? extends EvaluationSeasonRule> clazz = rule.getClass();
        if (clazz.equals(PreviousSeasonBlockingGrade.class)) {
            updateRedirect = UPDATEPREVIOUSSEASONBLOCKINGGRADE_URL;
        } else if (clazz.equals(PreviousSeasonMinimumGrade.class)) {
            updateRedirect = UPDATEPREVIOUSSEASONMINIMUMGRADE_URL;
        } else if (clazz.equals(GradeScaleValidator.class)) {
            updateRedirect = UPDATEGRADESCALEVALIDATOR_URL;
        } else if (clazz.equals(EvaluationSeasonShiftType.class)) {
            updateRedirect = UPDATEEVALUATIONSEASONSHIFTTYPE_URL;
        } else if (clazz.equals(EvaluationSeasonStatuteType.class)) {
            updateRedirect = UPDATEEVALUATIONSEASONSTATUTETYPE_URL;
        }

        return redirect(updateRedirect + rule.getExternalId(), model, redirectAttributes);
    }

    private static final String _UPDATEPREVIOUSSEASONBLOCKINGGRADE_URI = "/updatepreviousseasonblockinggrade/";
    public static final String UPDATEPREVIOUSSEASONBLOCKINGGRADE_URL = CONTROLLER_URL + _UPDATEPREVIOUSSEASONBLOCKINGGRADE_URI;

    @RequestMapping(value = _UPDATEPREVIOUSSEASONBLOCKINGGRADE_URI + "{oid}", method = RequestMethod.GET)
    public String updatePreviousSeasonBlockingGrade(@PathVariable("oid") final PreviousSeasonBlockingGrade rule,
            final Model model) {
        setEvaluationSeasonRule(rule, model);

        final EvaluationSeasonRuleBean bean = EvaluationSeasonRuleBean.update(rule);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("updatepreviousseasonblockinggrade");
    }

    private static final String _UPDATEPREVIOUSSEASONBLOCKINGGRADEPOSTBACK_URI = "/updatepreviousseasonblockinggradepostback/";
    public static final String UPDATEPREVIOUSSEASONBLOCKINGGRADEPOSTBACK_URL =
            CONTROLLER_URL + _UPDATEPREVIOUSSEASONBLOCKINGGRADEPOSTBACK_URI;

    @RequestMapping(value = _UPDATEPREVIOUSSEASONBLOCKINGGRADEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updatePreviousSeasonBlockingGradepostback(
            @PathVariable("oid") final EvaluationSeasonRule rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATEPREVIOUSSEASONBLOCKINGGRADE_URI + "{oid}", method = RequestMethod.POST)
    public String updatePreviousSeasonBlockingGrade(@PathVariable("oid") final PreviousSeasonBlockingGrade rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {
        setEvaluationSeasonRule(rule, model);

        try {
            rule.edit(bean.getGrade());
            return redirect(SEARCH_URL + rule.getSeason().getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            setEvaluationSeasonRule(rule, model);
            this.setEvaluationSeasonRuleBean(bean, model);

            return jspPage("updatepreviousseasonblockinggrade");
        }
    }

    private static final String _UPDATEPREVIOUSSEASONMINIMUMGRADE_URI = "/updatepreviousseasonminimumgrade/";
    public static final String UPDATEPREVIOUSSEASONMINIMUMGRADE_URL = CONTROLLER_URL + _UPDATEPREVIOUSSEASONMINIMUMGRADE_URI;

    @RequestMapping(value = _UPDATEPREVIOUSSEASONMINIMUMGRADE_URI + "{oid}", method = RequestMethod.GET)
    public String updatePreviousSeasonMinimumGrade(@PathVariable("oid") final PreviousSeasonMinimumGrade rule,
            final Model model) {
        setEvaluationSeasonRule(rule, model);

        final EvaluationSeasonRuleBean bean = EvaluationSeasonRuleBean.update(rule);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("updatepreviousseasonminimumgrade");
    }

    private static final String _UPDATEPREVIOUSSEASONMINIMUMGRADEPOSTBACK_URI = "/updatepreviousseasonminimumgradepostback/";
    public static final String UPDATEPREVIOUSSEASONMINIMUMGRADEPOSTBACK_URL =
            CONTROLLER_URL + _UPDATEPREVIOUSSEASONMINIMUMGRADEPOSTBACK_URI;

    @RequestMapping(value = _UPDATEPREVIOUSSEASONMINIMUMGRADEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updatePreviousSeasonMinimumGradepostback(
            @PathVariable("oid") final EvaluationSeasonRule rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATEPREVIOUSSEASONMINIMUMGRADE_URI + "{oid}", method = RequestMethod.POST)
    public String updatePreviousSeasonMinimumGrade(@PathVariable("oid") final PreviousSeasonMinimumGrade rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {
        setEvaluationSeasonRule(rule, model);

        try {
            rule.edit(bean.getGrade());
            return redirect(SEARCH_URL + rule.getSeason().getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            setEvaluationSeasonRule(rule, model);
            this.setEvaluationSeasonRuleBean(bean, model);

            return jspPage("updatepreviousseasonminimumgrade");
        }
    }

    private static final String _UPDATEGRADESCALEVALIDATOR_URI = "/updategradescalevalidator/";
    public static final String UPDATEGRADESCALEVALIDATOR_URL = CONTROLLER_URL + _UPDATEGRADESCALEVALIDATOR_URI;

    @RequestMapping(value = _UPDATEGRADESCALEVALIDATOR_URI + "{oid}", method = RequestMethod.GET)
    public String updateGradeScaleValidator(@PathVariable("oid") final GradeScaleValidator rule, final Model model) {
        setEvaluationSeasonRule(rule, model);

        final EvaluationSeasonRuleBean bean = EvaluationSeasonRuleBean.update(rule);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("updategradescalevalidator");
    }

    private static final String _UPDATEGRADESCALEVALIDATORPOSTBACK_URI = "/updategradescalevalidatorpostback/";
    public static final String UPDATEGRADESCALEVALIDATORPOSTBACK_URL = CONTROLLER_URL + _UPDATEGRADESCALEVALIDATORPOSTBACK_URI;

    @RequestMapping(value = _UPDATEGRADESCALEVALIDATORPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updateGradeScaleValidatorpostback(
            @PathVariable("oid") final EvaluationSeasonRule rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATEGRADESCALEVALIDATOR_URI + "{oid}", method = RequestMethod.POST)
    public String updateGradeScaleValidator(@PathVariable("oid") final GradeScaleValidator rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {
        setEvaluationSeasonRule(rule, model);

        try {
            rule.edit(bean.getGradeScale(), bean.getGradeValues(), bean.getRuleDescription(),
                    bean.getAppliesToCurriculumAggregatorEntry(), bean.getDegreeTypes());
            return redirect(SEARCH_URL + rule.getSeason().getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            setEvaluationSeasonRule(rule, model);
            this.setEvaluationSeasonRuleBean(bean, model);

            return jspPage("updategradescalevalidator");
        }
    }

    private static final String _CREATEBLOCKINGTREASURYEVENTINDEBT_URI = "/createblockingtreasuryeventindebt/";
    public static final String CREATEBLOCKINGTREASURYEVENTINDEBT_URL = CONTROLLER_URL + _CREATEBLOCKINGTREASURYEVENTINDEBT_URI;

    @RequestMapping(value = _CREATEBLOCKINGTREASURYEVENTINDEBT_URI + "{oid}", method = RequestMethod.POST)
    public String createBlockingTreasuryEventInDebt(@PathVariable("oid") final EvaluationSeason season, final Model model,
            final RedirectAttributes redirectAttributes) {

        try {
            final EvaluationSeasonRule rule = BlockingTreasuryEventInDebt.create(season);
            model.addAttribute("evaluationSeasonRule", rule);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
        }

        return redirect(SEARCH_URL + season.getExternalId(), model, redirectAttributes);
    }

    private static final String _CREATEPREVIOUSSEASONEVALUATION_URI = "/createpreviousseasonevaluated/";
    public static final String CREATEPREVIOUSSEASONEVALUATION_URL = CONTROLLER_URL + _CREATEPREVIOUSSEASONEVALUATION_URI;

    @RequestMapping(value = _CREATEPREVIOUSSEASONEVALUATION_URI + "{oid}", method = RequestMethod.POST)
    public String createPreviousSeasonEvaluation(@PathVariable("oid") final EvaluationSeason season, final Model model,
            final RedirectAttributes redirectAttributes) {

        try {
            final EvaluationSeasonRule rule = PreviousSeasonEvaluation.create(season);
            model.addAttribute("evaluationSeasonRule", rule);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
        }

        return redirect(SEARCH_URL + season.getExternalId(), model, redirectAttributes);
    }

    private static final String _CREATEPREVIOUSSEASONBLOCKINGGRADE_URI = "/createpreviousseasonblockinggrade/";
    public static final String CREATEPREVIOUSSEASONBLOCKINGGRADE_URL = CONTROLLER_URL + _CREATEPREVIOUSSEASONBLOCKINGGRADE_URI;

    @RequestMapping(value = _CREATEPREVIOUSSEASONBLOCKINGGRADE_URI + "{oid}", method = RequestMethod.GET)
    public String createPreviousSeasonBlockingGrade(@PathVariable("oid") final EvaluationSeason evaluationSeason,
            final Model model) {

        final EvaluationSeasonRuleBean bean =
                EvaluationSeasonRuleBean.creation(evaluationSeason, PreviousSeasonBlockingGrade.class);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("createpreviousseasonblockinggrade");
    }

    private static final String _CREATEPREVIOUSSEASONBLOCKINGGRADEPOSTBACK_URI = "/createpreviousseasonblockinggradepostback/";
    public static final String CREATEPREVIOUSSEASONBLOCKINGGRADEPOSTBACK_URL =
            CONTROLLER_URL + _CREATEPREVIOUSSEASONBLOCKINGGRADEPOSTBACK_URI;

    @RequestMapping(value = _CREATEPREVIOUSSEASONBLOCKINGGRADEPOSTBACK_URI, method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createPreviousSeasonBlockingGradepostback(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATEPREVIOUSSEASONBLOCKINGGRADE_URI, method = RequestMethod.POST)
    public String createPreviousSeasonBlockingGrade(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            RedirectAttributes redirectAttributes) {

        try {

            final EvaluationSeasonRule rule = PreviousSeasonBlockingGrade.create(bean.getSeason(), bean.getGrade());
            model.addAttribute("evaluationSeasonRule", rule);
            return redirect(SEARCH_URL + getEvaluationSeasonRule(model).getSeason().getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            this.setEvaluationSeasonRuleBean(bean, model);
            return jspPage("createpreviousseasonblockinggrade");
        }
    }

    private static final String _CREATEPREVIOUSSEASONMINIMUMGRADE_URI = "/createpreviousseasonminimumgrade/";
    public static final String CREATEPREVIOUSSEASONMINIMUMGRADE_URL = CONTROLLER_URL + _CREATEPREVIOUSSEASONMINIMUMGRADE_URI;

    @RequestMapping(value = _CREATEPREVIOUSSEASONMINIMUMGRADE_URI + "{oid}", method = RequestMethod.GET)
    public String createPreviousSeasonMinimumGrade(@PathVariable("oid") final EvaluationSeason evaluationSeason,
            final Model model) {

        final EvaluationSeasonRuleBean bean =
                EvaluationSeasonRuleBean.creation(evaluationSeason, PreviousSeasonMinimumGrade.class);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("createpreviousseasonminimumgrade");
    }

    private static final String _CREATEPREVIOUSSEASONMINIMUMGRADEPOSTBACK_URI = "/createpreviousseasonminimumgradepostback/";
    public static final String CREATEPREVIOUSSEASONMINIMUMGRADEPOSTBACK_URL =
            CONTROLLER_URL + _CREATEPREVIOUSSEASONMINIMUMGRADEPOSTBACK_URI;

    @RequestMapping(value = _CREATEPREVIOUSSEASONMINIMUMGRADEPOSTBACK_URI, method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createPreviousSeasonMinimumGradepostback(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATEPREVIOUSSEASONMINIMUMGRADE_URI, method = RequestMethod.POST)
    public String createPreviousSeasonMinimumGrade(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            RedirectAttributes redirectAttributes) {

        try {

            final EvaluationSeasonRule rule = PreviousSeasonMinimumGrade.create(bean.getSeason(), bean.getGrade());
            model.addAttribute("evaluationSeasonRule", rule);
            return redirect(SEARCH_URL + getEvaluationSeasonRule(model).getSeason().getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            this.setEvaluationSeasonRuleBean(bean, model);
            return jspPage("createpreviousseasonminimumgrade");
        }
    }

    private static final String _CREATEGRADESCALEVALIDATOR_URI = "/creategradescalevalidator/";
    public static final String CREATEGRADESCALEVALIDATOR_URL = CONTROLLER_URL + _CREATEGRADESCALEVALIDATOR_URI;

    @RequestMapping(value = _CREATEGRADESCALEVALIDATOR_URI + "{oid}", method = RequestMethod.GET)
    public String createGradeScaleValidator(@PathVariable("oid") final EvaluationSeason evaluationSeason, final Model model) {

        final EvaluationSeasonRuleBean bean = EvaluationSeasonRuleBean.creation(evaluationSeason, GradeScaleValidator.class);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("creategradescalevalidator");
    }

    private static final String _CREATEGRADESCALEVALIDATORPOSTBACK_URI = "/creategradescalevalidatorpostback/";
    public static final String CREATEGRADESCALEVALIDATORPOSTBACK_URL = CONTROLLER_URL + _CREATEGRADESCALEVALIDATORPOSTBACK_URI;

    @RequestMapping(value = _CREATEGRADESCALEVALIDATORPOSTBACK_URI, method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createGradeScaleValidatorpostback(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATEGRADESCALEVALIDATOR_URI, method = RequestMethod.POST)
    public String createGradeScaleValidator(@RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean,
            final Model model, RedirectAttributes redirectAttributes) {

        try {

            final EvaluationSeasonRule rule =
                    GradeScaleValidator.create(bean.getSeason(), bean.getGradeScale(), bean.getGradeValues(),
                            bean.getRuleDescription(), bean.getAppliesToCurriculumAggregatorEntry(), bean.getDegreeTypes());
            model.addAttribute("evaluationSeasonRule", rule);
            return redirect(SEARCH_URL + getEvaluationSeasonRule(model).getSeason().getExternalId(), model, redirectAttributes);

        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            this.setEvaluationSeasonRuleBean(bean, model);
            return jspPage("creategradescalevalidator");
        }
    }

    private static final String _UPDATEEVALUATIONSEASONSHIFTTYPE_URI = "/updateevaluationseasonshifttype/";
    public static final String UPDATEEVALUATIONSEASONSHIFTTYPE_URL = CONTROLLER_URL + _UPDATEEVALUATIONSEASONSHIFTTYPE_URI;

    @RequestMapping(value = _UPDATEEVALUATIONSEASONSHIFTTYPE_URI + "{oid}", method = RequestMethod.GET)
    public String updateEvaluationSeasonShiftType(@PathVariable("oid") final EvaluationSeasonShiftType rule, final Model model) {
        setEvaluationSeasonRule(rule, model);

        final EvaluationSeasonRuleBean bean = EvaluationSeasonRuleBean.update(rule);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("updateevaluationseasonshifttype");
    }

    private static final String _UPDATEEVALUATIONSEASONSHIFTTYPEPOSTBACK_URI = "/updateevaluationseasonshifttypepostback/";
    public static final String UPDATEEVALUATIONSEASONSHIFTTYPEPOSTBACK_URL =
            CONTROLLER_URL + _UPDATEEVALUATIONSEASONSHIFTTYPEPOSTBACK_URI;

    @RequestMapping(value = _UPDATEEVALUATIONSEASONSHIFTTYPEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updateEvaluationSeasonShiftTypepostback(
            @PathVariable("oid") final EvaluationSeasonRule rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATEEVALUATIONSEASONSHIFTTYPE_URI + "{oid}", method = RequestMethod.POST)
    public String updateEvaluationSeasonShiftType(@PathVariable("oid") final EvaluationSeasonShiftType rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {
        setEvaluationSeasonRule(rule, model);

        try {
            rule.edit(bean.getShiftTypes());
            return redirect(SEARCH_URL + rule.getSeason().getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            setEvaluationSeasonRule(rule, model);
            this.setEvaluationSeasonRuleBean(bean, model);

            return jspPage("updateevaluationseasonshifttype");
        }
    }

    private static final String _CREATEEVALUATIONSEASONSHIFTTYPE_URI = "/createevaluationseasonshifttype/";
    public static final String CREATEEVALUATIONSEASONSHIFTTYPE_URL = CONTROLLER_URL + _CREATEEVALUATIONSEASONSHIFTTYPE_URI;

    @RequestMapping(value = _CREATEEVALUATIONSEASONSHIFTTYPE_URI + "{oid}", method = RequestMethod.GET)
    public String createEvaluationSeasonShiftType(@PathVariable("oid") final EvaluationSeason evaluationSeason,
            final Model model) {

        final EvaluationSeasonRuleBean bean =
                EvaluationSeasonRuleBean.creation(evaluationSeason, EvaluationSeasonShiftType.class);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("createevaluationseasonshifttype");
    }

    private static final String _CREATEEVALUATIONSEASONSHIFTTYPEPOSTBACK_URI = "/createevaluationseasonshifttypepostback/";
    public static final String CREATEEVALUATIONSEASONSHIFTTYPEPOSTBACK_URL =
            CONTROLLER_URL + _CREATEEVALUATIONSEASONSHIFTTYPEPOSTBACK_URI;

    @RequestMapping(value = _CREATEEVALUATIONSEASONSHIFTTYPEPOSTBACK_URI, method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createEvaluationSeasonShiftTypepostback(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATEEVALUATIONSEASONSHIFTTYPE_URI, method = RequestMethod.POST)
    public String createEvaluationSeasonShiftType(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            RedirectAttributes redirectAttributes) {

        try {

            final EvaluationSeasonRule rule = EvaluationSeasonShiftType.create(bean.getSeason(), bean.getShiftTypes());
            model.addAttribute("evaluationSeasonRule", rule);
            return redirect(SEARCH_URL + getEvaluationSeasonRule(model).getSeason().getExternalId(), model, redirectAttributes);

        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            this.setEvaluationSeasonRuleBean(bean, model);
            return jspPage("createevaluationseasonshifttype");
        }
    }

    private static final String _UPDATEEVALUATIONSEASONSTATUTETYPE_URI = "/updateevaluationseasonstatutetype/";
    public static final String UPDATEEVALUATIONSEASONSTATUTETYPE_URL = CONTROLLER_URL + _UPDATEEVALUATIONSEASONSTATUTETYPE_URI;

    @RequestMapping(value = _UPDATEEVALUATIONSEASONSTATUTETYPE_URI + "{oid}", method = RequestMethod.GET)
    public String updateEvaluationSeasonStatuteType(@PathVariable("oid") final EvaluationSeasonStatuteType rule,
            final Model model) {
        setEvaluationSeasonRule(rule, model);

        final EvaluationSeasonRuleBean bean = EvaluationSeasonRuleBean.update(rule);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("updateevaluationseasonstatutetype");
    }

    private static final String _UPDATEEVALUATIONSEASONSTATUTETYPEPOSTBACK_URI = "/updateevaluationseasonstatutetypepostback/";
    public static final String UPDATEEVALUATIONSEASONSTATUTETYPEPOSTBACK_URL =
            CONTROLLER_URL + _UPDATEEVALUATIONSEASONSTATUTETYPEPOSTBACK_URI;

    @RequestMapping(value = _UPDATEEVALUATIONSEASONSTATUTETYPEPOSTBACK_URI + "{oid}", method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> updateEvaluationSeasonStatuteTypepostback(
            @PathVariable("oid") final EvaluationSeasonRule rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _UPDATEEVALUATIONSEASONSTATUTETYPE_URI + "{oid}", method = RequestMethod.POST)
    public String updateEvaluationSeasonStatuteType(@PathVariable("oid") final EvaluationSeasonStatuteType rule,
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            final RedirectAttributes redirectAttributes) {
        setEvaluationSeasonRule(rule, model);

        try {
            rule.edit(bean.getStatuteTypes());
            return redirect(SEARCH_URL + rule.getSeason().getExternalId(), model, redirectAttributes);
        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            setEvaluationSeasonRule(rule, model);
            this.setEvaluationSeasonRuleBean(bean, model);

            return jspPage("updateevaluationseasonstatutetype");
        }
    }

    private static final String _CREATEEVALUATIONSEASONSTATUTETYPE_URI = "/createevaluationseasonstatutetype/";
    public static final String CREATEEVALUATIONSEASONSTATUTETYPE_URL = CONTROLLER_URL + _CREATEEVALUATIONSEASONSTATUTETYPE_URI;

    @RequestMapping(value = _CREATEEVALUATIONSEASONSTATUTETYPE_URI + "{oid}", method = RequestMethod.GET)
    public String createEvaluationSeasonStatuteType(@PathVariable("oid") final EvaluationSeason evaluationSeason,
            final Model model) {

        final EvaluationSeasonRuleBean bean =
                EvaluationSeasonRuleBean.creation(evaluationSeason, EvaluationSeasonStatuteType.class);
        this.setEvaluationSeasonRuleBean(bean, model);

        return jspPage("createevaluationseasonstatutetype");
    }

    private static final String _CREATEEVALUATIONSEASONSTATUTETYPEPOSTBACK_URI = "/createevaluationseasonstatutetypepostback/";
    public static final String CREATEEVALUATIONSEASONSTATUTETYPEPOSTBACK_URL =
            CONTROLLER_URL + _CREATEEVALUATIONSEASONSTATUTETYPEPOSTBACK_URI;

    @RequestMapping(value = _CREATEEVALUATIONSEASONSTATUTETYPEPOSTBACK_URI, method = RequestMethod.POST,
            produces = "application/json;charset=UTF-8")
    public @ResponseBody ResponseEntity<String> createEvaluationSeasonStatuteTypepostback(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model) {

        this.setEvaluationSeasonRuleBean(bean, model);
        return new ResponseEntity<String>(getBeanJson(bean), HttpStatus.OK);
    }

    @RequestMapping(value = _CREATEEVALUATIONSEASONSTATUTETYPE_URI, method = RequestMethod.POST)
    public String createEvaluationSeasonStatuteType(
            @RequestParam(value = "bean", required = false) final EvaluationSeasonRuleBean bean, final Model model,
            RedirectAttributes redirectAttributes) {

        try {

            final EvaluationSeasonRule rule = EvaluationSeasonStatuteType.create(bean.getSeason(), bean.getStatuteTypes());
            model.addAttribute("evaluationSeasonRule", rule);
            return redirect(SEARCH_URL + getEvaluationSeasonRule(model).getSeason().getExternalId(), model, redirectAttributes);

        } catch (Exception de) {

            addErrorMessage(de.getLocalizedMessage(), model);
            this.setEvaluationSeasonRuleBean(bean, model);
            return jspPage("createevaluationseasonstatutetype");
        }
    }

}
