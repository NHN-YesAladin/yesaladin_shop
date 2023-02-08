package shop.yesaladin.shop.common.aspect.advice;

import java.lang.reflect.Parameter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import shop.yesaladin.common.code.ErrorCode;
import shop.yesaladin.common.exception.ClientException;
import shop.yesaladin.shop.common.aspect.annotation.LoginId;

@Aspect
@Component
public class LoginIdAspect {

    @Pointcut("execution(* *(.., @shop.yesaladin.shop.common.aspect.annotation.LoginId (*), ..)))")
    public void loginIdPointcut() {

    }

    @Around("loginIdPointcut()")
    public Object extractLoginId(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] methodParams = signature.getMethod().getParameters();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        for (int i = 0; i < args.length; i++) {
            if (methodParams[i].isAnnotationPresent(LoginId.class)) {
                args[i] = getLoginId(methodParams[i], authentication);
                break;
            }
        }

        return joinPoint.proceed(args);
    }

    private String getLoginId(
            Parameter methodParam, Authentication authentication
    ) {
        boolean isAnonymous = authentication instanceof AnonymousAuthenticationToken;
        if (methodParam.getAnnotation(LoginId.class).required() && isAnonymous) {
            throw new ClientException(ErrorCode.UNAUTHORIZED, "Unauthorized request");
        }
        if (isAnonymous) {
            return null;
        }
        return authentication.getName();
    }
}
