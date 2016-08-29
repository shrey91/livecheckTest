package com.liverton.livecheck.web.controller;

import com.liverton.livecheck.dao.model.*;
import com.liverton.livecheck.dao.repository.*;
import com.liverton.livecheck.model.NotificationAction;
import com.liverton.livecheck.model.SiteState;
import com.liverton.livecheck.service.*;
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
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    SitePingResultRepository sitePingResultRepository;

    @Autowired
    private SitePingResultService sitePingResultService;

    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name", required = false, defaultValue = "XYZ") String name, Model model) {
        model.addAttribute("name", name);
        model.addAttribute("sites", service.findSites());
        model.addAttribute("users", userService.findUsers());
        model.addAttribute("authority", authorityService.findRoles());
        model.addAttribute("organisations", organisationService.findByOrgName());
        model.addAttribute("sitestate");
        model.addAttribute("sitepingresult", sitePingResultService.findByDate());
        return "hello";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
//        securityService.autologin("username","password");
        return "login";
    }
//    @RequestMapping(value="/login", method = RequestMethod.POST)
//    public String login(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
//        userValidator.validate(userForm, bindingResult);
//        if(bindingResult.hasErrors()){
//            return "login";
//        }
//
//        securityService.autologin(userForm.getName(),userForm.getPassword());
//        return "redirect/welcome";
//    }

    @RequestMapping("welcome")
    public String welcomeForm() {
        return "welcome";
    }


    @RequestMapping(value = "/newsite", method = RequestMethod.GET)
    public String newSite(Model model) {
        SiteModelForm sitemodel = new SiteModelForm();
        List<Organisation> organisations = organisationService.getAllOrganisations();
        model.addAttribute("organisations", organisations);
        model.addAttribute("siteModelForm", sitemodel);
        return "newsite";
    }

    @RequestMapping(value = "/newsite", method = RequestMethod.POST)
    public String newSite(@ModelAttribute @Valid SiteModelForm siteModelForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Site Form contains errors - {}", bindingResult.getAllErrors());
            return "newSite";
        } else {
            LOGGER.info("Validation successful - {}", siteModelForm);

            Organisation organisation = organisationRepository.findOne(siteModelForm.getOrganisation().getId());

            NotificationAction notificationAction = null;
            if (notificationAction == null) {
                siteModelForm.setAction(NotificationAction.EMAIL);
                siteModelForm.setAverageResponse("0ms");
                siteModelForm.setState(SiteState.OKAY);
            }


            Site save = siteRepository.save(new Site(siteModelForm.getSiteName(),
                    siteModelForm.getEnabled(),
                    siteModelForm.getIpAddress(),
                    new Date(),
                    siteModelForm.getState(),
                    false,
                    siteModelForm.getAction(),
                    0,
                    siteModelForm.getAverageResponse(),
                    false,
                    false,
                    false,
                    siteModelForm.getOrganisation()));
            model.addAttribute("newSite", save);
            return "redirect:/displaySite";
        }
    }

    @RequestMapping(value = "/displaySite", method = RequestMethod.GET)
    public String displayNewSite(@ModelAttribute("newSite") Site newSite, Model model) {
        model.addAttribute("newSite", newSite);
        return "submitform";
    }

    @RequestMapping("/existingsites")
    public String existingSites(Model model) {
        model.addAttribute("existingSite", siteRepository.findAll());
        return "existingsites";
    }

    @RequestMapping("/deleteSite")
    public String deleteSite(@RequestParam("id") Long id) {
        siteRepository.delete(id);
        return "redirect:/existingsites";
    }

    @RequestMapping("/editsite")
    public String editSite(@RequestParam("id") Long id, Model model) {
        Site site = siteRepository.findOne(id);
        model.addAttribute("siteModelForm", new EditSiteModelForm(site.getSiteName(), site.getEnabled(), site.getIpAddress(), site.getState(), site.getAverageResponse(), site.getId()));
        model.addAttribute("siteState", site.getState());
        return "editsite";

    }


    @RequestMapping("/viewpingresult")
    public String viewPingResult(@RequestParam("id") Long id, Model model) {
        model.addAttribute("site", siteRepository.findOne(id));
        return "viewpingresult";

    }

    @RequestMapping("/vieworganisationsites")
    public String viewOrganisationSites(@RequestParam("id") Long id, Model model) {
        model.addAttribute("site", organisationRepository.findOne(id));
        return "vieworganisationsites";

    }


    @RequestMapping(value = "/editsite", method = RequestMethod.POST)
    public String editSite(@Valid EditSiteModelForm editSiteModelForm, BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Item not found -{}", bindingResult.getAllErrors());
            return "editsite";
        } else {
            Site site = siteRepository.findOne(editSiteModelForm.getId());
            site.setDate(new Date());
            site.setEnabled(editSiteModelForm.getEnabled());
            site.setIpAddress(editSiteModelForm.getIpAddress());
            site.setSiteName(editSiteModelForm.getSiteName());
            site.setState(editSiteModelForm.getState());

            siteRepository.save(site);
//            redirectAttributes.addFlashAttribute("message");
//            return "redirect:/editsite?id=" + site.getId();
            return "redirect:/existingsites";
        }
    }

    @RequestMapping("users")
    public String users(Model model) {
        model.addAttribute("existingUser", userRepository.findAll());
        return "users";
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.GET)
    public String newUser(Model model) {
        User user = new User();
        List<Authority> authorities = authorityService.findRoles();
        model.addAttribute("authorities", authorities);
        model.addAttribute("userForm", new UserForm());
        return "newuser";
    }

    @RequestMapping(value = "/newuser", method = RequestMethod.POST)
    public String newUser(@ModelAttribute @Valid UserForm userForm, BindingResult bindingResult, Model model) {
//        userValidator.validate(userForm, bindingResult);
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

    @RequestMapping("/edituser")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userRepository.findOne(id);
        model.addAttribute("editUserForm", new EditUserForm(user.getName(), user.getPassword(), user.getUserEnabled(), user.getId()));
        return "edituser";

    }

    @RequestMapping(value = "/edituser", method = RequestMethod.POST)
    public String editSite(@Valid EditUserForm editUserForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("Item not found -{}", bindingResult.getAllErrors());
            return "edituser";
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
        return "submituser";
    }

    @RequestMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userRepository.delete(id);
        return "redirect:/users";
    }

    @RequestMapping("organisations")
    public String organisations(Model model) {
        model.addAttribute("existingOrganisations", organisationRepository.findAll());
        return "organisations";
    }

    @RequestMapping(value = "/newOrganisation", method = RequestMethod.GET)
    public String newOrganisation(Model model) {
        model.addAttribute("organisationForm", new OrganisationForm());
        return "newOrganisation";
    }

    @RequestMapping(value = "/newOrganisation", method = RequestMethod.POST)
    public String newUser(@ModelAttribute @Valid OrganisationForm organisationForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            LOGGER.warn("User Form contains errors - {}", bindingResult.getAllErrors());
            return "newSite";
        } else {
            LOGGER.info("Validation successful - {}", organisationForm);
            Organisation save3 = organisationRepository.save(new Organisation(organisationForm.getOrgName(), organisationForm.getDescription()));
            model.addAttribute("newSite", save3);
            return "redirect:/displayOrg";
        }
    }

    @RequestMapping(value = "/displayOrg", method = RequestMethod.GET)
    public String displayOrg(@ModelAttribute("newSite") Organisation newSite, Model model) {
        model.addAttribute("newSite", newSite);
        return "submitOrg";
    }

    @RequestMapping("/deleteOrg")
    public String deleteOrg(@RequestParam("id") Long id) {
        organisationRepository.delete(id);
        return "redirect:/organisations";
    }

    @RequestMapping("/editOrg")
    public String editOrg(@RequestParam("id") Long id, Model model) {
        Organisation organisation = organisationRepository.findOne(id);
        model.addAttribute("organisationForm", new EditOrganisationForm(organisation.getOrgName(), organisation.getDescription(), organisation.getId()));
        model.addAttribute("sites", organisation.getSites());
        return "editOrg";

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

            organisationRepository.save(organisation);
            return "redirect:/organisations";
        }
    }

    @RequestMapping("authorities")
    public String authorities(Model model) {
        model.addAttribute("existingAuth", authorityRepository.findAll());
        return "authorities";
    }

    @RequestMapping(value = "/addAuth", method = RequestMethod.GET)
    public String newAuth(Model model) {
        model.addAttribute("authorityForm", new AuthorityForm());
        return "addAuth";
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
        return "submitAuth";
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
        return "editAuth";

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

    @RequestMapping("/sitemonitor")
    public String monitorSites(Model model) {
        model.addAttribute("siteMonitor", siteRepository.findAll());
        return "sitemonitor";
    }

    @RequestMapping("/sitedisable")
    public String disableSite(@RequestParam("id") Long id) {
        Site site = siteRepository.findOne(id);
        site.setEnabled(false);
        site.setState(SiteState.DISABLED);
        siteRepository.save(site);
        return "redirect:/existingsites";
    }
    @RequestMapping("/siteenable")
    public String enableSite(@RequestParam("id") Long id) {
        Site site = siteRepository.findOne(id);
        site.setEnabled(true);
        site.setState(SiteState.WARNING);
        siteRepository.save(site);
        return "redirect:/existingsites";
    }

    @RequestMapping("/acknowledgesite")
    public String acknowledgeSite(@RequestParam("id") Long id) {
        Site site = siteRepository.findOne(id);
        site.setAcknowledged(true);
        siteRepository.save(site);
        return "redirect:/existingsites";
    }
}