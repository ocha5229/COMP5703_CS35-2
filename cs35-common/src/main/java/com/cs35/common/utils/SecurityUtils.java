package com.cs35.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.PatternMatchUtils;
import com.cs35.common.constant.Constants;
import com.cs35.common.constant.HttpStatus;
import com.cs35.common.core.domain.entity.SysRole;
import com.cs35.common.core.domain.model.LoginUser;
import com.cs35.common.exception.ServiceException;

/**
 * 
 *
 * @author cs35
 */
public class SecurityUtils {

    /**
     * ID
     **/
    public static Long getUserId() {
        try {
            return getLoginUser().getUserId();
        } catch (Exception e) {
            throw new ServiceException("Exception in obtaining user ID", HttpStatus.UNAUTHORIZED);
        }
    }


    /**
     * 
     **/
    public static String getUsername() {
        try {
            return getLoginUser().getUsername();
        } catch (Exception e) {
            throw new ServiceException("Exception in obtaining user account", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 
     **/
    public static LoginUser getLoginUser() {
        try {
            return (LoginUser) getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new ServiceException("Exception in obtaining user information", HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * BCryptPasswordEncoder
     *
     * @param password 
     * @return 
     */
    public static String encryptPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    /**
     * 
     *
     * @param rawPassword     
     * @param encodedPassword 
     * @return 
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 
     *
     * @param userId ID
     * @return 
     */
    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

    /**
     * 
     *
     * @param permission 
     * @return 
     */
    public static boolean hasPermi(String permission) {
        return hasPermi(getLoginUser().getPermissions(), permission);
    }

    /**
     * 
     *
     * @param authorities 
     * @param permission  
     * @return 
     */
    public static boolean hasPermi(Collection<String> authorities, String permission) {
        return authorities.stream().filter(StringUtils::hasText)
                .anyMatch(x -> Constants.ALL_PERMISSION.equals(x) || PatternMatchUtils.simpleMatch(x, permission));
    }

    /**
     * 
     *
     * @param role 
     * @return 
     */
    public static boolean hasRole(String role) {
        List<SysRole> roleList = getLoginUser().getUser().getRoles();
        Collection<String> roles = roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
        return hasRole(roles, role);
    }

    /**
     * 
     *
     * @param roles 
     * @param role  
     * @return 
     */
    public static boolean hasRole(Collection<String> roles, String role) {
        return roles.stream().filter(StringUtils::hasText)
                .anyMatch(x -> Constants.SUPER_ADMIN.equals(x) || PatternMatchUtils.simpleMatch(x, role));
    }

}
