package top.sinkdev.rm.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.google.common.hash.Hashing;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.sinkdev.rm.common.SimpleResponse;
import top.sinkdev.rm.common.entity.User;
import top.sinkdev.rm.common.util.DataValidator;
import top.sinkdev.rm.service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Slf4j
@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/captcha")
    public void captcha(HttpSession session, HttpServletResponse response) throws IOException {
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 100);
        session.setAttribute("code", lineCaptcha.getCode());
        response.setContentType("image/png");
        response.getOutputStream().write(lineCaptcha.getImageBytes());
        response.getOutputStream().close();
    }

    @PostMapping("/login")
    public String postLogin(Model model, HttpSession session, @RequestParam Map<String, Object> req) {
        log.info("login req = {}", req);

        var email = (String) req.get("email");
        var password = (String) req.get("password");
        if (DataValidator.illegalEmail(email) || DataValidator.illegalEmailPassword(password)) {
            model.addAttribute("errorMessage", "请输入正确的邮箱和密码！");
            return "login";
        }
        // pre-proc password, sha256
        password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        // call userService, to verify email and password
        SimpleResponse<User> userSimpleResponse = userService.login(email, password);
        if (userSimpleResponse.isSuccessful()) {
            session.setAttribute("cur_user", userSimpleResponse.getData());
            // redirect to home page
            return "redirect:home";
        }
        model.addAttribute("errorMessage", userSimpleResponse.getMessage());
        return "login";
    }

    @PostMapping("/register")
    public String postRegister(Model model, HttpSession session, @RequestParam Map<String, Object> req) {
        log.info("register req = {}", req);
        // verify captcha
        String captcha = (String) req.get("captcha");
        if (captcha == null || !captcha.equalsIgnoreCase((String) session.getAttribute("code"))) {
            model.addAttribute("message", "验证码错误！");
            return "register";
        }
        // check data validity
        var email = (String) req.get("email");
        var password = (String) req.get("password");
        var gender = (String) req.get("gender");
        var username = (String) req.get("username");
        // email
        if (DataValidator.illegalEmail(email)) {
            model.addAttribute("message", "请输入正确的邮箱！");
            return "register";
        }
        // password
        if (DataValidator.illegalEmailPassword(password)) {
            model.addAttribute("message", "请输入正确的密码！");
            return "register";
        }
        // gender
        if (DataValidator.illegalGender(gender)) {
            model.addAttribute("message", "请输入正确的性别！");
            return "register";
        }
        // username
        if (DataValidator.illegalName(username)) {
            model.addAttribute("message", "请输入正确的用户名！");
            return "register";
        }
        // set user information
        User user = new User();
        user.setEmail(email);
        user.setGender(gender);
        // anti-xss
        user.setUsername(Encode.forHtml(username));
        user.setPassword(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString());
        SimpleResponse<User> userSimpleResponse = userService.register(user);
        // success || failure
        model.addAttribute("message", userSimpleResponse.getMessage());
        return "register";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }


}
