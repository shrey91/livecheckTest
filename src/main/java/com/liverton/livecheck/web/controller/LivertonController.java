package com.liverton.livecheck.web.controller;

import com.liverton.livecheck.boot.config.Application;
import com.liverton.livecheck.dao.model.*;
import com.liverton.livecheck.dao.repository.*;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.service.*;
import com.liverton.livecheck.service.impl.SiteServiceImpl;
import com.liverton.livecheck.validator.UserValidator;
import com.liverton.livecheck.web.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by sshah on 8/08/2016.
 */

@Controller
@SessionAttributes("newSite")
public class LivertonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LivertonController.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SiteService service;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private OrganisationService organisationService;

    @Autowired
    private OrganisationRepository organisationRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    SitePingResultRepository sitePingResultRepository;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }


    @RequestMapping("welcome")
    public String welcomeForm() {
        return "welcome";
    }


    @RequestMapping(value = "/newSite", method = RequestMethod.GET)
    public String newSite(Model model) {
        SiteModelForm sitemodel = new SiteModelForm();
        List<Organisation> organisations = organisationService.getAllOrganisations();
        model.addAttribute("organisations", organisations);
        model.addAttribute("siteModelForm", sitemodel);
        return "site/newSite";
    }

    @RequestMapping(value = "/newSite", method = RequestMethod.POST)
    public String newSite(@ModelAttribute @Valid SiteModelForm siteModelForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Site Form contains errors - {}", bindingResult.getAllErrors());
            return "newSite";
        } else {
            LOGGER.info("Validation successful - {}", siteModelForm);

            NotificationAction notificationAction = null;
            if (notificationAction == null) {
                siteModelForm.setAction(NotificationAction.TEXT_AND_EMAIL);
                siteModelForm.setAverageResponse("0ms");
                siteModelForm.setState(SiteState.OKAY);

            }
            Site site = service.newSite(siteModelForm.getSiteName(), siteModelForm.getEnabled(), siteModelForm.getIpAddress(), siteModelForm.getState(), siteModelForm.getAction(), siteModelForm.getAverageResponse(), siteModelForm.getOrganisation());
            model.addAttribute("newSite", site);
            return "redirect:/displaySite";
        }
    }

    @RequestMapping(value = "/displaySite", method = RequestMethod.GET)
    public String displayNewSite(@ModelAttribute("newSite") Site newSite, Model model) {
        model.addAttribute("newSite", newSite);
        return "site/submitForm";
    }

    @RequestMapping("/existingSites")
    public String existingSites(Model model) {
        model.addAttribute("existingSite", siteRepository.findAllByOrderBySiteName());
        return "site/existingSites";
    }

    @RequestMapping("/deleteSite")
    public String deleteSite(@RequestParam("id") Long id) {
        siteRepository.delete(id);
        return "redirect:/siteMonitor";
    }

    @RequestMapping("/editSite")
    public String editSite(@RequestParam("id") Long id, Model model) {
        Site site = siteRepository.findOne(id);
        model.addAttribute("siteModelForm", new SiteModelForm(site.getSiteName(), site.getEnabled(), site.getIpAddress(), null, null, site.getAction(), null, site.getApplicationStatus(), site.getOrganisation(), site.getState(), site.getAverageResponse(), site.getId()));
        model.addAttribute("siteState", site.getState());
        return "site/editSite";

    }


    @RequestMapping("/viewPingResult")
    public String viewPingResult(@RequestParam("id") Long id, Model model) {
        Site site = siteRepository.findOne(id);
        model.addAttribute("pingResults", sitePingResultRepository.findTop10BySiteOrderByDateDesc(site));
        model.addAttribute("siteName", site.getSiteName());


        return "site/viewPingResult";

    }

    @RequestMapping("/viewOrganisationSites")
    public String viewOrganisationSites(@RequestParam("id") Long id, Model model) {
        model.addAttribute("site", organisationRepository.findOne(id));
        return "organisation/viewOrganisationSites";

    }


    @RequestMapping(value = "editSite", method = RequestMethod.POST)
    public String editSite(@Valid SiteModelForm editSiteModelForm, BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Item not found -{}", bindingResult.getAllErrors());
            return "editSite";
        } else {
            Site site = siteRepository.findOne(editSiteModelForm.getId());
            site.setDate(new Date());
            site.setEnabled(editSiteModelForm.getEnabled());
            site.setIpAddress(editSiteModelForm.getIpAddress());
            site.setSiteName(editSiteModelForm.getSiteName());
            site.setState(editSiteModelForm.getState());
            updateApplicationStatus(site,editSiteModelForm);
            siteRepository.save(site);
            return "redirect:/existingSites";
        }
    }

    private void updateApplicationStatus(Site site, SiteModelForm siteModelForm) {
        for (ApplicationStatus applicationStatus : site.getApplicationStatus()) {
            for (ApplicationStatus submitted : siteModelForm.getApplicationStatus()) {
                if (applicationStatus.getApplicationType().equals(submitted.getApplicationType())) {
                    applicationStatus.setEnabled(submitted.getEnabled() != null ? submitted.getEnabled() : false);
                }
            }
        }
    }

    @RequestMapping("users")
    public String users(Model model) {
        model.addAttribute("existingUser", userRepository.findAll());
        return "user/users";
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.GET)
    public String newUser(Model model) {
        User user = new User();
        List<Authority> authorities = authorityService.findRoles();
        model.addAttribute("authorities", authorities);
        model.addAttribute("userForm", new UserForm());
        return "user/newUser";
    }

    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public String newUser(@ModelAttribute @Valid UserForm userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("User Form contains errors - {}", bindingResult.getAllErrors());
            return "newSite";
        } else {
            LOGGER.info("Validation successful - {}", userForm);
            User user = new User(userForm.getName(), passwordEncoder.encode(userForm.getPassword()), userForm.getEnabled());
            user.getAuthorityList().add(authorityRepository.findByRole("ROLE_USER"));
            User persisted = userRepository.save(user);
            model.addAttribute("newSite", persisted);
            return "redirect:/displayUser";
        }
    }

    @RequestMapping("/editUser")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userRepository.findOne(id);
        model.addAttribute("editUserForm", new EditUserForm(user.getName(), user.getPassword(), user.getUserEnabled(), user.getId()));
        return "user/editUser";

    }

    @RequestMapping(value = "/editUser", method = RequestMethod.POST)
    public String editSite(@Valid EditUserForm editUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Item not found -{}", bindingResult.getAllErrors());
            return "editUser";
        } else {
            User user = userRepository.findOne(editUserForm.getId());
            user.setName(editUserForm.getName());
            user.setPassword(passwordEncoder.encode(editUserForm.getPassword()));
            user.setUserEnabled(editUserForm.getEnabled());
            userRepository.save(user);
            return "redirect:/users";
        }
    }

    @RequestMapping(value = "/displayUser", method = RequestMethod.GET)
    public String displayUser(@ModelAttribute("newSite") User newSite, Model model) {
        model.addAttribute("newSite", newSite);
        return "user/submitUser";
    }

    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userRepository.delete(id);
        return "redirect:user/users";
    }

    @RequestMapping("organisations")
    public String organisations(Model model) {
        model.addAttribute("existingOrganisations", organisationRepository.findAll());
        return "organisation/organisations";
    }

    @RequestMapping(value = "/newOrganisation", method = RequestMethod.GET)
    public String newOrganisation(Model model) {
        model.addAttribute("organisationForm", new OrganisationForm());
        return "organisation/newOrganisation";
    }

    @RequestMapping(value = "/newOrganisation", method = RequestMethod.POST)
    public String newUser(@ModelAttribute @Valid OrganisationForm organisationForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("User Form contains errors - {}", bindingResult.getAllErrors());
            return "newSite";
        } else {
            LOGGER.info("Validation successful - {}", organisationForm);
            Organisation save3 = organisationRepository.save(new Organisation(organisationForm.getOrgName(), organisationForm.getDescription(),organisationForm.getFromEmail(),organisationForm.getToEmail(),organisationForm.getHost(),organisationForm.getPortNumber(),organisationForm.getTextDestination()));
            model.addAttribute("newSite", save3);
            return "redirect:/displayOrg";
        }
    }

    @RequestMapping(value = "/displayOrg", method = RequestMethod.GET)
    public String displayOrg(@ModelAttribute("newSite") Organisation newSite, Model model) {
        model.addAttribute("newSite", newSite);
        return "organisation/submitOrg";
    }

    @RequestMapping("/deleteOrg")
    public String deleteOrg(@RequestParam("id") Long id) {
        organisationRepository.delete(id);
        return "redirect:/organisations";
    }

    @RequestMapping("/editOrg")
    public String editOrg(@RequestParam("id") Long id, Model model) {
        Organisation organisation = organisationRepository.findOne(id);
        model.addAttribute("organisationForm", new EditOrganisationForm(organisation.getOrgName(), organisation.getDescription(),organisation.getFromEmail(),organisation.getToEmail(),organisation.getHost(),organisation.getPortNumber(),organisation.getTextDestination(), organisation.getId()));
        model.addAttribute("sites", organisation.getSites());
        return "organisation/editOrg";

    }

    @RequestMapping(value = "/editOrg", method = RequestMethod.POST)
    public String editOrg(@Valid EditOrganisationForm editOrganisationForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Item not found -{}", bindingResult.getAllErrors());
            return "editOrg";
        } else {
            Organisation organisation = organisationRepository.findOne(editOrganisationForm.getId());
            organisation.setOrgName(editOrganisationForm.getOrgName());
            organisation.setDescription(editOrganisationForm.getDescription());
            organisation.setFromEmail(editOrganisationForm.getFromEmail());
            organisation.setHost(editOrganisationForm.getHost());
            organisation.setPortNumber(editOrganisationForm.getPortNumber());
            organisation.setTextDestination(editOrganisationForm.getTextDestination());
            organisation.setToEmail(editOrganisationForm.getToEmail());

            organisationRepository.save(organisation);
            return "redirect:/organisations";
        }
    }

    @RequestMapping("authorities")
    public String authorities(Model model) {
        model.addAttribute("existingAuth", authorityRepository.findAll());
        return "authority/authorities";
    }

    @RequestMapping(value = "/addAuth", method = RequestMethod.GET)
    public String newAuth(Model model) {
        model.addAttribute("authorityForm", new AuthorityForm());
        return "authority/addAuth";
    }

    @RequestMapping(value = "/addAuth", method = RequestMethod.POST)
    public String newAuth(@ModelAttribute @Valid AuthorityForm authorityForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Authority contains errors - {}", bindingResult.getAllErrors());
            return "newSite";
        } else {
            LOGGER.info("Validation successful - {}", authorityForm);
            Authority save4 = authorityRepository.save(new Authority(authorityForm.getRole(), authorityForm.getDescription()));
            model.addAttribute("newSite", save4);
            return "redirect:/displayAuth";
        }
    }

    @RequestMapping(value = "/displayAuth", method = RequestMethod.GET)
    public String displayAuth(@ModelAttribute("newSite") Authority newSite, Model model) {
        model.addAttribute("newSite", newSite);
        return "authority/submitAuth";
    }

    @RequestMapping("/deleteAuth")
    public String deleteAuth(@RequestParam("id") Long id) {
        authorityRepository.delete(id);
        return "redirect:/authorities";
    }

    @RequestMapping("/editAuth")
    public String editAuth(@RequestParam("id") Long id, Model model) {
        Authority authority = authorityRepository.findOne(id);
        model.addAttribute("authorityForm", new EditAuthorityForm(authority.getRole(), authority.getDescription(), authority.getId()));
        return "authority/editAuth";

    }

    @RequestMapping(value = "/editAuth", method = RequestMethod.POST)
    public String editOrg(@Valid EditAuthorityForm editAuthorityForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Item not found -{}", bindingResult.getAllErrors());
            return "editAuth";
        } else {
            Authority authority = authorityRepository.findOne(editAuthorityForm.getId());
            authority.setRole(editAuthorityForm.getRole());
            authority.setDescription(editAuthorityForm.getDescription());
            authorityRepository.save(authority);
            return "redirect:/authorities";
        }
    }

    @RequestMapping("/siteMonitor")
    public String monitorSites(Model model) {
        model.addAttribute("organisations", organisationRepository.findAll());
//        model.addAttribute("siteMonitor", siteRepository.findAllByOrderBySiteName());
        return "monitor/siteMonitor";
    }

    @RequestMapping("/siteDisable")
    public String disableSite(@RequestParam("id") Long id) {
        Site site = siteRepository.findOne(id);
        site.setEnabled(false);
        site.setState(SiteState.DISABLED);
        siteRepository.save(site);
        return "redirect:/existingSites";
    }

    @RequestMapping("/siteEnable")
    public String enableSite(@RequestParam("id") Long id) {
        Site site = siteRepository.findOne(id);
        site.setEnabled(true);
        site.setState(SiteState.WARNING);
        siteRepository.save(site);
        return "redirect:/existingSites";
    }

    @RequestMapping("/acknowledgeSite")
    public String acknowledgeSite(@RequestParam("id") Long id) {
        Site site = siteRepository.findOne(id);
        site.setAcknowledged(true);
        site.setState(SiteState.ACKNOWLEDGED);
        siteRepository.save(site);
        return "redirect:/existingSites";
    }

    @RequestMapping("/unacknowledgeSite")
    public String unAcknowledgeSite(@RequestParam("id") Long id){
        Site site = siteRepository.findOne(id);
        site.setAcknowledged(false);
        site.setState(SiteState.WARNING);
        siteRepository.save(site);
        return "redirect:/existingSites";
    }


}