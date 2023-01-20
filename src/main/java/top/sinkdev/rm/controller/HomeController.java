package top.sinkdev.rm.controller;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.sinkdev.rm.common.SimpleResponse;
import top.sinkdev.rm.common.entity.Resident;
import top.sinkdev.rm.common.entity.User;
import top.sinkdev.rm.common.util.DataValidator;
import top.sinkdev.rm.service.HomeService;

import java.util.Date;
import java.util.Map;

@Slf4j
@Controller
public class HomeController {

    private final HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping("/home")
    public String getHome(Model model, HttpSession session) {
        var curUser = (User) session.getAttribute("cur_user");
        var simpleResponse = homeService.getResidentList(curUser.getUid());
        if (simpleResponse.isSuccessful()) {
            model.addAttribute("residents", simpleResponse.getData());
        } else {
            model.addAttribute("errorMessage", simpleResponse.getMessage());
        }
        return "home";
    }


    @PostMapping("/add")
    public String postAdd(Model model, HttpSession session, @RequestParam Map<String, Object> req) {
        log.info("add req = {}", req);
        // verify req data
        String idCN = (String) req.get("idCN");
        if (DataValidator.illegalIDCN(idCN)) {
            model.addAttribute("message", "请输入正确的身份证号！");
            return "add";
        }
        String name = (String) req.get("name");
        if (DataValidator.illegalName(name)) {
            model.addAttribute("message", "请输入正确的姓名！");
            return "add";
        }
        String gender = (String) req.get("gender");
        if (DataValidator.illegalGender(gender)) {
            model.addAttribute("message", "请输入正确的性别！");
            return "add";
        }
        String birthday = (String) req.get("birthday");
        Date birthdayDate = DataValidator.convert2Date(birthday);
        if (birthdayDate == null) {
            model.addAttribute("message", "请输入正确的出生日期！");
            return "add";
        }
        String province = (String) req.get("province");
        if (DataValidator.illegalCityName(province)) {
            model.addAttribute("message", "请输入正确的省份！");
            return "add";
        }
        String city = (String) req.get("city");
        if (DataValidator.illegalCityName(city)) {
            model.addAttribute("message", "请输入正确的城市！");
            return "add";
        }
        String county = (String) req.get("county");
        if (DataValidator.illegalCityName(county)) {
            model.addAttribute("message", "请输入正确的县/区！");
            return "add";
        }
        String address = (String) req.get("address");
        if (DataValidator.illegalAddress(address)) {
            model.addAttribute("message", "请输入正确的地址！");
            return "add";
        }
        String phone = (String) req.get("phone");
        if (DataValidator.illegalPhoneNumber(phone)) {
            model.addAttribute("message", "请输入正确的手机！");
            return "add";
        }
        Resident resident = new Resident(null, idCN, Encode.forHtml(name), gender, birthdayDate,
                Encode.forHtml(province), Encode.forHtml(city), Encode.forHtml(county), Encode.forHtml(address), phone, null);
        // get current user
        var curUser = (User) session.getAttribute("cur_user");
        // add resident
        SimpleResponse<Resident> simpleResponse = homeService.addResident(resident, curUser.getUid());
        // whether successful or not
        model.addAttribute("message", simpleResponse.getMessage());
        return "add";
    }

    @GetMapping("/add")
    public String getAdd() {
        return "add";
    }

    @PostMapping("/modify/{idCN}")
    public String postModify(Model model, HttpSession session, @RequestParam Map<String, Object> req, @PathVariable("idCN") String idCN) {
        log.info("modify req = {}", req);
        if (DataValidator.illegalIDCN(idCN)) {
            return "redirect:/home";
        }
        // verify req data
        String name = (String) req.get("name");
        String gender = (String) req.get("gender");
        String birthday = (String) req.get("birthday");
        String phone = (String) req.get("phone");
        String city = (String) req.get("city");
        String county = (String) req.get("county");
        String province = (String) req.get("province");
        String address = (String) req.get("address");
        Date birthdayDate = DataValidator.convert2Date(birthday);

        Resident resident = new Resident(null, idCN, Encode.forHtml(name), gender, birthdayDate,
                Encode.forHtml(province), Encode.forHtml(city), Encode.forHtml(county), Encode.forHtml(address), phone, null);
        model.addAttribute("resident", resident);
        if (DataValidator.illegalIDCN(idCN)) {
            model.addAttribute("message", "请输入正确的身份证号！");
            return "modify";
        }
        if (DataValidator.illegalName(name)) {
            model.addAttribute("message", "请输入正确的姓名！");
            return "modify";
        }
        if (DataValidator.illegalGender(gender)) {
            model.addAttribute("message", "请输入正确的性别！");
            return "modify";
        }
        if (birthdayDate == null) {
            model.addAttribute("message", "请输入正确的出生日期！");
            return "modify";
        }
        if (DataValidator.illegalCityName(province)) {
            model.addAttribute("message", "请输入正确的省份！");
            return "modify";
        }
        if (DataValidator.illegalCityName(city)) {
            model.addAttribute("message", "请输入正确的城市！");
            return "modify";
        }
        if (DataValidator.illegalCityName(county)) {
            model.addAttribute("message", "请输入正确的县/区！");
            return "modify";
        }
        if (DataValidator.illegalAddress(address)) {
            model.addAttribute("message", "请输入正确的地址！");
            return "modify";
        }
        if (DataValidator.illegalPhoneNumber(phone)) {
            model.addAttribute("message", "请输入正确的手机！");
            return "modify";
        }

        var curUser = (User) session.getAttribute("cur_user");
        // update
        SimpleResponse<Resident> simpleResponse = homeService.updateResident(resident, curUser.getUid());
        if (simpleResponse.isSuccessful()) {
            model.addAttribute("resident", resident);
        }
        model.addAttribute("message", simpleResponse.getMessage());
        return "modify";
    }

    @GetMapping("/modify/{idCN}")
    public String getModify(Model model, HttpSession session, @PathVariable("idCN") String idCN) {
        if (DataValidator.illegalIDCN(idCN)) {
            return "redirect:/home";
        }
        var curUser = (User) session.getAttribute("cur_user");
        SimpleResponse<Resident> simpleResponse = homeService.getResident(idCN, curUser.getUid());
        if (!simpleResponse.isSuccessful()) {
            return "redirect:/home";
        }
        model.addAttribute("resident", simpleResponse.getData());
        return "modify";
    }

    @GetMapping("/delete/{idCN}")
    public String getDelete(Model model, HttpSession session, @PathVariable("idCN") String idCN) {
        if (DataValidator.illegalIDCN(idCN)) {
            return "redirect:/home";
        }
        var curUser = (User) session.getAttribute("cur_user");
        SimpleResponse<Boolean> simpleResponse = homeService.deleteResident(idCN, curUser.getUid());
        if (!simpleResponse.isSuccessful()) {
            model.addAttribute("errorMessage", simpleResponse.getMessage());
            return "home";
        }
        return "redirect:/home";
    }
}
