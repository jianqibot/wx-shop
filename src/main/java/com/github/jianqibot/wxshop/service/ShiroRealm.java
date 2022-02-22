package com.github.jianqibot.wxshop.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@SuppressFBWarnings("EI_EXPOSE_REP2")
@Service
public class    ShiroRealm extends AuthorizingRealm {
    private final VerificationCodeCheckService codeCheckService;

    @Inject
    public ShiroRealm(VerificationCodeCheckService codeCheckService) {
        this.codeCheckService = codeCheckService;
        this.setCredentialsMatcher((token, info) -> new String((char[]) token.getCredentials()).equals(info.getCredentials()));
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String tel = (String) authenticationToken.getPrincipal();
        String correctCode = codeCheckService.getCorrectCode(tel);
        return new SimpleAuthenticationInfo(tel, correctCode, getName());
    }
}
