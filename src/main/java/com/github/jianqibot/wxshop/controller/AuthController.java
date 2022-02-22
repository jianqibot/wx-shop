package com.github.jianqibot.wxshop.controller;

import com.github.jianqibot.wxshop.entity.LoginResponse;
import com.github.jianqibot.wxshop.entity.TelAndCode;
import com.github.jianqibot.wxshop.service.AuthService;
import com.github.jianqibot.wxshop.service.TelVerificationService;
import com.github.jianqibot.wxshop.service.UserContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final AuthService authService;
    private final TelVerificationService telVerificationService;

    @Inject
    public AuthController(AuthService authService,
                          TelVerificationService telVerificationService) {
        this.authService = authService;
        this.telVerificationService = telVerificationService;
    }

    /**
     * @api {post} /code Request Verification Code
     * @apiName GetVerificationCode
     * @apiGroup Login & Authentication
     *
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam {String} tel user mobile number.
     * @apiParamExample {json} Request-Example:
     *     {
     *         "tel": "13812345678",
     *     }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 Bad Request
     *     {
     *         "message": "Bad Request"
     *     }
     */

    /**
     * @param telAndCode tel number & verification code
     * @param response HTTP response
     */
    @PostMapping("/code")
    public void code(@RequestBody TelAndCode telAndCode,
                     HttpServletResponse response) {
        if (telVerificationService.verifyTelParam(telAndCode)) {
            authService.sendVerificationCode(telAndCode.getTel());
        } else {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
        }
    }


    /**
     * @api {post} /login login
     * @apiName Login
     * @apiGroup Login & Authentication
     *
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     *
     * @apiParam {String} tel tel number
     * @apiParam {String} code verification code
     * @apiParamExample {json} Request-Example:
     *     {
     *         "tel": "13812345678",
     *         "code": "000000"
     *     }
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *
     * @apiError 400 Bad Request error in request
     * @apiError 403 Forbidden wrong verification code
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 Bad Request
     *     {
     *       "message": "Bad Request"
     *     }
     */

    /**
     * @param telAndCode tel number & verification code
     * @param response response
     */
    @PostMapping("/login")
    public void login(@RequestBody TelAndCode telAndCode,
                      HttpServletResponse response) {
        UsernamePasswordToken token = new UsernamePasswordToken(
                telAndCode.getTel(),
                telAndCode.getCode());
        token.setRememberMe(true);

        try {
            SecurityUtils.getSubject().login(token);
        } catch (IncorrectCredentialsException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }


    /**
     * @api {post} /logout logout
     * @apiName Logout
     * @apiGroup Login & Authentication
     *
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *
     * @apiError 401 Unauthorized user not login
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 400 Bad Request
     *     {
     *         "message": "Bad Request"
     *     }
     */
    @PostMapping("/logout")
    public void logout() {
        SecurityUtils.getSubject().logout();
        SecurityUtils.getSubject();
    }


    /**
     * @api {get} /status get login status
     * @apiName Status
     * @apiGroup Login & Authentication
     *
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     *
     * @apiSuccess {User} user user info
     * @apiSuccess {Boolean} login login status
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *         "login": true,
     *         "user": {
     *             "id": 123,
     *             "name": "David",
     *             "tel": "13812345678",
     *             "avatarUrl": "https://url",
     *             "address": "12 Rose St LA",
     *         }
     *     }
     *
     * @apiError 401 Unauthorized user not login
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *         "message": "Unauthorized"
     *     }
     */

    /**
     *
     * @return login response
     */
    @GetMapping("/status")
    public LoginResponse loginStatus() {
        if (UserContext.getCurrentUser() != null) {
            return LoginResponse.alreadyLoginResponse(UserContext.getCurrentUser());
        } else {
            return LoginResponse.notLoginResponse();
        }
    }
}
