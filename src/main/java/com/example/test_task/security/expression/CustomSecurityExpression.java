package com.example.test_task.security.expression;

import com.example.test_task.model.enums.RoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {

    public boolean canEdit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return hasAnyRole(authentication, RoleEnum.ROLE_EDITOR);
    }

    private boolean hasAnyRole(Authentication authentication, RoleEnum... roles) {
        for (RoleEnum roleEnum : roles) {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(roleEnum.name());
            if (authentication.getAuthorities().contains(authority)) {
                return true;
            }
        }
        return false;
    }
}